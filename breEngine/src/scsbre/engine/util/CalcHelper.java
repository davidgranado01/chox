package scsbre.engine.util;

import java.math.BigDecimal;
import java.util.Date;

public class CalcHelper {

    public static boolean EqualTo(BigDecimal a, BigDecimal b) {
        
        //a.
        //return Math.round(Math.floor(a.doubleValue())) == Math.round(Math.floor(b.doubleValue()));
        
        double x = Math.round(a.doubleValue());
        double y = Math.round(b.doubleValue());
        
        return x == y;
        
        
        
        //return true;
        
        
    }
    private static final long MILISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

    public static int getDaysBetweenDates(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        int days = (int) Math.floor(diff / MILISECONDS_PER_DAY);
        return Math.abs(days); //add an extra day to factor "part thereof" rule
    }
    
    //TODO
    // Mantis Id 0000255
    // Updated by Carlson @ 20081211
    // public static final BigDecimal VAT_RATE = new BigDecimal(.175);
    public static final BigDecimal VAT_RATE = new BigDecimal(.15);
}
