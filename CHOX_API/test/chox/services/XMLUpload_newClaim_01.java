package chox.services;

import chox.model.Bordereau;
import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.model.ClaimResult;
import java.io.File;
import junit.framework.Assert;
import org.junit.Test;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XMLUpload_newClaim_01 extends TestCase{
    private ClassPathXmlApplicationContext ctx;
    private UploadClaimXMLService service = null;
    private BordereauService bordereauService = null;
    private String testFilePath;
    
    public XMLUpload_newClaim_01() {
        testFilePath = new File("").getAbsolutePath()+"/test/chox/testFile/";
        String[] paths = {"applicationContext.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);        
    } 

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        service = (UploadClaimXMLService) ctx.getBean("uploadClaimXMLService");
        bordereauService = (BordereauService) ctx.getBean("bordereauService");
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
    @Test
    public void testFile_1_Error() {
    
        String fileName = "UnitTest-NewClaim_01.xml";
        try
        {

            // 1. DELETE OBJECT
            bordereauService.deleteObject(fileName);
            
            // 2. PROCESS THE XML
            File testFile = new File(testFilePath + fileName);
            BordereauResult parseResult = service.processClaimXMLFile(testFile, fileName);
            
            // 3. CHECK XML RESULT
            assertEquals(true, parseResult.isValid());
            assertEquals(0, parseResult.getMessage().size());
            assertEquals(1, parseResult.getClaimResult().size());
            
            // 4. CHECK XML CLAIM RESULT
            ClaimResult claimResult = parseResult.getClaimResult().get(0);
            assertEquals(false, claimResult.isValid());
            assertEquals(false, claimResult.isDataValid());
            assertEquals(4, claimResult.getMessage().size());
            assertEquals("Invalid or incorrect character in 'Managing repair' for 'Claim Header'.".toLowerCase(), claimResult.getMessage().get(0).toLowerCase());
            assertEquals("Invalid or incorrect character in 'Customer's Vehicle Registration' for 'Customer Detail'.".toLowerCase(), claimResult.getMessage().get(1).toLowerCase());
            assertEquals("No 'Third Party's Driver First Name' information supplied for 'Third Party Details'. Please re-submit with this information.".toLowerCase(), claimResult.getMessage().get(2).toLowerCase());
            assertEquals("Invalid or incorrect character in 'Number Days Hire' for 'Vehicle Hire Details'.".toLowerCase(), claimResult.getMessage().get(3).toLowerCase());
            
            // 5. CHECK BORDEREAU RESULT
            bordereau = null;
            bordereau = bordereauService.getObject(fileName);
            assertEquals(fileName, bordereau.getFileName());
            assertEquals("allRejected", bordereau.getStatus());
            
        }catch(Exception ex){

        }
    }   
    */ 

    @Test
    public void testFile_2_Successful() {
    
        String fileName = "UnitTest-NewClaim_02.xml";
        
        try
        {
            // 1. DELETE OBJECT
            bordereauService.deleteObject(fileName);
            
            // 2. PROCESS THE XML
            File testFile = new File(testFilePath + fileName);
            BordereauResult parseResult = service.processClaimXMLFile(testFile, fileName);
            
            // 3. CHECK XML RESULT
            assertEquals(true, parseResult.isValid());
            assertEquals(0, parseResult.getMessage().size());
            assertEquals(1, parseResult.getClaimResult().size());
            
            // 4. CHECK XML CLAIM RESULT
            ClaimResult claimResult = parseResult.getClaimResult().get(0);
            assertEquals(false, claimResult.isValid());
            assertEquals(false, claimResult.isDataValid());
            assertEquals(3, claimResult.getMessage().size());
            
            // assertEquals("Invalid or incorrect character in 'Managing repair' for 'Claim Header'.".toLowerCase(), claimResult.getMessage().get(0).toLowerCase());
            // assertEquals("No 'Third Party's Driver First Name' information supplied for 'Third Party Details'. Please re-submit with this information.".toLowerCase(), claimResult.getMessage().get(1).toLowerCase());
            // assertEquals("Invalid or incorrect character in 'Number Days Hire' for 'Vehicle Hire Details'.".toLowerCase(), claimResult.getMessage().get(2).toLowerCase());
            /*
            // 5. CHECK BORDEREAU RESULT
            bordereau = null;
            bordereau = bordereauService.getObject(fileName);
            assertEquals(fileName, bordereau.getFileName());
            assertEquals("allRejected", bordereau.getStatus());
            */
            
        }catch(Exception ex){

        }
    }
}
