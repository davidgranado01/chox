drop function fnol_notifications_report(integer[], integer, text, text);
create or replace function fnol_notifications_report
(
   IN insurerIds integer[], IN choId integer, IN startPeriod text, IN endPeriod text
)
returns table
(
   "Supplier Reference" character varying(128),
   "Insurer Name" character varying(128),
   "Insurer Claim Handler" character varying(128),
   "Insurer Claim Handler Telephone Number" character varying(32),
   "Claim Status" text,
   "Liability Status" text,
   "Supporting Liability Note" text,
   "Claim Closure Reason" text,
   "Claim Closure Note" text,
   "Policy Number" character varying(32),
   "Claim Number" character varying(64)

)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY


select c.cho_reference as supplier_reference, ins.name, (wu.first_name || ' ' || wu.last_name)::character varying(128), wu.telephone,
        'Claim Accepted' as claim_status,  getLiabilityStatus(c.liability_status),
	substring(co.comment from 28) as liability_status_note, '' as claim_close_reason, '' as claim_close_note,
    tp.policy_number, tp.claim_reference
       
from  insurer ins, chorganisation cho, third_party tp, audit_trail at, claim c
 left outer join comment co on (c.id = co.claim_id   and co.comment like 'Supporting Liability Notes:%' and co.created_date between (c.liability_status_modified_date - interval '1 second') and (c.liability_status_modified_date + interval '1 second') )
 left outer join web_user wu on (c.claim_owner_id = wu.id)
 where c.insurer_id = ins.id and c.chorganisation_id = cho.id and c.id = at.claim_id and c.third_party_id = tp.id 
  and c.status <>'ClaimClosed'
  and at.new_status ='AwaitingCarHireInfo' and at.reverted=false
  and at.created_date >= startDate::date and at.created_date < endDate::date
  and cho.insurer_upload_only = true
  and (choId = -1 or c.chorganisation_id = choId)
  and (insurerIds is null  or c.insurer_id = ANY(insurerIds))


UNION ALL

select c.cho_reference as supplier_reference, ins.name, (wu.first_name || ' ' || wu.last_name)::character varying(128), wu.telephone,
        'Claim Rejected' as claim_status,  getLiabilityStatus(c.liability_status),
	substring(co.comment from 28) as liability_status_note,
    (select substring(co3.comment from 13) from comment co3 where co3.claim_id=c.id and co3.comment like 'Claim Closed:%' and co3.created_date between (c.status_modified_date - interval '1 second') and (c.status_modified_date + interval '1 second') ) as claim_close_reason,
    (select substring(co2.comment from 20) from comment co2 where co2.claim_id=c.id and co2.comment like 'Claim Closed Note:%' and co2.created_date between (c.status_modified_date - interval '1 second') and (c.status_modified_date + interval '1 second') ) as claim_close_note,
    tp.policy_number, tp.claim_reference
       
from insurer ins, chorganisation cho, third_party tp, audit_trail at, claim c
 left outer join comment co on (c.id = co.claim_id   and co.comment like 'Supporting Liability Notes:%' and co.created_date between (c.liability_status_modified_date - interval '1 second') and (c.liability_status_modified_date + interval '1 second') )
 left outer join web_user wu on (c.claim_owner_id = wu.id)

 where c.insurer_id = ins.id and c.chorganisation_id = cho.id and c.id = at.claim_id  and c.third_party_id = tp.id and c.invoice_id IS NULL
  and at.new_status ='ClaimClosed' and at.reverted=false
  and at.created_date >= startDate::date and at.created_date < endDate::date
  and cho.insurer_upload_only = true
  and (choId = -1 or c.chorganisation_id = choId)
  and (insurerIds is null  or c.insurer_id = ANY(insurerIds))

  order by supplier_reference;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION fnol_notifications_report(integer[], integer, text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION fnol_notifications_report(integer[], integer, text, text) TO chox_mi;
-- e.g.
--     select * from fnol_notifications_report(array[6], 1125, '2016-03-02','2016-03-03');
