/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Injury;
import chox.services.InjuryService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class InjuryAction extends AccessibilityControlAction implements ModelDriven<Injury>, Preparable {

    private InjuryService service;
    private Injury model;
    private int injuryId = -1;
    private String claimStatus;
    private String actionResult;

    public int getInjuryId() {
        return injuryId;
    }

    public void setInjuryId(int id) {
        this.injuryId = id;
    }
    
    public void setClaimStatus(String claimStatus)
    {
        this.claimStatus = claimStatus;
    }

    public void setInjuryService(InjuryService service) {
        this.service = service;
    }

    public Injury getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (injuryId == -1) {
            model = new Injury();
        } else {
            model = service.getInjury(injuryId);
        }
    }

    public String getActionResult() {
        return actionResult;
    }

    public String updateInjury() {
        try {
            this.service.updateInjury(model);
            this.actionResult = "";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    public String getJsonData() {
        JSONObject jObject = JSONObject.fromObject(this.model);
        return jObject.toString();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }

    @Override
    String getCaimStatus() {
        return claimStatus;
    }
    
    
}
