package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;

public class IncidentServiceImpl extends DataService implements IncidentService {

    public XMLParseResult saveIncidentForXMLUploader(XMLParseResult xmlParseResult) {

        Incident incident = xmlParseResult.getClaim().getIncident();

        if (incident != null) {

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try {
                    xmlParseResult.getCurrentSession().saveOrUpdate(incident);
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }

                xmlParseResult.getClaim().setIncident(incident);
            }
        }

        return xmlParseResult;
    }

    public Incident getObject(int id) {
        return (Incident) getCurrentSession().get(Incident.class, id);
    }

    public void updateObject(Incident incident) {        
       
        getCurrentSession().beginTransaction();
        getCurrentSession().update(incident);
        getCurrentSession().getTransaction().commit();
    }
}
