--------------------------------------------------------------------------------
-- 8.4.1 Full or partial ERAC branding
--------------------------------------------------------------------------------
ALTER TABLE insurer ADD COLUMN branding int DEFAULT 0 NOT NULL;
ALTER TABLE chorganisation ADD COLUMN branding int DEFAULT 0 NOT NULL;
----------------------
-- End of 8.4.1
----------------------


--------------------------------------------------------------------------------
-- 8.4.2 Ability to link CHOs and switch claims and prevent duplicates
--------------------------------------------------------------------------------
ALTER TABLE chorganisation ADD COLUMN linked_cho int;

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimUnacknowledgedUnrouted',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
   SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedUnrouted';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedUnrouted';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedUnrouted';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedUnrouted';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimUnacknowledgedRouted',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedRouted';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedRouted';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedRouted';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedRouted';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimUnacknowledgedUnassigned',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedUnassigned';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedUnassigned';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedUnassigned';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUnacknowledgedUnassigned';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimRejected',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimRejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimRejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimRejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimRejected';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.SubscriberClaimRejected',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.SubscriberClaimRejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.SubscriberClaimRejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.SubscriberClaimRejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.SubscriberClaimRejected';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimPending',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimPending';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimPending';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimPending';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimPending';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimRejectionContested',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimRejectionContested';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimRejectionContested';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimRejectionContested';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimRejectionContested';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimReferredToFNOL',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimReferredToFNOL';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimReferredToFNOL';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimReferredToFNOL';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimReferredToFNOL';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimUpdatedByEngineer',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUpdatedByEngineer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUpdatedByEngineer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUpdatedByEngineer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimUpdatedByEngineer';


INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.ClaimReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimReferredToEngineer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimReferredToEngineer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimReferredToEngineer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.ClaimReferredToEngineer';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SwitchCho.AwaitingInvoiceData',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SwitchCho.AwaitingInvoiceData';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_MNG',2, id FROM accessibility WHERE name = 'activity.SwitchCho.AwaitingInvoiceData';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO_OPR',2, id FROM accessibility WHERE name = 'activity.SwitchCho.AwaitingInvoiceData';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SwitchCho.AwaitingInvoiceData';


----------------------
-- End of 8.4.2
----------------------
