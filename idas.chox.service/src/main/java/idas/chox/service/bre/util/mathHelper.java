package idas.chox.service.bre.util;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class mathHelper {

    public static int getIntegerFromDecimalRound(BigDecimal bValue){
        int oValue = 0;
        oValue = bValue.setScale(0, RoundingMode.HALF_UP).intValue();
        return oValue;
    }
    
    public static int getIntegerFromDecimalRoundUp(BigDecimal bValue){
        int oValue = 0;
        oValue = bValue.setScale(0, RoundingMode.UP).intValue();
        return oValue;
    }
    
    public static BigDecimal getNotNullDecimalValue(BigDecimal bValue){
        BigDecimal oValue = new BigDecimal(0.00);
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
