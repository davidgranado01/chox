/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.web.security.ApplicationAccessibility;
import idas.chox.web.viewdata.HireMonitoringEcdViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class GetHireMonitoringEcdsAction extends BaseModelAction {

    private List<HireMonitoringEcdViewData> hireMonitoringEcds;

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.hireMonitoringEcds);

        return "{totalCount:" + this.hireMonitoringEcds.size() + ",results:" + jObject.toString() + "}";

    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_NOTES;
    }

    public String getHireMonitoringEcds() {

        Claim c = claimService.getClaim(claimId);
        List<HireMonitoringEcd> hireMonitoringEcdsData = c.getHireMonitoringEcds();

        hireMonitoringEcds = new ArrayList<HireMonitoringEcdViewData>();
        int seq = 1;
        for (HireMonitoringEcd h : hireMonitoringEcdsData) {
            hireMonitoringEcds.add(new HireMonitoringEcdViewData(h, seq));
            seq++;
        }

        return SUCCESS;
    }
}
