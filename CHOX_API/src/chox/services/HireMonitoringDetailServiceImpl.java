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

        Criteria criteria = getCurrentSession().createCriteria(HireMonitoringDetail.class);
        criteria.add(Restrictions.eq("id", hiremonitoringdetailid));

        hiremonitoringdetail = (HireMonitoringDetail) criteria.uniqueResult();


        return hiremonitoringdetail;
    }

    public HireMonitoringDetail getObject(int id) {
        return (HireMonitoringDetail) getCurrentSession().get(HireMonitoringDetail.class, id);
    }

    public void updateObject(HireMonitoringDetail hireMonitoringDetail) {

        getCurrentSession().beginTransaction();
        getCurrentSession().saveOrUpdate(hireMonitoringDetail);
        getCurrentSession().getTransaction().commit();


    }
    /*
    public Insurer getInsurerByName(String s){
    
    Insurer insurer = new Insurer();
    
    try {
    
    Criteria criteria = getCurrentSession().createCriteria(Insurer.class);
    criteria.add(Restrictions.eq("name", s));
    
    insurer = (Insurer) criteria.uniqueResult();
    
    } catch (Throwable e) {
    e.printStackTrace();
    }
    
    
    
    
    
    return insurer;
    }
     * */
}
