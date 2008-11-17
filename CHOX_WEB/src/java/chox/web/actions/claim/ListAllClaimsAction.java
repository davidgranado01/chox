package chox.web.actions.claim;

import chox.web.actions.*;
import chox.services.ClaimService;
import java.util.List;
import net.sf.json.JSONArray;
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
    
    public String getJasonData()
    {
        JSONArray jsonArray = JSONArray.fromObject(results); 
        return jsonArray.toString();
    }

    @Override
    public String execute() throws Exception {
        results = this.service.listAllClaims();
        return SUCCESS;
    }
}