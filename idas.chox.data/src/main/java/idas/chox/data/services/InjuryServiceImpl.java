package idas.chox.data.services;

import idas.chox.core.model.Injury;
import idas.chox.core.services.InjuryService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class InjuryServiceImpl extends SecureDataService implements InjuryService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveInjuryForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getInjuries()) != null) {
            for (Integer i = 0; i < (claimResult.getInjuries()).size(); i++) {
                save(((claimResult.getInjuries()).get(i)));
            }
        }

    }

    public Injury getInjury(int id) {
        return (Injury) get(Injury.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveInjury(Injury injury) {
        save(injury);
    }
}
