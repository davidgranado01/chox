package chox.Util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat LocalDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat DBDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat TimeFormat = new SimpleDateFormat("kk:mm");
    
    public static java.sql.Timestamp getCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(sdf.format(cal.getTime()));
    }

    public static String getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }
    
    public static String getCurrentDateWithFormat(String sFotmat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(sFotmat);
        return sdf.format(cal.getTime());
    }
    
    public static String getTwoDigitValueInString(int iValue) {
        String returnValue = String.valueOf(iValue);
        if (iValue < 10) {
            returnValue = "0" + returnValue;
        }
        return returnValue;
    }

    public static long daysBetween(Date startDate, Date endDate) {
        long milliseconds1 = startDate.getTime();
        long milliseconds2 = endDate.getTime();
        long diff = milliseconds2 - milliseconds1;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays;
    }
    
    public static Date mergeTimeToDate(Date a,Date b)
    {
        a.setHours(b.getHours());
        a.setMinutes(b.getMinutes());
        return a;
    }

    public static Date getFirstDateOfTheMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.DATE, -dayOfMonth);
        return cal.getTime();
    }

    public static Date getFirstDateOfTheWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer dayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, -dayOfMonth);
        return cal.getTime();
    }

    public static Date getMinDate() {
        Date a = new Date();
        try {
            DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
            a = dfm.parse("1900-01-01");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return a;

    }

    public static Date getMaxDate() {
        Date a = new Date();
        try {
            DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
            a = dfm.parse("2999-12-31");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return a;

    }
}
