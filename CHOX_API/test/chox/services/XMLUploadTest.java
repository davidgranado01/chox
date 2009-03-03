package chox.services;

import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class XMLUploadTest extends SecureDataService{
    
    public XMLUploadTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testXMLFileUpload() {
        
        UploadClaimXMLServiceImpl instance = new UploadClaimXMLServiceImpl();
        
        
        // UploadClaimXMLServiceImpl instance = (UploadClaimXMLServiceImpl)SpringContextTestFactory.getServiceContext().getBean("uploadClaimXMLService");
        File claimFile = new File("C:/Project Workplace/Greefinch/choxida/trunk/CHOX_API/test/chox/testFile/BRE_PASSED.xml");
        
        try
        {
            ArrayList<XMLParseResult> parseResult = new ArrayList<XMLParseResult>();
            instance.processClaimXMLFile(claimFile, true);
        }
        catch(Exception ex)
        {
            
        }
         
    }  

}
