package idas.chox.data.services;

import idas.chox.core.model.IPWhitelist;
import idas.chox.core.services.IPWhitelistService;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author John
 */
public class IPWhitelistServiceImpl extends SecureDataService implements IPWhitelistService {

    @Override
    public boolean validateInsurerIP(int orgId, String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty())
            return false;

        List<IPWhitelist> ipWhitelist;

        DetachedCriteria criteria = DetachedCriteria.forClass(IPWhitelist.class);
        criteria.createCriteria("insurer").add(Restrictions.eq("id", orgId));


        ipWhitelist = findByCriteria(criteria);


        for(IPWhitelist entry : ipWhitelist) {
            if (ipAddress.startsWith(entry.getIpAddress()))
                return true;
        }
            
        return false;
    }

    @Override
    public boolean validateChoIP(int orgId, String ipAddress) {
        if (ipAddress == null || ipAddress.isEmpty())
            return false;

        List<IPWhitelist> ipWhitelist;

        DetachedCriteria criteria = DetachedCriteria.forClass(IPWhitelist.class);
        criteria.createCriteria("chorganisation").add(Restrictions.eq("id", orgId));


        ipWhitelist = findByCriteria(criteria);
        

        for(IPWhitelist entry : ipWhitelist) {
            if (ipAddress.startsWith(entry.getIpAddress()))
                return true;
        }
            
        return false;
    }
    
}
