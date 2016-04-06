--
-- SQL Updates to support KBBS dashboards
--
ALTER TABLE chorganisation ADD COLUMN enable_kbbs_dashboard boolean not null default false;
ALTER TABLE chorganisation ADD COLUMN kbbs_manager_password character varying;
ALTER TABLE chorganisation ADD COLUMN kbbs_operative_password character varying;

ALTER TABLE insurer ADD COLUMN enable_kbbs_dashboard boolean not null default false;
ALTER TABLE insurer ADD COLUMN kbbs_manager_password character varying;
ALTER TABLE insurer ADD COLUMN kbbs_operative_password character varying;

