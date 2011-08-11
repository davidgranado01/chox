package idas.chox.data.services;

import idas.chox.core.model.WebBordereau;
import idas.chox.core.services.WebBordereauService;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebBordereauServiceImpl extends SecureDataService implements WebBordereauService {

    private static final Logger LOG = LoggerFactory.getLogger(WebBordereauServiceImpl.class);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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

    private void addSort(Criteria criteria, String sort, String dir) {
        if (dir.equalsIgnoreCase("desc")) {
            criteria.addOrder(Order.desc(sort));
        } else {
            criteria.addOrder(Order.asc(sort));
        }
    }

 
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
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
