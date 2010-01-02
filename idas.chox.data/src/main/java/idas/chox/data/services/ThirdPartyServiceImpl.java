package idas.chox.data.services;

import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.ThirdPartyService;
import idas.chox.core.xmlValidation.ClaimResult;

public class ThirdPartyServiceImpl extends SecureDataService implements ThirdPartyService {

    public void saveThirdPartyForXMLUploader(final ClaimResult claimResult) {

        ThirdParty thirdparty = claimResult.getClaim().getThirdParty();

        if (thirdparty != null) {
            getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getThirdParty());
        }
    }

    public ThirdParty getThirdParty(int id) {
        return (ThirdParty) get(ThirdParty.class, id);
    }

    public void saveThirdParty(ThirdParty thirdParty) {
        save(thirdParty);
    }
}
