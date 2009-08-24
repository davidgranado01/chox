/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Claim;
import chox.model.ClaimStatus;
import chox.services.ClaimService;
import chox.web.data.FilterRecordCounter;
import chox.web.security.ApplicationAccessibility;
import chox.web.security.FilterAccessibility;
import chox.web.security.MenuAccessibility;
import chox.web.security.ReportAccessibility;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import chox.services.AuditTrailService;
import chox.services.SystemLogService;
import chox.web.security.AdminAccessibility;

/**
 *
 * @author Emmanuel
 */
public class InboxAction extends BaseAction implements SessionAware {

    private Map session;
    private ClaimService service;
    private ApplicationAccessibility applicationAccessibility;
    private ReportAccessibility reportAccessibility;
    private MenuAccessibility menuAccessibility;
    private AdminAccessibility adminAccessibility;
    private AuditTrailService auditTrailService;
    private SystemLogService systemLogService;
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
            reportAccessibility = getApplicationAccessibility().getReportAccessibility(super.getAuthenticatedUser().getAuthorities());
        }
        return reportAccessibility;
    }
    
    public AdminAccessibility getAdminAccessibility() {
        
        if (adminAccessibility == null) {
            adminAccessibility = getApplicationAccessibility().getAdminAccessibility(super.getAuthenticatedUser().getAuthorities());
        }
        return adminAccessibility;
    }
    
    public MenuAccessibility getMenuAccessibility() {
        if (menuAccessibility == null) {
            menuAccessibility = getApplicationAccessibility().getMenuAccessibility(super.getAuthenticatedUser().getAuthorities());
        }
        return menuAccessibility;
    }
    
    public boolean getIsApprovePaymentAccessibile()
    {
        short accessRight = applicationAccessibility.checkActionAccessibility("logInvoicePayment", super.getAuthenticatedUser().getAuthorities(), ClaimStatus.AWAITING_INVOICE_PAYMENT);
        return accessRight > 0;
    }
    
    public boolean getIsClearBREApprovedInvoicesForPaymentAccessibile()
    {
        short accessRight = applicationAccessibility.checkActionAccessibility("approveBREPassedClaim", super.getAuthenticatedUser().getAuthorities(), ClaimStatus.INVOICE_APPROVED_BY_BRE);
        return accessRight > 0;
    }

	// SPRINT 8
    public boolean getIsDoInvoicePaymentReceivedAccessibile()
    {
        short accessRight = applicationAccessibility.checkActionAccessibility("doInvoicePaymentReceived", super.getAuthenticatedUser().getAuthorities(), ClaimStatus.INVOICE_PAYMENT_LOGGED);
        return accessRight > 0;
    }
    
    public boolean getIsDoClaimRoutedAccessibile()
    {
        short accessRight = applicationAccessibility.checkActionAccessibility("routeClaims", super.getAuthenticatedUser().getAuthorities(), ClaimStatus.CLAIM_UNACKNOWLEDGED_UNROUTED);
        return accessRight > 0;
    }
    
    
    public void setSession(Map arg0) {
        this.session = arg0;
    }   
    
    public Integer getTab()
    {
        if(session.containsKey("tabIndex")){
            return (Integer)this.session.get("tabIndex");
        }
        else
        {
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

    public void setSystemLogService(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }
}

