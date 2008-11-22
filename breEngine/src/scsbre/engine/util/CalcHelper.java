package scsbre.engine.util;

import java.math.BigDecimal;
import java.util.Date;

public class CalcHelper {

    public static boolean EqualTo(BigDecimal a, BigDecimal b) {
        return Math.floor(a.doubleValue()) == Math.floor(b.doubleValue());
    }
    private static final long MILISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

    public static int getDaysBetweenDates(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        int days = (int) Math.floor(diff / MILISECONDS_PER_DAY);
        return Math.abs(days); //add an extra day to factor "part thereof" rule
    }
    
    public static final BigDecimal VAT_RATE = new BigDecimal(.175);
}
