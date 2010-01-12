/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.services.ClaimService;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.web.viewdata.HireMonitoringEcdViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class GetHireMonitoringEcdsAction extends BaseAction {

    private List<HireMonitoringEcdViewData> hireMonitoringEcds;
    private Integer claimId;
    private ClaimService claimService;

    public void setClaimId(Integer claimId) {
        this.claimId = claimId;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.hireMonitoringEcds);

        return "{totalCount:" + this.hireMonitoringEcds.size() + ",results:" + jObject.toString() + "}";

    }

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

    /**
     * @param claimService the claimService to set
     */
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
}
