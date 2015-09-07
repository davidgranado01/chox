DROP FUNCTION lv_on_hire_report(IN insId INT, IN choId INT, IN claimTypes INT[], IN totalLoss BOOLEAN);
 
CREATE OR REPLACE FUNCTION lv_on_hire_report(IN insId INT, IN choId INT, IN claimTypes INT[], IN totalLoss BOOLEAN)
RETURNS TABLE (
   "At Fault Insurer Name" character varying(128),
   "Insurer Claim Number" character varying(128),
   "CHO Reference Number" character varying(128),
   "Loss Date" timestamp without time zone,
   "Non-Fault Driver's Last Name" character varying(64),
   "Non-Fault Driver's First Name" character varying(128),
   "Non-Fault Driver's Vehicle Registration Number" character varying(16),
   "Non-Fault Vehicle Class" character varying(10),
   "Hire Vehicle Class" character varying(10),
   "Non-Fault Insurer Name" character varying,
   "Comprehensive Cover?" varchar(3),
   "Total Loss?" varchar(3),
   "CHO Managing Repair?" varchar(3),
   "Non-Fault Insurer Managing Repair?" varchar(3),
   "Repairer Name & Telephone" character varying,
   "Number Of Hire Days" integer,
   "ECD Date" timestamp without time zone,
   "Reason For Delay" text
)
AS
$BODY$
DECLARE
BEGIN 

RETURN QUERY 

select ins.name, c.claim_number, c.cho_reference, i.date,
       cu.last_name, cu.first_name, cu.vehicle_registration, vc.name,
       vc2.name, cu.insurer_name,
       case when cu.comprehensive then 'Yes'::varchar(3) else 'No'::varchar(3) end,
       case when cu.is_total_loss then 'Yes'::varchar(3) else 'No'::varchar(3) end,
       case when c.managing_repair then 'Yes'::varchar(3) else 'No'::varchar(3) end,
       case when hmd.is_non_fault_insurer_managing_repair then 'Yes'::varchar(3) else 'No'::varchar(3) end,
       hmd.name_of_repairer,
       extract(day from now() - vh.rental_start)::integer + 1,
       case when (select count(*) from hire_monitoring_ecd where claim_id = c.id) = 0 then cu.initial_ecd
            else (select ecd_date from hire_monitoring_ecd where claim_id = c.id order by id desc limit 1) end,
--       (select ecd_date from hire_monitoring_ecd where claim_id = c.id order by id desc limit 1),
       (select reason || ': ' || supporting_note from hire_monitoring_ecd where claim_id = c.id order by id desc limit 1)
from claim c, insurer ins, vehicle_hire vh, hire_monitoring_detail hmd, incident i, customer cu, vehicle_class vc, vehicle_class vc2
where c.insurer_id = ins.id
  and c.vehicle_hire_id = vh.id
  and c.hire_monitoring_detail_id = hmd.id
  and c.incident_id = i.id
  and c.customer_id = cu.id
  and cu.vehicle_class_id = vc.id
  and vh.vehicle_class_id = vc2.id
  and ins.id = insId and c.chorganisation_id = choId
  and (claimTypes is null or c.claim_type = ANY(claimTypes))
  and c.invoice_id is null
  and cu.is_total_loss = totalLoss
  and c.status not in ('ClaimClosed','ClaimRejectionAccepted','AwaitingInvoiceData')
  and vh.rental_start is not null
;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION lv_on_hire_report(IN insId INT, IN choId INT, IN claimTypes INT[], IN totalLoss BOOLEAN) TO chox_user;
GRANT EXECUTE ON FUNCTION lv_on_hire_report(IN insId INT, IN choId INT, IN claimTypes INT[], IN totalLoss BOOLEAN) TO chox_mi;

-- select * from lv_on_hire_report(3, 1007, null, true);
