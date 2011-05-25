//package idas.chox.service.xml.validations;
//
//import idas.chox.core.util.FileHelper;
//import idas.chox.core.xmlValidation.BordereauParseStatus;
//import idas.chox.core.xmlValidation.BordereauResult;
//import java.io.File;
//import java.util.List;
//
//public class BordereauFileValidation{
//    
//    public static String V_FILE_ERROR = "Invalid File";
//    public static String V_FILE_TYPR_ERROR = "Incorrect File Type.";
//    public static String V_FILE_SIZE_ERROR = "File Size is not allowed exceed "+FileHelper.maxFileSize("MB")+" MB";
//    
//    private List<String> allowTypes;
//
//    public void validate(File file, String fileName, BordereauResult bordereauResult){
//        
//        try {
//            
//            if(!FileHelper.isFileValid(file)){
//                bordereauResult.setValid(false);
//                bordereauResult.addMessage(V_FILE_ERROR);
//            }
//            
//            if(!FileHelper.isFileTypeAllow(fileName, allowTypes)){
//                bordereauResult.addMessage(V_FILE_TYPR_ERROR);
//                bordereauResult.setValid(false);
//            }
//            
//            int iResult = FileHelper.isFileSizeAllow(file);
//            if(iResult==0){
//                bordereauResult.addMessage(V_FILE_ERROR);
//                bordereauResult.setValid(false);     
//            }else if(iResult<0){
//                bordereauResult.addMessage(V_FILE_SIZE_ERROR);
//                bordereauResult.setValid(false);
//            }
//            
//        } catch (Exception ex) {
//            bordereauResult.setValid(false);
//        }
//        
//        if(!bordereauResult.isValid()){
//            bordereauResult.setBordereauStatus(BordereauParseStatus.error);
//        }
//    }
//      
//    public void setAllowTypes(List<String> allowTypes) {
//        this.allowTypes = allowTypes;
//    }
//
//}
