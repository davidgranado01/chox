package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;

public class SolicitorServiceImpl {

    public static XMLParseResult saveSolicitorForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getSolicitors())!=null){
            
            for(Integer i=0; i<(xmlParseResult.getSolicitors()).size(); i++){
                
                ((xmlParseResult.getSolicitors()).get(i)).setCreatedBy(WebUserServiceImpl.getCurrentUser());
                ((xmlParseResult.getSolicitors()).get(i)).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
                ((xmlParseResult.getSolicitors()).get(i)).setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
                ((xmlParseResult.getSolicitors()).get(i)).setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try{
                        xmlParseResult.getCurrentSession().saveOrUpdate(((xmlParseResult.getSolicitors()).get(i)));
                    } catch (Exception e) {
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                    }
                }
            }
        }
        
        return xmlParseResult;
    }  
    
}
