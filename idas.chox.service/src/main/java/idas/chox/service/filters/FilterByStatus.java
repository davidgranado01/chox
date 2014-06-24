package idas.chox.service.filters;

import idas.chox.core.model.ClaimStatus;
import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.search.ClaimSearchCriteria;

public class FilterByStatus extends BaseFilter {

    private String status;
    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria, boolean paymentsTeamActive) {

        claimSearchCriteria.setStatuses(new HashSet<String>(Arrays.asList(getStatus())));
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

// Need to set following for 'Approved Invoices Awaiting Payment' and
// 'Manual Invoices Approved By BRE' queues  if payments team active....   
        if (paymentsTeamActive && (getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)
                || getStatus().equals(ClaimStatus.MANUAL_INVOICE_APPROVED))) {
            claimSearchCriteria.setPaymentsTeamFilter(Boolean.FALSE);
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
