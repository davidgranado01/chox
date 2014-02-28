--------------------------------------------------------------------------------
-- 8.1.2 IMS Email/Task Connectivity
--------------------------------------------------------------------------------
insert into scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver,
                           created_by, created_date, last_modified_by, last_modified_date, version) 
    select 'admin@erac.com', 'Ch0xAdm1n', 'TOTALLOSS_NOTIFICATION', 'IMS Total Loss Notification',
           'john.dowson@sherwoodts.co.uk', 'jldowson@gmail.com', 'john.dowson@sherwoodts.co.uk',
           999, now(), 999, now(), 0;


----------------------
-- End of 8.1.2 
----------------------
