package idas.chox.web;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class BigDecimalConverter extends StrutsTypeConverter {
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {

		Object result = null;
		if (values != null && values.length > 0 && values[0] != null) {

			if (values[0] == null)
				result = BigDecimal.valueOf(0L);
			else if (values[0] == "")
				result = BigDecimal.valueOf(0L);
			else if (toClass == BigDecimal.class)
				result = new BigDecimal(values[0]);

		}
		return result;
	}

	@Override
	public String convertToString(Map context, Object o) {
		return o.toString();
	}
}
