package idas.chox.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DateHelper.class);
    public static final int SUBSCRIBER_SLA_DAYS = 5;
    public static final int FIXED_FEE_SLA_DAYS = 14;

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
        c1.add(Calendar.DAY_OF_MONTH, dayIntever);

        return c1.getTime();
    }

    public static Date getCurrentDate() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    public static String getCurrentDateInString() {
        Calendar cal = Calendar.getInstance();
        return getSdf().format(cal.getTime());
    }

    public static String getCurrentDateWithFormat(String sFotmat) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(sFotmat);
        return sdf.format(cal.getTime());
    }

    public static boolean isBefore3pm() {
        return isBefore3pm(new Date(), 0);
    }

    public static boolean isBefore3pm(int leeway) {
        return isBefore3pm(new Date(), leeway);
    }

    public static boolean isBefore3pm(Date date, int leeway) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if (leeway != 0) {
            cal.add(Calendar.MINUTE, -leeway);
        }
        return cal.get(Calendar.HOUR_OF_DAY) < 15 ? true : false;
    }

    public static String getTwoDigitValueInString(int iValue) {
        String returnValue = String.valueOf(iValue);
        if (iValue < 10) {
            returnValue = "0" + returnValue;
        }
        return returnValue;
    }

    public static long getNumberOf24HourPeriodsBetween(Date startDate, Date endDate) {
        long milliseconds1 = startDate.getTime();
        long milliseconds2 = endDate.getTime();
        long diff = milliseconds2 - milliseconds1;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        return diffDays;
    }

    public static int getNumberOfDaysBetween(Date startDate, Date endDate) {
        // Determine no days claim was in status
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(startDate);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(endDate);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        Long days = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (24 * 60 * 60 * 1000);

        return days.intValue();
    }

    public static Date mergeTimeToDate(Date a, Date b) {
        a.setHours(b.getHours());
        a.setMinutes(b.getMinutes());
        return a;
    }

    //format: dd/MM/yyyy
    public static Date parse(String source) {
        return parse(source, getLocalDateFormat());
    }

    public static Date parseDateTime(String source) {
        return parse(source, getLocalDateTimeFormat());
    }

    public static Date parseDBDateTime(String source) {
        return parse(source, getDBDateTimeFormat());
    }

    public static Date parse(String source, String format) {

        return parse(source, new SimpleDateFormat(format));
    }

    public static Date parse(String source, SimpleDateFormat dateFormat) {

        if ((source.trim()).length() > 0 && source != null && !source.equalsIgnoreCase("")) {

            try {
                return dateFormat.parse(source);
            } catch (ParseException ex) {
                LOG.warn("Unable to parse date: '{}'", source);
//                ex.printStackTrace();
                return null;
            }

        }

        return null;
    }

    public static boolean dateCompare(Date date1, Date date2) {
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

    public static Date setEndOfDay(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The argument 'date' cannot be null.");
        }
        LOG.debug("Adding time to date: {}", date.toString());

        // Get an instance of the Calendar.
        Calendar calendar = Calendar.getInstance();

        // Make sure the calendar will not perform automatic correction.
        calendar.setLenient(false);

        // Set the time of the calendar to the given date.
        calendar.setTime(date);

        // Remove the hours, minutes, seconds and milliseconds.
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
//        calendar.set(Calendar.MILLISECOND, 999);

        LOG.debug("Time from date: {} is {}", date.toString(), calendar.getTime().toString());

        // Return the date again.
        return calendar.getTime();
    }

    public static Date setStartOfDay(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The argument 'date' cannot be null.");
        }
        LOG.debug("Adding time to date: {}", date.toString());

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

    public static Date removeTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The argument 'date' cannot be null.");
        }
        LOG.debug("Removing time from date: {}", date.toString());

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

    public static double differenceInMonths(Date date1, Date date2) {
        return differenceInYears(date1, date2) * 12;
    }

    public static double differenceInYears(Date date1, Date date2) {
        double days = differenceInDays(date1, date2);
        return days / 365.0;
    }

    public static double differenceInDays(Date date1, Date date2) {
        return differenceInHours(date1, date2) / 24.0;
    }

    public static double differenceInHours(Date date1, Date date2) {
        return differenceInMinutes(date1, date2) / 60.0;
    }

    public static double differenceInMinutes(Date date1, Date date2) {
        return differenceInSeconds(date1, date2) / 60.0;
    }

    public static double differenceInSeconds(Date date1, Date date2) {
        return differenceInMilliseconds(date1, date2) / 1000.0;
    }

    private static double differenceInMilliseconds(Date date1, Date date2) {
        return Math.abs(getTimeInMilliseconds(date1) - getTimeInMilliseconds(date2));
    }

    private static long getTimeInMilliseconds(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getTimeInMillis() + cal.getTimeZone().getOffset(cal.getTimeInMillis());
    }

    /**
     * @return the sdf
     */
    public static SimpleDateFormat getSdf() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * @return the LocalDateFormat
     */
    public static SimpleDateFormat getLocalDateFormat() {
        return new SimpleDateFormat("dd/MM/yyyy");
    }

    /**
     * @return the DBDateFormat
     */
    public static SimpleDateFormat getDBDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * @return the DBDateTimeFormat
     */
    public static SimpleDateFormat getDBDateTimeFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * @return the TimeFormat
     */
    public static SimpleDateFormat getTimeFormat() {
        return new SimpleDateFormat("kk:mm");
    }

    /**
     * @return the LocalDateTimeFormat
     */
    public static SimpleDateFormat getLocalDateTimeFormat() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm");
    }

    public static SimpleDateFormat getLocalDateTimeFormatWithSecs() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    /**
     * @return the EXTDateTimeFormat
     */
    public static SimpleDateFormat getEXTDateTimeFormat() {
        return new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss");
    }

    /**
     * @return the EXTDateFormat
     */
    public static SimpleDateFormat getEXTDateFormat() {
        return new SimpleDateFormat("dd MMM yyyy");
    }
}

