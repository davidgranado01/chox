package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;

public class ThirdPartyServiceImpl extends DataService implements ThirdPartyService {

    public XMLParseResult saveThirdPartyForXMLUploader(XMLParseResult xmlParseResult) {


        ThirdParty thirdparty = xmlParseResult.getClaim().getThirdParty();

        if (thirdparty != null) {

            xmlParseResult.getClaim().getThirdParty().setCreatedBy(getCurrentUser().getId());
            xmlParseResult.getClaim().getThirdParty().setCreatedDate(DateHelper.getCurrentTimeStamp());
            xmlParseResult.getClaim().getThirdParty().setLastModifiedBy(getCurrentUser().getId());
            xmlParseResult.getClaim().getThirdParty().setLastModifiedDate(DateHelper.getCurrentTimeStamp());

            if (xmlParseResult.getIsDataValid() && xmlParseResult.getIsSchemaValid()) {

                try {
                    xmlParseResult.getCurrentSession().saveOrUpdate(xmlParseResult.getClaim().getThirdParty());
                } catch (Exception e) {
                    xmlParseResult = XmlHelper.setErrorMessage(xmlParseResult, e.getMessage(), false);
                }
            }
        }

        return xmlParseResult;
    }

    public ThirdParty getObject(int id) {
        return (ThirdParty) currentSession.get(ThirdParty.class, id);
    }

    public void updateObject(ThirdParty thirdParty) {

        currentSession.beginTransaction();
        currentSession.update(thirdParty);
        currentSession.getTransaction().commit();
    }
}
