--
-- CHOX-250: Add new field within Admin/Insurer/Details/Workflow Parameters config
--
ALTER TABLE insurer ADD COLUMN allow_default_hm_updates boolean not null default false;
