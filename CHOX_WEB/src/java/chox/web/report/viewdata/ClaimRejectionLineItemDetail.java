/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.report.viewdata;

import chox.Util.MathHelper;
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

    public String getNumberOfClaimPercentage() {
        return MathHelper.getExcelDisplayPerc(numberOfClaimPercentage);
    }

    public void setNumberOfClaimPercentage(BigDecimal numberOfClaimPercentage) {
        this.numberOfClaimPercentage = numberOfClaimPercentage;
    }
    
    
}