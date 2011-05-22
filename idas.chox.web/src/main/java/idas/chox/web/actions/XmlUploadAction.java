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
            Document document = null;
            try {
                document = DocumentHelper.getDocumentFromFile(uploadedFile);
                if (document == null) {
                    LOG.error("Exception thrown in saving file while writing to document : {}", DocumentHelper.FileNotAfileError);
                    this.getActionResponse().AddError("File is not valid file. Please upload again.");
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
//            LOG.debug("uploaded file has been saved successfully {} ", bordereau.getFileName());
            this.getActionResponse().AssignMessageResult("File has been uploaded successfully");
            return SUCCESS;

        } else {
            LOG.error("unknown file format is found ");
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

        List<ClaimResult> claimResults = null;
        List<UploadedXMLClaimsDetail> claimsDetails = new ArrayList<UploadedXMLClaimsDetail>();
        List<String> choReferences = new ArrayList<String>();
        Bordereau bordereau = null;
        if (bordereauId >= 0) {

            bordereau = bordereauService.getBordereauById(bordereauId);
            if (!bordereau.isProcessed() && bordereau.isValid()) {
                Document document = null;
                InputStream inputStream = new ByteArrayInputStream(bordereau.getFileBuffer());
                try {
                    document = DocumentHelper.getDocumentFromFile(inputStream);
                } catch (Exception ex) {
                    LOG.error("Exception thrown while writing to document : {}", ex.getMessage());
                    this.getActionResponse().AddError("Error occured while writting to document. Please report to chox admin.");
                    return ERROR;
                }

                bordereau.setStatus("Processing..");
                bordereau.setDescription("File is being processed on the server");
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

                    LOG.error("Unexpected Error thrown while processing claim in file {}, Error message {}", bordereau.getFileName(), ex.getMessage());
                    LOG.error("total processed record {}, out of {} before error thrown", totalProcessed, totalRecord);
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
                    LOG.error("Invalid schema found in this file : {}", bordereau.getFileName());
                    this.getActionResponse().AddError("Invalid Schema.");
                    return ERROR;
                }
                LOG.error("this file have been processed already: {}", bordereau.getFileName());
                this.getActionResponse().AddError("This file has been processed already.");
                return ERROR;
            }
        } else {
            LOG.error("this file is not found: {}", bordereau.getFileName());
            this.getActionResponse().AddError("File not found.");
            return ERROR;
        }
        bordereau.setProcessed(true);
        this.getActionResponse().AssignMessageResult("File processing completed");
        for (UploadedXMLClaimsDetail claimsDetail : claimsDetails) {
            uploadedXMLClaimsDetailService.saveUploadedXMLClaimsDetail(claimsDetail);
        }
        bordereauService.saveBordereau(bordereau);
        LOG.debug("this file have been processed successfully: {}", bordereau.getFileName());
        session.put("claimsDetails", null);
        return SUCCESS;
    }

    public String getUploadedClaimsDetails() {
        if (bordereauId > 0) {
            Bordereau bordereau = bordereauService.getBordereauById(bordereauId);
            List<UploadedXMLClaimsDetail> claimsDetails = new ArrayList<UploadedXMLClaimsDetail>();
            if (bordereau.isProcessed()) {
                claimsDetails = uploadedXMLClaimsDetailService.getUploadedXMLClaimsDetailByBordereauId(bordereauId);
                if ((claimsDetails.size() != bordereau.getTotalClaims()) && bordereau.getTotalClaims() != 0) {
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
                        LOG.debug("getting claimDetails from session total size is: {}", claimsDetails.size());
                    }
                }
            }
            for (UploadedXMLClaimsDetail claimDetailViewData : claimsDetails) {
                claimsDetailsViewData.add(new UploadedClaimDetailViewData(claimDetailViewData));
            }
            this.jObject = JSONArray.fromObject(claimsDetailsViewData);
            totalCount = this.jObject.size();
            return SUCCESS;
        } else {
            this.getActionResponse().AddError("No file have been selected.");
            return ERROR;
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
