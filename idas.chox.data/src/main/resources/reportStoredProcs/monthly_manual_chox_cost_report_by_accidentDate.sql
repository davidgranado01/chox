drop function manual_chox_cost_report_ad(text, int[], int);
create or replace function manual_chox_cost_report_ad
(
   dat text, choids int[] ,insid int
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
as $$

DECLARE dat1 date;

BEGIN
    dat1 = dat::Date;
    IF (array_length(choids, 1) = 1 AND choids[1]=-1) THEN
        choids = null;
    END IF;
RETURN QUERY

SELECT 1 as id, 'Average Hire Days Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.incident_id = inc.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
    FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.incident_id = inc.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.incident_id = inc.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.incident_id = inc.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.incident_id = inc.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.incident_id = inc.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.incident_id = inc.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
    FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.incident_id = inc.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION


SELECT 2 as id, 'Average Hire Days Paid' AS title,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION


SELECT 3 as id, 'Average Hire Rate Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' 
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 4 as id, 'Average Hire Rate Paid' AS title,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.hire_rate_charged_per_day), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params


UNION


SELECT 5 as id, 'Average Hire Value Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND c.incident_id = inc.id
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION
SELECT 6 as id, 'Average Hire Value Paid (exc pens)' AS title,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND c.incident_id = inc.id
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.hire_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
          
          UNION

SELECT 7 as id, 'Average Hire Value Paid plus Average Hire Penalties Paid' AS title,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.hire_gross + i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
          
UNION

SELECT 8 as id, 'Average Repair Hire Days Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND c.incident_id = inc.id
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND c.incident_id = inc.id
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 9 as id, 'Average Repair Hire Days Paid' AS title,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.repair_gross > 0
     AND c.vehicle_hire_id = vh.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 10 as id, 'Average Total Loss Hire Days Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 11 as id, 'Average Total Loss Hire Days Paid' AS title,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 12 as id, 'Average Non Total Loss Hire Days Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
    AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND i.hire_net - i.admin_fee > 0
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 13 as id, 'Average Non Total Loss Hire Days Paid' AS title,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(vh.days), 0)::numeric(8,1)
   FROM claim c, invoice i, vehicle_hire vh, customer cu, chorganisation cho, incident inc
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
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params


UNION


SELECT 14 as id, 'Average Penalty Payment' AS title,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
    FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
    FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.total_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 15 as id, 'Average Hire Penalty Payment' AS title,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
    FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
    FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.hire_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 16 as id, 'Average Repair Penalty Payment' AS title,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION


SELECT 17 as id, 'Average Repair Value Invoiced' AS title,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId  OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c , invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END), 0)::numeric(8,2)
   FROM chorganisation cho, claim c, invoice i, incident inc
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 18 as id, 'Average Repair Value Paid (exc pens)' AS title,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.repair_gross), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
          
          UNION

SELECT 19 as id, 'Average Repair Value Paid plus Average Repair Penalties Paid' AS title,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(avg(i.repair_gross + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND i.repair_net > 0
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
          
UNION


SELECT 20 as id, 'Total No. Claims Penalty Payments' AS title,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND i.total_penalty_charge > 0.0
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION

SELECT 21 as id, 'Total No. Claims Uploaded' AS title,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
                            AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
                            AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
                            AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
                            AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c, chorganisation cho, incident inc
   WHERE (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
          
UNION

SELECT 22 as id, 'Total No. Invoices Paid' AS title,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c, audit_trail a, chorganisation cho, incident inc
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params
UNION
          
SELECT 23 as id, 'Total No. Invoices Uploaded' AS title,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.incident_id = inc.id
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params

UNION

select 24 as id, 'Total Hire Paid' AS title,
(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0) from claim c , invoice i
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(case when i.final_payment is not null then (i.hire_gross_paid + i.hire_penalty_charge_paid) else (i.hire_gross + i.hire_penalty_charge) end), 0)
    from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and c.incident_id = inc.id
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and c.status_modified_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params

UNION

select 25 as id, 'Total Hire Value Invoiced' AS title,
(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(io.hire_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choids AS chorgIds, insid as insurerId) params


UNION

select 26 as id, 'Total Hire Value Paid (exc pens)' AS title,
(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choids as chorgIds, insid as insurerId) params

UNION
          
SELECT 27 as id, 'Total Hire Value Paid (inc pens)' AS title,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(sum(i.hire_gross + i.hire_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params

UNION

select 28 as id, 'Total Repair Value Invoiced' AS title,
(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(io.repair_gross), 0) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choids as chorgIds, insid as insurerId) params


UNION

select 29 as id, 'Total Repair Value Paid (exc pens)' AS title,
(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select coalesce(sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END), 0) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choids as chorgIds, insid as insurerId) params

UNION
          
SELECT 30 as id, 'Total Repair Value Paid (inc pens)' AS title,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(sum(i.repair_gross + i.repair_penalty_charge), 0)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params

UNION

select 31 as id, 'Total Repair Value Invoiced' AS title,
(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select sum(io.repair_gross) from claim c , invoice i, invoice_original io, incident inc
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choids as chorgIds, insid as insurerId) params


UNION

select 32 as id, 'Total Repair Value Paid (exc pens)' AS title,
(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select sum(CASE WHEN i.repair_gross_paid is null THEN i.repair_gross ELSE i.repair_gross_paid END) from claim c , invoice i, incident inc
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and c.claim_type IN (10,14,15,16,17)
      and c.incident_id = inc.id
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
      and inc.date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choids as chorgIds, insid as insurerId) params

UNION
          
SELECT 33 as id, 'Total Repair Value Paid (inc pens)' AS title,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT sum(i.repair_gross + i.repair_penalty_charge)::numeric(12,2)
   FROM claim c , invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id = i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params

UNION

SELECT 34 as id, 'Average Repair Duration' AS title,
  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.hire_monitoring_detail_id = hmd.id
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.hire_monitoring_detail_id = hmd.id
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
        AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
        AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
        AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
        AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
        AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
        AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
        AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
        AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
        AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
        AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
        AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(extract(epoch from (hmd.repair_completion_date - hmd.repair_commenced_date))/(3600*24))::numeric(8,1)
   FROM claim c, invoice i, hire_monitoring_detail hmd, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND hmd.repair_commenced_date is not null and hmd.repair_completion_date is not null
     AND c.incident_id = inc.id
     AND c.hire_monitoring_detail_id = hmd.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
        AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month

FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params

UNION

SELECT 35 as id, 'Total Value Of Claims Penalty Payments Paid' AS title,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT coalesce(sum(i.hire_penalty_charge + i.repair_penalty_charge), 0)::numeric(8,2)
   FROM claim c, invoice i, chorganisation cho, incident inc
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND c.incident_id = inc.id
     AND (c.insurer_id = params.insurerId OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0 OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgIds is null OR c.chorganisation_id = ANY(params.chorgIds))
     AND inc.date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choids AS chorgIds,
          insid AS insurerId) params

ORDER BY id;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION manual_chox_cost_report_ad(text, integer[], integer) TO chox_user;
GRANT EXECUTE ON FUNCTION manual_chox_cost_report_ad(text, integer[], integer) TO chox_mi;
