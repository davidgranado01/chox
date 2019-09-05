/*
 * CHOX-720: Claim Cycle Report
 *  Example usage:
 *      select * from claimCycleReport(array[6], null::integer[], null::integer[], '2017-01-01', '2017-01-01', array['ClaimClosed','PaymentReceived','ManualInvoicePaid','ClaimRejectionAccepted','InvoiceRejectionAccepted'], array['ClaimRejected']);
 */
DROP FUNCTION claimCycleReport(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN claimUploadDate VARCHAR,
    IN closedClaimDate VARCHAR,
    IN closedClaimStatuses VARCHAR[],
    IN openClaimStatuses VARCHAR[]);


CREATE OR REPLACE FUNCTION claimCycleReport(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN claimUploadDate VARCHAR,
    IN closedClaimDate VARCHAR,
    IN closedClaimStatuses VARCHAR[],
    IN openClaimStatuses VARCHAR[])
RETURNS TABLE(
                "Supplier Reference" VARCHAR,
                "Insurer Claim Number" VARCHAR,
                "Modified Date" timestamp without time zone,
                "Modified By" text,
                "Status" character varying(40),
                "Reverted" text
) AS $BODY$
BEGIN

--select c.id, rat.*
--from claim c, get_reconstructed_audit_trail(c.id) rat
--where c.id=40840 order by c.id, rat.update_date;

RETURN QUERY

select c.cho_reference, c.claim_number, at.created_date, case when w.hashed then 'GDPR: Data Removed (' ||
        case when ins is null and cho is null then 'System Admin' else case when cho is null then ins.name else cho.name end end || ')'
    else w.first_name || ' ' || w.last_name || ' (' || case when ins is null and cho is null then 'System Admin' else case when cho is null then ins.name else cho.name end end || ')' end, at.new_status,
    case when at.reverted then 'Yes' else '' end
from claim c, audit_trail at, web_user w left outer join chorganisation cho on (w.chorganisation_id=cho.id)
        left outer join insurer ins on (w.insurer_id=ins.id)
where c.id = at.claim_id
    and at.created_by = w.id
    and (insIds is null or c.insurer_id = ANY(insIds))
    and (choIds is null or c.chorganisation_id = ANY(choIds))
    and (claimTypes is null or c.claim_type = ANY(claimTypes))
    and c.created_date >= claimUploadDate::Date
    and ((c.status!=ALL(closedClaimStatuses) and c.status!=ALL(openClaimStatuses))
            or ((c.status=ANY(closedClaimStatuses) or (openClaimStatuses is not null and c.status=ANY(openClaimStatuses))) and c.status_modified_date >= closedClaimDate::Date))
order by c.created_date, c.cho_reference, at.last_modified_date;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


GRANT EXECUTE ON FUNCTION claimCycleReport(
                            IN insIds INTEGER[],
                            IN choIds INTEGER[],
                            IN claimTypes INTEGER[],
                            IN claimUploadDate VARCHAR,
                            IN closedClaimDate VARCHAR,
                            IN closedClaimStatuses VARCHAR[],
                            IN openClaimStatuses VARCHAR[])
TO chox_user;
GRANT EXECUTE ON FUNCTION claimCycleReport(
                            IN insIds INTEGER[],
                            IN choIds INTEGER[],
                            IN claimTypes INTEGER[],
                            IN claimUploadDate VARCHAR,
                            IN closedClaimDate VARCHAR,
                            IN closedClaimStatuses VARCHAR[],
                            IN openClaimStatuses VARCHAR[])
TO chox_mi;
