package chox.web.actions;

import chox.Util.FileHelper;
import chox.model.Attachment;
import chox.model.GlobalConfiguration;
import chox.services.AttachmentService;
import chox.services.ClaimService;
import chox.services.GlobalConfigurationService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;
import junit.framework.TestCase;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AttachmentTest extends TestCase {

    private ApplicationContext ctx;
    private GlobalConfigurationService globalConfigurationService;
    private AttachmentService attachmentService;
    private ClaimService claimService;
    
    
    public AttachmentTest() {
        String[] paths = {"applicationContext.xml","applicationContext-services.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);
    }

    @Override
    public void setUp() {
        globalConfigurationService = (GlobalConfigurationService) ctx.getBean("globalConfigurationService");
        attachmentService = (AttachmentService) ctx.getBean("attachmentService");
        
    }
    
    @Override
    public void tearDown() {
    }
    
    @Test
    public void test() throws IOException, SQLException {    
        
        
        GlobalConfiguration gc = globalConfigurationService.getValueByParam("attachment_path");
        String attachmentPath = gc.getValue();
       
        File file = new File(attachmentPath+"\\migration.txt");

        int iResult = FileHelper.isFileSizeAllow(file);
        
        System.out.println("MAX SIZE:"+FileHelper.MAX_FILE_SIZE_ALLOW+" |File Size:"+file.length() + "|KB:" +file.length()/1000+ " |MB:"+file.length()/1000/1024);
       
        if(iResult==0){
            System.out.println("ERROR : Invalid File");
        }else if(iResult<0){
            System.out.println("ERROR : File Size is not allowed exceed "+FileHelper.maxFileSize("MB")+" MB");

        }
            

          /*        
        //AttachmentAction action = new AttachmentAction(504, attachmentService, globalConfigurationService);
        //action.runTest(file);
        */
        
        /*
        GetAttachmentsAction action = new GetAttachmentsAction(504, attachmentService);
        List<Attachment> attas = action.getAttachments();
        
        Attachment atta = attas.get(0);
        ByteArrayInputStream streamIn = new ByteArrayInputStream(atta.getFileBuffer());
        
        File newFile = new File(attachmentPath + ".abc.pdf");
        newFile.createNewFile();
        FileOutputStream streamOut = new FileOutputStream(newFile);

        int c;
        while ((c = streamIn.read()) != -1) {
            streamOut.write(c);
        }

            */
    }
    
    /*
    @Test
    public void testGetAttachmentCategory() throws IOException, SQLException {
        
        String status = "";
        
        GlobalConfiguration gc = globalConfigurationService.getValueByParam("attachment_path");
        String attachmentPath = gc.getValue();
        
        File file = new File(attachmentPath+"\\test.pdf");
        
        
            // String fileName = FileHelper.getNewFileName(file.getName());
            // String fileType = FileHelper.getFileExtension(fileName);
            
        if(file.canRead()){
            
            String OldFileName = file.getName();
            String fileType = FileHelper.getFileExtension(OldFileName);
            String newFileName = FileHelper.getNewFileName(OldFileName);
            
            FileInputStream streamIn = new FileInputStream(file);
            
            // File newFile = new File(attachmentPath + newFileName);
            // newFile.createNewFile();
            // FileOutputStream streamOut = new FileOutputStream(newFile);
            
            
            int c;
            while ((c = streamIn.read()) != -1) {
                streamOut.write(c);
            }

            streamIn.close();
            streamOut.close();
          

 
            
            
        }else{
            
            status = "Invalid File";
            
        }
        
        
            
        System.out.println(">>>>"+status);
        
       
        AttachmentAction ctrl = new AttachmentAction();
        
        
        Boolean bFlag = false;



        if (FileHelper.isFileValid(file)) {

            String fileName = FileHelper.getNewFileName(OldFileName);
            String fileType = FileHelper.getFileExtension(fileName);

            // READ INPUT FILE
            FileInputStream streamIn = new FileInputStream(file);

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
        
    }
    */
    
}

