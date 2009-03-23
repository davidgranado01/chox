package chox.Util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class MathHelper {

    public static BigDecimal getPercentage(float iValue, float iDevider) {
        float oValue = 0;
        if(iValue>0 && iDevider>0){
            oValue = iValue / iDevider;
        }
        return new BigDecimal(oValue);
    }
    
    public static String getExcelDisplayPerc(BigDecimal iValue){
        String sValue = "N/A";
        if(iValue!=null){
            iValue = iValue.multiply(new BigDecimal(100));
            sValue = iValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString()+"%";
        }
        return sValue;
    }
    
    public static BigDecimal getPercentageBigDecimal(float iValue, float iDevider, int decimalplace){
        BigDecimal bValue = new BigDecimal(iValue*100);
        BigDecimal bDevider = new BigDecimal(iDevider);
        BigDecimal oValue = bValue.divide(bDevider, decimalplace, BigDecimal.ROUND_HALF_UP);
        return oValue;
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
   
    public static BigDecimal Round(BigDecimal iValue, int decimalPlace){
        iValue.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return iValue;
    }
}
