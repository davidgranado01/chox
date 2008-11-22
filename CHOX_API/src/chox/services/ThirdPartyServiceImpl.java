package chox.services;

import chox.data.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import chox.model.*;

public class ThirdPartyServiceImpl  extends DataService implements ThirdPartyService{

    public XMLParseResult saveThirdPartyForXMLUploader(XMLParseResult xmlParseResult){
        
        
        ThirdParty thirdparty = xmlParseResult.getClaim().getThirdParty();
        
        if(thirdparty!=null){
        
            xmlParseResult.getClaim().getThirdParty().setCreatedBy(getCurrentUser().getId());
            xmlParseResult.getClaim().getThirdParty().setCreatedDate(generalServiceImpl.getCurrentTimeStamp());
            xmlParseResult.getClaim().getThirdParty().setLastModifiedBy(getCurrentUser().getId());
            xmlParseResult.getClaim().getThirdParty().setLastModifiedDate(generalServiceImpl.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try{
                    xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim().getThirdParty());
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
            }
        }
        
        return xmlParseResult;
    }
}
