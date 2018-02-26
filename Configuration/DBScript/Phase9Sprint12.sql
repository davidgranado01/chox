--
-- CHOX-482: New BRE Rule - Impecunious Check
--
ALTER TABLE bre_band ADD COLUMN impecunious_check boolean NOT NULL DEFAULT false;
ALTER TABLE bre_band ADD COLUMN impecunious_start_date timestamp without time zone;
--
-- End of CHOX-482
--

--
-- CHOX-469: New Extras field required: VED Charge
--
ALTER TABLE invoice ADD COLUMN ved_fee numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice ADD COLUMN ved_qty smallint NOT NULL DEFAULT 0;
ALTER TABLE invoice_original ADD COLUMN ved_fee numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice_original ADD COLUMN ved_qty smallint NOT NULL DEFAULT 0;
--
-- End of CHOX-469
--

--
-- CHOX-468: New BRE Rule: VED Charge Check
--
ALTER TABLE bre_band ADD COLUMN ved_charge_check boolean NOT NULL DEFAULT false;
ALTER TABLE bre_band ADD COLUMN ved_charge_ceiling numeric(10,2) NOT NULL DEFAULT 0.00;
--
-- End of CHOX-468
--

--
-- CHOX-472: New Queue Required: Payments Team Returns
--
ALTER TABLE claim ADD COLUMN payments_team_return boolean NOT NULL DEFAULT false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'filter.PaymentTeamReturns', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_PC' FROM accessibility WHERE name='filter.PaymentTeamReturns';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_CH' FROM accessibility WHERE name='filter.PaymentTeamReturns';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_MNG' FROM accessibility WHERE name='filter.PaymentTeamReturns';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_MI' FROM accessibility WHERE name='filter.PaymentTeamReturns';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHOX_ADMIN' FROM accessibility WHERE name='filter.PaymentTeamReturns';
--
-- End of CHOX-472
--

--
-- CHOX-475: More action to update Claim Supplier Owner should be moved to an activity
--
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    select  'activity.UpdateSupplierClaimOwner' || substring(a.name from 37), false, false
    from accessibility a where  a.name ilike 'extraAction.updateClaimSupplierOwner%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHO_OPR', 2 FROM accessibility WHERE name like 'activity.UpdateSupplierClaimOwner%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHO_MNG', 2 FROM accessibility WHERE name like 'activity.UpdateSupplierClaimOwner%';
--
-- End of CHOX-472
--
