package idas.chox.web.actions;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AccessDeniedException;

import net.sf.json.JSONArray;

import idas.chox.core.common.AttachmentCategory;
import idas.chox.core.model.Attachment;
import idas.chox.core.model.AttachmentType;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.AttachmentTypeService;
import idas.chox.core.util.FileHelper;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.AttachmentViewData;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


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
    private boolean notifyTask;

    public boolean isNotifyTask() {
        return notifyTask;
    }

    public void setNotifyTask(boolean notifyTask) {
        this.notifyTask = notifyTask;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Implementation of BaseModelAction">
    @Override
    String getTabName() {
        return TabAccessibility.TAB_PAYMENT_PACK;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
        LOG.debug("Set fileId={}", fileId);
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
        this.uploadFileName = Jsoup.clean(uploadFileName, Whitelist.basic());
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
            LOG.debug("Returning attachments for grid:\n{}\n", jObject.toString());
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";
    }

    public String getAttachments() {

        try {

            List<AttachmentViewData> viewDatas = new ArrayList<>();
            List<Attachment> result = attachmentService.getAttachmentsByClaim(claim.getId());

            for (Attachment attachment : result) {
                viewDatas.add(new AttachmentViewData(attachment));
            }

            this.jObject = JSONArray.fromObject(viewDatas);
            return SUCCESS;
        } catch (Exception ex) {
            LOG.error("Exception thrown getting attachments: {}", ex.getMessage());
            setActionError(formErrorMessage(ex));
            return ERROR;
        }
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String deleteAttachment() {

        try {
            if (model == null) {
                model = loadModel();
                if (model == null) {
                    getActionResponse().AssignMessageResult("Unknown error occurred trying to delete the attachment.");
                    LOG.error("Cannot delete attachment with fileId={} - no model", fileId);
                    return ERROR;
                }
            }

            if (attachmentService.deleteAtatchment(getAuthenticatedUser().getId(), model.getId())) {
                this.getActionResponse().AssignMessageResult("File has been deleted");
            } else {
                LOG.debug("User {} cannot delete attachment {}", getAuthenticatedUser().getDisplayName(), model.getId());
                this.getActionResponse().AssignMessageResult("You do not have the necessary permissions to delete this attachment.");
                setActionError("You do not have the necessary permissions to delete this attachment");
                return ERROR;
            }

        } catch (Exception ex) {
            LOG.error("Exception thrown deleting attachment with userId={}, modelId={}", getAuthenticatedUser().getId(), model.getId());

            if (ex != null) {
                LOG.error("Exception thrown deleting attachment: {}", ex.getMessage());
                this.getActionResponse().AssignMessageResult(ex.getMessage());
            } else {
                LOG.error("Empty Exception thrown deleting attachment!");
                this.getActionResponse().AssignMessageResult("Unknown error occurred trying to delete the attachment.");
            }
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

            fileStream = new ByteArrayInputStream(model.getAttachment().getFileBuffer());
            this.contentDisposition = "filename=" + model.getFileName();
            AttachmentType attachmentType = attachmentTypeService.getAttachmentType(model.getFileType());

            if (attachmentType != null) {
                this.contentType = attachmentType.getMimeType();
            } else {
                this.contentType = "text/html";
            }

        } catch (Exception ex) {
            LOG.warn("Exception thrown exporting attachment: {}", ex.getMessage());
            setActionError("An internal error occurred trying to export this attachment. Please try again. If the problem persists, please contact CHOX Support.");
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
        List attachmentCategory = new ArrayList<>();
        for (String s : AttachmentCategory.getAttachmentCategory()) {
            attachmentCategory.add(new LookupItem(s, s));
        }
        return attachmentCategory;
    }

    public String getIsChoOrIns() {
        String userName;
        if (getIsCHO()) {
            userName = "Insurer";
        } else {
            userName = "CHO";
        }
        return userName;
    }

    public String getWhoCreated() {
        String userName;
        if (getIsCHO()) {
            userName = "CHO";
        } else {
            userName = "Insurer";
        }
        return userName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    public String createNewAttachment() throws Exception {
        try {

            if (!FileHelper.isFileValid(this.attachmentFile)) {
                this.getActionResponse().AddError("Unknown File Format");
                return SUCCESS;
            }
            LOG.info("File '{}' [{}] is valid.", uploadFileName, attachmentFile.getName());
            List<String> attTypes = attachmentTypeService.getAttachmentTypeCode();
            if (!FileHelper.isFileTypeAllow(this.uploadFileName, attTypes)) {
                this.getActionResponse().AddError("Invalid File Type");
                return SUCCESS;
            }
            LOG.debug("File type of file '{}' is allowed.", uploadFileName);

            int iResult = FileHelper.isFileSizeAllow(this.attachmentFile);
            if (iResult == 0) {
                this.getActionResponse().AddError("Invalid File");
                return SUCCESS;
            } else if (iResult < 0) {
                LOG.debug("Attachment File is too big: {}", attachmentFile.length());
                this.getActionResponse().AddError("File Size is exceeded " + FileHelper.maxFileSize("MB") + " MB limit.");
                return SUCCESS;
            }
            LOG.debug("Attachment file '{}' is of write type and size ({})- processing", uploadFileName, attachmentFile.length());
            if (!attachmentFile.canRead()) {
                LOG.warn("Cannot read attachment file: {}", uploadFileName);
                this.getActionResponse().AddError("Unknown Error occurred, please try again.");
            } else {
                InputStream streamIn = new FileInputStream(attachmentFile);

                
                String result = attachmentService.addAttachment(claim, streamIn, uploadFileName, attachmentFile.length(),
                        category, remark, notifyTask, this.getIsInsurer(), this.getWhoCreated());
                if (result != null) {
                    this.getActionResponse().AddError(result);
                } else {
                    updateModelInSession(Arrays.asList(claim));
                    if (notifyTask) {
                        this.getActionResponse().AssignMessageResult("File has been uploaded successfully and " + getIsChoOrIns() + " informed");
                    } else {
                        this.getActionResponse().AssignMessageResult("File has been uploaded successfully");
                    }
                }
            }

        } catch (IOException ex) {
            if (attachmentFile != null) {
                this.getActionResponse().AddError(ex.getMessage());
                LOG.error("IOException thrown creating attachment from file '{}'", uploadFileName, ex);
            } else {
                this.getActionResponse().AddError(ex.getMessage());
                LOG.error("IOException thrown", ex);
            }
            setActionError(formErrorMessage(ex));
            return SUCCESS;
        } catch (Exception ex) {
            if (attachmentFile != null) {
                this.getActionResponse().AddError(ex.getMessage());
                LOG.error("Unknown Exception thrown creating attachment from file '{}'", uploadFileName, ex);
            } else {
                this.getActionResponse().AddError(ex.getMessage());
                LOG.error("Unknown Exception thrown creating attachment", ex);
            }
            setActionError(formErrorMessage(ex));
            return SUCCESS;
        }

        return SUCCESS;
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
            Attachment attachment = (Attachment) baseDataService.get(Attachment.class, getFileId());
            if (attachment != null && claim != null && attachment.getClaim().getId().equals(claim.getId())) {
                return attachment;
            } else {
                LOG.error("Attachment load failed, Attempt to access a attachment that you do not own.");
                throw new AccessDeniedException("Attempt to access a attachment that you do not own.");
            }
        } else {
            return new Attachment();
        }
    }

    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("AttachmentAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
        }
    }
}
