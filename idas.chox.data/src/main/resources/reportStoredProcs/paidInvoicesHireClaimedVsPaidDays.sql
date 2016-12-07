create or replace function paid_invoices_hire_claimed_vs_paid_days(
    IN insId integer,
    IN choIds integer[],
    IN startPeriod text,
    IN endPeriod text)
returns table
(
   "Name of CHO" character varying(128),
   "Supplier Reference" character varying(128),
   "Insurer Claim Reference" character varying(128),
   "Hire Days Claimed" numeric(4,0),
   "Hire Days Paid" numeric(4,0),
   "Hire Gross Paid" numeric(8,2),
   "Repair Gross Paid" numeric(8,2),
   "Total Loss" text
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY
    select cho.name, c.cho_reference, c.claim_number,
        case when vh.days_original is null then vh.days else vh.days_original end,
        vh.days,
        case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end,
        case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end,
        case when cu.is_total_loss then 'Yes' else 'No' end
    from claim c, chorganisation cho, vehicle_hire vh, invoice i, customer cu, audit_trail at
    where c.chorganisation_id = cho.id and c.customer_id=cu.id and c.vehicle_hire_id=vh.id and c.invoice_id=i.id
      and c.id = at.claim_id and at.reverted=false and at.new_status='InvoicePaymentLogged'
      and at.created_date::date between startDate and endDate
      and c.insurer_id = insId
      and (choIds is null or c.chorganisation_id = ANY(choIds));

END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION paid_invoices_hire_claimed_vs_paid_days(integer, integer[], text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION paid_invoices_hire_claimed_vs_paid_days(integer, integer[], text, text) TO chox_mi;
-- select * from paid_invoices_hire_claimed_vs_paid_days(6, array[1015], '2016-11-28', '2016-12-02');
