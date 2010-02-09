/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Claim;
import idas.chox.core.util.DateHelper;
import idas.chox.service.security.ApplicationAccessibility;

/**
 *
 * @author Emmanuel
 */
public class ClaimDetailsAction extends ClaimModelAction<Claim> {

    @Override
    public Claim loadModel() {
        return getClaim();
    }

    @Override
    String getTabName() {
        return ApplicationAccessibility.TAB_CLAIM_DETAIL;
    }

    public String getGtaNoticeDate() {
        return DateHelper.LocalDateFormat.format(model.getGtaNoticeDate());
    }
    public String getCreditAgreementDate() {
        return DateHelper.LocalDateFormat.format(model.getCreditAgreementDate());
    }

    public void setGtaNoticeDate(String gtaNoticeDate) {
        model.setGtaNoticeDate(DateHelper.Parse(gtaNoticeDate));
    }
    public void setCreditAgreementDate(String creditAgreementDate) {
        model.setCreditAgreementDate(DateHelper.Parse(creditAgreementDate));
    }

}
