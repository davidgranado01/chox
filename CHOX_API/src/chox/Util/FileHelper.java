/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.Util;

import java.util.List;
import java.io.File;
import chox.data.AttachmentType;
import java.math.BigInteger;
import java.util.Random;
import java.security.SecureRandom;

public class FileHelper {
    
    public static String getClassPath(){
        String fileName = "struts.xml";
        String classPath = FileHelper.class.getClassLoader().getResource(fileName).getPath();
        int dotIndex = classPath.lastIndexOf(fileName);
        classPath = classPath.substring(0, dotIndex);
        return classPath;
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
    
    public static String getNewFileName(String oldFileName){
    
        // Random randomGenerator = new Random(1234567890);
        // int iRandom = randomGenerator.nextInt(99999999);
        return TextHelper.trimWhiteSpace(getRandomString()+"_"+oldFileName);
    }
    
    public static String getFileExtension(String filename){
        //String filename = file.getName();
        int pos = filename.lastIndexOf(".");
        return filename.substring(pos+1);        
    }
    
    public static Boolean isFileTypeAllow(File file){
        Boolean bFlag = false;
        String fileExpension = getFileExtension(file.getName()).toUpperCase();
        List<String> allowTypes = AttachmentType.getAttachmentType();
        for(String s : allowTypes){
            if(s.toUpperCase().equalsIgnoreCase(fileExpension)){
                bFlag = true;
                break;
            }
        }
        return bFlag;
    }
    
   
}
