drop function remaining_sla_days_report(IN insurerids integer[]);

create or replace function remaining_sla_days_report(IN insurerids integer[])
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
   "SLA Days Remaining" character varying(5)
)
as $BODY$

BEGIN 

RETURN QUERY
select remaining_sla_days."Supplier Reference", remaining_sla_days."Insurer Claim Number", remaining_sla_days."Current Status", remaining_sla_days."Workgroup", remaining_sla_days."Insurer Claim Owner", remaining_sla_days."CHO Name", remaining_sla_days."Claim Type", remaining_sla_days."Insurer Name",
        case when remaining_sla_days."SLA Days Remaining" = 0 then remaining_sla_days."SLA Cut-Off Time" else remaining_sla_days."SLA Days Remaining"::character varying(5) end as "SLA Days Remaining"
from remaining_sla_days(insurerids)
where remaining_sla_days."SLA Days Remaining" >= 0
;

END;
$BODY$
LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION remaining_sla_days_report(IN insurerids integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION remaining_sla_days_report(IN insurerids integer[]) TO chox_mi;

-- select * from remaining_sla_days_report(array[3]);
