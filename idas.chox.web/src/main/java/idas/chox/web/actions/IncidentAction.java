/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Incident;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.Date;

/**
 *
 * @author Emmanuel
 */
public class IncidentAction extends ClaimModelAction<Incident> {

    @Override
    public Incident loadModel() {
        Incident incident = getClaim().getIncident();
        if (incident != null) {
            return incident;
        }
        return new Incident();
    }

    @Override
    public String updateModel() {

        claim.setIncident(model);
        return super.updateModel();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }

    public String getTime() {
        return DateHelper.TimeFormat.format(model.getDate());
    }

    public void setTime(String time) {
        if (model != null) {
            try {
                Date a = model.getDate();
                Date b = DateHelper.TimeFormat.parse(time);
                model.setDate(DateHelper.mergeTimeToDate(a, b));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
