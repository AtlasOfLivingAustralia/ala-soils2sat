package au.org.ala.soils2sat

import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: baird
 * Date: 16/01/13
 * Time: 9:08 AM
 * To change this template use File | Settings | File Templates.
 */
class DateUtils {

    public static String S2S_DATE_FORMAT = "yyyy-MM-dd"

    public static Date tryParse(dateStr) {
        if (dateStr) {
            def sdf = new SimpleDateFormat(S2S_DATE_FORMAT)
            try {
                def date = sdf.parse(dateStr)
                return date
            } catch (ParseException ex) {
                return null
            }
        }
        return null
    }
}
