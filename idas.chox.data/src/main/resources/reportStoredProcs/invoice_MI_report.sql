
drop function invoice_MI_report(text, text, text, text);

create or replace function invoice_MI_report
(
    IN insid text ,IN choid text, IN startdate text, IN enddate text
)
returns table
(
   "Supplier Reference" character varying(128),
   "insurer name" character varying(128),
   "cho name" character varying(128),
   "Claim Status" character varying(128),
   "Invoice Created Date" timestamp,
   "Time Since Upload" numeric(6,2),
   "Time With CHO" numeric(6,2),
   "Time With Insurer" numeric(6,2),
   "Time ALR" numeric(6,2),
   "Date Moved To Logged" timestamp,
   "TimeToLogged" numeric(6,2),
   "Date Moved To Payment Received" timestamp,
   "Time To Payment Received" numeric(6,2)
)
as $$ DECLARE
cho_id INT[] = choid::INT[];
ins_id INT[] = insid::INT[]; 
BEGIN 
RETURN QUERY

 select 
  c.cho_reference,
  ins.name,
  cho.name, 
  c.status, 
  i.created_date,
  (select cast(EXTRACT(EPOCH FROM (now() - i.created_date))/(3600*24) as numeric(6,2))) as TimeSinceUpload,
  
  case when (not exists (select * from audit_trail a where a.claim_id=c.id and a.original_status in ('ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect'))) then 0 else
          (select cast(sum(EXTRACT(EPOCH FROM (a2.update_date - a1.update_date))/(3600*24)) as numeric(6,2))
           from audit_trail a1, audit_trail a2
           where a1.claim_id = a2.claim_id and a1.update_date < a2.update_date
                 and a1.new_status = a2.original_status
                 and not exists (select * from audit_trail a3 where a3.claim_id = a2.claim_id  and a3.update_date > a1.update_date and a3.update_date < a2.update_date and a1.new_status = a3.original_status) and a1.claim_id = c.id  and a1.new_status in ('ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect'))
   end
    +
   case when (c.status not in ('ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect'))
       then 0 else (select cast(sum(EXTRACT(EPOCH FROM (now() - c2.status_modified_date))/(3600*24)) as numeric(6,2))
           from claim c2 where c.id=c2.id and c2.status in ('ContestedInvoiceReferredToCHO','InvoiceDataCalculationIncorrect'))
   end as TimeWithCHO,

       
   case when (not exists (select * from audit_trail a where a.claim_id=c.id and a.original_status in ('InvoiceApprovedByBRE','AwaitingInvoicePayment','ContestedInvoiceReferredToInsurer','InvoiceReferredToClaimsHandler','InvoiceEscalated','InvoiceEscalatedToHandler','InvoiceReferredToEngineer'))) then 0 else
          (select cast(sum(EXTRACT(EPOCH FROM (a2.update_date - a1.update_date))/(3600*24)) as numeric(6,2))
           from audit_trail a1, audit_trail a2
           where a1.claim_id = a2.claim_id and a1.update_date < a2.update_date
             and a1.new_status = a2.original_status
             and not exists (select * from audit_trail a3 where a3.claim_id = a2.claim_id and a3.update_date > a1.update_date and a3.update_date < a2.update_date and a1.new_status = a3.original_status) and a1.claim_id = c.id and a1.new_status in ('InvoiceApprovedByBRE','AwaitingInvoicePayment','ContestedInvoiceReferredToInsurer',
                                   'InvoiceReferredToClaimsHandler','InvoiceEscalated','InvoiceEscalatedToHandler','InvoiceReferredToEngineer')) end
    +
   case when (c.status not in ('InvoiceApprovedByBRE','AwaitingInvoicePayment','ContestedInvoiceReferredToInsurer',
                                   'InvoiceReferredToClaimsHandler','InvoiceEscalated','InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer')) then 0 else
          (select cast(sum(EXTRACT(EPOCH FROM (now() - c2.status_modified_date))/(3600*24)) as numeric(6,2))
           from claim c2 where c.id=c2.id and c2.status in ('InvoiceApprovedByBRE','AwaitingInvoicePayment','ContestedInvoiceReferredToInsurer',
                                              'InvoiceReferredToClaimsHandler','InvoiceEscalated','InvoiceEscalatedToHandler','InvoiceReferredToEngineer')) 
                                              end as TimeWithInsurer,
                                              
   case when (not exists (select * from audit_trail a where a.claim_id=c.id
                                 and a.original_status in ('AwaitingLiabilityResolution'))) then 0 else
          (select cast(sum(EXTRACT(EPOCH FROM (a2.update_date - a1.update_date))/(3600*24)) as numeric(6,2))
           from audit_trail a1, audit_trail a2
           where a1.claim_id = a2.claim_id and a1.update_date < a2.update_date
             and a1.new_status = a2.original_status
             and not exists (select * from audit_trail a3 where a3.claim_id = a2.claim_id and a3.update_date > a1.update_date and a3.update_date < a2.update_date and a1.new_status = a3.original_status) and a1.claim_id = c.id and a1.new_status='AwaitingLiabilityResolution')
   end
    +
   case when (c.status not in ('AwaitingLiabilityResolution'))
       then 0 else
          (select cast(sum(EXTRACT(EPOCH FROM (now() - c2.status_modified_date))/(3600*24)) as numeric(6,2))
           from claim c2 where c.id=c2.id and c2.status ='AwaitingLiabilityResolution')
   end as TimeALR,
   
   (select a.update_date from audit_trail a where a.claim_id=c.id and a.new_status = 'InvoicePaymentLogged' and a.reverted = false and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status = 'InvoicePaymentLogged' and a2.update_date > a.update_date and a2.reverted = false)) as DateMovedToLogged,
               
   (select cast(EXTRACT(EPOCH FROM (a.update_date - i.created_date))/(3600*24) as numeric(6,2)) from audit_trail a where a.claim_id=c.id and a.new_status='InvoicePaymentLogged' and a.reverted = false
          and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status= 'InvoicePaymentLogged'
                             and a2.update_date > a.update_date and a2.reverted = false)) as TimeToLogged,
                             
   (select a.update_date from audit_trail a where a.claim_id=c.id and a.new_status = 'PaymentReceived' and a.reverted = false 
       and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status = 'PaymentReceived' and a2.update_date > a.update_date and a2.reverted = false)) as DateMovedToPaymentReceived,    
      
   (select cast(EXTRACT(EPOCH FROM (a.update_date - i.created_date))/(3600*24) as numeric(6,2)) from audit_trail a where a.claim_id=c.id and a.new_status='PaymentReceived' and a.reverted = false
          and not exists (select * from audit_trail a2 where a2.claim_id=a.claim_id and a2.new_status= 'PaymentReceived'
                             and a2.update_date > a.update_date and a2.reverted = false)) as TimeToPaymentReceived 
                             
from claim c, invoice i, insurer ins, chorganisation cho
where c.invoice_id = i.id
  and ins.id = c.insurer_id
  and cho.id = c.chorganisation_id
  and i.created_date between startdate::date and enddate::date
  and ((-1 = ANY (cho_id)) OR (c.chorganisation_id = ANY (cho_id)))
  and ((-1 = ANY (ins_id)) OR (c.insurer_id = ANY (ins_id)))
  order by ins.id, cho.id, i.created_date, c.status;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION invoice_MI_report(text, text, text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION invoice_MI_report(text, text, text, text) TO chox_mi;