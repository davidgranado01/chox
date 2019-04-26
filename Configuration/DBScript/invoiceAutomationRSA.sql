--
-- CHOX-611: Production - Claims Router Unable to Assign Manual Invoices
--
insert into accessibility_item(role, access_right, accessibility_id)
  select 'ROLE_INS_CR', 1, id from accessibility
  where name='activity.AssignOwner.ManualInvoiceUnassigned' and claim_type=17;

--
-- CHOX-647: Enable 'PaidInvoices' automation functionality for RSA
--
insert into scheduler_job(login_username, login_password, job_name, active, version, created_by, created_date, last_modified_by, last_modified_date)
    select 'paidInvoices.rsa', 'C0mpliance', 'PAID_INVOICES', true, 1, 999, now(), 999, now();
insert into web_user(email, first_name, last_name, password, created_by, created_date, last_modified_by, last_modified_date, insurer_id,
                            status, is_expired, user_name, version, telephone, show_browser_warning, password_last_modified_date)
    select 'john.dowson@valexa.com', 'Paid Invoices', '(via SFTP)', '6b53da3955e5fdaf93656fc15a0f0318', 999, now(), 999, now(), 3,
                            true, false, 'paidInvoices.rsa', 0, '07702 904147', false, now();

insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='paidInvoices.rsa' and r.name='ROLE_INS';
insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='paidInvoices.rsa' and r.name='ROLE_INS_CH';
