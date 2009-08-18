/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.WebUser;
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
public class UserCacheManagerTest {

    public UserCacheManagerTest() {
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
    public void testCaching() {
        int id = 0;
        UserCacheManager instance = UserCacheManager.getInstance();
        instance.setCacheLifeTime(1000);
        WebUser user = new WebUser();
        user.setId(0);
        instance.putUserToCache(user);        
        WebUser result = instance.getUserFromCache(id);        
        assertNotNull(result);
        
        //test set chache lifetime to 1 sec, and retrieve the object again, it should be expired and return null
        try
        {
            Thread.sleep(1001);
        }
        catch(Exception ex)
        {
            
        }
        assertNull(instance.getUserFromCache(id));      

    }

   
}