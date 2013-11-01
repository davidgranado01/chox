drop function manual_chox_cost_report_by_team(text, int, int, text);
create or replace function manual_chox_cost_report_by_team
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

SELECT 'no_claims_uploaded' AS title ,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c,
        chorganisation cho,
        workgroup w
   WHERE (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND c.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
          
UNION
          
SELECT 'no_invoice_uploaded' AS title ,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'no_invoice_paid' AS title ,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c ,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_hire_value' AS title ,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_gross ELSE o.hire_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION
SELECT 'avg_hire_value_paid' AS title ,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.hire_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
          
          UNION

SELECT 'avg_hire_value_paid_plus_avg_hire_penalty_paid' AS title ,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.hire_gross + i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
          
UNION

select 'total_hire_value_invoiced' as title ,
(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id 
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select sum(io.hire_gross) from claim c , invoice i, invoice_original io, workgroup w
    where c.invoice_id = i.id and i.invoice_original_id = io.id
      and c.claim_type IN (10,14,15,16,17)
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId,
          teamName AS team) params

UNION

select 'total_hire_value_paid_exc_pens' as title,
(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as last_12_months,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')
      and to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as current_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month'
      and to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy'))  as previous_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months'
      and to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy'))  as previous_2_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months'
      and to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_3_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months'
      and to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_4_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months'
      and to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_5_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months'
      and to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_6_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months'
      and to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_7_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months'
      and to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_8_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months'
      and to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_9_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months'
      and to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_10_month,

(select sum(CASE WHEN i.hire_gross_paid is null THEN i.hire_gross ELSE i.hire_gross_paid END) from claim c , invoice i, workgroup w
    where c.invoice_id = i.id and c.status='ManualInvoicePaid'
      and (c.insurer_id = params.insurerId or params.insurerId = -1)                                        
      and (c.chorganisation_id = params.chorgId or params.chorgId = -1)
      and c.workgroup_id = w.id and w.team like params.team
      and i.created_date between to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months'
      and to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy'))  as previous_11_month

from (select dat1 as startDate, choid as chorgId, insid as insurerId,
          teamName AS team) params

UNION

SELECT 'avg_repair_value' AS title ,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.repair_gross ELSE o.repair_gross END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id = i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_repair_value_paid' AS title ,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.repair_gross)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
          
          UNION

SELECT 'avg_repair_value_paid_plus_avg_repair_penalty_paid' AS title ,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.repair_gross + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
          
          UNION


SELECT 'no_claims_penalty_payments_paid' AS title ,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.status = 'ManualInvoicePaid'
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_penalty_paid' AS title ,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_hire_penalty_charged' AS title ,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.hire_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_repair_penalty_charged' AS title ,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND cho.insurer_upload_only = TRUE
     AND c.workgroup_id = w.id AND w.team like params.team
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION


SELECT 'total_penalty_paid' AS title ,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT sum(i.hire_penalty_charge + i.repair_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND (i.hire_penalty_charge > 0.0
          OR i.repair_penalty_charge > 0.0)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params

UNION
          
SELECT 'avg_penalty_charged' AS title ,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.total_penalty_charge)::numeric(8,2)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND i.total_penalty_charge > 0
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION
SELECT 'no_claims_penalty_payments_charged' AS title ,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT count(*)
   FROM claim c,
        invoice i,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.status = 'ManualInvoicePaid'
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.total_penalty_charge > 0.0
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_hire_days' AS title ,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.chorganisation_id = cho.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION
SELECT 'avg_total_loss_hire_days' AS title ,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION
SELECT 'avg_non_total_loss_hire_days' AS title ,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(CASE WHEN vh.days_original IS NOT NULL THEN vh.days_original ELSE vh.days END)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_hire_days_paid' AS title ,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION
SELECT 'avg_total_loss_hire_days_paid' AS title ,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = TRUE
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_non_total_loss_hire_days_paid' AS title ,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(vh.days)::numeric(8,2)
   FROM claim c,
        invoice i,
        vehicle_hire vh,
        audit_trail a,
        customer cu,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id=i.id
     AND c.vehicle_hire_id = vh.id
     AND c.customer_id = cu.id
     AND cu.is_total_loss = FALSE
     AND c.chorganisation_id = cho.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION
SELECT 'avg_hire_rate' AS title ,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(CASE WHEN o IS NULL THEN i.hire_rate_charged_per_day ELSE o.hire_rate_charged_per_day END)::numeric(8,2)
   FROM chorganisation cho,
        claim c,
        workgroup w,
        invoice i
   LEFT JOIN invoice_original o ON i.invoice_original_id = o.id
   WHERE c.invoice_id=i.id
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.claim_type IN (10,14,15,16,17)
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION

SELECT 'avg_hire_rate_paid' AS title ,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT avg(i.hire_rate_charged_per_day)::numeric(8,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND i.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
UNION
SELECT 'total_hire_paid' AS title ,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS last_12_months,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') AND to_date(to_char(params.startDate + interval '1 month', 'MM') || '-01-' || to_char(params.startDate + interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS current_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '1 month' AND to_date(to_char(params.startDate , 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy')) AS previous_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '2 months' AND to_date(to_char(params.startDate - interval '1 month' , 'MM') || '-01-' || to_char(params.startDate - interval '1 month', 'yyyy'), 'mm-dd-yyyy')) AS previous_2_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '3 months' AND to_date(to_char(params.startDate - interval '2 months' , 'MM') || '-01-' || to_char(params.startDate - interval '2 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_3_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '4 months' AND to_date(to_char(params.startDate - interval '3 months' , 'MM') || '-01-' || to_char(params.startDate - interval '3 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_4_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '5 months' AND to_date(to_char(params.startDate - interval '4 months' , 'MM') || '-01-' || to_char(params.startDate - interval '4 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_5_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '6 months' AND to_date(to_char(params.startDate - interval '5 months' , 'MM') || '-01-' || to_char(params.startDate - interval '5 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_6_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '7 months' AND to_date(to_char(params.startDate - interval '6 months' , 'MM') || '-01-' || to_char(params.startDate - interval '6 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_7_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '8 months' AND to_date(to_char(params.startDate - interval '7 months' , 'MM') || '-01-' || to_char(params.startDate - interval '7 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_8_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '9 months' AND to_date(to_char(params.startDate - interval '8 months' , 'MM') || '-01-' || to_char(params.startDate - interval '8 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_9_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '10 months' AND to_date(to_char(params.startDate - interval '9 months' , 'MM') || '-01-' || to_char(params.startDate - interval '9 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_10_month,

  (SELECT sum(i.hire_gross + i.hire_penalty_charge)::numeric(12,2)
   FROM claim c,
        invoice i,
        audit_trail a,
        chorganisation cho,
        workgroup w
   WHERE c.invoice_id = i.id
     AND a.claim_id = c.id
     AND a.new_status = 'ManualInvoicePaid'
     AND c.status = 'ManualInvoicePaid'
     AND a.reverted = FALSE
     AND (c.insurer_id = params.insurerId
          OR params.insurerId = -1)
     AND c.chorganisation_id = cho.id
     AND c.workgroup_id = w.id AND w.team like params.team
     AND cho.insurer_upload_only = TRUE
     AND (params.chorgId = -1
          OR c.chorganisation_id = params.chorgId)
     AND a.created_date BETWEEN to_date(to_char(params.startDate, 'MM') || '-01-' || to_char(params.startDate, 'yyyy'), 'mm-dd-yyyy') - interval '11 months' AND to_date(to_char(params.startDate - interval '10 months' , 'MM') || '-01-' || to_char(params.startDate - interval '10 months', 'yyyy'), 'mm-dd-yyyy')) AS previous_11_month
FROM
  (SELECT dat1 AS startDate,
          choid AS chorgId,
          insid AS insurerId,
          teamName AS team) params
ORDER BY title;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION manual_chox_cost_report_by_team(text, integer, integer, text) TO chox_user;
GRANT EXECUTE ON FUNCTION manual_chox_cost_report_by_team(text, integer, integer, text) TO chox_mi;
