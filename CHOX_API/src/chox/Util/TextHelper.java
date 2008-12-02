package chox.Util;

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
   
}
