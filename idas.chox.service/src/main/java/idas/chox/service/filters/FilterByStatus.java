package idas.chox.service.filters;

import java.util.Arrays;
import java.util.HashSet;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.search.ClaimSearchCriteria;

public class FilterByStatus extends BaseFilter {

    private String status;
    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria(ClaimSearchCriteria claimSearchCriteria) {

        claimSearchCriteria.setStatuses(new HashSet<>(Arrays.asList(getStatus())));
        claimSearchCriteria.setManual(getIsManualFilter());
        claimSearchCriteria.setWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

        // Need to set following for 'Approved Invoices Awaiting Payment' queueif payments team active....
        //NB: null check added to getCurrentUser() to prevent error being thrown when user logd out before queues loaded
        if (securityInfoProvider.getCurrentUser() != null
                && (securityInfoProvider.getCurrentUser().isCHOXAdmin() || (securityInfoProvider.getCurrentUser().isAnInsurer()
                    && securityInfoProvider.getCurrentUser().getInsurer().isPaymentsTeamEnable()))
                && getStatus().equals(ClaimStatus.AWAITING_INVOICE_PAYMENT)) {
            claimSearchCriteria.setApprovedInvoiceOwnershipSearchParamIds(new HashSet<>(Arrays.asList(new Integer[]{new Integer("1")})));
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
