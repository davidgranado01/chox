package idas.chox.service.bre.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.util.DateHelper;
import java.math.BigDecimal;
import java.util.Date;

public class CalcHelper {
    private static final Logger LOG = LoggerFactory.getLogger(CalcHelper.class);

    public static boolean EqualTo(BigDecimal a, BigDecimal b) {

        boolean bFlag = false;

        double cx = Math.ceil(a.doubleValue());
        double cy = Math.ceil(b.doubleValue());

        double fx = Math.floor(a.doubleValue());
        double fy = Math.floor(b.doubleValue());

        if((cx==cy) || (fx==fy)){
            bFlag = true;
        }

        return bFlag;

    }

    public static boolean LessThanOrEqualTo(BigDecimal a, BigDecimal b) {

        boolean bFlag = false;

        double cx = Math.ceil(a.doubleValue());
        double cy = Math.ceil(b.doubleValue());

        double fx = Math.floor(a.doubleValue());
        double fy = Math.floor(b.doubleValue());

        if((cx<=cy) || (fx<=fy)){
            bFlag = true;
        }

        return bFlag;
    }

    private static final long MILISECONDS_PER_DAY = 24 * 60 * 60 * 1000;

    public static int getDaysBetweenDates(Date startDate, Date endDate) {
        LOG.debug("getDaysBetweenDates {} and {}", startDate.toString(), endDate.toString());
        // Mantis Id 0000912
        long diff = DateHelper.removeTime(endDate).getTime() - DateHelper.removeTime(startDate).getTime();
        // long diff = endDate.getTime() - startDate.getTime();
        LOG.debug("Differnece is {} miliseconds", diff);
        int days = (int) Math.floor(diff / MILISECONDS_PER_DAY);
        LOG.debug("Returning {} days", Math.abs(days));
        return Math.abs(days); 
    }

    public static final BigDecimal VAT_RATE = new BigDecimal(".175");
}
