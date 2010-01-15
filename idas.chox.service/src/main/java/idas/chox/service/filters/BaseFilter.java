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

    protected abstract boolean getIsCheckWorkGroup();

    protected abstract boolean getIsCheckOwnership();

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
