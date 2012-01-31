package idas.chox.data.services;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.security.SecurityInfoProvider;
import idas.chox.core.model.WebUser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecureDataService extends BaseDataService {

    private static final Logger LOG = LoggerFactory.getLogger(SecureDataService.class);
    private SecurityInfoProvider securityInforProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider provider) {

        this.securityInforProvider = provider;

        if (this.securityInforProvider != null && this.securityInforProvider.getCurrentUser() != null) {
            initGlobalFilter();
        }
    }

    public void initGlobalFilter() {
        if (!this.getSecurityInfoProvider().getIsCHOXAdmin()) {

            if (this.getSecurityInfoProvider().getIsCHO()) {
                
                if (getCurrentSession().getEnabledFilter("insurer_filter") == null) {
                    Set<Integer> ids = getInsurerIds();
                    getCurrentSession().enableFilter("insurer_filter").setParameterList("insurerIds", ids);
                }
                if (getCurrentSession().getEnabledFilter("cho_filter") == null) {
                    getCurrentSession().enableFilter("cho_filter").setParameter("choIds", this.getCurrentUser().getChorganisation().getId());
                }
                

            } else if (this.getSecurityInfoProvider().getIsINS()) {

                if (getCurrentSession().getEnabledFilter("cho_filter") == null) {
                    Set<Integer> ids = getSupplierIds();
                    getCurrentSession().enableFilter("cho_filter").setParameterList("choIds", ids);
                }
                if (getCurrentSession().getEnabledFilter("insurer_filter") == null) {
                    getCurrentSession().enableFilter("insurer_filter").setParameter("insurerIds", this.getCurrentUser().getInsurer().getId());
                }
            } 
        }
    }

    public WebUser getCurrentUser() {
        return getSecurityInfoProvider().getCurrentUser();
    }

    public SecurityInfoProvider getSecurityInfoProvider() {
        return this.securityInforProvider;
    }

    public Set<Integer> getInsurerIds() {
        Set<Integer> ids = new HashSet<Integer>();
        Iterator itr = getInsurers(this.getSecurityInfoProvider().getCurrentUser().getChorganisation().getId()).iterator();

        while (itr.hasNext()) {
            Insurer ins = (Insurer) itr.next();
            ids.add(ins.getId());
        }
        return ids;
    }
    
    public Set<Integer> getSupplierIds() {
        Set<Integer> ids = new HashSet<Integer>();
        Iterator itr = getSuppliers(this.getSecurityInfoProvider().getCurrentUser().getInsurer().getId()).iterator();

        while (itr.hasNext()) {
            Chorganisation cho = (Chorganisation) itr.next();
            ids.add(cho.getId());
        }
        return ids;
    }
    
    public List<Insurer> getInsurers(Integer choId) {

        List<Insurer> results = new ArrayList<Insurer>();

        if (choId == null) {
            LOG.error("Cannot get insurers for null choId.");
            return results;
        }


        try {

            List result = new ArrayList();

            StringBuilder sb = new StringBuilder();
            sb.append("select a.id as id, a.name as name from insurer ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.insurer_id and b.status=true ");
            sb.append("where a.status=true and b.chorganisation_id=:pChorganisationId order by a.name");

            Map extParameters = new HashMap();

            extParameters.put("pChorganisationId", choId);

            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);

            for (Object o : result) {
                IdLookupItem data = (IdLookupItem) o;
                Insurer item = new Insurer();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }

        } catch (Exception ex) {
            LOG.error("Exception caught getting Insurers for CHO with ID={}: {}", choId, ex.getMessage());
        }

        return results;

    }
    
    public List<Chorganisation> getSuppliers(Integer insurerId) {

        List<Chorganisation> results = new ArrayList<Chorganisation>();

        if (insurerId == null) {
            LOG.error("Cannot get suppliers for null insurerId.");
            return results;
        }

        try {

            List result;

            StringBuilder sb = new StringBuilder();
            sb.append("select a.id as id, a.name as name from chorganisation ");
            sb.append("a inner join insurer_chorganisation b on a.id = b.chorganisation_id and b.status=true ");
            sb.append("where a.status=true and b.insurer_id=:pInsurerId order by a.name");

            Map extParameters = new HashMap();
            extParameters.put("pInsurerId", insurerId);
            result = externalQuery(sb.toString(), extParameters, IdLookupItem.class);

            for (Object o : result) {
                IdLookupItem data = (IdLookupItem) o;
                Chorganisation item = new Chorganisation();
                item.setId(data.getId());
                item.setName(data.getName());
                results.add(item);
            }

        } catch (Exception ex) {
            LOG.error("Exception caught getting suppliers for insurer with ID={}: {}", insurerId, ex.getMessage());
        }

        return results;
    }

}
