package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;

public class SolicitorServiceImpl  extends DataService implements SolicitorService{

    public XMLParseResult saveSolicitorForXMLUploader(XMLParseResult xmlParseResult){
        
        if((xmlParseResult.getSolicitors())!=null){
            
            for(Integer i=0; i<(xmlParseResult.getSolicitors()).size(); i++){
                
                ((xmlParseResult.getSolicitors()).get(i)).setCreatedBy(getCurrentUser().getId());
                ((xmlParseResult.getSolicitors()).get(i)).setCreatedDate(DateHelper.getCurrentTimeStamp());
                ((xmlParseResult.getSolicitors()).get(i)).setLastModifiedBy(getCurrentUser().getId());
                ((xmlParseResult.getSolicitors()).get(i)).setLastModifiedDate(DateHelper.getCurrentTimeStamp());

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
