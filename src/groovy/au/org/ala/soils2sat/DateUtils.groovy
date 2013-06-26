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

    public static final String S2S_DATE_FORMAT = "yyyy-MM-dd"
    public static final String S2S_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    public static final String W3CDTF_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";


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

    public static boolean isSameDay(Date date1, Date date2) {
        return (date1..date2).size() == 1
    }

    public static String getW3CDTFDate(Date date) {
        if (!date) {
            return ''
        }

        String str = new SimpleDateFormat( W3CDTF_FORMAT ).format(date);
        return "${str.substring(0, str.length() - 2)}:${str.substring(str.length() - 2)}";
    }

}
