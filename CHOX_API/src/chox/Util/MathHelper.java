package chox.Util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathHelper {

    public static BigDecimal getPercentage(float iValue, float iDevider) {
        float oValue = 0;
        if(iValue>0 && iDevider>0){
            oValue = iValue / iDevider;
        }
        return new BigDecimal(oValue);
    }
    
    public static Integer getIntegerValue(Object v) {
        if (v.getClass().equals(Integer.class)) {
            return (Integer) v;
        } else if (v.getClass().equals(BigInteger.class)) {
            return ((BigInteger) v).intValue();
        } else {
            return 0;
        }
    }    
}
