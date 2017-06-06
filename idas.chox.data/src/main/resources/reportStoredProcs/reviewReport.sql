--DROP FUNCTION reviewReport(IN choId INTEGER, days INTEGER, status TEXT, dependsInvUploadDate BOOLEAN, invoice_upload_date_from VARCHAR, invoice_upload_date_to VARCHAR);
CREATE OR REPLACE FUNCTION reviewReport(IN choId INTEGER, days INTEGER, status TEXT, dependsInvUploadDate BOOLEAN, invoice_upload_date_from VARCHAR, invoice_upload_date_to VARCHAR)
  RETURNS TABLE("Supplier Reference" VARCHAR, "Insurer Claim Number" VARCHAR, "Insurer" VARCHAR, "Claim Type" TEXT, "Invoice Upload Date" TIMESTAMP, 
                "Time Since Invoice Upload (Days)" INTEGER, "Current Status" VARCHAR, "Time In Current Status (Days)" INTEGER) AS
$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $5::DATE;
DATE_TO = $6::DATE;

IF ($4) THEN

RETURN QUERY

SELECT
   c.cho_reference AS "Supplier Reference",
   c.claim_number AS "Insurer Claim Number",
   ins.name AS "Insurer",
   getClaimType(c.claim_type) AS "Claim Type",
   i.created_date AS "Invoice Upload Date",
   (current_date - i.created_date::DATE) + 1 AS "Time Since Invoice Upload (Days)",
   a.new_status AS "Current Status",
   (CURRENT_DATE - a.created_date::DATE) + 1 AS "Time In Current Status (Days)"
FROM 
   claim c, 
   Invoice i,
   audit_trail a,
   insurer ins
WHERE 
   c.invoice_id = i.id
   AND c.status = $3
   AND ins.id = c.insurer_id
   AND a.claim_id = c.id
   AND c.chorganisation_id = $1
   AND a.new_status = c.status
   AND a.reverted = false
   AND NOT EXISTS (SELECT * FROM audit_trail a2 WHERE a2.claim_id = a.claim_id AND a2.new_status = a.new_status AND a2.created_date > a.created_date AND a2.reverted = FALSE)
   AND i.created_date BETWEEN DATE_FROM AND DATE_TO
   AND ((CURRENT_DATE - i.created_date::DATE) + 1) >= $2
   ORDER BY ins.name, "Time Since Invoice Upload (Days)" ASC, c.cho_reference;

ELSE 

RETURN QUERY

SELECT 
   c.cho_reference AS "Supplier Reference",
   c.claim_number AS "Insurer Claim Number",
   ins.name AS "Insurer",
   getClaimType(c.claim_type) AS "Claim Type",
   i.created_date AS "Invoice Upload Date",
   (CURRENT_DATE - i.created_date::DATE) + 1 AS "Time Since Invoice Upload (Days)",
   a.new_status AS "Current Status",
   (CURRENT_DATE - a.created_date::DATE) + 1 AS "Time In Current Status (Days)"
FROM 
   claim c, 
   Invoice i,
   audit_trail a,
   insurer ins
WHERE 
   c.invoice_id = i.id
   AND c.status = $3
   AND ins.id = c.insurer_id
   AND a.claim_id = c.id
   AND c.chorganisation_id = $1
   AND a.new_status = c.status
   AND a.reverted = false
   AND NOT EXISTS (SELECT * FROM audit_trail a2 WHERE a2.claim_id = a.claim_id AND a2.new_status = a.new_status AND a2.created_date > a.created_date AND a2.reverted = FALSE)
   AND i.created_date BETWEEN DATE_FROM AND DATE_TO
   AND ((CURRENT_DATE - a.created_date::DATE) + 1) >= $2
   ORDER BY ins.name, "Time In Current Status (Days)" ASC, c.cho_reference;
   
END IF;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
GRANT EXECUTE ON FUNCTION reviewReport(IN choId INTEGER, days INTEGER, status TEXT, dependsInvUploadDate BOOLEAN, invoice_upload_date_from VARCHAR, invoice_upload_date_to VARCHAR) TO chox_user;

