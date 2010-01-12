/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.model.HireMonitoringEcd;
import idas.chox.core.model.ReasonOfDelay;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ReasonOfDelayService;
import idas.chox.service.notifications.ClaimAnomalousChecker;
import idas.chox.service.notifications.EcdUpdatedNotification;
import idas.chox.service.security.ApplicationAccessibility;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class HireMonitoringEcdAction extends ClaimModelAction<HireMonitoringEcd> {

    private ReasonOfDelayService reasonOfDelayService;
    private Integer iECDFormAccessRight;
    private List reasonOfDelay;
    private LookupService lookupService;
    private int reasonOfDelayId = -1;
    private ClaimAnomalousChecker newECDAddedChecker;
    private boolean isUpdateInsurer;

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_HIRE_MONITORING;
    }

    @Override
    public HireMonitoringEcd loadModel(){
        return new HireMonitoringEcd();
    }

    public String addNewHireMonitoringEcd() {

        try {

            if (reasonOfDelayId > 0) {

                ReasonOfDelay reasonOfDelayObject = reasonOfDelayService.getReasonOfDelay(reasonOfDelayId);
                model.setReason(reasonOfDelayObject.getName());

                claim.addHireMonitoringEcd(model);

                List notifications = newECDAddedChecker.getAnomalousNotifications(claim);
                claim.AddNotifications(newECDAddedChecker.getAnomalousChecks(), notifications);

                if (isIsUpdateInsurer()) {
                    claim.AddNotification(new EcdUpdatedNotification());
                }

                super.updateModel();

            }

        } catch (Exception ex) {
            handleException(ex);
        }

        return SUCCESS;
    }

    public List getReasonOfDelay() {
        if (reasonOfDelay == null) {
            reasonOfDelay = this.lookupService.getReasonOfDelay();
        }
        return reasonOfDelay;
    }

    /* Edited by: Carlson
     * Edited Date: 20090708
     * Description: New Reason Type
     */
    public List<String> getReasonTypes() {

        List<String> reasonTypes = new ArrayList<String>();
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

    /* Edited by: Carlson
     * Edited Date: 20081216
     * Source: According to Emm, the NEW ECD only can be added by CHO 
     * and Where the claim status is either AwaitingInvoiceData OR AwaitingCarHireInfo
     */
    public Boolean getIsECDFormVisible() {

        Boolean bFlag = false;

        if (iECDFormAccessRight == 2) {
            bFlag = true;
        }

        return bFlag;

    }

    public void setReasonOfDelayService(ReasonOfDelayService reasonOfDelayService) {
        this.reasonOfDelayService = reasonOfDelayService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public void setNewECDAddedChecker(ClaimAnomalousChecker claimAnomalousChecker) {
        this.newECDAddedChecker = claimAnomalousChecker;
    }

    public boolean isIsUpdateInsurer() {
        return isUpdateInsurer;
    }

    public void setIsUpdateInsurer(boolean isUpdateInsurer) {
        this.isUpdateInsurer = isUpdateInsurer;
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
}
