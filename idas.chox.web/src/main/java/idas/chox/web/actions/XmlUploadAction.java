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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import net.sf.jxls.transformer.XLSTransformer;
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
    private String jsonData;
    private InputStream excelStream;
    private ByteArrayOutputStream buf1;
    private List<UploadedClaimDetailViewData> claimsDetailsViewData = new ArrayList<UploadedClaimDetailViewData>();
    private int recursiveCount;

    public InputStream getExcelStream() {
        return excelStream;
    }

    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getJsonData() {
        return jsonData;
    }

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

    private static String getExtension(String fileName) {
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
            this.getActionResponse().AddError("File is not valid");
            return ERROR;
        }

        if ((this.uploadedFileFileName.lastIndexOf(".")) <= 0) {
            this.getActionResponse().AddError("Unknown File extension - file must end with '.xml'");
            return ERROR;
        }

        int iResult = FileHelper.isFileSizeAllow(this.uploadedFile);
        if (iResult == 0) {
            this.getActionResponse().AddError("Invalid File");
            return ERROR;
        } else if (iResult < 0) {
            LOG.debug("File '{}' is too big: {}", uploadedFileFileName, uploadedFile.length());
            this.getActionResponse().AddError("File size has exceeded " + FileHelper.maxFileSize("MB") + " MB limit.");
            return ERROR;
        }
        String extension = getExtension(this.uploadedFileFileName).toLowerCase();
        FileInputStream streamIn = null;
        if (extension.matches("\\.xml")) {

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
            Document document = null;
            try {
                document = DocumentHelper.getDocumentFromFile(uploadedFile);
                if (document == null) {
                    LOG.error("Could not create document from file : {}", uploadedFile.getAbsolutePath());
                    this.getActionResponse().AddError("File is not a valid file.");
                    return ERROR;
                }
            } catch (Exception ex) {
                LOG.error("Exception thrown in saving file while writing to document : {}", ex.getMessage());
                bordereau.setStatus("Error");
                bordereau.setDescription("Invalid Schema");
            }
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
                LOG.error("Error thrown while getting claims from document, error message is : {}", ex.getMessage());
                bordereau.setStatus("Error");
                bordereau.setDescription("Invalid Schema");
            }
            bordereau.setFileSize((Long) uploadedFile.length());
            bordereau.setFileName(uploadedFileFileName);
            bordereau.setFileBuffer(fileContent);
            bordereau.setProcessed(false);
            bordereauService.saveBordereau(bordereau);
            LOG.debug("Uploaded file '{}' has been saved successfully.", bordereau.getFileName());
            this.getActionResponse().AssignMessageResult("File has been uploaded successfully");
            return SUCCESS;

        } else {
            LOG.error("File extension is not '.xml': {}", extension);
            this.getActionResponse().AddError("Unknown File extension");
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
            this.getActionResponse().AddError("Please wait until the previous Bordereau processing request has completed.");
            return ERROR;
        }
        int totalRecord = 0;
        int totalProcessed = 0;

        List<ClaimResult> claimResults = null;
        List<UploadedXMLClaimsDetail> claimsDetails = new ArrayList<UploadedXMLClaimsDetail>();
        List<String> choReferences = new ArrayList<String>();
        Bordereau bordereau = null;
        if (bordereauId >= 0) {
            bordereau = bordereauService.getBordereauById(bordereauId);
            if (getAuthenticatedUser().getChorganisation().getId().equals(bordereau.getCreatedBy().getChorganisation().getId())) {
                if (!bordereau.isProcessed() && bordereau.isValid()) {
                    Document document = null;
                    InputStream inputStream = new ByteArrayInputStream(bordereau.getFileBuffer());
                    try {
                        document = DocumentHelper.getDocumentFromStream(inputStream);
                    } catch (Exception ex) {
                        LOG.error("Exception thrown creating document from bordereau with id={} : {}", bordereau.getId(), ex.getMessage());
                        this.getActionResponse().AddError("Error occured while processing Bordereau.");
                        return ERROR;
                    }

                    bordereau.setStatus("Processing..");
                    bordereau.setDescription("File is being processed on the server");
                    bordereauService.saveBordereau(bordereau);
                    try {
                        claimResults = this.service.formClaimResults(document);
                        totalRecord = claimResults.size();
                    } catch (Exception ex) {
                        LOG.error("Error thrown while getting claims from brodereau with is={} : {}", bordereau.getId(), ex.getMessage());
                        this.getActionResponse().AddError("An unexpected error occured while processing this Bordereau.");
                        bordereau.setStatus(NEW_UPLOADED_XML_FILE_STATUS);
                        bordereau.setDescription(NEW_UPLOADED_XML_FILE_DESCRIPTION);
                        bordereauService.saveBordereau(bordereau);
                        return ERROR;
                    }
                    try {
                        for (ClaimResult claimResult : claimResults) {
                            UploadedXMLClaimsDetail xmlClaimsDetail = new UploadedXMLClaimsDetail();

                            if (this.service.doProcessBordereauResult(claimResult, choReferences)) {
                                totalProcessed++;
                                xmlClaimsDetail.setValid(true);
                            } else {
                                xmlClaimsDetail.setValid(false);
                            }

                            xmlClaimsDetail.setBordereauId(bordereau.getId());
                            xmlClaimsDetail.setProcessStatus(claimResult.getProcessStatus());

                            if (!claimResult.getMessage().isEmpty()) {
                                xmlClaimsDetail.setMessage(claimResult.getMessage().toString());
                            } else {
                                xmlClaimsDetail.setMessage("");
                            }

                            xmlClaimsDetail.setRemark(claimResult.getUploadedStatus());
                            if (claimResult.getClaim() != null && claimResult.getClaim().getChoReference() != null) {
                                xmlClaimsDetail.setChoReference(claimResult.getClaim().getChoReference());
                                if (claimResult.getClaim().getId() != null && claimResult.getClaimStatus() != null && !claimResult.getClaimStatus().equals("")) {
                                    if (claimResult.isDuplicateClaimInSameXmlFile()) {
                                        xmlClaimsDetail.setClaimId(0);
                                        xmlClaimsDetail.setClaimStatus("N/A");
                                    } else {
                                        xmlClaimsDetail.setClaimId(claimResult.getClaim().getId());
                                        xmlClaimsDetail.setClaimStatus(claimResult.getClaimStatus());
                                    }
                                    this.service.evictClaim(claimResult.getClaim());
                                    LOG.debug("Claim evicted.");
                                } else {
                                    xmlClaimsDetail.setClaimStatus("N/A");
                                }
                            }
                            claimsDetails.add(0, xmlClaimsDetail);
                            synchronized (session) {
                                session.put("claimsDetails", claimsDetails);
                            }
                            LOG.debug("putting claimDetails into session total size is: {}", claimsDetails.size());
                            LOG.debug("{} of {} claims have been processed", totalRecord, totalProcessed);
                        }
                    } catch (Throwable ex) {
                        LOG.error("Unexpected error thrown while processing claim : {}", ex.getMessage());
                        session.put("claimsDetails", null);
                        this.getActionResponse().AddError("An unexpected error has occured - please report to CHOX support.");
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
                        LOG.error("Invalid schema found in this file : {}", bordereau.getFileName());
                        this.getActionResponse().AddError("Invalid Schema.");
                        return ERROR;
                    }
                    LOG.error("this file have been processed already: {}", bordereau.getFileName());
                    this.getActionResponse().AddError("This bordereau has already been processed.");
                    return ERROR;
                }
            } else {
                LOG.error("Un authOrised user trying to process the file : file name :{}, user name : {}", bordereau.getFileName(), getAuthenticatedUser().getUserName());
                this.getActionResponse().AddError("You do not have permission to process this file. Please contact CHOX support.");
                return ERROR;
            }
        } else {
            LOG.error("Bordereau not found: id={}", bordereauId);
            this.getActionResponse().AddError("Bordereau not found.");
            return ERROR;
        }
        bordereau.setProcessed(true);
        this.getActionResponse().AssignMessageResult("The Bordereau has been processed successfully.");
        for (UploadedXMLClaimsDetail claimsDetail : claimsDetails) {
            uploadedXMLClaimsDetailService.saveUploadedXMLClaimsDetail(claimsDetail);
        }
        bordereauService.saveBordereau(bordereau);
        LOG.debug("This file has been processed successfully: {}", bordereau.getFileName());
        session.put("claimsDetails", null);
        return SUCCESS;
    }

    public String getUploadedClaimsDetails() {
        if (bordereauId > 0) {
            Bordereau bordereau = bordereauService.getBordereauById(bordereauId);
            if (getAuthenticatedUser().getChorganisation().getId().equals(bordereau.getCreatedBy().getChorganisation().getId())) {
                List<UploadedXMLClaimsDetail> claimsDetails = new ArrayList<UploadedXMLClaimsDetail>();
                if (bordereau.isProcessed()) {
                    claimsDetails = uploadedXMLClaimsDetailService.getUploadedXMLClaimsDetailByBordereauId(bordereauId);
                    if ((claimsDetails.size() != bordereau.getTotalClaims()) && bordereau.getTotalClaims() != 0 & recursiveCount<10) {
                        recursiveCount++;
                        getUploadedClaimsDetails();
                        this.jObject = JSONArray.fromObject(claimsDetailsViewData);
                        totalCount = this.jObject.size();
                        return SUCCESS;
                    }
                    LOG.debug("getting claimDetails from databse total size is: {}", claimsDetails.size());
                } else {
                    synchronized (session) {
                        if (session.containsKey("claimsDetails") && session.get("claimsDetails") != null) {
                            claimsDetails = (List<UploadedXMLClaimsDetail>) session.get("claimsDetails");
                            LOG.debug("Getting claimDetails from session - total size is: {}", claimsDetails.size());
                        }
                    }
                }
                for (UploadedXMLClaimsDetail claimDetailViewData : claimsDetails) {
                    claimsDetailsViewData.add(new UploadedClaimDetailViewData(claimDetailViewData));
                }
//            Collections.sort(claimsDetailsViewData, new XmlUploadClaimsViewDataComparator());
                this.jObject = JSONArray.fromObject(claimsDetailsViewData);
                totalCount = this.jObject.size();
                return SUCCESS;
            } else {
                LOG.debug("Un authorised user trying to access the uploaded claims detail : file name : {}, user name : {}", bordereau.getFileName(), getAuthenticatedUser().getUserName());
                this.getActionResponse().AddError("You do not have permission to get details of this file. Please contact CHOX support.");
                return ERROR;
            }

        } else {
            return ERROR;
        }
    }

    public String removeUploadedFile() {
        if (bordereauId > 0) {
            Bordereau bordereau = bordereauService.getBordereauById(bordereauId);
            if (getAuthenticatedUser().getChorganisation().getId().equals(bordereau.getCreatedBy().getChorganisation().getId())) {
                if (bordereau.isProcessed()) {
                    this.getActionResponse().AddError("Sorry - a processed file cannot be deleted.");
                    return ERROR;
                } else {
                    if (this.bordereauService.deleteBordereau(bordereau)) {
                        this.getActionResponse().AssignMessageResult("File removed successfully.");
                        return SUCCESS;
                    } else {
                        this.getActionResponse().AddError("An unexpected error occured while deleting this file. Please report to CHOX support.");
                        return ERROR;
                    }
                }
            } else {
                LOG.error("Unauthorised user trying to delete the uploaded file : file name : {}, user name : {}", bordereau.getFileName(), getAuthenticatedUser().getUserName());
                this.getActionResponse().AddError("You do not have permission to delete this file. Please contact CHOX support.");
                return ERROR;
            }
        } else {
            this.getActionResponse().AddError("No file has been selected.");
            return ERROR;
        }
    }

    public String generateExcelReport() throws IOException {
        byte[] b;
        InputStream templateIS = new ClassPathResource("uploadedClaimDetailsTemplate.xls").getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (bordereauId > 0 /*session.get("uploadedClaimsDetails") != null */) {
            if (getUploadedClaimsDetails().equals(SUCCESS)) {
                List<UploadedClaimDetailViewData> listOfUploadedClaimsDetail = claimsDetailsViewData; //(List<UploadedClaimDetailViewData>) session.get("uploadedClaimsDetails");
                Map excelMap = new HashMap();
                excelMap.put("uploadedClaims", listOfUploadedClaimsDetail);
                XLSTransformer transformer = new XLSTransformer();
                transformer.transformXLS(templateIS, excelMap).write(out);
                excelMap.clear();
                b = out.toByteArray();
                excelStream = new ByteArrayInputStream(b);
//            session.put("uploadedClaimsDetails", null);
                return SUCCESS;
            } else {
                this.getActionResponse().AddError("An unexpected error occured while deleting the file. Please report to chox admin.");
                return ERROR;
            }
        } else {
            this.getActionResponse().AddError("No record have been selected.");
            return ERROR;
        }
    }

//    private List<UploadedClaimDetailViewData> mapListFromJsonString(String json) {
//        JSONArray jsonarray = JSONArray.fromObject(json);
//        List<UploadedClaimDetailViewData> list = new ArrayList<UploadedClaimDetailViewData>();
//        for (Iterator iterator = jsonarray.iterator(); iterator.hasNext();) {
//            JSONObject object = (JSONObject) iterator.next();
//            list.add(fromJSONObjectToMap(object));
//        }
//        return list;
//    }
//    private static UploadedClaimDetailViewData fromJSONObjectToMap(JSONObject object) {
//        UploadedClaimDetailViewData claimDetailViewData = new UploadedClaimDetailViewData();
//        claimDetailViewData.setSupplierReferenceNumber(object.getString("supplierReferenceNumber"));
//        claimDetailViewData.setClaimStatus(object.getString("claimStatus"));
//        claimDetailViewData.setProcessStatus(object.getString("processStatus"));
//        claimDetailViewData.setRemark(object.getString("remark"));
//        claimDetailViewData.setMessage(object.getString("message"));
//        return claimDetailViewData;
//    }
//    public String createReportDetailsInSession() {
//        List<UploadedClaimDetailViewData> listOfUploadedClaimsDetail = mapListFromJsonString(jsonData);
//        session.put("uploadedClaimsDetails", listOfUploadedClaimsDetail);
//        return SUCCESS;
//    }
    @Override
    public void setSession(Map map) {
        this.session = map;
    }
}
