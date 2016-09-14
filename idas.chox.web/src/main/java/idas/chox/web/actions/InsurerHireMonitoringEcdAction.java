package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.InsurerHireMonitoringEcd;
import idas.chox.core.services.HireMonitoringEcdService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ReasonOfDelayService;
import idas.chox.service.security.TabAccessibility;
import idas.chox.web.viewdata.HireMonitoringEcdViewData;
import net.sf.json.JSONArray;

/**
 *
 * @author Emmanuel
 */
public class InsurerHireMonitoringEcdAction extends ClaimModelAction<InsurerHireMonitoringEcd> {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerHireMonitoringEcdAction.class);
    private JSONArray jObject;

    private ReasonOfDelayService reasonOfDelayService;
    private Integer iECDFormAccessRight;
    private List reasonOfDelay;
    private LookupService lookupService;
    private int reasonOfDelayId = -1;
    private HireMonitoringEcdService hireMonitoringEcdService;

    @Override
    String getTabName() {
        return TabAccessibility.TAB_INSURER_HIRE_MONITORING;
    }

    @Override
    public InsurerHireMonitoringEcd loadModel() {
        return new InsurerHireMonitoringEcd();
    }

    public List getReasonOfDelay() {
        if (reasonOfDelay == null) {
            reasonOfDelay = this.lookupService.getReasonOfDelay();
        }
        return reasonOfDelay;
    }

    public List<String> getReasonTypes() {

        List<String> reasonTypes = new ArrayList<>();
        reasonTypes.add("Additional Damage");
        reasonTypes.add("Failed QC");
        reasonTypes.add("First ECD");
        reasonTypes.add("Gone To Dealers");
        reasonTypes.add("Parts Delay");
        reasonTypes.add("Repairs Taking Longer Than Expected");
        reasonTypes.add("Total Loss");
        reasonTypes.add("Other");
        return reasonTypes;

    }

    public boolean isECDFormVisible() {

        Boolean bFlag = false;

        if (iECDFormAccessRight == 2) {
            bFlag = true;
        }
        return bFlag;

    }

    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("InsurerHireMonitoringEcdAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("InsurerHireMonitoringEcdAction validate success");
        }
        else {
            LOG.debug(" InsurerHireMonitoringEcdAction validation is not done as claim is null");
        }
    }

    public void setReasonOfDelayService(ReasonOfDelayService reasonOfDelayService) {
        this.reasonOfDelayService = reasonOfDelayService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public Integer getIECDFormAccessRight() {
        return iECDFormAccessRight;
    }

    public void setIECDFormAccessRight(Integer iECDFormAccessRight) {
        this.iECDFormAccessRight = iECDFormAccessRight;
    }

    public int getReasonOfDelayId() {
        return reasonOfDelayId;
    }

    public void setReasonOfDelayId(int reasonOfDelayId) {
        this.reasonOfDelayId = reasonOfDelayId;
    }

    public void setHireMonitoringEcdService(HireMonitoringEcdService hireMonitoringEcdService) {
        this.hireMonitoringEcdService = hireMonitoringEcdService;
    }
    public String getInsurerHireMonitoringEcds() {

        List<InsurerHireMonitoringEcd> hireMonitoringEcds = claim.getInsurerHireMonitoringEcds();

        List<HireMonitoringEcdViewData> viewDatas = new ArrayList<>();
        int seq = 1;
        for (InsurerHireMonitoringEcd h : hireMonitoringEcds) {
            viewDatas.add(new HireMonitoringEcdViewData(h, seq));
            seq++;
        }

        jObject = JSONArray.fromObject(viewDatas);

        return SUCCESS;
    }

    public String getJsonArrayData() {

        if (jObject != null) {
            return "{totalCount:" + this.jObject.size() + ",results:" + jObject.toString() + "}";
        }
        return "";

    }

}
