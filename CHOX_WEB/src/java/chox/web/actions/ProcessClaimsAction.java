/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.XMLParseResult;
import chox.services.ClaimService;
import java.io.File;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class ProcessClaimsAction extends BaseAction {

    private File file;
    private String filename;
    private ClaimService service;
    private List<XMLParseResult> result;
    private String uploadType;

    public void setUpload(File file) {
        this.file = file;
    }

    public void setClaimService(ClaimService service) {
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

        String extention = getExtention(this.filename).toLowerCase();

        if (extention.matches("\\.xml")) {
            List<XMLParseResult> parseResult = this.service.processClaimXMLFile(this.file , true);
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
}