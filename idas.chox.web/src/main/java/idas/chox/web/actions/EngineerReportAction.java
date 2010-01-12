package idas.chox.web.actions;

import idas.chox.core.model.EngineerReport;
import idas.chox.service.security.ApplicationAccessibility;

public class EngineerReportAction extends ClaimModelAction<EngineerReport> {

    @Override
    public EngineerReport loadModel() {
        EngineerReport engineerReport = claim.getEngineerReport();

        if (engineerReport != null) {
            return engineerReport;
        }

        return new EngineerReport();
    }

    @Override
    public String updateModel() {

        claim.setEngineerReport(model);
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
}
