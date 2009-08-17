/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.WebUser;
import junit.framework.Assert;
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
public class UserServiceImplTest {

    public UserServiceImplTest() {
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
     * Test of getObject method, of class UserServiceImpl.
     */
    @Test
    public void testGetObject() {
        System.out.println("getObject");
        int id = 1;
        UserServiceImpl instance = new UserServiceImpl();
        WebUser user = new WebUser();

        WebUser result = instance.getObject(id);
        Assert.assertNotNull(result);

    }

}