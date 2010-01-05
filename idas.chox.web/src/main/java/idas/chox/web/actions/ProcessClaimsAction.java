/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.common.UploadStatus;
import idas.chox.core.model.XMLParseResult;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.xmlValidation.BordereauParseStatus;
import idas.chox.core.xmlValidation.BordereauResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Emmanuel
 */
public class ProcessClaimsAction extends BaseAction {

    private File upload;
    private String filename;
    private UploadClaimXMLService service;
    private List<XMLParseResult> result;
    private String bordereauStatus;
    private String bordereauStatusDesc;
    private BordereauResult bordereauResult;
    private Integer totalClaim;

    public String getBordereauStatusDesc() {
        return bordereauStatusDesc;
    }

    public void setBordereauStatusDesc(String bordereauStatusDesc) {
        this.bordereauStatusDesc = bordereauStatusDesc;
    }

    public Integer getTotalClaim() {
        return totalClaim;
    }

    public void setTotalClaim(Integer totalClaim) {
        this.totalClaim = totalClaim;
    }

    public String getBordereauStatus() {
        return bordereauStatus;
    }

    public void setBordereauStatus(String bordereauStatus) {
        this.bordereauStatus = bordereauStatus;
    }

    public BordereauResult getBordereauResult() {
        return bordereauResult;
    }

    public void setBordereauResult(BordereauResult bordereauResult) {
        this.bordereauResult = bordereauResult;
    }

    public void setUpload(File upload) {
        this.upload = upload;
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

    private static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    @Override    
    public String execute() {

        if (this.upload == null || this.filename == null) {
            return ERROR;
        }

        //int pos = this.filename.lastIndexOf(".");
        if ((this.filename.lastIndexOf(".")) <= 0) {
            return ERROR;
        }

        String extention = getExtention(this.filename).toLowerCase();

        if (extention.matches("\\.xml")) {

            bordereauResult = this.service.processClaimXMLFile(this.upload, this.filename.toLowerCase());

            // SET CREATED BY USER AND CREATED DATE
            this.bordereauResult.setCreatedBy( this.getAuthenticatedUser());
            this.bordereauResult.setCreatedDate(DateHelper.getCurrentDateTime());

            // SET STATUS
            if (this.bordereauResult.getBordereauStatus().equals(BordereauParseStatus.allRejected)) {
                bordereauStatus = "All Rejected";
            } else if (this.bordereauResult.getBordereauStatus().equals(BordereauParseStatus.allUploaded)) {
                bordereauStatus = "All Uploaded";
            } else if (this.bordereauResult.getBordereauStatus().equals(BordereauParseStatus.partialUpload)) {
                bordereauStatus = "Partially Uploaded";
            } else if (this.bordereauResult.getBordereauStatus().equals(BordereauParseStatus.error)) {
                bordereauStatus = "Error";
            }

            // GET COUNT
            totalClaim = this.bordereauResult.getClaimResult().size();

            // GET DESCRIPTION
            bordereauStatusDesc = this.bordereauResult.getBordereau().getDescription();

            if (bordereauResult == null) {
                return ERROR;
            } else {
                return SUCCESS;
            }

            /*
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
             */

        } else {
            return ERROR;
        }

    }

    private List<XMLParseResult> doOrderXMLUploadResult(List<XMLParseResult> xmlParseResult) {

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
            String strUpdateStatus) {

        ListIterator listIteratorName = xmlParseResult.listIterator();

        while (listIteratorName.hasNext()) {

            XMLParseResult nextElement = (XMLParseResult) listIteratorName.next();

            if (nextElement.getUploadStatus().equalsIgnoreCase(strUpdateStatus)) {
                xmlNewParseResult.add(nextElement);
            }
        }

        return xmlNewParseResult;
    }
}
