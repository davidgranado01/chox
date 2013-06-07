CREATE OR REPLACE FUNCTION paymentsNotificationReport(
    IN choIds INTEGER[],
    IN insIds INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR)


RETURNS TABLE(  "Supplier Reference" VARCHAR,
                "Insurer Claim Number" VARCHAR,
                "Insurer Name" VARCHAR,
                "Date Payment Made" timestamp without time zone,
                "Total Paid Amount" numeric(8,2))
AS

$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $3::DATE;
DATE_TO = $4::DATE;

RETURN QUERY

select  c.cho_reference as "Supplier Reference",
        c.claim_number as "Insurer Claim Number",
        ins.name as "Insurer Name",
        at.created_date as "Date Payment Made",
        inv.total_to_pay as "Total Paid Amount"

FROM claim c, insurer ins, invoice inv, audit_trail at
WHERE c.insurer_id = ins.id
  AND c.invoice_id = inv.id
  AND c.id = at.claim_id
  AND c.insurer_id = ANY(insIds)
  AND c.chorganisation_id = ANY(insIds)
  AND at.new_status='InvoicePaymentLogged' and at.reverted=false
  AND at.created_date >= DATE_FROM and at.created_date < DATE_TO;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



GRANT EXECUTE ON FUNCTION paymentsNotificationReport(
                                              IN choIds INTEGER[],
                                              IN insIds INTEGER[],
                                              IN startPeriod VARCHAR,
                                              IN endPeriod VARCHAR)
TO chox_user;

GRANT EXECUTE ON FUNCTION paymentsNotificationReport(
                                              IN choIds INTEGER[],
                                              IN insIds INTEGER[],
                                              IN startPeriod VARCHAR,
                                              IN endPeriod VARCHAR)
TO chox_mi;

/* select * from paymentsNotificationReport(array[1132], array[3,19], '2013-05-31', '2013-06-07'); */
