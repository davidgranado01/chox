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
select "Supplier Name", "Supplier Reference","Insurer Reference","Original Insurer Workgroup","Claim Type",to_char("Incident Date",'dd/mm/yyyy hh24:mi'),to_char("Claim Upload Date",'dd/mm/yyyy hh24:mi'),to_char("Invoice Upload Date",'dd/mm/yyyy hh24:mi'),to_char("Date Paid",'dd/mm/yyyy hh24:mi'),
       "Repair Manager", "Original Hire Gross", "Current Hire Gross", "Original Repair Gross", "Current Repair Gross", "Hire Gross (Inc LPPs)", "Repair Gross (Inc LPPs)",
       "Current Storage and Recovery", "Current Hire Days","Paid Daily Rate","Original Hire Vehicle Class","Current Hire Vehicle Class"
from (select cho."name"  as "Supplier Name", c.cho_reference as "Supplier Reference", ins."name" as "Insurer Reference",
case when c.workgroup_id_original is null then w."name"
else wo."name"
end as "Original Insurer Workgroup",
ct.claim_type_string as "Claim Type",inc."date" as "Incident Date", c.created_date as "Claim Upload Date",
inv.created_date as "Invoice Upload Date",
row_number() over (partition by c.id order by el.created_date desc) as "rn",
el.created_date as "Date Paid",
case when c.insurer_hire_monitoring_detail_id is not null then ihmd."who_managed_repair"
else NULL end as "Repair Manager",
io.hire_gross as "Original Hire Gross", inv.hire_gross as "Current Hire Gross",
io.repair_gross as "Original Repair Gross", inv.repair_gross as "Current Repair Gross",
((inv.hire_gross + inv.hire_penalty_charge) * c.percentage_liability_accepted/100.0) as "Hire Gross (Inc LPPs)",
((inv.repair_gross + inv.repair_penalty_charge + inv.engineer_fee_gross)*c.percentage_liability_accepted/100.0) as "Repair Gross (Inc LPPs)",
io.storage_recovery_net as "Original Storage and Recovery", inv.storage_recovery_net as "Current Storage and Recovery",
vh.days_original as "Original Hire Days", vh.days as "Current Hire Days",
io.hire_rate_charged_per_day as "Original Daily Rate", inv.hire_rate_charged_per_day as "Paid Daily Rate",
vc."name" as "Original Hire Vehicle Class", vhn."name" as "Current Hire Vehicle Class"
from claim c
join event_log el on el.claim_id = c.id
join chorganisation cho on cho.id = c.chorganisation_id
join incident inc on inc.id = c.incident_id
join insurer ins on ins.id = c.insurer_id
left join workgroup w on w.id = c.workgroup_id
left join workgroup wo on wo.id = c.workgroup_id_original
join claim_type ct on ct.claim_type = c.claim_type
join invoice inv on inv.id = c.invoice_id
join invoice_original io on io.id = inv.invoice_original_id
join vehicle_hire vh on c.vehicle_hire_id = vh.id
left join vehicle_class vc on vc.id = vh.vehicle_class_original_id
left join vehicle_class vhn on (vhn.id = vh.vehicle_class_original_id )
left join insurer_hire_monitoring_detail ihmd on ihmd.id = c.insurer_hire_monitoring_detail_id
where  el.status in ('InvoicePaymentLogged','ManualInvoicePaid') and event_name='InvoicePaidEvent'
and el.created_date > current_date - interval '13 month'
and ins."name" = 'Direct Line Group'
and w."name" not in ('Birmingham Credit Hire Team 3', 'Birmingham Credit Hire Team 5', 'Birmingham Credit Hire Team 6', 'Birmingham Credit Hire Team 9', 'Private Insurance Team 9')
) reporttable where rn=1
--EOF--

echo "Cleaning the file"
cat "${OUTPUT_FILE_ONE}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_ONE_AUX}"
cat "${OUTPUT_FILE_ONE_AUX}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_ONE}"
rm -rf "${OUTPUT_FILE_ONE_AUX}"


$PSQL_COMMAND -h "${HOST}" -U "${USER}" -d "${DB}" -o "${OUTPUT_FILE_TWO}" << --EOF--
select "Supplier Name", "Supplier Reference","Insurer Reference","Original Insurer Workgroup","Claim Type",to_char("Incident Date",'dd/mm/yyyy hh24:mi'),to_char("Claim Upload Date",'dd/mm/yyyy hh24:mi'),to_char("Invoice Upload Date",'dd/mm/yyyy hh24:mi'),to_char("Date Paid",'dd/mm/yyyy hh24:mi'),
       "Repair Manager", "Original Hire Gross", "Current Hire Gross", "Original Repair Gross", "Current Repair Gross", "Hire Gross (Inc LPPs)", "Repair Gross (Inc LPPs)",
       "Current Storage and Recovery", "Current Hire Days","Paid Daily Rate","Original Hire Vehicle Class","Current Hire Vehicle Class"
from (select cho."name"  as "Supplier Name", c.cho_reference as "Supplier Reference", ins."name" as "Insurer Reference",
case when c.workgroup_id_original is null then w."name"
else wo."name"
end as "Original Insurer Workgroup",
ct.claim_type_string as "Claim Type",inc."date" as "Incident Date", c.created_date as "Claim Upload Date",
inv.created_date as "Invoice Upload Date",
row_number() over (partition by c.id order by el.created_date desc) as "rn",
el.created_date as "Date Paid",
case when c.insurer_hire_monitoring_detail_id is not null then ihmd."who_managed_repair"
else NULL end as "Repair Manager",
io.hire_gross as "Original Hire Gross", inv.hire_gross as "Current Hire Gross",
io.repair_gross as "Original Repair Gross", inv.repair_gross as "Current Repair Gross",
((inv.hire_gross + inv.hire_penalty_charge) * c.percentage_liability_accepted/100.0) as "Hire Gross (Inc LPPs)",
((inv.repair_gross + inv.repair_penalty_charge + inv.engineer_fee_gross)*c.percentage_liability_accepted/100.0) as "Repair Gross (Inc LPPs)",
io.storage_recovery_net as "Original Storage and Recovery", inv.storage_recovery_net as "Current Storage and Recovery",
vh.days_original as "Original Hire Days", vh.days as "Current Hire Days",
io.hire_rate_charged_per_day as "Original Daily Rate", inv.hire_rate_charged_per_day as "Paid Daily Rate",
vc."name" as "Original Hire Vehicle Class", vhn."name" as "Current Hire Vehicle Class"
from claim c
join event_log el on el.claim_id = c.id
join chorganisation cho on cho.id = c.chorganisation_id
join incident inc on inc.id = c.incident_id
join insurer ins on ins.id = c.insurer_id
left join workgroup w on w.id = c.workgroup_id
left join workgroup wo on wo.id = c.workgroup_id_original
join claim_type ct on ct.claim_type = c.claim_type
join invoice inv on inv.id = c.invoice_id
join invoice_original io on io.id = inv.invoice_original_id
join vehicle_hire vh on c.vehicle_hire_id = vh.id
left join vehicle_class vc on vc.id = vh.vehicle_class_original_id
left join vehicle_class vhn on (vhn.id = vh.vehicle_class_original_id )
left join insurer_hire_monitoring_detail ihmd on ihmd.id = c.insurer_hire_monitoring_detail_id
where  el.status in ('InvoicePaymentLogged','ManualInvoicePaid') and event_name='InvoicePaidEvent'
and el.created_date > current_date - interval '13 month'
and ins."name" = 'Direct Line Group'
and date_part('year',inc."date") = date_part('month', now())
and w."name" not in ('Birmingham Credit Hire Team 3', 'Birmingham Credit Hire Team 5', 'Birmingham Credit Hire Team 6', 'Birmingham Credit Hire Team 9', 'Private Insurance Team 9')
) reporttable where rn=1
--EOF--

echo "Cleaning the file"
cat "${OUTPUT_FILE_TWO}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_TWO_AUX}"
cat "${OUTPUT_FILE_TWO_AUX}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_TWO}"
rm -rf "${OUTPUT_FILE_TWO_AUX}"


$PSQL_COMMAND -h "${HOST}" -U "${USER}" -d "${DB}" -o "${OUTPUT_FILE_THREE}" << --EOF--
select "Supplier Name", "Supplier Reference","Insurer Reference","Original Insurer Workgroup","Claim Type",to_char("Incident Date",'dd/mm/yyyy hh24:mi'),to_char("Claim Upload Date",'dd/mm/yyyy hh24:mi'),to_char("Invoice Upload Date",'dd/mm/yyyy hh24:mi'),to_char("Date Paid",'dd/mm/yyyy hh24:mi'),
       "Repair Manager", "Original Hire Gross", "Current Hire Gross", "Original Repair Gross", "Current Repair Gross", "Hire Gross (Inc LPPs)", "Repair Gross (Inc LPPs)",
       "Current Storage and Recovery", "Current Hire Days","Paid Daily Rate","Original Hire Vehicle Class","Current Hire Vehicle Class"
from (select cho."name"  as "Supplier Name", c.cho_reference as "Supplier Reference", ins."name" as "Insurer Reference",
case when c.workgroup_id_original is null then w."name"
else wo."name"
end as "Original Insurer Workgroup",
ct.claim_type_string as "Claim Type",inc."date" as "Incident Date", c.created_date as "Claim Upload Date",
inv.created_date as "Invoice Upload Date",
row_number() over (partition by c.id order by el.created_date desc) as "rn",
el.created_date as "Date Paid",
case when c.insurer_hire_monitoring_detail_id is not null then ihmd."who_managed_repair"
else NULL end as "Repair Manager",
io.hire_gross as "Original Hire Gross", inv.hire_gross as "Current Hire Gross",
io.repair_gross as "Original Repair Gross", inv.repair_gross as "Current Repair Gross",
((inv.hire_gross + inv.hire_penalty_charge) * c.percentage_liability_accepted/100.0) as "Hire Gross (Inc LPPs)",
((inv.repair_gross + inv.repair_penalty_charge + inv.engineer_fee_gross)*c.percentage_liability_accepted/100.0) as "Repair Gross (Inc LPPs)",
io.storage_recovery_net as "Original Storage and Recovery", inv.storage_recovery_net as "Current Storage and Recovery",
vh.days_original as "Original Hire Days", vh.days as "Current Hire Days",
io.hire_rate_charged_per_day as "Original Daily Rate", inv.hire_rate_charged_per_day as "Paid Daily Rate",
vc."name" as "Original Hire Vehicle Class", vhn."name" as "Current Hire Vehicle Class"
from claim c
join event_log el on el.claim_id = c.id
join chorganisation cho on cho.id = c.chorganisation_id
join incident inc on inc.id = c.incident_id
join insurer ins on ins.id = c.insurer_id
left join workgroup w on w.id = c.workgroup_id
left join workgroup wo on wo.id = c.workgroup_id_original
join claim_type ct on ct.claim_type = c.claim_type
join invoice inv on inv.id = c.invoice_id
join invoice_original io on io.id = inv.invoice_original_id
join vehicle_hire vh on c.vehicle_hire_id = vh.id
left join vehicle_class vc on vc.id = vh.vehicle_class_original_id
left join vehicle_class vhn on (vhn.id = vh.vehicle_class_original_id )
left join insurer_hire_monitoring_detail ihmd on ihmd.id = c.insurer_hire_monitoring_detail_id
where  el.status in ('InvoicePaymentLogged','ManualInvoicePaid') and event_name='InvoicePaidEvent'
and el.created_date > current_date - interval '13 month'
and ins."name" = 'Direct Line Group'
and date_part('year',inc."date") = date_part('month', now()) - 1
and w."name" not in ('Birmingham Credit Hire Team 3', 'Birmingham Credit Hire Team 5', 'Birmingham Credit Hire Team 6', 'Birmingham Credit Hire Team 9', 'Private Insurance Team 9')
) reporttable where rn=1
--EOF--

echo "Cleaning the file"

cat "${OUTPUT_FILE_THREE}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_THREE_AUX}"
cat "${OUTPUT_FILE_THREE_AUX}" | sed 's/\(\.[0-9][0-9]\)[0-9]*/\1/g' > "${OUTPUT_FILE_THREE}"
rm -rf "${OUTPUT_FILE_THREE_AUX}"


echo "Finished Generating the Reports.."
