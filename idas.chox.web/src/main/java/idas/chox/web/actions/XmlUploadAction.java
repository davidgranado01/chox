package idas.chox.web.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;


import idas.chox.core.model.Bordereau;
import idas.chox.core.model.BordereauWithoutFile;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.BordereauService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.services.UploadedXMLClaimsDetailService;
import idas.chox.core.util.FileHelper;
import idas.chox.web.viewdata.BordereauViewData;
import idas.chox.web.viewdata.UploadedClaimDetailViewData;

/**
 *
 * @author seeni
 */
public class XmlUploadAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(XmlUploadAction.class);
    private ChorganisationService chorganisationService;
    private BordereauService bordereauService;
    private UploadedXMLClaimsDetailService uploadedXMLClaimsDetailService;
    private String jObject;
    private boolean uploadFlag;
    private File uploadedFile;
    private String uploadedFileFileName;
    private int bordereauId;
    private UploadClaimXMLService service;
    private String sort;
    private String dir;
    private int days;
    private int start;
    private int limit;
    private int totalCount;
    private String jsonData;
    private InputStream excelStream;
    private List<UploadedClaimDetailViewData> claimsDetailsViewData = new ArrayList<>();
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

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
        this.uploadedFileFileName = Jsoup.clean(fileName, Whitelist.basic());
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
        } else if (this.getIsInsurer()) {
            uploadFlag = getAuthenticatedUser().getInsurer().isInvoiceUploadEnabled() || getAuthenticatedUser().getInsurer().isClaimUploadEnabled();
        }
        return uploadFlag;
    }

    
    public String getJsonArrayData() {
        if (jObject != null) {
            return "{totalCount:" + totalCount + ",results:" + jObject + "}";
        }
        return "";
    }

    private static String getExtension(String fileName) {
        int pos = fileName.lastIndexOf('.');
        return fileName.substring(pos);
    }

    @Override
    public String execute() throws Exception {
        return SUCCESS;
    }

    public String uploadNewClaimsFile() {


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
        if (extension.matches("\\.xml")) {

            if (this.service.saveUploadedFile(uploadedFile, uploadedFileFileName)) {
                this.getActionResponse().AssignMessageResult(this.service.getSuccessMessage());
                return SUCCESS;
            } else {
                this.getActionResponse().AddError(this.service.getErrorMessage());
                return ERROR;
            }

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
        List<BordereauViewData> viewDatas = new ArrayList<>();
        SearchResult searchResult = bordereauService.getUploadedFiles(getAuthenticatedUser(), defaultDays, sort, dir, start, limit);
        List<BordereauWithoutFile> uploadedFileList = searchResult.getResult();
        for (BordereauWithoutFile bordereau : uploadedFileList) {
            viewDatas.add(new BordereauViewData(bordereau));
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            jObject = mapper.writeValueAsString(viewDatas);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting UploadedFiles to json string.");
            jObject = null;
        }
        totalCount = searchResult.getTotalCount();
        return SUCCESS;
    }

//    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public String processUploadedXmlFile() {
            LOG.info("Process bordereau request for file with id={}", bordereauId);
            if (getSession().get("claimsDetails") != null) {
                this.getActionResponse().AddError("Please wait until the previous Bordereau processing request has completed.");
                return ERROR;
            }
            if (this.service.processFile(bordereauId, getSession())) {
                this.getActionResponse().AssignMessageResult(this.service.getSuccessMessage());
                synchronized (getSessionLock()) {
                    getSession().put("claimsDetails", null);
                }
                return SUCCESS;
            } else {
                this.getActionResponse().AddError(this.service.getErrorMessage());
                synchronized (getSessionLock()) {
                    getSession().put("claimsDetails", null);
                }
                return ERROR;
            }


    }

    public String getUploadedClaimsDetails() {
        if (bordereauId > 0) {
            Bordereau bordereau = bordereauService.getBordereauById(bordereauId);
            int userOrgId, bordereauOrgId;
            if (getAuthenticatedUser().isAnInsurer()) {
                userOrgId = getAuthenticatedUser().getInsurer().getId();
                bordereauOrgId = bordereau.getCreatedBy().getInsurer().getId();
            } else {
                userOrgId = getAuthenticatedUser().getChorganisation().getId();
                bordereauOrgId = bordereau.getCreatedBy().getChorganisation().getId();
            }
            if (userOrgId == bordereauOrgId) {
                    List<UploadedXMLClaimsDetail> claimsDetails = null;
                    if (bordereau.isProcessed()) {
                        claimsDetails = uploadedXMLClaimsDetailService.getUploadedXMLClaimsDetailByBordereauId(bordereauId);
                        LOG.debug("getting claimDetails from databse total size is: {}", claimsDetails.size());
                    } else {
                        LOG.debug("Synchronizing on session");
                        synchronized (getSessionLock()) {
                            if (getSession().containsKey("claimsDetails") && getSession().get("claimsDetails") != null) {
                                claimsDetails = (List<UploadedXMLClaimsDetail>) getSession().get("claimsDetails");
                            }
                        }
                        if (claimsDetails == null) { // Should not happen!
                            claimsDetails = new CopyOnWriteArrayList<>();
                            LOG.warn("No claimsDetails in session - empty list created.");
                        }
                        LOG.debug("Finished synchronizing on session");
                    }
                    for (UploadedXMLClaimsDetail claimDetailViewData : claimsDetails) {
                        claimsDetailsViewData.add(new UploadedClaimDetailViewData(claimDetailViewData));
                    }
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        jObject = mapper.writeValueAsString(claimsDetailsViewData);
                    } catch (JsonProcessingException ex) {
                        LOG.error("Error converting claimsDetailsViewData to json string.");
                        jObject = null;
                    }
                    totalCount = this.claimsDetailsViewData.size();
                    return SUCCESS;
            } else {
                LOG.error("User trying to access bordereau of different org : file name='{}', user name='{}', user org='{}', bordereau org='{}'",
                        new Object[]{bordereau.getFileName(), getAuthenticatedUser().getUserName(), userOrgId, bordereauOrgId});
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
            Integer userOrgId, bordereauOrgId;
            if (getAuthenticatedUser().isAnInsurer()) {
                userOrgId = getAuthenticatedUser().getInsurer().getId();
                bordereauOrgId = bordereau.getCreatedBy().getInsurer().getId();
            } else {
                userOrgId = getAuthenticatedUser().getChorganisation().getId();
                bordereauOrgId = bordereau.getCreatedBy().getChorganisation().getId();
            }
            
            if (userOrgId.equals(bordereauOrgId)) {
                if (bordereau.isProcessed()||bordereau.isBeingProcessed()) {
                    this.getActionResponse().AddError("Sorry - a processed file cannot be deleted.");
                    return ERROR;
                } else {
                    if (this.bordereauService.deleteBordereau(bordereau)) {
                        this.getActionResponse().AssignMessageResult("File removed successfully.");
                        return SUCCESS;
                    } else {
                        this.getActionResponse().AddError("An unexpected error occurred while deleting this file. Please report to CHOX support.");
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
        try {
            byte[] b;
            InputStream templateIS = new ClassPathResource("/reports/uploadedClaimDetailsTemplate.xls").getInputStream();
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
                    return SUCCESS;
                } else {
                    LOG.warn("Error response received from the getUploadedClaimsDetails method.");
                    setErrorMessage("An unexpected error occurred while generating the report. Please report to chox support.");
                    return ERROR;
                }
            } else {
                LOG.error("No file(bordereauId) has been selected/provided while trying to export 'uploaded claims details'.");
                setErrorMessage("No record have been selected.");
                return ERROR;
            }
        } catch (IOException | ParsePropertyException | InvalidFormatException ex) {
            LOG.error("Error Generating Report : ", ex);
            setErrorMessage("Error Generating Report. Please report to chox support.");
            return ERROR;
        }
    }
}
