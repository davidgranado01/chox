package idas.chox.web.actions;

import idas.chox.core.model.ClaimStatus;
import idas.chox.core.services.AuditTrailService;
import idas.chox.core.services.ClaimService;
import idas.chox.service.security.AdminAccessibility;
import idas.chox.service.security.ApplicationAccessibility;
import idas.chox.service.security.MenuAccessibility;
import idas.chox.service.security.ReportAccessibility;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;

public class InboxAction extends BaseAction implements SessionAware {

    private Map session;
    private ClaimService service;
    private ApplicationAccessibility applicationAccessibility;
    private ReportAccessibility reportAccessibility;
    private MenuAccessibility menuAccessibility;
    private AdminAccessibility adminAccessibility;
    private AuditTrailService auditTrailService;
    private String actionResult;

    public void setClaimService(ClaimService service) {
        this.service = service;
    }

    @Override
    public String execute() throws Exception {

        return SUCCESS;
    }

    public ReportAccessibility getReportAccessibility() {

        if (reportAccessibility == null) {
            reportAccessibility = getApplicationAccessibility().getReportAccessibility(super.getAuthenticatedUser().getRoles());
        }
        return reportAccessibility;
    }

    public AdminAccessibility getAdminAccessibility() {

        if (adminAccessibility == null) {
            adminAccessibility = getApplicationAccessibility().getAdminAccessibility(super.getAuthenticatedUser().getRoles());
        }
        return adminAccessibility;
    }

    public MenuAccessibility getMenuAccessibility() {
        if (menuAccessibility == null) {
            menuAccessibility = getApplicationAccessibility().getMenuAccessibility(super.getAuthenticatedUser().getRoles());
        }
        return menuAccessibility;
    }

    public boolean getIsApprovePaymentAccessibile() {
        short accessRight = applicationAccessibility.checkActionAccessibility("logInvoicePayment", super.getAuthenticatedUser().getRoles(), ClaimStatus.AWAITING_INVOICE_PAYMENT);
        return accessRight > 0;
    }

    public boolean getIsClearBREApprovedInvoicesForPaymentAccessibile() {
        short accessRight = applicationAccessibility.checkActionAccessibility("approveBREPassedClaim", super.getAuthenticatedUser().getRoles(), ClaimStatus.INVOICE_APPROVED_BY_BRE);
        return accessRight > 0;
    }

    public boolean getIsDoInvoicePaymentReceivedAccessibile() {
        short accessRight = applicationAccessibility.checkActionAccessibility("doInvoicePaymentReceived", super.getAuthenticatedUser().getRoles(), ClaimStatus.INVOICE_PAYMENT_LOGGED);
        return accessRight > 0;
    }

    public boolean getIsDoClaimRoutedAccessibile() {
        short accessRight = applicationAccessibility.checkActionAccessibility("routeClaims", super.getAuthenticatedUser().getRoles(), ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        return accessRight > 0;
    }

    public boolean getIsDoClaimOwnershipAccessibile() {
        short accessRight = applicationAccessibility.checkActionAccessibility("claimOwnership", super.getAuthenticatedUser().getRoles(), ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        return accessRight > 0;
    }

    // TODO: REFACTORING TO SEPERATE BATCH UPDATE FROM GENERAL ACTION
    public boolean getIsDoUpdateClaimOwnershipAccessibile()
    {
        short accessRight = applicationAccessibility.checkActionAccessibility("claimOwnership", super.getAuthenticatedUser().getRoles(), ClaimStatus.CLAIM_UNACKNOWLEDGED_UNASSIGNED);
        return accessRight > 0;
    }
    
    public void setSession(Map arg0) {
        this.session = arg0;
    }

    public Integer getTab() {
        if (session.containsKey("tabIndex")) {
            return (Integer) this.session.get("tabIndex");
        } else {
            return 0;
        }
    }

    public ApplicationAccessibility getApplicationAccessibility() {
        return applicationAccessibility;
    }

    public void setApplicationAccessibility(ApplicationAccessibility applicationAccessibility) {
        this.applicationAccessibility = applicationAccessibility;
    }

    public AuditTrailService getAuditTrailService() {
        return auditTrailService;
    }

    public void setAuditTrailService(AuditTrailService auditTrailService) {
        this.auditTrailService = auditTrailService;
    }

    public String getActionResult() {
        return actionResult;
    }

    public void setActionResult(String actionResult) {
        this.actionResult = actionResult;
    }
}
