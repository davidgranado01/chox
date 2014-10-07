package idas.chox.reporttoexcel;

/**
 *
 * @author John
 */
public class Util {
    private static final String moneyRegex = "-?\\d+\\.\\d{2}";
    private static final String dateTimeRegex = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?";

    public static boolean isDate(String str) {
        return false;
    }
    
    public static boolean isDateTime(String str) {
        return str.matches(dateTimeRegex);
    }
    
    public static boolean isMoney(String str) {
        return str.matches(moneyRegex);
    }
    
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public static boolean isNumeric2(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }
}
