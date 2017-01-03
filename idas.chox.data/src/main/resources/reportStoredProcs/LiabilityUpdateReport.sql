create or replace function liability_update_report(
    IN choId integer,
    IN insurerIds integer[],
    IN startPeriod text,
    IN endPeriod text)
returns table
(
   "Name of Insurer" character varying(128),
   "Supplier Reference" character varying(128),
   "Previous Liability Status" text,
   "Current Liability Status" text,
   "Liability % Agreed (Insurer)" numeric(4,1),
   "Liability % Agreed (CHO)" numeric(4,1),
   "Date of Latest Invoice Upload on Claim" date,
   "Supporting Liability Notes (Public)" character varying
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select ins.name, c.cho_reference, '', getLiabilityStatus(c.liability_status),
       c.percentage_liability_accepted::numeric(4,1), c.percentage_liability_cho::numeric(4,1),
       (select max(i.created_date)::date from invoice i, claim c2 where c2.customer_id=c.customer_id and c2.invoice_id=i.id),
       (select co2.comment from comment co2 where co2.claim_id=c.id
           and co2.created_date between (co.created_date - interval '5 seconds') and (co.created_date + interval '5 seconds')
           and co2.comment like 'Supporting Liability Note%')
from claim c, chorganisation cho, insurer ins, comment co
where c.insurer_id=ins.id and c.chorganisation_id=cho.id and c.id=co.claim_id
  and (co.comment like 'Liability status changed to%' or co.comment like 'Liability status changed from ''''%')
  and (co.comment like '%Full Liability Accepted%' or co.comment like '%Liability Split%'
            or co.comment like '%Proceed Without Prejudice%' or co.comment like '%Liability Repudiated%')
  and co.created_date::date between startDate and endDate
  and (c.chorganisation_id = choId)
  and (insurerIds is null or c.insurer_id = ANY(insurerIds))

UNION

select ins.name, c.cho_reference, 'Liability Unknown', getLiabilityStatus(c.liability_status),
       c.percentage_liability_accepted::numeric(4,1), c.percentage_liability_cho::numeric(4,1),
       (select max(i.created_date)::date from invoice i, claim c2 where c2.customer_id=c.customer_id and c2.invoice_id=i.id),
       (select co2.comment from comment co2 where co2.claim_id=c.id
           and co2.created_date between (co.created_date - interval '5 seconds') and (co.created_date + interval '5 seconds')
           and co2.comment like 'Supporting Liability Notes:%')
from claim c, chorganisation cho, insurer ins, comment co
where c.insurer_id=ins.id and c.chorganisation_id=cho.id and c.id=co.claim_id
  and co.comment like 'Liability status changed from ''Liability Unknown''%'
  and (co.comment like '%Full Liability Accepted%' or co.comment like '%Liability Split%'
            or co.comment like '%Proceed Without Prejudice%' or co.comment like '%Liability Repudiated%')
  and co.created_date::date between startDate and endDate
  and (c.chorganisation_id = choId)
  and (insurerIds is null or c.insurer_id = ANY(insurerIds))

UNION

select ins.name, c.cho_reference, 'Liability In Negotiation', getLiabilityStatus(c.liability_status),
       c.percentage_liability_accepted::numeric(4,1), c.percentage_liability_cho::numeric(4,1),
       (select max(i.created_date)::date from invoice i, claim c2 where c2.customer_id=c.customer_id and c2.invoice_id=i.id),
       (select co2.comment from comment co2 where co2.claim_id=c.id
           and co2.created_date between (co.created_date - interval '5 seconds') and (co.created_date + interval '5 seconds')
           and co2.comment like 'Supporting Liability Notes:%')
from claim c, chorganisation cho, insurer ins, comment co
where c.insurer_id=ins.id and c.chorganisation_id=cho.id and c.id=co.claim_id
  and co.comment like 'Liability status changed from ''Liability In Negotiation''%'
  and (co.comment like '%Full Liability Accepted%' or co.comment like '%Liability Split%'
            or co.comment like '%Proceed Without Prejudice%' or co.comment like '%Liability Repudiated%')
  and co.created_date::date between startDate and endDate
  and (c.chorganisation_id = choId)
  and (insurerIds is null or c.insurer_id = ANY(insurerIds))
;

END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION liability_update_report(integer, integer[], text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION liability_update_report(integer, integer[], text, text) TO chox_mi;
-- select * from liability_update_report(1015, array[6], '2016-12-02', '2016-12-02');