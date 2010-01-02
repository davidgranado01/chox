package idas.chox.data.services;

import idas.chox.core.model.Bordereau;
import idas.chox.core.services.BordereauService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class BordereauServiceImpl extends SecureDataService implements BordereauService {

    public void saveBordereau(Bordereau bordereau) {
        save(bordereau);
    }

    public Bordereau getBordereauByFileName(String fileName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Bordereau.class);
        criteria.add(Restrictions.eq("fileName", fileName));
        return (Bordereau) getByCriteria(criteria);
    }
}
