package chox.Util;

import java.math.BigDecimal;

public class MathHelper {

    public static BigDecimal getPercentage(float iValue, float iDevider) {
        float oValue = 0;
        if(iValue>0 && iDevider>0){
            oValue = iValue / iDevider;
        }
        return new BigDecimal(oValue);
    }  
}
