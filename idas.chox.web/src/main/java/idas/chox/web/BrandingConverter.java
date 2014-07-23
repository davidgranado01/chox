package idas.chox.web;

import idas.chox.core.model.Branding;
import java.util.Map;
import org.apache.struts2.util.StrutsTypeConverter;

public class BrandingConverter extends StrutsTypeConverter {

    Branding values[] = Branding.values();

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {
        if (values != null && values.length > 0 && values[0] != null && values[0].length() > 0) {
            return Branding.values()[Integer.valueOf(values[0])];
        }
        return null;
    }

    @Override
    public String convertToString(Map context, Object o) {
        if (o instanceof Branding) {
            return ((Branding) o).toString();
        }
        return "";
    }

}
