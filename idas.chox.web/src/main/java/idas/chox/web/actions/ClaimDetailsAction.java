package idas.chox.web.actions;

import java.util.Date;

import idas.chox.core.model.Claim;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.TabAccessibility;

/**
 *
 * @author Emmanuel
 */
public class ClaimDetailsAction extends ClaimModelAction<Claim> {

    private boolean managingRepair;
    
    @Override
    public Claim loadModel() {
        managingRepair = claim.isManagingRepair();
        return claim;
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }

    @Override
    public String updateModel() {
        if (managingRepair != model.getManagingRepair() && model.getManagingRepairOriginal() == null) {
            model.setManagingRepairOriginal(managingRepair);
        }
        if (managingRepair != model.getManagingRepair()) {
            model.setManagingRepairLastModified(new Date());
        }
        return super.updateModel();
    }
    
    public String getGtaNoticeDate() {
        return DateHelper.getLocalDateTimeFormat().format(model.getGtaNoticeDate());
    }
    public String getCreditAgreementDate() {
        return DateHelper.getLocalDateTimeFormat().format(model.getCreditAgreementDate());
    }

}
