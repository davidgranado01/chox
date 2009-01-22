package chox.services;

import chox.model.EngineerReport;
import chox.model.XMLParseResult;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class EngineerReportServiceImpl extends DataService implements EngineerReportService {

    public void saveObjectForXMLUploader(final XMLParseResult xmlParseResult) {

        if ((xmlParseResult.getClaim().getEngineerReport()) != null) {
             save(xmlParseResult.getClaim().getEngineerReport());              
        }
    }

    public EngineerReport getClaimByCHOReferenceNumber(String sClaimReferenceNumber) {

        EngineerReport engineerreport = new EngineerReport();

        try {

            DetachedCriteria criteria = DetachedCriteria.forClass(EngineerReport.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
            engineerreport = (EngineerReport) getByCriteria(criteria);

        } catch (Throwable e) {
            e.printStackTrace();
        }       
        
        return engineerreport;
    }

    public EngineerReport getObject(int id) {
        return (EngineerReport) get(EngineerReport.class, id);
    }

    public void updateObject(EngineerReport engineerReport) {
        save(engineerReport);        
    }
}
