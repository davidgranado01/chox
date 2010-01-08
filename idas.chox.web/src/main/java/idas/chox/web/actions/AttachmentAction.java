package idas.chox.web.actions;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.common.AttachmentCategory;
import idas.chox.core.model.Attachment;
import idas.chox.core.model.AttachmentType;
import idas.chox.core.model.Claim;
import idas.chox.core.services.AttachmentTypeService;
import idas.chox.core.util.FileHelper;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.security.ApplicationAccessibility;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import net.sf.json.JSONObject;

public class AttachmentAction extends BaseModelAction implements ModelDriven<Attachment>, Preparable {

    // <editor-fold defaultstate="collapsed" desc="Member Variables">
    private Attachment model;
    private AttachmentTypeService attachmentTypeService;
    private File attachmentFile;
    private String remark;
    private String category;
    private String uploadFileName;
    private int fileId;
    private InputStream fileStream;
    private String contentDisposition;
    private String contentType;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Properties">
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

    public List<String> getAttachmentCategory() {
        return AttachmentCategory.getAttachmentCategory();
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public InputStream getFileStream() {
        return fileStream;
    }

    public void setFileStream(InputStream fileStream) {
        this.fileStream = fileStream;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Action Methods">
    public String create() throws Exception {

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

        } catch (Exception ex) {
            this.getActionResponse().AddError(ex.getMessage());
        }

        return SUCCESS;
    }

    public String export() {

        if (model == null) {
            return ERROR;
        }

        fileStream = new ByteArrayInputStream(model.getFileBuffer());
        String strContentDisposition = "filename=" + model.getFileName();
        this.setContentDisposition(strContentDisposition);
        AttachmentType attachmentType = attachmentTypeService.getAttachmentType(model.getFileType());

        if (attachmentType != null) {
            this.setContentType(attachmentType.getMimeType());
        } else {
            this.setContentType("text/html");
        }

        return SUCCESS;
    }

    public String delete() {
        try {
            Claim claim = getClaim();
            claim.deleteAttachment(model);
            claimService.updateClaim(claim);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    public String detail()
    {
        return SUCCESS;
    }
// </editor-fold>

    private boolean processFile(File file) throws IOException, SQLException {

        boolean bFlag = false;

        if (file.canRead()) {

            String OldFileName = this.uploadFileName;
            String fileType = FileHelper.getFileExtension(OldFileName);
            String newFileName = FileHelper.getNewFileName(OldFileName, false);
            FileInputStream streamIn = new FileInputStream(file);
            byte fileContent[] = new byte[(int) file.length()];
            streamIn.read(fileContent);

            bFlag = saveAttachement(this.claimId, this.category, newFileName, this.remark, fileType, fileContent);

        }

        return bFlag;

    }

    private Boolean saveAttachement(
            int claimId,
            String strCategory,
            String strFileName,
            String strRemark,
            String strFileType,
            byte[] obj) throws IOException {

        Boolean bFlag = false;

        Claim claim = claimService.getClaim(claimId);

        model.setFileName(strFileName);
        model.setRemarks(strRemark);
        model.setCategory(strCategory);
        model.setFileType(strFileType);
        model.setFileBuffer(obj);

        claim.addAttachment(model);

        claimService.updateClaim(claim);

        return bFlag;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    // <editor-fold defaultstate="collapsed" desc="Services">
    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService) {
        this.attachmentTypeService = attachmentTypeService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Implementation of BaseModelAction">
    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_PAYMENT_PACK;
    }

    public Attachment getModel() {
        return null;
    }

    public void prepare() throws Exception {
        if (getFileId() > 0) {
            model = (Attachment) baseDataService.get(Attachment.class, getFileId());
        } else {
            model = new Attachment();
        }
    }
}
// </editor-fold>

