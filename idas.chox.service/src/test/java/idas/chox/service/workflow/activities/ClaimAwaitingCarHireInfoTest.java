package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import junit.framework.Assert;

import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Customer;
import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.workflow.Activity;
import idas.chox.test.BaseTest;

public class ClaimAwaitingCarHireInfoTest extends BaseTest{

    @Test(expected = AccessDeniedException.class)
    public void testClaimAwaitingCarHireInfoWithInvalidStatus() throws Exception {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
        Activity activity = activityFactory.getActivity("awaitingCarHireInfo");
        activity.process(claim);
    }

    @Test
    public void testClaimAwaitingCarHireInfo() throws Throwable {

        Claim claim = new Claim();
        claim.setStatus(ClaimStatus.CLAIM_AWAITING_CAR_HIRE_INFO);
        Insurer insurer = insurerService.getInsurer(3);
        claim.setInsurer(insurer);
        ThirdParty thirdParty = new ThirdParty();
        claim.setThirdParty(thirdParty);
        claim.getThirdParty().setPolicyNumber("0002001029");
        Customer customer = new Customer();
        customer.setTitle("mr");
        customer.setFirstName("seeni");
        customer.setLastName("shan");
        customer.setInsurerName("RSA");
        customer.setVehicleRegistration("EU52 XSA");
        customer.setVehicleManufacturer("Ferrari");
        customer.setVehicleModel("top class");
        customer.setIsTotalLoss(false);
        customer.setInitialECD(new Date());
        HireMonitoringDetail hireMonitoringDetail = new HireMonitoringDetail();
        hireMonitoringDetail.setNonProvisionReason("testing");
        hireMonitoringDetail.setLabourCost(BigDecimal.ONE);
        hireMonitoringDetail.setLabourHour(BigDecimal.ONE);
        hireMonitoringDetail.setIsTotalLostCheck(true);
        claim.setHireMonitoringDetail(hireMonitoringDetail);
        claim.setCustomer(customer);
        ClaimAwaitingCarHireInfo activity = (ClaimAwaitingCarHireInfo) activityFactory.getActivity("awaitingCarHireInfo");

//        activity.setOasWorkgroupId(101);
//        activity.setClaimOwnerId(999);

        activity.process(claim);
        Assert.assertEquals(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA, claim.getStatus());
    }
}
