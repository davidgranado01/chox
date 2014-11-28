DROP FUNCTION on_hire_report(IN insIDs INT[], IN choIDs INT[], IN workgroupIDs INT[]);

CREATE OR REPLACE FUNCTION on_hire_report(IN insIDs INT[], IN choIDs INT[], IN workgroupIDs INT[])
RETURNS TABLE (
   "Supplier Reference" character varying(128),
   "Insurer Claim Number" character varying(128),
   "Claim Type" text,
   "CHO Name" varchar(128),
   "Insurer Name" varchar(128),
   "Workgroup" varchar(128),
   "Insurer Claim Handler" text,
   "CHO Claim Handler" text,
   "Total Loss?" text,
   "Customer Vehicle Class" varchar(10),
   "Hire Start Date" timestamp without time zone,
   "Days Since Hire Start" integer
)
AS
$BODY$
DECLARE
BEGIN 

RETURN QUERY 

select c.cho_reference, c.claim_number, getClaimType(c.claim_type), cho.name, ins.name, w.name,
    wu1.first_name || ' ' || wu1.last_name as claims_handler,
    wu2.first_name || ' ' || wu2.last_name as cho_claims_handler,
    case when cu.is_total_loss then 'Yes' else 'No' end as total_loss,
    vc.name,
    case when vh.rental_start::date < '1900-01-01'::date then null else vh.rental_start end as rental_start,
    case when vh.rental_start::date < '1900-01-01'::date then null else extract(day from now() - vh.rental_start)::integer end
--    (current_date - rental_start::date)+1
from claim c
 left outer join workgroup w on (w.id = c.workgroup_id)
 left outer join web_user wu1 on (wu1.id = c.claim_owner_id)
 left outer join web_user wu2 on (wu2.id = c.cho_claim_owner_id),
 insurer ins, chorganisation cho, customer cu, vehicle_hire vh, vehicle_class vc
where c.insurer_id = ins.id and c.chorganisation_id = cho.id
  and c.customer_id = cu.id and c.vehicle_hire_id = vh.id
  and cu.vehicle_class_id = vc.id
  and (insIDs is null or c.insurer_id = ANY(insIDs))
  and (choIDs is null or c.chorganisation_id = ANY(choIDs))
  and (workgroupIDs is null or c.workgroup_id is null or c.workgroup_id = ANY(workgroupIDs))
  and c.status in ('ClaimUnacknowledgedUnrouted','ClaimUnacknowledgedRouted','ClaimUnacknowledgedRouted',
                    'ClaimReferredToFNOL','ClaimPending','ClaimReferredToEngineer','ClaimUpdatedByEngineer',
                    'ClaimRejectionContested','AwaitingCarHireInfo')
  and vh.rental_start is not null
order by claims_handler, rental_start asc;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION on_hire_report(IN insIDs INT[], IN choIDs INT[], IN workgroupIDs INT[]) TO chox_user;
GRANT EXECUTE ON FUNCTION on_hire_report(IN insIDs INT[], IN choIDs INT[], IN workgroupIDs INT[]) TO chox_mi;

-- select * from on_hire_report(array[3, 19], null::int[], array[14,123,15]);