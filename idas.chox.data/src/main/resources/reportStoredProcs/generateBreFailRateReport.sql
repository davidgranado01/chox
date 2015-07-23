-- Create
-- 1.   function to return rule id of top failed rule:
--      arguments will be
--            start date
--            end date
--            choId
--            insId
--            claimType
--            no: number of rule, e.g. 1 would be top rule failure, 2 2nd, etc
--
-- 2.   function to return %age failure for a rule
--      arguments will be
--            start date
--            end date
--            choId
--            insId
--            claimType
--            ruleId
--
-- 3.  function to return report
--


-- 1. Function to return rule id of top failed rule
drop function getRule(startDateAsText DATE, endDateAsText DATE, chorgId INT, insId INT, claimType INTEGER[], ruleNo INT);
create or replace function getRule(
   startDate DATE, endDate DATE, chorgId INT, insId INT, claimType INTEGER[], ruleNo INT
)
returns CHARACTER VARYING(3) as
$BODY$
DECLARE
   result CHARACTER VARYING(3);
BEGIN

select into result t.rule_id from (select rule_id, count(*) as ruleFailurers from invoice i, chorganisation cho, claim c
                                                join (select distinct h2.claim_id, h2.rule_id from history h2, claim c2, invoice i2
                                                  where c2.id=h2.claim_id and c2.invoice_id=i2.id and (c2.chorganisation_id = chorgId or chorgId = -1) and (c2.insurer_id = insId or insId = -1) and (case when array_length(claimType, 1) > 0 then c2.claim_type = ANY(claimType) else true end) and i2.created_date >= startDate and i2.created_date < endDate and h2.type='ERROR') as h on c.id = h.claim_id
                                              where c.invoice_id = i.id and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
                                                and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
                                                and (c.insurer_id = insId or insId = -1)
                                                and (c.chorganisation_id = chorgId or chorgId = -1)
                                                and i.created_date >= startDate and i.created_date < endDate
                                              group by rule_id order by ruleFailurers desc limit 1 offset ruleNo-1) as t;


RETURN result;

END;
$BODY$
   LANGUAGE plpgsql;

--
-- to use:
--        select getRule('2014-01-01'::Date, '2015-01-01'::Date, 1007, 6, null, 1);
--


-- 2. Function to return %age failure for a rule
drop function getFailureRate(startDateAsText TEXT, endDateAsText TEXT, chorgId INT, insId INT, claimType INTEGER[], ruleId CHARACTER VARYING(3));
create or replace function getFailureRate(
   startDate DATE, endDate DATE, chorgId INT, insId INT, claimType INTEGER[], ruleId CHARACTER VARYING(3)
)
returns NUMERIC(5,1) as
$BODY$
DECLARE
   result NUMERIC(5,1);
BEGIN

select into result count(*)*100.0/(select case when count(*)=0 then 1 else count(*) end from claim c, invoice i, chorganisation cho
                                   where c.invoice_id=i.id
                                     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
                                     and (c.insurer_id = insId or insId = -1)
                                     and c.chorganisation_id = cho.id and cho.insurer_upload_only = false
                                     and (chorgId = -1 or c.chorganisation_id = chorgId)
                                     and i.created_date >= startDate and i.created_date < endDate)
from (select distinct c.id from claim c, history h, invoice i, chorganisation cho
      where c.invoice_id = i.id and h.claim_id = c.id
        and h.rule_id = ruleId and h.type='ERROR'
        and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
        and (c.insurer_id = insId or insId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only = false
        and (chorgId = -1 or c.chorganisation_id = chorgId)
        and i.created_date >= startDate and i.created_date < endDate) t
;

RETURN result;

END;
$BODY$
   LANGUAGE plpgsql;

--
-- to use:
--        select getFailureRate('2014-01-01'::date, '2015-01-01'::date, 1007, 6, null, '056');
--

-- 3. Function to return report
create or replace function generateBreFailRateReport(
    dateAsText TEXT, chorgId INT, insId INT, claimType INTEGER[]
)
returns table
(
   rowName TEXT,
   last_12_months TEXT,
   current_month TEXT,
   previous_month TEXT,
   previous_2_month TEXT,
   previous_3_month TEXT,
   previous_4_month TEXT,
   previous_5_month TEXT,
   previous_6_month TEXT,
   previous_7_month TEXT,
   previous_8_month TEXT,
   previous_9_month TEXT,
   previous_10_month TEXT,
   previous_11_month TEXT
)
as $BODY$
DECLARE
   startDate DATE;
   periodStart DATE;
   periodEnd DATE;
   ruleId CHARACTER VARYING(3);
   failRate NUMERIC(5,1);
   endIncr INT;
BEGIN
   startDate = dateAsText::DATE;

   CREATE TEMP TABLE resultsTable (id INT, rowTitle TEXT, all_12_months TEXT, month_1 TEXT, month_2 TEXT, month_3 TEXT, month_4 TEXT, month_5 TEXT, month_6 TEXT, month_7 TEXT, month_8 TEXT, month_9 TEXT, month_10 TEXT, month_11 TEXT, month_12 TEXT);


-- Now loop over row
   FOR rowNo in 1..5 LOOP
   -- First insert last 12 months column only
   -- Determine period start and end date
   select into periodStart (to_date(to_char(startDate, 'MM') || '-01-' || to_char(startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months');
   select into periodEnd (to_date(to_char(startDate + interval '1 month', 'MM') || '-01-' || to_char(startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'));

   -- Get BRE rule
   select into ruleId getRule(periodStart, periodEnd, chorgId, insId, claimType, rowNo);

   -- Calculate  % faile rate for 12 month period
   select into failRate getFailureRate(periodStart, periodEnd, chorgId, insId, claimType, ruleId);

--RAISE NOTICE 'inserting row with rownNo=%, ruleId=%, failRate=%', rowNo, ruleId, failRate;

   -- insert row
   insert into resultsTable(id, all_12_months) values (rowNo, 'Rule ' || coalesce(ruleId,'---') || ':' || failRate || '%');

   select into endIncr 1;
   -- Now loop of months
    FOR month in 1..12 LOOP
    -- Determine period start and end date
      select into periodStart to_date(to_char(startDate, 'MM') || '-01-' || to_char(startDate, 'yyyy'), 'mm-dd-yyyy') - ((month-1) || ' month')::interval;
      select into periodEnd to_date(to_char(startDate + (endIncr || ' month')::interval, 'MM') || '-01-' || to_char(startDate + (endIncr || ' month')::interval, 'yyyy'), 'mm-dd-yyyy');

    -- Get BRE rule
      select into ruleId getRule(periodStart, periodEnd, chorgId, insId, claimType, rowNo);

    -- Calculate  % faile rate for 12 month period
      select into failRate getFailureRate(periodStart, periodEnd, chorgId, insId, claimType, ruleId);

--RAISE NOTICE 'updating row with rownNo=%, month=%, ruleId=%, failRate=%', rowNo, month, ruleId, failRate;
    -- update row
      execute 'update resultsTable set month_' || month || ' = ''' ||  'Rule ' || coalesce(ruleId,'---') || ':' ||  failRate  || '%'' where id=' || rowNo;

      endIncr := endIncr -1;
    END LOOP;
   END LOOP;

-- Add row titles
   update resultsTable set rowTitle = 'Top BRE rule failure' where id=1;
   update resultsTable set rowTitle = '2nd BRE rule failure' where id=2;
   update resultsTable set rowTitle = '3rd BRE rule failure' where id=3;
   update resultsTable set rowTitle = '4th BRE rule failure' where id=4;
   update resultsTable set rowTitle = '5th BRE rule failure' where id=5;

RETURN QUERY
   select rowTitle, all_12_months, month_1, month_2, month_3, month_4, month_5, month_6, month_7, month_8, month_9, month_10, month_11, month_12 from resultsTable order by id;

   DROP TABLE resultsTable;
END;
$BODY$
   LANGUAGE plpgsql;

--
-- select * from generateBreFailRateReport('2015-07-03', 1007, 6, null);
--