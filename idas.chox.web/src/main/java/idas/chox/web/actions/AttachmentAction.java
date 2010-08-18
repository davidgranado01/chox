package idas.chox.web.actions;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.common.AttachmentCategory;
import idas.chox.core.model.Attachment;
import idas.chox.core.model.AttachmentType;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.AttachmentTypeService;
import idas.chox.core.util.FileHelper;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.web.viewdata.AttachmentViewData;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;

public class AttachmentAction extends ClaimModelAction<Attachment> {
    private static final Logger LOG = LoggerFactory.getLogger(AttachmentAction.class);

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private int fileId;
    private JSONArray jObject;
    private InputStream fileStream;
    private String contentDisposition;
    private String contentType;
    private AttachmentService attachmentService;
    private AttachmentTypeService attachmentTypeService;
    private File attachmentFile;
    private String remark;
    private String category;
    private String uploadFileName;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Implementation of BaseModelAction">
    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_PAYMENT_PACK;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public String getContentType() {
        return contentType;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public void setAttachmentFile(File attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUploadFileName() {
        return this.uploadFileName;
    }

    public File getAttachmentFile() {
        return this.attachmentFile;
    }

    public String getCategory() {
        return category;
    }

    public String getRemark() {
        return remark;
    }

    public String getJsonArrayData() {
        if (jObject != null) {
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";
    }

    public String getAttachments() {

        try {

            List<AttachmentViewData> viewDatas = new ArrayList<AttachmentViewData>();
            List result = attachmentService.getAttachmentsByClaim(claim.getId());

            for (Object o : result) {
                Map data = (Map) o;
                viewDatas.add(new AttachmentViewData(data));
            }

            this.jObject = JSONArray.fromObject(viewDatas);

            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
            setActionError(formErrorMessage(ex));
            return ERROR;
        }
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String deleteAttachment() {
        LOG.debug("Deleting attachment...");
        try {

            attachmentService.deleteAtatchment(model.getId());
            LOG.debug("Attachment deleted.");
            this.getActionResponse().AssignMessageResult("File has been deleted");

        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
            setActionError(formErrorMessage(ex));
            return ERROR;
        }

        return SUCCESS;
    }

    public String doExportAttachment() {

        try {

            if (model == null) {
                return ERROR;
            }

            fileStream = new ByteArrayInputStream(model.getFileBuffer());
            this.contentDisposition = "filename=" + model.getFileName();
            AttachmentType attachmentType = attachmentTypeService.getAttachmentType(model.getFileType());

            if (attachmentType != null) {
                this.contentType = attachmentType.getMimeType();
            } else {
                this.contentType = "text/html";
            }

        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage());
            setActionError(formErrorMessage(ex));
            return ERROR;
        }

        LOG.debug("Exporting attachment: {} (mime type is '{}'", this.contentDisposition, this.contentType);
        return SUCCESS;
    }

    public List<AttachmentType> getAllowFileTypes() {
        return attachmentTypeService.getAllAttachmentType();
    }

    public String getAllowFileTypeHelpNote() {

        String sAllowFileType = "";

        for (AttachmentType a : attachmentTypeService.getAllAttachmentType()) {
            sAllowFileType += "." + a.getCode() + ", ";
        }

        if (sAllowFileType.length() > 2) {
            sAllowFileType = sAllowFileType.substring(0, sAllowFileType.length() - 2);
        }

        return sAllowFileType;
    }

    public int getMaxFileSize() {
        return FileHelper.MAX_FILE_SIZE_ALLOW;
    }

    public List getAttachmentCategory() {
        List attachmentCategory = new ArrayList<LookupItem>();
        for (String s : AttachmentCategory.getAttachmentCategory()) {
            attachmentCategory.add(new LookupItem(s, s));
        }
        return attachmentCategory;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    public String createNewAttachment() throws Exception {

        try {

            if (!FileHelper.isFileValid(this.attachmentFile)) {
                this.getActionResponse().AddError("Unknown File Format");
                return SUCCESS;
            }

            List<String> attTypes = attachmentTypeService.getAttachmentTypeCode();
            if (!FileHelper.isFileTypeAllow(this.uploadFileName, attTypes)) {
                this.getActionResponse().AddError("Invalid File Type");
                return SUCCESS;
            }

            int iResult = FileHelper.isFileSizeAllow(this.attachmentFile);
            if (iResult == 0) {
                this.getActionResponse().AddError("Invalid File");
                return SUCCESS;
            } else if (iResult < 0) {
                this.getActionResponse().AddError("File Size is not allowed exceed " + FileHelper.maxFileSize("MB") + " MB");
                return SUCCESS;
            }

            if (!processFile(this.attachmentFile)) {
                this.getActionResponse().AddError("Unknown Error occured, please try again.");
            } else {
                this.getActionResponse().AssignMessageResult("File has been uploaded successfully");
            }

        } catch (SQLException ex) {
            if (attachmentFile != null)
                LOG.error("SQL Exception thrown creating attachment from file '{}': {}", uploadFileName, ex.getMessage());
            else
                LOG.error("SQLException thrown: {}", ex.getMessage());
            setActionError(formErrorMessage(ex));
            return ERROR;
        } catch (IOException ex) {
            if (attachmentFile != null)
                LOG.error("IOException thrown creating attachment from file '{}': {}", uploadFileName, ex.getMessage());
            else
                LOG.error("IOException thrown: {}", ex.getMessage());
            setActionError(formErrorMessage(ex));
            return ERROR;
        } catch (Exception ex) {
            if (attachmentFile != null)
                LOG.error("Unknown Exception thrown creating attachment from file '{}': {}", uploadFileName, ex.getMessage());
            else
                LOG.error("Unknown Exception thrown: {}", ex.getMessage());
            setActionError(formErrorMessage(ex));
            return ERROR;
        }

        return SUCCESS;
    }

    private boolean processFile(File file) throws IOException, SQLException {

        boolean bFlag = false;

        if (file.canRead()) {
            String oldFileName = this.uploadFileName;
            String fileType = FileHelper.getFileExtension(oldFileName);
            String newFileName = FileHelper.getNewFileName(oldFileName, false);
            FileInputStream streamIn = new FileInputStream(file);
            byte fileContent[] = new byte[(int) file.length()];
            streamIn.read(fileContent);
            saveAttachement(this.claimId, this.category, newFileName, this.remark, fileType, fileContent);
            bFlag = true;
            streamIn.close();
        }

        return bFlag;

    }

    private void saveAttachement(
            int claimId,
            String strCategory,
            String strFileName,
            String strRemark,
            String strFileType,
            byte[] obj) throws IOException {

        model.setFileName(strFileName);
        model.setRemarks(strRemark);
        model.setCategory(strCategory);
        model.setFileType(strFileType);
        model.setFileBuffer(obj);
        claim.addAttachment(model);
        claimService.updateClaim(claim);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService) {
        this.attachmentTypeService = attachmentTypeService;
    }

    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
    // </editor-fold>

    @Override
    protected Attachment loadModel() {

        if (getFileId() > 0) {
            return (Attachment) baseDataService.get(Attachment.class, getFileId());
        } else {
            return new Attachment();
        }
    }
}
