package idas.chox.service.filters;

import idas.chox.core.model.Filter;
import idas.chox.core.security.SecurityInfoProvider;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseFilter implements Filter {

    private boolean isManualFilter = false;
    private boolean isFilterWorkGroup = false;
    private boolean isFilterOwnership = false;
    private boolean isFilterSupplierOwnership = false;
    private boolean isCheckWorkGroup = false;
    private boolean isCheckOwnership = false;
    private boolean isCheckFnol = false;
    private boolean isCheckEngineers = false;
    private boolean isCheckClaimMatching = false;
    private String queueDescription;
    @Autowired
    protected SecurityInfoProvider securityInfoProvider;

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider) {
        this.securityInfoProvider = securityInfoProvider;
    }

    @Override
    public boolean getIsCheckEngineers() {
        return isCheckEngineers;
    }

    public void setIsCheckEngineers(boolean isCheckEngineers) {
        this.isCheckEngineers = isCheckEngineers;
    }

    @Override
    public boolean getIsCheckClaimMatching() {
        return isCheckClaimMatching;
    }

    public void setIsCheckClaimMatching(boolean isCheckClaimMatching) {
        this.isCheckClaimMatching = isCheckClaimMatching;
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
    public boolean getIsManualFilter() {
        return isManualFilter;
    }

    public void setIsManualFilter(boolean isManualFilter) {
        this.isManualFilter = isManualFilter;
    }

    public String getQueueDescription() {
        return queueDescription;
    }

    public void setQueueDescription(String queueDescription) {
        this.queueDescription = queueDescription;
    }
    
}
