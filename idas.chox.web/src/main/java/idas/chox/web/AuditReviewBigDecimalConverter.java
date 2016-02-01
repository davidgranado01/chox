package idas.chox.web;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

// The global BigDecimal converter is converting all the null value to zero which is not desirable behaviour for AuditReview validation
// So implemented the custom AuditReview converter to override the global converter behaviour.
public class AuditReviewBigDecimalConverter extends StrutsTypeConverter {

    @Override
    public Object convertFromString(Map context, String[] values, Class toClass) {

        Object result = null;
        if (values != null && values.length > 0 && values[0] != null) {

            if (values[0] == null) {
                result = null;
            } else if (values[0].isEmpty()) {
                result = null;
            } else if (toClass == BigDecimal.class) {
                result = new BigDecimal(values[0]);
            }

        }
        return result;
    }

    @Override
    public String convertToString(Map context, Object o) {
        return o.toString();
    }
}
