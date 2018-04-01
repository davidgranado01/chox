CREATE OR REPLACE FUNCTION removeNotes(claimAge integer)
     RETURNS void AS
$BODY$
DECLARE
     cutOff date;
     BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;

        update comment
            set comment = 'GDPR: notes content has been removed.',
                version = comment.version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        from claim c
        where comment.claim_id = c.id and comment.user_comment = true
          and removed_notes = false and c.hashed = true and c.hashed_date < cutOff;

        update claim
            set removed_notes = true, removed_notes_date = now(), version=version+1, last_modified_date=now(), last_modified_by=999
        where removed_notes = false and hashed = true and hashed_date < cutOff;
     END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION removeNotes(integer) to chox_user;
