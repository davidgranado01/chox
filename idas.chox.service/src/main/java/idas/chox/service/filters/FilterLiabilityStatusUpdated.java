/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.filters;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.search.ClaimSearchCriteria;
import java.util.ArrayList;
import java.util.List;

public class FilterLiabilityStatusUpdated extends BaseFilter {
    private static List<String> excludeList = new ArrayList<String>();
    {
        excludeList.add(ClaimStatus.CLAIM_CLOSED);
        excludeList.add(ClaimStatus.INVOICE_PAYMENT_RECEIVED);
        excludeList.add(ClaimStatus.CLAIM_REJECTION_ACCEPTED);
        excludeList.add(ClaimStatus.INVOICE_REJECTED_ACCEPTED);
    }

    private String name;
    private String key;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria() {

        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setLiabilityStatusUpdated(true);
        claimSearchCriteria.setIsWorkgroupCheck(getIsFilterWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsFilterOwnership());
        claimSearchCriteria.setIsSupplierOwnerShipCheck(getIsFilterSupplierOwnership());

        claimSearchCriteria.setStatusExcludeList(excludeList);
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
