package idas.chox.web;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import idas.chox.core.model.LiabilityStatus;

/**
 *
 * @author abrar
 */
public class LiabilityStatusConverter extends StrutsTypeConverter{

    LiabilityStatus values[] = LiabilityStatus.values();

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values != null && values.length > 0 && values[0] != null && values[0].length() > 0) {
            return LiabilityStatus.values()[Integer.valueOf(values[0])];

        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if ( o instanceof LiabilityStatus){
            return ((LiabilityStatus)o).toString();
        }
        return "";
    }

}
