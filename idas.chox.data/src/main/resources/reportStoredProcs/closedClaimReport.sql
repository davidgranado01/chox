-- e.g.
--     select * from closed_claim_report(array[26], null::integer[], '2016-08-22','2016-08-29');
-- drop function closed_claim_report(integer[], integer[], text, text);
create or replace function closed_claim_report
(
   IN insurerIds integer[], IN choIds integer[], IN startPeriod text, IN endPeriod text
)
returns table
(
   "Supplier Reference" character varying(128),
   "Status" character varying(128),
   "Status Modified Date" timestamp without time zone
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select cho_reference, status, status_modified_date
from claim
where (insurerIds is null or insurer_id = ANY(insurerIds))
  and (choIds is null or chorganisation_id = ANY(choIds))
  and status in ('ClaimClosed','ClaimRejectionAccepted')
  and status_modified_date > startDate and status_modified_date < endDate
order by status_modified_date;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION closed_claim_report(integer[], integer[], text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION closed_claim_report(integer[], integer[], text, text) TO chox_mi;
