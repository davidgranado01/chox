package chox.services;

import chox.xmlValidation.model.BordereauResult;
import java.io.File;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class XMLUploadTest{

    @Autowired
    UploadClaimXMLService service;

    @Test
    @Transactional
    public void testFile() {

        //File testFile = new File("C:/Project Workplace/Greefinch/Sherwood/testXML/Demo Data XML.xml");

        try
        {
            String fileName = "20090824-BRETest1.xml";
            String path = "/chox/testFile/20090824-BRETest1.xml";
            File file = new ClassPathResource(path).getFile();
            BordereauResult parseResult = service.processBordereau(file, file.getName());

            // System.out.println(">>>"+parseResult.isStatus());
            // ArrayList<XMLParseResult> parseResult = new ArrayList<XMLParseResult>();
            // instance.processClaimXMLFile(claimFile, true);
        }
        catch(Exception ex)
        {

        }

    }  
    
    /*
    @Test
    public void testFileValidation_corrupted_file() {
        
        File testFile = new File("C:/Project Workplace/Greefinch/Sherwood/testXML/test_corrupted_file.txt");
        ParseResult parseResult = service.processClaimXMLFile(testFile, testFile.getName());
        
        // EXPECTED RESULT
        boolean expResult = false;
        List<String> expErrMsg = new ArrayList<String>();
        expErrMsg.add(fileValidation.V_FILE_ERROR);
        expErrMsg.add(fileValidation.V_FILE_TYPR_ERROR);
        
        // RESULT
        boolean oResult = parseResult.isStatus();
        List<String> oErrMsg = parseResult.getMessage();
        
        // testErrMsg("testFileValidation_corrupted_file", oErrMsg);
        
        // COMPARING
        assertEquals(expResult, oResult);
        assertEquals(expErrMsg.size(), oErrMsg.size());
        
    }   
    
    @Test
    public void testFileValidation_exceed_file_size() {
        
        File testFile = new File("C:/Project Workplace/Greefinch/Sherwood/testXML/test_file_size.txt");
        ParseResult parseResult = uploadClaimXMLService.processClaimXMLFile(testFile, testFile.getName());
        
        // EXPECTED RESULT
        boolean expResult = false;
        List<String> expErrMsg = new ArrayList<String>();
        expErrMsg.add(fileValidation.V_FILE_SIZE_ERROR);
        expErrMsg.add(fileValidation.V_FILE_TYPR_ERROR);
        
        // RESULT
        boolean oResult = parseResult.isStatus();
        List<String> oErrMsg = parseResult.getMessage();
        
        // testErrMsg("testFileValidation_exceed_file_size", oErrMsg);
                
        // COMPARING
        assertEquals(expResult, oResult);
        assertEquals(expErrMsg.size(), oErrMsg.size());
        
    } 
   
    @Test
    public void testFileValidation_incorrect_type() {
        
        System.out.println(">>>"+uploadClaimXMLService);
        
        File testFile = new File("C:/Project Workplace/Greefinch/Sherwood/testXML/test_file_type.pdf");
        ParseResult parseResult = uploadClaimXMLService.processClaimXMLFile(testFile, testFile.getName());
        
        // EXPECTED RESULT
        boolean expResult = false;
        List<String> expErrMsg = new ArrayList<String>();
        expErrMsg.add(fileValidation.V_FILE_TYPR_ERROR);
        
        // RESULT
        boolean oResult = parseResult.isStatus();
        List<String> oErrMsg = parseResult.getMessage();
        
        // testErrMsg("testFileValidation_incorrect_type", oErrMsg);
                
        // COMPARING
        assertEquals(expResult, oResult);
        assertEquals(expErrMsg.size(), oErrMsg.size());
    }
    
    @Test
    public void testFileValidation_valid_file() {
        
        
        File testFile = new File("C:/Project Workplace/Greefinch/Sherwood/testXML/Demo Data XML.xml");
        // UploadClaimXMLServiceImpl instance = new UploadClaimXMLServiceImpl(testFile, testFile.getName());
        ParseResult parseResult = uploadClaimXMLService.processClaimXMLFile(testFile, testFile.getName());
        
        //BordereauServiceImpl bordereauService = new BordereauServiceImpl();
       
        // EXPECTED RESULT
        boolean expResult = true;
        List<String> expErrMsg = new ArrayList<String>();
        
        // RESULT
        boolean oResult = parseResult.isStatus();
        List<String> oErrMsg = parseResult.getMessage();
        
        // testErrMsg("testFileValidation_incorrect_type", oErrMsg);

        // COMPARING
        assertEquals(expResult, oResult);
        assertEquals(expErrMsg.size(), oErrMsg.size());
        
    }

    @Test
    public void testFile() {
        
        File testFile = new File("C:/Project Workplace/Greefinch/Sherwood/testXML/Demo Data XML.xml");
        
        try
        {

            BordereauResult parseResult = service.processClaimXMLFile(testFile, testFile.getName());
            
            // System.out.println(">>>"+parseResult.isStatus());
            // ArrayList<XMLParseResult> parseResult = new ArrayList<XMLParseResult>();
            // instance.processClaimXMLFile(claimFile, true);
        }
        catch(Exception ex)
        {
            
        }
         
    }  
    
    private void testErrMsg(String f, List<String> e){
        
        for(String s : e){
            System.out.println(f + " >>>>>> "+s);
        }
    }
    */
}
