--
-- CHOX-510: Replace use of SMTP with Google's Gmail API for receiving emails in the CHOX Automation functionality
--
CREATE TABLE gmail_scheduler_job (
id serial NOT NULL,
login_username character varying(256) NOT NULL,
login_password character varying(256) NOT NULL,
job_name character varying(256) NOT NULL,
email_subject character varying(256) NOT NULL,
authorised_user character varying NOT NULL,
bcc_receiver character varying NOT NULL,
processed_label character varying,
error_message_receiver character varying NOT NULL,
active boolean NOT NULL DEFAULT true,
reply_to_sender boolean NOT NULL DEFAULT true,
version integer,
created_by integer NOT NULL,
created_date timestamp without time zone NOT NULL DEFAULT now(),
last_modified_by integer NOT NULL,
last_modified_date timestamp without time zone NOT NULL DEFAULT now(),
UNIQUE(email_subject),
CONSTRAINT created_by_fkey FOREIGN KEY (created_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT last_modified_by_fkey FOREIGN KEY (last_modified_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
GRANT SELECT ON TABLE gmail_scheduler_job TO chox_user;
GRANT SELECT ON TABLE gmail_scheduler_job TO chox_quartz;


insert into gmail_scheduler_job (version, login_username, login_password, job_name, email_subject, authorised_user, bcc_receiver,
                error_message_receiver, active, reply_to_sender, created_by, created_date, last_modified_by, last_modified_date)
select 0, login_username, login_password, job_name, email_subject, autherised_user, bcc_receiver,
                error_message_receiver, active, reply_to_sender, created_by, created_date, last_modified_by, last_modified_date
from scheduler_job where job_name in ('ECD_UPDATE', 'HIRE_UPDATE', 'LOU_UPDATE', 'TOTALLOSS_PACK', 'PENALTY_UPDATE', 'REFERENCE_UPDATE', 'TOTALLOSS_NOTIFICATION', 'TL_TASK', 'TOTALLOSS_STOP_CHASE_TASK');

delete from scheduler_job where job_name in ('ECD_UPDATE', 'HIRE_UPDATE', 'LOU_UPDATE', 'TOTALLOSS_PACK', 'PENALTY_UPDATE', 'REFERENCE_UPDATE', 'TOTALLOSS_NOTIFICATION', 'TL_TASK', 'TOTALLOSS_STOP_CHASE_TASK');

update gmail_scheduler_job
    set processed_label = 'Processed ECD Updates',
        job_name='updateEcds'
where job_name = 'ECD_UPDATE';
update gmail_scheduler_job
    set processed_label = 'Processed Hire Updates',
        job_name='updateHire'
where job_name = 'HIRE_UPDATE';
update gmail_scheduler_job
    set processed_label = 'Processed Total Loss',
        job_name='totalLossPack'
where job_name = 'TOTALLOSS_PACK';
update gmail_scheduler_job
    set processed_label = 'Processed Hire Monitoring Updates',
        job_name='updateLou'
where job_name = 'LOU_UPDATE';
update gmail_scheduler_job
    set processed_label = 'Processed Reference Updates',
        job_name='updateChoReference'
where job_name = 'REFERENCE_UPDATE';
update gmail_scheduler_job
    set processed_label = 'Processed Penalty Updates',
        job_name='penaltyUpdate'
where job_name = 'PENALTY_UPDATE';
update gmail_scheduler_job
    set processed_label = 'Processed Total Loss',
        job_name='totalLossNotification'
where job_name = 'TOTALLOSS_NOTIFICATION';
update gmail_scheduler_job
    set processed_label = 'Processed Total Loss',
        job_name='totalLossTask'
where job_name = 'TL_TASK';
update gmail_scheduler_job
    set processed_label = 'Processed Total Loss',
        job_name='totalLossStopChaseTask'
where job_name = 'TOTALLOSS_STOP_CHASE_TASK';

ALTER TABLE scheduler_job DROP COLUMN email_subject;
ALTER TABLE scheduler_job DROP COLUMN autherised_user;
ALTER TABLE scheduler_job DROP COLUMN bcc_receiver;
ALTER TABLE scheduler_job DROP COLUMN error_message_receiver;
ALTER TABLE scheduler_job DROP COLUMN reply_to_sender;

--
-- CHOX-526: Refactor handling of linked tasks
--
ALTER TABLE task ADD COLUMN visibility_role2 character varying(24);

UPDATE task
  set visibility_role2 = t.visibility_role
from task t
where task.related_task = t.id;

DELETE FROM task  where related_task is not null and related_task > id;

ALTER TABLE task DROP COLUMN related_task;

create index task_ix on task(claim_id, visibility, visibility_role, visibility_role2, insurer, complete);

--
-- CHOX-540: Updates needed to batch update functionality that updates claim workgroup and owner
--
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ClaimPending' where id=1395;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ClaimReferredToEngineer' where id=1396;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.AwaitingCarHireInfo' where id=1397;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ClaimUnacknowledgedRouted' where id=1398;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ClaimRejectionContested' where id=1399;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ClaimUpdatedByEngineer' where id=1400;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.AwaitingInvoiceData' where id=1401;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ClaimReferredToFNOL' where id=1402;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ClaimUnacknowledgedUnassigned' where id=1403;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ManualInvoiceUnassigned' where id=933;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ManualInvoiceContested' where id=1024;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ManualInvoicePaid' where id=1025;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ManualInvoiceBRERejected' where id=1026;
UPDATE accessibility set name='activity.assignManualInvoiceOwner.ManualInvoiceBREApproved' where id=1027;
delete from accessibility_item where accessibility_id in (select id from accessibility where name like 'activity.assignManualInvoiceOwner.%' and id !=933);
delete from accessibility where name like 'activity.assignManualInvoiceOwner.%' and id !=933;

insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ManualInvoiceUnassigned', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ManualInvoiceContested', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ManualInvoicePaid', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ManualInvoiceBRERejected', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ManualInvoiceBREApproved', false, false, false, false, 17);

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceUnassigned' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceUnassigned' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceUnassigned' and claim_type=17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceContested' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceContested' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceContested' and claim_type=17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoicePaid' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoicePaid' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoicePaid' and claim_type=17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceBRERejected' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceBRERejected' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceBRERejected' and claim_type=17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceBREApproved' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceBREApproved' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='activity.AssignOwner.ManualInvoiceBREApproved' and claim_type=17;

delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.claimOwnership.ManualInvoiceBREApproved');
delete from accessibility where name='batch.claimOwnership.ManualInvoiceBREApproved';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.claimOwnership.ManualInvoiceBRERejected');
delete from accessibility where name='batch.claimOwnership.ManualInvoiceBRERejected';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.claimOwnership.ManualInvoiceContested');
delete from accessibility where name='batch.claimOwnership.ManualInvoiceContested';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='batch.claimOwnership.ManualInvoicePaid');
delete from accessibility where name='batch.claimOwnership.ManualInvoicePaid';



delete from accessibility_item where accessibility_id in (select id from accessibility where name like 'batch.updateClaimWorkgroupAndOwner.%');
delete from accessibility where name like 'batch.updateClaimWorkgroupAndOwner.%';

insert into accessibility(name,is_workgroup_check,is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('batch.updateInsurerClaimOwner.ManualInvoiceBREApproved', false, false, false, false, false, true, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('batch.updateInsurerClaimOwner.ManualInvoiceBRERejected', false, false, false, false, false, true, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('batch.updateInsurerClaimOwner.ManualInvoiceContested', false, false, false, false, false, true, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_workgroup_enabled, check_claimownership_enabled, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('batch.updateInsurerClaimOwner.ManualInvoicePaid', false, false, false, false, false, true, 17);

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceBREApproved' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceBREApproved' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceBREApproved' and claim_type=17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceBRERejected' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceBRERejected' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceBRERejected' and claim_type=17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceContested' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceContested' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoiceContested' and claim_type=17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoicePaid' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoicePaid' and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name='batch.updateInsurerClaimOwner.ManualInvoicePaid' and claim_type=17;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 2 FROM accessibility WHERE name like 'extraAction.updateManualInvWorkgroupClaimOwner.%';


insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.AwaitingLiabilityResolution', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.AwaitingInvoiceData', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ClaimReferredToEngineer', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ContestedInvoiceReferredToCHO', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ContestedInvoiceReferredToInsurer', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.InvoiceApprovedByBRE', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.InvoiceDataCalculationIncorrect', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.InvoiceEscalated', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.InvoiceEscalatedToHandler', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.InvoicePaymentLogged', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ClaimUnacknowledgedRouted', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.AwaitingInvoicePayment', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.ClaimUpdatedByEngineer', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.PaymentReceived', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.InvoiceReferredToClaimsHandler', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.InvoiceReferredToEngineer', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.AwaitingCarHireInfo', false, false, false, false, 17);
insert into accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled,claim_type)
      VALUES ('activity.AssignOwner.AwaitingLitigationOutcome', false, false, false, false, 17);

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%'
          and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome') and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%'
          and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome') and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%'
          and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome') and claim_type=17;
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_CHOX_ADMIN', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%'
          and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome') and claim_type=17;


insert into accessibility(name,is_workgroup_check,is_ownership_check, check_claimownership_enabled,claim_type)
      select name, false, false, true, 3 from accessibility where name like 'activity.AssignOwner.%' and claim_type is null
         and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome' );

insert into accessibility(name,is_workgroup_check,is_ownership_check, check_claimownership_enabled,claim_type)
      select name, false, false, true, 4 from accessibility where name like 'activity.AssignOwner.%' and claim_type is null
      and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
             or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
             or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
             or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
             or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
             or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome' );

insert into accessibility(name,is_workgroup_check,is_ownership_check, check_claimownership_enabled,claim_type)
      select name, false, false, true, 7 from accessibility where name like 'activity.AssignOwner.%' and claim_type is null
      and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
             or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
             or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
             or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
             or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
             or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome' );

insert into accessibility(name,is_workgroup_check,is_ownership_check, check_claimownership_enabled,claim_type)
      select name, false, false, true, 11 from accessibility where name like 'activity.AssignOwner.%' and claim_type is null
      and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
             or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
             or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
             or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
             or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
             or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome' );

insert into accessibility(name,is_workgroup_check,is_ownership_check, check_claimownership_enabled,claim_type)
      select name, false, false, true, 18 from accessibility where name like 'activity.AssignOwner.%' and claim_type is null
      and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
             or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
             or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
             or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
             or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
             or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome' );

INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_COM', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%'
          and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome') and claim_type in (3,4,7,11,18);
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_MNG', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%'
          and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome') and claim_type in (3,4,7,11,18);
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_INS_CH', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%'
          and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome') and claim_type in (3,4,7,11,18);
INSERT INTO accessibility_item(accessibility_id, role, access_right)
      SELECT id, 'ROLE_CHOX_ADMIN', 1 FROM accessibility WHERE name like 'activity.AssignOwner.%'
          and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome') and claim_type in (3,4,7,11,18);

update accessibility set claim_type=0
where claim_type is null and name like 'activity.AssignOwner.%'
  and (name like '%.AwaitingLiabilityResolution' or name like '%.AwaitingInvoiceData' or name like '%.ClaimReferredToEngineer'
                or name like '%.ContestedInvoiceReferredToCHO' or name like '%.ContestedInvoiceReferredToInsurer'
                or name like '%.InvoiceApprovedByBRE' or name like '%.InvoiceDataCalculationIncorrect' or name like '%.InvoiceEscalated' or name like '%.InvoiceEscalatedToHandler'
                or name like '%.InvoicePaymentLogged' or name like '%.ClaimUnacknowledgedRouted' or name like '%.AwaitingInvoicePayment'
                or name like '%.ClaimUpdatedByEngineer' or name like '%.PaymentReceived' or name like '%.InvoiceReferredToClaimsHandler' or name like '%.InvoiceReferredToEngineer'
                or name like '%.ClaimReferredToFNOL' or name like '%.AwaitingCarHireInfo' or name like '%.AwaitingLitigationOutcome');

--
-- CHOX-551: Remove BRE INFO messages
--
delete from history where type='INFO';


--
-- CHOX-564: Claim status is changed when updating owner
--
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateInsurerClaimOwner.ClaimReferredToFNOL');
delete from accessibility where name='extraAction.updateInsurerClaimOwner.ClaimReferredToFNOL';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.ClaimReferredToFNOL');
delete from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.ClaimReferredToFNOL';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.SubscriberClaimRejected');
delete from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.SubscriberClaimRejected';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateClaimOwner.ClaimReferredToFNOL');
delete from accessibility where name='extraAction.updateClaimOwner.ClaimReferredToFNOL';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroup.ClaimReferredToFNOL');
delete from accessibility where name='extraAction.updateClaimWorkgroup.ClaimReferredToFNOL';

delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateInsurerClaimOwner.ClaimPending');
delete from accessibility where name='extraAction.updateInsurerClaimOwner.ClaimPending';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateInsurerClaimOwner.ClaimRejectionContested');
delete from accessibility where name='extraAction.updateInsurerClaimOwner.ClaimRejectionContested';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.ClaimPending');
delete from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.ClaimPending';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.ClaimRejectionContested');
delete from accessibility where name='extraAction.updateClaimWorkgroupAndOwner.ClaimRejectionContested';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroup.ClaimPending');
delete from accessibility where name='extraAction.updateClaimWorkgroup.ClaimPending';
delete from accessibility_item where accessibility_id in (select id from accessibility where name='extraAction.updateClaimWorkgroup.ClaimRejectionContested');
delete from accessibility where name='extraAction.updateClaimWorkgroup.ClaimRejectionContested';

--
-- CHOX-556: Unable to update workgroup with a user with only Claim Handler role
--
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.AwaitingCarHireInfo';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.AwaitingInvoiceData';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.AwaitingInvoicePayment';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.AwaitingLiabilityResolution';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.AwaitingLitigationOutcome';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.ClaimReferredToEngineer';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.ClaimUnacknowledgedRouted';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.ClaimUpdatedByEngineer';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.ContestedInvoiceReferredToCHO';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.ContestedInvoiceReferredToInsurer';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.InvoiceApprovedByBRE';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.InvoiceDataCalculationIncorrec';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.InvoiceEscalated';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.InvoiceEscalatedToHandler';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.InvoicePaymentLogged';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.InvoiceReferredToClaimsHandler';
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CH', 1, id from accessibility where name='activity.AssignWorkgroup.InvoiceReferredToEngineer';

