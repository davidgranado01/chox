-- DROP FUNCTION passedBREReviewReport(IN insId integer, dateFrom VARCHAR, dateTo VARCHAR);
CREATE OR REPLACE FUNCTION passedBREReviewReport(IN insId integer, dateFrom VARCHAR, dateTo VARCHAR)
  RETURNS TABLE("Third Party Insurer's Claim Number" VARCHAR, "Supplier Reference No." VARCHAR, "CHO" VARCHAR, "Workgroup" VARCHAR, "Insurer Claim Owner" TEXT, "Date Invoice Was Uploaded" TIMESTAMP, "Original Total To Pay Amount" NUMERIC, "Settled Amount" NUMERIC,
                "Date claim moved to 'Invoice Payment Logged'" TIMESTAMP, "Days To Pay claim" NUMERIC, "No. of times claim went into 'ContestedInvoiceReferredToInsurer'" BIGINT, "No. of times claim went into 'ContestedInvoiceReferredToCHO'" BIGINT,
                "Original Hire Gross" NUMERIC, "Current Hire Gross" NUMERIC, "Original Repair Gross" NUMERIC, "Current Repair Gross" NUMERIC, "Original Engineer Fee Gross" NUMERIC, "Current Engineer Fee Gross" NUMERIC, "Original Total Loss Fee Gross" NUMERIC,
                "Current Total Loss Fee Gross" NUMERIC, "Original Storage Recovery Gross" NUMERIC, "Current Storage Recovery Gross" NUMERIC, "Original Hire Days" NUMERIC, "Current Hire Days" NUMERIC, "Total Penalty Charge Amount" NUMERIC) AS
$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $2::DATE;
DATE_TO = $3::DATE;

RETURN QUERY

select c.claim_number as "Third Party Insurer's Claim Number",
       c.cho_reference as "Supplier Reference No.",
       ch.name as "CHO",
       w.name as "Workgroup",
       wu.first_name || ' ' || wu.last_name as "Insurer Claim Owner",
       i.created_date as "Date Invoice Was Uploaded",
       io.total_to_pay as "Original Total To Pay Amount",
       i.total_to_pay as "Settled Amount",
       a1.update_date as "Date claim moved to 'Invoice Payment Logged'",
       cast(extract(epoch from a1.update_date - i.created_date)/(3600*24.0) as numeric(6,1)) as "Days To Pay claim",
       (select count(*) from audit_trail a2
        where a2.claim_id = c.id and a2.reverted = false and a2.new_status = 'ContestedInvoiceReferredToInsurer' GROUP BY c.id)
               as "Nof time claim went into 'ContestedInvoiceReferredToInsurer'",
       (select count(*) from audit_trail a3
        where a3.claim_id = c.id and a3.reverted = false and a3.new_status = 'ContestedInvoiceReferredToCHO' GROUP BY c.id)
               as "Nof time claim went into 'ContestedInvoiceReferredToCHO'",
       io.hire_gross as "Original Hire Gross",
       i.hire_gross as "Current Hire Gross",
       io.repair_gross as "Original Repair Gross",
       i.repair_gross as "Current Repair Gross",
       io.engineer_fee_gross as "Original Engineer Fee Gross",
       i.engineer_fee_gross as "Current Engineer Fee Gross",
       io.total_loss_gross as "Original Total Loss Fee Gross",
       i.total_loss_gross as "Current Total Loss Fee Gross",
       io.storage_recovery_gross as "Original Storage Recovery Gross",
       i.storage_recovery_gross as "Current Storage Recovery Gross",
       case when vh.days_original is null then vh.days else vh.days_original end as "Original Hire Days",
       vh.days as "Current Hire Days",
       i.total_penalty_charge as "Total Penalty Charge Amount"
from claim c left outer join workgroup w on c.workgroup_id = w.id
                   left outer join vehicle_hire vh on c.vehicle_hire_id = vh.id,
     web_user wu, invoice i, invoice_original io, audit_trail a1, chorganisation ch, insurer ins
where c.invoice_id = i.id
  and io.id = i.invoice_original_id
  and ins.id = $1                                       -- restricted to insurer
  and c.id = a1.claim_id
  and i.created_date between DATE_FROM and DATE_TO      -- restricted to given period
  and c.chorganisation_id = ch.id
  and c.insurer_id = ins.id
  and c.claim_owner_id = wu.id
  and a1.new_status = 'InvoicePaymentLogged'
  and a1.reverted = false
  and not exists (select * from history h where h.claim_id = c.id and h.type = 'ERROR');

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
GRANT EXECUTE ON FUNCTION passedBREReviewReport(IN insId integer, dateFrom VARCHAR, dateTo VARCHAR) TO chox_user;

