package idas.chox.core.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DateHelper.class);

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat LocalDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static SimpleDateFormat DBDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat DBDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat TimeFormat = new SimpleDateFormat("kk:mm");
    public static SimpleDateFormat LocalDateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static Date getCurrentDateTime() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static int getDay(Date date) {
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

    public static Date mergeTimeToDate(Date a, Date b) {
        a.setHours(b.getHours());
        a.setMinutes(b.getMinutes());
        return a;
    }

    //format: dd/MM/yyyy
    public static Date Parse(String source) {
        return Parse(source, LocalDateFormat);
    }

    public static Date ParseDateTime(String source) {
         return Parse(source, LocalDateTimeFormat);
    }

    public static Date ParseDBDateTime(String source) {
        return Parse(source, DBDateTimeFormat);
    }

    public static Date Parse(String source, String format) {

        return Parse(source, new SimpleDateFormat(format));
    }

    public static Date Parse(String source, SimpleDateFormat dateFormat) {

        if ((source.trim()).length() > 0 && source != null && !source.equalsIgnoreCase("")) {

            try {
                return dateFormat.parse(source);
            } catch (ParseException ex) {
                ex.printStackTrace();
                return null;
            }

        }

        return null;
    }

    public static boolean DateCompare(Date date1, Date date2) {
        boolean bFlag = false;

        try {

            if (date1.compareTo(date2) == 0) {
                bFlag = true;
            }

        } catch (Exception ex) {
            bFlag = false;
        }

        return bFlag;
    }

    public static int getDayOfWeek(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);

    }

    public static Date removeTime(Date date) {
    LOG.debug("Removing time from date: {}", date.toString());
    if(date == null) {
      throw new IllegalArgumentException("The argument 'date' cannot be null.");
    }

    // Get an instance of the Calendar.
    Calendar calendar = Calendar.getInstance();

    // Make sure the calendar will not perform automatic correction.
    calendar.setLenient(false);

    // Set the time of the calendar to the given date.
    calendar.setTime(date);

    // Remove the hours, minutes, seconds and milliseconds.
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    LOG.debug("Time from date: {} is {}", date.toString(), calendar.getTime().toString());

    // Return the date again.
    return calendar.getTime();
  }
}
