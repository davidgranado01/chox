--
-- CHOX-785: the HireMonitoring tab should be editable by the CHO when the claim is in ContestedInvoiceReferredtoCHO state.
--
UPDATE accessibility_item
SET access_right = 2
WHERE (role = 'ROLE_CHO_MNG' OR role = 'ROLE_CHO_OPR' )
AND accessibility_id = (SELECT accessibility.id FROM accessibility WHERE name='tab.HireMonitoring.ContestedInvoiceReferredToCHO' AND claim_type is null);


-- THis includes the ECD Update fields - update tables with claim type is null (default)
INSERT into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
     SELECT 'activity.EcdUpdate.ContestedInvoiceReferredToCHO', false, false, false, false
WHERE NOT EXISTS
      (Select id from accessibility WHERE name = 'activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null) ;


INSERT INTO accessibility_item(accessibility_id, role, access_right)
   SELECT id, 'ROLE_CHO_MNG', 2  FROM accessibility WHERE name='activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null
AND
    NOT EXISTS (
        SELECT accessibility_item.id FROM accessibility_item, accessibility WHERE accessibility_id = accessibility.id AND role='ROLE_CHO_MNG' and access_right='2' AND accessibility.name='activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null
    );

INSERT INTO accessibility_item(accessibility_id, role, access_right)
       SELECT id, 'ROLE_CHO_OPR', 2  FROM accessibility WHERE name='activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null
    AND
        NOT EXISTS (
            SELECT accessibility_item.id FROM accessibility_item, accessibility WHERE accessibility_id = accessibility.id AND role='ROLE_CHO_OPR' and access_right='2' AND accessibility.name='activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null
        );

INSERT INTO accessibility_item(accessibility_id, role, access_right)
          SELECT id, 'ROLE_ALL', 1  FROM accessibility WHERE name='activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null
       AND
           NOT EXISTS (
               SELECT accessibility_item.id FROM accessibility_item, accessibility WHERE accessibility_id = accessibility.id AND role='ROLE_ALL' and access_right='1' AND accessibility.name='activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null
           );

INSERT INTO accessibility_item(accessibility_id, role, access_right)
              SELECT id, 'ROLE_INS', 1  FROM accessibility WHERE name='activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND  claim_type is null
           AND
               NOT EXISTS (
                   SELECT accessibility_item.id FROM accessibility_item, accessibility WHERE accessibility_id = accessibility.id AND role='ROLE_INS' and access_right='1' AND accessibility.name='activity.EcdUpdate.ContestedInvoiceReferredToCHO' AND claim_type is null
               );

