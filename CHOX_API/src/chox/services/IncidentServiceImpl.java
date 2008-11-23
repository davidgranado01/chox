
package chox.services;

import chox.Util.DateHelper;
import chox.model.*;

public class IncidentServiceImpl  extends DataService implements IncidentService{

    public XMLParseResult saveIncidentForXMLUploader(XMLParseResult xmlParseResult){
        
        Incident incident = xmlParseResult.getClaim().getIncident();
        
        if(incident!=null){
        
            incident.setCreatedBy(getCurrentUser().getId());
            incident.setCreatedDate(DateHelper.getCurrentTimeStamp());
            incident.setLastModifiedBy(getCurrentUser().getId());
            incident.setLastModifiedDate(DateHelper.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try{
                    xmlParseResult.getCurrentSession().saveOrUpdate(incident);
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }

                xmlParseResult.getClaim().setIncident(incident);
            }
        }
        
        return xmlParseResult;
    }
    
}
