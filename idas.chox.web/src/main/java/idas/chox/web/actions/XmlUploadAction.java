/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Bordereau;
import idas.chox.core.services.BordereauService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DocumentHelper;
import idas.chox.core.util.FileHelper;
import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.service.xml.validations.BordereauFileValidation;
import idas.chox.service.xml.validations.BordereauSchemaValidation;
import idas.chox.web.viewdata.BordereauViewData;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import idas.chox.core.services.UploadClaimXMLService;
import idas.chox.core.xmlValidation.BordereauParseStatus;

/**
 *
 * @author seeni
 */
public class XmlUploadAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(XmlUploadAction.class);
    protected static String NEW_UPLOADED_XML_FILE_STATUS = "Waiting to be Processed";
    protected static String NEW_UPLOADED_XML_FILE_DESCRIPTION = "File is waiting to be processed";
    private ChorganisationService chorganisationService;
    private BordereauService bordereauService;
    private UserService userService;
    private JSONArray jObject;
    private boolean uploadFlag;
    private File uploadedFile;
    private String uploadedFileFileName;
    private int bordereauId;
    private UploadClaimXMLService service;
    private BordereauSchemaValidation bordereauSchemaValidation;

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

    public void setUserService(UserService userService) {
        this.userService = userService;
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
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
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

            bordereau.setFileName(uploadedFileFileName);
            bordereau.setStatus(NEW_UPLOADED_XML_FILE_STATUS);

            bordereau.setFileBuffer(fileContent);
            bordereau.setDescription(NEW_UPLOADED_XML_FILE_DESCRIPTION);
            bordereauService.saveBordereau(bordereau);
            this.getActionResponse().AssignMessageResult("File has been uploaded successfully");
            return SUCCESS;

        } else {
            this.getActionResponse().AddError("Unknown File Format");
            return ERROR;
        }

    }

    public String getUploadedFiles() {

        List<BordereauViewData> viewDatas = new ArrayList<BordereauViewData>();
        List<Bordereau> uploadedFileList = bordereauService.getBordereauByUserIdUploadedToday(getAuthenticatedUser());
        for (Bordereau bordereau : uploadedFileList) {
            viewDatas.add(new BordereauViewData(bordereau));
        }
        this.jObject = JSONArray.fromObject(viewDatas);
        return SUCCESS;
    }

    public String processUploadedXmlFile() {


        int totalRecord = 0;
        int totalProcessed = 0;
        FileOutputStream outputStream = null;
        File uplodedFile = new File("temp.xml");
        List<ClaimResult> claimResults = null;
        BordereauResult bordereauResult = new BordereauResult();
        if (bordereauId >= 0) {
            Bordereau bordereau = bordereauService.getBordereauById(bordereauId);
            try {
                outputStream = new FileOutputStream(uplodedFile);
            } catch (FileNotFoundException ex) {
                LOG.debug("File not found");
                this.getActionResponse().AddError("Error occured while reading file. Please report to chox admin.");
                return ERROR;
            }
            try {
                outputStream.write(bordereau.getFileBuffer());
            } catch (IOException ex) {
                LOG.debug("IOException thrown while writing to file");
                this.getActionResponse().AddError("Error occured while writting to file. Please report to chox admin.");
                return ERROR;
            }
            Document document = DocumentHelper.getDocumentFromFile(uplodedFile);
            bordereauSchemaValidation.validate(document, bordereauResult);

            if (bordereauResult.isValid()) {
                try {
                    claimResults = this.service.formClaimResults(document);
                    bordereauResult.setClaimResult(claimResults);
                    totalRecord = claimResults.size();
                } catch (Exception ex) {
                    bordereauResult.setBordereauStatus(BordereauParseStatus.error);
                    bordereauResult.setBordereauParseStatusDescription("Invalid Schema");
                }

                for (ClaimResult claimResult : claimResults) {
                    this.service.doProcessBordereauResult(claimResult);
                    totalProcessed++;
                    LOG.debug("{} of {} claims have been processed", totalRecord, totalProcessed);
                }

                if (totalProcessed >= totalRecord) {
                    bordereauResult.setBordereauStatus(BordereauParseStatus.allUploaded);
                    bordereauResult.setBordereauParseStatusDescription("All claims have been uploaded successfully");
                } else if (totalProcessed < totalRecord && totalProcessed != 0) {
                    bordereauResult.setBordereauStatus(BordereauParseStatus.partialUpload);
                    bordereauResult.setBordereauParseStatusDescription(totalProcessed + " out of " + totalRecord + " claims have been uploaded");
                } else if (totalProcessed == 0) {
                    bordereauResult.setBordereauStatus(BordereauParseStatus.allRejected);
                    bordereauResult.setBordereauParseStatusDescription("All " + totalRecord + " claims have been rejected");
                }

            } else {
                bordereauResult.setBordereauStatus(BordereauParseStatus.error);
                bordereauResult.setBordereauParseStatusDescription("Invalid Schema");
            }
            

        }else{
            this.getActionResponse().AddError("File not found.");
            return ERROR;
        }
        return SUCCESS;
    }
}
