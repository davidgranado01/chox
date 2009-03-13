/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.report.viewdata;

import java.math.BigDecimal;

public class ClaimRejectionLineItemDetail {
    
    private Integer numberOfClaim = 0;
    private BigDecimal numberOfClaimPercentage;

    public Integer getNumberOfClaim() {
        return numberOfClaim;
    }

    public void setNumberOfClaim(Integer numberOfClaim) {
        this.numberOfClaim = numberOfClaim;
    }

    public BigDecimal getNumberOfClaimPercentage() {
        return numberOfClaimPercentage;
    }

    public void setNumberOfClaimPercentage(BigDecimal numberOfClaimPercentage) {
        this.numberOfClaimPercentage = numberOfClaimPercentage;
    }
    
    
}