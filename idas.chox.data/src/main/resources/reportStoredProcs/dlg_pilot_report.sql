drop function dlg_pilot_report(text, integer, integer[], integer[], text);
-- select * from dlg_pilot_report('2016-04-16', 6, array[1007,1008,1009,1125], null, '2015-11-01');
create or replace function dlg_pilot_report
(
   reportStart text, insid integer, chorgs integer[], claimType integer[], claimUpload text
)
returns table
(
   id   integer,
   Report text,
   last_6_months numeric(14,2),
   current_month numeric(12,2),
   previous_month numeric(12,2),
   previous_2_month numeric(12,2),
   previous_3_month numeric(12,2),
   previous_4_month numeric(12,2),
   previous_5_month numeric(12,2)
)
as $$

DECLARE
    reportStartDate date;
    claimUploadStart date;
BEGIN
    reportStartDate = reportStart::Date;
    claimUploadStart = claimUpload::Date;

RETURN QUERY

select 1 as id, 'Total No. New Claims Uploaded' as title ,
(select count(*) from claim c, chorganisation cho
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select count(*) from claim c, chorganisation cho
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as current_month,

(select count(*) from claim c, chorganisation cho
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select count(*) from claim c, chorganisation cho
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select count(*) from claim c, chorganisation cho
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month,

(select count(*) from claim c, chorganisation cho
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) as previous_4_month,

(select count(*) from claim c, chorganisation cho
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month
from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

select 2 as id, 'Total No. Invoices Paid' as title ,
(select count(*) from claim c, invoice i
 where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged', 'PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_6_months,  


(select count(*) from claim c, invoice i
 where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged', 'PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,  


(select count(*) from claim c, invoice i
 where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged', 'PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,  

(select count(*) from claim c, invoice i
 where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged', 'PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month, 

(select count(*) from claim c, invoice i
 where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged', 'PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month, 

(select count(*) from claim c, invoice i
 where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged', 'PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) as previous_4_month,  

(select count(*) from claim c, invoice i
 where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged', 'PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 3 as id, 'Total No. Invoices Uploaded' as title ,
(select count(*)
from claim c, invoice i, chorganisation cho
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select count(*)
from claim c, invoice i, chorganisation cho
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select count(*)
from claim c, invoice i, chorganisation cho
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select count(*)
from claim c, invoice i, chorganisation cho
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select count(*)
from claim c, invoice i, chorganisation cho
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month,

(select count(*)
from claim c, invoice i, chorganisation cho
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*)
from claim c, invoice i, chorganisation cho
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

-- Invoices Paid as a percentage of invoices uploaded (less those at Claim Closed or Invoice Rejection Accepted) to be reported in the month of upload.
select 4 as id, 'Settlement Ratio %' as title ,
(select (100*sum(case when c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid') then 1 else 0 end)) / sum(1)
from claim c, invoice i
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and c.status not in ('ClaimClosed','InvoiceRejectionAccepted')
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select (100*sum(case when c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid') then 1 else 0 end)) / sum(1)
from claim c, invoice i
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and c.status not in ('ClaimClosed','InvoiceRejectionAccepted')
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select (100*sum(case when c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid') then 1 else 0 end)) / sum(1)
from claim c, invoice i
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and c.status not in ('ClaimClosed','InvoiceRejectionAccepted')
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select (100*sum(case when c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid') then 1 else 0 end)) / sum(1)
from claim c, invoice i
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and c.status not in ('ClaimClosed','InvoiceRejectionAccepted')
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select (100*sum(case when c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid') then 1 else 0 end)) / sum(1)
from claim c, invoice i
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and c.status not in ('ClaimClosed','InvoiceRejectionAccepted')
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month,

(select (100*sum(case when c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid') then 1 else 0 end)) / sum(1)
from claim c, invoice i
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and c.status not in ('ClaimClosed','InvoiceRejectionAccepted')
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select (100*sum(case when c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid') then 1 else 0 end)) / sum(1)
from claim c, invoice i
    where c.invoice_id=i.id
       and c.created_date > claimUploadStart
       and c.status not in ('ClaimClosed','InvoiceRejectionAccepted')
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

select 5 as id, 'Total Hire Invoiced' as title ,
(select coalesce(sum(io.hire_gross), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(sum(io.hire_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(io.hire_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(io.hire_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(io.hire_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(io.hire_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(io.hire_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

select 6 as id, 'Total Hire Paid Exc. LPPs' as title,
(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0)::numeric(14,2) from claim c , invoice i
    where c.invoice_id = i.id and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                         and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0)::numeric(12,2) from claim c , invoice i
    where c.invoice_id = i.id and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                         and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0)::numeric(12,2) from claim c , invoice i
    where c.invoice_id = i.id and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                         and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0)::numeric(12,2) from claim c , invoice i
    where c.invoice_id = i.id and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0)::numeric(12,2) from claim c , invoice i
    where c.invoice_id = i.id and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0)::numeric(12,2) from claim c , invoice i
    where c.invoice_id = i.id and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
                         and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0)::numeric(12,2) from claim c , invoice i
    where c.invoice_id = i.id and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                         and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

select 7 as id, 'Total Hire Paid Inc. LPPs' as title ,
(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(14,2) from claim c , invoice i, chorganisation cho
    where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                         and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_6_months,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                          and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                          and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                          and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                          and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
                          and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                          and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

-- Show the total of Original Full Total Requested less total of Total To Pay and Total Penalty Charge for invoices that
-- have moved to 'Payment Received' or 'Manual Invoice Paid', the invoices will be reported in the period/column that the
-- invoice were uploaded into CHOX.
select 8 as id, 'Total Hire Savings Exc. LPPs' as title ,
(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                         and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                         and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                         and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
                         and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                         and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month


from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

-- Show the total of Original Full Total Requested less total of Total To Pay for invoices that have moved to
-- 'Payment Received' or 'Manual Invoice Paid', the invoices will be reported in the period/column that the invoice
-- were uploaded into CHOX.
select 9 as id, 'Total Hire Savings Inc. LPPs' as title ,
(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid + i.hire_penalty_charge_paid else i.hire_gross + i.hire_penalty_charge end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                         and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid + i.hire_penalty_charge_paid else i.hire_gross + i.hire_penalty_charge end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                         and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid + i.hire_penalty_charge_paid else i.hire_gross + i.hire_penalty_charge end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                         and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid + i.hire_penalty_charge_paid else i.hire_gross + i.hire_penalty_charge end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid + i.hire_penalty_charge_paid else i.hire_gross + i.hire_penalty_charge end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid + i.hire_penalty_charge_paid else i.hire_gross + i.hire_penalty_charge end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
                         and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum((case when io.hire_gross = 0 then i.hire_gross else io.hire_gross end) - (case when i.final_payment is not null then i.hire_gross_paid + i.hire_penalty_charge_paid else i.hire_gross + i.hire_penalty_charge end)), 0)::numeric(14,2) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                         and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month


from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 10 as id, 'Average Hire Value Invoiced' as title ,
(select coalesce(avg(o.hire_gross), 0)::numeric(12,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and i.hire_net - i.admin_fee > 0
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
     and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_6_months,


(select coalesce(avg(o.hire_gross), 0)::numeric(12,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(12,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select coalesce(avg(o.hire_gross), 0)::numeric(12,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(12,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(12,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(12,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month


from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 11 as id, 'Average Hire Value Paid' as title ,
(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(12,2) from claim c , invoice i
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(12,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(12,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(12,2) from claim c , invoice i
where c.invoice_id = i.id   
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(12,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(12,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(12,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.hire_net - i.admin_fee > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 12 as id, 'Average Hire Days Invoiced' as title ,
(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end) 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_6_months,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 13 as id, 'Average Hire Days Paid' as title ,
(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end) 
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

select 14 as id, 'Average Daily Hire Rate Invoiced' as title ,
(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION


select 15 as id, 'Average Daily Hire Rate Paid' as title ,
(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice i, chorganisation cho
   where c.invoice_id = i.id
        and c.created_date > claimUploadStart
        and i.hire_net - i.admin_fee > 0
        and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
        and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice i, chorganisation cho
   where c.invoice_id = i.id
        and c.created_date > claimUploadStart
        and i.hire_net - i.admin_fee > 0
        and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
        and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice i, chorganisation cho
   where c.invoice_id = i.id
        and c.created_date > claimUploadStart
        and i.hire_net - i.admin_fee > 0
        and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
        and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice i, chorganisation cho
   where c.invoice_id = i.id
        and c.created_date > claimUploadStart
        and i.hire_net - i.admin_fee > 0
        and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
        and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as previous_2_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice i, chorganisation cho
   where c.invoice_id = i.id
        and c.created_date > claimUploadStart
        and i.hire_net - i.admin_fee > 0
        and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
        and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,
                                    
(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice i, chorganisation cho
   where c.invoice_id = i.id
        and c.created_date > claimUploadStart
        and i.hire_net - i.admin_fee > 0
        and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
        and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(10,2) from claim c, invoice i, chorganisation cho
   where c.invoice_id = i.id
        and c.created_date > claimUploadStart
        and i.hire_net - i.admin_fee > 0
        and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
        and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 16 as id, 'Average Total Loss Hire Days Invoiced' as title ,
(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_6_months,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 17 as id, 'Average Total Loss Hire Days Paid' as title ,
(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 18 as id, 'Average Non Total Loss Hire Days Invoiced' as title ,
(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_6_months,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month
from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

select 19 as id, 'Average Non Total Loss Hire Days Paid' as title ,
(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and c.created_date > claimUploadStart
       and i.hire_net - i.admin_fee > 0
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

-- Show the average days between Date Repair Commenced and Repair Completion Date in Days for invoices that have moved
-- to 'Payment Received' or 'Manual Invoice Paid'. Use the average based on the number of invoices with the relevant
-- data not against all invoices in that time period as this data is often not included in the claim
select 20 as id, 'Average Repair Duration' as title ,
(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month


from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

-- Show the average days between Date Repair Commenced and Repair Completion Date in Days for invoices that have moved
-- to 'Payment Received' or 'Manual Invoice Paid' with a value >£100 in Repair Net. Use the average based on the number
-- of invoices with the relevant data not against all invoices in that time period as this data is often not included
-- in the claim.
select 21 as id, 'Average Credit Repair Duration' as title ,
(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and i.repair_net > 100.00
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and i.repair_net > 100.00
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and i.repair_net > 100.00
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and i.repair_net > 100.00
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and i.repair_net > 100.00
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and i.repair_net > 100.00
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1)::numeric(10,1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and i.repair_net > 100.00
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

-- Show the average days between Date Repair Commenced and Repair Completion Date in Days for invoices that have moved
-- to 'Payment Received' or 'Manual Invoice Paid' and 'Non-Fault Insurer Managing Repair?' marked as 'Yes'. Use the
-- average based on the number of invoices with the relevant data not against all invoices in that time period as this
-- data is often not included in the claim.
select 22 as id, 'Average TPI Dealing Repair Duration' as title ,
(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = true
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = true
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = true
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = true
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = true
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = true
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = true
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

-- Show the average days between Date Repair Commenced and Repair Completion Date in Days for invoices that have moved
-- to 'Payment Received' or 'Manual Invoice Paid' and 'Non-Fault Insurer Managing Repair?' marked as 'No' and >£100 in
-- Repair Net and 'CHO Managing Repair?' as 'No'. Use the average based on the number of invoices with the relevant
-- data not against all invoices in that time period as this data is often not included in the claim.
select 23 as id, 'Average DLG Dealing Repair Duration' as title ,
(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = false
       and i.repair_net > 100.00
       and c.managing_repair = false
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = false
       and i.repair_net > 100.00
       and c.managing_repair = false
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = false
       and i.repair_net > 100.00
       and c.managing_repair = false
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = false
       and i.repair_net > 100.00
       and c.managing_repair = false
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = false
       and i.repair_net > 100.00
       and c.managing_repair = false
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = false
       and i.repair_net > 100.00
       and c.managing_repair = false
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(hmd.repair_completion_date::Date - hmd.repair_commenced_date::Date + 1) from claim c, invoice i, hire_monitoring_detail hmd
 where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id
       and c.created_date > claimUploadStart
       and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
       and hmd.is_non_fault_insurer_managing_repair = false
       and i.repair_net > 100.00
       and c.managing_repair = false
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 24 as id, 'Total Repair Value Invoiced' as title ,
(select coalesce(sum(io.repair_gross), 0)::numeric(14,2) from claim c , invoice i, invoice_original io, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(sum(io.repair_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(io.repair_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(io.repair_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(io.repair_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(io.repair_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(io.repair_gross), 0)::numeric(12,2) from claim c , invoice i, invoice_original io, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

select 25 as id, 'Total Repair Value Paid' as title ,
(select coalesce(sum(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(14,2) from claim c , invoice i, chorganisation cho
    where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_6_months,


(select coalesce(sum(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(12,2) from claim c , invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(sum(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(12,2) from claim c , invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select coalesce(sum(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(12,2) from claim c , invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(12,2) from claim c , invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(sum(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(12,2) from claim c , invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(12,2) from claim c , invoice i, chorganisation cho
    where c.invoice_id = i.id
       and c.created_date > claimUploadStart
       and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
       and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

-- Show the sum of Total Repair Value Invoiced less Total Repair Value Paid.
select 26 as id, 'Total Repair Savings' as title ,
(select coalesce(sum((case when o.repair_gross = 0 then i.repair_gross else o.repair_gross end) - (case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                            and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_6_months,

(select coalesce(sum((case when o.repair_gross = 0 then i.repair_gross else o.repair_gross end) - (case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                            and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(sum((case when o.repair_gross = 0 then i.repair_gross else o.repair_gross end) - (case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                            and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum((case when o.repair_gross = 0 then i.repair_gross else o.repair_gross end) - (case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                            and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum((case when o.repair_gross = 0 then i.repair_gross else o.repair_gross end) - (case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                            and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(sum((case when o.repair_gross = 0 then i.repair_gross else o.repair_gross end) - (case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
                            and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum((case when o.repair_gross = 0 then i.repair_gross else o.repair_gross end) - (case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                            and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

select 27 as id, 'Average Repair Value Invoiced Exc. LPPs' as title ,
(select coalesce(avg(o.repair_gross), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and c.created_date > claimUploadStart
     and i.repair_net > 0
     and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
     and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_6_months,


(select coalesce(avg(o.repair_gross), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select coalesce(avg(o.repair_gross), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(10,2) from claim c, invoice_original o, invoice i, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = o.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and (params.chorgIds is null or c.chorganisation_id = ANY(params.chorgIds))
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month


from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

select 28 as id, 'Average Repair Value Paid Exc. LPPs' as title ,
(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(10,2) from claim c , invoice i
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id 
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)  
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION

-- Shows the Average Repair Gross (originally billed) plus Average Repair Penalties Paid for invoices marked as Paid by
-- the Insurer for invoices that have moved to 'Payment Received' or 'Manual Invoice Paid', the invoices will be
-- reported in the period/column that the invoices were uploaded into CHOX.
select 29 as id, 'Average Repair Value Invoiced Inc LPPs ' as title ,
(select coalesce(avg(io.repair_gross + (case when i.final_payment is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)), 0)::numeric(10,2) from claim c , invoice i, invoice_original io
   where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(io.repair_gross + (case when i.final_payment is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)), 0)::numeric(10,2) from claim c , invoice i, invoice_original io
   where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(io.repair_gross + (case when i.final_payment is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)), 0)::numeric(10,2) from claim c , invoice i, invoice_original io
   where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(io.repair_gross + (case when i.final_payment is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)), 0)::numeric(10,2) from claim c , invoice i, invoice_original io
   where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)  
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(io.repair_gross + (case when i.final_payment is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)), 0)::numeric(10,2) from claim c , invoice i, invoice_original io
   where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(io.repair_gross + (case when i.final_payment is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)), 0)::numeric(10,2) from claim c , invoice i, invoice_original io
   where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(io.repair_gross + (case when i.final_payment is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)), 0)::numeric(10,2) from claim c , invoice i, invoice_original io
   where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


UNION


select 30 as id, 'Average Repair Value Paid plus Average Repair Penalties Paid' as title ,
(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(10,2) from claim c , invoice i
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id   
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(10,2) from claim c , invoice i
where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and i.repair_net > 0
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.status in ('InvoicePaymentLogged','PaymentReceived','ManualInvoicePaid')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

-- Shows the number of claims moved into 'Claim Closed', 'Claim Rejection Accepted' or 'Invoice Rejection Accepted'
-- reported in the period/column that the claims were uploaded into CHOX.
select 31 as id, 'Volume Rejected/Aborted Claims' as title ,
(select count(*) from claim c
   where (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.created_date > claimUploadStart
      and c.status in ('ClaimClosed','ClaimRejectionAccepted','InvoiceRejectionAccepted')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                             and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select count(*) from claim c
   where (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.created_date > claimUploadStart
      and c.status in ('ClaimClosed','ClaimRejectionAccepted','InvoiceRejectionAccepted')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                             and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select count(*) from claim c
   where (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.created_date > claimUploadStart
      and c.status in ('ClaimClosed','ClaimRejectionAccepted','InvoiceRejectionAccepted')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                             and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*) from claim c
   where (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.created_date > claimUploadStart
      and c.status in ('ClaimClosed','ClaimRejectionAccepted','InvoiceRejectionAccepted')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                             and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select count(*) from claim c
   where (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.created_date > claimUploadStart
      and c.status in ('ClaimClosed','ClaimRejectionAccepted','InvoiceRejectionAccepted')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                             and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select count(*) from claim c
   where (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.created_date > claimUploadStart
      and c.status in ('ClaimClosed','ClaimRejectionAccepted','InvoiceRejectionAccepted')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
                             and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*) from claim c
   where (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and c.created_date > claimUploadStart
      and c.status in ('ClaimClosed','ClaimRejectionAccepted','InvoiceRejectionAccepted')
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                             and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params

UNION

-- shows the average in days between 'Invoice Upload Date' and 'Invoice Payment Logged' for normal invoices and
-- 'Invoice Upload Date' and 'Manual Invoice Paid' for manual invoices. One blended figure based on all relevant invoices.
select 32 as id, 'Average Time To Pay' as title ,
(select avg(at.created_date::Date - i.created_date::Date)::numeric(10,1) from claim c , invoice i, audit_trail at
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and at.claim_id = c.id and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted = false
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                             and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_6_months,

(select avg(at.created_date::Date - i.created_date::Date)::numeric(10,1) from claim c , invoice i, audit_trail at
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and at.claim_id = c.id and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted = false
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                             and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(at.created_date::Date - i.created_date::Date)::numeric(10,1) from claim c , invoice i, audit_trail at
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and at.claim_id = c.id and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted = false
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                             and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(at.created_date::Date - i.created_date::Date)::numeric(10,1) from claim c , invoice i, audit_trail at
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and at.claim_id = c.id and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted = false
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                             and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(at.created_date::Date - i.created_date::Date)::numeric(10,1) from claim c , invoice i, audit_trail at
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and at.claim_id = c.id and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted = false
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                             and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(at.created_date::Date - i.created_date::Date)::numeric(10,1) from claim c , invoice i, audit_trail at
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and at.claim_id = c.id and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted = false
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
                             and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(at.created_date::Date - i.created_date::Date)::numeric(10,1) from claim c , invoice i, audit_trail at
   where c.invoice_id = i.id
      and c.created_date > claimUploadStart
      and at.claim_id = c.id and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted = false
      and (case when array_length(claimType, 1) > 0 then c.claim_type = ANY(claimType) else true end)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = ANY(params.chorgIds) or params.chorgIds is null)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
                             and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month

from (select reportStartDate as startDate, chorgs as chorgIds, insid as insurerId) params


order by id;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION dlg_pilot_report(text, integer, integer[], integer[], text) TO chox_user;
GRANT EXECUTE ON FUNCTION dlg_pilot_report(text, integer, integer[], integer[], text) TO chox_mi;
