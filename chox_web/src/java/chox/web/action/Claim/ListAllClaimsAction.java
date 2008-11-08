package chox.web.action.Claim;

import chox.web.action.*;
import chox.model.Claim;
import chox.services.ClaimService;
import java.util.List;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.ParentPackage;

/**
 *
 * @author Emmanuel
 */
@ParentPackage("base-package")
@Result(name = "success", value = "/WEB-INF/jsp/listClaims-partial.jsp")
public class ListAllClaimsAction extends BaseAction {
    
    public ListAllClaimsAction() {
    }
    private List<Claim> results;
    private ClaimService service;

    public void setClaimService(ClaimService service) {
        this.service = service;
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