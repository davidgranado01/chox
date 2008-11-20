package scsbre.tests;

import java.math.BigDecimal;



import org.junit.*;
import scsbre.engine.util.CalcHelper;
import static org.junit.Assert.*;

import scsbre.engine.util.InvoiceCalcHelper;
import scsbre.model.IInvoiceInfo;
import scsbre.sample.InvoiceInfo;

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

        BigDecimal vatRate = new BigDecimal(17.5);
        IInvoiceInfo inv = new InvoiceInfo();

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
        inv.setTotalToPay(BigDecimal.ZERO);

        InvoiceCalcHelper helper = InvoiceCalcHelper.Create(inv, vatRate);

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
    public void TestIvoiceCalculator() {

        BigDecimal vatRate = new BigDecimal(.175);
        IInvoiceInfo inv = new InvoiceInfo();

        inv.setHireNet(new BigDecimal(300));
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
        inv.setTotalToPay(BigDecimal.ZERO);

        InvoiceCalcHelper helper = InvoiceCalcHelper.Create(inv, vatRate);

        System.out.println(inv.getHireGross());
        System.out.println(inv.getHireNet());
        System.out.println(inv.getHireVat());
        System.out.println(helper.getCalculatedHireVat());
        
        assertTrue(CalcHelper.EqualTo(inv.getClaimsHandlingInvoiceAmount(), BigDecimal.ZERO));
        
        
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireVat(), new BigDecimal(52.5)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireGross(),new BigDecimal(352.5)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairVat(), BigDecimal.ZERO));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairGross(), BigDecimal.ZERO));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalNet(), new BigDecimal(300.00)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalVat(), new BigDecimal(52.5)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalGross(), new BigDecimal(352.5)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalToPay(), new BigDecimal(352.0)));
        
  
    }
    
    @Test
    public void TestIvoiceCalculator1() {
        

        BigDecimal vatRate = new BigDecimal(.175);
        IInvoiceInfo inv = new InvoiceInfo();

        inv.setHireNet(new BigDecimal(300));
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
        inv.setTotalToPay(BigDecimal.ZERO);

        InvoiceCalcHelper helper = InvoiceCalcHelper.Create(inv, vatRate);

        System.out.println(inv.getHireGross());
        System.out.println(inv.getHireNet());
        System.out.println(inv.getHireVat());
        System.out.println(helper.getCalculatedHireVat());
        
        assertTrue(CalcHelper.EqualTo(inv.getClaimsHandlingInvoiceAmount(), BigDecimal.ZERO));
        
        
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireVat(), new BigDecimal(52.5)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedHireGross(),new BigDecimal(352.5)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairVat(), BigDecimal.ZERO));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedRepairGross(), BigDecimal.ZERO));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalNet(), new BigDecimal(300.00)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalVat(), new BigDecimal(52.5)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalGross(), new BigDecimal(352.5)));
        assertTrue(CalcHelper.EqualTo(helper.getCalculatedTotalToPay(), new BigDecimal(352.0)));
  
    }    

    @After
    public void tearDown() throws Exception {
    }
}
