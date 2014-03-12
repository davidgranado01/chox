-- drop function total_loss_liability_updates(integer[], integer[], text, text);

-- select * from total_loss_liability_updates(array[]::integer[], array[1007], '2013-01-01', '2014-01-01');

create or replace function total_loss_liability_updates
(
    IN insurerIds integer[], IN choIds integer[],
    IN startPeriod text, IN endPeriod text
)
returns table
(
   "Supplier Reference" character varying(128),
   "Insurer Name" character varying(32),
   "Date Liability Changed" text,
   "Liability Status" text,
   "Liability %age For Insurer" numeric(5,2),
   "Liability %age For CHO" numeric(5,2),
   "Claim Status" character varying(40)
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select c.cho_reference as "Supplier Reference", i.name as "Insurer Name",
    case when liability_modified_date between startDate and endDate then case when liability_status_modified_date between  startDate and endDate then case when liability_modified_date > liability_status_modified_date then to_char(liability_modified_date, 'dd/mm/yyyy') else to_char(liability_status_modified_date, 'dd/mm/yyyy') end else to_char(liability_modified_date, 'dd/mm/yyyy') end else to_char(liability_status_modified_date, 'dd/mm/yyyy') end as "Date Liability Changed",
    getLiabilityStatus(c.liability_status) as "Liability Status",
    c.percentage_liability_accepted as "Liability %age For Insurer",
    c.percentage_liability_cho as "Liability %age For CHO",
    c.status as "Claim Status"
from claim c, insurer i
where c.insurer_id = i.id
  and (case when array_length(insurerIds, 1) > 0 then c.insurer_id = ANY(insurerIds) else true end)
  and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
  and (c.liability_modified_date between startDate and endDate or c.liability_status_modified_date  between startDate and endDate)
;
END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION total_loss_liability_updates(integer[], integer[], text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION total_loss_liability_updates(integer[], integer[], text, text) TO chox_mi;
