drop function hire_invoice_upload_report(integer[], integer[], integer[], text, text);

create or replace function hire_invoice_upload_report
(
   IN insurerIds integer[], IN choIds integer[], IN claimTypes integer[], IN startPeriod text, IN endPeriod text
)
returns table
(
   "Insurer Claim Number" character varying(128),
   "Supplier Reference" character varying(128),
   "Invoice Upload Date" text,
   "Hire Start Date" text,
   "Hire End Date" text,
   "Claim Status" character varying(40),
   "Vehicle Registration Number" character varying(16)
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select c.claim_number, c.cho_reference, to_char(i.created_date, 'dd/mm/yyyy'), to_char(vh.rental_start, 'dd/mm/yyyy hh24:mi:ss'), to_char(vh.rental_end, 'dd/mm/yyyy hh24:mi:ss'), c.status, cu.vehicle_registration
from claim c, vehicle_hire vh, customer cu, invoice i
where c.invoice_id=i.id and c.customer_id=cu.id and c.vehicle_hire_id=vh.id
  and i.hire_net > i.admin_fee
  and (insurerIds is null or c.insurer_id = ANY(insurerIds))
  and (choIds is null or c.chorganisation_id = ANY(choIds))
  and (claimTypes is null or array_length(claimTypes, 1) < 1 or c.claim_type = ANY(claimTypes))
  and i.created_date::date >= startDate and i.created_date::date < endDate
order by i.created_date asc;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION hire_invoice_upload_report(integer[], integer[], integer[], text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION hire_invoice_upload_report(integer[], integer[], integer[], text, text) TO chox_mi;
-- select * from hire_invoice_upload_report(array[6], array[1015, 1016, 1735], null::integer[], '2018-09-17', '2018-09-18');