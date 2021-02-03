DO $rejectedRollback$
BEGIN


DELETE FROM accessibility_item
WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroup.ClaimRejected');

DELETE FROM accessibility
WHERE name = 'extraAction.updateClaimWorkgroup.ClaimRejected';

DELETE FROM accessibility_item
WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateInsurerClaimOwner.ClaimRejected');

DELETE FROM accessibility
WHERE name = 'extraAction.updateInsurerClaimOwner.ClaimRejected';

DELETE FROM accessibility_item
WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroupAndOwner.ClaimRejected');

DELETE FROM accessibility
WHERE name = 'extraAction.updateClaimWorkgroupAndOwner.ClaimRejected';


END $rejectedRollback$;
