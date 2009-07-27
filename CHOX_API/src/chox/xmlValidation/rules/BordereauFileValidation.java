package chox.xmlValidation.rules;

import chox.Util.FileHelper;
import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.model.status.BordereauParseStatus;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BordereauFileValidation{
    
    public static String V_FILE_ERROR = "Invalid File";
    public static String V_FILE_TYPR_ERROR = "Incorrect File Type.";
    public static String V_FILE_SIZE_ERROR = "File Size is not allowed exceed "+FileHelper.maxFileSize("MB")+" MB";
    
    public BordereauResult validate(File file, String fileName, BordereauResult bordereauResult){
        
        try {

            if(!FileHelper.isFileValid(file)){
                bordereauResult.setValid(false);
                bordereauResult.addMessage(V_FILE_ERROR);
            }
            
            int iResult = FileHelper.isFileSizeAllow(file);
            if(iResult==0){
                bordereauResult.addMessage(V_FILE_ERROR);
                bordereauResult.setValid(false);     
            }else if(iResult<0){
                bordereauResult.addMessage(V_FILE_SIZE_ERROR);
                bordereauResult.setValid(false);
            }
            
        } catch (Exception ex) {
            bordereauResult.setValid(false);
            
            bordereauResult.addMessage(ex.getLocalizedMessage());
        }
        
        if(!bordereauResult.isValid()){
            bordereauResult.setBordereauStatus(BordereauParseStatus.error);
        }
        
        return bordereauResult;
    }
    
    private List<String> getAllowType(){
        List<String> allowType = new ArrayList<String>();
        allowType.add("xml");
        return allowType;
    }

}
