package idas.chox.data.services;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.IPWhitelist;
import idas.chox.core.services.IPWhitelistService;

/**
 *
 * @author John
 */
public class IPWhitelistServiceImpl extends SecureDataService implements IPWhitelistService {

    @Override
    public boolean validateUserIPAddress(int orgId, String ipAddress, boolean isChoUser, boolean isInsurerUser) {
        if (ipAddress == null || ipAddress.isEmpty()) {
            return false;
        }

        List<IPWhitelist> ipWhitelist;
        DetachedCriteria criteria = DetachedCriteria.forClass(IPWhitelist.class);
        
        if (isInsurerUser) 
            criteria.createCriteria("insurer").add(Restrictions.eq("id", orgId));
        else if (isChoUser) 
            criteria.createCriteria("chorganisation").add(Restrictions.eq("id", orgId));
        
        ipWhitelist = findByCriteria(criteria);

        for (IPWhitelist entry : ipWhitelist) {
            if (ipAddress.startsWith(entry.getIpAddress())) {
                return true;
            }
        }

        return false;
    }
    
    @Override
    public List<IPWhitelist> getIPWhitelistsByOrgId(int orgId, boolean cho, boolean ins) {

        DetachedCriteria criteria = DetachedCriteria.forClass(IPWhitelist.class);
        if (cho) 
            criteria.createCriteria("chorganisation").add(Restrictions.eq("id", orgId));
        else if (ins) 
            criteria.createCriteria("insurer").add(Restrictions.eq("id", orgId));
         
        criteria.addOrder(Order.asc("id"));

        return findByCriteria(criteria);
    }
    
    @Override
    public IPWhitelist getIPWhitelistById(int ipWhitelistId) {
        return (IPWhitelist) get(IPWhitelist.class, ipWhitelistId);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void saveIPWhitelist(IPWhitelist iPWhitelist) {
        save(iPWhitelist);
    }
    
    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value="transactionManager")
    public void deleteIPWhitelist(IPWhitelist iPWhitelist) {
        delete(iPWhitelist);
    }
}
