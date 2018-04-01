CREATE OR REPLACE FUNCTION removeAttachments(claimAge integer)
    RETURNS void AS
$BODY$
DECLARE
    cutOff date;
    BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;

        update attachment_file
            set file_buffer = null,
                version = attachment_file.version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        from claim c, attachment a
        where attachment_file.attachment_id = a.id and a.claim_id = c.id
          and removed_attachments = false and a.removed = false and c.hashed = true and c.hashed_date < cutOff;

        update attachment
            set removed = true,
                remarks='GDPR: attachment removed.',
                version = attachment.version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        from claim c
        where attachment.claim_id = c.id
          and removed_attachments = false and c.hashed = true and c.hashed_date < cutOff;

        update claim
            set removed_attachments = true, removed_attachments_date = now(), version=version+1, last_modified_date=now(), last_modified_by=999
        where removed_attachments = false and hashed = true and hashed_date < cutOff;
    END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION removeAttachments(integer) to chox_user;
