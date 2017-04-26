drop function contested_count_report(int, int);
create or replace function contested_count_report
(
   choId int ,insId int
)
returns table
(
   "Supplier Reference" character varying(128),
   "Insurer Name" character varying(128),
   "No. Contested to CHO" bigint,
   "No. Contested to Insurer" bigint,
   "Reason of Rejection" character varying(32),
   "Total time in Contested to CHO" numeric(10,3),
   "Total time in Contested to Insurer" numeric(10,3),
   "Current Status" character varying(40)
)
as $$
DECLARE 
BEGIN 
RETURN QUERY

select cho_reference, ins.name,
       (select count(*) from audit_trail a
        where a.claim_id=c.id and a.reverted=false
          and a.new_status='ContestedInvoiceReferredToCHO') as NoContestedToCHO,
       (select count(*) from audit_trail a
        where a.claim_id=c.id and a.reverted=false
          and a.new_status='ContestedInvoiceReferredToInsurer') as NoContestedToInsurer,
       ror.name as "Reason of Rejection",
       (select case when (not exists (select * from audit_trail a where a.claim_id=c.id 
                                and a.original_status='ContestedInvoiceReferredToCHO')) 
       then 0.00 
       else 
          (select cast(sum(EXTRACT(EPOCH FROM (a2.update_date - a1.update_date))/(3600*24)) as numeric(10,3)) 
           from audit_trail a1, audit_trail a2 
           where a1.claim_id = a2.claim_id and a1.update_date < a2.update_date 
             and a1.new_status = a2.original_status and a1.reverted=false and a2.reverted=false
             and not exists (select * from audit_trail a3 where a3.claim_id = a2.claim_id 
                                and a3.update_date > a1.update_date and a3.update_date < a2.update_date 
                                and a1.new_status = a3.original_status and a3.reverted=false) 
             and a1.claim_id = c.id 
             and a1.new_status= 'ContestedInvoiceReferredToCHO') 
       end 
    + 
       case when (c.status!='ContestedInvoiceReferredToCHO') 
       then 0 
       else 
          (select cast(sum(EXTRACT(EPOCH FROM (now() - c2.status_modified_date))/(3600*24)) as numeric(10,3)) 
           from claim c2 
           where c.id=c2.id and c2.status='ContestedInvoiceReferredToCHO') 
       end ) as "Total time in Contested to CHO",
       (select case when (not exists (select * from audit_trail a where a.claim_id=c.id 
                                and a.original_status='ContestedInvoiceReferredToInsurer')) 
       then 0.00 
       else 
          (select cast(sum(EXTRACT(EPOCH FROM (a2.update_date - a1.update_date))/(3600*24)) as numeric(10,3)) 
           from audit_trail a1, audit_trail a2 
           where a1.claim_id = a2.claim_id and a1.update_date < a2.update_date 
             and a1.new_status = a2.original_status and a1.reverted=false and a2.reverted=false
             and not exists (select * from audit_trail a3 where a3.claim_id = a2.claim_id 
                                and a3.update_date > a1.update_date and a3.update_date < a2.update_date 
                                and a1.new_status = a3.original_status and a3.reverted=false) 
             and a1.claim_id = c.id 
             and a1.new_status= 'ContestedInvoiceReferredToInsurer') 
       end 
    + 
       case when (c.status!='ContestedInvoiceReferredToInsurer') 
       then 0 
       else 
          (select cast(sum(EXTRACT(EPOCH FROM (now() - c2.status_modified_date))/(3600*24)) as numeric(10,3)) 
           from claim c2 
           where c.id=c2.id and c2.status='ContestedInvoiceReferredToInsurer') 
       end ) as "Total time in Contested to Insurer",
       c.status as "Current Status"   
from claim c, insurer ins, invoice i left outer join reason_of_rejection ror on (i.reason_of_rejection_id = ror.id)
where c.invoice_id = i.id
  and c.insurer_id = ins.id
  and (c.chorganisation_id = choId or choId = -1)
  and (c.insurer_id = insId or insId = -1)
  and c.status in ('ContestedInvoiceReferredToCHO', 'ContestedInvoiceReferredToInsurer');

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION contested_count_report(int, int) TO chox_user;
GRANT EXECUTE ON FUNCTION contested_count_report(int, int) TO chox_mi;
