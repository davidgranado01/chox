drop function if exists prestige_claim_report(integer, integer[], integer[], varchar[], text, text);

create or replace function prestige_claim_report(IN insid integer, IN chorgs integer[], IN claimType integer[], IN vehicleClass varchar[], IN startDate text, IN endDate text)
returns table
(
    "Insurer Claim Number" character varying(128),
    "Supplier Reference" character varying(128),
    "CHO Name" varchar(128),
    "Notification Date" text,
    "Vehicle group for TP Vehicle" varchar(10),
    "Vehicle Group for Hire Vehicle" varchar(10),
    "Insurer Claim Owner" text
)
as $BODY$

DECLARE
    reportStartDate date;
    reportEndDate date;
BEGIN
    reportStartDate = startDate::Date;
    reportEndDate = endDate::Date;

RETURN QUERY
    select c.claim_number, c.cho_reference, cho.name, to_char(c.created_date, 'dd/mm/yyyy'), vccu.name, vch.name, wu.first_name || ' ' || wu.last_name
    from claim c left outer join web_user wu on (c.claim_owner_id = wu.id) left outer join vehicle_hire vh on (c.vehicle_hire_id=vh.id) left outer join vehicle_class vch on (vh.vehicle_class_id=vch.id), chorganisation cho, customer cu, vehicle_class vccu
    where c.chorganisation_id=cho.id and c.customer_id=cu.id and cu.vehicle_class_id=vccu.id
      and c.created_date >= reportStartDate and c.created_date < reportEndDate
      and case when vehicleClass is null
            then (vccu.name in ('P11','P12','P13','SP12','SP13','P11EST','P12EST','P13EST','SP12EST','SP13EST','P11A','P12A','P13A','SP12A','SP13A','P11ESTA','P12ESTA','P13ESTA','SP12ESTA','SP13ESTA')
                        or vch.name in ('P11','P12','P13','SP12','SP13','P11EST','P12EST','P13EST','SP12EST','SP13EST','P11A','P12A','P13A','SP12A','SP13A','P11ESTA','P12ESTA','P13ESTA','SP12ESTA','SP13ESTA'))
            else (vccu.name=ANY(vehicleClass) or vch.name=ANY(vehicleClass)) end
      and c.insurer_id = insid
      and (chorgs is null or c.chorganisation_id=ANY(chorgs))
      and (claimType is null or c.claim_type=ANY(claimType))
    order by c.created_date;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION prestige_claim_report(integer, integer[], integer[], varchar[], text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION prestige_claim_report(integer, integer[], integer[], varchar[], text, text) TO chox_mi;
-- select * from prestige_claim_report(6, null, null, null, '2017-01-01', '2017-10-01');
