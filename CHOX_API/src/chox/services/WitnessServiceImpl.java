package chox.services;

import chox.model.*;
import java.util.ArrayList;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class WitnessServiceImpl extends SecureDataService implements WitnessService {

    public Witness getWitnessByIncident(Incident incident) {

        Witness witness = null;

        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Witness.class).add(Restrictions.eq("incident", incident));
            witness = (Witness) getByCriteria(criteria);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return witness;
    }

    public Witness getObject(int id) {
        return (Witness) get(Witness.class, id);
    }

    public void updateObject(Witness witness) {

        save(witness);
    }

    public void saveObjectForXMLUploader(final ClaimResult claimResult) {

        ArrayList<Witness> witnesses = claimResult.getWitnesses();

        if (witnesses != null) {

            for (Witness witness : witnesses) {
                save(witness);                 
                }
            }
        }
}

