/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.workflow.activities;

import idas.chox.core.hpi.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.service.xml.util.NodeHelper;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewTpiClaim extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(NewTpiClaim.class);

    @Override
    protected void beforeProcess(Claim claim) {
        if (claim.getStatus() == null) {
            if (claim.getHireMonitoringDetail() != null && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {

                claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
            }
            //String policyNumber = claim.getThirdParty().getPolicyNumber().trim();

            String claimNumber = claim.getThirdParty().getClaimReference();

            if (claimNumber != null && !claimNumber.equalsIgnoreCase("") && claim.getInsurer().getTpiRegexExpression() != null) {
                NodeHelper nodeHelper = new NodeHelper();
                if (nodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getTpiRegexExpression(), claimNumber.toUpperCase())) {
                    claim.setSpecialRoutedTpiClaim(false);
                } else {
                    claim.setSpecialRoutedTpiClaim(true);
                }
            } else {
                claim.setSpecialRoutedTpiClaim(true);
            }
        }


    }

    @Override
    protected void validate(Claim claim) throws Exception {

        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")) {
            throw new AccessDeniedException("Not in correct role to create a claim.");
        }
    }

    @Override
    protected void doProcess(Claim claim) throws Exception {
        //First Set Liability status
        claim.setLiabilityStatus(LiabilityStatus.LIABILITY_ACCEPTED);
        claim.setLiabilityAgreedDate(new Date());
        claim.setPercentageLiabilityAccepted(new BigDecimal("100.00"));
        claim.setPercentageLiabilityCho(BigDecimal.ZERO);
        claim.updateLiabilityPayment();

        if (claim.getStatus() == null) {
            claim.setStatus(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
            claim.setStatusModifiedDate(new Date());
            if (claim.getChorganisation().getPhone() != null && claim.getChorganisation().getPhone().length() > 0) {
                Comment comment = Comment.New(0, "CHO contact number is " + claim.getChorganisation().getPhone());
                claim.addComment(comment);
            }
            try {
                HpiResponse response = Hpi.getHpiInfo(claim.getCustomer().getVehicleRegistration());
                claim.getCustomer().setHpiVehicleManufacturer(response.getManufacturer());
                claim.getCustomer().setHpiVehicleModel(response.getModel());
                claim.getCustomer().setHpiVehicleYear(response.getYear());
                claim.getCustomer().setHpiVehicleCapacity(response.getCapacity());
                claim.getCustomer().setHpiVehicleDoorplan(response.getDoorPlan());
                claim.getCustomer().setHpiVehicleTransmission(response.getTransmission());
                claim.getCustomer().setHpiFirstRegistration(response.getFirstRegistration());
            } catch (HpiException ex) {
                LOG.warn("Error getting HPI info for vrn '{}': {}", claim.getCustomer().getVehicleRegistration(), ex.getMessage());
                claim.getCustomer().setHpiError(ex.getMessage());
            }
            getDataService().save(claim);
            logTransaction(claim);
        }

        if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT)) {

            // move claim to next status
            currentStatus = claim.getStatus();
            claim.setPreviousStatus(currentStatus);
            claim.setStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);

        } else if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_APPROVED_BY_BRE)) {
            if (!claim.isSpecialRoutedTpiClaim()) {

                // move claim to next status
                currentStatus = claim.getStatus();
                claim.setPreviousStatus(currentStatus);
                claim.setStatus(ClaimStatus.INVOICE_UNASSIGNED);

            } else {

                // move claim to next status
                if (claim.getInsurer().isWorkgroupEnable()) {
                    if (claim.getInsurer().getTpiWorkgroup() != null) {
                        claim.setWorkgroup(claim.getInsurer().getTpiWorkgroup());
                    }
                }
                if (claim.getInsurer().getTpiClaimOwner() != null) {
                    claim.setClaimOwner(claim.getInsurer().getTpiClaimOwner());
                    if (claim.getInsurer().getTpiClaimOwner().getTelephone() != null && claim.getInsurer().getTpiClaimOwner().getTelephone().length() > 0) {
                        Comment comment = Comment.New(0, "Insurer Claims Handler is '" + claim.getInsurer().getTpiClaimOwner().getFullName() + "' (contact number: " + claim.getInsurer().getTpiClaimOwner().getTelephone() + ").");
                        claim.addComment(comment);
                    }
                }
                currentStatus = claim.getStatus();
                claim.setPreviousStatus(currentStatus);
                claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
                getDataService().save(claim);
                logTransaction(claim, currentStatus, claim.getStatus(), 0);
                // move claim to next status
                currentStatus = claim.getStatus();
                claim.setPreviousStatus(currentStatus);
                claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);
            }
        } else if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_ESCALATED)) {

            // move claim to next status
            currentStatus = claim.getStatus();
            claim.setPreviousStatus(currentStatus);
            claim.setStatus(ClaimStatus.INVOICE_UNASSIGNED);
        } else if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_ESCALATED_TO_CH)) {
            // move claim to next status
            currentStatus = claim.getStatus();
            claim.setPreviousStatus(currentStatus);
            claim.setStatus(ClaimStatus.INVOICE_UNASSIGNED);
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
        getDataService().save(claim);
        logTransaction(claim, currentStatus, claim.getStatus(), 0);

        if (chainActivity != null) {
            LOG.debug("Processing next chain activity.");
            chainActivity.setWorkflowContext(processContext);
            chainActivity.processInBatch(claim);
        }
    }

    @Override
    protected String getCurrentStatus() {
        return "";
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(null);
    }
}
