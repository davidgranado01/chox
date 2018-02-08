drop function switched_claim_report(IN insurerids integer[], IN startDate text, IN endDate text);
create or replace function switched_claim_report(IN insurerIds integer[], IN startDate text, IN endDate text)
returns table (
   "Supplier Reference" character varying,
   "CHO Name" character varying(128),
   "Claim Type" character varying,
   "Insurer Claim Number" character varying,
   "Claim Status" character varying,
   "New Insurer" character varying,
   "Date Switched" date
)
as $$ DECLARE
    startOfPeriod date;
    endOfPeriod date;
BEGIN
    startOfPeriod = startDate::Date;
    endOfPeriod = endDate::Date;
RETURN QUERY

select ea1.value as "Supplier Reference", cho.name as "CHO Name", ea3.value as "Claim Type", ea4.value as "Insurer Claim Number", el1.status as "Claim Status", ea2.value as "New Insurer", el1.created_date::Date as "Date Switched"
from claim c, event_log el1, event_log el2, chorganisation cho, event_attributes ea1, event_attributes ea2, event_attributes ea3, event_attributes ea4
where c.id=el1.claim_id and c.id=el2.claim_id
  and el1.activity_name in ('SwitchClaim','SwitchClaimToMultipleInsurer') and el1.event_name in ('ClaimClosedEvent', 'SwitchInsEvent')
  and el1.insurer_id = ANY(insurerIds) and el1.created_date >= startOfPeriod and el1.created_date < endOfPeriod
  and el1.chorganisation_id = cho.id
  and el2.activity_name = 'NewClaim' and el2.event_name='NewClaimEvent'
  and el2.created_date > el1.created_date - interval '6 seconds' and el2.created_date < el1.created_date + interval '6 seconds'
  and ea1.event_log_id = el2.id and ea1.key='choReference'
  and ea2.event_log_id = el2.id and ea2.key='insurerName'
  and ea3.event_log_id = el2.id and ea3.key='claimType'
  and ea4.event_log_id = el2.id and ea4.key='claimNumber'
order by "Date Switched";

END;
$$ LANGUAGE plpgsql;
GRANT EXECUTE ON FUNCTION switched_claim_report(integer[], text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION switched_claim_report(integer[], text, text) TO chox_mi;
--select * from switched_claim_report(array[26], '2017-12-01', '2018-01-01');

