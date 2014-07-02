--------------------------------------------------------------------------------
-- 8.3.1 Payment Processing Updates
--------------------------------------------------------------------------------
ALTER TABLE insurer ADD COLUMN payment_team_enable boolean not null default false;
ALTER TABLE invoice ADD COLUMN payment_team boolean not null default false;
ALTER TABLE invoice_original ADD COLUMN payment_team boolean not null default false;
ALTER TABLE workgroup ADD COLUMN stp_excluded boolean not null default false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'filter.PaymentTeam', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_PC' FROM accessibility WHERE name='filter.PaymentTeam';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHOX_ADMIN' FROM accessibility WHERE name='filter.PaymentTeam';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'activity.SwitchFromPaymentsTeam.AwaitingInvoicePayment', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_PC' FROM accessibility WHERE name='activity.SwitchFromPaymentsTeam.AwaitingInvoicePayment';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHOX_ADMIN' FROM accessibility WHERE name='activity.SwitchFromPaymentsTeam.AwaitingInvoicePayment';


ALTER TABLE bre_band ADD COLUMN payment_team_active boolean not null default false;

DELETE from accessibility_item where role='ROLE_INS_PC' and accessibility_id=(select id from accessibility where name='filter.ManualInvoiceBREApproved');

UPDATE accessibility SET claim_type=17 WHERE name='activity.UpdateManualInvoicePaid.AwaitingInvoicePayment';

UPDATE accessibility SET name='filter.NewClaimsToBeRouted' WHERE name='filter.NewClaimsToBerouted';
----------------------
-- End of 8.3.1
----------------------


--------------------------------------------------------------------------------
-- 8.3.4 Adjust Customer Claim Number for Supplementary Invoice Uploads
--------------------------------------------------------------------------------
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.AwaitingInvoicePayment', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.AwaitingLiabilityResolution', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.AwaitingLitigationOutcome', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.ContestedInvoiceReferredToInsurer', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.InvoiceApprovedByBRE', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.InvoiceEscalated', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.InvoiceEscalatedToHandler', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.InvoicePaymentLogged', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.InvoiceReferredToClaimsHandler', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.InvoiceReferredToEngineer', false, false, false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.updateCustomerClaimNumber.PaymentReceived', false, false, false, false;

INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 2, 'ROLE_CHOX_ADMIN' FROM accessibility WHERE name like 'extraAction.updateCustomerClaimNumber.%';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 2, 'ROLE_CHO_OPR' FROM accessibility WHERE name like 'extraAction.updateCustomerClaimNumber.%';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 2, 'ROLE_CHO_MNG' FROM accessibility WHERE name like 'extraAction.updateCustomerClaimNumber.%';

----------------------
-- End of 8.3.4
----------------------
