package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import idas.chox.core.model.ClaimType;
import idas.chox.core.search.ClaimSearchCriteria;


public class InvoiceEscalatedToSupervisor extends BaseFilter {

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(int insurerId, int choId, int claimTypeId) {
        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setShowOpenClaimsOnly(true);
        claimSearchCriteria.setEscalatedToSupervisor(true);
        claimSearchCriteria.setIsManual(getIsManualFilter());
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        if (insurerId > -1) {
            claimSearchCriteria.setInsurerIds(new HashSet<Integer>(Arrays.asList(insurerId)));
        }
        if (choId > -1) {
            claimSearchCriteria.setSupplierIds(new HashSet<Integer>(Arrays.asList(choId)));
        }
        if (claimTypeId > -1) {
            Set<ClaimType> claimTypes = new HashSet<ClaimType>();
            claimTypes.add(ClaimType.values()[claimTypeId]);
            claimSearchCriteria.setClaimTypes(claimTypes);
        }

        return claimSearchCriteria;
    }
    
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
