package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.EngineerReport;
import chox.model.XMLParseResult;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class EngineerReportServiceImpl extends DataService implements EngineerReportService {

    public XMLParseResult saveEngineerReportForXMLUploader(XMLParseResult xmlParseResult) {

        if ((xmlParseResult.getClaim().getEngineerReport()) != null) {

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try {
                    xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim().getEngineerReport());
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
            }
        }

        return xmlParseResult;
    }

    public EngineerReport getClaimByCHOReferenceNumber(String sClaimReferenceNumber) {

        EngineerReport engineerreport = new EngineerReport();

        try {

            Criteria criteria = getCurrentSession().createCriteria(EngineerReport.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
            engineerreport = (EngineerReport) criteria.uniqueResult();

        } catch (Throwable e) {
            e.printStackTrace();
        }

        getCurrentSession().clear();
        getCurrentSession().disconnect();
        return engineerreport;
    }

    public EngineerReport getObject(int id) {
        return (EngineerReport) getCurrentSession().get(EngineerReport.class, id);
    }

    public void updateObject(EngineerReport engineerReport) {

        getCurrentSession().beginTransaction();
        getCurrentSession().update(engineerReport);
        getCurrentSession().getTransaction().commit();
    }
}
