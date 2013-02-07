package au.org.ala.soils2sat

import java.util.regex.Pattern

class SearchCriteriaUtils {

    public static Pattern DoublePattern = Pattern.compile("^(gt|lt)\\s(\\d+[\\.]{0,1}\\d*)\$")
    public static Pattern IntegerPattern = Pattern.compile("^(gt|lt)\\s(\\d+)\$")

    public static CriteriaEvaluator newDoubleEvaluator(String pattern) {
        return new DoublePatternEvaluator(pattern)
    }

    public static boolean eval(SearchCriteria criteria, String value) {
        CriteriaEvaluator evaluator = factory(criteria)
        if (evaluator) {
            return evaluator.evaluate(value)
        }
        return false;
    }

    public static String format(SearchCriteria criteria, String valueDelimter = "") {
        CriteriaEvaluator evaluator = factory(criteria)
        if (evaluator) {
            return evaluator.displayString(valueDelimter);
        }
        return criteria.value
    }

    private static CriteriaEvaluator factory(SearchCriteria criteria) {
        switch (criteria.criteriaDefinition.valueType) {
            case CriteriaValueType.NumberRangeDouble:
                return new DoublePatternEvaluator(criteria.value)
            case CriteriaValueType.StringDirectEntry:
            case CriteriaValueType.StringMultiSelect:
                return new MultiStringPatternEvaluator(criteria.value)
                break;
            case CriteriaValueType.StringSingleSelect:
                return new StringPatternEvaluator(criteria.value)
                break;
            default:
                throw new RuntimeException("Unhandled value type: " + criteria.criteriaDefinition.valueType)
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

        String displayString(String valueDelimiter) {
            if (values) {
                def sb = new StringBuilder()
                if (values.size() == 1) {
                    sb << "matches "
                    if (valueDelimiter) {
                        sb << "<" << valueDelimiter << ">"
                    }
                    sb << values[0]
                    if (valueDelimiter) {
                        sb << "</" << valueDelimiter << ">"
                    }
                } else {
                    sb << "is one of "
                    if (valueDelimiter) {
                        sb << "<" << valueDelimiter << ">"
                    }
                    sb << values[0..values.size() - 2].join(", ")
                    if (valueDelimiter) {
                        sb << "</" << valueDelimiter << ">"
                    }
                    sb << " or "
                    if (valueDelimiter) {
                        sb << "<" << valueDelimiter << ">"
                    }
                    sb << values[values.size()-1]
                    if (valueDelimiter) {
                        sb << "</" << valueDelimiter << ">"
                    }
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

        public String displayString(String valueDelimiter) {
            def sb = new StringBuilder()
            sb << "matches "
            if (valueDelimiter) {
                sb << "<" << valueDelimiter << ">"
            }
            sb << value
            if (valueDelimiter) {
                sb << "</" << valueDelimiter << ">"
            }
            return sb.toString()
        }

    }

    public static class DoublePatternEvaluator implements CriteriaEvaluator {

        String operator
        Double value

        public DoublePatternEvaluator(String pattern) {
            def m = DoublePattern.matcher(pattern)
            if (!m.matches()) {
                throw new RuntimeException("Invalid criteria pattern: " + pattern)
            }

            operator = m.group(1)
            value = Double.parseDouble(m.group(2))
        }

        public boolean evaluate(String testValue) {
            Double val = Double.parseDouble(testValue)
            switch (operator) {
                case "lt":
                    return val <= value
                    break;
                case "gt":
                    return val >= value
                    break;
            }
        }

        public String displayString(String valueDelimiter) {
            def sb = new StringBuilder()
            switch (operator) {
                case "lt":
                    sb << "is less than "
                    break;
                case "gt":
                    sb << "is greater than "
                    break;
            }
            if (valueDelimiter) {
                sb << "<" << valueDelimiter << ">"
            }
            sb << value
            if (valueDelimiter) {
                sb << "</" << valueDelimiter << ">"
            }
            return sb.toString()
        }

    }

}

public interface CriteriaEvaluator {

    public boolean evaluate(String value)
    public String displayString(String valueDelimiter)

}

