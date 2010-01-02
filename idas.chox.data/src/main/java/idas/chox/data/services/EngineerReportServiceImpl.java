package idas.chox.data.services;

import idas.chox.core.model.EngineerReport;
import idas.chox.core.services.EngineerReportService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class EngineerReportServiceImpl extends SecureDataService implements EngineerReportService {

    public void saveEngineerReportForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getClaim().getEngineerReport()) != null) {
            save(claimResult.getClaim().getEngineerReport());
        }
    }

    public EngineerReport getClaimByCHOReferenceNumber(String sClaimReferenceNumber) {

        EngineerReport engineerreport = new EngineerReport();


        DetachedCriteria criteria = DetachedCriteria.forClass(EngineerReport.class);
        criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
        engineerreport = (EngineerReport) getByCriteria(criteria);

        return engineerreport;
    }

    public EngineerReport getEngineerReport(int id) {
        return (EngineerReport) get(EngineerReport.class, id);
    }

    public void saveEngineerReport(EngineerReport engineerReport) {
        save(engineerReport);
    }
}
