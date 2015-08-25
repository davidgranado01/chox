drop function remaining_sla_days(IN insurerids integer[]);

create or replace function remaining_sla_days(IN insurerids integer[])
returns table
(
   "Supplier Reference" character varying(128),
   "Insurer Claim Number" character varying(128),
   "Current Status" character varying(128),
   "Workgroup" character varying(128),
   "Insurer Claim Owner" text,
   "CHO Name" character varying(128),
   "Claim Type" text,
   "Insurer Name" character varying(128),
   "SLA Days Remaining" int,
   "SLA Cut-Off Time" character varying(5)
)
as $BODY$

BEGIN 

RETURN QUERY

SELECT c.cho_reference as "Supplier Reference",
       c.claim_number as "Insurer Claim Number",
       c.status as "Current Status",
       w.name as "Workgroup",
       wu.first_name || ' ' || wu.last_name AS "Insurer Claim Owner",
       ch.name as "CHO Name",
       (CASE WHEN c.claim_type IN (7,8,9) THEN 'Subscriber'
             ELSE 'Fixed Fee' END) as "Claim Type",
       ins.name as "Insurer Name",
       (CASE WHEN c.claim_type IN (7,8,9) THEN bre.subscriber_sla_days - get_days_in_status(c.id, '{"ClaimUnacknowledgedUnassigned","ClaimUnacknowledgedUnrouted","ClaimUnacknowledgedRouted",
                    "ClaimPending","ClaimReferredToFNOL","ClaimReferredToEngineer","ClaimUpdatedByEngineer","ClaimRejectionContested"}')
             ELSE bre.fixedfee_sla_days - get_days_in_status(c.id, '{"ClaimUnacknowledgedUnassigned","ClaimUnacknowledgedUnrouted","ClaimUnacknowledgedRouted",
                    "ClaimPending","ClaimReferredToFNOL","ClaimReferredToEngineer","ClaimUpdatedByEngineer","ClaimRejectionContested"}') END) as "SLA Days Remaining",
       (CASE WHEN c.claim_type IN (7,8,9) THEN bre.subscriber_time_cut_off
             ELSE bre.fixedfee_time_cut_off END) as "SLA Cut-Off Time"
FROM claim c LEFT OUTER JOIN workgroup w ON c.workgroup_id = w.id,
     web_user wu,
     chorganisation ch,
     insurer ins,
     bre_band bre,
     bre_band_organisation bbo
WHERE (insurerids is null or ins.id = ANY(insurerids)) -- restricted to insurers
  AND c.chorganisation_id = ch.id
  AND c.insurer_id = ins.id
  AND c.claim_owner_id = wu.id
  AND bbo.chorganisation_id = ch.id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = ins.id
  AND c.status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted', 'ClaimPending', 'ClaimReferredToEngineer',
                    'ClaimUpdatedByEngineer', 'ClaimReferredToFNOL', 'SubscriberClaimRejected', 'ClaimRejected',
                    'ClaimRejectionContested', 'ClaimUnacknowledgedUnassigned')
  AND c.claim_type in (7,8,9,11,12,13)
  AND NOT EXISTS (select * from comment co where co.claim_id=c.id and co.comment ilike '%failed to respond to the % notification within the % day SLA%' and co.reverted = false)
ORDER BY "SLA Days Remaining"
;

END;
$BODY$
LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION remaining_sla_days(IN insurerids integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION remaining_sla_days(IN insurerids integer[]) TO chox_mi;

-- select "Supplier Reference", "Insurer Claim Number", "Current Status", "Workgroup", "Insurer Claim Owner", "CHO Name", "Claim Type", "Insurer Name",
--        case when "SLA Days Remaining" = 0 then "SLA Cut-Off Time" else "SLA Days Remaining"::character varying(5) end as "SLA Days Remaining"
-- from remaining_sla_days(array[3])
-- where "SLA Days Remaining" >= 0;
