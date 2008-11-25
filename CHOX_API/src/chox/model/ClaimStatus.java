/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public class ClaimStatus {

    public static final String CLAIM_UNACKNOWLEDGED = "ClaimUnacknowledged";
    public static final String CLAIM_ACKNOWLEDGED = "ClaimAcknowledged";
    public static final String AWAITING_CAR_HIRE_INFO = "AwaitingCarHireInfo";
    public static final String CLAIM_REJECTED = "ClaimRejected";
    public static final String CLAIM_REJECTED_ACCEPTED = "ClaimRejectionAccepted";
    public static final String AWAITING_INVOICE_DATA = "AwaitingInvoiceData";
    public static final String INVOICE_APPROVED = "InvoiceApproved";
    public static final String INVOICE_PAYMENT_LOGGED = "InvoicePaymentLogged";
    public static final String INVOICE_ESCALATED = "InvoiceEscalated";
    public static final String DISPUTED_INVOICE = "DisputedInvoice";
    public static final String AWAITING_PAYMENT_PACK = "AwaitingPaymentPack";
    public static final String PAYMENT_PACK_SUPPLIED = "PaymentPackSupplied";
    public static final String INVOICE_REJECTED_ACCEPTED = "InvoiceRejectionAccepted";
    public static final String CLAIM_UNROUNTED = "ClaimUnacknowledgedUnrouted";
    
    // NEW STATUS
    public static final String INVOICE_CALCULATION_INCORRECT = "InvoiceDataCalculationIncorrect";

    public static List<String> getStatus() {
        List<String> status = new ArrayList<String>();
        status.add(CLAIM_UNACKNOWLEDGED);
        status.add(CLAIM_ACKNOWLEDGED);
        status.add(AWAITING_CAR_HIRE_INFO);
        status.add(CLAIM_REJECTED);
        status.add(AWAITING_INVOICE_DATA);
        status.add(INVOICE_APPROVED);
        status.add(INVOICE_PAYMENT_LOGGED);
        status.add(INVOICE_ESCALATED);
        status.add(DISPUTED_INVOICE);
        status.add(AWAITING_PAYMENT_PACK);
        status.add(PAYMENT_PACK_SUPPLIED);
        status.add(INVOICE_REJECTED_ACCEPTED);
        status.add(CLAIM_UNROUNTED);

        return status;
    }
    
    public static String getUploadStatus(String sClaimStatus){
        String rStatus = "";
        if(sClaimStatus.equalsIgnoreCase(INVOICE_APPROVED)){
            rStatus = "Invoice Approved";
        }else if(sClaimStatus.equalsIgnoreCase(INVOICE_ESCALATED)){
            rStatus = "Invoice Escalated";
        }else if(sClaimStatus.equalsIgnoreCase(AWAITING_PAYMENT_PACK)){
            rStatus = "Awaiting Payment Pack";
        }else if(sClaimStatus.equalsIgnoreCase(INVOICE_CALCULATION_INCORRECT)){
            rStatus = "Invoice Data Calculation Incorrect";
        }
        return rStatus;
    }
    
}
