drop function get_days_in_status(IN claimId INT, IN statuses CHARACTER VARYING(40)[]);

create or replace function get_days_in_status(IN claimId INT, IN statuses CHARACTER VARYING(40)[])
RETURNS INT AS
$BODY$
DECLARE
    days INT;
    lastDayCounted INT;
    dayInstatus INT;
    dayOutstatus INT;
    previousStatus CHARACTER VARYING(40);
    statusStart timestamp without time zone;
    auditTrailRecord record;
BEGIN
    days = 0;
    lastDayCounted = 0;
    previousStatus = '';
    statusStart = null;

    FOR auditTrailRecord  IN
        select * from get_reconstructed_audit_trail(claimId) order by update_date, id
    LOOP
        IF (statusStart is null and statuses @> ARRAY[auditTrailRecord.new_status]) THEN
            statusStart = auditTrailRecord.update_date;
--            RAISE NOTICE 'Status % start at %', auditTrailRecord.new_status, auditTrailRecord.update_date;
        ELSEIF (statusStart is not null and not statuses @> ARRAY[auditTrailRecord.new_status]) THEN
            -- Determine time in status
            dayInStatus = date_part('doy', statusStart);
            dayOutStatus = date_part('doy', auditTrailRecord.update_date);
            IF (not (lastDayCounted = dayInStatus and dayInStatus = dayOutStatus)) THEN
                days = days + (auditTrailRecord.update_date::date - statusStart::date) + 1;
--                RAISE NOTICE 'In status for %', date_part('doy', auditTrailRecord.update_date) - dayInStatus + 1;
            END IF;
            lastDayCounted = dayOutStatus;
            statusStart = null;
--        ELSE
--            RAISE NOTICE 'Nothing to do for status % (statusStart=%)', auditTrailRecord.new_status,statusStart;
        END IF;
--        RAISE NOTICE 'Total Days: %', days;
    END LOOP;

    IF (statusStart is not null) THEN
        -- We must currently be in the status, so count days until now()
        days = days + (now()::date - statusStart::date);
        dayInStatus = date_part('doy', statusStart);
--        RAISE NOTICE 'dayInStatus=%, statusStart=%, days added=%', dayInStatus, statusStart, (now()::date - statusStart::date);
        IF (lastDayCounted != dayInStatus) THEN
            days = days +  1;
--            RAISE NOTICE '1 day added';
        END IF;
    END IF;

    RETURN days;
END;
$BODY$
LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION get_days_in_status(IN claimId INT, IN statuses CHARACTER VARYING(40)[]) TO chox_user;
GRANT EXECUTE ON FUNCTION get_days_in_status(IN claimId INT, IN statuses CHARACTER VARYING(40)[]) TO chox_mi;

-- select * from get_days_in_status(191138, '{"ClaimUnacknowledgedUnassigned","ClaimUnacknowledgedUnrouted","ClaimUnacknowledgedRouted","ClaimPending","ClaimReferredToFNOL","ClaimReferredToEngineer","ClaimUpdatedByEngineer","ClaimRejectionContested"}');
