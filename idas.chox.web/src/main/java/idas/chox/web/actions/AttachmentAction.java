package idas.chox.web.actions;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONArray;

import idas.chox.core.common.AttachmentCategory;
import idas.chox.core.model.Attachment;
import idas.chox.core.model.AttachmentFile;
import idas.chox.core.model.AttachmentType;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.Task;
import idas.chox.core.services.AttachmentService;
import idas.chox.core.services.AttachmentTypeService;
import idas.chox.core.services.TaskService;
import idas.chox.core.services.UserService;
import idas.chox.core.util.DateHelper;
import idas.chox.core.util.FileHelper;
import idas.chox.service.security.TabAccessibility;
import idas.chox.service.workflow.activities.ActivityEvent;
import idas.chox.web.viewdata.AttachmentViewData;

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
    private TaskService taskService;
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

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
            LOG.debug("Returning attachments for grid:\n{}\n", jObject.toString());
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";
    }

    public String getAttachments() {

        try {

            List<AttachmentViewData> viewDatas = new ArrayList<AttachmentViewData>();
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

            LOG.debug("Deleting attachment (model='{}')...", model.getClass());
            if (attachmentService.deleteAtatchment(getAuthenticatedUser().getId(), model.getId())) {
                LOG.debug("Attachment deleted.");
                this.getActionResponse().AssignMessageResult("File has been deleted");
            } else {
                LOG.info("User {} cannot delete attachment {}", getAuthenticatedUser().getDisplayName(), model.getId());
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
            LOG.error("Exception thrown exporting attchment: {}", ex.getMessage());
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
            LOG.info("File '{}' is valid.", attachmentFile.getName());
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
            if (!processFile(this.attachmentFile)) {
                this.getActionResponse().AddError("Unknown Error occurred, please try again.");
            } else {
               activityEventGenerator.generate(claim, model, ActivityEvent.ATTACHMENT_UPLOADED_EVENT);
               if (notifyTask) {
                    Task task = new Task();
                    task.setComplete(Boolean.FALSE);
                    task.setDescription("The " + getWhoCreated() + " has uploaded the following attachment '" + this.category + "' which requires review.");
                    task.setDueDate(DateHelper.getCurrentDateTime());
                    task.setType("Attachment");
                    task.setVisibility(3);
                    task.setRaisedBy(userService.findByUserName("system"));
                    task.setInsurer(getIsInsurer());
                    task.setClaim(claim);
                    taskService.createNewTask(task);
                    this.getActionResponse().AssignMessageResult("File has been uploaded successfully and " + getIsChoOrIns() + " informed");
                } else {
                    this.getActionResponse().AssignMessageResult("File has been uploaded successfully");
                }

            }

        } catch (SQLException ex) {
            if (attachmentFile != null) {
                LOG.debug("SQL Exception thrown creating attachment from file '{}': {}", uploadFileName, ex.getMessage());
                this.getActionResponse().AddError(ex.getMessage());
            } else {
                this.getActionResponse().AddError(ex.getMessage());

                LOG.debug("SQLException thrown: {}", ex.getMessage());
            }
            setActionError(formErrorMessage(ex));

            // SUCCESS IS RETURNED EVENTHOUGH ERROR OCCURRED BECAUSE THERE IS NO ERROR MAPED IN STRUTS AND IT'S A AJAX CALL NO NEED TO MAP ERROR PAGE
            return SUCCESS;
        } catch (IOException ex) {
            if (attachmentFile != null) {
                this.getActionResponse().AddError(ex.getMessage());
                LOG.debug("IOException thrown creating attachment from file '{}': {}", uploadFileName, ex.getMessage());
            } else {
                this.getActionResponse().AddError(ex.getMessage());
                LOG.debug("IOException thrown: {}", ex.getMessage());
            }
            setActionError(formErrorMessage(ex));
            return SUCCESS;
        } catch (Exception ex) {
            if (attachmentFile != null) {
                this.getActionResponse().AddError(ex.getMessage());
                LOG.debug("Unknown Exception thrown creating attachment from file '{}': {}", uploadFileName, ex.getMessage());
            } else {
                this.getActionResponse().AddError(ex.getMessage());
                LOG.debug("Unknown Exception thrown creating attachment: {}", ex.getMessage());
            }
            setActionError(formErrorMessage(ex));
            return SUCCESS;
        }

        return SUCCESS;
    }

    private boolean processFile(File file) throws IOException, SQLException {

        boolean bFlag = false;

        if (file.canRead()) {
            LOG.debug("Can read file '{}' of length {}", file.getName(), file.length());
            String oldFileName = this.uploadFileName;
            String fileType = FileHelper.getFileExtension(oldFileName);
            String newFileName = FileHelper.getNewFileName(oldFileName, false);
            LOG.debug("Processing file {} of type {}", oldFileName, fileType);
            FileInputStream streamIn = new FileInputStream(file);
            byte fileContent[];
            try {
                fileContent = new byte[safeLongToInt(file.length())];
                streamIn.read(fileContent);
                streamIn.close();
                LOG.debug("Saving attachment {} for claimId {}", newFileName, this.claimId);
                saveAttachement(this.category, newFileName, this.remark, fileType, fileContent);
                bFlag = true;
                LOG.debug("Attachment saved.");
            } catch (Exception ex) {
                LOG.error("Error processing file with length={}: ", file.length(), ex);
                return bFlag;
            }
        }

        return bFlag;

    }

    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    private void saveAttachement(
            String strCategory,
            String strFileName,
            String strRemark,
            String strFileType,
            byte[] obj) throws IOException {

        model.setFileName(strFileName);
        model.setRemarks(strRemark);
        model.setCategory(strCategory);
        model.setFileType(strFileType);
        claim.addAttachment(model);
        claim.setNoAttachments(claim.getNoAttachments()+1);
        LOG.debug("Saving claim for the 1st time...");
        claimService.updateClaim(claim);
        AttachmentFile aFile = new AttachmentFile();
        aFile.setFileBuffer(obj);
        aFile.setAttachment(model);
        model.setAttachment(aFile);
        LOG.debug("Saving claim for the 2nd time...");
        claimService.updateClaim(claim);
        updateModelInSession(Arrays.asList(claim));
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
            LOG.debug("Loading Attachment model with fileId={}", getFileId());
            return (Attachment) baseDataService.get(Attachment.class, getFileId());
        } else {
            LOG.debug("No fileId(={}), returning new Attachment", getFileId());
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
            LOG.debug("AttachmentAction validate success");
        }
        else {
            LOG.debug(" AttachmentAction validation not done as claim is null");
        }
    }
}
