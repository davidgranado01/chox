package chox.model;

import chox.model.*;
import chox.data.SecurityInfoProvider;

public interface IntelligentNote {
    public Boolean isShowingFor(Claim c, SecurityInfoProvider securityInfoProvider);
    public String getNote();
}
