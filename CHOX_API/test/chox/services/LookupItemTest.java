/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import java.util.List;
import org.junit.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author emmanuel
 */
public class LookupItemTest {
    private ClassPathXmlApplicationContext ctx;
    private LookupService service;
    public LookupItemTest()
    {
        String[] paths = {"applicationContext.xml"};
        ctx = new ClassPathXmlApplicationContext(paths);
        service = (LookupService) ctx.getBean("lookupService");
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
    public void testCanGetAllVehicleClass() {
        
        Integer i;

        //VehicleClasses
        for(i = 0; i <= 1000; i++)
        {
             List result1 = service.getVehicleClasses();
             Assert.assertNotNull(result1);
        }      
    }
/*
    @Test
    public void testCanGetAllLob() {

        Integer i;
      //LineOfBusinesses
        for(i = 0; i <= 1000; i++)
        {
             List result1 = service.getAllLineOfBusinesses();
             Assert.assertNotNull(result1);
        }
    }
*/
    @Test
    public void testCanGetAllSupplier() {

        Integer i;

        //AllSuppliers
        for(i = 0; i <= 1000; i++)
        {
             List result1 = service.getAllSuppliers();
             Assert.assertNotNull(result1);
        }
    }

    @Test
    public void testCanGetAllInsurers() {

        Integer i;

        //AllInsurers
        for(i = 0; i <= 1000; i++)
        {
             List result1 = service.getAllInsurers();
             Assert.assertNotNull(result1);
        }
    }

}
