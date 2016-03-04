drop function invoice_notifications_report(integer[], integer, text, text);
create or replace function invoice_notifications_report
(
   IN insurerIds integer[], IN choId integer, IN startPeriod text, IN endPeriod text
)
returns table
(
   "Supplier Reference" character varying(128),
   "Insurer Name" character varying(128),
   "Claim Status" text,
   "Policy Number" character varying(32),
   "Claim Number" character varying(64),
   "Invoice Upload Date" date,
   "Liability Status" text,
   "Total To Pay" numeric(10,2)
   
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select c.cho_reference, ins.name,
       case when c.status in ('ManualInvoiceBREApproved','ManualInvoiceBRERejected') then 'Awaiting Invoice Action' else
            case when c.status='ManualInvoiceContested' then 'Invoice Contested' else 'Awaiting Liability Resolution' end end,
       tp.policy_number, tp.claim_reference, i.created_date::date as upload_date, getLiabilityStatus(c.liability_status), i.total_to_pay
from claim c, insurer ins, chorganisation cho, third_party tp, invoice i
where c.insurer_id = ins.id and c.chorganisation_id = cho.id and c.third_party_id = tp.id and c.invoice_id = i.id
  and cho.insurer_upload_only = true
  and c.status in ('ManualInvoiceBREApproved','ManualInvoiceBRERejected','ManualInvoiceContested','AwaitingLiabilityResolution')
  and (choId = -1 or c.chorganisation_id = choId)
  and (insurerIds is null or c.insurer_id = ANY(insurerIds))

UNION ALL

select c.cho_reference, ins.name,
       case when c.status = 'ManualInvoicePaid' then 'Invoice Paid' else 'Claim Closed' end,
       tp.policy_number, tp.claim_reference, i.created_date::date as upload_date, getLiabilityStatus(c.liability_status), i.total_to_pay
from claim c, insurer ins, chorganisation cho, audit_trail at, third_party tp, invoice i
where c.insurer_id = ins.id and c.chorganisation_id = cho.id and c.id = at.claim_id and c.third_party_id = tp.id and c.invoice_id = i.id
  and at.new_status in ('ManualInvoicePaid','ClaimClosed') and at.reverted=false
  and at.created_date >= startDate::date and at.created_date < endDate::date
  and cho.insurer_upload_only = true
  and c.status in ('ManualInvoicePaid','ClaimClosed')
  and (choId = -1 or c.chorganisation_id = choId)
  and (insurerIds is null or c.insurer_id = ANY(insurerIds))

order by upload_date;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION invoice_notifications_report(integer[], integer, text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION invoice_notifications_report(integer[], integer, text, text) TO chox_mi;
-- e.g.
--     select * from invoice_notifications_report(array[6], 1125, '2016-03-02','2016-03-03');
