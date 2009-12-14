package idas.chox.service.reports.viewdata;

import java.util.List;

public class ClaimRejection {
    
    private List<ClaimRejectionLineItem> claimRejectionLineItem;
    private List<String> orgName;

    public List<ClaimRejectionLineItem> getClaimRejectionLineItem() {
        return claimRejectionLineItem;
    }

    public void setClaimRejectionLineItem(List<ClaimRejectionLineItem> claimRejectionLineItem) {
        this.claimRejectionLineItem = claimRejectionLineItem;
    }

    public List<String> getOrgName() {
        return orgName;
    }

    public void setOrgName(List<String> orgName) {
        this.orgName = orgName;
    }
    
    
}
