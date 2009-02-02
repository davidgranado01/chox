package chox.Util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
    public static SimpleDateFormat LocalDateFormat = new SimpleDateFormat("dd/MM/yyyy");  
    public static SimpleDateFormat DBDateFormat = new SimpleDateFormat("yyyy-MM-dd");  
    
    public static java.sql.Timestamp getCurrentTimeStamp() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return Timestamp.valueOf(sdf.format(cal.getTime()));
    }
    
    public static String getTwoDigitValueInString(int iValue){
        String returnValue = String.valueOf(iValue);
        if(iValue<10){
            returnValue = "0"+returnValue;
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
    
    public static Date getFirstDateOfTheMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.DATE, -dayOfMonth);
        return cal.getTime();
    }
    
    public static Date getFirstDateOfTheWeek(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Integer dayOfMonth = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, -dayOfMonth);
        return cal.getTime();
    }
    
    public static Date getMinDate()
    {
        return null;
    }
}
