-- drop function non_invoiced_total_loss_report(integer[], integer[], integer[], integer[], integer, boolean);
create or replace function non_invoiced_total_loss_report
(
   IN insurerIds integer[], IN choIds integer[], IN claimTypes integer[], IN vehicleClasses integer[], IN days integer, IN totalLoss boolean
)
returns table
(
   "Insurer Claim Number" character varying(128),
   "Supplier Reference" character varying(128),
   "Claim Status" character varying(128),
   "CHO" character varying(128),
   "Incident Date" text,
   "Vehicle Group for TP Vehicle" character varying(10),
   "Number of Days Since Incident" integer
)
as $$ 
BEGIN 

RETURN QUERY

    select c.claim_number, c.cho_reference, c.status, cho.name, to_char(i.date, 'dd/mm/yyyy'), vc.name, now()::date - i.date::date
    from claim c, chorganisation cho, incident i, customer cu, vehicle_class vc
    where c.chorganisation_id = cho.id and c.invoice_id is null and c.incident_id=i.id
      and c.customer_id=cu.id and cu.vehicle_class_id=vc.id
      and c.status not in ('ClaimClosed','ClaimRejectionAccepted')
      and now()::date - i.date::date >= days
      and (totalLoss is null or cu.is_total_loss = totalLoss)
      and (insurerIds is null or c.insurer_id = ANY(insurerIds))
      and (choIds is null or c.chorganisation_id = ANY(choIds))
      and (claimTypes is null or c.claim_type = ANY(claimTypes))
      and (vehicleClasses is null or cu.vehicle_class_id = ANY(vehicleClasses))
    order by i.date;

END;
$$ LANGUAGE plpgsql;
GRANT EXECUTE ON FUNCTION non_invoiced_total_loss_report(integer[], integer[], integer[], integer[], integer, boolean) TO chox_user;
GRANT EXECUTE ON FUNCTION non_invoiced_total_loss_report(integer[], integer[], integer[], integer[], integer, boolean) TO chox_mi;

-- e.g.
--      select * from non_invoiced_total_loss_report(array[6], null::integer[], null::integer[], null::integer[], 80, true);
--
