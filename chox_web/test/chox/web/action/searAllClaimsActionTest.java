/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.action;

import chox.web.actions.ListAllClaimsAction;
import chox.model.Claim;
import chox.services.ClaimService;
import java.util.ArrayList;
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
            oneOf (claimService).getAllClaims();will(returnValue(new ArrayList<Claim>()));
        }});
        
        //execute
        String result = action.execute();
        
        // verify
        context.assertIsSatisfied();             
        assertEquals("success", result);        
        assertNotSame(null,action.getResults());

    }

}