package idas.chox.core.util;

import java.util.Calendar;
import org.w3c.dom.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


public class XmlHelper {

    public static boolean isNotNull(String s) {
        Boolean bFlag = false;
        if (s != null && !s.equalsIgnoreCase("")) {
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
        if (sOutput != null && !sOutput.equalsIgnoreCase("") && sOutput.length() > 0) {
            bOutput = Integer.parseInt(sOutput);
        }
        return bOutput;
    }

    public static BigDecimal getBigDecimalFromNode(Element thisElement, String thisNodeName) {

        BigDecimal bOutput = new BigDecimal("0.00");
        String sOutput = XMLUtils.getElementValue(thisElement, thisNodeName);

        if (sOutput != null && !sOutput.equalsIgnoreCase("") && sOutput.length() > 0) {
            bOutput = new BigDecimal(sOutput);
        }

        return bOutput;
    }

    public static double getDoubleFromNode(Element thisElement, String thisNodeName) {
        double bOutput = 0.00;
        String sOutput = XMLUtils.getElementValue(thisElement, thisNodeName);
        if (sOutput != null && !sOutput.equalsIgnoreCase("") && sOutput.length() > 0) {
            bOutput = Double.parseDouble(sOutput);
        }
        return bOutput;
    }

    public static Boolean getBooleanFromNode(Element thisElement, String thisNodeName) {

        Boolean returnBoolean = false;
        String thisNodeValue = XMLUtils.getElementValue(thisElement, thisNodeName);

        if (thisNodeValue.equalsIgnoreCase("y")) {
            returnBoolean = true;
        }

        return returnBoolean;

    }

    public static Timestamp getTimeStampFromNode(Element thisElement, String thisNodeName) {
        String thisNodeValue = XMLUtils.getElementValue(thisElement, thisNodeName);

        Timestamp returnTimeStamp = null;

        if (thisNodeValue != null && !thisNodeValue.equalsIgnoreCase("")) {
            returnTimeStamp = parseDate(thisNodeValue);
        }

        return returnTimeStamp;
    }

    public static Timestamp parseDate(String t) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.YEAR, Integer.parseInt(t.substring(0, 4)));
        c.set(Calendar.MONTH, Integer.parseInt(t.substring(5, 7)) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(t.substring(8, 10)));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t.substring(11, 13)));
        c.set(Calendar.MINUTE, Integer.parseInt(t.substring(14, 16)));
        c.set(Calendar.SECOND, Integer.parseInt(t.substring(17)));
        return new Timestamp(c.getTimeInMillis());
    }
}
