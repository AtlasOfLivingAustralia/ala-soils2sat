package au.org.ala.soils2sat

import java.text.SimpleDateFormat
import java.util.regex.Pattern

class SearchCriteriaUtils {

    public static CriteriaEvaluator newDoubleEvaluator(String pattern) {
        return new DoubleCriteriaEvaluator(pattern)
    }

    public static boolean eval(SearchCriteria criteria, String value) {
        CriteriaEvaluator evaluator = factory(criteria)
        if (evaluator) {
            return evaluator.evaluate(value)
        }
        return false
    }

    public static String format(SearchCriteria criteria, Closure<String> valueFormatter = null) {
        CriteriaEvaluator evaluator = factory(criteria)
        if (evaluator) {
            if (!valueFormatter) {
                valueFormatter = { it }
            }
            def output = evaluator.displayString(valueFormatter)
            if (criteria.displayUnits) {
                output += " (" + criteria.displayUnits + ")"
            }
            return output
        }
        return criteria.value
    }

    private static CriteriaEvaluator factory(SearchCriteria criteria) {
        switch (criteria.criteriaDefinition.valueType) {
            case CriteriaValueType.NumberRangeDouble:
                return new DoubleCriteriaEvaluator(criteria.value)
                break
            case CriteriaValueType.NumberRangeInteger:
                return new IntegerCriteriaEvaluator(criteria.value)
                break
            case CriteriaValueType.StringDirectEntry:
            case CriteriaValueType.StringMultiSelect:
                return new MultiStringPatternEvaluator(criteria.value)
                break
            case CriteriaValueType.StringSingleSelect:
                return new StringPatternEvaluator(criteria.value)
                break
            case CriteriaValueType.DateRange:
                return new DateRangeCriteriaEvaluator(criteria.value)
                break
            case CriteriaValueType.Boolean:
                return new BooleanCriteriaEvaluator(criteria.value)
                break
            default:
                throw new RuntimeException("Unhandled value type: " + criteria.criteriaDefinition.valueType)
        }
    }

    public static class DateRangeCriteriaEvaluator implements CriteriaEvaluator {

        public static Pattern DatePattern = Pattern.compile("^(gt|lt)\\s(\\d{1,2}/\\d{1,2}/\\d\\d\\d\\d)\$")
        public static Pattern DateRangePattern = Pattern.compile("^(bt)\\s(\\d{1,2}/\\d{1,2}/\\d\\d\\d\\d)[:](\\d{1,2}/\\d{1,2}/\\d\\d\\d\\d)\$")

        def _sdf = new SimpleDateFormat("dd/MM/yyyy")
        def _sdf2 = new SimpleDateFormat("MMM dd, yyyy")

        Date startDate
        Date endDate
        String operator

        public DateRangeCriteriaEvaluator(String pattern) {
            def m = DatePattern.matcher(pattern)
            if (m.matches()) {
                operator = m.group(1)
                startDate = _sdf.parse(m.group(2))
            } else {
                m = DateRangePattern.matcher(pattern)
                if (m.matches()) {
                    operator = m.group(1)
                    def date1 = _sdf.parse(m.group(2))
                    def date2 = _sdf.parse(m.group(3))
                    startDate = [date1, date2].min()
                    endDate = [date1, date2].max()
                } else {
                    throw new RuntimeException("Unrecognized number range criteria format: " + pattern)
                }
            }

        }

        boolean evaluate(String value) {
            Date date = null
            try {
                date = _sdf.parse(value)
            } catch (Exception ex) {
                date = _sdf2.parse(value)
            }

            switch (operator) {
                case "lt":
                    return date <= startDate
                    break
                case "gt":
                    return date >= startDate
                    break
                case "bt":
                    return date >= startDate && date <= endDate
                    break
            }
        }

        String displayString(Closure<String> valueFormatter) {
            switch (operator) {
                case "lt":
                    return "is on or before " + valueFormatter(_sdf.format(startDate))
                    break
                case "gt":
                    return "is on or after " + valueFormatter(_sdf.format(startDate))
                    break
                case "bt":
                    return "is between " + valueFormatter(_sdf.format(startDate)) + " and " + valueFormatter(_sdf.format(endDate))
                    break
            }
        }
    }

    public static class MultiStringPatternEvaluator implements CriteriaEvaluator {
        String[] values

        public MultiStringPatternEvaluator(String value) {
            this.values = value?.split("\\|")
        }

        boolean evaluate(String value) {
            for (String candidate : values) {
                if (candidate.equalsIgnoreCase(value)) {
                    return true
                }
            }
            return false
        }

        String displayString(Closure<String> formatValue) {
            if (values) {
                def sb = new StringBuilder()
                if (values.size() == 1) {
                    sb << "matches " + formatValue(values[0])
                } else {
                    sb << "is one of "
                    def formattedValues = values.collect { formatValue(it) }
                    sb << formattedValues[0..formattedValues.size() - 2].join(", ")
                    sb << " or "
                    sb << formattedValues[formattedValues.size()-1]
                }
                return sb.toString()
            }
            return "[Empty list!]"
        }
    }

    public static class StringPatternEvaluator implements CriteriaEvaluator {

        private String valueToMatch

        public StringPatternEvaluator(String valueToMatch) {
            this.valueToMatch = valueToMatch
        }

        public boolean evaluate(String testValue) {
            return valueToMatch.equalsIgnoreCase(testValue)
        }

        public String displayString(Closure<String> formatValue) {
            return "matches ${formatValue(valueToMatch)}"
        }

    }

    public static class DoubleCriteriaEvaluator implements CriteriaEvaluator {

        public static Pattern DoublePattern = Pattern.compile("^(gt|lt)\\s([-]{0,1}\\d+[\\.]{0,1}\\d*)\$")
        public static Pattern DoubleRangePattern = Pattern.compile("^(bt)\\s([-]{0,1}\\d+[\\.]{0,1}\\d*)[:]([-]{0,1}\\d+[\\.]{0,1}\\d*)\$")

        String operator
        Double value1
        Double value2

        public DoubleCriteriaEvaluator(String pattern) {
            def m = DoublePattern.matcher(pattern)
            if (m.matches()) {
                operator = m.group(1)
                value1 = Double.parseDouble(m.group(2))
            } else {
                m = DoubleRangePattern.matcher(pattern)
                if (m.matches()) {
                    operator = m.group(1)
                    def num1 = Double.parseDouble(m.group(2))
                    def num2 = Double.parseDouble(m.group(3))
                    value1 = Math.min(num1, num2)
                    value2 = Math.max(num1, num2)
                } else {
                    throw new RuntimeException("Unrecognized number range criteria format: " + pattern)
                }
            }

        }

        public boolean evaluate(String testValue) {
            Double val = Double.parseDouble(testValue)
            switch (operator) {
                case "lt":
                    return val <= value1
                    break
                case "gt":
                    return val >= value1
                    break
                case "bt":
                    return val >= value1 && val <= value2
                    break
            }
            return false
        }

        public String displayString(Closure<String> formatValue) {
            switch (operator) {
                case "lt":
                    return "is less or equal to " + formatValue(value1)
                    break
                case "gt":
                    return "is greater or equal to " + formatValue(value1)
                    break
                case "bt":
                    return "is between ${formatValue(value1)} and ${formatValue(value2)}"
                    break
            }
            return "???"
        }

    }

    public static class IntegerCriteriaEvaluator implements CriteriaEvaluator {

        public static Pattern IntegerPattern = Pattern.compile("^(gt|lt)\\s([-]{0,1}\\d+)\$")
        public static Pattern IntegerRangePattern = Pattern.compile("^(bt)\\s([-]{0,1}\\d+)[:]([-]{0,1}\\d+)\$")

        String operator
        Integer value1
        Integer value2

        public IntegerCriteriaEvaluator(String pattern) {
            def m = IntegerPattern.matcher(pattern)
            if (m.matches()) {
                operator = m.group(1)
                value1 = Integer.parseInt(m.group(2))
            } else {
                m = IntegerRangePattern.matcher(pattern)
                if (m.matches()) {
                    operator = m.group(1)
                    def num1 = Integer.parseInt(m.group(2))
                    def num2 = Integer.parseInt(m.group(3))
                    value1 = Math.min(num1, num2)
                    value2 = Math.max(num1, num2)
                } else {
                    throw new RuntimeException("Unrecognized number range criteria format: " + pattern)
                }
            }

        }

        public boolean evaluate(String testValue) {
            Integer val = Integer.parseInt(testValue)
            switch (operator) {
                case "lt":
                    return val <= value1
                    break
                case "gt":
                    return val >= value1
                    break
                case "bt":
                    return val >= value1 && val <= value2
                    break
            }
            return false
        }

        public String displayString(Closure<String> formatValue) {
            switch (operator) {
                case "lt":
                    return "is less or equal to " + formatValue(value1)
                    break
                case "gt":
                    return "is greater or equal to " + formatValue(value1)
                    break
                case "bt":
                    return "is between ${formatValue(value1)} and ${formatValue(value2)}"
                    break
            }
            return "???"
        }

    }

    public static class BooleanCriteriaEvaluator implements CriteriaEvaluator {

        Boolean value

        public BooleanCriteriaEvaluator(String pattern) {
            value = Boolean.parseBoolean(pattern)
        }

        public boolean evaluate(String testValue) {
            def other = Boolean.parseBoolean(testValue)
            return other == value
        }

        public String displayString(Closure<String> formatValue) {
            if (value) {
                return formatValue("Yes")
            }
            return formatValue("No")
        }

    }


}

public interface CriteriaEvaluator {

    public boolean evaluate(String value)
    public String displayString(Closure<String> valueFormatter)

}


