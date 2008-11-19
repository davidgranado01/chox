package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;

public class EngineerReportServiceImpl {

    public static XMLParseResult saveEngineerReportForXMLUploader(Session currentSession, XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getEngineerReports())!=null){
            
            for(Integer i=0; i<(xmlParseResult.getEngineerReports()).size(); i++){
                
                ((xmlParseResult.getEngineerReports()).get(i)).setCreatedBy(WebUserServiceImpl.getCurrentUser());
                ((xmlParseResult.getEngineerReports()).get(i)).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
                ((xmlParseResult.getEngineerReports()).get(i)).setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
                ((xmlParseResult.getEngineerReports()).get(i)).setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try{
                        currentSession.saveOrUpdate(((xmlParseResult.getEngineerReports()).get(i)));
                    } catch (Exception e) {
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                    }
                }
            }
        }
        
        return xmlParseResult;
    }  
}
