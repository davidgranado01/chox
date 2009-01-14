/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.Claim;
import chox.services.ChoBandService;
import chox.services.ClaimService;
import chox.services.InvoiceService;
import chox.web.security.TabAccessibility;
import java.util.List;
import java.util.Map;
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
public class ClaimActionTest {

    public ClaimActionTest() {
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
     * Test of getAttachmentCategory method, of class ClaimAction.
     */
    @Test
    public void testGetAttachmentCategory() {
        System.out.println("getAttachmentCategory");
        ClaimAction instance = new ClaimAction();
        List expResult = null;
        List result = instance.getAttachmentCategory();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class ClaimAction.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        ClaimAction instance = new ClaimAction();
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class ClaimAction.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        int id = 0;
        ClaimAction instance = new ClaimAction();
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getModel method, of class ClaimAction.
     */
    @Test
    public void testGetModel() {
        System.out.println("getModel");
        ClaimAction instance = new ClaimAction();
        ClaimService claimService = (ClaimService)SpringContextTestFactory.getServiceContext().getBean("claimService");
        instance.setClaimService(claimService);

        Claim result = instance.getModel();

    }

    /**
     * Test of prepare method, of class ClaimAction.
     */
    @Test
    public void testPrepare() throws Exception {
        System.out.println("prepare");
        ClaimAction instance = new ClaimAction();
        instance.prepare();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatuses method, of class ClaimAction.
     */
    @Test
    public void testGetStatuses() {
        System.out.println("getStatuses");
        ClaimAction instance = new ClaimAction();
        List expResult = null;
        List result = instance.getStatuses();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLineOfBusinesses method, of class ClaimAction.
     */
    @Test
    public void testGetLineOfBusinesses() {
        System.out.println("getLineOfBusinesses");
        ClaimAction instance = new ClaimAction();
        List expResult = null;
        List result = instance.getLineOfBusinesses();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVehicleClasses method, of class ClaimAction.
     */
    @Test
    public void testGetVehicleClasses() {
        System.out.println("getVehicleClasses");
        ClaimAction instance = new ClaimAction();
        List expResult = null;
        List result = instance.getVehicleClasses();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInsurers method, of class ClaimAction.
     */
    @Test
    public void testGetInsurers() {
        System.out.println("getInsurers");
        ClaimAction instance = new ClaimAction();
        List expResult = null;
        List result = instance.getInsurers();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getActionResult method, of class ClaimAction.
     */
    @Test
    public void testGetActionResult() {
        System.out.println("getActionResult");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.getActionResult();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateClaimDetail method, of class ClaimAction.
     */
    @Test
    public void testUpdateClaimDetail() {
        System.out.println("updateClaimDetail");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.updateClaimDetail();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTabAccessibility method, of class ClaimAction.
     */
    @Test
    public void testGetTabAccessibility() {
        System.out.println("getTabAccessibility");
        ClaimAction instance = new ClaimAction();
        TabAccessibility expResult = null;
        TabAccessibility result = instance.getTabAccessibility();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of execute method, of class ClaimAction.
     */
    @Test
    public void testExecute() throws Exception {
        System.out.println("execute");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.execute();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getActionPanel method, of class ClaimAction.
     */
    @Test
    public void testGetActionPanel() {
        System.out.println("getActionPanel");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.getActionPanel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of route method, of class ClaimAction.
     */
    @Test
    public void testRoute() {
        System.out.println("route");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.route();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of acknowledge method, of class ClaimAction.
     */
    @Test
    public void testAcknowledge() {
        System.out.println("acknowledge");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.acknowledge();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAcknowledgeClaimActions method, of class ClaimAction.
     */
    @Test
    public void testGetAcknowledgeClaimActions() {
        System.out.println("getAcknowledgeClaimActions");
        ClaimAction instance = new ClaimAction();
        Map expResult = null;
        
        /*
        Map result = instance.getAcknowledgeClaimActions();
        assertEquals(expResult, result);
        */
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contestOrAcceptRejectedClaim method, of class ClaimAction.
     */
    @Test
    public void testContestOrAcceptRejectedClaim() {
        System.out.println("contestOrAcceptRejectedClaim");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.contestOrAcceptRejectedClaim();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContestOrAcceptRejectedClaimActions method, of class ClaimAction.
     */
    @Test
    public void testGetContestOrAcceptRejectedClaimActions() {
        System.out.println("getContestOrAcceptRejectedClaimActions");
        ClaimAction instance = new ClaimAction();
        Map expResult = null;
        /*
        Map result = instance.getContestOrAcceptRejectedClaimActions();
        assertEquals(expResult, result);
         * */
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of approveContestedClaim method, of class ClaimAction.
     */
    @Test
    public void testApproveContestedClaim() {
        System.out.println("approveContestedClaim");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.approveContestedClaim();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApproveContestedClaimActions method, of class ClaimAction.
     */
    @Test
    public void testGetApproveContestedClaimActions() {
        System.out.println("getApproveContestedClaimActions");
        ClaimAction instance = new ClaimAction();
        Map expResult = null;
        /*
        Map result = instance.getApproveContestedClaimActions();
        assertEquals(expResult, result);
        */
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of submitHireMonitoringDetail method, of class ClaimAction.
     */
    @Test
    public void testSubmitHireMonitoringDetail() {
        System.out.println("submitHireMonitoringDetail");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.submitHireMonitoringDetail();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of reSubmitRejectedClaim method, of class ClaimAction.
     */
    @Test
    public void testReSubmitRejectedClaim() {
        System.out.println("reSubmitRejectedClaim");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.reSubmitRejectedClaim();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of contestOrAcceptRejectedInvoice method, of class ClaimAction.
     */
    @Test
    public void testContestOrAcceptRejectedInvoice() {
        System.out.println("contestOrAcceptRejectedInvoice");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.contestOrAcceptRejectedInvoice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of approveBREPassedClaim method, of class ClaimAction.
     */
    @Test
    public void testApproveBREPassedClaim() {
        System.out.println("approveBREPassedClaim");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.approveBREPassedClaim();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApproveBREPassedClaimActions method, of class ClaimAction.
     */
    @Test
    public void testGetApproveBREPassedClaimActions() {
        System.out.println("getApproveBREPassedClaimActions");
        ClaimAction instance = new ClaimAction();
        Map expResult = null;
        // Map result = instance.getApproveBREPassedClaimActions();
        // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of approveEscalatedInvoice method, of class ClaimAction.
     */
    @Test
    public void testApproveEscalatedInvoice() {
        System.out.println("approveEscalatedInvoice");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.approveEscalatedInvoice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApproveEscalatedInvoiceActions method, of class ClaimAction.
     */
    @Test
    public void testGetApproveEscalatedInvoiceActions() {
        System.out.println("getApproveEscalatedInvoiceActions");
        ClaimAction instance = new ClaimAction();
        Map expResult = null;
        // Map result = instance.getApproveEscalatedInvoiceActions();
        // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of approveContestedInvoice method, of class ClaimAction.
     */
    @Test
    public void testApproveContestedInvoice() {
        System.out.println("approveContestedInvoice");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.approveContestedInvoice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getApproveContestedInvoiceActions method, of class ClaimAction.
     */
    @Test
    public void testGetApproveContestedInvoiceActions() {
        System.out.println("getApproveContestedInvoiceActions");
        ClaimAction instance = new ClaimAction();
        Map expResult = null;
        // Map result = instance.getApproveContestedInvoiceActions();
        // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of resubmitOrAcceptContestedInvoice method, of class ClaimAction.
     */
    @Test
    public void testResubmitOrAcceptContestedInvoice() {
        System.out.println("resubmitOrAcceptContestedInvoice");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.resubmitOrAcceptContestedInvoice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getResubmitOrAcceptContestedInvoiceActions method, of class ClaimAction.
     */
    @Test
    public void testGetResubmitOrAcceptContestedInvoiceActions() {
        System.out.println("getResubmitOrAcceptContestedInvoiceActions");
        ClaimAction instance = new ClaimAction();
        Map expResult = null;
        // Map result = instance.getResubmitOrAcceptContestedInvoiceActions();
        // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of logInvoicePayment method, of class ClaimAction.
     */
    @Test
    public void testLogInvoicePayment() {
        System.out.println("logInvoicePayment");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.logInvoicePayment();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validateHireMonitoringDetail method, of class ClaimAction.
     */
    @Test
    public void testValidateHireMonitoringDetail() {
        System.out.println("validateHireMonitoringDetail");
        ClaimAction instance = new ClaimAction();
        boolean expResult = false;
        // boolean result = instance.validateHireMonitoringDetail();
        // assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getVehicleClassId method, of class ClaimAction.
     */
    @Test
    public void testGetVehicleClassId() {
        System.out.println("getVehicleClassId");
        ClaimAction instance = new ClaimAction();
        int expResult = 0;
        int result = instance.getVehicleClassId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setVehicleClassId method, of class ClaimAction.
     */
    @Test
    public void testSetVehicleClassId() {
        System.out.println("setVehicleClassId");
        int vehicleClassId = 0;
        ClaimAction instance = new ClaimAction();
        instance.setVehicleClassId(vehicleClassId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInsurerClassId method, of class ClaimAction.
     */
    @Test
    public void testGetInsurerClassId() {
        System.out.println("getInsurerClassId");
        ClaimAction instance = new ClaimAction();
        int expResult = 0;
        int result = instance.getInsurerClassId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setInsurerId method, of class ClaimAction.
     */
    @Test
    public void testSetInsurerId() {
        System.out.println("setInsurerId");
        int insurerId = 0;
        ClaimAction instance = new ClaimAction();
        instance.setInsurerId(insurerId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLineOfBusinessId method, of class ClaimAction.
     */
    @Test
    public void testGetLineOfBusinessId() {
        System.out.println("getLineOfBusinessId");
        ClaimAction instance = new ClaimAction();
        int expResult = 0;
        int result = instance.getLineOfBusinessId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLineOfBusinessId method, of class ClaimAction.
     */
    @Test
    public void testSetLineOfBusinessId() {
        System.out.println("setLineOfBusinessId");
        int lineOfBusinessId = 0;
        ClaimAction instance = new ClaimAction();
        instance.setLineOfBusinessId(lineOfBusinessId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getActionName method, of class ClaimAction.
     */
    @Test
    public void testGetActionName() {
        System.out.println("getActionName");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.getActionName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCreatedByDesc method, of class ClaimAction.
     */
    @Test
    public void testGetCreatedByDesc() {
        System.out.println("getCreatedByDesc");
        ClaimAction instance = new ClaimAction();
        String expResult = "";
        String result = instance.getCreatedByDesc();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setActionName method, of class ClaimAction.
     */
    @Test
    public void testSetActionName() {
        System.out.println("setActionName");
        String actionName = "";
        ClaimAction instance = new ClaimAction();
        instance.setActionName(actionName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHireMonitoringDetailId method, of class ClaimAction.
     */
    @Test
    public void testGetHireMonitoringDetailId() {
        System.out.println("getHireMonitoringDetailId");
        ClaimAction instance = new ClaimAction();
        int expResult = 0;
        int result = instance.getHireMonitoringDetailId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setInvoiceService method, of class ClaimAction.
     */
    @Test
    public void testSetInvoiceService() {
        System.out.println("setInvoiceService");
        InvoiceService invoiceService = null;
        ClaimAction instance = new ClaimAction();
        instance.setInvoiceService(invoiceService);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setChoBandService method, of class ClaimAction.
     */
    @Test
    public void testSetChoBandService() {
        System.out.println("setChoBandService");
        ChoBandService choBandService = null;
        ClaimAction instance = new ClaimAction();
        instance.setChoBandService(choBandService);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}