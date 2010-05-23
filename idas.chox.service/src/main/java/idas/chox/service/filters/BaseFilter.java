/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.filters;

import idas.chox.core.model.Filter;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.services.ClaimService;

public abstract class BaseFilter implements Filter {

    private ClaimService claimService;
    private Integer count;
    private boolean isFilterWorkGroup = false;
    private boolean isFilterOwnership = false;
    private boolean isFilterSupplierOwnership = false;
    private boolean isCheckWorkGroup = false;
    private boolean isCheckOwnership = false;
    private boolean isCheckFnol = false;
    private boolean isCheckEngineers = false;

    @Override
    public boolean getIsCheckEngineers() {
        return isCheckEngineers;
    }

    public void setIsCheckEngineers(boolean isCheckEngineers) {
        this.isCheckEngineers = isCheckEngineers;
    }

    @Override
    public boolean getIsCheckFnol() {
        return isCheckFnol;
    }

    public void setIsCheckFnol(boolean isCheckFnol) {
        this.isCheckFnol = isCheckFnol;
    }

    @Override
    public boolean getIsCheckOwnership() {
        return isCheckOwnership;
    }

    public void setIsCheckOwnership(boolean isCheckOwnership) {
        this.isCheckOwnership = isCheckOwnership;
    }

    @Override
    public boolean getIsCheckWorkGroup() {
        return isCheckWorkGroup;
    }

    public void setIsCheckWorkGroup(boolean isCheckWorkGroup) {
        this.isCheckWorkGroup = isCheckWorkGroup;
    }

    @Override
    public boolean getIsFilterWorkGroup() {
        return isFilterWorkGroup;
    }

    public void setIsFilterWorkGroup(boolean isFilterWorkGroup) {
        this.isFilterWorkGroup = isFilterWorkGroup;
    }

    @Override
    public boolean getIsFilterOwnership() {
        return isFilterOwnership;
    }

    public void setIsFilterOwnership(boolean isFilterOwnership) {
        this.isFilterOwnership = isFilterOwnership;
    }

    @Override
    public boolean getIsFilterSupplierOwnership() {
        return isFilterSupplierOwnership;
    }

    public void setIsFilterSupplierOwnership(boolean isFilterSupplierOwnership) {
        this.isFilterSupplierOwnership = isFilterSupplierOwnership;
    }

    @Override
    public abstract ClaimSearchCriteria getClaimSearchCriteria();

    @Override
    public Integer getCount() {
        return count;
    }

    @Override
    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String getDescription() {
        return String.format("%s (%d)", getName(), getCount().intValue());
    }
}
