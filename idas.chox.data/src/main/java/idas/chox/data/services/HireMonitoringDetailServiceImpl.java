package idas.chox.data.services;

import idas.chox.core.model.HireMonitoringDetail;
import idas.chox.core.services.HireMonitoringDetailService;
import idas.chox.core.xmlValidation.ClaimResult;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class HireMonitoringDetailServiceImpl extends SecureDataService implements HireMonitoringDetailService {

    @Override
    public HireMonitoringDetail getHireMonitoringDetailByVehicleHireId(int hiremonitoringdetailid) {
        HireMonitoringDetail hiremonitoringdetail = new HireMonitoringDetail();

        DetachedCriteria criteria = DetachedCriteria.forClass(HireMonitoringDetail.class);
        criteria.add(Restrictions.eq("id", hiremonitoringdetailid));

        hiremonitoringdetail = (HireMonitoringDetail) getByCriteria(criteria);


        return hiremonitoringdetail;
    }

    @Override
    public HireMonitoringDetail getHireMonitoringDetail(int id) {
        return (HireMonitoringDetail) get(HireMonitoringDetail.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveHireMonitoringDetail(HireMonitoringDetail hireMonitoringDetail) {

        this.save(hireMonitoringDetail);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    @Override
    public void saveObjectForXMLUploader(final ClaimResult claimResult) {
        if (claimResult.getClaim().getHireMonitoringDetail() != null) {
            super.getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getHireMonitoringDetail());
        }
    }
}
