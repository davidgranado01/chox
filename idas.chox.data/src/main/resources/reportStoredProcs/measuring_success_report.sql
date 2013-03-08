DROP FUNCTION measuring_success_report(IN insurerid integer, IN startdate text, IN enddate text);

CREATE OR REPLACE FUNCTION measuring_success_report(IN insurerid integer, IN startdate text, IN enddate text)
  RETURNS TABLE("Month" text, "Invoice Rejections" bigint, "Claim Rejections" bigint, "Claim Accepted Cycle Time" numeric, 
                "Insurer Actions" numeric, "Insurer and CHO Actions" numeric, "Penalty Amount" numeric, "Uploaded Claims" bigint, "Uploaded Invoices" bigint) AS
$BODY$

DECLARE

   start_date date;
   end_date date;
   report_end_date date;

BEGIN

   start_date = (SELECT to_date(to_char(startDate::date, 'MM') || '-01-' || to_char(startDate::date, 'yyyy'), 'mm-dd-yyyy'));
   report_end_date = (SELECT to_date(to_char(enddate::date, 'MM') || '-01-' || to_char(enddate::date, 'yyyy'), 'mm-dd-yyyy') + interval '1 month');
   end_date = start_date + interval '1 month';

WHILE start_date < report_end_date LOOP

   
RETURN QUERY
   --------------------   SELECT MONTH -----------------
   SELECT to_char(start_date, 'TMMonth') || ' - ' || to_char(start_date , 'yyyy') AS "Month",

   --------------------   SELECT "Invoice Rejections" -----------------
  (SELECT 
      COUNT(*) AS "Invoice Rejections" 
   FROM 
      claim c
      INNER JOIN audit_trail a ON c.id = a.claim_id
   WHERE 
      a.original_status IN ('InvoiceApprovedByBRE',
                            'InvoiceEscalatedToHandler' ,
                            'InvoiceEscalated',
                            'ContestedInvoiceReferredToInsurer' ,
                            'InvoiceReferredToClaimsHandler',
                            'InvoiceReferredToEngineer')
      AND a.new_status = 'ContestedInvoiceReferredToCHO'
      AND c.insurer_id = insurerid
      AND a.created_date BETWEEN start_date AND end_date
      AND c.claim_type NOT IN (10,14,15,16,17)),
  
  --------------------   SELECT "Claim Rejections" -----------------     
  (SELECT 
      COUNT(*) AS "Claim Rejections" 
   FROM 
      claim c
      INNER JOIN audit_trail a ON c.id = a.claim_id
   WHERE 
      a.original_status IN ('ClaimUnacknowledgedUnrouted',
                            'ClaimUnacknowledgedRouted',
                            'ClaimUnacknowledgedUnassigned',
                            'ClaimUpdatedByEngineer',
                            'ClaimRejectionContested',
                            'ClaimPending')
      AND a.new_status = 'ClaimRejected'
      AND c.insurer_id = insurerid
      AND a.created_date BETWEEN start_date AND end_date
      AND c.claim_type NOT IN (10,14,15,16,17)),

  --------------------   SELECT "Claim Accepted Cycle Time" -----------------
  (SELECT 
      round(AVG(cast(date_part('epoch', a1.created_date - a2.created_date)/(60*60*24.0) AS numeric)),2) AS "Claim Accepted Cycle Time"
   FROM 
      claim c
      INNER JOIN audit_trail a1 ON c.id = a1.claim_id
      INNER JOIN audit_trail a2 ON c.id = a2.claim_id
   WHERE 
      a2.new_status = 'AwaitingCarHireInfo'
      AND a2.reverted = FALSE
      AND a1.new_status = 'InvoicePaymentLogged'
      AND a1.reverted = FALSE
      AND c.insurer_id = insurerid
      AND a1.created_date BETWEEN start_date AND end_date
      AND c.claim_type NOT IN (10,14,15,16,17)),
     
  --------------------   SELECT "Insurer Actions" -----------------             
  (SELECT 
      round(AVG((SELECT
                     COUNT(*)
                 FROM 
                     audit_trail a1
                 WHERE
                     c.id = a1.claim_id
                     AND a1.reverted = false
                     AND a1.original_status IN('ClaimUnacknowledgedUnrouted',
                                          'ClaimUnacknowledgedRouted',
                                          'ClaimRejectionContested', 
                                          'InvoiceApprovedByBRE',
                                          'InvoiceEscalated',
                                          'InvoiceEscalatedToHandler', 
                                          'ContestedInvoiceReferredToInsurer',
                                          'AwaitingInvoicePayment',
                                          'ClaimReferredToEngineer', 
                                          'ClaimReferredToFNOL',
                                          'ClaimPending',
                                          'InvoiceReferredToClaimsHandler',
                                          'ClaimUpdatedByEngineer',
                                          'InvoiceReferredToEngineer',
                                          'ClaimUnacknowledgedUnassigned',
                                          'AwaitingLiabilityResolution', 
                                          'InvoiceUnassigned'))),2) AS "Insurer Actions"
   FROM 
      claim c
      INNER JOIN audit_trail a2 ON c.id = a2.claim_id
   WHERE 
      c.status = 'PaymentReceived'
      AND c.insurer_id = insurerid
      AND a2.new_status = 'PaymentReceived'
      AND a2.reverted = FALSE
      AND a2.created_date BETWEEN start_date AND end_date
      AND c.claim_type NOT IN (10,14,15,16,17)),
     
  --------------------   SELECT "Insurer and CHO Actions" -----------------  
  (SELECT 
         round(AVG((SELECT
                       COUNT(*)
                    FROM 
                       audit_trail a1  
                    WHERE
                       c.id = a1.claim_id
                       AND a1.reverted = FALSE)),2) AS "Insurer and CHO Actions"
   FROM 
        claim c
        INNER JOIN audit_trail a2 ON c.id = a2.claim_id
   WHERE 
        c.status = 'PaymentReceived'
        AND c.insurer_id = insurerid
	AND a2.new_status = 'PaymentReceived'
	AND a2.reverted = FALSE
	AND a2.created_date BETWEEN start_date AND end_date
        AND c.claim_type NOT IN (10,14,15,16,17)),
       
   --------------------   SELECT "Penalty Amount" -----------------               
   (SELECT
        SUM(i.total_penalty_charge) AS "Penalty Amount"
    FROM 
        claim c
        INNER JOIN invoice i ON i.id = c.invoice_id
        INNER JOIN audit_trail a ON a.claim_id = c.id
    WHERE 
        c.status = 'PaymentReceived'
        AND i.total_penalty_charge > 0
        AND c.insurer_id = insurerid
        AND a.new_status = 'PaymentReceived'
        AND a.reverted = FALSE
        AND a.created_date BETWEEN start_date AND end_date
        AND c.claim_type NOT IN (10,14,15,16,17)),
      
   --------------------   SELECT "Uploaded Claims" -----------------        
   (SELECT 
       COUNT(*) AS "Uploaded Claims"
    FROM 
       claim c
    WHERE
       c.created_date BETWEEN start_date AND end_date 
       AND c.insurer_id = insurerid
       AND c.claim_type NOT IN (10,14,15,16,17)),
   
   --------------------   SELECT "Uploaded Invoices" -----------------
   (SELECT 
       COUNT(*) AS "Uploaded Invoices"
    FROM 
       claim c 
       INNER JOIN invoice i ON i.id = c.invoice_id
    WHERE
       i.created_date BETWEEN start_date AND end_date 
       AND c.insurer_id = insurerid
       AND c.claim_type NOT IN (10,14,15,16,17));
       
start_date = end_date;
end_date = end_date + interval '1 month';

END LOOP;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
