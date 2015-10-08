package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.search.ClaimSearchCriteria;

public class PaymentTeam extends BaseFilter {

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {
        claimSearchCriteria.setStatuses(new HashSet<>(Arrays.asList("AwaitingInvoicePayment")));
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        claimSearchCriteria.setApprovedInvoiceOwnershipSearchParamIds(new HashSet<>(Arrays.asList(new Integer[]{new Integer("2")})));

        if (securityInfoProvider.getCurrentUser().isCHOXAdmin()
                || (securityInfoProvider.getCurrentUser().isAnInsurer() && securityInfoProvider.getCurrentUser().getInsurer().isPaymentDisputesEnable())) {
            claimSearchCriteria.setPaymentDisputesSearchParamIds(new HashSet<>(Arrays.asList(new String[]{"false"})));
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
