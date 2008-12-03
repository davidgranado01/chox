/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Witness;
import chox.services.WitnessService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class WitnessAction extends BaseModelAction implements ModelDriven<Witness>, Preparable {

    private WitnessService service;
    private Witness model;

    public void setWitnessService(WitnessService service) {
        this.service = service;
    }

    public Witness getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId <= 0) {
            model = new Witness();
        } else {
            model = service.getObject(objectId);
        }
    }
    
    public String updateModel() {
        try {
            this.service.updateObject(model);
            this.actionResult = "1";
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
   
    
}
