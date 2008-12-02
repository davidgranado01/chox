/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.Util;

import java.util.List;
import java.io.File;
import chox.data.AttachmentType;

public class FileHelper {
    
    public static Boolean isFileValid(File file){
        Boolean bFlag = false;
        
        if(file.canRead() && file.isFile() && file.exists()){
            bFlag = true;
        }
        
        return bFlag;
    }
    
    public static String getNewFileName(String oldFileName){
        return TextHelper.trimWhiteSpace(oldFileName);
    }
    
    public static String getFileExtension(File file){
        String filename = file.getName();
        int pos = filename.lastIndexOf(".");
        return filename.substring(pos+1);        
    }
    
    public static Boolean isFileTypeAllow(File file){
        Boolean bFlag = false;
        String fileExpension = getFileExtension(file).toUpperCase();
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
