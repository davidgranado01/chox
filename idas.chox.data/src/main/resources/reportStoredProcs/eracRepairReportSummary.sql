drop function erac_repair_report_summary(integer[], integer[], text, text, integer[]);

create or replace function erac_repair_report_summary
(
   IN insurerIds integer[], IN choIds integer[], IN startPeriod text, IN endPeriod text, IN claimTypes integer[]
)
returns table
(
   "No. Claims Uploaded" bigint,
   "No. Invoices Uploaded" bigint,
   "No. Invoices Paid" bigint,
   "No. Invoices Discarded" bigint,
   "Total Original Repair Invoice Value" numeric(10,2),
   "Total Current Repair Invoice Value" numeric(10,2),
   "Total Repair Value Paid" numeric(10,2),
   "Total Repair Value Discarded" numeric(10,2),
   "Total Interim Payments Paid" numeric(10,2),
   "Total Repair Penalties Charged" numeric(10,2),
   "Total Repair Penalties Paid" numeric(10,2)
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY


select
    (select count(*) from claim c left outer join invoice i on c.invoice_id = i.id, hire_monitoring_detail hmd
      where c.hire_monitoring_detail_id = hmd.id
        and ((c.invoice_id is null and (hmd.is_repair_only_check is true or c.managing_repair is true))
            or (c.invoice_id is not null and i.repair_net > 0.0))
        and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
        and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
        and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
        and c.created_date between startDate and endDate) as "No. Claims Uploaded",

    (select count(*) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
        and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
        and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
        and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
        and c.created_date between startDate and endDate) as "No. Invoices Uploaded",

    (select count(*) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
       and (c.status='PaymentReceived' or c.status='InvoicePaymentLogged')
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "No. Invoices Paid",

    (select count(*) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
       and (c.status='ClaimClosed' or c.status='InvoiceRejectionAccepted')
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "No. Invoices Discarded",

    (select sum(io.repair_gross) from claim c, invoice i, invoice_original io
      where c.invoice_id = i.id and i.repair_net > 0.0
       and i.invoice_original_id = io.id
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "Total Original Repair Invoice Value",

    (select sum(i.repair_gross) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "Total Current Repair Invoice Value",

    (select sum(i.repair_gross) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
       and (c.status='PaymentReceived' or c.status='InvoicePaymentLogged')
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "Total Repair Value Paid", 

    (select sum(i.repair_gross) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
       and (c.status='ClaimClosed' or c.status='InvoiceRejectionAccepted')
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "Total Repair Value Discarded",

    (select sum(i.interim_payment_made) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
       and c.status!='PaymentReceived' and c.status != 'InvoicePaymentLogged'
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "Total Interim Payments Paid",

    (select sum(i.repair_penalty_charge) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "Total Repair Penalties Charged",

    (select sum(i.repair_penalty_charge) from claim c, invoice i
      where c.invoice_id = i.id and i.repair_net > 0.0
       and (c.status='PaymentReceived' or c.status='InvoicePaymentLogged')
       and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
       and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
       and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
       and c.created_date between startDate and endDate) as "Total Repair Penalties Paid"
;


END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION erac_repair_report_summary(integer[], integer[], text, text, integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION erac_repair_report_summary(integer[], integer[], text, text, integer[]) TO chox_mi;
-- e.g.
--     select * from erac_repair_report_summary(array[]::integer[], array[1007], '2012-01-01','2014-07-01',array[]::integer[]);
