-- Function: bre_failure_report(text, text, integer, integer)

-- DROP FUNCTION bre_failure_report(text, text, integer, integer);

CREATE OR REPLACE FUNCTION bre_failure_report(IN startdate text, IN enddate text, IN insurerid integer, IN choid integer)
  RETURNS TABLE(ruleid character varying, rulename character varying, numberoffailures bigint, percentage numeric, numberofuniquecases bigint, averageinvoicedvalue numeric, averagessettledvalue numeric) AS
$BODY$
DECLARE
BEGIN 

RETURN QUERY 
select h.rule_id as RuleId, n.name,
count(*) as NumberOfFailures, (100.0*count(*)/(select count(*)
      from claim, invoice, history
      where  claim.invoice_id = invoice.id
             and claim.id = history.claim_id
             and  invoice.created_date between startDate::date and endDate::date -- invoice created date
             and claim.insurer_id in (insurerId) -- insurer ids
and (claim.chorganisation_id = choId or choId = -1 )-- chorganisation id
and history.claim_id = claim.id  
             and history.type = 'ERROR'))::numeric(6,3) as Percentage,

      count(distinct(c.id)) as NumberOfUniqueCases,

      (select avg(coalesce(invoice_original.total_to_pay, invoice.original_total_to_pay, invoice.total_to_pay ))::numeric(8,2) 
       from invoice_original, claim
       inner join invoice on claim.invoice_id = invoice.id
       where invoice.invoice_original_id = invoice_original.id and claim.id in (select distinct(c.id)
from history hi, invoice i, claim c 
where c.invoice_id = i.id
and c.id = hi.claim_id
and  i.created_date between startDate::date and endDate::date -- invoice created date
and c.insurer_id in (insurerId) -- insurer ids
and (c.chorganisation_id = choId or choId = -1 ) -- chorganisation id   
and hi.type = 'ERROR' 
and hi.rule_id = h.rule_id)) as AveragesSettledValue,

     (select avg(inv.total_to_pay)::numeric(8,2) 
     from invoice inv 
     where inv.id in (select distinct(i.id) 
from history hi, invoice i, claim c 
where c.invoice_id = i.id
and c.id = hi.claim_id
and i.created_date between startDate::date and endDate::date -- invoice created date
and c.insurer_id in (insurerId) -- insurer ids
and (c.chorganisation_id = choId or choId = -1 )-- chorganisation id   
and hi.type = 'ERROR' 
and hi.rule_id = h.rule_id)) as AverageInvoicedValue

from bre_rules n, history h, invoice i, claim c 
where n.rule_id = h.rule_id
   and c.invoice_id = i.id
   and c.id = h.claim_id
   and  i.created_date between startDate::date and endDate::date -- invoice created date
   and c.insurer_id in (insurerId) -- insurer ids
   and (c.chorganisation_id = choId or choId = -1 ) -- chorganisation id   
   and h.type = 'ERROR' 
group by h.rule_id, n.name order by h.rule_id;END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;

GRANT EXECUTE ON FUNCTION bre_failure_report(text, text, integer, integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION bre_failure_report(text, text, integer, integer[]) TO chox_mi;

--
-- Second version that accepts multiple CHOs instead of on one (or all)
--

-- Function: bre_failure_report2(text, text, integer, integer[])
-- select * from bre_failure_report2( '2014-11-01', '2015-05-01', 6, array[1016,1015,1011,1006,1008,1025,1021,1132,1125,1124,1123]);

-- DROP FUNCTION bre_failure_report2(text, text, integer, integer[]);

CREATE OR REPLACE FUNCTION bre_failure_report2(IN startdate text, IN enddate text, IN insurerid integer, IN choids integer[])
  RETURNS TABLE(ruleid character varying, rulename character varying, numberoffailures bigint, percentage numeric, numberofuniquecases bigint, averageinvoicedvalue numeric, averagessettledvalue numeric) AS
$BODY$
DECLARE
BEGIN 

RETURN QUERY 
select h.rule_id as RuleId, n.name,
count(*) as NumberOfFailures, (100.0*count(*)/(select count(*)
      from claim, invoice, history
      where  claim.invoice_id = invoice.id
             and claim.id = history.claim_id
             and  invoice.created_date between startDate::date and endDate::date -- invoice created date
             and claim.insurer_id in (insurerId) -- insurer ids
             and (case when array_length(choIds, 1) > 0 then claim.chorganisation_id = ANY(choIds) else true end) -- chorganisation id
             and history.claim_id = claim.id  
             and history.type = 'ERROR'))::numeric(6,3) as Percentage,

      count(distinct(c.id)) as NumberOfUniqueCases,

      (select avg(coalesce(invoice_original.total_to_pay, invoice.total_to_pay ))::numeric(8,2) 
       from invoice_original, claim
       inner join invoice on claim.invoice_id = invoice.id
       where invoice.invoice_original_id = invoice_original.id and claim.id in (select distinct(c.id)
from history hi, invoice i, claim c 
where c.invoice_id = i.id
and c.id = hi.claim_id
and  i.created_date between startDate::date and endDate::date -- invoice created date
and c.insurer_id in (insurerId) -- insurer ids
and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end) -- chorganisation id
and hi.type = 'ERROR' 
and hi.rule_id = h.rule_id)) as AveragesSettledValue,

     (select avg(inv.total_to_pay)::numeric(8,2) 
     from invoice inv 
     where inv.id in (select distinct(i.id) 
from history hi, invoice i, claim c 
where c.invoice_id = i.id
and c.id = hi.claim_id
and i.created_date between startDate::date and endDate::date -- invoice created date
and c.insurer_id in (insurerId) -- insurer ids
and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end) -- chorganisation id
and hi.type = 'ERROR' 
and hi.rule_id = h.rule_id)) as AverageInvoicedValue

from bre_rules n, history h, invoice i, claim c 
where n.rule_id = h.rule_id
   and c.invoice_id = i.id
   and c.id = h.claim_id
   and  i.created_date between startDate::date and endDate::date -- invoice created date
   and c.insurer_id in (insurerId) -- insurer ids
   and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end) -- chorganisation id
   and h.type = 'ERROR' 
group by h.rule_id, n.name order by h.rule_id;END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;

GRANT EXECUTE ON FUNCTION bre_failure_report2(text, text, integer, integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION bre_failure_report2(text, text, integer, integer[]) TO chox_mi;
