package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.search.ClaimSearchCriteria;

public class PaymentTeam extends BaseFilter {

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList("AwaitingInvoicePayment")));
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        claimSearchCriteria.setApprovedInvoiceOwnershipSearchParamIds(new HashSet<Integer>(Arrays.asList(new Integer[]{new Integer("2")})));

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
