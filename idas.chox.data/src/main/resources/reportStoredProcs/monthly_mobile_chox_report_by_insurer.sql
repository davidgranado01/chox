
DROP FUNCTION monthly_mobile_chox_report_by_insurer(integer);

CREATE OR REPLACE FUNCTION monthly_mobile_chox_report_by_insurer(IN insurerid integer)
  RETURNS TABLE("Month" text, "No Notifications Received" bigint, "No Invoices Received" bigint, "No Invoices Paid" bigint, "Value of Invoices Received" numeric, "Value of Invoices Paid" numeric, "Average Days Invoice Loaded to Payment" numeric, "Cumulative Total Invoices Submitted" bigint, "Cumulative Total Invoices Paid" bigint, "Cumulative Total Invoices Outstanding" bigint, "No of CHOs Onboard" text, "No Manual CHOs" text, "No of Insurers Onboard" text, "No of CHO Insurer Pairings" bigint, "Number of Registered Users" bigint) AS
$BODY$

DECLARE

   insurerId ALIAS FOR $1;
   first_claim_date date;
   start_date date;
   end_date date;

BEGIN

   first_claim_date = (select min(created_date) FROM claim where insurer_id = insurerId);
   start_date = (select to_date(to_char(first_claim_date, 'MM') || '-01-' || to_char(first_claim_date, 'yyyy'), 'mm-dd-yyyy'));
   end_date = start_date +  interval '1 month';

WHILE start_date < now() LOOP

   
RETURN QUERY

   select to_char(start_date, 'TMMonth') || ' - ' || to_char(start_date , 'yyyy') AS "Month",

  (select count(*) as No_Notifications_Received from claim where created_date between start_date and end_date and insurer_id = insurerId),
       
  (select count(*) as No_Invoices_Received from invoice i, claim c where c.invoice_id = i.id and c.insurer_id = insurerId and i.created_date between start_date and end_date),

  (select count(*) as No_Invoices_Paid from audit_trail a, claim c
    where c.id = a.claim_id and c.insurer_id = insurerId
      and a.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
      and a.update_date between start_date and end_date
      and a.reverted=false),
                  
  (select sum(io.total_to_pay)::numeric(15,2) as Value_of_Invoices_Received 
       from invoice i, claim c, invoice_original io 
       where i.invoice_original_id = io.id and c.invoice_id = i.id and i.created_date between start_date and end_date and c.insurer_id = insurerId),
       
  (select sum(i.total_to_pay)::numeric(15,2) as Value_of_Invoices_Paid from audit_trail a, invoice i, claim c
       where a.new_status='InvoicePaymentLogged' and a.update_date between start_date and end_date
       and a.reverted=false and c.insurer_id = insurerId
       and c.id = a.claim_id and c.invoice_id = i.id),
                      
   (select case when count(*) = 0 then 0 else avg(EXTRACT(DAY FROM (a.update_date - i.created_date)))::numeric(8,2) end as Average_Days_Invoice_Loaded_to_Payment 
              from invoice i, claim c, audit_trail a where a.claim_id = c.id and c.invoice_id = i.id and a.new_status in ('InvoicePaymentLogged', 'ManualInvoicePaid') 
              and a.reverted=false and i.created_date between start_date and end_date and c.insurer_id = insurerId),
              
   (select count(*) as Cumulative_Total_Invoices_Submitted from invoice i, claim c where c.invoice_id = i.id and c.insurer_id = insurerId and i.created_date < end_date),
   
   (select count(*) as Cumulative_Total_Invoices_Paid from audit_trail a, claim c where c.id = a.claim_id and c.insurer_id = insurerId
              and a.new_status='InvoicePaymentLogged' and a.update_date < end_date and a.reverted=false),

   (select count(*) as Cumulative_Total_Invoices_Outstanding
        from claim c , audit_trail a
        where a.claim_id = c.id and c.insurer_id = insurerId
              and a.new_status in ('InvoiceReferredToEngineer','InvoiceEscalated', 'InvoiceDataCalculationIncorrect', 'InvoiceApprovedByBRE', 'ContestedInvoiceReferredToInsurer',
                               'ContestedInvoiceReferredToCHO', 'InvoiceReferredToClaimsHandler', 'AwaitingLiabilityResolution', 'InvoiceUnassigned', 'InvoiceEscalatedToHandler', 
                               'ManualInvoiceBRERejected', 'ManualInvoiceContested', 'ManualInvoiceBREApproved', 'AwaitingLitigationOutcome')
              and a.created_date < end_date
              and (a.reverted = false or (a.reverted=true and a.last_modified_date > end_date))  -- check it wasn't reverted, or if it was, it was reverted AFTER the end of the month
              and not exists (select * from audit_trail a2 where a2.claim_id = c.id and a2.created_date >= a.created_date and a2.created_date < end_date  and a2.id != a.id and (a2.reverted=false or a2.last_modified_date > end_date))),
              
   (select '-'::text as No_of_CHOS_Onboard),
   
   (select '-'::text as No_Manual_CHOs ),
              
   (select '-'::text as No_of_Insurers_Onboard),

   (select count(*) as No_of_CHO_Insurer_Pairings from insurer_chorganisation ic , insurer i, chorganisation c where ic.insurer_id = i.id 
                   and i.id=insurerId and ic.chorganisation_id = c.id and c.status = true and i.status = true and ic.created_date < end_date),
              
   (select count(*) from web_user wu
          where status = true and created_date < end_date and insurer_id = insurerId
          and not exists (select * from web_user_user_role wuur, web_user_role wur
                  where wuur.web_user_id = wu.id and wuur.web_user_role_id = wur.id 
                  and wur.name = 'ROLE_CHOX_ADMIN'));
start_date = end_date;
end_date = end_date + interval '1 month';

END LOOP;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION monthly_mobile_chox_report_by_insurer(integer)
  OWNER TO chox;
