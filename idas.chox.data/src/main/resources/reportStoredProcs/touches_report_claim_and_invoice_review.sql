drop function touches_report_claim_and_invoice_review(text, text, text, text);

create or replace function touches_report_claim_and_invoice_review
(
    IN insid text ,IN choid text, IN startdate text, IN enddate text
)
returns table
(
   "Supplier Reference" character varying(128),
   "insurer name" character varying(128),
   "cho name" character varying(128),
   "Claim Cycle Time" bigint,
   "Invoice Cycle Time" bigint,
   "No. Claim Touch Points" bigint,
   "No. Invoice Touch Points" bigint
)
as $$ DECLARE
cho_id INT[] = choid::INT[];
ins_id INT[] = insid::INT[]; 
BEGIN 
RETURN QUERY

select
  
  c.cho_reference as "Supplier Reference",
 
  cho.name as "cho name",

  ins.name as "insurer name",
  
  (select cast(round(EXTRACT(DAY FROM (a.update_date - c.created_date))) as bigint))as "Claim Cycle Time",

  (select cast(round(EXTRACT(DAY FROM (a.update_date - i.created_date))) as bigint))as "Invoice Cycle Time",

  ((select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'ClaimUnacknowledgedRouted' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'ClaimRejectionContested' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'ClaimPending' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'ClaimUpdatedByEngineer' and a2.reverted = false )) as "No. Claim Touch Points",

  ((select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'InvoiceApprovedByBRE' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'InvoiceEscalated' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'InvoiceEscalatedToHandler' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'InvoiceReferredToClaimsHandler' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'InvoiceReferredToEngineer' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'InvoiceUnassigned' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'ContestedInvoiceReferredToInsurer' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'AwaitingInvoicePayment' and a2.reverted = false )
      +(select count(*) from audit_trail a2 where a2.claim_id = c.id and a2.new_status = 'AwaitingLiabilityResolution' and a2.reverted = false )) as "No. Invoice Touch Points"   
  
  from claim c , invoice i , audit_trail a, chorganisation cho, insurer ins where c.invoice_id = i.id 
    and a.claim_id = c.id
    and cho.id = c.chorganisation_id
    and ins.id = c.insurer_id
    and a.new_status = 'PaymentReceived'
    and a.reverted = false
    and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
    and not exists (select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed') 
    and i.created_date between startdate::date and enddate::date
    and ((-1 = ANY (cho_id)) OR (c.chorganisation_id = ANY (cho_id)))
    and ((-1 = ANY (ins_id)) OR (c.insurer_id = ANY (ins_id)))
    order by ins.id, cho.id; 

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION touches_report_claim_and_invoice_review(text, text, text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION touches_report_claim_and_invoice_review(text, text, text, text) TO chox_mi;