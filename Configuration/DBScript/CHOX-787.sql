--
-- New db fields for bordereau/macro update, CHOX-787
ALTER TABLE insurer_hire_monitoring_detail
    ADD COLUMN engineers_report_sent timestamp without time zone;
