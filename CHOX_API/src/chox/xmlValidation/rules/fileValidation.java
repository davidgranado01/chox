/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.xmlValidation.rules;

import chox.Util.FileHelper;
import chox.model.Bordereau;
import chox.xmlValidation.result.ParseResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class fileValidation {
    
    public static String V_FILE_ERROR = "Invalid File";
    public static String V_FILE_TYPR_ERROR = "Incorrect File Type.";
    public static String V_FILE_SIZE_ERROR = "File Size is not allowed exceed "+FileHelper.maxFileSize("MB")+" MB";
    
    private boolean status = true;
    
    public ParseResult validate(File file, String fileName) throws FileNotFoundException, IOException {

        ParseResult parseResult = new ParseResult(); 
        List<String> errMsg = new ArrayList<String>();
        Bordereau bordereau = new Bordereau();
        
        try {

            if(!FileHelper.isFileValid(file)){
                status = false;     
                errMsg.add(V_FILE_ERROR);
            }

            if(!FileHelper.isFileTypeAllow(fileName, getAllowType())){
                status = false;  
                errMsg.add(V_FILE_TYPR_ERROR);
            }

            int iResult = FileHelper.isFileSizeAllow(file);
            if(iResult==0){
                errMsg.add(V_FILE_ERROR);
                status = false;        
            }else if(iResult<0){
                errMsg.add(V_FILE_SIZE_ERROR);
                status = false;
            }
            
        } catch (Exception ex) {
            status = false;
            errMsg.add(ex.getLocalizedMessage());
        }

        if(status){
            
            FileInputStream streamIn = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            streamIn.read(fileContent);
            bordereau.setFileBuffer(fileContent);
            
        }
        
        // SET BORDEREAU
        bordereau.setFileName(fileName);
        bordereau.setStatus(status);
        bordereau.setDescription("ASD");
        
        // SET PARSE RESULT
        parseResult.setBordereau(bordereau);
        parseResult.setStatus(status);
        parseResult.setMessage(errMsg);
        
        return parseResult;
    }
    
    private List<String> getAllowType(){
        List<String> allowType = new ArrayList<String>();
        allowType.add("xml");
        return allowType;
    }
}
