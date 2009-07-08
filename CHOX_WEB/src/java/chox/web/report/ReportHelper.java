package chox.web.report;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ReportHelper{
    
    public static Integer getIntegerValue(Object v) {
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            return ((BigInteger) v).intValue();
        } else {
            return 0;
        }
    }
    
    public static BigDecimal getBigDecimalValue(Object v) {
        return (BigDecimal) v;
    }    
}
