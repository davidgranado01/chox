DROP FUNCTION eracDataExport(IN choIds INTEGER[], IN insIds INTEGER[], IN claimTypes INTEGER[]);

CREATE OR REPLACE FUNCTION eracDataExport(IN choIds INTEGER[], IN insIds INTEGER[], IN claimTypes INTEGER[])

RETURNS TABLE (
    "Supplier Reference" VARCHAR,
    "Claim Type" text,
    "Insurer Claim Number" VARCHAR,
    "Customer VRN" VARCHAR,
    "Status" VARCHAR,
    "Status Modified Date" timestamp without time zone,
    "Liability Status" text
) AS

$BODY$

BEGIN

RETURN QUERY

select c.cho_reference, getClaimType(c.claim_type), c.claim_number, cu.vehicle_registration,
       c.status, c.status_modified_date, getLiabilityStatus(c.liability_status)
from claim c, customer cu
where c.customer_id = cu.id
  and (insIds is null or c.insurer_id = ANY(insIds))
  and (choIds is null or c.chorganisation_id = ANY(choIds))
  and (claimTypes is null or c.claim_type = ANY(claimTypes))
  and c.status not in ('ClaimClosed','ClaimRejectionAccepted','PaymentReceived','InvoiceRejectionAccepted')
order by c.cho_reference;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


GRANT EXECUTE ON FUNCTION eracDataExport(IN choIds INTEGER[], IN insIds INTEGER[], IN claimTypes INTEGER[]) TO chox_user;
GRANT EXECUTE ON FUNCTION eracDataExport(IN choIds INTEGER[], IN insIds INTEGER[], IN claimTypes INTEGER[]) TO chox_mi;

-- select * from eracDataExport(array[1007], null::integer[], null::integer[]);
