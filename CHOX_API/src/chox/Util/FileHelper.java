/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.Util;

import java.util.List;
import java.io.File;
import chox.data.AttachmentType;
import java.util.Random;

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
    
    public static String getNewFileName(String oldFileName){
        
        Random randomGenerator = new Random(1234567890);
        int iRandom = randomGenerator.nextInt(99999999);
        return TextHelper.trimWhiteSpace(String.valueOf(iRandom)+"_"+oldFileName);
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
