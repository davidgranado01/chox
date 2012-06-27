package idas.chox.web;

import idas.chox.core.model.InsurerDiscountType;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;

/**
 *
 * @author John
 */
public class InsurerDiscountTypeConverter extends StrutsTypeConverter{

    InsurerDiscountType values[] = InsurerDiscountType.values();

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values != null && values.length > 0 && values[0] != null && values[0].length() > 0) {
            return InsurerDiscountType.values()[Integer.valueOf(values[0])];

        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if ( o instanceof InsurerDiscountType){
            return ((InsurerDiscountType)o).toString();
            //return Integer.toString(((LiabilityStatus)o).ordinal());
        }
        return "";
    }

}
