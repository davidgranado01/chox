package idas.chox.data.services;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;
import idas.chox.core.services.WitnessService;
import idas.chox.core.xmlValidation.ClaimResult;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class WitnessServiceImpl extends SecureDataService implements WitnessService {

    public Witness getWitnessByIncident(Incident incident) {

        Witness witness = null;


        DetachedCriteria criteria = DetachedCriteria.forClass(Witness.class).add(Restrictions.eq("incident", incident));
        witness = (Witness) getByCriteria(criteria);


        return witness;
    }

    public Witness getWitness(int id) {
        return (Witness) get(Witness.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveWitness(Witness witness) {
        save(witness);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveWitnessForXMLUploader(final ClaimResult claimResult) {

        ArrayList<Witness> witnesses = claimResult.getWitnesses();

        if (witnesses != null) {

            for (Witness witness : witnesses) {
                save(witness);
            }
        }
    }

    public void saveObjectForXMLUploader(ClaimResult claimResult) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Witness getObject(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

