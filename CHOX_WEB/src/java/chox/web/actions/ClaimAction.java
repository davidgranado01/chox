/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.LineOfBusiness;
import chox.services.ClaimService;
import chox.services.LookupService;
import chox.web.data.PanelAction;
import chox.web.security.ApplicationAccessibility;
import chox.web.security.TabAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.List;
import org.acegisecurity.GrantedAuthority;

/**
 *  
 * @author Emmanuel
 */
public class ClaimAction extends BaseAction implements ModelDriven<Claim>, Preparable {

    public static final String EMPTY = "empty";
    private Claim claim = new Claim();
    private int id = -1;
    private List lineOfBusinesses;
    private List statuses;
    private ClaimService service;
    private LookupService lookupService;
    private String actionResult;
    private TabAccessibility tabAccessibility;
    private int lineOfBusinessId = -1;

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

    public List getLineOfBusinesses() {
        if (lineOfBusinesses == null) {
            lineOfBusinesses = lookupService.getLineOfBusinesses();
        }
        return lineOfBusinesses;
    }

    public String getActionResult() {
        return actionResult;
    }

    public String updateClaimDetail() {
        this.service.updateClaim(claim);
        this.actionResult = "Claim Updated!";
        return SUCCESS;
    }

    public String updateIncident() {
        this.service.updateIncident(claim.getIncident());
        this.actionResult = "Incident Updated!";
        return SUCCESS;
    }

    public TabAccessibility getTabAccessibility() {

        if (tabAccessibility == null) {
            tabAccessibility = new TabAccessibility(getAuthenticatedUser().getAuthorities(), claim.getStatus());
        }
        return tabAccessibility;
    }

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    public String getActionPanel() {
        GrantedAuthority[] grantedAuthorities = getAuthenticatedUser().getAuthorities();
        List<String> actions = PanelAction.getPanelActions();

        for (String action : actions) {
            short accessRight = ApplicationAccessibility.getInstance().checkActionAccessibility(action, grantedAuthorities, claim.getStatus());

            if (accessRight > 0) {
                return action;
            }
        }

        return EMPTY;
    }
    //Claim Actions
    public String route() {
        //chack whether line of busineess if set 
        if (this.getLineOfBusinesses() == null) {
            this.actionResult = "ERROR : You need to provide line of business to route this claim.";
        } else {
            if (!claim.getStatus().equalsIgnoreCase(ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED)) {
                this.actionResult = "ERROR : Invalid operation!";
            } else {
                //LineOfBusiness lob = new LineOfBusiness();
                //lob.setId(this.lineOfBusinessId);
                //claim.setLineOfBusiness(lob);
                claim.setStatus(ClaimStatus.CLAIM_UNACKNOWLEDGED_ROUTED);
                try {
                    this.service.updateClaim(claim);
                } catch (Exception ex) {
                    this.actionResult = "ERROR : " + ex.getMessage();
                }
            }
        }

        return SUCCESS;
    }

    public String acknowledge() {
        //chack whether line of busineess if set 
        if (validateAcknowledgeClaimInfo()) {

            claim.setStatus(ClaimStatus.AWAITING_CAR_HIRE_INFO);
            try {
                this.service.updateClaim(claim);
            } catch (Exception ex) {
                this.actionResult = "ERROR : " + ex.getMessage();
            }

        } else {
            this.actionResult = "ERROR : You need to correct detail to acknowledge this claim.";
        }
        return SUCCESS;
    }

    private boolean validateAcknowledgeClaimInfo() {
        return true;
    }

    public int getLineOfBusinessId() {
        return lineOfBusinessId;
    }

    public void setLineOfBusinessId(int lineOfBusinessId) {
        this.lineOfBusinessId = lineOfBusinessId;
    }
}
