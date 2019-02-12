ALTER TABLE insurer ADD COLUMN unacknowledged_claim_bulk_processing boolean NOT NULL DEFAULT false;

--
-- Set-up Scheduler Job and user
--
insert into web_user(email, first_name, last_name, password, created_by, created_date, last_modified_by, last_modified_date, insurer_id,
                        status, is_expired, user_name, version, telephone, show_browser_warning, password_last_modified_date)
    select 'john.dowson@valexa.com', 'Automation', 'Bulk Claim Acknowledgment', '#{DB_USER.SYSTEM_PASSWORD_HASHED}', 999, now(), 999, now(), 6,
                        true, false, 'acknowledgeClaims.dlg', 0, '07702 904147', false, now();

insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='acknowledgeClaims.dlg' and r.name='ROLE_INS';
insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='acknowledgeClaims.dlg' and r.name='ROLE_INS_CH';
insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='acknowledgeClaims.dlg' and r.name='ROLE_INS_CR';
insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='acknowledgeClaims.dlg' and r.name='ROLE_INS_COM';

insert into scheduler_job (login_username, login_password, job_name, active, created_by, created_date, last_modified_by, last_modified_date, version)
    select 'acknowledgeClaims.dlg', '#{DB_USER.SYSTEM_PASSWORD}', 'UNACKNOWLEDGED_CLAIM_BULK_PROCESSING', true, 999, now(), 999, now(), 0;
