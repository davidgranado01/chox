/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.Util.DateHelper;
import chox.model.HireMonitoringDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

public class HireMonitoringDetailServiceImpl extends DataService implements HireMonitoringDetailService {

    public HireMonitoringDetail getHireMonitoringDetailByVehicleHireId(int hiremonitoringdetailid) {
        HireMonitoringDetail hiremonitoringdetail = new HireMonitoringDetail();

        Criteria criteria = currentSession.createCriteria(HireMonitoringDetail.class);
        criteria.add(Restrictions.eq("id", hiremonitoringdetailid));

        hiremonitoringdetail = (HireMonitoringDetail) criteria.uniqueResult();


        return hiremonitoringdetail;
    }

    public HireMonitoringDetail getObject(int id) {
        return (HireMonitoringDetail) currentSession.get(HireMonitoringDetail.class, id);
    }

    public void updateObject(HireMonitoringDetail hireMonitoringDetail) {

        if (hireMonitoringDetail.getId() <= 0) {
            hireMonitoringDetail.setCreatedBy(getCurrentUser().getId());
            hireMonitoringDetail.setCreatedDate(DateHelper.getCurrentTimeStamp());
            hireMonitoringDetail.setLastModifiedBy(getCurrentUser().getId());
            hireMonitoringDetail.setLastModifiedDate(DateHelper.getCurrentTimeStamp());

        }
        currentSession.beginTransaction();
        currentSession.saveOrUpdate(hireMonitoringDetail);
        currentSession.getTransaction().commit();


    }
    /*
    public Insurer getInsurerByName(String s){
    
    Insurer insurer = new Insurer();
    
    try {
    
    Criteria criteria = currentSession.createCriteria(Insurer.class);
    criteria.add(Restrictions.eq("name", s));
    
    insurer = (Insurer) criteria.uniqueResult();
    
    } catch (Throwable e) {
    e.printStackTrace();
    }
    
    currentSession.clear();
    currentSession.disconnect();
    
    
    return insurer;
    }
     * */
}
