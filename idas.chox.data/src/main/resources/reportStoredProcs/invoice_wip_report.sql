DROP FUNCTION invoiceWipReport(IN choIds INTEGER[], IN insIds INTEGER[], invoice_upload_date_from VARCHAR, invoice_upload_date_to VARCHAR);
CREATE OR REPLACE FUNCTION invoiceWipReport(IN choIds INTEGER[],IN insIds INTEGER[], invoice_upload_date_from VARCHAR, invoice_upload_date_to VARCHAR)
  RETURNS TABLE("Supplier Reference" VARCHAR, "Insurer Claim Number" VARCHAR, "Insurer" VARCHAR, "Invoice Upload Date" TIMESTAMP, 
                "Time Since Invoice Upload (Days)" INTEGER, "Current Status" VARCHAR, "Time In Current Status (Days)" INTEGER, 
                "Days Since Last Action (By CHO)" INTEGER) AS
$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $3::DATE;
DATE_TO = $4::DATE;


RETURN QUERY

SELECT
   c.cho_reference AS "Supplier Reference",
   c.claim_number AS "Insurer Claim Number",
   ins.name AS "Insurer",
   i.created_date AS "Invoice Upload Date",
   (current_date - i.created_date::DATE) + 1 AS "Time Since Invoice Upload (Days)",
   c.status AS "Current Status",
   (CURRENT_DATE - a.created_date::DATE) + 1 AS "Time In Current Status (Days)",
   (CURRENT_DATE - co.created_date::DATE) + 1 AS "Days Since Last Action"
FROM 
   
   invoice i,
   audit_trail a,
   insurer ins,
   claim c LEFT OUTER JOIN comment co
   ON c.id = co.claim_id
WHERE 
   c.invoice_id = i.id
   AND c.status NOT IN ('ClaimClosed', 'PaymentReceived', 'InvoiceRejectionAccepted')
   AND ins.id = c.insurer_id
   AND a.claim_id = c.id
   AND (case when array_length($1, 1) > 0 then c.chorganisation_id = ANY($1) else true end)
   AND (case when array_length($2, 1) > 0  then c.insurer_id = ANY($2) else true end)
   AND a.new_status = c.status
   AND a.reverted = false
   AND i.created_date BETWEEN DATE_FROM AND DATE_TO
   AND co.id = (SELECT id FROM comment co WHERE co.claim_id = c.id 
                AND co.visibility_type = 0 
                AND co.created_by != 999
                AND (co.created_by in (select id from web_user where chorganisation_id IS NOT null))
                AND co.created_date = (select max(created_date) FROM comment WHERE claim_id = c.id 
			    AND (comment NOT LIKE ('%failed to respond to the Subscriber notification within%')
					OR comment NOT LIKE ('%failed to respond to the Fixed Fee notification within%')
					OR comment NOT LIKE ('%Supplier Reference updated from%')
					OR comment NOT LIKE ('%A discount of £%has been applied to%on this invoice based on the discount contract in place%')
					OR comment NOT LIKE ('%Penalty charges have been removed from the invoice as the date from which penalty charges are calculated has been manually updated%')
					OR comment NOT LIKE ('%Insurer Claims Handler is%')
					OR comment NOT LIKE ('%Supplier Claims Handler is%')
					OR comment NOT LIKE ('%Supporting Liability Notes%')
					OR comment NOT LIKE ('%Reason For Rejection%')
					OR comment NOT LIKE ('%Supporting Rejection Notes%')
					OR comment NOT LIKE ('%The claim was marked as %')
					OR comment NOT LIKE ('%A full payment amount of £%')
					OR comment NOT LIKE ('%A payment amount of £%')
					OR comment NOT LIKE ('%Updating interim payments received to £%')
					OR comment NOT LIKE ('%Supporting Notes%')
					OR comment NOT LIKE ('%CHO contact number is%')
					OR comment NOT LIKE ('%This is a supplementary Invoice%')
					OR comment NOT LIKE ('%Claim switched from %')
					OR comment NOT LIKE ('%An interim payment of £%')
					OR comment NOT LIKE ('%Supplier Claim Owner changed from%')
					OR comment NOT LIKE ('%Supplier Claim Owner is %')
					OR comment NOT LIKE ('%Final Review Reason%')
					OR comment NOT LIKE ('%Insurer Claims Handler changed from %')
					OR comment NOT LIKE ('%Claim owner changed from%'))) limit 1)
   ORDER BY ins.name, "Time Since Invoice Upload (Days)" ASC, c.cho_reference;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
GRANT EXECUTE ON FUNCTION invoiceWipReport(IN choIds INTEGER[],IN insIds INTEGER[], invoice_upload_date_from VARCHAR, invoice_upload_date_to VARCHAR) TO chox_user;