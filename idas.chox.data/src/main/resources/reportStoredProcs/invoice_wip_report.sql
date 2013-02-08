CREATE OR REPLACE FUNCTION invoiceWipReport(
    IN choIds INTEGER[],
    IN insIds INTEGER[],
    invoice_upload_date_from VARCHAR,
    invoice_upload_date_to VARCHAR)


RETURNS TABLE("Supplier Reference" VARCHAR,
              "Insurer Claim Number" VARCHAR,
              "Insurer" VARCHAR,
              "Invoice Upload Date" TIMESTAMP, 
              "Time Since Invoice Upload (Days)" INTEGER,
              "Current Status" VARCHAR,
              "Time In Current Status (Days)" INTEGER, 
              "Days Since Last Public Note (By CHO)" INTEGER)
AS

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
   (CURRENT_DATE - c.status_modified_date::DATE) + 1 AS "Time In Current Status (Days)",
   (case when not exists(SELECT * FROM comment co, web_user w, claim c2
                WHERE c2.claim_number = c.claim_number
                  AND c.insurer_id = c2.insurer_id
                  AND co.claim_id = c2.id 
                  AND co.created_by = w.id
                  AND co.visibility_type = 0 
                  AND co.created_by != 999
                  AND co.created_date >= i.created_date
                  AND w.chorganisation_id IS NOT null
                  AND comment NOT LIKE ('Supplier Claims Handler is%')
                  AND comment NOT LIKE ('The claim was marked as %')
                  AND comment NOT LIKE ('Updating interim payments received to')
                  AND comment NOT LIKE ('CHO contact number is%')
                  AND comment NOT LIKE ('Hire rate adjusted from%')
                  AND comment NOT LIKE ('This is a supplementary Invoice%')
                  AND comment NOT LIKE ('Claim switched from%')
                  AND comment NOT LIKE ('An interim payment of £%')
                  AND comment NOT LIKE ('Supplier Claim Owner changed from%')
                  AND comment NOT LIKE ('Supplier Claim Owner is %')
                  AND comment NOT LIKE ('Final Review Reason:%')
                  AND comment NOT LIKE ('%failed to respond to the Subscriber notification within the%')
                  AND comment NOT LIKE ('%failed to respond to the Fixed Fee notification within the%')
                  AND comment NOT LIKE ('Supplier Reference updated from%')
                  AND comment NOT LIKE ('A discount of £% has been applied to the % on this invoice based on the discount contract in place.')
                  AND comment NOT LIKE ('Penalty charges have been removed from the invoice as the date from which penalty charges are calculated has been manually updated.')) then null
    else (select CURRENT_DATE - co.created_date::DATE + 1  FROM comment co
                  WHERE co.id = (select max(co.id) from comment co, web_user w, claim c2
                WHERE c2.claim_number = c.claim_number
                  AND c.insurer_id = c2.insurer_id
                  AND co.claim_id = c2.id 
                  AND co.created_by = w.id
                  AND co.visibility_type = 0 
                  AND co.created_by != 999
                  AND co.created_date >= i.created_date
                  AND w.chorganisation_id IS NOT null
                  AND comment NOT LIKE ('Supplier Claims Handler is%')
                  AND comment NOT LIKE ('The claim was marked as %')
                  AND comment NOT LIKE ('Updating interim payments received to')
                  AND comment NOT LIKE ('CHO contact number is%')
                  AND comment NOT LIKE ('Hire rate adjusted from%')
                  AND comment NOT LIKE ('This is a supplementary Invoice%')
                  AND comment NOT LIKE ('Claim switched from%')
                  AND comment NOT LIKE ('An interim payment of £%')
                  AND comment NOT LIKE ('Supplier Claim Owner changed from%')
                  AND comment NOT LIKE ('Supplier Claim Owner is %')
                  AND comment NOT LIKE ('Final Review Reason:%')
                  AND comment NOT LIKE ('%failed to respond to the Subscriber notification within the%')
                  AND comment NOT LIKE ('%failed to respond to the Fixed Fee notification within the%')
                  AND comment NOT LIKE ('Supplier Reference updated from%')
                  AND comment NOT LIKE ('A discount of £% has been applied to the % on this invoice based on the discount contract in place.')
                  AND comment NOT LIKE ('Penalty charges have been removed from the invoice as the date from which penalty charges are calculated has been manually updated.')))
    end)  AS "Days Since Last Public Note (By CHO)"
FROM 
   invoice i,
   insurer ins,
   claim c
WHERE c.invoice_id = i.id
   AND ins.id = c.insurer_id
   AND c.status NOT IN ('ClaimClosed', 'PaymentReceived', 'InvoiceRejectionAccepted')
   AND (case when array_length($1, 1) > 0 then c.chorganisation_id = ANY($1) else true end)
   AND (case when array_length($2, 1) > 0  then c.insurer_id = ANY($2) else true end)
   AND i.created_date BETWEEN DATE_FROM AND DATE_TO
   ORDER BY ins.name, "Time Since Invoice Upload (Days)" ASC, "Supplier Reference";

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
GRANT EXECUTE ON FUNCTION invoiceWipReport(IN choIds INTEGER[],IN insIds INTEGER[], invoice_upload_date_from VARCHAR, invoice_upload_date_to VARCHAR) TO chox_user;
