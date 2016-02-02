--------------------------------------------------------------------------------
-- 8.10.5 Updated Rejection Reasons Panel
--------------------------------------------------------------------------------
ALTER TABLE reason_of_rejection ALTER COLUMN type TYPE varchar(17);
UPDATE reason_of_rejection set type = 'Claim Rejection' where type='Claim';
UPDATE reason_of_rejection set type = 'Invoice Rejection' where type='Invoice';
ALTER TABLE reason_of_rejection_template ALTER COLUMN type TYPE varchar(17);
UPDATE reason_of_rejection_template set type = 'Claim Rejection' where type='Claim';
UPDATE reason_of_rejection_template set type = 'Invoice Rejection' where type='Invoice';
----------------------
-- End of 8.10.5
----------------------

--------------------------------------------------------------------------------
-- 8.10.7 Claim Closure Note
--------------------------------------------------------------------------------
-- Add template closure reasons
INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Accepted Interim Payment As Full & Final', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'No Longer Pursuing Claim', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Incorrect At-Fault Insurer', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Litigating', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Other', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Out Of Scope', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Payment Received In Full', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Pursued Outside Of CHOX', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Write Off - Liability', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Write Off - Indemnity', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Write Off - Claim Validation', 'Closure', '', true, true, true, true, true, true, true, false;

-- Add closure reasons for existing insurers
ALTER TABLE reason_of_rejection ALTER COLUMN name TYPE character varying;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Accepted Interim Payment As Full & Final', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'No Longer Pursuing Claim', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Incorrect At-Fault Insurer', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Litigating', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Out Of Scope', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Payment Received In Full', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Pursued Outside Of CHOX', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Write Off - Liability', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Write Off - Indemnity', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date)
    SELECT id, 'Write Off - Claim Validation', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now() from insurer;

----------------------
-- End of 8.10.5
----------------------


--------------------------------------------------------------------------------
-- 8.10.8 Potential PI Case/With Clients Solicitor Pot
--------------------------------------------------------------------------------
ALTER TABLE claim ADD COLUMN with_solicitor boolean not null default false;
ALTER TABLE claim ADD COLUMN date_marked_with_solicitor timestamp without time zone;
ALTER TABLE claim ADD COLUMN user_marked_with_solicitor varchar(512);

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'filter.CaseWithClientsSolicitor', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHO' FROM accessibility WHERE name='filter.CaseWithClientsSolicitor';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHOX_ADMIN' FROM accessibility WHERE name='filter.CaseWithClientsSolicitor';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.markCaseWithClientsSolicitor.ContestedInvoiceReferredToCHO', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 2, 'ROLE_CHO' FROM accessibility WHERE name='extraAction.markCaseWithClientsSolicitor.ContestedInvoiceReferredToCHO';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCaseWithSolicitor.ContestedInvoiceReferredToCHO', false, false, false, false);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
   SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.UpdateCaseWithSolicitor.ContestedInvoiceReferredToCHO';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO',1, id FROM accessibility WHERE name = 'activity.UpdateCaseWithSolicitor.ContestedInvoiceReferredToCHO';

ALTER TABLE chorganisation ADD COLUMN is_solicitor_enable boolean not null default false;
----------------------
-- End of 8.10.8
----------------------

-- bug#3601 - Production - incorrect audit trail for manual invoices
--------------------------------------------------------------------------------
update audit_trail
  set original_status='AwaitingInvoiceData', version=version+1
where original_status='InvoiceApprovedByBRE' and new_status='ManualInvoiceBREApproved';
----------------------
-- End of bug#3601
