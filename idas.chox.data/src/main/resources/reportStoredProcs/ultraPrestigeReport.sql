DROP FUNCTION ultra_prestige_report(IN insIDs INT[], IN workgroupIDs INT[], IN startDate text);

CREATE OR REPLACE FUNCTION ultra_prestige_report(IN insIDs INT[], IN workgroupIDs INT[], IN startDate text)
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
   "Customer Vehicle Class" varchar(10)
)
AS
$BODY$
DECLARE
BEGIN 

RETURN QUERY 

select c.cho_reference,
    c.claim_number,
    getClaimType(c.claim_type),
    cho.name, ins.name,
    w.name,
    wu1.first_name || ' ' || wu1.last_name as claims_handler,
    wu2.first_name || ' ' || wu2.last_name as cho_claims_handler,
    case when cu.is_total_loss then 'Yes' else 'No' end as total_loss,
    vc.name
from claim c
    left outer join workgroup w on (w.id = c.workgroup_id)
    left outer join web_user wu1 on (wu1.id = c.claim_owner_id)
    left outer join web_user wu2 on (wu2.id = c.cho_claim_owner_id),
    insurer ins, chorganisation cho, customer cu, vehicle_class vc
where c.insurer_id = ins.id and c.chorganisation_id = cho.id
  and c.customer_id = cu.id and cu.vehicle_class_id = vc.id
  and (insIDs is null or c.insurer_id = ANY(insIDs))
  and (workgroupIDs is null or c.workgroup_id is null or c.workgroup_id = ANY(workgroupIDs))
  and vc.name in ('P13ESTA','P13A','P13EST','P13','P12ESTA','SP13ESTA','P12A','P12EST','SP13A','SP13EST','P12','SP13',
                  'SP12ESTA','SP12A','SP12EST','SP12','P11ESTA','P11A','P11EST','P11','SP11ESTA','SP11A','SP11EST',
                  'SP11','P10ESTA','P10A','P10EST','F9ESTA','P10', 'F9A', 'F9EST', 'F9', 'SP10ESTA', 'SP10A', 'SP10EST',
                  'SP10', 'P9ESTA', 'P9A', 'P9EST', 'F8ESTA', 'SP9ESTA', 'P9', 'F8A', 'F8EST', 'SP9A', 'SP9EST', 'F8',
                  'SP9', 'PT13ESTA', 'F7ESTA', 'T13ESTA', 'PT13A', 'PT13EST', 'F7A', 'F7EST', 'SP8ESTA', 'T13A',
                  'T13EST', 'PT13', 'F7', 'SP8A', 'SP8EST', 'P8ESTA', 'T13', 'SP8', 'P8A', 'P8EST', 'P8')
  and c.created_date > startDate::DATE
order by claims_handler, vc.name;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION ultra_prestige_report(IN insIDs INT[], IN workgroupIDs INT[], IN startDate text) TO chox_user;
GRANT EXECUTE ON FUNCTION ultra_prestige_report(IN insIDs INT[], IN workgroupIDs INT[], IN startDate text) TO chox_mi;

-- select * from ultra_prestige_report(array[3,19], array[14,123,15], '2008-01-01');
-- select * from ultra_prestige_report(array[3,19], array[14,123,15], '2014-11-24');
