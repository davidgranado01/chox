package idas.chox.service.bre.util;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MathHelper {

    public static int getIntegerFromDecimalRound(BigDecimal bValue){
        return bValue.setScale(0, RoundingMode.HALF_UP).intValue();
    }
    
    public static int getIntegerFromDecimalRoundUp(BigDecimal bValue){
        return bValue.setScale(0, RoundingMode.UP).intValue();
    }
    
    public static BigDecimal getNotNullDecimalValue(BigDecimal bValue){
        BigDecimal oValue = BigDecimal.ZERO;
        if(bValue!=null){
            oValue = bValue;
        }
        return oValue;
    }
    
    public static int getNotNullIntValue(Integer bValue){
        Integer oValue = 0;
        if(bValue!=null){
            oValue = bValue;
        }
        return oValue;
    }    
}
