/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.model.EngineerReport;
import chox.services.EngineerReportService;
import chox.web.security.ApplicationAccessibility;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import net.sf.json.JSONObject;

/**
 *
 * @author Emmanuel
 */
public class EngineerReportAction extends BaseModelAction implements ModelDriven<EngineerReport>, Preparable {

    private EngineerReportService service;
    private EngineerReport model;    

    public void setEngineerReportService(EngineerReportService service) {
        this.service = service;
    }

    public EngineerReport getModel() {
        return model;
    }

    public void prepare() throws Exception {
        if (objectId == -1) {
            model = new EngineerReport();
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
        return ApplicationAccessibility.TAB_INVOICE_DETAIL;
    }
}
