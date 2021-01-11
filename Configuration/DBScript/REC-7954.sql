DO $pending$
DECLARE copy_accessibility_w_id integer;
DECLARE new_accessibility_w_id integer;
DECLARE copy_accessibility_o_id integer;
DECLARE new_accessibility_o_id integer;
DECLARE copy_accessibility_wo_id integer;
DECLARE new_accessibility_wo_id integer;
BEGIN




DELETE FROM accessibility_item
WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroup.ClaimPending');

DELETE FROM accessibility
WHERE name = 'extraAction.updateClaimWorkgroup.ClaimPending';

DELETE FROM accessibility_item
WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateInsurerClaimOwner.ClaimPending');

DELETE FROM accessibility
WHERE name = 'extraAction.updateInsurerClaimOwner.ClaimPending';

DELETE FROM accessibility_item
WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroupAndOwner.ClaimPending');

DELETE FROM accessibility
WHERE name = 'extraAction.updateClaimWorkgroupAndOwner.ClaimPending';




SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroup.ClaimReferredToEngineer' INTO copy_accessibility_w_id;

INSERT INTO accessibility (
    id, name,
    is_workgroup_check, is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled, check_supplier_ownership, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type
)
SELECT nextval('accessibility_id_seq'), 'extraAction.updateClaimWorkgroup.ClaimPending',
    is_workgroup_check, is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled, check_supplier_ownership, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type
FROM accessibility
WHERE id = copy_accessibility_w_id;


SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroup.ClaimPending' INTO new_accessibility_w_id;


INSERT INTO accessibility_item (
    id, accessibility_id, role, access_right
)
SELECT nextval('accessibility_item_id_seq'), new_accessibility_w_id, role, access_right
FROM accessibility_item
WHERE accessibility_id = copy_accessibility_w_id;




SELECT id FROM accessibility WHERE name = 'extraAction.updateInsurerClaimOwner.ClaimReferredToEngineer' INTO copy_accessibility_o_id;

INSERT INTO accessibility (
    id, name,
    is_workgroup_check, is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled, check_supplier_ownership, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type
)
SELECT nextval('accessibility_id_seq'), 'extraAction.updateInsurerClaimOwner.ClaimPending',
    is_workgroup_check, is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled, check_supplier_ownership, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type
FROM accessibility
WHERE id = copy_accessibility_o_id;


SELECT id FROM accessibility WHERE name = 'extraAction.updateInsurerClaimOwner.ClaimPending' INTO new_accessibility_o_id;


INSERT INTO accessibility_item (
    id, accessibility_id, role, access_right
)
SELECT nextval('accessibility_item_id_seq'), new_accessibility_o_id, role, access_right
FROM accessibility_item
WHERE accessibility_id = copy_accessibility_o_id;




SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroupAndOwner.ClaimReferredToEngineer' INTO copy_accessibility_wo_id;

INSERT INTO accessibility (
    id, name,
    is_workgroup_check, is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled, check_supplier_ownership, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type
)
SELECT nextval('accessibility_id_seq'), 'extraAction.updateClaimWorkgroupAndOwner.ClaimPending',
    is_workgroup_check, is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled, check_supplier_ownership, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type
FROM accessibility
WHERE id = copy_accessibility_wo_id;


SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroupAndOwner.ClaimPending' INTO new_accessibility_wo_id;


INSERT INTO accessibility_item (
    id, accessibility_id, role, access_right
)
SELECT nextval('accessibility_item_id_seq'), new_accessibility_wo_id, role, access_right
FROM accessibility_item
WHERE accessibility_id = copy_accessibility_wo_id;




END $pending$;
