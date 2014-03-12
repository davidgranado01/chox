-- DROP FUNCTION monthlyBREReviewReportLine(IN start_date text, IN end_date text, IN claimTypes integer[], IN choIds  integer[], IN insIds integer[]);
-- select * from monthlyBREReviewReportLine('2011-01-01','2014-01-01', array[]::integer[], array[]::integer[], array[6]);
CREATE OR REPLACE FUNCTION monthlyBREReviewReportLine(
    IN start_date text, 
    IN end_date text, 
    IN claimTypes integer[], 
    IN choIds  integer[],
    IN insIds integer[])

RETURNS TABLE(
    label text, 
    "No. Inv Presented" BIGINT,    
    "No. Inv Settled" BIGINT,
    "No. Inv Passed BRE" BIGINT,
    "Total. Uplift/Reduction Inv Passed BRE" NUMERIC,
    "Avg. Uplift/Reduction For Inv Passed BRE" NUMERIC,
    "No. Inv Passed BRE & Disputed" BIGINT,
    "No. Additional Touch Points For Inv Passed BRE" BIGINT,
    "No. Inv Passed BRE & Disputed & Resulted No Change" BIGINT,
    "No. Inv Passed BRE & Disputed & Resulted Uplift/Reduction" BIGINT,
    "Total. Uplift/Reduction Inv Passed BRE & Disputed" NUMERIC,
    "Total. Penalty Charges Paid Inv Passed BRE & Disputed" NUMERIC,
    "Avg. Uplift/Reduction Inv Passed BRE & Disputed" NUMERIC,
    "No. Inv Passed BRE & Not Disputed" BIGINT,
    "No. Inv Passed BRE & Not Disputed & Resulted No Change" BIGINT,
    "No. Inv Passed BRE & Not Disputed & Resulted Uplift/Reduction" BIGINT,
    "Total. Uplift/Reduction Inv Passed BRE & Not Disputed" NUMERIC,
    "Total. Penalty Charges Paid Inv Passed BRE & Not Disputed" NUMERIC,
    "Avg. Uplift/Reduction Inv Passed BRE & Not Disputed" NUMERIC,
    "No. Inv Failed BRE" BIGINT,
    "Total. Uplift/Reduction Inv Failed BRE" NUMERIC,
    "Avg. Uplift/Reduction Inv Failed BRE" NUMERIC,
    "No. Inv Failed BRE & Disputed" BIGINT,
    "No. Additional Touch Points For Inv Failed BRE" BIGINT,
    "No. Inv Failed BRE & Disputed & Resulted No Change" BIGINT,
    "No. Inv Failed BRE & Disputed & Resulted Uplift/Reduction" BIGINT,
    "Total. Uplift/Reduction Inv Failed BRE & Disputed" NUMERIC,
    "Total. Penalty Charges Paid Inv Failed BRE & Disputed" NUMERIC,
    "Avg. Uplift/Reduction Inv Failed BRE & Disputed" NUMERIC,
    "No. Inv Failed BRE & Not Disputed" BIGINT,
    "No. Inv Failed BRE & Not Disputed & Resulted No Change" BIGINT,
    "No. Inv Failed BRE & Not Disputed & Resulted Uplift/Reduction" BIGINT,
    "Total. Uplift/Reduction Inv Failed BRE & Not Disputed" NUMERIC,
    "Total. Penalty Charges Paid Inv Failed BRE & Not Disputed" NUMERIC,
    "Avg. Uplift/Reduction Inv Failed BRE & Not Disputed" NUMERIC)

AS

$BODY$
DECLARE
    monthlyBreakDownDatesRecord RECORD;
BEGIN
      FOR monthlyBreakDownDatesRecord IN SELECT * FROM breakDownDatesByMonthly(start_date, end_date) LOOP -- loop through each row
      
          RETURN QUERY
  
              SELECT monthlyBreakDownDatesRecord.month_label,
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------              
              (SELECT 
                    COUNT(*) AS "No. Inv Presented"
               FROM 
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Settled"
               FROM 
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Passed BRE"
               FROM 
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    SUM(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Total. Uplift/Reduction Inv Passed BRE"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    AVG(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Avg. Uplift/Reduction For Inv Passed BRE"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Passed BRE & Disputed"
               FROM 
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Additional Touch Points For Inv Passed BRE"
               FROM 
                    audit_trail a,
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND a.claim_id = c.id
                    AND a.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                    AND a.reverted = FALSE
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a1.reverted = FALSE AND a1.created_date < a.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Passed BRE & Disputed & Resulted No Change"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND ((io.total_to_pay != 0.0 and abs(io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) <= 1.00) or (io.total_to_pay = 0.0 and abs(io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) <= 1.00))
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Passed BRE & Disputed & Resulted Uplift/Reduction"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND ((io.total_to_pay != 0.0 and abs(io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) > 1.00) or (io.total_to_pay = 0.0 and abs(io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) > 1.00))
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT
                    SUM(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Total. Uplift/Reduction Inv Passed BRE & Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT
                    SUM(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(15,2) AS "Total. Penalty Charges Paid Inv Passed BRE & Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    AVG(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Avg. Uplift/Reduction Inv Passed BRE & Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Passed BRE & Not Disputed"
               FROM 
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Passed BRE & Not Disputed & Resulted No Change"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND ((io.total_to_pay != 0.0 and abs(io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) <= 1.00) or (io.total_to_pay = 0.0 and abs(io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) <= 1.00))
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Passed BRE & Not Disputed & Resulted Uplift/Reduction"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND ((io.total_to_pay != 0.0 and abs(io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) > 1.00) or (io.total_to_pay = 0.0 and abs(io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) > 1.00))
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    SUM(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Total. Uplift/Reduction Inv Passed BRE & Not Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    SUM(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(15,2) AS "Total. Penalty Charges Paid Inv Passed BRE & Not Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    AVG(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Avg. Uplift/Reduction Inv Passed BRE & Not Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBREApproved', 'InvoiceApprovedByBRE')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Failed BRE"
               FROM 
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    SUM(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Total. Uplift/Reduction Inv Failed BRE"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    AVG(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Avg. Uplift/Reduction Inv Failed BRE"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Failed BRE & Disputed"
               FROM 
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Additional Touch Points For Inv Failed BRE"
               FROM 
                    audit_trail a,
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND a.claim_id = c.id
                    AND a.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                    AND a.reverted = FALSE
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a1.reverted = FALSE AND a1.created_date < a.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Failed BRE & Disputed & Resulted No Change"
               FROM 
                    invoice i, 
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND ((io.total_to_pay != 0.0 and abs(io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) <= 1.00) or (io.total_to_pay = 0.0 and abs(io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) <= 1.00))
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Failed BRE & Disputed & Resulted Uplift/Reduction"
               FROM 
                    invoice i, 
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND ((io.total_to_pay != 0.0 and abs(io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) > 1.00) or (io.total_to_pay = 0.0 and abs(io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) > 1.00))
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    SUM(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Total. Uplift/Reduction Inv Failed BRE & Disputed"
               FROM 
                    invoice i, 
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id 
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    SUM(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(15,2) AS "Total. Penalty Charges Paid Inv Failed BRE & Disputed"
               FROM 
                    invoice i, 
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id 
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    AVG(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Avg. Uplift/Reduction Inv Failed BRE & Disputed"
               FROM 
                    invoice i, 
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id 
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Failed BRE & Not Disputed"
               FROM 
                    invoice i, 
                    claim c 
               WHERE 
                    c.invoice_id = i.id
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Failed BRE & Not Disputed & Resulted No Change"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND ((io.total_to_pay != 0.0 and abs(io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) <= 1.00) or (io.total_to_pay = 0.0 and abs(io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) <= 1.00))
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    COUNT(*) AS "No. Inv Failed BRE & Not Disputed & Resulted Uplift/Reduction"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND ((io.total_to_pay != 0.0 and abs(io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) > 1.00) or (io.total_to_pay = 0.0 and abs(io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 - i.total_to_pay) > 1.00))
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    SUM(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Total. Uplift/Reduction Inv Failed BRE & Not Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id 
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    SUM(case when i.final_payment is not null then (i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) else (i.hire_penalty_charge + i.repair_penalty_charge) end)::numeric(15,2) AS "Total. Penalty Charges Paid Inv Failed BRE & Not Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id 
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date)),
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
              (SELECT 
                    AVG(CASE WHEN io.total_to_pay=0.0 THEN io.full_total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 ELSE io.total_to_pay*(CASE WHEN c.claim_type in (10,14,15,16,17) and c.liability_status in (3,2) THEN 100.0 ELSE c.percentage_liability_accepted END)/100.0 END - i.total_to_pay)::numeric(15,2) AS "Avg. Uplift/Reduction Inv Failed BRE & Not Disputed"
               FROM 
                    invoice i,
                    invoice_original io,
                    claim c 
               WHERE 
                    c.invoice_id = i.id 
                    AND c.status IN ('PaymentReceived', 'ManualInvoicePaid')
                    AND i.invoice_original_id = io.id
                    AND (CASE WHEN array_length(choIds, 1) > 0 THEN c.chorganisation_id = ANY(choIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(insIds, 1) > 0  THEN c.insurer_id = ANY(insIds) ELSE TRUE END)
                    AND (CASE WHEN array_length(claimTypes, 1) > 0 THEN c.claim_type = ANY(claimTypes) ELSE TRUE END)
                    AND i.created_date BETWEEN monthlyBreakDownDatesRecord.month_start_date AND monthlyBreakDownDatesRecord.month_end_date
                    AND EXISTS (SELECT * FROM audit_trail a WHERE a.claim_id = c.id AND a.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler') AND a.reverted = FALSE)
                    AND NOT EXISTS (SELECT 
                                        * 
                                  FROM 
                                        audit_trail a1, 
                                        audit_trail a2 
                                  WHERE 
                                        a1.claim_id = c.id 
                                        AND a1.claim_id = a2.claim_id 
                                        AND a1.new_status IN ('ManualInvoiceBRERejected', 'InvoiceEscalated', 'InvoiceEscalatedToHandler')
                                        AND a1.reverted = FALSE
                                        AND a2.new_status IN ('ManualInvoiceContested', 'ContestedInvoiceReferredToCHO')
                                        AND a2.reverted = FALSE
                                        AND a1.created_date < a2.created_date));
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
      END LOOP;
END;

$BODY$
LANGUAGE plpgsql VOLATILE COST 100;

GRANT EXECUTE ON FUNCTION monthlyBREReviewReportLine(IN start_date text, IN end_date text, IN claimTypes integer[], IN choIds  integer[], IN insIds integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION monthlyBREReviewReportLine(IN start_date text, IN end_date text, IN claimTypes integer[], IN choIds  integer[], IN insIds integer[]) TO chox_mi;
