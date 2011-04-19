/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.BordereauWithoutFile;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.BordereauService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.util.FileHelper;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.validations.BordereauSchemaValidation;
import idas.chox.web.viewdata.BordereauViewData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.UploadedXMLClaimsDetailService;
import idas.chox.web.viewdata.UploadedClaimDetailViewData;
import java.text.Format;
import java.text.SimpleDateFormat;
import org.apache.struts2.interceptor.SessionAware;

/**
 *
 * @author seeni
 */
public class XmlUploadAction extends BaseAction implements SessionAware {

    private static final Logger LOG = LoggerFactory.getLogger(XmlUploadAction.class);
    protected static String NEW_UPLOADED_XML_FILE_STATUS = "Waiting to be Processed";
    protected static String NEW_UPLOADED_XML_FILE_DESCRIPTION = "File is waiting to be processed";
    private ChorganisationService chorganisationService;
    private BordereauService bordereauService;
    private UploadedXMLClaimsDetailService uploadedXMLClaimsDetailService;
    private JSONArray jObject;
    private boolean uploadFlag;
    private File uploadedFile;
    private String uploadedFileFileName;
    private int bordereauId;
    private UploadClaimXMLService service;
    private BordereauSchemaValidation bordereauSchemaValidation;
    private Map session;
    Format dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private String sort;
    private String dir;
    private int days;
    private int start;
    private int limit;
    private int totalCount;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setBordereauSchemaValidation(BordereauSchemaValidation bordereauSchemaValidation) {
        this.bordereauSchemaValidation = bordereauSchemaValidation;
    }

    public int getBordereauId() {
        return bordereauId;
    }

    public void setBordereauId(int bordereauId) {
        this.bordereauId = bordereauId;
    }

    public String getUploadedFileFileName() {
        return uploadedFileFileName;
    }

    public void setUploadedFileFileName(String fileName) {
        this.uploadedFileFileName = fileName;
    }

    public File getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setBordereauService(BordereauService bordereauService) {
        this.bordereauService = bordereauService;
    }

    public void setUploadedXMLClaimsDetailService(UploadedXMLClaimsDetailService claimsDetailService) {
        this.uploadedXMLClaimsDetailService = claimsDetailService;
    }

    public void setUploadClaimXMLService(UploadClaimXMLService service) {
        this.service = service;
    }

    public boolean isUploadFlag() {
        uploadFlag = false;
        if (getIsCHO()) {
            int chorgId = getAuthenticatedUser().getChorganisation().getId();
            uploadFlag = chorganisationService.isCreditHireWithBreBand(chorgId);
        }
        return uploadFlag;
    }

    public String getJsonArrayData() {
        if (jObject != null) {
//            LOG.debug("returning claimDetails from jobject total size is: {}", this.jObject.size());
            return "{totalCount:" + totalCount + ",results:" + jObject.toString() + "}";
        }
        return "";
    }

    private static String getExtention(String fileName) {
        int pos = fileName.lastIndexOf(".");
        return fileName.substring(pos);
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String uploadNewClaimsFile() {

        List<ClaimResult> claimResults = null;

        if (this.uploadedFile == null || this.uploadedFileFileName == null) {
            this.getActionResponse().AddError("No File Uploaded");
            return ERROR;
        }

        if (!FileHelper.isFileValid(uploadedFile)) {
            this.getActionResponse().AddError("File is not Valid");
            return ERROR;
        }

        if ((this.uploadedFileFileName.lastIndexOf(".")) <= 0) {
            this.getActionResponse().AddError("Unknown File Format");
            return ERROR;
        }

        int iResult = FileHelper.isFileSizeAllow(this.uploadedFile);
        if (iResult == 0) {
            this.getActionResponse().AddError("Invalid File");
            return ERROR;
        } else if (iResult < 0) {
            LOG.debug("Attachment File is too big: {}", uploadedFile.length());
            this.getActionResponse().AddError("File Size is exceeded " + FileHelper.maxFileSize("MB") + " MB limit.");
            return ERROR;
        }
        String extention = getExtention(this.uploadedFileFileName).toLowerCase();
        FileInputStream streamIn = null;
        if (extention.matches("\\.xml")) {

            Bordereau bordereau = new Bordereau();
            try {
                streamIn = new FileInputStream(uploadedFile);
            } catch (FileNotFoundException ex) {
                this.getActionResponse().AddError(ex.getMessage());
                return ERROR;
            }

            byte fileContent[] = new byte[(int) uploadedFile.length()];
            try {
                streamIn.read(fileContent);
            } catch (IOException ex) {
                this.getActionResponse().AddError(ex.getMessage());
                return ERROR;
            }
            Document document = DocumentHelper.getDocumentFromFile(uploadedFile);
            bordereau.setValid(true);
            bordereauSchemaValidation.validate(document, bordereau);
            if (!bordereau.isValid()) {
                bordereau.setStatus("Error");
                bordereau.setDescription("Invalid Schema");
            } else {
                bordereau.setStatus(NEW_UPLOADED_XML_FILE_STATUS);
                bordereau.setDescription(NEW_UPLOADED_XML_FILE_DESCRIPTION);
            }
            try {
                claimResults = this.service.formClaimResults(document);
                bordereau.setTotalClaims(claimResults.size());
            } catch (Exception ex) {
                LOG.debug("Error thrown while getting claims from document, error message is : {}", ex.getMessage());
                bordereau.setStatus("Error");
                bordereau.setDescription("Invalid Schema");
            }
            bordereau.setFileSize((Long) uploadedFile.length());
            bordereau.setFileName(uploadedFileFileName);
            bordereau.setFileBuffer(fileContent);
            bordereau.setProcessed(false);
            bordereauService.saveBordereau(bordereau);
//            LOG.debug("uploaded file has been saved successfully {} ", bordereau.getFileName());
            this.getActionResponse().AssignMessageResult("File has been uploaded successfully");
            return SUCCESS;

        } else {
            LOG.debug("unknown file format is found ");
            this.getActionResponse().AddError("Unknown File Format");
            return ERROR;
        }

    }

    public String getUploadedFiles() {
        int defaultDays = 0;
        if (days > 1) {
            defaultDays = days;
        }
        List<BordereauViewData> viewDatas = new ArrayList<BordereauViewData>();
        List<BordereauWithoutFile> uploadedFileList = null;
        SearchResult searchResult = bordereauService.getUploadedFiles(getAuthenticatedUser(), defaultDays, sort, dir, start, limit);
        uploadedFileList = searchResult.getResult();
        for (BordereauWithoutFile bordereau : uploadedFileList) {
            viewDatas.add(new BordereauViewData(bordereau));
        }
        this.jObject = JSONArray.fromObject(viewDatas);
        totalCount = searchResult.getTotalCount();
        return SUCCESS;
    }

    public String processUploadedXmlFile() {
        if (session.get("claimsDetails") != null) {
            this.getActionResponse().AddError("Please wait un till previous file processing request complete.");
            return ERROR;
        }
        int totalRecord = 0;
        int totalProcessed = 0;
        FileOutputStream outputStream = null;
        File uplodedFile = new File("temp.xml");
        List<ClaimResult> claimResults = null;
        List<UploadedXMLClaimsDetail> claimsDetails = new ArrayList<UploadedXMLClaimsDetail>();
        List<String> choReferences = new ArrayList<String>();
        Bordereau bordereau = null;
        if (bordereauId >= 0) {
            bordereau = bordereauService.getBordereauById(bordereauId);
            if (!bordereau.isProcessed() && bordereau.isValid()) {
                try {
                    outputStream = new FileOutputStream(uplodedFile);
                } catch (FileNotFoundException ex) {
                    LOG.error("File not found");
                    this.getActionResponse().AddError("Error occured while reading file. Please report to chox admin.");
                    return ERROR;
                }
                try {
                    outputStream.write(bordereau.getFileBuffer());
                } catch (IOException ex) {
                    LOG.error("IOException thrown while writing to file");
                    this.getActionResponse().AddError("Error occured while writting to file. Please report to chox admin.");
                    return ERROR;
                }
                Document document = DocumentHelper.getDocumentFromFile(uplodedFile);
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException ex) {
                    LOG.error("IOException thrown while closing the file, error message is : {}", ex.getMessage());
                }
                bordereau.setStatus("Processing..");
                bordereau.setDescription("File is being processed in the server");
                bordereauService.saveBordereau(bordereau);
                try {
                    claimResults = this.service.formClaimResults(document);
                    totalRecord = claimResults.size();
                } catch (Exception ex) {
                    LOG.error("Error thrown while getting claims from document, error message is : {}", ex.getMessage());
                    this.getActionResponse().AddError("An unexpected error occured while reading the file. Please report to chox admin.");
                    bordereau.setStatus(NEW_UPLOADED_XML_FILE_STATUS);
                    bordereau.setDescription(NEW_UPLOADED_XML_FILE_DESCRIPTION);
                    bordereauService.saveBordereau(bordereau);
                    return ERROR;
                }
                try {
                    for (ClaimResult claimResult : claimResults) {
                        UploadedXMLClaimsDetail xMLClaimsDetail = new UploadedXMLClaimsDetail();

                        if (this.service.doProcessBordereauResult(claimResult, choReferences)) {

                            totalProcessed++;
                            xMLClaimsDetail.setValid(true);

                        } else {
                            xMLClaimsDetail.setValid(false);
                        }


                        xMLClaimsDetail.setBordereauId(bordereau.getId());

                        xMLClaimsDetail.setProcessStatus(claimResult.getProcessStatus());
                        if (!claimResult.getMessage().isEmpty()) {
                            xMLClaimsDetail.setMessage(claimResult.getMessage().toString());
                        } else {
                            xMLClaimsDetail.setMessage("");
                        }

                        xMLClaimsDetail.setRemark(claimResult.getUploadedStatus());
                        if (claimResult.getClaim() != null && claimResult.getClaim().getChoReference() != null) {
                            xMLClaimsDetail.setChoReference(claimResult.getClaim().getChoReference());
                            if (claimResult.getClaim().getId() != null && claimResult.getClaimStatus() != null && !claimResult.getClaimStatus().equals("")) {
                                if (claimResult.isDuplicateClaimInSameXmlFile()) {
                                    xMLClaimsDetail.setClaimId(0);
                                    xMLClaimsDetail.setClaimStatus("N/A");
                                } else {
                                    xMLClaimsDetail.setClaimId(claimResult.getClaim().getId());
                                    xMLClaimsDetail.setClaimStatus(claimResult.getClaimStatus());
                                }
                                this.service.evictClaim(claimResult.getClaim());
                                LOG.debug("Claim evicted.");
                            } else {
                                xMLClaimsDetail.setClaimStatus("N/A");
                            }
                        }
                        claimsDetails.add(0, xMLClaimsDetail);
                        synchronized (session) {
                            session.put("claimsDetails", claimsDetails);
                        }
                        LOG.debug("putting claimDetails into session total size is: {}", claimsDetails.size());

                        LOG.debug("{} of {} claims have been processed", totalRecord, totalProcessed);
                    }
                } catch (Throwable ex) {

                    LOG.error("Unexpected Error thrown while processing claim , Error message {}", ex.getMessage());
                    session.put("claimsDetails", null);
                    this.getActionResponse().AddError("Unexpected Error occured, Please report to Chox admin.");
                    bordereau.setStatus(NEW_UPLOADED_XML_FILE_STATUS);
                    bordereau.setDescription(NEW_UPLOADED_XML_FILE_DESCRIPTION);
                    bordereauService.saveBordereau(bordereau);
                    return ERROR;
                }
                if (totalProcessed >= totalRecord) {
                    bordereau.setStatus("All Uploaded");
                    bordereau.setDescription("All claims have been uploaded successfully");
                } else if (totalProcessed < totalRecord && totalProcessed != 0) {
                    bordereau.setStatus("Partially Uploaded");
                    bordereau.setDescription(totalProcessed + " out of " + totalRecord + " claims have been uploaded");
                } else if (totalProcessed == 0) {
                    bordereau.setStatus("All Rejected");
                    bordereau.setDescription("All " + totalRecord + " claims have been rejected");
                }
            } else {
                if (!bordereau.isValid()) {
                    LOG.debug("Invalid schema found in this file : {}", bordereau.getFileName());
                    this.getActionResponse().AddError("Invalid Schema.");
                    return ERROR;
                }
                LOG.debug("this file have been processed already: {}", bordereau.getFileName());
                this.getActionResponse().AddError("This file has been processed already.");
                return ERROR;
            }
        } else {
            LOG.error("this file is not found: {}", bordereau.getFileName());
            this.getActionResponse().AddError("File not found.");
            return ERROR;
        }
        this.getActionResponse().AssignMessageResult("File processing completed");
        for (UploadedXMLClaimsDetail claimsDetail : claimsDetails) {
            uploadedXMLClaimsDetailService.saveUploadedXMLClaimsDetail(claimsDetail);
        }
        bordereau.setProcessed(true);
        bordereauService.saveBordereau(bordereau);
        LOG.debug("this file have been processed successfully: {}", bordereau.getFileName());
        session.put("claimsDetails", null);
        return SUCCESS;
    }

    public String getUploadedClaimsDetails() {
        if (bordereauId > 0) {
            Bordereau bordereau = bordereauService.getBordereauById(bordereauId);
            List<UploadedXMLClaimsDetail> claimsDetails = new ArrayList<UploadedXMLClaimsDetail>();
            List<UploadedClaimDetailViewData> claimsDetailsViewData = new ArrayList<UploadedClaimDetailViewData>();
            if (bordereau.isProcessed()) {
                claimsDetails = uploadedXMLClaimsDetailService.getUploadedXMLClaimsDetailByBordereauId(bordereauId);
                LOG.debug("getting claimDetails from databse total size is: {}", claimsDetails.size());
            } else {

                synchronized (session) {
                    if (session.containsKey("claimsDetails") && session.get("claimsDetails") != null) {
                        claimsDetails = (List<UploadedXMLClaimsDetail>) session.get("claimsDetails");
                        LOG.debug("getting claimDetails from session total size is: {}", claimsDetails.size());
                    }
                }
            }
//            int rowNumber = 0;
            for (UploadedXMLClaimsDetail claimDetailViewData : claimsDetails) {
//                rowNumber++;
                claimsDetailsViewData.add(new UploadedClaimDetailViewData(claimDetailViewData));
            }
//            Collections.sort(claimsDetailsViewData, new XmlUploadClaimsViewDataComparator());
            this.jObject = JSONArray.fromObject(claimsDetailsViewData);
            totalCount = this.jObject.size();
            return SUCCESS;
        } else {
            return SUCCESS;
        }
    }

    public String removeUploadedFile() {
        if (bordereauId > 0) {
            Bordereau bordereau = bordereauService.getBordereauById(bordereauId);
            if (bordereau.isProcessed()) {
                this.getActionResponse().AddError("Sorry processed file can not be deleted.");
                return ERROR;
            } else {
                if (this.bordereauService.deleteBordereau(bordereau)) {
                    this.getActionResponse().AssignMessageResult("File removed successfully.");
                    return SUCCESS;
                } else {
                    this.getActionResponse().AddError("An unexpected error occured while deleting the file. Please report to chox admin.");
                    return ERROR;
                }
            }
        } else {
            this.getActionResponse().AddError("No file have been selected.");
            return ERROR;
        }
    }

    @Override
    public void setSession(Map map) {
        this.session = map;
    }
}
