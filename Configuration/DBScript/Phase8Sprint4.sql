--------------------------------------------------------------------------------
-- 8.4.1 Full or partial ERAC branding
--------------------------------------------------------------------------------
ALTER TABLE insurer ADD COLUMN branding int DEFAULT 0 NOT NULL;
ALTER TABLE chorganisation ADD COLUMN branding int DEFAULT 0 NOT NULL;
----------------------
-- End of 8.4.1
----------------------


--------------------------------------------------------------------------------
-- 8.4.2 Ability to link CHOs and switch claims and prevent duplicates
--------------------------------------------------------------------------------
ALTER TABLE chorganisation ADD COLUMN linked_cho int;
----------------------
-- End of 8.4.2
----------------------
