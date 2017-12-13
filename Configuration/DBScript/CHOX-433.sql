--
-- SQL Updates for CHOX-433: Event mechanism updates
--
-- new activity UpdateInsurerClaimNumber
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoiceApprovedByBRE', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoiceDataCalculationIncorrect', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoiceEscalated', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.AwaitingInvoicePayment', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoicePaymentLogged', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoiceRejectionAccepted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.AwaitingCarHireInfo', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.AwaitingInvoiceData', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoiceEscalatedToHandler', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.PaymentReceived', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimReferredToEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimUpdatedByEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimRejected', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimRejectionAccepted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimPending', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimClosed', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoiceReferredToEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimReferredToFNOL', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimRejectionContested', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimUnacknowledgedRouted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimUnacknowledgedUnassigned', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ClaimUnacknowledgedUnrouted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoiceReferredToClaimsHandler', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ContestedInvoiceReferredToCHO', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ContestedInvoiceReferredToInsurer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.AwaitingLiabilityResolution', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.InvoiceUnassigned', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ManualInvoiceBREApproved', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ManualInvoiceBRERejected', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ManualInvoiceContested', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ManualInvoiceUnassigned', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.ManualInvoicePaid', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.AwaitingLitigationOutcome', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateInsurerClaimNumber.SubscriberClaimRejected', false, false, false, false);

INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_MNG',2, id FROM accessibility WHERE name like 'activity.UpdateInsurerClaimNumber.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CH',2, id FROM accessibility WHERE name like 'activity.UpdateInsurerClaimNumber.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CR',2, id FROM accessibility WHERE name like 'activity.UpdateInsurerClaimNumber.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_COM',2, id FROM accessibility WHERE name like 'activity.UpdateInsurerClaimNumber.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_SUP',2, id FROM accessibility WHERE name like 'activity.UpdateInsurerClaimNumber.%';

INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name like 'activity.UpdateInsurerClaimNumber.%';

    -- new activity UpdateSupplierReference
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoiceApprovedByBRE', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoiceDataCalculationIncorrect', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoiceEscalated', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.AwaitingInvoicePayment', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoicePaymentLogged', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoiceRejectionAccepted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.AwaitingCarHireInfo', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.AwaitingInvoiceData', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoiceEscalatedToHandler', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.PaymentReceived', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimReferredToEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimUpdatedByEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimRejected', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimRejectionAccepted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimPending', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimClosed', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoiceReferredToEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimReferredToFNOL', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimRejectionContested', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimUnacknowledgedRouted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimUnacknowledgedUnassigned', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ClaimUnacknowledgedUnrouted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoiceReferredToClaimsHandler', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ContestedInvoiceReferredToCHO', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ContestedInvoiceReferredToInsurer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.AwaitingLiabilityResolution', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.InvoiceUnassigned', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ManualInvoiceBREApproved', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ManualInvoiceBRERejected', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ManualInvoiceContested', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ManualInvoiceUnassigned', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.ManualInvoicePaid', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.AwaitingLitigationOutcome', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateSupplierReference.SubscriberClaimRejected', false, false, false, false);


INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name like 'activity.UpdateSupplierReference.%';


    -- new activity UpdateCustomerClaimNumber
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.AwaitingInvoicePayment', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.AwaitingLiabilityResolution', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.AwaitingLitigationOutcome', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.ContestedInvoiceReferredToInsurer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.InvoiceApprovedByBRE', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.InvoiceEscalated', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.InvoiceEscalatedToHandler', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.InvoicePaymentLogged', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.InvoiceReferredToClaimsHandler', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.InvoiceReferredToEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCustomerClaimNumber.PaymentReceived', false, false, false, false);

INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name like 'activity.UpdateCustomerClaimNumber.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name like 'activity.UpdateCustomerClaimNumber.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name like 'activity.UpdateCustomerClaimNumber.%';

