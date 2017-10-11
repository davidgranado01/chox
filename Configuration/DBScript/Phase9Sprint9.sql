--
-- CHOX-15: New BRE Rule: ECD vs Repair Completion Date
--
ALTER TABLE bre_band ADD COLUMN ecd_vs_repair_completion_date_check boolean NOT NULL default false;

--
-- CHOX-407: Add 'Copley' questions to Insurer Hire Monitoring
--
ALTER TABLE insurer_hire_monitoring_detail ADD COLUMN copley_offer_made boolean;
ALTER TABLE insurer_hire_monitoring_detail ADD COLUMN copley_offer_made_date timestamp without time zone;

--
-- CHOX-408: Add BRE Rule: Full Total Requested
--
ALTER TABLE bre_band ADD COLUMN full_total_requested_tolerance numeric(10,2);
ALTER TABLE bre_band ADD COLUMN full_total_requested_ceiling_check boolean NOT NULL default false;
