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
