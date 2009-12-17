package idas.chox.core.util;

import java.util.StringTokenizer;

public class TextHelper {

    public static String trimWhiteSpace(String iStr){
        StringTokenizer st = new StringTokenizer(iStr," ",false);
        String t="";
        while (st.hasMoreElements()){
            String stmp = (String)st.nextElement();
            if(stmp.length()>0){
                t += stmp.trim();
            }
        }
        return t;
    }
    
    public static boolean isValidText(String strInp){
        
        boolean bFlag = false;
        
        if((strInp.trim()).length()>1){
            bFlag = true;
        }
        
        return bFlag;
    }

    public static String getComma(String iString){

        String oString = "";
        StringTokenizer st = new StringTokenizer(iString);
        while (st.hasMoreTokens()) {
             oString += "'"+(st.nextToken(",")).trim()+"',";
        }

        if(oString.length()>0){
            oString = oString.substring(0, oString.length()-1);
        }

        return oString;
    }
    
    public static String getSamiColon(String iString){
        
        String oString = "";
        StringTokenizer st = new StringTokenizer(iString);
        while (st.hasMoreTokens()) {
             oString += "'"+(st.nextToken(";")).trim()+"',";
        }
        
        if(oString.length()>0){
            oString = oString.substring(0, oString.length()-1);
        }
     
        return oString;
    }

    public static int getId(String sInput){

        int iOutput = -1;

        if((sInput.trim()).length()>0 && sInput!=null && !sInput.equalsIgnoreCase("")){
            iOutput = Integer.parseInt(sInput);
        }

        return iOutput;
    }
}
