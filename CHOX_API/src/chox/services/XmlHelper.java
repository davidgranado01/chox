package chox.services;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.regex.*;

public class XmlHelper {
//TODO: LEARN REGULER EXPRESSION
    public static Boolean isValidDataType(String dataValue, String regExpression){
        Boolean bFlag = true;
        
        if(!regExpression.equalsIgnoreCase("")){

            Pattern p = Pattern.compile(regExpression);
            Matcher m = p.matcher(dataValue);
            
            if(!m.find()){
                bFlag = false;
            }

        }
        
        return bFlag;
    }
}
