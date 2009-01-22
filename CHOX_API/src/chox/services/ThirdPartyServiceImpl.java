package chox.services;

import chox.model.*;

public class ThirdPartyServiceImpl extends DataService implements ThirdPartyService {

    public void saveObjectForXMLUploader(final XMLParseResult xmlParseResult) {

        ThirdParty thirdparty = xmlParseResult.getClaim().getThirdParty();

        if (thirdparty != null) {
            getHibernateTemplate().saveOrUpdate(xmlParseResult.getClaim().getThirdParty());
        }
    }

    public ThirdParty getObject(int id) {
        return (ThirdParty) get(ThirdParty.class, id);
    }

    public void updateObject(ThirdParty thirdParty) {

        save(thirdParty);
    }
}
