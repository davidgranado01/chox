DROP FUNCTION fullDataDump_breHistory(
    IN insIds INTEGER[],
    IN claimTypes INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR);

CREATE OR REPLACE FUNCTION fullDataDump_breHistory(
    IN insIds INTEGER[],
    IN claimTypes INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR)

RETURNS TABLE( 
    "Supplier Reference" VARCHAR,
    "Date Processed" timestamp without time zone,
    "Rule ID" VARCHAR,
    "Type" VARCHAR,
    "Narrative" VARCHAR
) AS

$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $3::DATE;
DATE_TO = $4::DATE;

RETURN QUERY

select c.cho_reference,
       h.process_date,
       h.rule_id,
       h.type,
       h.narrative
from claim c
join history h on (c.id = h.claim_id)
where h.type != 'INFO' and h.is_public
  and c.insurer_id = ANY(insIds)
  and c.claim_type = ANY(claimTypes)
  and c.created_date >= DATE_FROM and c.created_date < DATE_TO
order by c.created_date, c.cho_reference, h.process_date, h.rule_id;


END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



GRANT EXECUTE ON FUNCTION fullDataDump_breHistory(
					IN insIds INTEGER[],
					IN claimTypes INTEGER[],
					IN startPeriod VARCHAR,
					IN endPeriod VARCHAR)
TO chox_user;

GRANT EXECUTE ON FUNCTION fullDataDump_breHistory(
					IN insIds INTEGER[],
					IN claimTypes INTEGER[],
					IN startPeriod VARCHAR,
					IN endPeriod VARCHAR)
TO chox_mi;


/*
select * from fullDataDump_breHistory(array[3,19], array[0,1,2], '2008-01-01','2013-10-01');
*/