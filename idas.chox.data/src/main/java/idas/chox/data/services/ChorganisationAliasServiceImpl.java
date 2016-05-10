package idas.chox.data.services;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ChorganisationAlias;
import idas.chox.core.services.ChorganisationAliasService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class ChorganisationAliasServiceImpl extends SecureDataService implements ChorganisationAliasService {

    private static final Logger LOG = LoggerFactory.getLogger(ChorganisationAliasServiceImpl.class);
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void createDefaultRecord(Chorganisation chorganisation) {
        ChorganisationAlias object = new ChorganisationAlias();
        object.setChorganisation(chorganisation);
        object.setAliasName(chorganisation.getName().replaceAll("[^A-Za-z0-9]", ""));
        saveChorganisationAlias(object);
    }

    @Override
    public ChorganisationAlias getChorganisationByAliasName(String aliasName) {

        ChorganisationAlias object = null;
        DetachedCriteria criteria = DetachedCriteria.forClass(ChorganisationAlias.class);
        criteria.add(Restrictions.ilike("aliasName", aliasName.replaceAll("[^A-Za-z0-9]", "")));
        object = (ChorganisationAlias) getByCriteria(criteria);

        return object;
    }

    @Override
    public List<ChorganisationAlias> getChorganisationAliasesByChorganisation(int chorganisationId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChorganisationAlias.class);

        if (chorganisationId > 0) {
            criteria.add(Restrictions.eq("chorganisation.id", chorganisationId));
        }

        criteria.addOrder(Order.asc("aliasName"));
        return findByCriteria(criteria);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void deleteChorganisationAlias(ChorganisationAlias chorganisationAlias) {
        delete(chorganisationAlias);
    }

    @Override
    public ChorganisationAlias getChorganisationAlias(int chorganisationAliasId) {
        return (ChorganisationAlias) get(ChorganisationAlias.class, chorganisationAliasId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveChorganisationAlias(ChorganisationAlias chorganisationAlias) {
        save(chorganisationAlias);
    }

    @Override
    public boolean isChorganisationAliasExist(int chorganisationId, String aliasName) {

        boolean bFlag = true;
        DetachedCriteria criteria = DetachedCriteria.forClass(ChorganisationAlias.class);
        criteria.add(Restrictions.eq("aliasName", aliasName.trim()));
        if (getByCriteria(criteria) == null) {
            bFlag = false;
        }
        return bFlag;

    }
}
