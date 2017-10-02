--
-- CHOX-15: New BRE Rule: ECD vs Repair Completion Date
--
ALTER TABLE bre_band ADD COLUMN ecd_vs_repair_completion_date_check boolean not null default false;
