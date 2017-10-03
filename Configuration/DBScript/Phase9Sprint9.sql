--
-- CHOX-15: New BRE Rule: ECD vs Repair Completion Date
--
ALTER TABLE bre_band ADD COLUMN ecd_vs_repair_completion_date_check boolean not null default false;

--
-- CHOX-407: Add 'Copley' questions to Insurer Hire Monitoring
--
ALTER TABLE insurer_hire_monitoring_detail ADD COLUMN copley_offer_made boolean;
ALTER TABLE insurer_hire_monitoring_detail ADD COLUMN copley_offer_made_date timestamp without time zone;
