package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.model.ClaimType;

public class FilterFixedFeeRejected extends BaseFilter {

    private String status;
    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCHO, int insurerId, int choId, int claimTypeId) {
        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setShowOpenClaimsOnly(false);
        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setIsManual(getIsManualFilter());
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        claimSearchCriteria.setClaimTypes(new HashSet<ClaimType>(Arrays.asList(ClaimType.FIXED_FEE)));
        if (insurerId > -1) {
            claimSearchCriteria.setInsurerIds(new HashSet<Integer>(Arrays.asList(insurerId)));
        }
        if (choId > -1) {
            claimSearchCriteria.setSupplierIds(new HashSet<Integer>(Arrays.asList(choId)));
        }
        if (claimTypeId > -1 && !ClaimType.FIXED_FEE.equals(ClaimType.values()[claimTypeId])) {
            return null;
        }

        return claimSearchCriteria;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
