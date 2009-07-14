package chox.xmlValidation.rules;

import chox.Util.FileHelper;
import chox.model.Bordereau;
import chox.xmlValidation.result.ParseResult;
import chox.xmlValidation.xmlInterface.fileValidationInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fileValidation implements fileValidationInterface{
    
    public static String V_FILE_ERROR = "Invalid File";
    public static String V_FILE_TYPR_ERROR = "Incorrect File Type.";
    public static String V_FILE_SIZE_ERROR = "File Size is not allowed exceed "+FileHelper.maxFileSize("MB")+" MB";
    
    public ParseResult validate(File file, String fileName, ParseResult parseResult){
        
        try {

            if(!FileHelper.isFileValid(file)){
                parseResult.setStatus(false);
                parseResult.addMessage(V_FILE_ERROR);
            }

            if(!FileHelper.isFileTypeAllow(fileName, getAllowType())){
                parseResult.setStatus(false);
                parseResult.addMessage(V_FILE_TYPR_ERROR);
            }

            int iResult = FileHelper.isFileSizeAllow(file);
            if(iResult==0){
                parseResult.addMessage(V_FILE_ERROR);
                parseResult.setStatus(false);     
            }else if(iResult<0){
                parseResult.addMessage(V_FILE_SIZE_ERROR);
                parseResult.setStatus(false);
            }
            
        } catch (Exception ex) {
            parseResult.setStatus(false);
            parseResult.addMessage(ex.getLocalizedMessage());
        }
        
        return parseResult;
    }
    
    private List<String> getAllowType(){
        List<String> allowType = new ArrayList<String>();
        allowType.add("xml");
        return allowType;
    }

    public String getRuleId() {
        return "F0002";
    }

}
