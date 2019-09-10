/*
 * CHOX-726: Notes Report
 *  Example usage:
 *      select * from notesReport(array[6], null::integer[], null::integer[], '2017-01-01', '2017-01-01', null::char(3), array['ClaimClosed','PaymentReceived','ManualInvoicePaid','ClaimRejectionAccepted','InvoiceRejectionAccepted'], array['ClaimRejected']);
 */
DROP FUNCTION notesReport(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN claimUploadDate VARCHAR,
    IN closedClaimDate VARCHAR,
    IN insOrCHO char(3),
    IN closedClaimStatuses VARCHAR[],
    IN openClaimStatuses VARCHAR[]);


CREATE OR REPLACE FUNCTION notesReport(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN claimUploadDate VARCHAR,
    IN closedClaimDate VARCHAR,
    IN insOrCHO char(3),
    IN closedClaimStatuses VARCHAR[],
    IN openClaimStatuses VARCHAR[])

RETURNS TABLE(
                "Supplier Reference" VARCHAR,
                "Insurer Claim Number" VARCHAR,
                "Created By" text,
                "Created Date" timestamp without time zone,
                "Message" VARCHAR
) AS $BODY$
BEGIN
RETURN QUERY

select c.cho_reference, c.claim_number, case when w.hashed then 'GDPR: Data Removed (' ||
        case when ins is null and cho is null then 'System Admin' else case when cho is null then ins.name else cho.name end end || ')'
    else w.first_name || ' ' || w.last_name || ' (' || case when ins is null and cho is null then 'System Admin' else case when cho is null then ins.name else cho.name end end || ')' end,
       co.created_date, case when c.hashed and co.user_comment then 'GDPR: note content removed' else co.comment end
from claim c, comment co, web_user w left outer join chorganisation cho on (w.chorganisation_id=cho.id)
        left outer join insurer ins on (w.insurer_id=ins.id)
where c.id = co.claim_id and co.created_by = w.id and co.reverted=false
    and (insOrCHO is null or (insOrCHO = 'INS' and co.visibility_type != 2) or (insOrCHO = 'CHO' and co.visibility_type != 1))
    and (insIds is null or c.insurer_id = ANY(insIds))
    and (choIds is null or c.chorganisation_id = ANY(choIds))
    and (claimTypes is null or c.claim_type = ANY(claimTypes))
    and c.created_date >= claimUploadDate::Date
    and ((c.status!=ALL(closedClaimStatuses) and (openClaimStatuses is null or c.status!=ALL(openClaimStatuses)))
            or ((c.status=ANY(closedClaimStatuses) or (openClaimStatuses is not null and c.status=ANY(openClaimStatuses))) and c.status_modified_date >= closedClaimDate::Date))
order by c.created_date, c.cho_reference, co.created_date;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


GRANT EXECUTE ON FUNCTION notesReport(
                            IN insIds INTEGER[],
                            IN choIds INTEGER[],
                            IN claimTypes INTEGER[],
                            IN claimUploadDate VARCHAR,
                            IN closedClaimDate VARCHAR,
                            IN insOrCHO char(3),
                            IN closedClaimStatuses VARCHAR[],
                            IN openClaimStatuses VARCHAR[])
TO chox_user;
GRANT EXECUTE ON FUNCTION notesReport(
                            IN insIds INTEGER[],
                            IN choIds INTEGER[],
                            IN claimTypes INTEGER[],
                            IN claimUploadDate VARCHAR,
                            IN closedClaimDate VARCHAR,
                            IN insOrCHO char(3),
                            IN closedClaimStatuses VARCHAR[],
                            IN openClaimStatuses VARCHAR[])
TO chox_mi;



