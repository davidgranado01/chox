drop function manual_chox_cost_report(text, int, int);
create or replace function manual_chox_cost_report
(
   dat text, choid int ,insid int
)
returns table
(  id               integer,
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

SELECT 1 as id, 'Average Hire Days Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
    FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
    FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION


SELECT 2 as id, 'Average Hire Days Paid' AS title,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION


SELECT 3 as id, 'Average Hire Rate Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' 
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION

SELECT 4 as id, 'Average Hire Rate Paid' AS title,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params


UNION


SELECT 5 as id, 'Average Hire Value Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION
SELECT 6 as id, 'Average Hire Value Paid (exc pens)' AS title,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
          
          UNION

SELECT 7 as id, 'Average Hire Value Paid plus Average Hire Penalties Paid' AS title,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
          
UNION

SELECT 8 as id, 'Average Total Loss Hire Days Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION

SELECT 9 as id, 'Average Total Loss Hire Days Paid' AS title,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION

SELECT 10 as id, 'Average Non Total Loss Hire Days Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
    AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION

SELECT 11 as id, 'Average Non Total Loss Hire Days Paid' AS title,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params


UNION


SELECT 12 as id, 'Average Penalty Payment' AS title,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
    FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
    FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION

SELECT 13 as id, 'Average Hire Penalty Payment' AS title,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
    FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
    FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION

SELECT 14 as id, 'Average Repair Penalty Payment' AS title,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION


SELECT 15 as id, 'Average Repair Value Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId  OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c , invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION

SELECT 16 as id, 'Average Repair Value Paid (exc pens)' AS title,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
          
          UNION

SELECT 17 as id, 'Average Repair Value Paid plus Average Repair Penalties Paid' AS title,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
          
UNION


SELECT 18 as id, 'Total No. Claims Penalty Payments' AS title,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION

SELECT 19 as id, 'Total No. Claims Uploaded' AS title,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
                            AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                            AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                            AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                            AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
          
UNION

SELECT 20 as id, 'Total No. Invoices Paid' AS title,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params
UNION
          
SELECT 21 as id, 'Total No. Invoices Uploaded' AS title,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params

UNION

select 22 as id, 'Total Hire Paid' as title,
(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params

UNION

select 23 as id, 'Total Hire Value Invoiced' as title,
(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId) params


UNION

select 24 as id, 'Total Hire Value Paid (exc pens)' as title,
(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId) params

UNION
          
SELECT 25 as id, 'Total Hire Value Paid (inc pens)' AS title,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params

UNION

select 26 as id, 'Total Repair Value Invoiced' as title,
(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId) params


UNION

select 27 as id, 'Total Repair Value Paid (exc pens)' as title,
(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId) params

UNION
          
SELECT 28 as id, 'Total Repair Value Paid (inc pens)' AS title,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params

UNION

SELECT 29 as id, 'Total Value Of Claims Penalty Payments Paid' AS title,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1 OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId) params

ORDER BY id;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION manual_chox_cost_report(text, integer, integer) TO chox_user;
GRANT EXECUTE ON FUNCTION manual_chox_cost_report(text, integer, integer) TO chox_mi;
