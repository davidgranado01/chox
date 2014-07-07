drop function erac_repair_report(integer[], integer[], text, text, integer[]);
create or replace function erac_repair_report
(
   IN insurerIds integer[], IN choIds integer[], IN startPeriod text, IN endPeriod text, IN claimTypes integer[]
)
returns table
(
   "Supplier Reference" character varying(128),
   "Insurer Claim Number" character varying(128),
   "Customer VRN" character varying(16),
   "Insurer Name" character varying(128),
   "Insurer Workgroup Name" character varying,
   "Insurer Claim Owner" text,
   "CHO Claim Owner" text,
   "Current Claim Status" character varying(40),
   "Reason Claim Closed" character varying(32),
   "Date Claim Uploaded" text,
   "Liability Status" text,
   "Has Invoice?" text,
   "Claim Closed?" text,
   "Invoice Paid?" text,
   "Invoice Payment Time" integer,
   "Original Repair Gross Amount" numeric(8,2),
   "Current Repair Gross Amount" numeric(8,2),
   "Repair Penalty Charge Amount" numeric(8,2),
   "Repair Gross Amount Paid" numeric(8,2),
   "Repair Penalty Charge Paid" numeric(8,2)
   
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY


select c.cho_reference as "Supplier Reference",
       c.claim_number as "Insurer Claim Number",
       cu.vehicle_registration as "Customer VRN",
       ins.name as "Insurer Name",
       w.name as "Insurer Workgroup Name",
       wu.first_name || ' ' || wu.last_name as "Insurer Claim Owner",
       wu2.first_name || ' ' || wu2.last_name as "CHO Claim Owner",
       c.status as "Current Claim Status",
       case when c.status='ClaimClosed' then substring(cm.comment from 15)
        else (case when c.status='ClaimRejectionAccepted' then (select ror.name from reason_of_rejection ror where id=at2.claim_reason_of_rejection)
        else '' end) end as "Reason Claim Closed",
       to_char(c.created_date, 'dd/mm/yyyy') as "Date Claim Uploaded",
       getLiabilityStatus(c.liability_status) as "Liability Status",
       case when c.invoice_id is null then 'No' else 'Yes' end as "Has Invoice?",
       case when c.status in ('ClaimRejectionAccepted','ClaimClosed','InvoiceRejectionAccepted') then 'Yes' else 'No' end as "Claim Closed?",
       case when c.status in ('InvoicePaymentLogged','PaymentReceived') then 'Yes' else 'No' end as "Invoice Paid?",
       floor((extract(epoch from (at.created_date - i.created_date)) / 86400))::integer as "Invoice Payment Time",
       io.repair_gross as "Original Repair Gross Amount",
       i.repair_gross as "Current Repair Gross Amount",
       i.repair_penalty_charge as "Repair Penalty Charge Amount",
       null as "Repair Gross Amount Paid",
       null as "Repair Penalty Charge Paid"
from claim c left outer join customer cu on c.customer_id = cu.id
             left outer join third_party tp on c.third_party_id = tp.id
             left outer join workgroup w on c.workgroup_id = w.id
             left outer join web_user wu on c.claim_owner_id = wu.id
             left outer join web_user wu2 on c.cho_claim_owner_id = wu2.id
             left outer join invoice i on c.invoice_id = i.id
             left outer join audit_trail at on c.id = at.claim_id and at.new_status = 'InvoicePaymentLogged' and at.reverted = false
             left outer join invoice_original io on i.invoice_original_id = io.id
             left outer join insurer ins on tp.insurer_id = ins.id
	     left outer join hire_monitoring_detail hmd on c.hire_monitoring_detail_id = hmd.id
	     left outer join comment cm on (cm.claim_id = c.id and cm.comment like 'Claim Closed:%'),
             audit_trail at2
where c.invoice_id is null
  and (hmd.is_repair_only_check is true or c.managing_repair is true)
  and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
  and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
  and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
  and c.created_date between startDate and endDate
  and at2.claim_id = c.id and at2.new_status = c.status and at2.reverted = false
  and not exists (select * from audit_trail at3 where at3.claim_id=c.id and at3.id != at2.id and at3.new_status = c.status and at3.created_date > at2.created_date)
  and not exists (select * from comment cm2 where cm2.claim_id=c.id and cm2.id != cm.id and cm2.comment like 'Claim Closed:%' and cm2.created_date > cm.created_date)
UNION
select c.cho_reference as "Supplier Reference",
       c.claim_number as "Insurer Claim Number",
       cu.vehicle_registration as "Customer VRN",
       ins.name as "Insurer Name",
       w.name as "Insurer Workgroup Name",
       wu.first_name || ' ' || wu.last_name as "Insurer Claim Owner",
       wu2.first_name || ' ' || wu2.last_name as "CHO Claim Owner",
       c.status as "Current Claim Status",
       case when c.status='ClaimClosed' then substring(cm.comment from 15)
        else (case when c.status='InvoiceRejectionAccepted' then (select ror.name from reason_of_rejection ror where id=at2.invoice_reason_of_rejection)
        else '' end) end as "Reason Claim Closed",
       to_char(c.created_date, 'dd/mm/yyyy') as "Date Claim Uploaded",
       getLiabilityStatus(c.liability_status) as "Liability Status",
       case when c.invoice_id is null then 'No' else 'Yes' end as "Has Invoice?",
       case when c.status in ('ClaimRejectionAccepted','ClaimClosed','InvoiceRejectionAccepted') then 'Yes' else 'No' end as "Claim Closed?",
       case when c.status in ('InvoicePaymentLogged','PaymentReceived') then 'Yes' else 'No' end as "Invoice Paid?",
       floor((extract(epoch from (at.created_date - i.created_date)) / 86400))::integer as "Invoice Payment Time",
       io.repair_gross as "Original Repair Gross Amount",
       i.repair_gross as "Current Repair Gross Amount",
       i.repair_penalty_charge as "Repair Penalty Charge Amount",
       case when i.final_payment is not null then i.repair_gross_paid else case when c.status in ('InvoicePaymentLogged','PaymentReceived') then i.repair_gross else null end end as "Repair Gross Amount Paid",
       case when i.final_payment is not null then i.repair_penalty_charge_paid else case when c.status in ('InvoicePaymentLogged','PaymentReceived') then i.repair_penalty_charge  else null end end as "Repair Penalty Charge Paid"
from claim c left outer join customer cu on c.customer_id = cu.id
             left outer join third_party tp on c.third_party_id = tp.id
             left outer join workgroup w on c.workgroup_id = w.id
             left outer join web_user wu on c.claim_owner_id = wu.id
             left outer join web_user wu2 on c.cho_claim_owner_id = wu2.id
             left outer join invoice i on c.invoice_id = i.id
             left outer join audit_trail at on c.id = at.claim_id and at.new_status = 'InvoicePaymentLogged' and at.reverted = false
             left outer join invoice_original io on i.invoice_original_id = io.id
             left outer join insurer ins on tp.insurer_id = ins.id
	     left outer join hire_monitoring_detail hmd on c.hire_monitoring_detail_id = hmd.id
	     left outer join comment cm on (cm.claim_id = c.id and cm.comment like 'Claim Closed:%'),
             audit_trail at2
where c.invoice_id is not null
  and i.repair_net > 0.0
  and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
  and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
  and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end) 
  and c.created_date between startDate and endDate
  and at2.claim_id = c.id and at2.new_status = c.status and at2.reverted = false
  and not exists (select * from audit_trail at3 where at3.claim_id=c.id and at3.id != at2.id and at3.new_status = c.status and at3.created_date > at2.created_date)
  and not exists (select * from comment cm2 where cm2.claim_id=c.id and cm2.id != cm.id and cm2.comment like 'Claim Closed:%' and cm2.created_date > cm.created_date)
order by "Date Claim Uploaded"
;


END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION erac_repair_report(integer[], integer[], text, text, integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION erac_repair_report(integer[], integer[], text, text, integer[]) TO chox_mi;
-- e.g.
--     select * from erac_repair_report(array[]::integer[], array[1007], '2012-01-01','2014-07-01',array[]::integer[]) order by "Supplier Reference";
