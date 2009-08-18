package chox.services;

import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.rules.BordereauFileValidation;
import java.io.File;
import junit.framework.Assert;
import org.junit.Test;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XMLUpload_TestFileType extends TestCase{
    private ClassPathXmlApplicationContext ctx;
    private UploadClaimXMLService service = null;
    private String testFilePath;
    
    public XMLUpload_TestFileType() {
        testFilePath = new File("").getAbsolutePath()+"/test/chox/testFile/";
        String[] paths = {"applicationContext.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);        
    } 

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        service = (UploadClaimXMLService) ctx.getBean("uploadClaimXMLService");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        service = null;
    }

    @Test
    public void testCanInitUploadClaimXMLServiceFromSpring()
    {
        service.toString();
        Assert.assertNotNull(service);
    }
    
    /*
     * TEST FILE TYPE
     * ONLY .xml FILE ALLOWED TO UPLOAD
     */
    @Test
    public void testFile_fileType() {

        try
        {
            File testFile = new File(testFilePath+"test_file_type.pdf");
            
            BordereauResult parseResult = service.processClaimXMLFile(testFile, "test_file_type.pdf");
            
            assertEquals(false, parseResult.isValid());
            assertEquals(1, parseResult.getMessage().size());
            assertEquals(BordereauFileValidation.V_FILE_TYPR_ERROR, parseResult.getMessage().get(0));
            
        }
        catch(Exception ex)
        {

        }
    }    
    
    /*
     * TEST FILE TYPE
     * ONLY ALLOWED FILE NOT MORE THAN 10MB TO BE UPLOADED
     */    
    @Test
    public void testFile_fileSize() {

        try
        {
            File testFile = new File(testFilePath+"test_file_size.txt");
            BordereauResult parseResult = service.processClaimXMLFile(testFile, "test_file_size.txt");
            assertEquals(false, parseResult.isValid());
            assertEquals(2, parseResult.getMessage().size());
            assertEquals(BordereauFileValidation.V_FILE_TYPR_ERROR, parseResult.getMessage().get(0));
            assertEquals(BordereauFileValidation.V_FILE_SIZE_ERROR, parseResult.getMessage().get(1));
            
        }
        catch(Exception ex)
        {

        }

    } 
    
}
