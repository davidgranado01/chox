--
-- CHOX-545
--
delete from accessibility_item where  accessibility_id in (select id from accessibility where name='extraAction.updateInsurerClaimOwner.ClaimRejected');
delete from accessibility_item where  accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroup.ClaimRejected');
delete from accessibility where name='extraAction.updateInsurerClaimOwner.ClaimRejected';
delete from accessibility where name='extraAction.updateClaimWorkgroup.ClaimRejected';

delete from accessibility_item where  accessibility_id in (select id from accessibility where name='extraAction.updateClaimOwner.ClaimRejected');
delete from accessibility_item where  accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.ClaimRejected');
delete from accessibility where name='extraAction.updateClaimOwner.ClaimRejected';
delete from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.ClaimRejected';

delete from accessibility_item where  accessibility_id in (select id from accessibility where name='batch.updateClaimWorkgroupAndOwner.ClaimRejected');
delete from accessibility_item where  accessibility_id in (select id from accessibility where name='batch.routeClaims.ClaimRejected');
delete from accessibility where name='batch.updateClaimWorkgroupAndOwner.ClaimRejected';
delete from accessibility where name='batch.routeClaims.ClaimRejected';

delete from accessibility_item where  accessibility_id in (select id from accessibility where name='batch.claimOwnership.ClaimRejected');
delete from accessibility_item where  accessibility_id in (select id from accessibility where name='batch.insurerClaimOwnership.ClaimRejected');
delete from accessibility where name='batch.claimOwnership.ClaimRejected';
delete from accessibility where name='batch.insurerClaimOwnership.ClaimRejected';

delete from accessibility_item where  accessibility_id in (select id from accessibility where name='batch.updateInsurerClaimOwner.ClaimRejected');
delete from accessibility where name='batch.updateInsurerClaimOwner.ClaimRejected';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHOX_ADMIN', 2 FROM accessibility WHERE name like 'extraAction.updateInsurerClaimOwner.%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHOX_ADMIN', 2 FROM accessibility WHERE name like 'extraAction.updateClaimWorkgroup.%';


INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'extraAction.updateClaimWorkgroupAndOwner.%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'extraAction.updateInsurerClaimOwner.%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'extraAction.updateClaimWorkgroup.%';

UPDATE accessibility set check_workgroup_enabled = false where name like 'activity.AssignOwner.%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%' and name not like '%ClaimUnacknowledgedUnassigned' and name not like '%InvoiceUnassigned' and claim_type is null;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CR', 2 FROM accessibility WHERE name like 'activity.AssignOwner.ClaimUnacknowledgedUnassigned' and claim_type in (0,18);

insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, check_workgroup_enabled, check_claimownership_enabled)
    VALUES ('batch.updateClaimWorkgroupAndOwner.ManualInvoiceBREApproved', true, false, false, false, true, true);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, check_workgroup_enabled, check_claimownership_enabled)
    VALUES ('batch.updateClaimWorkgroupAndOwner.ManualInvoiceBRERejected', true, false, false, false, true, true);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, check_workgroup_enabled, check_claimownership_enabled)
    VALUES ('batch.updateClaimWorkgroupAndOwner.ManualInvoiceContested', true, false, false, false, true, true);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, check_workgroup_enabled, check_claimownership_enabled)
    VALUES ('batch.updateClaimWorkgroupAndOwner.ManualInvoicePaid', true, false, false, false, true, true);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled, check_workgroup_enabled, check_claimownership_enabled)
    VALUES ('batch.updateClaimWorkgroupAndOwner.ManualInvoiceUnassigned', true, false, false, false, true, true);
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_COM', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceBREApproved';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceBREApproved';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_COM', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceBRERejected';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceBRERejected';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_COM', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceContested';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceContested';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_COM', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoicePaid';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoicePaid';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_COM', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceUnassigned';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_MNG', 2 FROM accessibility WHERE name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceUnassigned';

delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceBREApproved');
delete from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceBREApproved';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceBRERejected');
delete from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceBRERejected';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceContested');
delete from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceContested';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoicePaid');
delete from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoicePaid';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceUnassigned');
delete from accessibility where name='batch.updateClaimWorkgroupAndOwner.ManualInvoiceUnassigned';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'batch.updateClaimWorkgroupAndOwner.%' and name not like 'ClaimUnacknowledgedUnrouted%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'batch.routeClaims.%' and name not like 'ClaimUnacknowledgedUnrouted%';

update accessibility set is_workgroup_check=false where name='batch.claimOwnership.ClaimUnacknowledgedUnassigned';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'batch.updateInsurerClaimOwner.%';

UPDATE accessibility set check_claimownership_enabled=false, check_workgroup_enabled=false, check_manual_inv_workgroup_enabled=false where name like 'batch.claimOwnership.Manual%';


INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'batch.insurerClaimOwnership.%' and name not like '%ClaimUnacknowledgedUnassigned%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'batch.claimOwnership.%' and name not like '%ClaimUnacknowledgedUnassigned%';
INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'batch.routeClaims.%' and name not like '%ClaimUnacknowledgedUnrouted%';

UPDATE accessibility_item set access_right=2
from accessibility a where a.id=accessibility_id and a.name like 'extraAction.updateClaimSupplierOwner.%';
