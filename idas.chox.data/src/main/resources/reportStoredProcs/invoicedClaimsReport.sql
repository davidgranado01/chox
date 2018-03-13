DROP FUNCTION invoiced_claims_report(IN choIDs INT[], IN insIDs INT[], IN startPeriod text, IN endPeriod text);

CREATE OR REPLACE FUNCTION invoiced_claims_report(
    IN choIDs INT[],
    IN insIDs INT[],
    IN startPeriod text,
    IN endPeriod text
)
 RETURNS TABLE (
    "CHO Name" varchar(128),
    "Insurer Name" varchar(64),
    "Supplier Reference" varchar(128),
    "Insurer Claim Number" varchar(128),
    "Current Claim Status" varchar(128),
    "Status Modified Date" Date,
    "Total Net" numeric(10,2),
    "Total VAT" numeric(10,2),
    "Total Gross" numeric(10,2),
    "Less Insurer Discount" numeric(10,2),
    "Less CHO Discount" numeric(10,2),
    "Full Total Requested" numeric(10,2),
    "Total To Pay" numeric(10,2),
    "Original Billed Amount" numeric(10,2),
    "Interim Payment Amount Made" numeric(10,2),
    "Interim Payment Amount Received" numeric(10,2),
    "Has Interim Payment Been Received?" text,
    "Date Invoiced" Date,
    "Workgroup" varchar(32),
    "Supplier Claim Invoice Number" varchar(64)
) AS $BODY$
DECLARE
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;

RETURN QUERY

select cho.name as "CHO Name", 
       ins.name as "Insurer Name", 
       c.cho_reference as "Supplier Reference",
       c.claim_number as "Insurer Claim Number", 
       c.status as "Current Claim Status",
       c.status_modified_date::Date as "Status Modified Date",
       i.total_net as "Total Net", 
       i.total_vat as "Total VAT",
       i.total_gross as "Total Gross", 
       i.insurer_discount as "Less Insurer Discount",
       i.discount as "Less CHO Discount", 
       i.full_total_to_pay as "Full Total Requested",
       i.total_to_pay as "Total To Pay",
       io.full_total_to_pay as "Original Billed Amount",
       i.interim_payment_made as "Interim Payment Amount Made",
       i.interim_payment_received as "Interim Payment Amount Received",
       case when i.interim_payment_received = interim_payment_made then 'Yes' else 'No' end as "Has Interim Payment Been Received?",
       i.date_invoiced::Date as "Date Invoiced",
       w.name as "Workgroup",
       i.claim_invoice_no as "Supplier Claim Invoice Number"
from chorganisation cho,
     invoice i,
     invoice_original io,
     insurer ins,
     claim c 
     left outer join workgroup w on (c.workgroup_id = w.id)
where c.chorganisation_id = cho.id 
     and i.invoice_original_id = io.id
     and c.invoice_id = i.id 
     and c.insurer_id = ins.id
     and (choIDs is null or cho.id = ANY (choIDs))
     and (insIDs is null or ins.id = ANY (insIDs))
     and c.created_date >= startDate and (endDate is null or c.created_date < endDate)
order by cho.name, 
         w.name, 
         c.created_date;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION invoiced_claims_report(IN choIDs INT[], IN insIDs INT[], IN startPeriod text, IN endPeriod text) TO chox_user;
GRANT EXECUTE ON FUNCTION invoiced_claims_report(IN choIDs INT[], IN insIDs INT[], IN startPeriod text, IN endPeriod text) TO chox_mi;

select * from invoiced_claims_report('{1016,1028,1013,1014,1029,1030,1341}'::INT[], null, '2017-01-01', null);