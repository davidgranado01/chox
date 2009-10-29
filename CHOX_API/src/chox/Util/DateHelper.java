package chox.Util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateHelper {
    
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat LocalDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat DBDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat TimeFormat = new SimpleDateFormat("kk:mm");
    public static SimpleDateFormat GridViewDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    public static java.sql.Timestamp getCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat csdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(csdf.format(cal.getTime()));
    }
    
    public static int getYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    public static int getMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH)+1;
    }

     public static int getDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

      public static int getDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    
    public static Date addMonth(Date date, int monthIntever) {
         Calendar c1 = Calendar.getInstance();

        c1.setTime(date);
        c1.add(Calendar.MONTH, monthIntever);

        return c1.getTime();
    }

    public static Date addDay(Date date, int dayIntever) {
        Calendar c1 = Calendar.getInstance();

        c1.setTime(date);
        c1.add(Calendar.DATE, dayIntever);

        return c1.getTime();
    }

     public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }
    
    public static String getCurrentDateInString() {
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

    //format: dd/MM/yyyy
    public static Date Parse(String source)
    {
        try {
            return LocalDateFormat.parse(source);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static boolean DateCompare(Date date1, Date date2)
    {
        boolean bFlag = false;

        try {

            if(date1.compareTo(date2) == 0){
                bFlag = true;
            }

        } catch (Exception ex) {
            bFlag = false;
        }

        return bFlag;
    }
}
