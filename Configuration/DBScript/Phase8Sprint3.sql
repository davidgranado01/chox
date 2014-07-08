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
-- 8.3.2 LOU Dates Updates
--------------------------------------------------------------------------------

----------------------
-- End of 8.3.2
----------------------
INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver,
                           error_message_receiver,
                           created_by, created_date, last_modified_by, last_modified_date, version)
--    SELECT 'erac_scheduler', 'Ch0xAdm1n1', 'LOU_UPDATE', 'CHOX Support Email: Hire Monitoring Update Request',
--            'elliot.roberts@sherwoodts.co.uk,ben.richmond@sherwoodts.co.uk,neil.coogan@ehi.com,rasmus.k.kristensen@ehi.com',
--            'John.Dowson@SherwoodTS.co.uk', 'seeni.shanmugam@sherwoodts.co.uk,john.dowson@sherwoodts.co.uk',
--            999, now(), 999, now(), 0;
    SELECT 'erac_scheduler', 'C0mpliance', 'LOU_UPDATE', 'CHOX Support Email: Hire Monitoring Update Request',
            'elliot.roberts@sherwoodts.co.uk,ben.richmond@sherwoodts.co.uk,jldowson@gmail.com,bula.raghavan@sherwoodts.co.uk,paul.simpson@sherwoodts.co.uk,robert.hon@sherwoodts.co.uk',
            'John.Dowson@SherwoodTS.co.uk', 'seeni.shanmugam@sherwoodts.co.uk,john.dowson@sherwoodts.co.uk',
            999, now(), 999, now(), 0;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimUnacknowledgedUnrouted', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimUnacknowledgedRouted', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimUnacknowledgedUnassigned', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimReferredToFNOL', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimReferredToEngineer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimUpdatedByEngineer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.AwaitingCarHireInfo', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.AwaitingInvoiceData', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimRejectionContested', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimRejected', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.SubscriberClaimRejected', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.LouUpdate.ClaimPending', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimUnacknowledgedUnrouted', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimUnacknowledgedRouted', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimUnacknowledgedUnassigned', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimReferredToFNOL', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimReferredToEngineer', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimUpdatedByEngineer', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.AwaitingCarHireInfo', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.AwaitingInvoiceData', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimRejectionContested', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimRejected', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.SubscriberClaimRejected', false, false, 17;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.LouUpdate.ClaimPending', false, false, 17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHOX_ADMIN', 1
    FROM accessibility
    WHERE name like 'activity.LouUpdate.%';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHO_MNG', 1
    FROM accessibility
    WHERE name like 'activity.LouUpdate.%' AND claim_type is null;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHO_OPR', 1
    FROM accessibility
    WHERE name like 'activity.LouUpdate.%' AND claim_type is null;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 1
    FROM accessibility
    WHERE name like 'activity.LouUpdate.%' and claim_type = 17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 1
    FROM accessibility
    WHERE name like 'activity.LouUpdate.%' and claim_type = 17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_UPLOAD', 1
    FROM accessibility
    WHERE name like 'activity.LouUpdate.%' and claim_type = 17;


--------------------------------------------------------------------------------
-- 8.3.3 Hire Start Updates
--------------------------------------------------------------------------------
INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver,
                           error_message_receiver,
                           created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 'erac_scheduler', 'Ch0xAdm1n1', 'HIRE_UPDATE', 'CHOX Support Email: Hire Update Request',
            'elliot.roberts@sherwoodts.co.uk,ben.richmond@sherwoodts.co.uk,neil.coogan@ehi.com,rasmus.k.kristensen@ehi.com',
            'John.Dowson@SherwoodTS.co.uk', 'seeni.shanmugam@sherwoodts.co.uk,john.dowson@sherwoodts.co.uk',
            999, now(), 999, now(), 0;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimUnacknowledgedUnrouted', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimUnacknowledgedRouted', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimUnacknowledgedUnassigned', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimReferredToFNOL', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimReferredToEngineer', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimUpdatedByEngineer', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.AwaitingCarHireInfo', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.AwaitingInvoiceData', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimRejectionContested', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimRejected', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.SubscriberClaimRejected', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.HireUpdate.ClaimPending', false, false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimUnacknowledgedUnrouted', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimUnacknowledgedRouted', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimUnacknowledgedUnassigned', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimReferredToFNOL', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimReferredToEngineer', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimUpdatedByEngineer', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.AwaitingCarHireInfo', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.AwaitingInvoiceData', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimRejectionContested', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimRejected', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.SubscriberClaimRejected', false, false, 17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
    SELECT 'activity.HireUpdate.ClaimPending', false, false, 17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHOX_ADMIN', 1
    FROM accessibility
    WHERE name like 'activity.HireUpdate.%';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHO_MNG', 1
    FROM accessibility
    WHERE name like 'activity.HireUpdate.%' AND claim_type is null;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHO_OPR', 1
    FROM accessibility
    WHERE name like 'activity.HireUpdate.%' AND claim_type is null;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 1
    FROM accessibility
    WHERE name like 'activity.HireUpdate.%' and claim_type = 17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 1
    FROM accessibility
    WHERE name like 'activity.HireUpdate.%' and claim_type = 17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_UPLOAD', 1
    FROM accessibility
    WHERE name like 'activity.HireUpdate.%' and claim_type = 17;


----------------------
-- End of 8.3.3
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
