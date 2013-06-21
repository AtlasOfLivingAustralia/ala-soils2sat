package au.org.ala.soils2sat

import org.apache.cxf.jaxrs.ext.search.client.CompleteCondition
import org.apache.cxf.jaxrs.ext.search.client.PartialCondition
import org.apache.cxf.jaxrs.ext.search.client.SearchConditionBuilder

import java.text.SimpleDateFormat
import java.util.regex.Pattern

class SearchCriteriaUtils {

    public static CriteriaTranslator newDoubleEvaluator(String pattern) {
        return new DoubleCriteriaTranslator(pattern)
    }

    public static String format(SearchCriteria criteria, Closure<String> valueFormatter = null) {
        CriteriaTranslator evaluator = factory(criteria)
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

    public static CriteriaTranslator factory(SearchCriteria criteria) {
        switch (criteria.criteriaDefinition.valueType) {
            case CriteriaValueType.NumberRangeDouble:
                return new DoubleCriteriaTranslator(criteria.value)
                break
            case CriteriaValueType.NumberRangeInteger:
                return new IntegerCriteriaTranslator(criteria.value)
                break
            case CriteriaValueType.StringDirectEntry:
            case CriteriaValueType.StringMultiSelect:
                return new MultiStringPatternTranslator(criteria.value)
                break
            case CriteriaValueType.StringSingleSelect:
                return new StringPatternTranslator(criteria.value)
                break
            case CriteriaValueType.DateRange:
                return new DateRangeCriteriaTranslator(criteria.value)
                break
            case CriteriaValueType.Boolean:
                return new BooleanCriteriaTranslator(criteria.value)
                break
            default:
                throw new RuntimeException("Unhandled value type: " + criteria.criteriaDefinition.valueType)
        }
    }

    public static class DateRangeCriteriaTranslator implements CriteriaTranslator {

        public static Pattern DatePattern = Pattern.compile("^(gt|lt)\\s(\\d{1,2}/\\d{1,2}/\\d\\d\\d\\d)\$")
        public static Pattern DateRangePattern = Pattern.compile("^(bt)\\s(\\d{1,2}/\\d{1,2}/\\d\\d\\d\\d)[:](\\d{1,2}/\\d{1,2}/\\d\\d\\d\\d)\$")

        def _sdf = new SimpleDateFormat("dd/MM/yyyy")
        def _sdf2 = new SimpleDateFormat("MMM dd, yyyy")

        Date startDate
        Date endDate
        String operator

        public DateRangeCriteriaTranslator(String pattern) {
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

        String displayString(Closure<String> valueFormatter) {
            switch (operator) {
                case "lt":
                    return "is before " + valueFormatter(_sdf.format(startDate))
                    break
                case "gt":
                    return "is after " + valueFormatter(_sdf.format(startDate))
                    break
                case "bt":
                    return "is between " + valueFormatter(_sdf.format(startDate)) + " and " + valueFormatter(_sdf.format(endDate))
                    break
            }
        }

        CompleteCondition createFIQLCondition(SearchCriteria criteria, PartialCondition partial) {
            def field = criteria.criteriaDefinition.fieldName
            switch (operator) {
                case "lt":
                    return partial.is(field).before(startDate)
                    break
                case "gt":
                    return partial.is(field).after(startDate)
                    break
                case "bt":
                    return partial.is(field).after(startDate).and(field).before(endDate)
                    break
            }
            return null
        }
    }

    public static class MultiStringPatternTranslator implements CriteriaTranslator {
        String[] values

        public MultiStringPatternTranslator(String value) {
            this.values = value?.split("\\|")
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

        CompleteCondition createFIQLCondition(SearchCriteria criteria, PartialCondition partial) {
            SearchConditionBuilder b = SearchConditionBuilder.instance()
            def list = []
            def field = criteria.criteriaDefinition.fieldName
            values.each {
                list <<  b.is(field).equalTo(it)
            }
            return partial.or(list)
        }

    }

    public static class StringPatternTranslator implements CriteriaTranslator {

        private String valueToMatch

        public StringPatternTranslator(String valueToMatch) {
            this.valueToMatch = valueToMatch
        }

        public String displayString(Closure<String> formatValue) {
            return "matches ${formatValue(valueToMatch)}"
        }

        CompleteCondition createFIQLCondition(SearchCriteria criteria, PartialCondition partial) {
            def field = criteria.criteriaDefinition.fieldName
            return partial.is(field).equalTo(valueToMatch)
        }

    }

    public static class DoubleCriteriaTranslator implements CriteriaTranslator {

        public static Pattern DoublePattern = Pattern.compile("^(gt|lt)\\s([-]{0,1}\\d+[\\.]{0,1}\\d*)\$")
        public static Pattern DoubleRangePattern = Pattern.compile("^(bt)\\s([-]{0,1}\\d+[\\.]{0,1}\\d*)[:]([-]{0,1}\\d+[\\.]{0,1}\\d*)\$")

        String operator
        Double value1
        Double value2

        public DoubleCriteriaTranslator(String pattern) {
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

        CompleteCondition createFIQLCondition(SearchCriteria criteria, PartialCondition partial) {
            def field = criteria.criteriaDefinition.fieldName

            switch (operator) {
                case "lt":
                    return partial.is(field).lessOrEqualTo(value1)
                    break
                case "gt":
                    return partial.is(field).greaterOrEqualTo(value1)
                    break
                case "bt":
                    return partial.is(field).greaterOrEqualTo(value1).and(field).lessOrEqualTo(value2)
                    break
            }
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

    public static class IntegerCriteriaTranslator implements CriteriaTranslator {

        public static Pattern IntegerPattern = Pattern.compile("^(gt|lt)\\s([-]{0,1}\\d+)\$")
        public static Pattern IntegerRangePattern = Pattern.compile("^(bt)\\s([-]{0,1}\\d+)[:]([-]{0,1}\\d+)\$")

        String operator
        Integer value1
        Integer value2

        public IntegerCriteriaTranslator(String pattern) {
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

        CompleteCondition createFIQLCondition(SearchCriteria criteria, PartialCondition partial) {
            def field = criteria.criteriaDefinition.fieldName
            switch (operator) {
                case "lt":
                    return partial.is(field).lessOrEqualTo(value1)
                    break
                case "gt":
                    return partial.is(field).greaterOrEqualTo(value1)
                    break
                case "bt":
                    return partial.is(field).greaterOrEqualTo(value1).and(field).lessOrEqualTo(value2)
                    break
            }
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

    public static class BooleanCriteriaTranslator implements CriteriaTranslator {

        Boolean value

        public BooleanCriteriaTranslator(String pattern) {
            value = Boolean.parseBoolean(pattern)
        }

        public String displayString(Closure<String> formatValue) {
            if (value) {
                return formatValue("Yes")
            }
            return formatValue("No")
        }

        CompleteCondition createFIQLCondition(SearchCriteria criteria, PartialCondition partial) {
            def field = criteria.criteriaDefinition.fieldName
            return partial.is(field).equalTo(value?.toString())
        }

    }


}

public interface CriteriaTranslator {

    public String displayString(Closure<String> valueFormatter)
    public CompleteCondition createFIQLCondition(SearchCriteria criteria, PartialCondition condition)

}



