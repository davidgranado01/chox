--------------------------------------------------------------------------------
-- 8.6.1 CHO Supervisor Escalation Queue
--------------------------------------------------------------------------------
insert into web_user_role (name, created_by, last_modified_by, description,type_id, is_workgroup_related, is_ownership_related, version )
values ('ROLE_CHO_SUP',999, 999, 'Supervisor',2, true, false, 0);

ALTER TABLE chorganisation ADD COLUMN is_supervisor_enable boolean DEFAULT false;
ALTER TABLE chorganisation add column days_before_escalated integer;
ALTER TABLE chorganisation add column times_in_status_contested integer;

insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_SUP', 1, (select id from accessibility where name='filter.EscalatedInvoicesToSupervisor'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 1, (select id from accessibility where name='filter.EscalatedInvoicesToSupervisor'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MI', 1, (select id from accessibility where name='filter.EscalatedInvoicesToSupervisor'));
insert into accessibility_item(role, access_right, accessibility_id)
    select 'ROLE_CHO_SUP', 1, id from accessibility where name='menu.Search';
insert into accessibility_item(role, access_right, accessibility_id)
    select 'ROLE_CHO_SUP', 1, id from accessibility where name='menu.Inbox';

----------------------
-- End of 8.6.1
----------------------

--------------------------------------------------------------------------------
-- 8.6.2 Configurable Subscriber Parameters
--------------------------------------------------------------------------------


----------------------
-- End of 8.6.2
----------------------
