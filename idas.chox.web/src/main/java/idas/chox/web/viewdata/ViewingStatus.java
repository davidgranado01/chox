package idas.chox.web.viewdata;

/**
 *
 * @author Emmanuel
 */
public class ViewingStatus {
    
    private Integer claimId;
    private String status;
            
    public ViewingStatus(Integer claimId,Boolean status)
    {
        this.claimId = claimId;
        this.status = status ? "Yes" : "No";
    }

    public Integer getClaimId() {
        return claimId;
    }

    public String getStatus() {
        return status;
    }

}
