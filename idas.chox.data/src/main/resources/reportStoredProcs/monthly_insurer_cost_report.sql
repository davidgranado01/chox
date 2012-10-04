--drop function monthly_insurer_cost_report(text, int, int);
create or replace function monthly_insurer_cost_report
(
   dat text, chorganid int,insid int
)
returns table
(
   Report text,
   last_12_months numeric(10,2),
   current_month numeric(10,2),
   previous_month numeric(10,2),
   previous_2_months numeric(10,2),
   previous_3_months numeric(10,2),
   previous_4_months numeric(10,2),
   previous_5_months numeric(10,2),
   previous_6_months numeric(10,2),
   previous_7_months numeric(10,2),
   previous_8_months numeric(10,2),
   previous_9_months numeric(10,2),
   previous_10_months numeric(10,2),
   previous_11_months numeric(10,2)
)
as $$ DECLARE dat1 date
;
BEGIN dat1 = dat::Date
;
RETURN QUERY

select 'no_claims_uploaded' as title , (select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as current_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) as previous_4_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) as previous_6_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) as previous_7_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) as previous_8_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) as previous_9_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) as previous_10_month,

(select count(*) from claim c
   where (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'no_invoice_uploaded' as title , (select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select count(*)

from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) as previous_6_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) as previous_7_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) as previous_8_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select count(*)
from claim c, invoice i
    where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'no_invoice_paid' as title , (select count(*) from claim c , audit_trail a where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,  


(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,  


(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,  

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month, 

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month, 

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) as previous_4_month,  

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month, 

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) as previous_6_month,  

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) as previous_7_month,  

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) as previous_8_month,  

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) as previous_9_month, 

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) as previous_10_month, 

(select count(*) from claim c , audit_trail a
where a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) as previous_11_month 

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'avg_hire_value' as title , (select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
     and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
     and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
     and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i
   where c.invoice_id = i.id and i.invoice_original_id = o.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'avg_hire_value_paid' as title , (select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
   where c.invoice_id = i.id
    and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id   
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(i.hire_gross)::numeric(8,2) from claim c , invoice i, audit_trail a
where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
      and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'avg_penalty_charged' as title , (select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params

UNION

select 'no_claims_penalty_payment_charged' as title , (select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,

(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month,

(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) as previous_6_month,


(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,


(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,


(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,


(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select count(*) from claim c, invoice i
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.total_penalty_charge > 0.0
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params

UNION

select 'avg_hire_days' as title , (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'avg_total_loss_hire_days' as title , (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'avg_non_total_loss_hire_days' as title , (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'avg_hire_days_paid' as title , (select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params

UNION

select 'avg_total_loss_hire_days_paid' as title , (select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION

select 'avg_non_total_loss_hire_days_paid' as title , (select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu
  where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived')  
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION

select 'avg_hire_rate' as title , (select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i
  where c.invoice_id=i.id and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params

UNION


select 'avg_hire_rate_paid' as title , (select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as previous_2_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,
                                   

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,


(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
  where c.invoice_id = i.id
    and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and not exists ( select * from audit_trail a2 where c.id = a2.claim_id and a2.reverted = false and a2.update_date > a.update_date and a2.new_status = a.new_status )
       and not exists ( select * from audit_trail a3 where a3.original_status = a.new_status and a3.update_date > a.update_date and c.id = a3.claim_id and a3.reverted = false and a3.new_status!='ClaimClosed' and a3.new_status!='PaymentReceived') 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params


UNION


select 'avg_hire_rate_paid' as title , (select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as previous_2_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,
                                    
(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,


(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params

UNION


select 'total_hire_spend' as title , (select sum(i.hire_gross)::numeric(12,2) from claim c , invoice i, audit_trail a
    where c.invoice_id = i.id
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select sum(i.hire_gross)::numeric(12,2) from claim c, invoice i, audit_trail a
    where c.invoice_id = i.id
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
     and (case when params.chorgId = -1 then (c.chorganisation_id in ( select id from chorganisation where insurer_upload_only = false)) else (c.chorganisation_id = params.chorgId) end)
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, chorganid as chorgId, insid as insurerId) params;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION monthly_insurer_cost_report(text, integer, integer) TO chox;
GRANT EXECUTE ON FUNCTION monthly_insurer_cost_report(text, integer, integer) TO chox_user;