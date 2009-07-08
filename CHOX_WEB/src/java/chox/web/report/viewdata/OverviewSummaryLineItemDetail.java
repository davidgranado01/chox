package chox.web.report.viewdata;

import java.math.BigDecimal;

public class OverviewSummaryLineItemDetail {
    
    private int orgId;
    private String orgName;
    private Integer noCount;
    private BigDecimal totalValue;
    private BigDecimal totalPercentage;
    private Integer totalDay;

    public Object getNoCount() {
    
        if(noCount==null){
            return " ";
        }else{
            return noCount;
        }     
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

    public Object getTotalDay() {
        if(totalDay==null){
            return " ";
        }else{
            return totalDay;
        }            
    }

    public void setTotalDay(Integer totalDay) {
        this.totalDay = totalDay;
    }

    public Object getTotalPercentage() {
        if(totalPercentage==null){
            return " ";
        }else{
            return totalPercentage;
        }        
    }

    public void setTotalPercentage(BigDecimal totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public Object getTotalValue() {
        
        if(totalValue==null){
            return " ";
        }else{
            return totalValue;
        }
        
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }
    
    
}
