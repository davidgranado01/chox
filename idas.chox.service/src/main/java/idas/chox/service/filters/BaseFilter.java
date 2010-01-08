/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.service.filters;

import idas.chox.core.model.Filter;
import idas.chox.core.search.ClaimSearchCriteria;
import idas.chox.core.search.SearchResult;
import idas.chox.core.services.ClaimService;

public abstract class BaseFilter implements Filter {

    private ClaimService claimService;

    protected abstract boolean getIsCheckWorkGroup();

    protected abstract boolean getIsCheckOwnership();

    protected abstract ClaimSearchCriteria getClaimSearchCriteria();

    @Override
    public Integer getCount() {
        return claimService.countClaims(getClaimSearchCriteria());
    }

    @Override
    public SearchResult getResults(int start, int limit, String sort, String dir) {
        return claimService.searchClaims(getClaimSearchCriteria(), start, limit, sort, dir);
    }

    @Override
    public String getDescription() {
        return String.format("%s (%d)", getName(), getCount().intValue());
    }

    @Override
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
