/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;
import chox.data.SecurityInfoProvider;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import org.hibernate.SessionFactory;
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
    
   
    /**
     * Test of getUserFromCache method, of class UserCacheManager.
     */
    @Test
    public void testXMLFileUpload() {
        
        
        UploadClaimXMLServiceImpl instance = new UploadClaimXMLServiceImpl();
        
        SecurityInfoProvider securityInfoProvider = null;
        SessionFactory sessionFactory = null;
        
        ClaimServiceImpl claimService = new ClaimServiceImpl();
        claimService.setSecurityInfoProvider(securityInfoProvider);
        claimService.setSessionFactory(sessionFactory);
        
        instance.setSessionFactory(sessionFactory);
        instance.setSecurityInfoProvider(securityInfoProvider);
        
        
        instance.setClaimService(claimService);
        instance.setAuditTrailService(new AuditTrailServiceImpl());
        instance.setChoBandService(new ChoBandServiceImpl());
        instance.setChorganisationService(new ChorganisationServiceImpl());
        instance.setCustomerService(new CustomerServiceImpl());
        instance.setEngineerReportService(new EngineerReportServiceImpl());
        instance.setHireMonitoringDetailService(new HireMonitoringDetailServiceImpl());
        instance.setHireMonitoringEcdService(new HireMonitoringEcdServiceImpl());
        instance.setHistoryService(new HistoryServiceImpl());
        instance.setIncidentService(new IncidentServiceImpl());
        instance.setInjuryService(new InjuryServiceImpl());
        instance.setInsurerAlliasService(new InsurerAlliasServiceImpl());
        instance.setInvoiceService(new InvoiceServiceImpl());
        instance.setSolicitorService(new SolicitorServiceImpl());
        instance.setThirdPartyService(new ThirdPartyServiceImpl());
        instance.setVehicleClassService(new VehicleClassServiceImpl());
        instance.setVehicleHireService(new VehicleHireServiceImpl());
        instance.setWitnessService(new WitnessServiceImpl());

            
        
        File claimFile = new File("C:/Project Workplace/Greefinch/choxida/trunk/CHOX_API/test/chox/testFile/BRE_PASSED.xml");
        
        try
        {
            
            ArrayList<XMLParseResult> parseResult = new ArrayList<XMLParseResult>();
            parseResult = instance.processXML(claimFile, true);
        

        
        }
        catch(Exception ex)
        {
            
        }
        

    }  
}
