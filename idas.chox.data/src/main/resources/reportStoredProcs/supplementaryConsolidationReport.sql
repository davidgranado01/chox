DROP function supplementaryConsolidationReport(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR);

CREATE OR REPLACE FUNCTION supplementaryConsolidationReport(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR)

RETURNS TABLE("Supplier Reference" VARCHAR,
              "Current CHOX Status" VARCHAR,
              "Invoice Upload Date" timestamp without time zone,
              "Hire Gross" numeric(10,2),
              "Engineer Fee Gross" numeric(10,2),
              "Repair Gross" numeric(10,2),
              "Total Loss Fee Gross" numeric(10,2),
              "Storage & Recovery Gross" numeric(10,2),
              "Total Gross" numeric(10,2),
              "Total Penalty Charge" numeric(10,2),
              "Full Total Requested" numeric(10,2),
              "Total To Pay" numeric(10,2),
              "Interim Payment" numeric(10,2))
AS

$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $4::DATE;
DATE_TO = $5::DATE;

RETURN QUERY

SELECT c.cho_reference as "Supplier Reference",
    c.status as "Current CHOX Status",
    inv.created_date as "Invoice Upload Date",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.hire_gross else sum(i2.hire_gross) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Hire Gross",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.engineer_fee_gross else sum(i2.engineer_fee_gross) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Engineer Fee Gross",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.repair_gross else sum(i2.repair_gross) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Repair Gross",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.total_loss_gross else sum(i2.total_loss_gross) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Total Loss Fee Gross",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.storage_recovery_gross else sum(i2.storage_recovery_gross) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Storage & Recovery Gross",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.total_gross else sum(i2.total_gross) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Total Gross",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.total_penalty_charge else sum(i2.total_penalty_charge) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Total Penalty Charge",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.full_total_to_pay else sum(i2.full_total_to_pay) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Full Total Requested",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.total_to_pay else sum(i2.total_to_pay) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Total To Pay",
    (select case when c.claim_type not in (1,5,8,12,15,19) then inv.interim_payment_made else sum(i2.interim_payment_made) end
     from claim c2, invoice i2, customer cu2
      where c2.invoice_id=i2.id and c2.insurer_id=c.insurer_id and c2.chorganisation_id=c.chorganisation_id
        and c2.customer_id = cu2.id and cu2.claim_reference=cu.claim_reference and c2.claim_type in (1,5,8,12,15,19,2,6,9,13,16,20)) as "Interim Payment"

FROM claim c, invoice inv, customer cu
WHERE c.invoice_id = inv.id AND c.customer_id=cu.id
  AND inv.created_date >= DATE_FROM and inv.created_date < DATE_TO
  AND (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
  AND (case when array_length(insIds, 1) > 0 then c.insurer_id = ANY(insIds) else true end)
  AND (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end)
  AND c.claim_type not in (2,6,9,13,16,20);

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION processedNotificationsReport(
                                              IN choId INTEGER,
                                              IN insIds INTEGER[],
                                              IN claimTypes INTEGER[],
                                              IN startPeriod VARCHAR,
                                              IN endPeriod VARCHAR)
TO chox_user;

GRANT EXECUTE ON FUNCTION supplementaryConsolidationReport(
                                              IN insIds INTEGER[],
                                              IN choIds INTEGER[],
                                              IN claimTypes INTEGER[],
                                              IN startPeriod VARCHAR,
                                              IN endPeriod VARCHAR)
TO chox_mi;

/* select * from supplementaryConsolidationReport(null, array[1007], null, '2009-01-01', '2014-07-01'); */


