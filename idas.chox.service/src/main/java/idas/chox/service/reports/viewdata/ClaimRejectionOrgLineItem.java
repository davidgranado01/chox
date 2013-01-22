package idas.chox.service.reports.viewdata;

import java.util.List;

public class ClaimRejectionOrgLineItem {
    
    private String orgName;
    private List<Integer> orgClaimCounts;
    private List<String> orgClaimCountPercs;


    public List<Integer> getOrgClaimCounts() {
        return orgClaimCounts;
    }

    public void setOrgClaimCounts(List<Integer> orgClaimCounts) {
        this.orgClaimCounts = orgClaimCounts;
    }

    public List<String> getOrgClaimCountPercs() {
        return orgClaimCountPercs;
    }

    public void setOrgClaimCountPercs(List<String> orgClaimCountPercs) {
        this.orgClaimCountPercs = orgClaimCountPercs;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
    
}
