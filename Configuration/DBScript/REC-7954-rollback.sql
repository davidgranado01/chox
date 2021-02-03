DO $pendingRollback$
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


END $pendingRollback$;
