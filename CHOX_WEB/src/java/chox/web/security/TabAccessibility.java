package chox.web.security;
import chox.Util.AccessibilityHelper;
import chox.Util.RoleHelper;
import chox.model.Claim;
import chox.model.WebUser;
import chox.web.actions.BaseAction;
import org.acegisecurity.GrantedAuthority;

public class TabAccessibility extends BaseAction{

    private short claimDetailTabAccessibility;
    private short invoiceDetailTabAccessibility;
    private short hireMonitoringTabAccessibility;
    private short historyTabAccessibility;
    private short notesTabAccessibility;
    private short paymentPackTabAccessibility;
    private short auditTrailTabAccessibility;
    private Claim claim;
    
    public TabAccessibility(ApplicationAccessibility applicationAccessibility, GrantedAuthority[] grantedAuthorities, Claim claim) {

        setClaim(claim);
        claimDetailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_CLAIM_DETAIL, grantedAuthorities, claim.getStatus());
        hireMonitoringTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HIRE_MONITORING, grantedAuthorities, claim.getStatus());
        historyTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_HISTORY, grantedAuthorities, claim.getStatus());
        invoiceDetailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_INVOICE_DETAIL, grantedAuthorities, claim.getStatus());
        paymentPackTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_PAYMENT_PACK, grantedAuthorities, claim.getStatus());
        notesTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_NOTES, grantedAuthorities, claim.getStatus());
        auditTrailTabAccessibility = applicationAccessibility.checkTabAccessibility(ApplicationAccessibility.TAB_AUDIT_TRAIL, grantedAuthorities, claim.getStatus());
        
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }
   
    public short getClaimDetailTabAccessibility() {
        return doTabAccessibilityFilter(claimDetailTabAccessibility);
    }

    public short getInvoiceDetailTabAccessibility() {
        return doTabAccessibilityFilter(invoiceDetailTabAccessibility);
    }

    public short getHireMonitoringTabAccessibility() {
        return doTabAccessibilityFilter(hireMonitoringTabAccessibility);
    }

    public short getHistoryTabAccessibility() {
        return doTabAccessibilityFilter(historyTabAccessibility);
    }

    public short getNotesTabAccessibility() {
        return doTabAccessibilityFilter(notesTabAccessibility);
    }

    public short getPaymentPackTabAccessibility() {
        return doTabAccessibilityFilter(paymentPackTabAccessibility);
    }

    public short getAuditTrailTabAccessibility() {
        return doTabAccessibilityFilter(auditTrailTabAccessibility);
    }

    private Short doTabAccessibilityFilter(Short iResult){

        // ACCESS RIGHT IS 2 (EDITABLE)
        if(iResult>=2){

            WebUser user = getAuthenticatedUser().getUser();

            // CHECK THIS USER IS CH OR COM WITH WORKGROUP ENABLE
            if(RoleHelper.isClaimEditableCheckByWorkgroupEnabled(user)){
                System.out.println(">>>> CLAIM WORKGROUP CHECK");
                if(!AccessibilityHelper.isClaimWorkgroupOwnByUser(user, this.claim)){
                    iResult = 1;
                }   
            }

            // CHECK THIS USER IS CH OR COM WITH OWNERSHIP ENABLE
            if(RoleHelper.isClaimEditableCheckByOwnerEnabled(user)){
                System.out.println(">>>> CLAIM OWNERSHIP CHECK");
                if(!AccessibilityHelper.isClaimOwnByUser(user, this.claim)){
                    iResult = 1;
                }  
            }
            
        }
        
        return iResult;
    }
    
}
