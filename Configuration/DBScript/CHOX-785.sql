--
-- CHOX-785: he HireMonitoring tab should be editable by the CHO when the claim is in ContestedInvoiceReferredtoCHO state.
--
UPDATE accessibility_item
SET access_right = 2
WHERE (role = 'ROLE_CHO_MNG' OR role = 'ROLE_CHO_OPR' )
AND accessibility_id = (SELECT accessibility.id FROM accessibility WHERE name='tab.HireMonitoring.ContestedInvoiceReferredToCHO');

INSERT into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
     SELECT 'activity.EcdUpdate.ContestedInvoiceReferredToCHO', false, false, false, false
WHERE NOT EXISTS
      (Select id from accessibility WHERE name = 'activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null) ;

INSERT into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, claim_type)
    SELECT 'activity.EcdUpdate.ContestedInvoiceReferredToCHO', false, false, false, false, 17
WHERE NOT EXISTS
     (Select id from accessibility WHERE name = 'activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type = 17) ;

