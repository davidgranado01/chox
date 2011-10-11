package idas.chox.core.model;

import idas.chox.core.security.SecurityInfoProvider;

/**
 *
 * @author emmanuel
 */
public interface IntelligentNote {
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider);
    public String getNote();
}
