#!/bin/bash
PSQL_COMMAND=/usr/bin/psql
HOST=#{DB_HOST} #dev-ukre-pgs28.dc.solera-uk.com
USER=#{DB_USER.CHOX} #chox
DB=#{DB_NAME.CHOX} #dev_uk_chox
MI_DIRECTORY=/home/chox/reports/

echo '***********************************************************'
echo `date`': Generating Weekly Data Dump Reports For DLG'
echo '***********************************************************'

echo 'Generating DLG Weekly Claim Details Dump XML export'
DUMPFILE_ONE='WeeklyReportDataDump-AllAccidentYears-'`date +"%Y%m%d"`.txt
DUMPFILE_ONE_AUX='WeeklyReportDataDump-AllAccidentYears-'`date +"%Y%m%d"`_2.txt
DUMPFILE_TWO='WeeklyReportDataDump-CurrentAccidentYear-'`date +"%Y%m%d"`.txt
DUMPFILE_TWO_AUX='WeeklyReportDataDump-CurrentAccidentYear-'`date +"%Y%m%d"`_2.txt
DUMPFILE_THREE='WeeklyReportDataDump-PriorAccidentYear-'`date +"%Y%m%d"`.txt
DUMPFILE_THREE_AUX='WeeklyReportDataDump-Prior Accident Year-'`date +"%Y%m%d"`_2.txt

OUTPUT_FILE_ONE=${MI_DIRECTORY}${DUMPFILE_ONE}
OUTPUT_FILE_TWO=${MI_DIRECTORY}${DUMPFILE_TWO}
OUTPUT_FILE_THREE=${MI_DIRECTORY}${DUMPFILE_THREE}

OUTPUT_FILE_ONE_AUX=${MI_DIRECTORY}${DUMPFILE_ONE_AUX}
OUTPUT_FILE_TWO_AUX=${MI_DIRECTORY}${DUMPFILE_TWO_AUX}
OUTPUT_FILE_THREE_AUX=${MI_DIRECTORY}${DUMPFILE_THREE_AUX}

$PSQL_COMMAND -h "${HOST}" -U "${USER}" -d "${DB}" -o "${OUTPUT_FILE_ONE}" << --EOF--
select cho.name as "Supplier Name",c.cho_reference as "Supplier Reference" ,i."name" as "Insurer Name" ,
case when c.workgroup_id_original is null then w."name"
else wo."name"
end as "Original Insurer WorkGroup",
c.claim_type as "Claim Type",inc."date" as "Incident Date", c.created_date as "Claim Upload Date",
(select * from (select
el.created_date
as "Paid_Date" from event_log el
where el.event_name='InvoicePaidEvent' and el.claim_id = c.id
group by el.created_date ) g
order by g."Paid_Date" desc limit 1) as "Date Paid",
inv.created_date as "Invoice Upload Date", ihmd.who_managed_repair as "Repair Manager",
io.hire_gross as "Original Hire Gross", inv.hire_gross as "Current Hire Gross",
io.repair_gross as "Original Repair Gross", inv.repair_gross as "Current Repair Gross",
((inv.hire_gross + inv.hire_penalty_charge) * c.percentage_liability_accepted/100.0) as "Hire Gross (Inc LPPs)",
((inv.repair_gross + inv.repair_penalty_charge + inv.engineer_fee_gross)*c.percentage_liability_accepted/100.0) as "Repair Gross (Inc LPPs)",
io.storage_recovery_net as "Original Storage and Recovery", inv.storage_recovery_net as "Current Storage and Recovery",
vh.days_original as "Original Hire Days", vh.days as "Current Hire Days",
io.hire_rate_charged_per_day as "Original Daily Rate", inv.hire_rate_charged_per_day as "Paid Daily Rate",
vc."name" as "Original Hire Vehicle Class", vhn."name" as "Current Hire Vehicle Class"
from claim c
join chorganisation cho on (cho.id = c.chorganisation_id)
join insurer i on i.id=c.insurer_id
join workgroup w on w.id =c.workgroup_id
left join workgroup wo on wo.id = c.workgroup_id_original
join incident inc on inc.id = c.incident_id
join invoice inv on inv.id = c.invoice_id
join invoice_original io on io.id = inv.invoice_original_id
join insurer_hire_monitoring_detail ihmd on ihmd.id = c.insurer_hire_monitoring_detail_id
join vehicle_hire vh on c.vehicle_hire_id = vh.id
join vehicle_class vc on vc.id = vh.vehicle_class_original_id
left outer join vehicle_class vhn on (vhn.id = vh.vehicle_class_original_id )
where i."name" = 'Direct Line Group'
and w."name" not in ('Birmingham Credit Hire Team 3', 'Birmingham Credit Hire Team 5', 'Birmingham Credit Hire Team 6', 'Birmingham Credit Hire Team 9', 'Private Insurance Team 9')
and inc."date" >  current_date - interval '13 month';
--EOF--

echo "Cleaning the file"
cat "${OUTPUT_FILE_ONE}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_ONE_AUX}"
cat "${OUTPUT_FILE_ONE_AUX}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_ONE}"
rm -rf "${OUTPUT_FILE_ONE_AUX}"


$PSQL_COMMAND -h "${HOST}" -U "${USER}" -d "${DB}" -o "${OUTPUT_FILE_TWO}" << --EOF--
select cho.name as "Supplier Name",c.cho_reference as "Supplier Reference" ,i."name" as "Insurer Name" ,
case when c.workgroup_id_original is null then w."name"
else wo."name"
end as "Original Insurer WorkGroup",
c.claim_type as "Claim Type",inc."date" as "Incident Date", c.created_date as "Claim Upload Date",
(select * from (select
el.created_date
as "Paid_Date" from event_log el
where el.event_name='InvoicePaidEvent' and el.claim_id = c.id
group by el.created_date ) g
order by g."Paid_Date" desc limit 1) as "Date Paid",
inv.created_date as "Invoice Upload Date", ihmd.who_managed_repair as "Repair Manager",
io.hire_gross as "Original Hire Gross", inv.hire_gross as "Current Hire Gross",
io.repair_gross as "Original Repair Gross", inv.repair_gross as "Current Repair Gross",
((inv.hire_gross + inv.hire_penalty_charge) * c.percentage_liability_accepted/100.0) as "Hire Gross (Inc LPPs)",
((inv.repair_gross + inv.repair_penalty_charge + inv.engineer_fee_gross)*c.percentage_liability_accepted/100.0) as "Repair Gross (Inc LPPs)",
io.storage_recovery_net as "Original Storage and Recovery", inv.storage_recovery_net as "Current Storage and Recovery",
vh.days_original as "Original Hire Days", vh.days as "Current Hire Days",
io.hire_rate_charged_per_day as "Original Daily Rate", inv.hire_rate_charged_per_day as "Paid Daily Rate",
vc."name" as "Original Hire Vehicle Class", vhn."name" as "Current Hire Vehicle Class"
from claim c
join chorganisation cho on (cho.id = c.chorganisation_id)
join insurer i on i.id=c.insurer_id
join workgroup w on w.id =c.workgroup_id
left join workgroup wo on wo.id = c.workgroup_id_original
join incident inc on inc.id = c.incident_id
join invoice inv on inv.id = c.invoice_id
join invoice_original io on io.id = inv.invoice_original_id
join insurer_hire_monitoring_detail ihmd on ihmd.id = c.insurer_hire_monitoring_detail_id
join vehicle_hire vh on c.vehicle_hire_id = vh.id
join vehicle_class vc on vc.id = vh.vehicle_class_original_id
left outer join vehicle_class vhn on (vhn.id = vh.vehicle_class_original_id )
where i."name" = 'Direct Line Group'
and w."name" not in ('Birmingham Credit Hire Team 3', 'Birmingham Credit Hire Team 5', 'Birmingham Credit Hire Team 6', 'Birmingham Credit Hire Team 9', 'Private Insurance Team 9')
and inc."date" >  current_date - interval '13 month' and date_part('year',inc."date") = date_part('year', now());
--EOF--

echo "Cleaning the file"
cat "${OUTPUT_FILE_TWO}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_TWO_AUX}"
cat "${OUTPUT_FILE_TWO_AUX}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_TWO}"
rm -rf "${OUTPUT_FILE_TWO_AUX}"


$PSQL_COMMAND -h "${HOST}" -U "${USER}" -d "${DB}" -o "${OUTPUT_FILE_THREE}" << --EOF--
select cho.name as "Supplier Name",c.cho_reference as "Supplier Reference" ,i."name" as "Insurer Name" ,
case when c.workgroup_id_original is null then w."name"
else wo."name"
end as "Original Insurer WorkGroup",
c.claim_type as "Claim Type",inc."date" as "Incident Date", c.created_date as "Claim Upload Date",
(select * from (select
el.created_date
as "Paid_Date" from event_log el
where el.event_name='InvoicePaidEvent' and el.claim_id = c.id
group by el.created_date ) g
order by g."Paid_Date" desc limit 1) as "Date Paid",
inv.created_date as "Invoice Upload Date", ihmd.who_managed_repair as "Repair Manager",
io.hire_gross as "Original Hire Gross", inv.hire_gross as "Current Hire Gross",
io.repair_gross as "Original Repair Gross", inv.repair_gross as "Current Repair Gross",
((inv.hire_gross + inv.hire_penalty_charge) * c.percentage_liability_accepted/100.0) as "Hire Gross (Inc LPPs)",
((inv.repair_gross + inv.repair_penalty_charge + inv.engineer_fee_gross)*c.percentage_liability_accepted/100.0) as "Repair Gross (Inc LPPs)",
io.storage_recovery_net as "Original Storage and Recovery", inv.storage_recovery_net as "Current Storage and Recovery",
vh.days_original as "Original Hire Days", vh.days as "Current Hire Days",
io.hire_rate_charged_per_day as "Original Daily Rate", inv.hire_rate_charged_per_day as "Paid Daily Rate",
vc."name" as "Original Hire Vehicle Class", vhn."name" as "Current Hire Vehicle Class"
from claim c
join chorganisation cho on (cho.id = c.chorganisation_id)
join insurer i on i.id=c.insurer_id
join workgroup w on w.id =c.workgroup_id
left join workgroup wo on wo.id = c.workgroup_id_original
join incident inc on inc.id = c.incident_id
join invoice inv on inv.id = c.invoice_id
join invoice_original io on io.id = inv.invoice_original_id
join insurer_hire_monitoring_detail ihmd on ihmd.id = c.insurer_hire_monitoring_detail_id
join vehicle_hire vh on c.vehicle_hire_id = vh.id
join vehicle_class vc on vc.id = vh.vehicle_class_original_id
left outer join vehicle_class vhn on (vhn.id = vh.vehicle_class_original_id )
where i."name" = 'Direct Line Group'
and w."name" not in ('Birmingham Credit Hire Team 3', 'Birmingham Credit Hire Team 5', 'Birmingham Credit Hire Team 6', 'Birmingham Credit Hire Team 9', 'Private Insurance Team 9')
and inc."date" >  current_date - interval '13 month' and date_part('year',inc."date") = date_part('month', now()) - 1;
--EOF--

echo "Cleaning the file"

cat "${OUTPUT_FILE_THREE}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_THREE_AUX}"
cat "${OUTPUT_FILE_THREE_AUX}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_THREE}"
rm -rf "${OUTPUT_FILE_THREE_AUX}"


echo "Finished Generating the Reports.."
