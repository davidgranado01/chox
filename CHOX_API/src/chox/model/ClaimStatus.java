/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.model;

import java.util.ArrayList;
import java.util.List;

public class ClaimStatus {

    public static final String CLAIM_UNACKNOWLEDGED_UNROUTED = "ClaimUnacknowledgedUnrouted";
    public static final String CLAIM_UNACKNOWLEDGED_ROUTED = "ClaimUnacknowledgedRouted";
    public static final String CLAIM_REJECTED = "ClaimRejected";
    public static final String CLAIM_REJECTION_ACCEPTED = "ClaimRejectionAccepted";
    public static final String CLAIM_REJECTION_CONTESTED = "ClaimRejectionContested";
    public static final String AWAITING_CAR_HIRE_INFO = "AwaitingCarHireInfo";
    public static final String AWAITING_INVOICE_DATA = "AwaitingInvoiceData";
    public static final String INVOICE_DATA_CALCULATION_INCORRECT = "InvoiceDataCalculationIncorrect";
    public static final String INVOICE_APPROVED_BY_BRE = "InvoiceApprovedByBRE";
    public static final String INVOICE_ESCALATED = "InvoiceEscalated";
    public static final String CONTESTED_INVOICE_REF_TO_INS = "ContestedInvoiceReferredToInsurer";
    public static final String CONTESTED_INVOICE_REF_TO_CHO = "ContestedInvoiceReferredToCHO";
    public static final String INVOICE_REJECTED_ACCEPTED = "InvoiceRejectionAccepted";
    public static final String AWAITING_INVOICE_PAYMENT = "AwaitingInvoicePayment";
    public static final String INVOICE_PAYMENT_LOGGED = "InvoicePaymentLogged";    // NEW STATUS
    public static final String CLAIM_REF_TO_ENG = "ClaimReferredToEngineer";    // NEW STATUS @ 09 Dec 2008
    public static final String CLAIM_REFERRED_TO_FNOL = "ClaimReferredToFNOL"; // NEW STATUS, MANTIS ID 0000350
    public static final String CLAIM_CLOSED = "ClaimClosed"; // NEW STATUS, MANTIS ID 0000386

    public static List<String> getStatus() {
        List<String> status = new ArrayList<String>();
        status.add(CLAIM_UNACKNOWLEDGED_UNROUTED);
        status.add(CLAIM_UNACKNOWLEDGED_ROUTED);
        status.add(CLAIM_REJECTED);
        status.add(CLAIM_REJECTION_ACCEPTED);
        status.add(CLAIM_REJECTION_CONTESTED);
        status.add(AWAITING_CAR_HIRE_INFO);
        status.add(AWAITING_INVOICE_DATA);
        status.add(INVOICE_DATA_CALCULATION_INCORRECT);
        status.add(INVOICE_APPROVED_BY_BRE);
        status.add(INVOICE_ESCALATED);
        status.add(CONTESTED_INVOICE_REF_TO_INS);
        status.add(CONTESTED_INVOICE_REF_TO_CHO);
        status.add(INVOICE_REJECTED_ACCEPTED);
        status.add(AWAITING_INVOICE_PAYMENT);
        status.add(INVOICE_PAYMENT_LOGGED);
        status.add(CLAIM_REF_TO_ENG);
        status.add(CLAIM_REFERRED_TO_FNOL);
        status.add(CLAIM_CLOSED);
        return status;
    }

//    public static String getUploadStatus(String sClaimStatus) {
//        String rStatus = "";
//        if (sClaimStatus.equalsIgnoreCase(INVOICE_APPROVED)) {
//            rStatus = "Invoice Approved";
//        } else if (sClaimStatus.equalsIgnoreCase(INVOICE_ESCALATED)) {
//            rStatus = "Invoice Escalated";
//        } else if (sClaimStatus.equalsIgnoreCase(AWAITING_PAYMENT_PACK)) {
//            rStatus = "Awaiting Payment Pack";
//        } else if (sClaimStatus.equalsIgnoreCase(INVOICE_CALCULATION_INCORRECT)) {
//            rStatus = "Invoice Data Calculation Incorrect";
//        }
//        return rStatus;
//    }
    
}
