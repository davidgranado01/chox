drop function cho_payment_profile_inc_open_invoices(int, int);
create or replace function cho_payment_profile_inc_open_invoices
(
    choid int ,insid int
)
returns table
(
   Insurer character varying(128),
   Supplier_Reference character varying(128),
   Invoice_Upload_Month text,
   Insurer_Claim_Number character varying(128),
   Original_Total_To_Pay numeric(10,2),
   Original_Full_Total_Requested numeric(10,2),
   Final_Total_To_Pay numeric(10,2),
   Invoice_Rejection_Reason character varying(128),
   "Passed_BRE" text,
   "Contested_With_CHO" text,
   "Current_Status" character varying(40)
)
as $$ DECLARE 
BEGIN 
RETURN QUERY

SELECT ins.name AS Insurer,
       c.cho_reference AS Supplier_Reference,
       to_char(i.created_date, 'month') AS Invoice_Upload_Month,
       c.claim_number AS Insurer_Claim_Number,
       io.total_to_pay AS Original_Total_To_Pay,
       io.full_total_to_pay AS Original_Full_Total_Requested,
       i.total_to_pay AS Final_Total_To_Pay,
       ror.name AS Invoice_Rejection_Reason,
       CASE
           WHEN
                  (SELECT count(*)
                   FROM audit_trail a2
                   WHERE a2.claim_id=c.id
                     AND a2.new_status IN ('InvoiceEscalated',
                                           'InvoiceEscalatedToHandler')
                     AND reverted=FALSE) > 0 THEN 'N'
           ELSE 'Y'
       END AS Passed_BRE,
       CASE
           WHEN
                  (SELECT count(*)
                   FROM audit_trail a2
                   WHERE a2.claim_id=c.id
                     AND a2.new_status='ContestedInvoiceReferredToCHO'
                     AND reverted=FALSE) > 0 THEN 'Y'
           ELSE 'N'
       END AS Contested_With_CHO,
       c.status AS Current_Status
FROM claim c,
     insurer ins,
     invoice i
LEFT OUTER JOIN reason_of_rejection ror ON (i.reason_of_rejection_id = ror.id), invoice_original io
WHERE c.invoice_id = i.id
  AND io.id = i.invoice_original_id
  AND (c.insurer_id = insid or insid = -1) -- Insurer parameter

  AND c.insurer_id = ins.id
  AND (c.chorganisation_id = choid or choid = -1)  -- CHO parameter

  AND c.status NOT IN ('InvoiceRejectionAccepted',
                       'ClaimClosed',
                       'PaymentReceived')
  ORDER BY ins.name;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION cho_payment_profile_inc_open_invoices(int, int) TO chox_user;
GRANT EXECUTE ON FUNCTION cho_payment_profile_inc_open_invoices(int, int) TO chox_mi;