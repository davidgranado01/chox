DO $pendingRollback$
    BEGIN

        DELETE FROM accessibility_item
        WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroup.SubscriberClaimRejected');

        DELETE FROM accessibility
        WHERE name = 'extraAction.updateClaimWorkgroup.SubscriberClaimRejected';

        DELETE FROM accessibility_item
        WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateInsurerClaimOwner.SubscriberClaimRejected');

        DELETE FROM accessibility
        WHERE name = 'extraAction.updateInsurerClaimOwner.SubscriberClaimRejected';

        DELETE FROM accessibility_item
        WHERE accessibility_id IN (SELECT id FROM accessibility WHERE name = 'extraAction.updateClaimWorkgroupAndOwner.SubscriberClaimRejected');

        DELETE FROM accessibility
        WHERE name = 'extraAction.updateClaimWorkgroupAndOwner.SubscriberClaimRejected';

END $pendingRollback$;