package idas.chox.data.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.IdLookupItem;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.WebUser;

public class SecureDataService extends BaseDataService {

    private static final Logger LOG = LoggerFactory.getLogger(SecureDataService.class);
    private SecurityInfoProvider securityInfoProvider;

    
    public void setSecurityInfoProvider(SecurityInfoProvider provider) {
        this.securityInfoProvider = provider;

        if (this.securityInfoProvider != null && this.securityInfoProvider.getCurrentUser() != null) {
            initGlobalFilter();
        }
    }


    public SecurityInfoProvider getSecurityInfoProvider() {
        return this.securityInfoProvider;
    }

  
    public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUser();
    }



    private void initGlobalFilter() {
        if (!this.getSecurityInfoProvider().getIsCHOXAdmin()) {

            if (this.getSecurityInfoProvider().getIsCHO()) {
                
                if (getCurrentSession().getEnabledFilter("insurer_filter") == null) {
                    Set<Integer> ids = getInsurerIds();
                    getCurrentSession().enableFilter("insurer_filter").setParameterList("insurerIds", ids);
                }
                if (getCurrentSession().getEnabledFilter("cho_filter") == null) {
                    getCurrentSession().enableFilter("cho_filter").setParameter("choIds", this.getCurrentUser().getChorganisation().getId());
                }
                if (getCurrentSession().getEnabledFilter("cho_user_filter") == null) {
                    getCurrentSession().enableFilter("cho_user_filter").setParameter("choId", this.getCurrentUser().getChorganisation().getId());
                }
                

            } else if (this.getSecurityInfoProvider().getIsINS()) {

                if (getCurrentSession().getEnabledFilter("cho_filter") == null) {
                    Set<Integer> ids = getSupplierIds();
                    getCurrentSession().enableFilter("cho_filter").setParameterList("choIds", ids);
                }
                if (getCurrentSession().getEnabledFilter("insurer_filter") == null) {
                    getCurrentSession().enableFilter("insurer_filter").setParameter("insurerIds", this.getCurrentUser().getInsurer().getId());
                }
                if (getCurrentSession().getEnabledFilter("insurer_user_filter") == null) {
                    getCurrentSession().enableFilter("insurer_user_filter").setParameter("insurerId", this.getCurrentUser().getInsurer().getId());
                }
            } 
        }
    }


    private Set<Integer> getInsurerIds() {
        Set<Integer> ids = new HashSet<Integer>();

        Integer choId = this.getSecurityInfoProvider().getCurrentUser().getChorganisation().getId();
        if (choId == null) {
            LOG.error("Cannot get insurers for null choId.");
            return ids;
        }


        try {
            List result;

            StringBuilder sb = new StringBuilder();
            sb.append("select a.id as id, a.name as name from insurer ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.insurer_id ");
            sb.append("where a.status=true and b.chorganisation_id=:pChorganisationId ");

            Map extParameters = new HashMap();

            extParameters.put("pChorganisationId", choId);

            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);

            for (Object o : result) {
                IdLookupItem data = (IdLookupItem) o;
                ids.add(data.getId());
            }

        } catch (Exception ex) {
            LOG.error("Exception caught getting Insurers for CHO with ID={}: {}", choId, ex.getMessage());
        }

        return ids;
    }
    
    private Set<Integer> getSupplierIds() {
        Set<Integer> ids = new HashSet<Integer>();

        Integer insurerId = this.getSecurityInfoProvider().getCurrentUser().getInsurer().getId();
        if (insurerId == null) {
            LOG.error("Cannot get suppliers for null insurerId.");
            return ids;
        }

        try {

            List result;

            StringBuilder sb = new StringBuilder();
            sb.append("select a.id as id, a.name as name from chorganisation ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.chorganisation_id ");
            sb.append("where a.status=true and b.insurer_id=:pInsurerId ");
            sb.append("order by a.name");

            Map extParameters = new HashMap();
            extParameters.put("pInsurerId", insurerId);
            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);

            for (Object o : result) {
                IdLookupItem data = (IdLookupItem) o;
                ids.add(data.getId());
            }

        } catch (Exception ex) {
            LOG.error("Exception caught getting suppliers for insurer with ID={}: {}", insurerId, ex.getMessage());
        }

        return ids;
    }

}
