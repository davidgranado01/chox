/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.services.ClaimService;
import java.util.List;

public class AlertAction extends BaseAction {

    private ClaimService claimService;
    private Integer claimId;
    private String claimNumber;
    private List duplicatedClaims;
    private String actionResult;

    public String getDuplicatedClaimAlert() {
        duplicatedClaims = claimService.getOtherClaimsByClaimNumber(claimNumber, claimId);
        return SUCCESS;
    }
    
    public String checkIsClaimNumberDuplicated() {
        Boolean isDuplicated = false;
        if(!claimNumber.isEmpty())
        {
            isDuplicated = claimService.getClaimCountByClaimNumber(claimNumber, claimId) > 0;
        }
        this.getActionResponse().AssignYesNoResult(isDuplicated);
        return SUCCESS;
    }

    public List getOtherDuplicatedClaims() {
        return duplicatedClaims;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public Integer getClaimId() {
        return claimId;
    }

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public String getActionResult() {
        return actionResult;
    }
}
