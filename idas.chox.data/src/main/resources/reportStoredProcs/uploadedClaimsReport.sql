drop function uploaded_claims_report(varchar, varchar, integer);
create or replace function uploaded_claims_report (
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR,
    IN choId INTEGER)
returns table
(
    "Supplier Reference" varchar(128),
    "Claim Type" text,
    "Current Claim Status" varchar(128),
    "Insurer Name" varchar(128),
    "Upload Date" timestamp without time zone
)
AS

$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = startPeriod::DATE;
DATE_TO = endPeriod::DATE;

RETURN QUERY

select c.cho_reference as "Supplier Reference", getClaimType(c.claim_type) as "Claim Type",
       c.status as "Current Claim Status", i.name as "Insurer Name", c.created_date as "Upload Date"
from claim c, insurer i
where c.insurer_id = i.id and c.chorganisation_id = choId
  and c.created_date >= DATE_FROM and c.created_date < DATE_TO
order by "Upload Date";

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

/* select * from uploaded_claims_report('2009-01-01', '2013-06-01', 1009); */
/* select * from uploaded_claims_report('2015-01-27', '2015-01-28', 1007); */
/* select * from uploaded_claims_report('2014-01-01', '2015-01-01', 1007); */
