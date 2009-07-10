package chox.web.actions;

import chox.Util.DateHelper;
import java.io.IOException;
import java.io.File;
import chox.Util.FileHelper;
import java.io.FileInputStream;
import chox.model.Attachment;
import chox.model.History;
import chox.model.Claim;
import chox.services.AttachmentService;
import chox.services.HistoryService;
import chox.data.AttachmentCategory;
import chox.model.AttachmentType;
import chox.services.AttachmentTypeService;
import chox.services.GlobalConfigurationService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.sql.SQLException;
import java.util.List;
import net.sf.json.JSONObject;

public class AttachmentAction extends BaseModelAction implements ModelDriven<Attachment>, Preparable {

    private AttachmentService service;
    private HistoryService historyService;
    private GlobalConfigurationService globalConfigurationService;
    private Attachment model;
    private AttachmentTypeService attachmentTypeService;
    
    // FROM JSP
    private File attachmentFile;
    private String remark;
    private String category;
    private String uploadFileName;
    
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getUploadFileName() {
        return this.uploadFileName;
    }
    
    public File getAttachmentFile() {
        return this.attachmentFile;
    }

    public void setAttachmentFile(File attachmentFile) {
        this.attachmentFile = attachmentFile;
    }
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String createNewAttachment() throws Exception {

        try {
            
            if(!FileHelper.isFileValid(this.attachmentFile)){
                this.actionResult = "ERROR : Unknown File Format";
                return SUCCESS;                
            }
            
            List<String> attTypes = attachmentTypeService.getAttachmentTypeCode();
            
            if(!FileHelper.isFileTypeAllow(this.uploadFileName, attTypes)){
                this.actionResult = "ERROR : Invalid File Type";
                return SUCCESS;
            }
            
            int iResult = FileHelper.isFileSizeAllow(this.attachmentFile);
            if(iResult==0){
                this.actionResult = "ERROR : Invalid File";
                return SUCCESS;        
            }else if(iResult<0){
                this.actionResult = "ERROR : File Size is not allowed exceed "+FileHelper.maxFileSize("MB")+" MB";
                return SUCCESS;
            }
            
            if(!processFile(this.attachmentFile)){
                this.actionResult = "ERROR : Unknown Error occured, please try again.";
            }else{
                this.actionResult = "File has been uploaded successfully";
            }
            
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        
        return SUCCESS;
    }
    
    public boolean processFile(File file) throws IOException, SQLException {
        
        boolean bFlag = false;
        
        if(file.canRead()){
            
            String OldFileName = this.uploadFileName;
            String fileType = FileHelper.getFileExtension(OldFileName);
            String newFileName = FileHelper.getNewFileName(OldFileName, false);            
            FileInputStream streamIn = new FileInputStream(file);
            byte fileContent[] = new byte[(int)file.length()];
            streamIn.read(fileContent);

            bFlag = saveAttachement(this.claimId, this.category, newFileName, this.remark,  fileType, fileContent);
            
        }
        
        return bFlag;
        
    }
    
   
    /*
    private boolean processFile(File inputfile) throws IOException, SQLException {

        Boolean bFlag = false;

        GlobalConfiguration gc = globalConfigurationService.getValueByParam("attachment_path");
        String attachmentPath = gc.getValue();

        if (FileHelper.isFileValid(inputfile)) {

            String fileName = FileHelper.getNewFileName(this.uploadFileName);
            String fileType = FileHelper.getFileExtension(fileName);

            // READ INPUT FILE
            FileInputStream streamIn = new FileInputStream(inputfile);

            // CREATE OUTPUTFILE
            File newFile = new File(attachmentPath + fileName);
            newFile.createNewFile();
            FileOutputStream streamOut = new FileOutputStream(newFile);

            int c;
            while ((c = streamIn.read()) != -1) {
                streamOut.write(c);
            }

            streamIn.close();
            streamOut.close();

            bFlag = saveAttachement(this.claimId, this.category, fileName, this.remark, fileType);

        }

        return bFlag;
    }
    */
    
    private Boolean saveAttachement(
            int claimId, 
            String strCategory, 
            String strFileName, 
            String strRemark, 
            String strFileType, 
            byte[] obj) throws IOException {
        
            Boolean bFlag = false;
            
            Claim claim = claimService.getClaim(claimId);
            
            Attachment att = new Attachment();
            att.setFileName(strFileName);
            att.setRemarks(strRemark);
            att.setCategory(strCategory);
            att.setClaim(claim);
            att.setFileType(strFileType);
            att.setFileBuffer(obj);
  
        if (service.saveObj(att)) {
            bFlag = true;
            saveAttachmentHistory(att);
        }

        return bFlag;
    }

    private void saveAttachmentHistory(Attachment obj) {
        
        String strNarrative = String.format("New file is uploaded. [Claim id : %s][Category id : %s][File Name : %s][Attachment id : %s]", obj.getClaim().getId(), obj.getCategory(), obj.getFileName(), obj.getId());
        History his = new History();
        his.setClaim(obj.getClaim());
        his.setIsPublic(true);
        his.setIsSystem(false);
        his.setNarrative(strNarrative);
        his.setProcessDate(DateHelper.getCurrentTimeStamp());
        his.setRuleId("H01");
        his.setType("INFO");
        this.historyService.saveHistory(his);
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_PAYMENT_PACK;
    }

    public Attachment getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Attachment();
        } else {
            model = service.getObject(objectId);
        }
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }
    
    /** PREPARE DROP DOWN LIST **/
    public List<String> getAttachmentCategory() { return AttachmentCategory.getAttachmentCategory(); }
    
    /** SET SERVICES **/
    public void setHistoryService(HistoryService historyService) { this.historyService = historyService; }
    public void setGlobalConfigurationService(GlobalConfigurationService globalConfigurationService) { this.globalConfigurationService = globalConfigurationService; }
    public void setAttachmentService(AttachmentService service) { this.service = service; }
    public void setAttachmentTypeService(AttachmentTypeService attachmentTypeService){ this.attachmentTypeService = attachmentTypeService; }

}
