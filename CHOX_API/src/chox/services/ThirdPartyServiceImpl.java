package chox.services;

import chox.Util.XmlHelper;
import chox.Util.DateHelper;
import chox.model.*;

public class ThirdPartyServiceImpl extends DataService implements ThirdPartyService {

    public XMLParseResult saveThirdPartyForXMLUploader(XMLParseResult xmlParseResult) {


        ThirdParty thirdparty = xmlParseResult.getClaim().getThirdParty();

        if (thirdparty != null) {
          
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
        return (ThirdParty) getCurrentSession().get(ThirdParty.class, id);
    }

    public void updateObject(ThirdParty thirdParty) {

        getCurrentSession().beginTransaction();
        getCurrentSession().update(thirdParty);
        getCurrentSession().getTransaction().commit();
    }
}
