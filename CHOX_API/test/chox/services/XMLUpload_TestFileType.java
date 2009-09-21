package chox.services;

import chox.xmlValidation.model.BordereauResult;
import chox.xmlValidation.rules.BordereauFileValidation;
import java.io.File;
import java.io.IOException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml","classpath:applicationContext-services.xml"})
public class XMLUpload_TestFileType {

    @Autowired
    private UploadClaimXMLService service;
    private String testFilePath = "/chox/testFile/";

    /*
     * TEST FILE TYPE
     * ONLY .xml FILE ALLOWED TO UPLOAD
     */
    @Test
    public void testFile_fileType() throws IOException {


        String fileName = "test_file_type.pdf";
        File testFile = new ClassPathResource(testFilePath + fileName).getFile();

        BordereauResult parseResult = service.processClaimXMLFile(testFile, fileName);

        Assert.assertEquals(false, parseResult.isValid());
        Assert.assertEquals(1, parseResult.getMessage().size());
        Assert.assertEquals(BordereauFileValidation.V_FILE_TYPR_ERROR, parseResult.getMessage().get(0));


    }

    /*
     * TEST FILE TYPE
     * ONLY ALLOWED FILE NOT MORE THAN 10MB TO BE UPLOADED
     */
    @Test
    public void testFile_fileSize() throws IOException {


        String fileName = "test_file_size.txt";
        File testFile = new ClassPathResource(testFilePath + fileName).getFile();

        BordereauResult parseResult = service.processClaimXMLFile(testFile, fileName);

        Assert.assertEquals(false, parseResult.isValid());
        Assert.assertEquals(2, parseResult.getMessage().size());
        Assert.assertEquals(BordereauFileValidation.V_FILE_TYPR_ERROR, parseResult.getMessage().get(0));
        Assert.assertEquals(BordereauFileValidation.V_FILE_SIZE_ERROR, parseResult.getMessage().get(1));



    }
}
