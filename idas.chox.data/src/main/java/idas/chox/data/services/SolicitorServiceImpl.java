package idas.chox.data.services;

import idas.chox.core.model.Injury;
import idas.chox.core.model.Solicitor;
import idas.chox.core.services.SolicitorService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class SolicitorServiceImpl extends SecureDataService implements SolicitorService {

    public Solicitor getSolicitorByInjury(Injury injury) {

        Solicitor solicitor = null;

        DetachedCriteria criteria = DetachedCriteria.forClass(Solicitor.class).add(Restrictions.eq("injury", injury));
        solicitor = (Solicitor) getByCriteria(criteria);


        return solicitor;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveSolicitorForXMLUploader(final ClaimResult claimResult) {

        if ((claimResult.getSolicitors()) != null) {

            for (Integer i = 0; i < (claimResult.getSolicitors()).size(); i++) {

                getHibernateTemplate().saveOrUpdate(((claimResult.getSolicitors()).get(i)));

            }
        }
    }

    public Solicitor getSolicitor(int id) {
        return (Solicitor) get(Solicitor.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveSolicitor(Solicitor solicitor) {
        save(solicitor);
    }
}
