/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.Util.DateHelper;
import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.model.HireMonitoringEcd;
import chox.services.HireMonitoringEcdService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdAction extends BaseModelAction implements ModelDriven<HireMonitoringEcd>, Preparable {

    private HireMonitoringEcd model;
    private HireMonitoringEcdService service;
    private Boolean isECDFormVisible = false;
    
    public void setHireMonitoringEcdService(HireMonitoringEcdService service)
    {
        this.service = service;
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_HIRE_MONITORING;
    }

    public HireMonitoringEcd getModel() {
        return model;
    }

    public void prepare() throws Exception {
        model = new HireMonitoringEcd();
    }

    public String addNewHireMonitoringEcd() {
        try {
            Claim claim = claimService.getClaim(claimId);
            model.setClaim(claim);
            this.service.updateObject(model);

            this.actionResult = "";
        } catch (Exception ex) {
            this.actionResult = "ERROR :" + ex.getMessage();
        }
        return SUCCESS;
    }

    public List<String> getReasonTypes() {
        List<String> reasonTypes = new ArrayList<String>();        
        reasonTypes.add("Parts Delay");
        reasonTypes.add("Incorrect Parts");
        reasonTypes.add("Parts Damaged");
        reasonTypes.add("Delayed Insurer Approval");
        reasonTypes.add("Customer Delay");
        reasonTypes.add("Resource Inefficiencies");
        reasonTypes.add("Inspection Delay");
        reasonTypes.add("Other");
        return reasonTypes;

    }

    /* Edited by: Carlson
     * Edited Date: 20081216
     * Source: According to Emm, the NEW ECD only can be added by CHO 
     * and Where the claim status is either AwaitingInvoiceData OR AwaitingCarHireInfo
     */
    public Boolean getIsECDFormVisible() {
        Boolean bFlag = false;
        Claim claim = claimService.getClaim(claimId);        
        if(this.getIsCHO() 
            && (claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_CAR_HIRE_INFO)
            || claim.getStatus().equalsIgnoreCase(ClaimStatus.AWAITING_INVOICE_DATA))
        ){
            bFlag = true;
        }
        return bFlag;
    }    
    
}
