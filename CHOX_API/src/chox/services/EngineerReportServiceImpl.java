package chox.services;

import chox.model.EngineerReport;
import chox.model.XMLParseResult;
import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class EngineerReportServiceImpl implements EngineerReportService{

    public XMLParseResult saveEngineerReportForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getClaim().getEngineerReport())!=null){
                
            (xmlParseResult.getClaim().getEngineerReport()).setCreatedBy(WebUserServiceImpl.getCurrentUser());
            (xmlParseResult.getClaim().getEngineerReport()).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
            (xmlParseResult.getClaim().getEngineerReport()).setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
            (xmlParseResult.getClaim().getEngineerReport()).setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

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
        
        Session currentSession = HibernateUtil.currentSession();      
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
