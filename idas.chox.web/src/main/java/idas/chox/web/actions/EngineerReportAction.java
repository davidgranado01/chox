package idas.chox.web.actions;

import idas.chox.core.model.EngineerReport;
import idas.chox.service.security.ApplicationAccessibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EngineerReportAction extends ClaimModelAction<EngineerReport> {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleHireAction.class);
    @Override
    public EngineerReport loadModel() {

         LOG.debug("EngineerReportAction load Model is called ");
        EngineerReport engineerReport = claim.getEngineerReport();

        if (engineerReport != null) {
            return engineerReport;
        }

        return new EngineerReport();
    }

    @Override
    public String updateModel() {

        claim.setEngineerReport(model);
        LOG.debug("engineerreport is set in claim");
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
}
