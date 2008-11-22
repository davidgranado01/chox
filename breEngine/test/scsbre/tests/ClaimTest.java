/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package scsbre.tests;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Derm
 */
public class ClaimTest {

    public ClaimTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Test
    public void TestStuff(){
        
           // BigDecimal hireNetMinusExtras = claim.getInvoice().getHireNet().subtract(exCalcHelper.getTotalExtras());
                    //return hireNetMinusExtras.divide(new BigDecimal(claim.getHireDetail().getNumberOfHireDays()));
        
       BigDecimal x = new BigDecimal(1880);
       BigDecimal i = new BigDecimal(6);
       
       BigDecimal y = x.divide(i,4,1);
       
       System.out.println(y.doubleValue());
        
    }
    

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

}