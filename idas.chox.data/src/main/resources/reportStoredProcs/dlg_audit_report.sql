-- e.g.
--     select * from dlg_audit_report(array[6], array[1007], '2009-01-01','2014-03-11',array[]::integer[]);

-- drop function dlg_audit_report(integer[], integer[], text, text, integer[]);

create or replace function dlg_audit_report
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
   "Date Invoice Uploaded" text,
   "Hire Net Amount" numeric(8,2),
   "Storage & Recovery Net Amount" numeric(8,2)
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
       to_char(i.created_date, 'dd/mm/yyyy') as "Date Invoice Uploaded",
       i.hire_net as "Hire Net Amount",
       i.storage_recovery_net as "Storage & Recovery Net Amount"
from claim c left outer join customer cu on c.customer_id = cu.id
             left outer join third_party tp on c.third_party_id = tp.id
             left outer join workgroup w on c.workgroup_id = w.id
             left outer join web_user wu on c.claim_owner_id = wu.id
             left outer join web_user wu2 on c.cho_claim_owner_id = wu2.id
             inner join invoice i on c.invoice_id = i.id
             left outer join insurer ins on tp.insurer_id = ins.id
where 
  (i.hire_net > 5000 OR i.storage_recovery_net > 0)
  and (case when array_length(claimTypes, 1) > 0 then c.claim_type = ANY(claimTypes) else true end) 
  and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end) 
  and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
  and i.created_date between startDate and endDate;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION dlg_audit_report(integer[], integer[], text, text, integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION dlg_audit_report(integer[], integer[], text, text, integer[]) TO chox_mi;

