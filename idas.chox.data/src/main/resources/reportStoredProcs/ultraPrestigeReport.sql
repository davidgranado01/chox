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
   "Customer Vehicle Class" varchar(10),
   "Claim Upload Date" text
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
    vc.name, to_char(c.created_date, 'dd/mm/yyyy')
from claim c
    left outer join workgroup w on (w.id = c.workgroup_id)
    left outer join web_user wu1 on (wu1.id = c.claim_owner_id)
    left outer join web_user wu2 on (wu2.id = c.cho_claim_owner_id)
    left outer join vehicle_class_price_special_rate vcps on (vcps.insurer_id = c.insurer_id and vcps.chorganisation_id = c.chorganisation_id),
    insurer ins, chorganisation cho, customer cu, vehicle_class vc, bre_band bre, bre_band_organisation bbo,
    vehicle_class_price vcp
where c.insurer_id = ins.id and c.chorganisation_id = cho.id
  and c.customer_id = cu.id and cu.vehicle_class_id = vc.id
  and (insIDs is null or c.insurer_id = ANY(insIDs))
  and (workgroupIDs is null or c.workgroup_id is null or c.workgroup_id = ANY(workgroupIDs))
  and bre.insurer_id = c.insurer_id and bbo.chorganisation_id = c.chorganisation_id and bbo.band_id = bre.id
  and vcp.vehicle_class_id = vc.id and vcp.start_date = (select max(start_date) from vehicle_class_price vcp1 where vcp1.vehicle_class_id = vcp.vehicle_class_id)
  and vcp.age = (select min(age) from vehicle_class_price vcp1 where vcp1.vehicle_class_id = vcp.vehicle_class_id)
  and (vcps is null or (vcps.vehicle_class_id = vc.id and vcps.start_date = (select max(start_date) from vehicle_class_price_special_rate vcps1 where vcps1.vehicle_class_id = vc.id and vcps1.insurer_id=ins.id and vcps1.chorganisation_id = cho.id) and vcps.age = (select min(age) from vehicle_class_price_special_rate vcps1 where vcps1.vehicle_class_id = vc.id and vcps1.insurer_id=ins.id and vcps1.chorganisation_id = cho.id)))
  and ((bre.use_supplier_rates = false and c.claim_type not in (7,8,9,11,12,13,18,19,20) and vcp.price > 225.00)
    or (vcps.price > 225.00))
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
