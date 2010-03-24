/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.filters;

import idas.chox.core.search.ClaimSearchCriteria;

public class FilterLiabilityStatusUpdated extends BaseFilter {

    private String name;
    private String key;
    private boolean isCheckWorkGroup = false;
    private boolean isCheckOwnership = false;

    @Override
    public ClaimSearchCriteria getClaimSearchCriteria() {

        ClaimSearchCriteria claimSearchCriteria = new ClaimSearchCriteria();
        claimSearchCriteria.setLiabilityStatusUpdated(true);
        claimSearchCriteria.setIsWorkgroupCheck(getIsCheckWorkGroup());
        claimSearchCriteria.setIsOwnerShipCheck(getIsCheckOwnership());
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

    @Override
    public boolean getIsCheckWorkGroup() {
        return isCheckWorkGroup;
    }

    public void setIsCheckWorkGroup(boolean isCheckWorkGroup) {
        this.isCheckWorkGroup = isCheckWorkGroup;
    }

    @Override
    public boolean getIsCheckOwnership() {
        return isCheckOwnership;
    }

    public void setIsCheckOwnership(boolean isCheckOwnership) {
        this.isCheckOwnership = isCheckOwnership;
    }
}
