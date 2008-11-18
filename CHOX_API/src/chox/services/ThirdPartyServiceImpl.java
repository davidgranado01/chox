package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;

public class ThirdPartyServiceImpl {

    public static XMLParseResult saveThirdPartyForXMLUploader(Session currentSession, XMLParseResult xmlParseResult){
        
        
        ThirdParty thirdparty = xmlParseResult.getClaim().getThirdParty();
        
        if(thirdparty!=null){
        
            thirdparty.setCreatedBy(WebUserServiceImpl.getCurrentUser());
            thirdparty.setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
            thirdparty.setLastModifiedBy(WebUserServiceImpl.getCurrentUser());
            thirdparty.setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try{
                    currentSession.saveOrUpdate(thirdparty);
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }

                xmlParseResult.getClaim().setThirdParty(thirdparty);

            }
            
        }
        
        return xmlParseResult;
    }
}
