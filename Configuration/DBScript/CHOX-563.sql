--
-- CHOX-563: Production - Insurer cannot manually add penalty charges to manual claims/invoices in non manual states
--
delete from accessibility where id in (704, 705, 708, 709, 710,  711, 712, 713, 714, 715, 716, 717, 718,730);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
    VALUES ('extraAction.updatePenaltyCharges.AwaitingLiabilityResolution', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
    VALUES ('extraAction.updatePenaltyCharges.AwaitingInvoicePayment', false, false, false, false, 17);
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name='extraAction.updatePenaltyCharges.AwaitingLiabilityResolution' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 2 FROM accessibility WHERE name='extraAction.updatePenaltyCharges.AwaitingLiabilityResolution' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHOX_ADMIN', 2 FROM accessibility WHERE name='extraAction.updatePenaltyCharges.AwaitingLiabilityResolution' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name='extraAction.updatePenaltyCharges.AwaitingInvoicePayment' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 2 FROM accessibility WHERE name='extraAction.updatePenaltyCharges.AwaitingInvoicePayment' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHOX_ADMIN', 2 FROM accessibility WHERE name='extraAction.updatePenaltyCharges.AwaitingInvoicePayment' and claim_type=17;
