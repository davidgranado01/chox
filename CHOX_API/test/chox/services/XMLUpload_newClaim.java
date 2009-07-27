package chox.services;

import chox.xmlValidation.model.BordereauResult;
import java.io.File;
import junit.framework.Assert;
import org.junit.Test;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XMLUpload_newClaim extends TestCase{
    private ClassPathXmlApplicationContext ctx;
    private UploadClaimXMLService service = null;
    private String testFilePath;
    
    public XMLUpload_newClaim() {
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
    
    @Test
    public void testFile() {

        try
        {
            File testFile = new File(testFilePath+"UnitTest-NewClaim.xml");
            BordereauResult parseResult = service.processClaimXMLFile(testFile, "UnitTest-NewClaim.xml");
            
            // STATUS PASS
            assertEquals(true, parseResult.isValid());
            // assertEquals(1, parseResult.getMessage().size());
            
            for(String s : parseResult.getMessage()){
                System.out.println("SSS"+s);
            }
            
            // assertEquals(BordereauFileValidation.V_FILE_TYPR_ERROR, parseResult.getMessage().get(0));
        }
        catch(Exception ex)
        {

        }

    }      
}
