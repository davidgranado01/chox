ALTER TABLE insurer_hire_monitoring_detail
    ADD COLUMN IF NOT EXISTS payment_type VARCHAR(10);

ALTER TABLE insurer_hire_monitoring_detail
    RENAME total_loss_check_issued TO total_loss_payment_issued;

ALTER TABLE insurer_hire_monitoring_detail
    RENAME total_loss_check_received TO total_loss_payment_received;