--------------------------------------------------------------------------------
-- 8.6.1 CHO Supervisor Escalation Queue
--------------------------------------------------------------------------------
insert into web_user_role (name, created_by, last_modified_by, description,type_id, is_workgroup_related, is_ownership_related, version )
    values ('ROLE_CHO_SUP',999, 999, 'Supervisor',3, true, false, 0);

ALTER TABLE chorganisation ADD COLUMN is_supervisor_enable boolean DEFAULT false;
ALTER TABLE chorganisation ADD COLUMN days_before_escalated integer;
ALTER TABLE chorganisation ADD COLUMN times_in_status_contested integer;

insert into accessibility_item (role, access_right, accessibility_id)
    values ('ROLE_CHO_SUP', 1, (select id from accessibility where name='filter.EscalatedInvoicesToSupervisor'));
insert into accessibility_item (role, access_right, accessibility_id)
    values ('ROLE_CHO_MNG', 1, (select id from accessibility where name='filter.EscalatedInvoicesToSupervisor'));
insert into accessibility_item (role, access_right, accessibility_id)
    values ('ROLE_CHO_MI', 1, (select id from accessibility where name='filter.EscalatedInvoicesToSupervisor'));
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
ALTER TABLE bre_band ADD COLUMN subscriber_sla_days integer not null DEFAULT 5;
ALTER TABLE bre_band ADD COLUMN subscriber_time_cut_off character varying(5) not null DEFAULT '15:00';
ALTER TABLE bre_band ADD COLUMN subscriber_resubmission_allowed integer not null DEFAULT 2;
----------------------
-- End of 8.6.2
----------------------

--------------------------------------------------------------------------------
-- 8.6.3 Configurable Fixed Fee Parameters
--------------------------------------------------------------------------------
ALTER TABLE bre_band ADD COLUMN fixedfee_sla_days integer not null DEFAULT 14;
ALTER TABLE bre_band ADD COLUMN fixedfee_time_cut_off character varying(5) not null DEFAULT '15:00';
ALTER TABLE bre_band ADD COLUMN fixedfee_resubmission_allowed integer not null DEFAULT 2;
----------------------
-- End of 8.6.3
----------------------

--------------------------------------------------------------------------------
-- 8.6.4 Task to prompt engineers inspection
--------------------------------------------------------------------------------
ALTER TABLE chorganisation ADD COLUMN allow_engineers_inspection_task boolean not null DEFAULT false;
----------------------
-- End of 8.6.4
----------------------
