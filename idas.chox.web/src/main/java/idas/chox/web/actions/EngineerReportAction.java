package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.EngineerReport;
import idas.chox.service.security.ApplicationAccessibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.AccessDeniedException;

public class EngineerReportAction extends ClaimModelAction<EngineerReport> {

    private static final Logger LOG = LoggerFactory.getLogger(EngineerReportAction.class);
    @Override
    public EngineerReport loadModel() {

         LOG.debug("EngineerReportAction load Model is called ");
        EngineerReport engineerReport = claim.getEngineerReport();

        if (engineerReport != null) {
            return engineerReport;
        }

        return new EngineerReport();
    }

    //@Override
    public String updateModel(Claim claim) {
        if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
            throw new AccessDeniedException("Attempt to access a claim that you do not own.");
        }

        claim.setEngineerReport(model);
        LOG.debug("engineerreport is set in claim");
        return SUCCESS;
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
}
