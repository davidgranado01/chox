package idas.chox.web;

import idas.chox.core.model.ClaimType;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 *
 * @author John
 */
public class ClaimTypeConverter extends StrutsTypeConverter{

    ClaimType values[] = ClaimType.values();

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values != null && values.length > 0 && values[0] != null && values[0].length() > 0) {
            return ClaimType.values()[Integer.valueOf(values[0])];

        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if ( o instanceof ClaimType){
            return ((ClaimType)o).toString();
            //return Integer.toString(((LiabilityStatus)o).ordinal());
        }
        return "";
    }

}
