package idas.chox.data.services;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;
import idas.chox.core.services.WitnessService;

public class WitnessServiceImpl extends SecureDataService implements WitnessService {

    @Override
    public Witness getWitnessByIncident(Incident incident) {

        DetachedCriteria criteria = DetachedCriteria.forClass(Witness.class).add(Restrictions.eq("incident", incident));
        return (Witness) getByCriteria(criteria);
    }

    @Override
    public Witness getWitness(int id) {
        return (Witness) get(Witness.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveWitness(Witness witness) {
        save(witness);
    }
}

