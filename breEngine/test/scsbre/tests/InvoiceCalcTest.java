package scsbre.tests;

import java.math.BigDecimal;
import org.junit.*;
import scsbre.engine.util.CalcHelper;
import static org.junit.Assert.*;
import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.IInvoiceInfo;
import scsbre.tests.sample.InvoiceInfo;

public class InvoiceCalcTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void TestIvoiceCalculatorWithZeroValues() {
        
        InvoiceInfo inv = new InvoiceInfo();

        inv.setHireNet(BigDecimal.ZERO);
        inv.setHireVat(BigDecimal.ZERO);
        inv.setHireGross(BigDecimal.ZERO);

        inv.setRepairNet(BigDecimal.ZERO);
        inv.setRepairVat(BigDecimal.ZERO);
        inv.setRepairGross(BigDecimal.ZERO);

        inv.setEngineerFeeVat(BigDecimal.ZERO);
        inv.setEngineerFeeNet(BigDecimal.ZERO);
        inv.setEngineerFeeGross(BigDecimal.ZERO);

        inv.setStorageRecoveryGross(BigDecimal.ZERO);
        inv.setStorageRecoveryNet(BigDecimal.ZERO);
        inv.setStorageRecoveryVat(BigDecimal.ZERO);

        inv.setTotalNet(BigDecimal.ZERO);
        inv.setTotalVat(BigDecimal.ZERO);
        inv.setTotalGross(BigDecimal.ZERO);

        inv.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        inv.setDeductionForClaimsHandlingFee(BigDecimal.ZERO);
        inv.setDiscount(BigDecimal.ZERO);
        inv.setPenaltyCharge(BigDecimal.ZERO);
        inv.setTotalToPay(BigDecimal.ZERO);

        InvoiceCalcHelper helper = InvoiceCalcHelper.getInstance(inv);
        
        assertTrue(helper.getCalculatedHireVat().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(helper.getCalculatedHireGross().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(helper.getCalculatedRepairVat().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(helper.getCalculatedRepairGross().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(helper.getCalculatedTotalNet().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(helper.getCalculatedTotalVat().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(helper.getCalculatedTotalGross().compareTo(BigDecimal.ZERO) == 0);
        assertTrue(helper.getCalculatedTotalToPay().compareTo(BigDecimal.ZERO) == 0);
    }
    
    @Test
    public void TestEqualToFormula(){
    
        BigDecimal breCount = new BigDecimal("13.80");
        BigDecimal uInput = new BigDecimal("0.00");
        Boolean bFlag = false;
        
        uInput = new BigDecimal("12.80");
        assertFalse(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("12.90");
        assertFalse(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.00");
       assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.10");
       assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.20");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.30");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.40");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.50");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.60");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.70");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.80");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("13.90");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("14.00");
        assertTrue(CalcHelper.EqualTo(uInput, breCount));

        uInput = new BigDecimal("14.10");
        assertFalse(CalcHelper.EqualTo(uInput, breCount));
        
        uInput = new BigDecimal("14.20");
        assertFalse(CalcHelper.EqualTo(uInput, breCount));
    }
    
    /*
     * Desc:
     * TEST FOR INVOICE MORE THAN 100, WITHOUT DISCOUNT AND WITHOUT PENALTY CHARGE
     */        
    @Test
    public void TestIvoiceCalculator() {

        InvoiceInfo inv = new InvoiceInfo();

        inv.setHireNet(new BigDecimal(301));
        inv.setHireVat(new BigDecimal(45.15));
        inv.setHireGross(new BigDecimal(345.4));

        inv.setRepairNet(BigDecimal.ZERO);
        inv.setRepairVat(BigDecimal.ZERO);
        inv.setRepairGross(BigDecimal.ZERO);

        inv.setEngineerFeeVat(BigDecimal.ZERO);
        inv.setEngineerFeeNet(BigDecimal.ZERO);
        inv.setEngineerFeeGross(BigDecimal.ZERO);

        inv.setStorageRecoveryNet(new BigDecimal(100));
        inv.setStorageRecoveryVat(new BigDecimal(15));
        inv.setStorageRecoveryGross(new BigDecimal(115));

        inv.setTotalNet(new BigDecimal(401));
        inv.setTotalVat(new BigDecimal(60.15));
        inv.setTotalGross(new BigDecimal(461.15));

        inv.setClaimsHandlingInvoiceAmount(BigDecimal.ZERO);
        inv.setDeductionForClaimsHandlingFee(BigDecimal.ZERO);
        inv.setDiscount(BigDecimal.ZERO);
        inv.setTotalToPay(BigDecimal.ZERO);
        inv.setPenaltyCharge(BigDecimal.ZERO);
        
        InvoiceCalcHelper helper = InvoiceCalcHelper.getInstance(inv);
        
        System.out.println("");
        System.out.println("TestIvoiceCalculator");
        System.out.println("====================================");
        System.out.println("HireGross:"+inv.getHireGross());
        System.out.println("HireNet:"+inv.getHireNet());
        System.out.println("HireVat:"+inv.getHireVat());
        System.out.println("CalculatedHireVat:"+helper.getCalculatedHireVat());
        System.out.println("CalculatedTotalNet:"+helper.getCalculatedTotalNet());
        System.out.println("CalculatedTotalToPay:"+helper.getCalculatedTotalToPay());
        
        assertTrue(CalcHelper.EqualTo(inv.getClaimsHandlingInvoiceAmount(), BigDecimal.ZERO));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireVat(), new BigDecimal(45.4)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireGross(),new BigDecimal(346.95)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairVat(), BigDecimal.ZERO));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairGross(), BigDecimal.ZERO));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalNet(), new BigDecimal(401)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalVat(), new BigDecimal(60.00)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalGross(), new BigDecimal(461.15)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalToPay(), new BigDecimal(461.95)));
    }
    
    /*
     * Desc:
     * TEST FOR INVOICE MORE THAN 1000, WITH DISCOUNT AND PENALTY CHARGE
     */    
    @Test
    public void TestIvoiceCalculator1() {
        
        InvoiceInfo inv = new InvoiceInfo();

        inv.setHireNet(new BigDecimal(1350));
        inv.setHireVat(new BigDecimal(202.5));
        inv.setHireGross(new BigDecimal(1552.5));

        inv.setRepairNet(new BigDecimal(100));
        inv.setRepairVat(new BigDecimal(15));
        inv.setRepairGross(new BigDecimal(115));

        inv.setEngineerFeeNet(new BigDecimal(200));
        inv.setEngineerFeeVat(new BigDecimal(30));
        inv.setEngineerFeeGross(new BigDecimal(230));

        inv.setStorageRecoveryNet(new BigDecimal(100));
        inv.setStorageRecoveryVat(new BigDecimal(15));        
        inv.setStorageRecoveryGross(new BigDecimal(115));
        
        inv.setClaimsHandlingInvoiceAmount(new BigDecimal(100));
        inv.setDeductionForClaimsHandlingFee(new BigDecimal(-100));
        
        inv.setTotalNet(new BigDecimal(1650));
        inv.setTotalVat(new BigDecimal(247.5));
        inv.setTotalGross(new BigDecimal(1897.5));
        
        inv.setDiscount(new BigDecimal(-20.15));
        inv.setPenaltyCharge(new BigDecimal(10.15));
        inv.setTotalToPay(new BigDecimal(1887.5));
        
        InvoiceCalcHelper helper = InvoiceCalcHelper.getInstance(inv);

        System.out.println("");
        System.out.println("TestIvoiceCalculator1");
        System.out.println("====================================");
        System.out.println("HireGross:"+inv.getHireGross());
        System.out.println("HireNet:"+inv.getHireNet());
        System.out.println("HireVat:"+inv.getHireVat());
        System.out.println("CalculatedHireVat:"+helper.getCalculatedHireVat());
        System.out.println("CalculatedTotalNet:"+helper.getCalculatedTotalNet());
        System.out.println("CalculatedTotalToPay:"+helper.getCalculatedTotalToPay());
        
        assertTrue(CalcHelper.EqualTo(inv.getClaimsHandlingInvoiceAmount(), new BigDecimal(100)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireVat(), new BigDecimal(202)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireGross(),new BigDecimal(1552)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairVat(), new BigDecimal(15)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairGross(), new BigDecimal(115)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalNet(), new BigDecimal(1650)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalVat(), new BigDecimal(247.9)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalGross(), new BigDecimal(1897.2)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalToPay(), new BigDecimal(1887.0)));
    }
    
    /*
     * Desc:
     * TEST FOR INVOICE LESS THAN 100 AND HIRE NET LESS THAN 1 POUNDS
     */
    @Test
    public void TestIvoiceCalculator2() {

        InvoiceInfo inv = new InvoiceInfo();

        inv.setHireNet(new BigDecimal(0.99));
        inv.setHireVat(new BigDecimal(0.15));
        inv.setHireGross(new BigDecimal(1.14));

        inv.setRepairNet(new BigDecimal(0.55));
        inv.setRepairVat(new BigDecimal(0.08));
        inv.setRepairGross(new BigDecimal(0.63));
        
        inv.setEngineerFeeNet(new BigDecimal(0.55));
        inv.setEngineerFeeVat(new BigDecimal(0.08));
        inv.setEngineerFeeGross(new BigDecimal(0.63));

        inv.setStorageRecoveryNet(new BigDecimal(0.55));
        inv.setStorageRecoveryVat(new BigDecimal(0.08));
        inv.setStorageRecoveryGross(new BigDecimal(0.63));

        inv.setTotalNet(new BigDecimal(2.64));
        inv.setTotalVat(new BigDecimal(0.40));
        inv.setTotalGross(new BigDecimal(3.04));

        inv.setClaimsHandlingInvoiceAmount(new BigDecimal(2.00));
        inv.setDeductionForClaimsHandlingFee(new BigDecimal(0.00));
        inv.setDiscount(new BigDecimal(-15.00));
        inv.setPenaltyCharge(new BigDecimal(10.15));
        
        inv.setTotalToPay(new BigDecimal(0.00));

        InvoiceCalcHelper helper = InvoiceCalcHelper.getInstance(inv);

        System.out.println("");
        System.out.println("TestIvoiceCalculator2");
        System.out.println("====================================");
        System.out.println("HireGross:"+inv.getHireGross());
        System.out.println("HireNet:"+inv.getHireNet());
        System.out.println("HireVat:"+inv.getHireVat());
        System.out.println("CalculatedHireVat:"+helper.getCalculatedHireVat());
        System.out.println("CalculatedTotalNet:"+helper.getCalculatedTotalNet());
        System.out.println("CalculatedTotalToPay:"+helper.getCalculatedTotalToPay());
        
        assertTrue(CalcHelper.EqualTo(inv.getClaimsHandlingInvoiceAmount(), new BigDecimal(2.00)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireVat(), new BigDecimal(0.00)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireGross(),new BigDecimal(1.14)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairVat(), new BigDecimal(0.98)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairGross(), new BigDecimal(0.63)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalNet(), new BigDecimal(2.64)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalVat(), new BigDecimal(0.40)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalGross(), new BigDecimal(3.04)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalToPay(), new BigDecimal(-1.81)));
  
    }   
    
    
    @After
    public void tearDown() throws Exception {
    }
}
