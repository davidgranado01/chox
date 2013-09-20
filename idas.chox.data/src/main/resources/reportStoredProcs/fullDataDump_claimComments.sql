DROP FUNCTION fullDataDump_claimComments(
    IN insIds INTEGER[],
    IN claimTypes INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR);

CREATE OR REPLACE FUNCTION fullDataDump_claimComments(
    IN insIds INTEGER[],
    IN claimTypes INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR)

RETURNS TABLE( 
    "Supplier Reference" VARCHAR,
    "Note Created Date" timestamp without time zone,
    "Created By" text,
    "Comment" VARCHAR
) AS

$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $3::DATE;
DATE_TO = $4::DATE;

RETURN QUERY

select  c.cho_reference,
	n.created_date,
	 case when ins.name is not null then wu.last_name || ', ' || wu.first_name || ' (' || ins.name || ')'
                  else case when cho.name is not null then wu.last_name || ', ' || wu.first_name || ' (' || cho.name || ')'
            else wu.last_name || ', ' || wu.first_name end end as createdby,
	n.comment as comment
from claim c
  join comment n on (c.id = n.claim_id)
  left outer join web_user wu on (n.created_by = wu.id)
  left outer join insurer ins on (wu.insurer_id = ins.id)
  left outer join chorganisation cho on (wu.chorganisation_id = cho.id)
where n.reverted = false and n.visibility_type in (0,1)
  and c.insurer_id = ANY(insIds)
  and c.claim_type = ANY(claimTypes)
  and c.created_date >= DATE_FROM and c.created_date < DATE_TO
order by c.created_date, c.cho_reference, n.created_date;


END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



GRANT EXECUTE ON FUNCTION fullDataDump_claimComments(
					IN insIds INTEGER[],
					IN claimTypes INTEGER[],
					IN startPeriod VARCHAR,
					IN endPeriod VARCHAR)
TO chox_user;

GRANT EXECUTE ON FUNCTION fullDataDump_claimComments(
					IN insIds INTEGER[],
					IN claimTypes INTEGER[],
					IN startPeriod VARCHAR,
					IN endPeriod VARCHAR)
TO chox_mi;


/*
select * from fullDataDump_claimComments(array[3,19], array[0,1,2], '2008-01-01','2013-10-01');
*/
