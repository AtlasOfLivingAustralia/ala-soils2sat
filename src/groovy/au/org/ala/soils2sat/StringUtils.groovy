/*
 * ﻿Copyright (C) 2013 Atlas of Living Australia
 * All Rights Reserved.
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 */

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
