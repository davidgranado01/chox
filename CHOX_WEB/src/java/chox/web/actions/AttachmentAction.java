package chox.web.actions;

import chox.Util.DateHelper;
import java.io.IOException;
import java.io.File;
import chox.Util.FileHelper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import chox.model.Attachment;
import chox.model.History;
import chox.model.Claim;
import chox.model.GlobalConfiguration;
import chox.services.AttachmentService;
import chox.services.HistoryService;
import chox.data.AttachmentCategory;
import chox.services.GlobalConfigurationService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.List;
import net.sf.json.JSONObject;

public class AttachmentAction extends BaseModelAction implements ModelDriven<Attachment>, Preparable {

    private AttachmentService service;
    private File file;
    private String remark;
    private String uploadFileName;
    private String category;
    private Attachment model;
    private HistoryService historyService;
    private GlobalConfigurationService globalConfigurationService;
    private AttachmentService attachmentService;

    public void setAttachmentService(AttachmentService service) {
        this.service = service;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getUploadFileName() {
        return this.uploadFileName;
    }

    public File getAttachmentFile() {
        return this.file;
    }

    public void setAttachmentFile(File file) {
        this.file = file;
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

    public List<String> getAttachmentCategory() {
        return AttachmentCategory.getAttachmentCategory();
    }

    public static void main(String[] args) throws IOException {
        File inputfile = new File("C://Users//Carlson//Desktop//TestDataXML//bankUnitSelection.jpg");
        AttachmentAction thisCtrl = new AttachmentAction();
        thisCtrl.processFile(inputfile);
    }

    public String createNewAttachment() throws Exception {

        try {
            processFile(this.file);
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    private boolean processFile(File inputfile) throws IOException {

        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP1" + new File(".").getAbsolutePath());
        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP2" + new File(".").getCanonicalPath());
        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP3" + new File("..").getAbsolutePath());
        System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP4" + new File("..").getCanonicalPath());
        Boolean bFlag = false;

        GlobalConfiguration gc = globalConfigurationService.getValueByParam("attachement_path");
        String attachmentPath = gc.getValue();

        if (FileHelper.isFileValid(inputfile)) {

            String fileName = FileHelper.getNewFileName(this.uploadFileName);
            String fileType = FileHelper.getFileExtension(inputfile);

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

    private Boolean saveAttachement(int claimId, String strCategory, String strFileName, String strRemark, String strFileType) {

        Boolean bFlag = false;        
        Claim claim = claimService.getClaim(claimId);

        Attachment att = new Attachment();
        att.setClaim(claim);
        att.setCategory(strCategory);
        att.setFileName(strFileName);
        att.setRemarks(strRemark);
        att.setFileType(strFileType);

        if (attachmentService.saveObj(att)) {
            bFlag = true;
            saveAttachmentHistory(att);
        }

        return bFlag;
    }

    private void saveAttachmentHistory(Attachment obj) {
        String strNarrative = String.format("New file is uploaded. [Supplier Ref : %s][Category id : %s][File Name : %s][Attachment id : %s]", obj.getClaim().getChoReference(), obj.getCategory(), obj.getFileName(), obj.getId());
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

    public void setHistoryService(HistoryService historyService) {
        this.historyService = historyService;
    }

}
