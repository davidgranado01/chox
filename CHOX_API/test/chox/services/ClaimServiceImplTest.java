/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Claim;
import java.util.List;
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
public class ClaimServiceImplTest {

    public ClaimServiceImplTest() {
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
     * Test of getAllClaims method, of class ClaimServiceImpl.
     */
    @Test
    public void testGetAllClaims() {
        System.out.println("getAllClaims");
        ClaimServiceImpl instance = new ClaimServiceImpl();
        List<Claim> expResult = null;
        List<Claim> result = instance.getAllClaims();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listAllClaims method, of class ClaimServiceImpl.
     */
    @Test
    public void testListAllClaims() {
        System.out.println("listAllClaims");
        ClaimServiceImpl instance = new ClaimServiceImpl();
        List<Claim> expResult = null;
        List<Claim> result = instance.getAllClaims();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}