package idas.chox.data.services;

import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.ThirdPartyService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ThirdPartyServiceImpl extends SecureDataService implements ThirdPartyService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveThirdPartyForXMLUploader(final ClaimResult claimResult) {

        ThirdParty thirdparty = claimResult.getClaim().getThirdParty();

        if (thirdparty != null) {
            getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getThirdParty());
        }
    }

    public ThirdParty getThirdParty(int id) {
        return (ThirdParty) get(ThirdParty.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveThirdParty(ThirdParty thirdParty) {
        save(thirdParty);
    }
}
