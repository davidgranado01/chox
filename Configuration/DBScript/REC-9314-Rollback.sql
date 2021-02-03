ALTER TABLE insurer_hire_monitoring_detail
    DROP COLUMN IF EXISTS payment_type;

ALTER TABLE insurer_hire_monitoring_detail
    RENAME total_loss_payment_issued TO total_loss_check_issued;

ALTER TABLE insurer_hire_monitoring_detail
    RENAME total_loss_payment_received TO total_loss_check_received;