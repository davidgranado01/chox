/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.services.ClaimService;
import chox.services.LookupService;
import chox.web.security.TabAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class claimDetailAction extends BaseAction implements ModelDriven<Claim>, Preparable {

    private Claim claim = new Claim();
    private int id = -1;
    private List statuses;
    private ClaimService service;
    private LookupService lookupService;
    private String actionMessage;  
    private TabAccessibility tabAccessibility;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public Claim getModel() {
        return claim;
    }

    public void prepare() throws Exception {
        if (id == -1) {
            claim = new Claim();
        } else {
            claim = service.getClaim(id);
        } 
    }

    public List getStatuses() {
        if (statuses == null) {
            statuses = this.lookupService.getStatuses();
        }
        return statuses;
    }

    public String getActionMessage() {
        return actionMessage;
    }

    public String updateClaimDetail() {
        this.service.updateClaim(claim);
        this.actionMessage = "Claim Updated!";
        return SUCCESS;
    }

    public TabAccessibility getTabAccessible() {
        return tabAccessibility;
    }  

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }
}
