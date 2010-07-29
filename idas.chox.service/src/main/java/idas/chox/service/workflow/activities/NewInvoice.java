package idas.chox.service.workflow.activities;

import idas.chox.core.bre.RulesEngineResponse;
import idas.chox.core.hpi.*;
import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.History;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;
import org.springframework.security.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewInvoice extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(NewInvoice.class);

    @Override
    protected void validate(Claim claim) throws Exception {
        super.validate(claim);
        SecurityInfoProvider securityInfoProvider = this.getWorkflowContext().getSecurityInfoProvider();
        if (!securityInfoProvider.isInRoleOf("ROLE_CHO")) {
            throw new AccessDeniedException("Not in correct role to upload an invoice.");
        }
    }


    @Override
    protected void doProcess(Claim claim) throws Exception {
        // Perform HPI check
        try {
            HpiResponse hpiResponse = Hpi.getHpiInfo(claim.getVehicleHire().getVehicleRegistration());
            claim.getVehicleHire().setHpiVehicleManufacturer(hpiResponse.getManufacturer());
            claim.getVehicleHire().setHpiVehicleModel(hpiResponse.getModel());
            claim.getVehicleHire().setHpiVehicleYear(hpiResponse.getYear());
            claim.getVehicleHire().setHpiVehicleCapacity(hpiResponse.getCapacity());
            claim.getVehicleHire().setHpiVehicleDoorplan(hpiResponse.getDoorPlan());
            claim.getVehicleHire().setHpiVehicleTransmission(hpiResponse.getTransmission());
        } catch (HpiException ex) {
            LOG.warn("Error getting HPI info for vrn '{}': {}",  claim.getCustomer().getVehicleRegistration(), ex.getMessage());
            claim.getVehicleHire().setHpiError(ex.getMessage());
        }
        RulesEngineResponse response = getWorkflowContext().getBusinessRulesEngService().processResubmitInvoice(claim);

        for (History history : History.New(response)) {
            claim.addHistory(history);
        }

        claim.setStatus(response.getStatus(claim.getInsurer().isEngineersEnable()));
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.CLAIM_AWAITING_INVOICE_DATA);
    }
}
