package idas.chox.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathHelper {
    private static final Logger LOG = LoggerFactory.getLogger(MathHelper.class);

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
        
        BigDecimal oValue = new BigDecimal("0.00");
        
        if(iDevider > 0 && iValue>0){
            BigDecimal bValue = new BigDecimal(iValue*100);
            BigDecimal bDevider = new BigDecimal(iDevider);
            oValue = bValue.divide(bDevider, decimalplace, BigDecimal.ROUND_HALF_UP);
        }
        
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
   
    public static BigDecimal round(BigDecimal iValue, int decimalPlace){
        iValue.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return iValue;
    }
    
    public static BigDecimal divide(Integer iInput, Integer iDevider){
        BigDecimal bOutput = new BigDecimal(0.00);
        
        if(iInput>0 && iDevider>0){
//            bOutput = new BigDecimal(iInput.floatValue() / iDevider.floatValue());
            bOutput = new BigDecimal(iInput).divide(new BigDecimal(iDevider), 4, RoundingMode.HALF_UP);
        }

        LOG.debug("Dividing {} by {} - result is " + bOutput.toString(), iInput, iDevider);
        return bOutput;
    }
}
