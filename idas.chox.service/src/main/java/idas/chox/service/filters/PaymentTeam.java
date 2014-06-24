package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.search.ClaimSearchCriteria;

public class PaymentTeam extends BaseFilter {

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria, boolean paymentsTeamActive) {
        claimSearchCriteria.setShowOpenClaimsOnly(false);
        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList("AwaitingInvoicePayment", "ManualInvoiceBREApproved")));
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());
        claimSearchCriteria.setPaymentsTeamFilter(Boolean.TRUE);        

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
