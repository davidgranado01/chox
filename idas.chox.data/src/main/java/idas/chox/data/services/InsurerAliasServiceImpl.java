package idas.chox.data.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.services.InsurerAliasService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class InsurerAliasServiceImpl extends SecureDataService implements InsurerAliasService {

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void createDefaultRecord(Insurer insurer) {
        InsurerAlias object = new InsurerAlias();
        object.setInsurer(insurer);
        object.setAliasName(insurer.getName().replaceAll("[^A-Za-z0-9]", ""));
        saveInsurerAlias(object);
    }

    @Override
    public InsurerAlias getInsurerByAliasName(String aliasName) {

        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAlias.class);
        criteria.add(Restrictions.ilike("aliasName", aliasName.replaceAll("[^A-Za-z0-9]", "")));
        InsurerAlias insurerAlias = (InsurerAlias) getByCriteria(criteria);

        return insurerAlias;
    }

    @Override
    public List<InsurerAlias> getInsurerAliasesByInsurer(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAlias.class);

        if (insurerId > 0) {
            criteria.add(Restrictions.eq("insurer.id", insurerId));
        }

        criteria.addOrder(Order.asc("aliasName"));
        return findByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteInsurerAlias(InsurerAlias insurerAlias) {
        delete(insurerAlias);
    }

    @Override
    public InsurerAlias getInsurerAlias(int insurerAliasId) {
        return (InsurerAlias) get(InsurerAlias.class, insurerAliasId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveInsurerAlias(InsurerAlias insurerAlias) {
        save(insurerAlias);
    }

    @Override
    public boolean isInsurerAliasExist(int insurerId, String AliasName) {

        boolean bFlag = true;
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerAlias.class);
        criteria.add(Restrictions.eq("aliasName", AliasName.trim()));
        if (getByCriteria(criteria) == null) {
            bFlag = false;
        }
        return bFlag;

    }
}
