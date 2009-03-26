package chox.web.report.viewdata;

import java.math.BigDecimal;

public class OverviewSummaryLineItemDetail {
    
    private int orgId;
    private String orgName;
    private Integer noCount;
    private BigDecimal totalValue;
    private BigDecimal totalPercentage;
    private Integer totalDay;

    public Integer getNoCount() {
        return noCount;
    }

    public void setNoCount(Integer noCount) {
        this.noCount = noCount;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(Integer totalDay) {
        this.totalDay = totalDay;
    }

    public BigDecimal getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(BigDecimal totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
    
    
}
