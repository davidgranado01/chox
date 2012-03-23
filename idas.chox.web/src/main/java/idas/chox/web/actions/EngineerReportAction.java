//package idas.chox.web.actions;
//
//import idas.chox.core.model.Claim;
//import idas.chox.core.model.EngineerReport;
//import idas.chox.service.security.ApplicationAccessibility;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///*
// *  This action class is instantiated in InvoiceRecalculationAction (This is not really a action calss but act like bean )
// *  When you use any service classes it needed to be set first in InvoiceRecalculationAction prepare() method before any services accessed. eg. vehicleHireAction.setClaimService(claimService); 
// *  the above line applies to super class of this class as well.
// */
//
//public class EngineerReportAction extends ClaimModelAction<EngineerReport> {
//    private static final Logger LOG = LoggerFactory.getLogger(EngineerReportAction.class);
//
//    @Override
//    public EngineerReport loadModel() {
//
//        LOG.debug("EngineerReportAction load Model is called ");
//        EngineerReport engineerReport = claim.getEngineerReport();
//
//        if (engineerReport != null) {
//            return engineerReport;
//        }
//
//        return new EngineerReport();
//    }
//
//    //@Override
//    public String updateModel(Claim claim) {
//        
//        claim.setEngineerReport(model);
//        LOG.debug("engineerreport is set in claim");
//        return SUCCESS;
//    }
// 
//    @Override
//    String getTabName() {
//        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
//    }
//}
