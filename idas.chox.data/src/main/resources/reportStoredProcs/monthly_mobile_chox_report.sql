
DROP FUNCTION monthly_mobile_chox_report();

CREATE OR REPLACE FUNCTION monthly_mobile_chox_report()
  RETURNS TABLE("Month" text, no_notifications_received bigint, no_invoices_received bigint, no_invoices_paid bigint, value_of_invoices_received numeric, value_of_invoices_paid numeric, average_days_invoice_loaded_to_payment numeric, cumulative_total_invoices_submitted bigint, cumulative_total_invoices_paid bigint, cumulative_total_invoices_outstanding bigint, no_of_chos_onboard bigint, no_manual_chos bigint, no_of_insurers_onboard bigint, no_of_cho_insurer_pairings bigint, number_of_registered_users bigint) AS
$BODY$

DECLARE
   first_claim_date date;
   start_date date;
   end_date date;
BEGIN

   first_claim_date = (select min(created_date) FROM claim);
   start_date = (select to_date(to_char(first_claim_date, 'MM') || '-01-' || to_char(first_claim_date, 'yyyy'), 'mm-dd-yyyy'));
   end_date = start_date +  interval '1 month';

WHILE start_date < now() LOOP

   
RETURN QUERY

   select to_char(start_date, 'TMMonth') || ' - ' || to_char(start_date , 'yyyy') AS "Month",

  (select count(*) as No_Notifications_Received from claim where created_date between start_date and end_date),
       
  (select count(*) as No_Invoices_Received from invoice where created_date between start_date and end_date),

  (select count(*) as No_Invoices_Paid from audit_trail a where a.new_status='InvoicePaymentLogged' 
       and a.update_date between start_date and end_date
       and a.reverted=false),
                  
  (select sum(io.total_to_pay)::numeric(15,2) as Value_of_Invoices_Received 
       from invoice i, invoice_original io, claim c
       where c.invoice_id = i.id and i.invoice_original_id = io.id and i.created_date between start_date and end_date),
       
  (select sum(i.total_to_pay)::numeric(15,2) as Value_of_Invoices_Paid from audit_trail a, invoice i, claim c
       where a.new_status='InvoicePaymentLogged' and a.update_date between start_date and end_date
       and a.reverted=false
       and c.id = a.claim_id and c.invoice_id = i.id),
                      
   (select case when count(*) = 0 then 0 else avg(EXTRACT(DAY FROM (a.update_date - i.created_date)))::numeric(8,2) end as Average_Days_Invoice_Loaded_to_Payment 
              from invoice i, claim c, audit_trail a where a.claim_id = c.id and c.invoice_id = i.id and a.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') 
              and a.reverted=false and i.created_date between start_date and end_date),
              
   (select count(*) as Cumulative_Total_Invoices_Submitted from invoice where created_date < end_date),
   
   (select count(*) as Cumulative_Total_Invoices_Paid from audit_trail a where a.new_status='InvoicePaymentLogged' and a.update_date < end_date and a.reverted=false),

   (select count(*) as Cumulative_Total_Invoices_Outstanding
        from claim c , audit_trail a
        where a.claim_id = c.id
              and a.new_status in ('InvoiceReferredToEngineer','InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer',
                               'ContestedInvoiceReferredToCHO', 'InvoiceReferredToClaimsHandler', 'AwaitingLiabilityResolution', 'InvoiceUnassigned', 'InvoiceEscalatedToHandler', 
                               'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceBREApproved', 'AwaitingLitigationOutcome')
              and a.created_date < end_date
              and (a.reverted = false or (a.reverted=true and a.last_modified_date > end_date))  -- check it wasn't reverted, or if it was, it was reverted AFTER the end of the month
              and not exists (select * from audit_trail a2 where a2.claim_id = c.id and a2.created_date > a.created_date and a2.created_date < end_date)),
              
   (select count(*) as No_of_CHOS_Onboard from chorganisation where status = true and insurer_upload_only = false and created_date < end_date),
   
   (select count(*) as No_Manual_CHOs from chorganisation where status = true and insurer_upload_only = true and created_date < end_date),
              
   (select count(*) as No_of_Insurers_Onboard from insurer where status = true and created_date < end_date),

   (select count(*) as No_of_CHO_Insurer_Pairings from insurer_chorganisation ic , insurer i, chorganisation c where ic.insurer_id = i.id 
                   and ic.chorganisation_id = c.id and c.status = true and i.status = true and ic.created_date < end_date),
              
   (select count(distinct wu.id) from web_user wu, web_user_user_role wuur, web_user_role wur  where wu.id = wuur.web_user_id 
              and wur.name != 'ROLE_CHOX_ADMIN' 
              and wuur.web_user_role_id = wur.id 
              and wu.status = true and wu.created_date < end_date);


start_date = end_date;
end_date = end_date + interval '1 month';

END LOOP;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION monthly_mobile_chox_report()
  OWNER TO chox;
