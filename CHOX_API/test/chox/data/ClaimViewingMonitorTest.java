/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.data;

import java.util.Set;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Emmanuel
 */
public class ClaimViewingMonitorTest {

    public ClaimViewingMonitorTest() {
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
     * Test of getInstance method, of class ClaimViewingMonitor.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        ClaimViewingMonitor result = ClaimViewingMonitor.getInstance();
        Assert.assertNotNull(result);
    }

    /**
     * Test of ping method, of class ClaimViewingMonitor.
     */
    @Test
    public void testPing() throws InterruptedException {
        System.out.println("ping");
        Integer claimId = 1;
        Integer userId = 1;
        ClaimViewingMonitor monitor = ClaimViewingMonitor.getInstance();
        monitor.ping(claimId, "C", 1, userId);

/*        for (int i = 0; i < 31; i++) {
            Set<Integer> users = monitor.getWhoIsViewing(claimId, "C", 1);
            if (i < 30) {
                Assert.assertEquals(1, users.size());
                Assert.assertEquals(1, users.toArray()[0]);
            } else {
                Assert.assertNull(users);
            }

            Thread.sleep(1000);
        }*/

        monitor.ping(claimId, "I", 2, 2);
        Set<Integer> users = monitor.getWhoIsViewing(claimId, "I", 2);
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(2, users.toArray()[0]);
        //access to claim with same claimId, orgType but different orgId
        users = monitor.getWhoIsViewing(claimId, "I", 3);
        Assert.assertNull(users);
        
        monitor.ping(claimId, "I", 2, 3);
        users = monitor.getWhoIsViewing(claimId, "I", 2);
        Assert.assertEquals(2, users.size());
        Assert.assertEquals(3, users.toArray()[1]);
        
        //access to claim with same claimId, orgId but different org type
        users = monitor.getWhoIsViewing(claimId, "C", 2);
        Assert.assertNull(users);

    }
}