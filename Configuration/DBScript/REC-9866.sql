DO $subassignreject$
DECLARE copy_accessibility_id integer;
DECLARE new_accessibility_id integer;
BEGIN



DELETE FROM accessibility_item
WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'activity.AssignWorkgroup.SubscriberClaimRejected');

DELETE FROM accessibility
WHERE name = 'activity.AssignWorkgroup.SubscriberClaimRejected';


SELECT id FROM accessibility WHERE name = 'activity.AssignWorkgroup.ClaimReferredToEngineer' INTO copy_accessibility_id;

INSERT INTO accessibility (
    id, name,
    is_workgroup_check, is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled, check_supplier_ownership, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type
)
SELECT nextval('accessibility_id_seq'), 'activity.AssignWorkgroup.SubscriberClaimRejected',
    is_workgroup_check, is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled, check_supplier_ownership, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type
FROM accessibility
WHERE id = copy_accessibility_id;


SELECT id FROM accessibility WHERE name = 'activity.AssignWorkgroup.SubscriberClaimRejected' INTO new_accessibility_id;


INSERT INTO accessibility_item (
    id, accessibility_id, role, access_right
)
SELECT nextval('accessibility_item_id_seq'), new_accessibility_id, role, access_right
FROM accessibility_item
WHERE accessibility_id = copy_accessibility_id;



INSERT INTO accessibility_item (
    id, accessibility_id, role, access_right
)
SELECT nextval('accessibility_item_id_seq'), id,'ROLE_INS_CH', 1
FROM accessibility
WHERE name='activity.AssignWorkgroup.ClaimPending';


INSERT INTO accessibility_item (
    id, accessibility_id, role, access_right
)
SELECT nextval('accessibility_item_id_seq'), id,'ROLE_INS_CH', 1
FROM accessibility
WHERE name='activity.AssignWorkgroup.ClaimRejected';


END $subassignreject$;
