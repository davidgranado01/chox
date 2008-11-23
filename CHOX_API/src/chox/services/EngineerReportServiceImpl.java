package chox.services;

import chox.Util.DateHelper;
import chox.model.EngineerReport;
import chox.model.XMLParseResult;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class EngineerReportServiceImpl extends DataService implements EngineerReportService{

    public XMLParseResult saveEngineerReportForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getClaim().getEngineerReport())!=null){
            
            (xmlParseResult.getClaim().getEngineerReport()).setCreatedBy(getCurrentUser().getId());
            (xmlParseResult.getClaim().getEngineerReport()).setCreatedDate(DateHelper.getCurrentTimeStamp());
            (xmlParseResult.getClaim().getEngineerReport()).setLastModifiedBy(getCurrentUser().getId());
            (xmlParseResult.getClaim().getEngineerReport()).setLastModifiedDate(DateHelper.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try{
                    xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim().getEngineerReport());
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
            }
        }
        
        return xmlParseResult;
    }  
    
    public EngineerReport getClaimByCHOReferenceNumber(String sClaimReferenceNumber){
               
        EngineerReport engineerreport = new EngineerReport();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(EngineerReport.class);
            criteria.add(Restrictions.eq("choReference", sClaimReferenceNumber));
            engineerreport = (EngineerReport) criteria.uniqueResult();   
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        return engineerreport;
    }
}
