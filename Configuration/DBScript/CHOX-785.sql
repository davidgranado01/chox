--
-- CHOX-785: he HireMonitoring tab should be editable by the CHO when the claim is in ContestedInvoiceReferredtoCHO state.
--
UPDATE accessibility_item
SET access_right = 2
WHERE (role = 'ROLE_CHO_MNG' OR role = 'ROLE_CHO_OPR' )
AND accessibility_id = (SELECT accessibility.id FROM accessibility WHERE name='tab.HireMonitoring.ContestedInvoiceReferredToCHO');

