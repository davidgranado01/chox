/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.HireMonitoringDetail;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Element;
import chox.data.HibernateUtil;

public class HireMonitoringDetailServiceImpl  extends DataService{

    public HireMonitoringDetail getHireMonitoringDetailByVehicleHireId(int hiremonitoringdetailid){
        HireMonitoringDetail hiremonitoringdetail = new HireMonitoringDetail();
        
        try {
            
            Criteria criteria = currentSession.createCriteria(HireMonitoringDetail.class);
            criteria.add(Restrictions.eq("id", hiremonitoringdetailid));
            
            hiremonitoringdetail = (HireMonitoringDetail) criteria.uniqueResult();
            
        } catch (Throwable e) {
           e.printStackTrace();
        }
        
        currentSession.clear();
        currentSession.disconnect();
        
        return hiremonitoringdetail;
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
