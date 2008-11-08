package chox.services;

import java.sql.Timestamp;
import java.util.Calendar;

public class XmlHelper {
//TODO: LEARN REGULER EXPRESSION
    public static Boolean isValidDataType(String dataValue, String dataType, String regExpression){
        Boolean bFlag = dataValue.matches(regExpression);
        //return bFlag;
        return true;
    }
}
