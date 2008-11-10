/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.XMLParseResult;
import chox.services.ClaimService;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Emmanuel
 */
public class UploadClaimsAction extends BaseAction {

    private File file;
    private String contentType;
    private String filename;
    private ClaimService service;
   
    public void setUpload(File file) {
        this.file = file;
    }

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    public void setUploadContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUploadFileName(String filename) {
        this.filename = filename;
    }

    private static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    @Override
    public String execute() {

        String extention = getExtention(this.filename).toLowerCase();
        
        if(extention.matches("\\.xml"))
        {
            ArrayList<XMLParseResult> result = this.service.processClaimXMLFile(this.file, "A", true);
            return SUCCESS;
        }
        else
        {
            return ERROR;
        }                
    }
}