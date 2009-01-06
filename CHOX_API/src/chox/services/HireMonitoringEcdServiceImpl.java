/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.HireMonitoringEcd;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Emmanuel
 */

public class HireMonitoringEcdServiceImpl extends DataService implements HireMonitoringEcdService {

    public List<HireMonitoringEcd> getHireMonitoringEcdsByClaimId(int claimId) {
        Criteria criteria = getCurrentSession().createCriteria(HireMonitoringEcd.class);      
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        criteria.addOrder(Order.asc("createdDate"));
        return criteria.list();
    }
    
    public List<HireMonitoringEcd> getHireMonitoringEcdsByClaimIdFilter(int claimId, boolean isAsc, String orderByField){
        Criteria criteria = getCurrentSession().createCriteria(HireMonitoringEcd.class);      
        criteria.createCriteria("claim").add(Restrictions.eq("id", claimId));
        if(isAsc){
            criteria.addOrder(Order.asc(orderByField));
        }else{
            criteria.addOrder(Order.desc(orderByField));
        }
        return criteria.list();
    }
    
    public HireMonitoringEcd getObject(int id) {
       return (HireMonitoringEcd)getCurrentSession().get(HireMonitoringEcd.class, id);
    }

    public void updateObject(HireMonitoringEcd object) {
        
        getCurrentSession().beginTransaction();
        getCurrentSession().saveOrUpdate(object);
        getCurrentSession().getTransaction().commit();
    }
    
    public Date getLatestHireMonitoringECDDate(Claim claim){
        
        Date returnECD = null;
        
        Date originalEcd = claim.getCustomer().getInitialECD();
        List<HireMonitoringEcd> hireMonitoringEcds = getHireMonitoringEcdsByClaimIdFilter(claim.getId(), false, "createdDate");
        
        if(hireMonitoringEcds.size()>0){
            returnECD = hireMonitoringEcds.get(0).getEcdDate();
        }else{
            returnECD = originalEcd;
        }
        
        return returnECD;
    }
}
