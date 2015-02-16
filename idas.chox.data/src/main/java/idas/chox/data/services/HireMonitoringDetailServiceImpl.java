package idas.chox.data.services;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.services.HireMonitoringDetailService;
import idas.chox.core.xmlValidation.ClaimResult;

public class HireMonitoringDetailServiceImpl extends SecureDataService implements HireMonitoringDetailService {

    @Override
    public HireMonitoringDetail getHireMonitoringDetailByVehicleHireId(int hiremonitoringdetailid) {

        DetachedCriteria criteria = DetachedCriteria.forClass(HireMonitoringDetail.class);
        criteria.add(Restrictions.eq("id", hiremonitoringdetailid));

        return (HireMonitoringDetail) getByCriteria(criteria);
    }

    @Override
    public HireMonitoringDetail getHireMonitoringDetail(int id) {
        return (HireMonitoringDetail) get(HireMonitoringDetail.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveHireMonitoringDetail(HireMonitoringDetail hireMonitoringDetail) {

        this.save(hireMonitoringDetail);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    @Override
    public void saveObjectForXMLUploader(final ClaimResult claimResult) {
        if (claimResult.getClaim().getHireMonitoringDetail() != null) {
            super.getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getHireMonitoringDetail());
        }
    }
}
