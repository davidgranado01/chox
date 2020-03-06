-- VUL-6981 : remove passowrds values from gmail_scheduler_job and scheduler_job
--
-- Test : both following queries should return 1 row with an empty string value 
-- select distinct login_username, login_password from gmail_scheduler_job;
-- select distinct login_username, login_password from cheduler_job;


UPDATE gmail_scheduler_job  SET login_password='';
UPDATE scheduler_job  SET login_password='';
