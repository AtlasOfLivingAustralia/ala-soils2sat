package au.org.ala.soils2sat

class StringUtils {

    public static String collapseSpaces(String argStr) {
        if (argStr == null || argStr.length() == 0) {
            return argStr;
        }

        char last = argStr.charAt(0);
        StringBuilder argBuf = new StringBuilder();

        for (int cIdx = 0 ; cIdx < argStr.length(); cIdx++) {
            char ch = argStr.charAt(cIdx);
            if (ch != ' ' || last != ' ') {
                argBuf.append(ch);
                last = ch;
            }
        }

        return argBuf.toString();
    }

    public static String firstNumber(String str) {
        if (str == null || str.length() == 0) {
            return str
        }

        boolean seenDot = false;
        def sb = new StringBuilder()
        for (char ch : str) {
            if (Character.isDigit(ch) || (!seenDot && ch == '.')) {
                sb.append(ch)
                if (ch == '.') {
                    seenDot = true
                }
            } else {
                break;
            }

        }
        return sb.toString()
    }

    public static String makeTitleFromCamelCase(String str) {
        if (!str) {
            return str
        }

        StringBuilder b = new StringBuilder(str.charAt(0).toUpperCase().toString())
        for (int i = 1; i < str.length(); ++i) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                // insert a word breaking space
                b << ' '
            }
            b << ch
        }

        return b.toString()
    }

}
