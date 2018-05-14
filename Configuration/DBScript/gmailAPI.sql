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

