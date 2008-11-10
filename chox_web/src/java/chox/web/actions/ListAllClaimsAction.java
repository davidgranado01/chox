package chox.web.actions;

import chox.services.ClaimService;
import java.util.List;
/**
 *
 * @author Emmanuel
 */
public class ListAllClaimsAction extends BaseAction {
    
    private int number;
    private List results;
    private ClaimService service;
    
    public ListAllClaimsAction() {
    }    

    public void setClaimService(ClaimService service) {
        this.service = service;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }

    public List getResults() {
        return results;
    }

    @Override
    public String execute() throws Exception {
        results = this.service.listAllClaims();
        return SUCCESS;
    }
}