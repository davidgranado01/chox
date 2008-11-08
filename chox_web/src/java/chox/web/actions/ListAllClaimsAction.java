package chox.web.actions;

import chox.model.Claim;
import chox.services.ClaimService;
import java.util.List;
/**
 *
 * @author Emmanuel
 */
public class ListAllClaimsAction extends BaseAction {
    
    private int number;
    private List<Claim> results;
    private ClaimService service;
    
    public ListAllClaimsAction() {
    }
    

    public void setClaimService(ClaimService service) {
        this.service = service;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }

    public List<Claim> getResults() {
        return results;
    }

    @Override
    public String execute() throws Exception {
        results = this.service.getAllClaims();
        return SUCCESS;
    }
}