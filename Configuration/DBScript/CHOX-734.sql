--
-- New db fields for bordereau/macro update, CHOX-734 and CHOX-735
--
ALTER TABLE hire_monitoring_detail
    ADD COLUMN engineers_report_sent timestamp without time zone,
    ADD COLUMN engineers_report_sent_last_modified timestamp without time zone,
    ADD COLUMN who_is_sending_pav varchar(21);
