--------------------------------------------------------------------------------
-- 8.1.1 IMS Email/Task Connectivity
--------------------------------------------------------------------------------
ALTER TABLE scheduler_job ADD COLUMN reply_to_sender boolean not null DEFAULT true;

INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver, reply_to_sender,
                           created_by, created_date, last_modified_by, last_modified_date, version) 
    SELECT 'admin@erac.com', 'Ch0xAdm1n', 'TOTALLOSS_NOTIFICATION', 'IMS TL Notification',
           'john.dowson@sherwoodts.co.uk', 'jldowson@gmail.com', 'john.dowson@sherwoodts.co.uk', false,
           999, now(), 999, now(), 0;


INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver, reply_to_sender,
                           created_by, created_date, last_modified_by, last_modified_date, version) 
    SELECT 'admin@erac.com', 'Ch0xAdm1n', 'TOTALLOSS_PACK', 'IMS TL Pack',
           'john.dowson@sherwoodts.co.uk', 'jldowson@gmail.com', 'john.dowson@sherwoodts.co.uk', false,
           999, now(), 999, now(), 0;


INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver, reply_to_sender,
                           created_by, created_date, last_modified_by, last_modified_date, version) 
    SELECT 'admin@erac.com', 'Ch0xAdm1n', 'TOTALLOSS_CHASE_TASK', 'not used',
           'john.dowson@sherwoodts.co.uk', 'jldowson@gmail.com', 'john.dowson@sherwoodts.co.uk', false,
           999, now(), 999, now(), 0;


INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver, reply_to_sender,
                           created_by, created_date, last_modified_by, last_modified_date, version) 
    SELECT 'admin@erac.com', 'Ch0xAdm1n', 'TOTALLOSS_STOP_CHASE_TASK', 'IMS TL Stop Chase Request',
           'john.dowson@sherwoodts.co.uk', 'jldowson@gmail.com', 'john.dowson@sherwoodts.co.uk', false,
           999, now(), 999, now(), 0;


ALTER TABLE claim ADD COLUMN is_total_loss_chase boolean not null DEFAULT false;
ALTER TABLE claim ADD COLUMN liability_modified_date timestamp without time zone;
ALTER TABLE claim ADD COLUMN liability_status_modified_date timestamp without time zone;


----------------------
-- End of 8.1.1 
----------------------
