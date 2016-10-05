drop function monthly_chox_cost_report_by_team_ad(text, int, int, text);
create or replace function monthly_chox_cost_report_by_team_ad
(
   dat text, choid int ,insid int, teamName text
)
returns table
(  id           integer,
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

select 1 as id, 'Average Hire Days Invoiced' as title,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 2 as id, 'Average Hire Days Paid' as title,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1)
    from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
    where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 3 as id, 'Average Hire Rate Invoiced' as title,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and i.invoice_original_id = o.id 
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(o.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id 
        and c.incident_id = inc.id
        and i.invoice_original_id = o.id 
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 4 as id, 'Average Hire Rate Paid' as title,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as previous_2_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,
                                    
(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,


(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id = i.id
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION

select 5 as id, 'Average Hire Value Invoiced' as title,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2)
from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
      and c.incident_id = inc.id
      and i.hire_net - i.admin_fee > 0
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and i.invoice_original_id = o.id 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(o.hire_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 6 as id, 'Average Hire Value Paid (exc pens)' as title,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id   
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_gross_paid else i.hire_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params, workgroup w


UNION

select 7 as id, 'Average Hire Value Paid plus Average Hire Penalties Paid' as title,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id   
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.hire_net - i.admin_fee > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params, workgroup w


UNION

select 8 as id, 'Average Repair Hire Days Invoiced' as title,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 9 as id, 'Average Repair Hire Days Paid' as title,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id
        and c.incident_id = inc.id
        and i.repair_gross > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION

select 10 as id, 'Average Total Loss Hire Days Invoiced' as title,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 11 as id, 'Average Total Loss Hire Days Paid' as title,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = true
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 12 as id, 'Average Non Total Loss Hire Days Invoiced' as title,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(case when vh.days_original is not null then vh.days_original else vh.days end), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 13 as id, 'Average Non Total Loss Hire Days Paid' as title,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(vh.days), 0)::numeric(8,1) from claim c, incident inc, invoice i, vehicle_hire vh, customer cu, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.vehicle_hire_id = vh.id  and c.customer_id = cu.id and cu.is_total_loss = false
        and c.incident_id = inc.id
        and i.hire_net - i.admin_fee > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and c.status = 'PaymentReceived'
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 14 as id, 'Average Penalty Payment Charged' as title,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.total_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION

select 15 as id, 'Average Penalty Payment Paid' as title,
(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select coalesce(avg(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 16 as id, 'Average Hire Penalty Payment Charged' as title,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.hire_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION

select 17 as id, 'Average Hire Penalty Payment Paid' as title,
(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select coalesce(avg(case when i.final_payment is not null then i.hire_penalty_charge_paid else i.hire_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 18 as id, 'Average Repair Penalty Payment Charged' as title,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and i.repair_penalty_charge > 0
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION


 select 19 as id, 'Average Repair Penalty Payment Paid' as title,
(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_penalty_charge_paid else i.repair_penalty_charge end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION


select 20 as id, 'Average Repair Value Invoiced' as title,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2)
from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
      and c.incident_id = inc.id
      and i.repair_net > 0
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and i.invoice_original_id = o.id 
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(o.repair_gross), 0)::numeric(8,2) from claim c, incident inc, invoice_original o, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and i.invoice_original_id = o.id 
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION

select 21 as id, 'Average Repair Value Paid (exc pens)' as title,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id   
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and i.repair_net > 0
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(case when i.final_payment is not null then i.repair_gross_paid else i.repair_gross end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params, workgroup w


UNION

select 22 as id, 'Average Repair Value Paid plus Average Repair Penalties Paid' as title,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id   
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(avg(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
 where c.invoice_id = i.id
       and c.incident_id = inc.id
       and i.repair_net > 0
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 23 as id, 'Total No. Claims Penalty Payments Charged' as title,
(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,

(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month,

(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) as previous_6_month,


(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,


(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,


(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,


(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select count(*) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
   where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.status = 'PaymentReceived'
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and i.total_penalty_charge > 0.0
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 24 as id, 'Total No. Claims Penalty Payments Paid' as title,
(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select count(*) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id
       and c.incident_id = inc.id
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params


UNION

select 25 as id, 'Total No. Claims Uploaded' as title,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as current_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as previous_2_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select count(*) from claim c, incident inc, workgroup w, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION


select 26 as id, 'Total No. Invoices Paid' as title,

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,  


(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,  


(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) as previous_month,  

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month, 

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) as previous_3_month, 

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) as previous_4_month,  

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) as previous_5_month, 

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) as previous_6_month,  

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) as previous_7_month,  

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) as previous_8_month,  

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) as previous_9_month, 

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) as previous_10_month, 

(select count(*) from claim c, incident inc, audit_trail a, workgroup w, chorganisation cho
 where a.claim_id = c.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and a.new_status = 'PaymentReceived' and c.status = 'PaymentReceived'
       and a.reverted = false
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) as previous_11_month 

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION


select 27 as id, 'Total No. Invoices Uploaded' as title,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select count(*)
 from claim c, incident inc, invoice i, workgroup w, chorganisation cho
     where c.invoice_id=i.id
        and c.incident_id = inc.id
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION

select 28 as id, 'Total Hire Paid' as title,
(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.workgroup_id = w.id and w.team = params.team
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION

select 29 as id, 'Total Hire Value Invoiced' as title,
(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho    
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(io.hire_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 30 as id, 'Total Hire Value Paid (exc pens)' as title,
(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 31 as id, 'Total Hire Value Paid (inc pens)' as title,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
      and c.incident_id = inc.id
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and c.status = 'PaymentReceived'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION

select 32 as id, 'Total Repair Value Invoiced' as title,
(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho    
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(io.repair_gross), 0) from claim c, incident inc, invoice i, invoice_original io, workgroup w, chorganisation cho
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 33 as id, 'Total Repair Value Paid (exc pens)' as title,
(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c, incident inc, invoice i, workgroup w
    where c.invoice_id = i.id and c.status='PaymentReceived'
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

UNION

select 34 as id, 'Total Repair Value Paid (inc pens)' as title,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
      and c.incident_id = inc.id
      and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
      and c.status = 'PaymentReceived'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team = params.team
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as last_12_months,


(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as current_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,


(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_3_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_5_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,


(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(case when i.final_payment is not null then (i.repair_gross_paid + i.repair_penalty_charge_paid) else (i.repair_gross + i.repair_penalty_charge) end), 0)::numeric(12,2) from claim c, incident inc, invoice i, workgroup w, chorganisation cho
    where c.invoice_id = i.id
       and c.incident_id = inc.id
       and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
       and c.status = 'PaymentReceived'
       and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
       and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
       and c.workgroup_id = w.id and w.team = params.team
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month


from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params

UNION


select 35 as id, 'Average Repair Duration' as title,
(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))   as last_12_months,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                                         and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
                                         and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_7_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_8_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1) from claim c, incident inc, invoice i, hire_monitoring_detail hmd, workgroup w, chorganisation cho
   where c.invoice_id=i.id and c.hire_monitoring_detail_id = hmd.id 
        and c.incident_id = inc.id
        and hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
        and hmd.repair_commenced_date <= hmd.repair_completion_date
        and c.chorganisation_id = cho.id and cho.insurer_upload_only=false
        and (c.insurer_id = params.insurerId or params.insurerId = -1)
        and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
        and c.workgroup_id = w.id and w.team = params.team
        and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insId as insurerId, teamName as team) params


UNION

select 36 as id, 'Total Value Of Claims Penalty Payments Paid' as title,
(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
       and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
       and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
       and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) as previous_2_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
       and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
       and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
       and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
       and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
       and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
       and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
       and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
       and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_10_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end), 0)::numeric(8,2) from claim c, incident inc, invoice i, chorganisation cho, workgroup w
  where c.invoice_id=i.id  
       and c.incident_id = inc.id
       and c.status = 'PaymentReceived'
       and c.workgroup_id = w.id and w.team = params.team
       and (c.insurer_id = params.insurerId or params.insurerId = -1)
       and (((i.final_payment is not null and i.hire_penalty_charge_paid > 0.0) or (i.final_payment is null and i.hire_penalty_charge > 0.0)) or ((i.final_payment is not null and i.repair_penalty_charge_paid > 0.0) or (i.final_payment is null and i.repair_penalty_charge > 0.0)))
       and c.chorganisation_id = cho.id and cho.insurer_upload_only = false and (params.chorgId = -1 or c.chorganisation_id = params.chorgId)
       and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
       and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))   as previous_11_month


from (select dat1 as startDate, choid as chorgId, insid as insurerId, teamName as team) params

order by id;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION monthly_chox_cost_report_by_team_ad(text, integer, integer, text) TO chox_user;
GRANT EXECUTE ON FUNCTION monthly_chox_cost_report_by_team_ad(text, integer, integer, text) TO chox_mi;
