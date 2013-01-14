package idas.chox.service.reports.viewdata;

import java.math.BigDecimal;

import idas.chox.core.util.MathHelper;

public class ClaimRejectionSumLineItem {

    private Integer id;
    private String name;
    private String displayName;
    private Integer allOrgClaimCount;
    private BigDecimal allOrgClaimCountPerc;

    public Integer getAllOrgClaimCount() {
        return allOrgClaimCount;
    }

    public void setAllOrgClaimCount(Integer allOrgClaimCount) {
        this.allOrgClaimCount = allOrgClaimCount;
    }

    public String getAllOrgClaimCountPerc() {
        return MathHelper.getExcelDisplayPerc(allOrgClaimCountPerc);
    }

    public void setAllOrgClaimCountPerc(BigDecimal allOrgClaimCountPerc) {
        this.allOrgClaimCountPerc = allOrgClaimCountPerc;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getDisplayNameMap(String name) {
        return name;
    }

}
