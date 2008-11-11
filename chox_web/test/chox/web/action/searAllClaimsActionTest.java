/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.action;

import chox.data.HibernateUtil;
import chox.web.actions.ListAllClaimsAction;
import chox.model.*;
import chox.services.ClaimService;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 *
 * @author Emmanuel
 */
@RunWith(JMock.class)
public class searAllClaimsActionTest {
    
    Mockery context = new JUnit4Mockery();

    public searAllClaimsActionTest() {
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
     * Test of execute method, of class ListAllClaimsAction.
     */
    @Test
    public void testExecute() throws Exception {                       
        
        final ClaimService claimService = context.mock(ClaimService.class);

        ListAllClaimsAction action = new ListAllClaimsAction();
        action.setClaimService(claimService);
                
        // expectations
        context.checking(new Expectations() {{
            oneOf (claimService).listAllClaims();will(returnValue(new ArrayList<Claim>()));
        }});
        
        //execute
        String result = action.execute();
        
         
        // verify
        context.assertIsSatisfied();             
        assertEquals("success", result);        
        assertNotSame(null,action.getResults());

    }
    
    @Test
    public void testHibernate() throws Exception
    {
        Session currentSession = HibernateUtil.currentSession();
        
        Supplier s = (Supplier)currentSession.load(Supplier.class, 999);
        
        assertNotSame(null,s);      
                                         
        Claim c4 = new Claim();
        c4.setTpClaimReference("C4");
        c4.setRentalId(1000001);
        c4.setPolicyHolderName("AhKeong");
        c4.setVehicleRegistration("C48793FG");
        c4.setVehicleManufacturer("Hinda");
        c4.setVehicleModel("Civil 1.8 RX");
        c4.setVehicleClassId(1000000);
        c4.setUsable("y");
        c4.setInsurerCountryId(1000);
        c4.setIncidentDate(java.util.Calendar.getInstance().getTime());
        c4.setPoliceInvolved("y");
        c4.setClaimStatus("Awaiting Authorization");       
        c4.setTpInsurerCountryId(1000);
        c4.setTpVehicleClassId(1000000);
        c4.setProposedRentalClassId(1000000);        
        
       
        currentSession.beginTransaction();
        currentSession.save(c4);
        currentSession.getTransaction().commit();
        
        Claim loadedClaim = (Claim)currentSession.load(Claim.class, c4.getId());
               
        assertEquals(loadedClaim.getPolicyHolderName(),"AhKeong");
        currentSession = HibernateUtil.currentSession();
        currentSession.beginTransaction();
        currentSession.delete(c4);
        currentSession.getTransaction().commit();
        
        Criteria criteria = currentSession.createCriteria(Rental.class);       
        List rentals = criteria.list();
        assertNotSame(null,rentals);
        
        Rental r = new Rental();
        r.setSupplierReference("ABC123q");
        r.setRentalStatus("Completed");
        r.setSupplier(s);

        try{
        currentSession.beginTransaction();
        currentSession.save(r);
        currentSession.getTransaction().commit();
        }
        catch(Exception ex)
        {
           System.console().printf("%1", ex.getMessage()); 
        }
          
        
     
    }

}