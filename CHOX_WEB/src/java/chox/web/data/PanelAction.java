package chox.web.data;

import java.util.ArrayList;
import java.util.List;

public class PanelAction {

    public static final String ACTION_claimlineofbusiness = "updateclaimlineofbusiness";
    public static final String ACTION_claiminsurerdetail = "updateclaiminsurerdetail";
    public static final String ACTION_contestOrAcceptRejectedClaim = "contestOrAcceptRejectedClaim";
    public static final String ACTION_approveContestedClaim = "approveContestedClaim";
    public static final String ACTION_claimcarhiredetail = "updateclaimcarhiredetail";
    public static final String ACTION_uploadInvoiceData = "uploadInvoiceData";
    public static final String ACTION_invoicedtailforcalculationincorrect = "updateinvoicedtailforcalculationincorrect";
    public static final String ACTION_approveBREPassedClaim = "approveBREPassedClaim";
    public static final String ACTION_approveEscalatedInvoice = "approveEscalatedInvoice";
    public static final String ACTION_approveContestedInvoice = "approveContestedInvoice";
    public static final String ACTION_resubmitOrAcceptContestedInvoice = "resubmitOrAcceptContestedInvoice";
    public static final String ACTION_logInvoicePayment = "logInvoicePayment";

    public static List<String> getPanelActions() {
        List<String> action = new ArrayList<String>();
        action.add(ACTION_claimlineofbusiness);
        action.add(ACTION_claiminsurerdetail);
        action.add(ACTION_approveContestedClaim);
        action.add(ACTION_contestOrAcceptRejectedClaim);
        action.add(ACTION_claimcarhiredetail);
        action.add(ACTION_uploadInvoiceData);
        action.add(ACTION_invoicedtailforcalculationincorrect);
        action.add(ACTION_approveBREPassedClaim);
        action.add(ACTION_approveEscalatedInvoice);
        action.add(ACTION_approveContestedInvoice);
        action.add(ACTION_resubmitOrAcceptContestedInvoice);
        action.add(ACTION_logInvoicePayment);



        return action;
    }
}
