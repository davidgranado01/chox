package idas.chox.service.security;

import java.util.ArrayList;
import java.util.List;

public class ExtraAction {

    public static final String UPDATE_INSURER_CLAIM_NUMBER = "updateInsurerClaimNumber";
    public static final String UPDATE_CLAIM_WORKGROUP_AND_OWNER = "updateClaimWorkgroupAndOwner";
    public static final String ESCALATE_UNASSIGNED_CLAIM = "escalateUnassignedClaim";
    public static final String UPDATE_LIABILITY = "updateLiability";
    public static final String UPDATE_CLAIM_SUPPLIER_OWNER = "updateClaimSupplierOwner";
    public static final String MAKE_INTERIM_PAYMENT = "makeInterimPayment";
    public static final String UPDATE_INTERIM_PAYMENT_FULL_AND_FINAL = "updateInterimPaymentFullAndFinal";
    public static final String PAYMENT_RECEIVED = "updatePaymentReceived";
    public static final String UPDATE_INSURER_CLAIM_OWNER = "updateInsurerClaimOwner";
    public static final String UPDATE_PENALTY_CHARGES = "updatePenaltyCharges";
    public static final String MARK_SUPPLEMENTARY_INVOICED_CLAIM = "markSupplementaryInvoicedClaim";
    public static final String PENALTY_CHARGE_CONFIGURATION = "penaltyChargeConfiguration";
    public static final String INVOICE_REVIEW_REQUIRED = "invoiceReviewRequired";

    public static List<String> getExtraActions() {
        List<String> action = new ArrayList<String>();
        action.add(MAKE_INTERIM_PAYMENT);
        action.add(ESCALATE_UNASSIGNED_CLAIM);
        action.add(UPDATE_CLAIM_SUPPLIER_OWNER);
        action.add(UPDATE_INSURER_CLAIM_NUMBER);
        action.add(UPDATE_LIABILITY);
        action.add(UPDATE_CLAIM_WORKGROUP_AND_OWNER);
        action.add(UPDATE_INTERIM_PAYMENT_FULL_AND_FINAL);
        action.add(PAYMENT_RECEIVED);
        action.add(UPDATE_INSURER_CLAIM_OWNER);
        action.add(UPDATE_PENALTY_CHARGES);
        action.add(MARK_SUPPLEMENTARY_INVOICED_CLAIM);
        action.add(PENALTY_CHARGE_CONFIGURATION);
        action.add(INVOICE_REVIEW_REQUIRED);

        return action;
    }

    public static String getExtraActionName(String extraAction) {

        String returnStr = "";
        if (extraAction.equalsIgnoreCase(UPDATE_INSURER_CLAIM_NUMBER)) {
            returnStr = "Update Insurer Claim Number";
        } else if (extraAction.equalsIgnoreCase(UPDATE_CLAIM_WORKGROUP_AND_OWNER)) {
            returnStr = "Update Workgroup/Claim Owner";
        } else if (extraAction.equalsIgnoreCase(ESCALATE_UNASSIGNED_CLAIM)) {
            returnStr = "Re-assign Workgroup";
        } else if (extraAction.equalsIgnoreCase(UPDATE_LIABILITY)) {
            returnStr = "Update Liability";
        } else if (extraAction.equalsIgnoreCase(UPDATE_CLAIM_SUPPLIER_OWNER)) {
            returnStr = "Update Claim Owner";
        } else if (extraAction.equalsIgnoreCase(MAKE_INTERIM_PAYMENT)) {
            returnStr = "Make Interim Payment";
        } else if (extraAction.equalsIgnoreCase(UPDATE_INTERIM_PAYMENT_FULL_AND_FINAL)) {
            returnStr = "Update To Interim Payment Received Full & Final";
        } else if (extraAction.equalsIgnoreCase(PAYMENT_RECEIVED)) {
            returnStr = "Update To Payment Received";
        } else if (extraAction.equalsIgnoreCase(UPDATE_INSURER_CLAIM_OWNER)) {
            returnStr = "Update Claim Owner";
        } else if (extraAction.equalsIgnoreCase(UPDATE_PENALTY_CHARGES)) {
            returnStr = "Adjust Penalty Charges";
        } else if (extraAction.equalsIgnoreCase(MARK_SUPPLEMENTARY_INVOICED_CLAIM)) {
            returnStr = "Mark Claim For Supplementary Invoice(s)";
        } else if (extraAction.equalsIgnoreCase(PENALTY_CHARGE_CONFIGURATION)) {
            returnStr = "Penalty Charge Configuration";
        } else if (extraAction.equalsIgnoreCase(INVOICE_REVIEW_REQUIRED)) {
            returnStr = "Flag Claim For Review At Invoice Stage";
        }
        return returnStr;
    }
}
