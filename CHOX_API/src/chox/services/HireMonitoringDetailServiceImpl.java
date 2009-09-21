/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.HireMonitoringDetail;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HireMonitoringDetailServiceImpl extends SecureDataService implements HireMonitoringDetailService {

    public HireMonitoringDetail getHireMonitoringDetailByVehicleHireId(int hiremonitoringdetailid) {
        HireMonitoringDetail hiremonitoringdetail = new HireMonitoringDetail();

        DetachedCriteria criteria = DetachedCriteria.forClass(HireMonitoringDetail.class);
        criteria.add(Restrictions.eq("id", hiremonitoringdetailid));

        hiremonitoringdetail = (HireMonitoringDetail) getByCriteria(criteria);


        return hiremonitoringdetail;
    }

    public HireMonitoringDetail getObject(int id) {
        return (HireMonitoringDetail) get(HireMonitoringDetail.class, id);
    }

    public void updateObject(HireMonitoringDetail hireMonitoringDetail) {

        this.save(hireMonitoringDetail);
    }

    public void saveObjectForXMLUploader(final ClaimResult claimResult) {
        if (claimResult.getClaim().getHireMonitoringDetail() != null) {
            super.getHibernateTemplate().saveOrUpdate(claimResult.getClaim().getHireMonitoringDetail());
        }
    }
}
