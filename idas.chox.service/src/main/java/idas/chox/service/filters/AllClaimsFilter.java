package idas.chox.service.filters;

import idas.chox.core.search.ClaimSearchCriteria;

public class AllClaimsFilter extends BaseFilter {
    private String status;
    private String name;
    private String key;


    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(Boolean isCHO, boolean paymentsTeamActive, ClaimSearchCriteria claimSearchCriteria) {

        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

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
