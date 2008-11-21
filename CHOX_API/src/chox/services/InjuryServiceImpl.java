package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;
import java.util.ArrayList;

public class InjuryServiceImpl implements InjuryService{

    public XMLParseResult saveInjuryForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getInjuries())!=null){
            
            for(Integer i=0; i<(xmlParseResult.getInjuries()).size(); i++){
                
                ((xmlParseResult.getInjuries()).get(i)).setCreatedBy(WebUserServiceImpl.getCurrentUser());
                ((xmlParseResult.getInjuries()).get(i)).setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
                ((xmlParseResult.getInjuries()).get(i)).setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
                ((xmlParseResult.getInjuries()).get(i)).setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

                if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                    try{
                        xmlParseResult.getCurrentSession().saveOrUpdate(((xmlParseResult.getInjuries()).get(i)));
                    } catch (Exception e) {
                        xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                    }
                }
            }
        }
        
        return xmlParseResult;
    }  
}
