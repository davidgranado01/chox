drop function get_reconstructed_audit_trail(IN claimId INT);

create or replace function get_reconstructed_audit_trail(IN claimId INT)
RETURNS TABLE (
    id integer,
    update_date timestamp without time zone,
    original_status character varying(40),
    new_status character varying(40)
) AS
$BODY$
DECLARE
    auditTrailRecord record;
    newRecord record;
BEGIN

    FOR auditTrailRecord  IN
        select * from audit_trail where claim_id = claimId order by update_date asc, id
    LOOP
        RETURN QUERY
        select auditTrailRecord.id, auditTrailRecord.update_date, auditTrailRecord.original_status, auditTrailRecord.new_status;
        if (auditTrailRecord.reverted and auditTrailRecord.original_status != '') THEN
            RETURN QUERY
            select auditTrailRecord.id, auditTrailRecord.last_modified_date as update_date, auditTrailRecord.new_status as original_status,
                   auditTrailRecord.original_status as new_status;
        END IF;
    END LOOP;
END;
$BODY$
LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION get_reconstructed_audit_trail(IN claimId INT) TO chox_user;
GRANT EXECUTE ON FUNCTION get_reconstructed_audit_trail(IN claimId INT) TO chox_mi;
