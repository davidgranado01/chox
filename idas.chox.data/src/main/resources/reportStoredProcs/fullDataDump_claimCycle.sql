DROP FUNCTION fullDataDump_claimCycle(
    IN insIds INTEGER[],
    IN claimTypes INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR);

CREATE OR REPLACE FUNCTION fullDataDump_claimCycle(
    IN insIds INTEGER[],
    IN claimTypes INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR)

RETURNS TABLE( 
    "Supplier Reference" VARCHAR,
    "Date" timestamp without time zone,
    "Modified By" text,
    "New Status" VARCHAR,
    "Reverted?" boolean
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
       a.created_date,
       case when ins.name is not null then wu.first_name || ' ' || wu.last_name || ' (' || ins.name || ')'
          else case when cho.name is not null then wu.first_name || ' ' || wu.last_name || ' (' || cho.name || ')'
            else wu.first_name || ' ' || wu.last_name end end as modifiedby,
       a.new_status,
       a.reverted
from claim c
join audit_trail a on (c.id = a.claim_id)
left outer join web_user wu on (a.created_by = wu.id)
left outer join insurer ins on (wu.insurer_id = ins.id)
left outer join chorganisation cho on (wu.chorganisation_id = cho.id)
where c.insurer_id = ANY(insIds)
  and (claimTypes is null or c.claim_type = ANY(claimTypes))
  and c.created_date >= DATE_FROM and c.created_date < DATE_TO
order by c.created_date, c.cho_reference, a.created_date;


END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



GRANT EXECUTE ON FUNCTION fullDataDump_claimCycle(
					IN insIds INTEGER[],
					IN claimTypes INTEGER[],
					IN startPeriod VARCHAR,
					IN endPeriod VARCHAR)
TO chox_user;

GRANT EXECUTE ON FUNCTION fullDataDump_claimCycle(
					IN insIds INTEGER[],
					IN claimTypes INTEGER[],
					IN startPeriod VARCHAR,
					IN endPeriod VARCHAR)
TO chox_mi;


/*
select * from fullDataDump_claimCycle(array[3,19], array[0,1,2], '2008-01-01','2013-10-01');
*/
