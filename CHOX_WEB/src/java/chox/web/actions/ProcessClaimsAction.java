/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.data.UploadStatus;
import chox.model.ClaimStatus;
import chox.model.XMLParseResult;
import chox.services.UploadClaimXMLService;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Emmanuel
 */
public class ProcessClaimsAction extends BaseAction {
    
    private File file;
    private String filename;
    private UploadClaimXMLService service;
    private List<XMLParseResult> result;
    private String uploadType;

    public void setUpload(File file) {
        this.file = file;
    }

    public void setUploadClaimXMLService(UploadClaimXMLService service) {
        this.service = service;
    }   

    public void setUploadFileName(String filename) {
        this.filename = filename;
    }

    public List<XMLParseResult> getResults() {
        return this.result;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    private static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    @Override
    public String execute() {
            
            if(this.file==null || this.filename==null){
                return ERROR;
            }
            
            //int pos = this.filename.lastIndexOf(".");
            if((this.filename.lastIndexOf("."))<=0){
                return ERROR;
            }
            
            String extention = getExtention(this.filename).toLowerCase();
            
            if (extention.matches("\\.xml")) {
                
                List<XMLParseResult> parseResult = this.service.processXML(this.file , true);
                
                if(parseResult.size()>1){
                    parseResult = doOrderXMLUploadResult(parseResult);
                }
                
                if (parseResult == null) {
                    return ERROR;
                } else {
                    this.result = parseResult;
                    return SUCCESS;
                }
            } else {
                return ERROR;
            }

    }
    
    private List<XMLParseResult> doOrderXMLUploadResult(List<XMLParseResult> xmlParseResult){
        
        List<XMLParseResult> newReturnList = new ArrayList<XMLParseResult>();
        newReturnList = getListByUploadStatus(xmlParseResult, newReturnList, UploadStatus.INCORRECT_CLAIM_STATUS);
        newReturnList = getListByUploadStatus(xmlParseResult, newReturnList, UploadStatus.CLAIM_UPLOAD_FAILED);
        newReturnList = getListByUploadStatus(xmlParseResult, newReturnList, UploadStatus.INVOICE_UPLOAD_FAILED);
        newReturnList = getListByUploadStatus(xmlParseResult, newReturnList, UploadStatus.CLAIM_EXIST);
        newReturnList = getListByUploadStatus(xmlParseResult, newReturnList, UploadStatus.INVOICE_EXIST);
        newReturnList = getListByUploadStatus(xmlParseResult, newReturnList, UploadStatus.CLAIM_UPLOAD_SUCCESSFUL);
        newReturnList = getListByUploadStatus(xmlParseResult, newReturnList, UploadStatus.INVOICE_UPLOAD_SUCCESSFUL);        
        return newReturnList;
    }
    
    private List<XMLParseResult> getListByUploadStatus(
            List<XMLParseResult> xmlParseResult, 
            List<XMLParseResult> xmlNewParseResult, 
            String strUpdateStatus){
        
        ListIterator listIteratorName = xmlParseResult.listIterator();
        
        while (listIteratorName.hasNext()) {
            
            XMLParseResult nextElement = (XMLParseResult) listIteratorName.next();
            
            //System.out.println("CLAIM ID:"+nextElement.getClaim().getChoReference()+"|UPLOAD STATUS"+nextElement.getUploadStatus());
            
            if(nextElement.getUploadStatus().equalsIgnoreCase(strUpdateStatus)){
                xmlNewParseResult.add(nextElement);
            }
        }

        return xmlNewParseResult;
    }
}