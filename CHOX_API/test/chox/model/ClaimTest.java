/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
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
public class ClaimTest {

    private Session currentSession;
    
    public ClaimTest() {
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
    public void testLoad() throws Exception{
       
        Session currentSession = HibernateUtil.currentSession();
        Supplier s = (Supplier)currentSession.load(Supplier.class, 999);
        Rental r = new Rental();
        r.setSupplierReference("ABC123q");
        r.setRentalStatus("Completed");
        r.setSupplier(s);
        
        currentSession.beginTransaction();
        currentSession.saveOrUpdate(r);
        currentSession.getTransaction().commit();
        
        Rental r2 = (Rental)currentSession.load(Rental.class, r.getId());
        assertNotNull(r2);       

    }


}