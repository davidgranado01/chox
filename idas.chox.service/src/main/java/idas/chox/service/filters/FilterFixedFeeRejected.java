package idas.chox.service.filters;

import idas.chox.core.model.ClaimType;
import idas.chox.core.search.ClaimSearchCriteria;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;

public class FilterFixedFeeRejected extends BaseFilter {

    private String status;
    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        claimSearchCriteria.setClaimTypes(EnumSet.of(ClaimType.FIXED_FEE));

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
