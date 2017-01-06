package idas.chox.service.security;

import java.util.ArrayList;
import java.util.List;

public class ExtraAction {

    public static final String UPDATE_INSURER_CLAIM_NUMBER = "updateInsurerClaimNumber";
    public static final String UPDATE_SUPPLIER_REFERENCE_NUMBER = "updateSupplierReferenceNumber";
    public static final String UPDATE_CUSTOMER_CLAIM_NUMBER = "updateCustomerClaimNumber";
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
    public static final String ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP_CLAIM_OWNER = "updateManualInvWorkgroupClaimOwner";
    /*
     * ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP, ASSIGN_OR_UPDATE_MANUAL_INV_CLAIM_OWNER do not have entries in the database. 
     * This is used to change the more action lable name depending upon manual invoice workgroup or claim ownership enabled.
     */
    public static final String ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP = "updateManualInvWorkgroup";
    public static final String ASSIGN_OR_UPDATE_MANUAL_INV_CLAIM_OWNER = "updateManualInvClaimOwner";
    public static final String UPDATE_CLAIM_WORKGROUP = "updateClaimWorkgroup";
    public static final String FINAL_REVIEW = "finalReview";
    public static final String MARK_CASE_WITH_CLIENTS_SOLICITOR = "markCaseWithClientsSolicitor";
    public static final String REVIEW_CLAIM_ADUIT = "reviewClaimAudit";
    public static final String FRAUD_CHECK = "fraudCheck";
    
    private static final List<String> extraActionList = new ArrayList<String>(19);
    static {
        extraActionList.add(ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP_CLAIM_OWNER);
        extraActionList.add(ESCALATE_UNASSIGNED_CLAIM);
        extraActionList.add(FINAL_REVIEW);
        extraActionList.add(INVOICE_REVIEW_REQUIRED);
        extraActionList.add(MAKE_INTERIM_PAYMENT);
        extraActionList.add(MARK_SUPPLEMENTARY_INVOICED_CLAIM);
        extraActionList.add(PAYMENT_RECEIVED);
        extraActionList.add(PENALTY_CHARGE_CONFIGURATION);
        extraActionList.add(UPDATE_CLAIM_SUPPLIER_OWNER);
        extraActionList.add(UPDATE_CLAIM_WORKGROUP);
        extraActionList.add(UPDATE_CLAIM_WORKGROUP_AND_OWNER);
        extraActionList.add(UPDATE_INSURER_CLAIM_NUMBER);
        extraActionList.add(UPDATE_CUSTOMER_CLAIM_NUMBER);
        extraActionList.add(UPDATE_SUPPLIER_REFERENCE_NUMBER);
        extraActionList.add(UPDATE_INSURER_CLAIM_OWNER);
        extraActionList.add(UPDATE_INTERIM_PAYMENT_FULL_AND_FINAL);
        extraActionList.add(UPDATE_LIABILITY);
        extraActionList.add(UPDATE_PENALTY_CHARGES);
        extraActionList.add(MARK_CASE_WITH_CLIENTS_SOLICITOR);
        extraActionList.add(REVIEW_CLAIM_ADUIT);
        extraActionList.add(FRAUD_CHECK);
    }
    
    public static List<String> getExtraActions() {
        return extraActionList;
    }

    public static String getExtraActionName(String extraAction) {

        String returnStr = "";
        if (extraAction.equalsIgnoreCase(UPDATE_INSURER_CLAIM_NUMBER)) {
            returnStr = "Update Insurer Claim Number";
        } else if (extraAction.equalsIgnoreCase(UPDATE_SUPPLIER_REFERENCE_NUMBER)) {
            returnStr = "Update Supplier Reference Number";
        } else if (extraAction.equalsIgnoreCase(UPDATE_CUSTOMER_CLAIM_NUMBER)) {
            returnStr = "Update Customer Claim Number";
        } else if (extraAction.equalsIgnoreCase(UPDATE_CLAIM_WORKGROUP_AND_OWNER)) {
            returnStr = "Update Workgroup/Claim Owner";
        } else if (extraAction.equalsIgnoreCase(ESCALATE_UNASSIGNED_CLAIM)) {
            returnStr = "Re-assign Workgroup";
        } else if (extraAction.equalsIgnoreCase(UPDATE_LIABILITY)) {
            returnStr = "Update Liability/Indemnity";
        } else if (extraAction.equalsIgnoreCase(UPDATE_CLAIM_SUPPLIER_OWNER)) {
            returnStr = "Update Claim Owner";
        } else if (extraAction.equalsIgnoreCase(MAKE_INTERIM_PAYMENT)) {
            returnStr = "Make Interim Payment";
        } else if (extraAction.equalsIgnoreCase(UPDATE_INTERIM_PAYMENT_FULL_AND_FINAL)) {
            returnStr = "Update Interim Payment Received";
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
        } else if (extraAction.equalsIgnoreCase(ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP_CLAIM_OWNER)) {
            returnStr = "Update Manual Invoice Workgroup/Claim Owner";
        } else if (extraAction.equalsIgnoreCase(ASSIGN_OR_UPDATE_MANUAL_INV_WORKGROUP)) {
            returnStr = "Update Manual Invoice Workgroup";
        } else if (extraAction.equalsIgnoreCase(ASSIGN_OR_UPDATE_MANUAL_INV_CLAIM_OWNER)) {
            returnStr = "Update Manual Invoice Claim Owner";
        } else if (extraAction.equalsIgnoreCase(UPDATE_CLAIM_WORKGROUP)) {
            returnStr = "Update Workgroup";
        } else if (extraAction.equalsIgnoreCase(FINAL_REVIEW)) {
            returnStr = "Final Review";
        } else if (extraAction.equalsIgnoreCase(MARK_CASE_WITH_CLIENTS_SOLICITOR)) {
            returnStr = "Case With Clients Solicitor";
        } else if (extraAction.equalsIgnoreCase(REVIEW_CLAIM_ADUIT)) {
            returnStr = "Review Claim Audit";
        } else if (extraAction.equalsIgnoreCase(FRAUD_CHECK)) {
            returnStr = "Fraud Check";
        }
        return returnStr;
    }
}
