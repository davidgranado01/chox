/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.Claim;
import chox.services.ChoBandService;
import chox.services.ClaimService;
import chox.services.InvoiceService;
import chox.services.UploadClaimXMLService;
import chox.web.security.MenuAccessibility;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Emmanuel
 */
public class XMLUploaderTest {

    public XMLUploaderTest() {
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
    public void testSetChoBandService() {
        
        File newClaimFile = new File("C:/Project Workplace/Greefinch/choxida/trunk/CHOX_WEB/test/chox/web/textXML/BRE_PASSED.xml");
        
        UploadClaimXMLService uploadClaimXMLService = null;
        ProcessClaimsAction instance = new ProcessClaimsAction();
        
        instance.setUploadClaimXMLService(uploadClaimXMLService);
        instance.setUpload(newClaimFile);
        
        System.out.println(">>>>>"+instance.getActionMessages());
    }

}