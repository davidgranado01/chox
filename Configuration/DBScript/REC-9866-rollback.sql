DO $pendingRollback$
BEGIN

DELETE FROM accessibility_item
WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'activity.AssignWorkgroup.SubscriberClaimRejected');

DELETE FROM accessibility
WHERE name = 'activity.AssignWorkgroup.SubscriberClaimRejected';

END $pendingRollback$;