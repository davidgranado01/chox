package idas.chox.service.workflow.activities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.hpi.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Comment;
import idas.chox.core.model.LiabilityStatus;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.service.xml.util.NodeHelper;

public class NewTpiClaim extends BaseActivity {

    private static final Logger LOG = LoggerFactory.getLogger(NewTpiClaim.class);
    private boolean autoRoutedInvoice = false;

    @Override
    protected void beforeProcess(Claim claim) {
        if (claim.getStatus() == null) {
            if (claim.getHireMonitoringDetail() != null && claim.getCustomer() != null && claim.getCustomer().getIsTotalLoss() != null) {
                claim.getHireMonitoringDetail().setIsTotalLostCheck(claim.getCustomer().getIsTotalLoss());
            }
            
            String claimNumber = claim.getThirdParty().getClaimReference();

            NodeHelper nodeHelper = new NodeHelper();
            if (ClaimType.isTPI(claim.getClaimType())
                    && claim.getInsurer().isTpiAutoRoutingEnable() 
                    && (claimNumber == null || claim.getInsurer().getTpiRegexExpression() == null
                        || claim.getInsurer().getTpiRegexExpression().isEmpty()
                        || !nodeHelper.isRegularExpressionCheckPass(claim.getInsurer().getTpiRegexExpression(), claimNumber.toUpperCase()))) {
                autoRoutedInvoice = true;
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
        getWorkflowContext().getClaimService().updateLiabilityPayment(claim);

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
            super.setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(super.getCurrentStatus());
            claim.setStatus(ClaimStatus.INVOICE_DATA_CALCULATION_INCORRECT);

        } else if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_APPROVED_BY_BRE)) {
            
            if (!autoRoutedInvoice) {
                // move claim to next status
                super.setCurrentStatus(claim.getStatus());
                claim.setPreviousStatus(super.getCurrentStatus());
                claim.setStatus(ClaimStatus.INVOICE_UNASSIGNED);
            } else {
                if (claim.getInsurer().isWorkgroupEnable() && claim.getInsurer().getInvoiceWorkgroup() != null) {
                        claim.setWorkgroup(claim.getInsurer().getInvoiceWorkgroup());
                }
                if (claim.getInsurer().isClaimOwnershipEnable() && claim.getInsurer().getInvoiceOwner() != null) {
                    claim.setClaimOwner(claim.getInsurer().getInvoiceOwner());
                    if (claim.getInsurer().getInvoiceOwner().getTelephone() != null
                            && claim.getInsurer().getInvoiceOwner().getTelephone().length() > 0) {
                        Comment comment = Comment.New(0, "Insurer Claims Handler is '"
                                            + claim.getInsurer().getInvoiceOwner().getFullName() + "' (contact number: "
                                                        + claim.getInsurer().getInvoiceOwner().getTelephone() + ").");
                        claim.addComment(comment);
                    }
                }
                // move claim to next status
                super.setCurrentStatus(claim.getStatus());
                claim.setPreviousStatus(super.getCurrentStatus());
                claim.setStatus(ClaimStatus.INVOICE_APPROVED_BY_BRE);
                getDataService().save(claim);
                logTransaction(claim, super.getCurrentStatus(), claim.getStatus(), 1);

                // move claim to next status
                super.setCurrentStatus(claim.getStatus());
                claim.setPreviousStatus(super.getCurrentStatus());

                //if BRE approves the invoice and TPI is selected it will go into following status
                claim.setStatus(ClaimStatus.AWAITING_INVOICE_PAYMENT);     
            }
        } else if (claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_ESCALATED)
                    || claim.getTpiClaimStatus().equals(ClaimStatus.INVOICE_ESCALATED_TO_CH)) {
            // move claim to next status
            super.setCurrentStatus(claim.getStatus());
            claim.setPreviousStatus(super.getCurrentStatus());
            claim.setStatus(ClaimStatus.INVOICE_UNASSIGNED);
        }
    }

    @Override
    protected void afterProcess(Claim claim) throws Exception {
        if (getChainActivity() != null) {
            LOG.debug("Processing next chain activity.");
            getChainActivity().setWorkflowContext(getProcessContext());
            getChainActivity().processInBatch(claim);
        } else {
            LOG.debug("Saving Claim '{}' with status {}", claim.getChoReference(), claim.getStatus());
            getDataService().save(claim);
            logTransaction(claim, super.getCurrentStatus(), claim.getStatus(), 1);            
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
