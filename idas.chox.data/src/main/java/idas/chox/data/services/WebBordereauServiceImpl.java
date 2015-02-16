package idas.chox.data.services;


import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.WebBordereau;
import idas.chox.core.services.WebBordereauService;

public class WebBordereauServiceImpl extends SecureDataService implements WebBordereauService {

    private static final Logger LOG = LoggerFactory.getLogger(WebBordereauServiceImpl.class);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveBordereau(WebBordereau bordereau) {
        save(bordereau);
    }

    @Override
    public WebBordereau getBordereauById(int bordereauId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(WebBordereau.class);
        criteria.add(Restrictions.eq("id", bordereauId));
        return (WebBordereau) getByCriteria(criteria);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public boolean deleteBordereau(WebBordereau bordereau) {
        try {
            delete(bordereau);
            return true;
        } catch (Exception ex) {
            LOG.error("Cannot delete WebBordereau with id {}: {}", bordereau.getId(), ex.getMessage());
            return false;
        }

    }

    @Override
    public boolean deleteBordereau(int webBordereauId) {
        WebBordereau bordereau = getBordereauById(webBordereauId);
        try {
            delete(bordereau);
            return true;
        } catch (Exception ex) {
            LOG.error("Cannot delete WebBordereau with id {}: {}", webBordereauId, ex.getMessage());
            return false;
        }

    }
   
}
