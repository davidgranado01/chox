create or replace function cho_paid_claims(
    IN choId integer,
    IN startPeriod text,
    IN endPeriod text)
returns table
(
   "Name of Insurer" character varying(128),
   "Supplier Reference" character varying(128),
   "Total To Pay" numeric(8,2),
   "Date Claim Paid" date,
   "Current CHOX Status" character varying(40)
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select ins.name, c.cho_reference, i.total_to_pay, a.created_date::date, c.status
from claim c , invoice i, audit_trail a, insurer ins
where c.invoice_id = i.id 
  and a.claim_id = c.id
  and c.insurer_id = ins.id
  and (a.new_status = 'InvoicePaymentLogged' or a.new_status = 'ManualInvoicePaid')
  and a.reverted = false
  and c.chorganisation_id = choId
  and a.created_date::date between startDate and endDate
order by ins.id, a.created_date; 

END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION cho_paid_claims(integer, text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION cho_paid_claims(integer, text, text) TO chox_mi;
-- select * from cho_paid_claims(1010, '2016-11-28', '2016-12-02');
