package idas.chox.data.services;


import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.ThirdPartyService;
import idas.chox.core.xmlValidation.ClaimResult;

public class ThirdPartyServiceImpl extends SecureDataService implements ThirdPartyService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveThirdPartyForXMLUploader(final ClaimResult claimResult) {

        ThirdParty thirdparty = claimResult.getClaim().getThirdParty();

        if (thirdparty != null) {
            getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getThirdParty());
        }
    }

    @Override
    public ThirdParty getThirdParty(int id) {
        return (ThirdParty) get(ThirdParty.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveThirdParty(ThirdParty thirdParty) {
        save(thirdParty);
    }
}
