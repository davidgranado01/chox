CREATE OR REPLACE FUNCTION insurer_cycle_and_value_report(IN insid integer, IN startDate text, IN endDate text)
  RETURNS TABLE("CHO Reference" varchar, "Original Hire Gross" numeric, "Current Hire Gross" numeric, "Hire Gross Paid" numeric, "Current Hire Penalty Charge" numeric, 
  "Hire Penalty Charege Paid" numeric, "Days From Date Claim Uploaded Into CHOX to Date Claim Moved To Invoice Payment Logged" float, "Day Rate Of Hire Paid" numeric, 
  "No. Days Hire" numeric, "Current Claim Status" varchar, "Claim Type" text, "CHO Name" varchar, "Workgroup" varchar, 
  "Claim Owner" text, "Last Status Modified Date" timestamp, "Liability Status" text, "Date Claim Uploaded into CHOX" timestamp, "Vehicle Reg Number" varchar) AS
$BODY$

DECLARE
   INS_ID int;
   END_DATE date;
   START_DATE date;

BEGIN
   INS_ID =  insid;
   END_DATE = endDate::Date;
   START_DATE = startDate::Date;
   
RETURN QUERY

 select 
       c.cho_reference,
       io.hire_gross,
       i.hire_gross,
       i.hire_gross_paid,
       i.hire_penalty_charge,
       i.hire_penalty_charge_paid,
       case when a1.id is null then null else EXTRACT(DAY FROM (a1.created_date - c.created_date)) end,
       case when (vh.days is null or vh.days = 0) then 0 
            else ((i.hire_net - (i.miscellaneous_fee + i.automatic_fee + i.additional_driver_fee + i.sat_nav_fee + i.estate_fee 
                    + i.baby_seat_fee + i.tow_bars_fee + i.non_standard_insurance_premium_fee + i.admin_fee + i.roof_rack_fee 
                    + i.dual_control_fee + i.delivery_collection_fee)) / vh.days)::numeric(8,2) end,
       vh.days,
       c.status,
       getclaimtype(c.claim_type),
       cho.name,
       wg.name,
       (wu.first_name || wu.last_name),
       c.status_modified_date,
       getliabilitystatus(c.liability_status) ,
       c.created_date,
       tp.vehicle_registration 
 from 
      claim c left join vehicle_hire vh on vh.id = c.vehicle_hire_id 
      left join workgroup wg on wg.id = c.workgroup_id
      left join web_user wu on wu.id = c.claim_owner_id
      left join audit_trail a1 on (a1.claim_id = c.id and a1.reverted = false and a1.new_Status = 'InvoicePaymentLogged'),
      invoice i,
      invoice_original io,
      chorganisation cho,
      third_party tp
 where 
      c.invoice_id = i.id
      and i.invoice_original_id = io.id
      and c.chorganisation_id = cho.id
      and tp.id = c.third_party_id
      and not exists (select claim_id from audit_trail a where a.claim_id = a1.claim_id and a.reverted = false and a.new_status = 'InvoicePaymentLogged' and a.created_date > a1.created_date)
      and c.insurer_id = INS_ID
      and i.created_date between START_DATE and END_DATE
      order by c.status;

END;
$BODY$
LANGUAGE plpgsql VOLATILE;
ALTER FUNCTION insurer_cycle_and_value_report(IN insid integer, IN startDate text, IN endDate text)
OWNER TO chox;
