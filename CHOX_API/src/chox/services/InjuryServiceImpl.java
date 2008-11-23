package chox.services;

import chox.Util.DateHelper;
import chox.model.*;

public class InjuryServiceImpl extends DataService implements InjuryService{

    public XMLParseResult saveInjuryForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getInjuries())!=null){
            
            for(Integer i=0; i<(xmlParseResult.getInjuries()).size(); i++){
                
                ((xmlParseResult.getInjuries()).get(i)).setCreatedBy(getCurrentUser().getId());
                ((xmlParseResult.getInjuries()).get(i)).setCreatedDate(DateHelper.getCurrentTimeStamp());
                ((xmlParseResult.getInjuries()).get(i)).setLastModifiedBy(getCurrentUser().getId());
                ((xmlParseResult.getInjuries()).get(i)).setLastModifiedDate(DateHelper.getCurrentTimeStamp());

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
