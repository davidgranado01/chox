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
