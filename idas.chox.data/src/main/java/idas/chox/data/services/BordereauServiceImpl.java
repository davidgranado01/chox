package idas.chox.data.services;

import idas.chox.core.model.Bordereau;
import idas.chox.core.services.BordereauService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class BordereauServiceImpl extends SecureDataService implements BordereauService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void saveBordereau(Bordereau bordereau) {
        save(bordereau);
    }

    public Bordereau getBordereauByFileName(String fileName) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Bordereau.class);
        criteria.add(Restrictions.eq("fileName", fileName));
        return (Bordereau) getByCriteria(criteria);
    }
}
