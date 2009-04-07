/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.HireMonitoringEcd;
import chox.services.HireMonitoringEcdService;
import chox.web.security.ApplicationAccessibility;
import chox.web.viewdata.HireMonitoringEcdViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class GetHireMonitoringEcdsAction extends BaseModelAction {

    private HireMonitoringEcdService service;
    private List<HireMonitoringEcdViewData> hireMonitoringEcds;

    public void setHireMonitoringEcdService(HireMonitoringEcdService service) {
        this.service = service;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.hireMonitoringEcds);

        return "{totalCount:" + this.hireMonitoringEcds.size() + ",results:" + jObject.toString() + "}";

    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_NOTES;
    }

    public String getHireMonitoringEcds() {
        List<HireMonitoringEcd> hireMonitoringEcdsData = this.service.getHireMonitoringEcdsByClaimId(claimId);

        hireMonitoringEcds = new ArrayList<HireMonitoringEcdViewData>();
        int seq = 1;
        for (HireMonitoringEcd h : hireMonitoringEcdsData) {
            hireMonitoringEcds.add(new HireMonitoringEcdViewData(h,seq));
            seq++;
        }

        return SUCCESS;
    }

}