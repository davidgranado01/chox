drop function cho_payment_profile(text, text ,int, int);
create or replace function cho_payment_profile
(
   dat text, dat1 text, choid int ,insid int
)
returns table
(
   Insurer character varying(128),
   "Supplier Reference" character varying(128),
   "Invoice Upload Month" text,
   "Insurer Claim Number" character varying(128),
   "Original Total To Pay" numeric(10,2),
   "Original Full Total Requested" numeric(10,2),
   "Final Total To Pay" numeric(10,2),
   "Invoice Rejection Reason" character varying(128),
   "Passed_BRE" text,
   "Contested_With_CHO" text,
   "Invoice Payment Time" numeric(6,1),
   "Date Claim Went To Payment Received" timestamp(0)
)
as $$ DECLARE 
startDate date;
endDate date;
BEGIN 
startDate = dat::Date;
endDate = dat1::Date;
RETURN QUERY

SELECT ins.name AS "Insurer",
       c.cho_reference AS "Supplier Reference",
       to_char(i.created_date, 'month') AS "Invoice Upload Month",
       c.claim_number AS "Insurer Claim Number",
       io.total_to_pay AS "Original Total To Pay",
       io.full_total_to_pay AS "Original Full Total Requested",
       i.total_to_pay AS "Final Total To Pay",
       ror.name AS "Invoice Rejection Reason",
       CASE WHEN (SELECT count(*)
                   FROM audit_trail a2
                   WHERE a2.claim_id=c.id
                     AND a2.new_status IN ('InvoiceEscalated',
                                           'InvoiceEscalatedToHandler')
                     AND reverted=FALSE) > 0 THEN 'N'
           ELSE 'Y'
       END AS Passed_BRE,
       CASE WHEN (SELECT count(*)
                   FROM audit_trail a2
                   WHERE a2.claim_id=c.id
                     AND a2.new_status='ContestedInvoiceReferredToCHO'
                     AND reverted=FALSE) > 0 THEN 'Y'
           ELSE 'N'
       END AS Contested_With_CHO,
       cast(extract(EPOCH FROM a.update_date - i.created_date)/(60*60*24.0) AS numeric(6,1)) AS "Invoice Payment Time",
       a.created_date::timestamp(0) AS "Date Claim Went To Payment Received"
FROM claim c,
     insurer ins,
     invoice i
LEFT OUTER JOIN reason_of_rejection ror ON (i.reason_of_rejection_id = ror.id), audit_trail a, invoice_original io 
WHERE c.invoice_id = i.id
  AND io.id = i.invoice_original_id
  AND (c.insurer_id = insid or insid = -1)  -- Insurer parameter
  AND c.insurer_id = ins.id
  AND (c.chorganisation_id = choid or choid = -1)  -- CHO parameter

  AND a.claim_id = c.id
  AND a.new_status='PaymentReceived'
  AND a.created_date::Date BETWEEN startDate AND endDate -- Period parameter

  AND a.reverted = FALSE
  ORDER BY ins.name;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION cho_payment_profile(text, text ,int, int) TO chox_user;
GRANT EXECUTE ON FUNCTION cho_payment_profile(text, text ,int, int) TO chox_mi;