package idas.chox.data.services;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.core.services.InjuryService;
import idas.chox.core.xmlValidation.ClaimResult;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.DetachedCriteria;

public class InjuryServiceImpl extends SecureDataService implements InjuryService {

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

    public void saveInjury(Injury injury) {
        save(injury);
    }
}
