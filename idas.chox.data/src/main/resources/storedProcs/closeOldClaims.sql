CREATE OR REPLACE FUNCTION closeOldClaims(claimAge integer)
    RETURNS void AS
$BODY$
DECLARE
    cutOff date;
       claim_curs CURSOR(cutOff date) for
          SELECT * FROM claim WHERE created_date < cutOff and status not in ('PaymentReceived', 'ManualInvoicePaid', 'ClaimClosed', 'ClaimRejectionAccepted', 'InvoiceRejectionAccepted');
       claim_row RECORD;
    BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;
        OPEN claim_curs(cutOff);

        LOOP
            FETCH claim_curs INTO claim_row;
            EXIT WHEN NOT FOUND;

            insert into audit_trail(update_date, user_id, claim_id, original_status, new_status, created_by, last_modified_by, created_date, last_modified_date, version)
                select now(), 999, claim_row.id, claim_row.status, 'ClaimClosed', 999, 999, now(), now(), 0;

            update claim
                set previous_status = status,
                    status = 'ClaimClosed',
                    status_modified_date = now(),
                    last_modified_by=999,
                    last_modified_date = now(),
                    version = version+1
            where current of claim_curs;

            -- auto-complete open tasks
            update task
                set auto_completed=true,
                    complete=true,
                    completed_by=999,
                    last_modified_by=999,
                    last_modified_date=now(),
                    version=version+1
            where claim_id = claim_row.id and complete=false;

            -- add note
            insert into comment(claim_id, created_by, created_date, last_modified_by, last_modified_date, visibility_type, version, raised_by, comment)
                select claim_row.id, 999, now(), 999, now(), 0, 0, 999, 'Claim Closed: Claim closed automatically due to still being open ' || $1 || ' days after creation date.';

            --
            -- Add event to event log
            --
            insert into event_log(version, activity_name, event_name, status, claim_id, chorganisation_id, insurer_id, claim_type, created_by, created_date, last_modified_by, last_modified_date)
                select 0, 'GDPR', 'ClaimClosedEvent', claim_row.status, claim_row.id, claim_row.chorganisation_id, claim_row.insurer_id, claim_row.claim_type, 999, now(), 999, now();

        END LOOP;

        CLOSE claim_curs;

    END;
$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION closeOldClaims(integer) to chox_user;
