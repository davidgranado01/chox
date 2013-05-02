drop function monthly_chox_cost_report_by_team(text, int, int, text);
create or replace function monthly_chox_cost_report_by_team
(
   dat text, choid int ,insid int, teamName text
)
returns table
(
   Report text,
   last_12_months numeric(10,2),
   current_month numeric(10,2),
   previous_month numeric(10,2),
   previous_2_month numeric(10,2),
   previous_3_month numeric(10,2),
   previous_4_month numeric(10,2),
   previous_5_month numeric(10,2),
   previous_6_month numeric(10,2),
   previous_7_month numeric(10,2),
   previous_8_month numeric(10,2),
   previous_9_month numeric(10,2),
   previous_10_month numeric(10,2),
   previous_11_month numeric(10,2)
)
as $$ DECLARE dat1 date
;
BEGIN dat1 = dat::Date
;
RETURN QUERY

select 'no_claims_uploaded' as title ,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as current_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as previous_2_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select count(*) from claim c, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and c.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'no_invoice_uploaded' as title ,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select count(*)
 from claim c, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'no_invoice_paid' as title ,

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,  


(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,  


(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,  

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month, 

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month, 

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) as previous_4_month,  

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month, 

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) as previous_6_month,  

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) as previous_7_month,  

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) as previous_8_month,  

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) as previous_9_month, 

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) as previous_10_month, 

(select count(*) from claim c , audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) as previous_11_month 

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION

select 'avg_hire_value' as title ,

(select avg(o.hire_gross)::numeric(8,2)
from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and i.invoice_original_id = o.id 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(o.hire_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_hire_value_paid' as title ,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id   
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params, workgroup w


UNION

select 'avg_hire_value_paid_plus_avg_hire_penalty_paid' as title ,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id   
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params, workgroup w


UNION

select 'avg_repair_value' as title ,

(select avg(o.repair_gross)::numeric(8,2)
from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and i.invoice_original_id = o.id 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(o.repair_gross)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION

select 'avg_repair_value_paid' as title ,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id   
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params, workgroup w


UNION

select 'avg_repair_value_paid_plus_avg_repair_penalty_paid' as title ,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id   
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end)::numeric(8,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params, workgroup w


UNION


select 'no_claims_penalty_payments_paid' as title ,
(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select count(*) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params


UNION

select 'avg_penalty_paid' as title ,
(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 'avg_hire_penalty_paid' as title ,
(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 'avg_repair_penalty_paid' as title ,
(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION


select 'total_penalty_paid' as title ,
(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(8,2) from claim c, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team like params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 'avg_penalty_charged' as title ,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select avg(i.total_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION

select 'avg_hire_penalty_charged' as title ,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select avg(i.hire_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION

select 'avg_repair_penalty_charged' as title ,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select avg(i.repair_penalty_charge)::numeric(8,2) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION


 select 'no_claims_penalty_payments_charged' as title , (select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,

(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month,

(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) as previous_6_month,


(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,


(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,


(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,


(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select count(*) from claim c, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_hire_days' as title ,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_total_loss_hire_days' as title ,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_non_total_loss_hire_days' as title ,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_hire_days_paid' as title ,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
         and c.workgroup_id = w.id and w.team like params.team
       and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_total_loss_hire_days_paid' as title ,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_non_total_loss_hire_days_paid' as title ,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(vh.days)::numeric(8,2) from claim c, invoice i, vehicle_hire vh, audit_trail a, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_hire_rate' as title ,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(o.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
    and i.invoice_original_id = o.id 
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'avg_hire_rate_paid' as title ,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as previous_2_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,
                                    
(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,


(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
     and a.claim_id = c.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(i.hire_rate_charged_per_day)::numeric(8,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and a.claim_id = c.id
        and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
        and a.reverted = false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
        and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 'total_hire_paid' as title ,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c , invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and a.claim_id = c.id
      and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
      and a.reverted = false
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end)::numeric(12,2) from claim c, invoice i, audit_trail a, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.claim_id = c.id
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team like params.team
       and a.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params
order by title;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION monthly_chox_cost_report_by_team(text, integer, integer, text) TO chox_user;
GRANT EXECUTE ON FUNCTION monthly_chox_cost_report_by_team(text, integer, integer, text) TO chox_mi;