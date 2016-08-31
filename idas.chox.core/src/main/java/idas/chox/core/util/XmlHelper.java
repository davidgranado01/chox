package idas.chox.core.util;

import java.util.Calendar;
import org.w3c.dom.*;
import java.math.BigDecimal;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlHelper {

    private static final Logger LOG = LoggerFactory.getLogger(XmlHelper.class);

    public static boolean isNotNull(String s) {
        Boolean bFlag = false;
        if (s != null && !s.isEmpty()) {
            bFlag = true;
        }
        return bFlag;
    }

    public static boolean isNotNullDate(String s) {
        Boolean bFlag = false;
        if (s != null && !s.isEmpty() && !s.equalsIgnoreCase("0") && !s.equalsIgnoreCase("1899-12-30T00:00:00")) {
            bFlag = true;
        }
        return bFlag;
    }

    public static String getNodeValue(Element root, String nodeName) {
        String sOutput = "";
        if (isNotNull(XMLUtils.getElementValue(root, nodeName))) {
            sOutput = XMLUtils.getElementValue(root, nodeName);
        }
        return sOutput.trim();
    }

    public static String getEmailAddressFromNode(Element thisElement, String thisNodeName) {
        int iMaxEmailLenght = 64;

        String outEmail = XMLUtils.getElementValue(thisElement, thisNodeName);

        if (outEmail.length() < iMaxEmailLenght) {
            iMaxEmailLenght = outEmail.length();
        }

        return outEmail.substring(0, iMaxEmailLenght);
    }

    public static Integer getIntegerFromNode(Element thisElement, String thisNodeName) {
        Integer bOutput = 0;
        String sOutput = XMLUtils.getElementValue(thisElement, thisNodeName);
        if (sOutput != null && !sOutput.isEmpty()) {
            try {
                bOutput = Integer.parseInt(sOutput);
            } catch (NumberFormatException ex) {
                LOG.warn("Exception converting node '{}' to an integer: {}", thisNodeName, sOutput);
                throw ex;
            }
        }
        return bOutput;
    }

    public static BigDecimal getBigDecimalFromNode(Element thisElement, String thisNodeName) {

        BigDecimal bOutput = new BigDecimal("0.00");
        String sOutput = XMLUtils.getElementValue(thisElement, thisNodeName);

        if (sOutput != null && !sOutput.isEmpty()) {
            try {
                bOutput = new BigDecimal(sOutput);
            } catch (NumberFormatException ex) {
                LOG.error("Exception converting node '{}' to a bigdecimal: {}", thisNodeName, sOutput);
                throw ex;
            }
        }

        return bOutput;
    }

    public static double getDoubleFromNode(Element thisElement, String thisNodeName) {
        double bOutput = 0.00;
        String sOutput = XMLUtils.getElementValue(thisElement, thisNodeName);
        if (sOutput != null && !sOutput.isEmpty()) {
            try {
                bOutput = Double.parseDouble(sOutput);
            } catch (NumberFormatException ex) {
                LOG.warn("Exception converting node '{}' to an integer: {}", thisNodeName, sOutput);
                throw ex;
            }
        }
        return bOutput;
    }

    public static Boolean getBooleanFromNode(Element thisElement, String thisNodeName) {

        Boolean returnBoolean = null;
        String thisNodeValue = XMLUtils.getElementValue(thisElement, thisNodeName);

        if (thisNodeValue.equalsIgnoreCase("y") || thisNodeValue.equalsIgnoreCase("yes")) {
            returnBoolean = Boolean.TRUE;
        } else if (thisNodeValue.equalsIgnoreCase("n") || thisNodeValue.equalsIgnoreCase("no")) {
            returnBoolean = Boolean.FALSE;
        }

        return returnBoolean;
    }

    public static Date getDateFromNode(Element thisElement, String thisNodeName) {
        String thisNodeValue = XMLUtils.getElementValue(thisElement, thisNodeName);

        Date date = null;

        if (thisNodeValue != null && !thisNodeValue.equalsIgnoreCase("") && !thisNodeValue.equalsIgnoreCase("0") && !thisNodeValue.equalsIgnoreCase("1899-12-30T00:00:00")) {
            
            date = parseDateTime(thisNodeValue);
        }

        return date;
    }

    public static Date getDateFromDateTimeNode(Element thisElement, String thisNodeName) {
        String thisNodeValue = XMLUtils.getElementValue(thisElement, thisNodeName);

        Date date = null;
        LOG.debug("Parsing date from '{}'", thisNodeValue);
        try {
            if (thisNodeValue != null && !thisNodeValue.isEmpty()) {
                if (thisNodeValue.charAt(4) == '-') {
                    date = parseDateTime(thisNodeValue);
                } else {
                    date = parseDate(thisNodeValue.substring(0, 10));
                }
            }
        } catch (Exception ex) {
            LOG.error("Error getting Date from DateTime string '{}'", thisNodeValue);
            return null;
        }
        if (LOG.isDebugEnabled()) {
            if (thisNodeValue != null) {
                LOG.debug("Returning date '{}' from '{}'", date, thisNodeValue.substring(0, 10));
            } else {
                LOG.debug("Returning date '{}' from 'null'", date);
            }
        }
        return date;
    }

    public static String getTimeFromDateTimeNode(Element thisElement, String thisNodeName) {
        String thisNodeValue = XMLUtils.getElementValue(thisElement, thisNodeName);

        String time = null;

        LOG.debug("Parsing time from '{}'", thisNodeValue);
        if (thisNodeValue != null && !thisNodeValue.isEmpty()) {
            time = thisNodeValue.substring(10).trim();
            if (time.charAt(0)=='T')
                time = time.substring(1);
        }
        if (LOG.isDebugEnabled()) {
            if (thisNodeValue != null) {
                LOG.debug("Returning time '{}' from '{}'", time, thisNodeValue.substring(0, 10));
            } else {
                LOG.debug("Returning time '{}' from 'null'", time);
            }
        }

        return time;
    }

    public static Date parseDateTime(String t) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, Integer.parseInt(t.substring(0, 4)));
        c.set(Calendar.MONTH, Integer.parseInt(t.substring(5, 7)) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(t.substring(8, 10)));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t.substring(11, 13)));
        c.set(Calendar.MINUTE, Integer.parseInt(t.substring(14, 16)));
        c.set(Calendar.SECOND, Integer.parseInt(t.substring(17)));
        return c.getTime();
    }

    public static Date parseDate(String t) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, Integer.parseInt(t.substring(6, 10)));
        c.set(Calendar.MONTH, Integer.parseInt(t.substring(3, 5)) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(t.substring(0, 2)));
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }
}
