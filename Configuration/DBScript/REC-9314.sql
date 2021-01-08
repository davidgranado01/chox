DO $rename$
BEGIN

  IF NOT EXISTS(SELECT *
    FROM information_schema.columns
    WHERE table_name='insurer_hire_monitoring_detail' and column_name='payment_type')
  THEN
    ALTER TABLE insurer_hire_monitoring_detail ADD COLUMN IF NOT EXISTS payment_type VARCHAR(10);
  END IF;

  IF EXISTS(SELECT *
    FROM information_schema.columns
    WHERE table_name='insurer_hire_monitoring_detail' and column_name='total_loss_check_issued')
  THEN
      ALTER TABLE "public"."insurer_hire_monitoring_detail" RENAME COLUMN "total_loss_check_issued" TO "total_loss_payment_issued";
  END IF;

  IF EXISTS(SELECT *
    FROM information_schema.columns
    WHERE table_name='insurer_hire_monitoring_detail' and column_name='total_loss_check_received')
  THEN
      ALTER TABLE "public"."insurer_hire_monitoring_detail" RENAME COLUMN "total_loss_check_received" TO "total_loss_payment_received";
  END IF;
END $rename$;
