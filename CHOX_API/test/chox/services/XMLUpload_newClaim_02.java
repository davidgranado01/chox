package chox.services;

import chox.model.Bordereau;
import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.model.ClaimResult;
import java.io.File;
import junit.framework.Assert;
import org.junit.Test;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class XMLUpload_newClaim_02 extends TestCase{
    private ClassPathXmlApplicationContext ctx;
    private UploadClaimXMLService service = null;
    private BordereauService bordereauService = null;
    private String testFilePath;
    
    public XMLUpload_newClaim_02() {
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
    
      
}
