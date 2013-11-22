package idas.chox.core.util;

import java.util.List;
import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

public class FileHelper {

    public static int MAX_FILE_SIZE_ALLOW = 10485760; // 10MB

    public static int maxFileSize(String unit){

        Integer size = 0;

        if(unit.equalsIgnoreCase("MB")){
            size = MAX_FILE_SIZE_ALLOW / 1024 / 1024;
        }else if(unit.equalsIgnoreCase("KB")){
            size = MAX_FILE_SIZE_ALLOW / 1024;
        }

        return size;
    }

    public static Boolean isFileValid(File file){
        Boolean bFlag = false;
        if(file.canRead() && file.isFile() && file.exists()){
            bFlag = true;
        }
        return bFlag;
    }
    private static String getRandomString(){
       SecureRandom random = new SecureRandom();
       String sRandom = new BigInteger(130, random).toString(10);

       if(sRandom.length()>8){
            sRandom = sRandom.substring(0,8);
       }

       return sRandom.toUpperCase();
    }

    public static String getNewFileName(String oldFileName, boolean isRandom){
        String result;

        if(isRandom){
            result = TextHelper.trimWhiteSpace(getRandomString()+"_"+oldFileName).toLowerCase();
        }else{
            result = TextHelper.trimWhiteSpace(oldFileName).toLowerCase();
        }

        return result;
    }

    public static String getFileExtension(String filename){
        int pos = filename.lastIndexOf('.');
        return filename.substring(pos+1).toLowerCase();
    }

    public static Boolean isFileTypeAllow(String fileName, List<String> allowTypes){
        Boolean bFlag = false;
        String fileExpension = getFileExtension(fileName).toLowerCase();

        for(String s : allowTypes){
            if(s.toLowerCase().equalsIgnoreCase(fileExpension)){
                bFlag = true;
                break;
            }
        }

        return bFlag;
    }

    public static int isFileSizeAllow(File file){

        int result = 1;

        if(file.length() <= 0){
            result = 0;
        }

        if(file.length() > MAX_FILE_SIZE_ALLOW){
            result = -1;
        }

        return result;
    }


}
