ALTER TABLE insurer_hire_monitoring_detail
    ADD COLUMN IF NOT EXISTS tp_reported_incident_to_tpi timestamp without time zone,
    ADD COLUMN IF NOT EXISTS tl_report_sent_to_us timestamp without time zone,
    ADD COLUMN IF NOT EXISTS parts_received timestamp without time zone;

ALTER TABLE insurer_vehicle_hire
    ADD COLUMN IF NOT EXISTS rental_end timestamp without time zone;
