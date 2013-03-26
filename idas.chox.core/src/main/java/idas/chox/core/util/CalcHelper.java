package idas.chox.core.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalcHelper {

    private static final Logger LOG = LoggerFactory.getLogger(CalcHelper.class);

    public static boolean equalTo(BigDecimal a, BigDecimal b) {

        boolean bFlag = false;

        double cx = Math.ceil(a.doubleValue());
        double cy = Math.ceil(b.doubleValue());

        double fx = Math.floor(a.doubleValue());
        double fy = Math.floor(b.doubleValue());

        if ((cx == cy) || (fx == fy)) {
            bFlag = true;
        }

        return bFlag;

    }

    public static boolean lessThanOrEqualTo(BigDecimal a, BigDecimal b) {

        boolean bFlag = false;

        double cx = Math.ceil(a.doubleValue());
        double cy = Math.ceil(b.doubleValue());

        double fx = Math.floor(a.doubleValue());
        double fy = Math.floor(b.doubleValue());

        if ((cx <= cy) || (fx <= fy)) {
            bFlag = true;
        }

        return bFlag;
    }
    private static final long MILISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

    public static int getDaysBetweenDates(Date startDate, Date endDate) {
        LOG.debug("getDaysBetweenDates {} and {}", startDate, endDate);
        if (startDate == null || endDate == null) {
            LOG.warn("Cannot calculate difference between {} and {} as one is null - returning 0", startDate, endDate);
            return 0;
        }
        // Mantis Id 0000912
        long diff = DateHelper.removeTime(endDate).getTime() - DateHelper.removeTime(startDate).getTime();
        // long diff = endDate.getTime() - startDate.getTime();
        diff += 60 * 60 * 1000; // We'll add an hour, just to get over daylight saving time problems
        LOG.debug("Difference is {} miliseconds", diff);
        int days = (int) Math.floor(diff / MILISECONDS_PER_DAY);
        LOG.debug("Returning {} days", Math.abs(days));
        return Math.abs(days);
    }

    class VatRateS {

        String startDate;
        String rate;

        VatRateS(String startDate, String rate) {
            this.startDate = startDate;
            this.rate = rate;
        }
    }

    class VatRate {

        Date startDate;
        BigDecimal rate;

        VatRate(Date startDate, BigDecimal rate) {
            this.startDate = startDate;
            this.rate = rate;
        }
    }

    public static BigDecimal getVatRate(Date date) {
        BigDecimal vatRate = BigDecimal.ZERO;

        if (date == null) {
            LOG.warn("No date supplied: returning default VAT rate of {}%", VAT_RATE.multiply(new BigDecimal("100.0")));
            return VAT_RATE;
        }
        Calendar jan2010 = new GregorianCalendar(2010, Calendar.JANUARY, 1);
        Calendar jan2011 = new GregorianCalendar(2011, Calendar.JANUARY, 4);
        Calendar myCal = new GregorianCalendar();
        myCal.setTime(date);

        if (myCal.before(jan2010)) {
            vatRate = new BigDecimal(".15");
        } else if (myCal.after(jan2010) && myCal.before(jan2011)) {
            vatRate = new BigDecimal(".175");
        } else if (myCal.after(jan2011) || myCal.equals(jan2011)) {
            vatRate = new BigDecimal(".20");
        } else {
            LOG.warn("Cannot determine VAT rate for date '{}'", date);
        }

        LOG.debug("Returning VAT rate of {} for date '{}'", vatRate, date);
        return vatRate;
    }

    public static BigDecimal getInsurancePremiumVatRate(Date date) {
        BigDecimal insurancePremiumvatRate = BigDecimal.ZERO;

        if (date == null) {
            LOG.warn("No date supplied: returning default VAT rate of {}%", INSURANCE_PREMIUM_VAT_RATE.multiply(new BigDecimal("100.0")));
            return INSURANCE_PREMIUM_VAT_RATE;
        }
        Calendar jan2010 = new GregorianCalendar(2010, Calendar.JANUARY, 1);
        Calendar jan2011 = new GregorianCalendar(2011, Calendar.JANUARY, 4);
        Calendar myCal = new GregorianCalendar();
        myCal.setTime(date);

        if (myCal.before(jan2010)) {
            insurancePremiumvatRate = new BigDecimal(".06");
        } else if (myCal.after(jan2010) && myCal.before(jan2011)) {
            insurancePremiumvatRate = new BigDecimal(".06");
        } else if (myCal.after(jan2011) || myCal.equals(jan2011)) {
            insurancePremiumvatRate = new BigDecimal(".06");
        } else {
            LOG.warn("Cannot determine VAT rate for date '{}'", date);
        }

        LOG.debug("Returning VAT rate of {} for date '{}'", insurancePremiumvatRate, date);
        return insurancePremiumvatRate;
    }
    public static final BigDecimal VAT_RATE = new BigDecimal(".200");
    public static final BigDecimal INSURANCE_PREMIUM_VAT_RATE = new BigDecimal(".060");
}
