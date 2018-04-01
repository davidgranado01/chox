ALTER TABLE claim ADD COLUMN hashed boolean not null default false;
ALTER TABLE claim ADD COLUMN hashed_date timestamp without time zone;
ALTER TABLE claim ADD COLUMN removed_notes boolean not null default false;
ALTER TABLE claim ADD COLUMN removed_notes_date timestamp without time zone;
ALTER TABLE claim ADD COLUMN removed_tasks boolean not null default false;
ALTER TABLE claim ADD COLUMN removed_tasks_date timestamp without time zone;
ALTER TABLE claim ADD COLUMN removed_attachments boolean not null default false;
ALTER TABLE claim ADD COLUMN removed_attachments_date timestamp without time zone;
ALTER TABLE web_user ADD COLUMN hashed boolean not null default false;
ALTER TABLE web_user ADD COLUMN hashed_date timestamp without time zone;
ALTER TABLE web_user ADD COLUMN deactivated_date timestamp without time zone;
ALTER TABLE comment ADD COLUMN user_comment boolean not null default false;
ALTER TABLE attachment ADD COLUMN removed boolean not null default false;

UPDATE comment set user_comment = true, version=version+1
WHERE comment not like 'Reason For Rejection:%'
  AND comment not like 'CHO contact number is%'
  AND comment not like 'New CHO contact number is%'
  AND comment not like 'Insurer Claims Handler is%'
  AND comment not like 'Insurer Claims Handler changed from%'
  AND comment not like 'The Keoghs Fraud Check result has been acknowledged%'
  AND comment not like 'An interim payment of%'
  AND comment not like 'Supplier Reference updated from%'
  AND comment not like 'The claim was marked as ''Invoice Payment Logged'' on%'
  AND comment not like 'Claim Switched From Payments Team to Claims Handler%'
  AND comment not like 'Insurer Indemnity Stance%'
  AND comment not like 'Updating interim payments received to%'
  AND comment not like 'Claim Closed:%'
  AND comment not like 'Case Marked As With Clients Solicitor%'
  AND comment not like 'A full payment amount of%'
  AND comment not like 'A payment amount of%'
  AND comment not like 'The interim payment has been removed%'
  AND comment not like 'The interim payment made has been modified to a new total of%'
  AND comment not like 'An additional interim payment of%'
  AND comment not like 'Supplier Claim Owner changed from%'
  AND comment not like 'Supplier Claim Owner is%'
  AND comment not like 'Supplier Claims Handler is%'
  AND comment not like 'Claim Acceptance Reason:%'
  AND comment not like 'This claim has been referred to Keoghs%'
  AND comment not like 'Hire rate adjusted from%'
  AND comment not like 'Date of Last Review%'
  AND comment not like 'The Date of Last Review has been removed%'
  AND comment not like 'This is a supplementary Invoice%'
  AND comment not like 'Final Review Reason:%'
  AND comment not like 'A discount of%'
  AND comment not like '%failed to respond to the Subscriber notification within the%'
  AND comment not like '%failed to respond to the Fixed Fee notification within the%'
  AND comment not like 'Penalty charges have been removed from the invoice%'
  AND comment not like 'It was not possible for Keoghs ADA Fraud Check Tool to score this claim%'
  AND comment not like '%Fraud Check was run%'
  AND comment not like 'Claim owner changed from%'
  AND comment not like 'Automatic penalty charges of%'
  AND comment not like 'Liability status changed to%'
  AND comment not like 'Liability status changed from%'
  AND comment not like 'An invoice amendment has been%'
  AND comment not like 'Claim switched from%'
  AND comment not like 'Supplier Claim owner changed from%';

--GDPR getHash function
--    Need to install crypto extension: CREATE EXTENSION pgcrypto;

CREATE OR REPLACE FUNCTION getHash(stringToHash text, size int)
  RETURNS text AS
$BODY$
DECLARE
    normalisedString text;
    fullHashedString text;
    annotatedHashedString text;
    startString text;

   BEGIN
      -- if string already hashed, just return it
      startString = substring(stringToHash from 1 for 2);
      IF startString = '~~' THEN
        RETURN stringToHash;
      END IF;
      -- NORMALIZE: make all characters capitals and replace spaces with underscores
      normalisedString = upper(replace(stringToHash, ' ', '_'));

      -- HASH: use sha256
      fullHashedString = encode(digest(normalisedString, 'sha256'::text), 'hex');

      -- truncate to size-2 and annotate/prepend with marker '~~'
      annotatedHashedString = '~~' || substring(fullHashedString from 1 for size-2);

      RETURN annotatedHashedString;
   END;
$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION getHash(text, int) to chox_user;


CREATE OR REPLACE FUNCTION hashClaims(closedRetentionDays integer)
    RETURNS void AS
$BODY$
DECLARE
    closedCutOff date;
    BEGIN
      closedCutOff := now()::date  - ($1 || ' days')::interval;

    --
    -- Select closed claim data to be hashed
    --
    CREATE TEMP TABLE IF NOT EXISTS hash_claim AS
        select c.id, c.status, c.claim_type, c.insurer_id, c.chorganisation_id, c.customer_id, c.third_party_id,
               cu.title as customer_title, cu.first_name as customer_first_name, cu.last_name as customer_last_name,
               cu.address1 as customer_address1, cu.address2 as customer_address2, cu.address3 as customer_address3,
               cu.address4 as customer_address4, cu.address5 as customer_address5, cu.telephone_day as customer_telephone_day,
               cu.telephone_evening as customer_telephone_evening, cu.email as customer_email, cu.occupation as customer_occupation,
               tp.title as third_party_title, tp.first_name as third_party_first_name, tp.last_name as third_party_last_name,
               tp.address1 as third_party_address1, tp.address2 as third_party_address2, tp.address3 as third_party_address3,
               tp.address4 as third_party_address4, tp.address5 as third_party_address5, tp.telephone_day as third_party_telephone_day,
               tp.telephone_evening as third_party_telephone_evening, tp.email as third_party_email,
               w.id as witness_id, w.name as witness_name, w.address1 as witness_address1, w.address2 as witness_address2, w.address3 as witness_address3,
               w.address4 as witness_address4, w.address5 as witness_address5, w.telephone_day as witness_telephone_day,
               w.telephone_evening as witness_telephone_evening, w.email as witness_email,
               ij.id as injury_id, ij.name as injury_name, ij.address1 as injury_address1, ij.address2 as injury_address2, ij.address3 as injury_address3,
               ij.address4 as injury_address4, ij.address5 as injury_address5, ij.telephone_day as injury_telephone_day,
               ij.telephone_evening as injury_telephone_evening, ij.email as injury_email,
               ij.solicitor_name as injury_solicitor_name, ij.solicitor_address1 as injury_solicitor_address1, ij.solicitor_address2 as injury_solicitor_address2,
               ij.solicitor_address3 as injury_solicitor_address3, ij.solicitor_address4 as injury_solicitor_address4, ij.solicitor_address5 as injury_solicitor_address5,
               ij.solicitor_telephone as injury_solicitor_telephone, ij.solicitor_email as injury_solicitor_email
        from claim c, customer cu, third_party tp, incident i left outer join witness w on (w.incident_id=i.id) left outer join injury ij on (ij.incident_id=i.id)
        where c.customer_id = cu.id and c.third_party_id = tp.id and c.incident_id = i.id
          and c.status in ('PaymentReceived', 'ManualInvoicePaid', 'ClaimClosed', 'ClaimRejectionAccepted', 'InvoiceRejectionAccepted')
          and c.hashed = false
          and c.status_modified_date::date < closedCutOff;

  --
  -- Add open claim data to be hashed
  --
  --    insert into hash_claim
  --        select c.id, c.customer_id, cu.title as customer_title, cu.first_name as customer_first_name, cu.last_name as customer_last_name,
  --               cu.address1 as customer_address1, cu.address2 as customer_address2, cu.address3 as customer_address3,
  --               cu.address4 as customer_address4, cu.address5 as customer_address5, cu.telephone_day as customer_telephone_day,
  --               cu.telephone_evening as customer_telephone_evening, cu.email as customer_email, cu.occupation as customer_occupation,
  --               c.third_party_id, tp.title as third_party_title, tp.first_name as third_party_first_name, tp.last_name as third_party_last_name,
  --               tp.address1 as third_party_address1, tp.address2 as third_party_address2, tp.address3 as third_party_address3,
  --               tp.address4 as third_party_address4, tp.address5 as third_party_address5, tp.telephone_day as third_party_telephone_day,
  --               tp.telephone_evening as third_party_telephone_evening, tp.email as third_party_email,
  --               w.id as witness_id, w.name as witness_name, w.address1 as witness_address1, w.address2 as witness_address2, w.address3 as witness_address3,
  --               w.address4 as witness_address4, w.address5 as witness_address5, w.telephone_day as witness_telephone_day,
  --               w.telephone_evening as witness_telephone_evening, w.email as witness_email,
  --               ij.id as injury_id, ij.name as injury_name, ij.address1 as injury_address1, ij.address2 as injury_address2, ij.address3 as injury_address3,
  --               ij.address4 as injury_address4, ij.address5 as injury_address5, ij.telephone_day as injury_telephone_day,
  --               ij.telephone_evening as injury_telephone_evening, ij.email as injury_email,
  --               ij.solicitor_name as injury_solicitor_name, ij.solicitor_address1 as injury_solicitor_address1, ij.solicitor_address2 as injury_solicitor_address2,
  --               ij.solicitor_address3 as injury_solicitor_address3, ij.solicitor_address4 as injury_solicitor_address4, ij.solicitor_address5 as injury_solicitor_address5,
  --               ij.solicitor_telephone as injury_solicitor_telephone, ij.solicitor_email as injury_solicitor_email
  --        from claim c, customer cu, third_party tp, incident i left outer join witness w on (w.incident_id=i.id) left outer join injury ij on (ij.incident_id=i.id)
  --        where c.customer_id = cu.id and c.third_party_id = tp.id and c.incident_id = i.id
  --          and c.status not in ('PaymentReceived', 'ManualInvoicePaid', 'ClaimClosed', 'ClaimRejectionAccepted', 'InvoiceRejectionAccepted')
  --        and c.hashed = false;
  --         and c.status_modified_date::date < openCutOff;

        update hash_claim
          set customer_title = getHash(customer_title, 32),
              customer_first_name = getHash(customer_first_name, 128),
              customer_last_name = getHash(customer_last_name, 64),
              customer_address1 = getHash(customer_address1, 128),
              customer_address2 = getHash(customer_address2, 128),
              customer_address3 = getHash(customer_address3, 128),
              customer_address4 = getHash(customer_address4, 128),
              customer_address5 = getHash(customer_address5, 128),
              customer_telephone_day = getHash(customer_telephone_day, 50),
              customer_telephone_evening = getHash(customer_telephone_evening, 50),
              customer_email = getHash(customer_email, 64),
              customer_occupation = getHash(customer_occupation, 64),
              third_party_title = getHash(third_party_title, 32),
              third_party_first_name = getHash(third_party_first_name, 128),
              third_party_last_name = getHash(third_party_last_name, 64),
              third_party_address1 = getHash(third_party_address1, 128),
              third_party_address2 = getHash(third_party_address2, 128),
              third_party_address3 = getHash(third_party_address3, 128),
              third_party_address4 = getHash(third_party_address4, 128),
              third_party_address5 = getHash(third_party_address5, 128),
              third_party_telephone_day = getHash(third_party_telephone_day, 50),
              third_party_telephone_evening = getHash(third_party_telephone_evening, 50),
              third_party_email = getHash(third_party_email, 64),
              witness_name = getHash(witness_name, 128),
              witness_address1 = getHash(witness_address1, 128),
              witness_address2 = getHash(witness_address2, 128),
              witness_address3 = getHash(witness_address3, 128),
              witness_address4 = getHash(witness_address4, 128),
              witness_address5 = getHash(witness_address5, 128),
              witness_telephone_day = getHash(witness_telephone_day, 50),
              witness_telephone_evening = getHash(witness_telephone_evening, 50),
              witness_email = getHash(witness_email, 64),
              injury_name = getHash(injury_name, 128),
              injury_address1 = getHash(injury_address1, 128),
              injury_address2 = getHash(injury_address2, 128),
              injury_address3 = getHash(injury_address3, 128),
              injury_address4 = getHash(injury_address4, 128),
              injury_address5 = getHash(injury_address5, 128),
              injury_telephone_day = getHash(injury_telephone_day, 50),
              injury_telephone_evening = getHash(injury_telephone_evening, 50),
              injury_email = getHash(injury_email, 64),
              injury_solicitor_name = getHash(injury_solicitor_name, 64),
              injury_solicitor_address1 = getHash(injury_solicitor_address1, 128),
              injury_solicitor_address2 = getHash(injury_solicitor_address2, 128),
              injury_solicitor_address3 = getHash(injury_solicitor_address3, 128),
              injury_solicitor_address4 = getHash(injury_solicitor_address4, 128),
              injury_solicitor_address5 = getHash(injury_solicitor_address5, 128),
              injury_solicitor_telephone = getHash(injury_solicitor_telephone, 50),
              injury_solicitor_email = getHash(injury_solicitor_email, 64);

        create index hash_claim_claim on hash_claim(id);
        create index hash_claim_customer on hash_claim(customer_id);
        create index hash_claim_third_party on hash_claim(third_party_id);
        create index hash_claim_witness on hash_claim(witness_id);
        create index hash_claim_injury on hash_claim(injury_id);

        update customer
          set version = customer.version+1,
              last_modified_date=now(),
              last_modified_by=999,
              title = hc.customer_title,
              first_name = hc.customer_first_name,
              last_name = hc.customer_last_name,
              address1 = hc.customer_address1,
              address2 = hc.customer_address2,
              address3 = hc.customer_address3,
              address4 = hc.customer_address4,
              address5 = hc.customer_address5,
              postcode = substring(postcode from 1 for 4),
              telephone_day = hc.customer_telephone_day,
              telephone_evening = hc.customer_telephone_evening,
              email = hc.customer_email,
              occupation = hc.customer_occupation
        from hash_claim hc
        where hc.customer_id = customer.id;

        update third_party
          set version = third_party.version+1,
              last_modified_date=now(),
              last_modified_by=999,
              title = hc.third_party_title,
              first_name = hc.third_party_first_name,
              last_name = hc.third_party_last_name,
              address1 = hc.third_party_address1,
              address2 = hc.third_party_address2,
              address3 = hc.third_party_address3,
              address4 = hc.third_party_address4,
              address5 = hc.third_party_address5,
              postcode = substring(postcode from 1 for 4),
              telephone_day = hc.third_party_telephone_day,
              telephone_evening = hc.third_party_telephone_evening,
              email = hc.third_party_email
        from hash_claim hc
        where hc.third_party_id = third_party.id;

        update witness
          set version = witness.version+1,
              last_modified_date=now(),
              last_modified_by=999,
              name = hc.witness_name,
              address1 = hc.witness_address1,
              address2 = hc.witness_address2,
              address3 = hc.witness_address3,
              address4 = hc.witness_address4,
              address5 = hc.witness_address5,
              postcode = substring(postcode from 1 for 4),
              telephone_day = hc.witness_telephone_day,
              telephone_evening = hc.witness_telephone_evening,
              email = hc.witness_email
        from hash_claim hc
        where hc.witness_id = witness.id;

        update injury
          set version = injury.version+1,
              last_modified_date=now(),
              last_modified_by=999,
              name = hc.injury_name,
              address1 = hc.injury_address1,
              address2 = hc.injury_address2,
              address3 = hc.injury_address3,
              address4 = hc.injury_address4,
              address5 = hc.injury_address5,
              postcode = substring(postcode from 1 for 4),
              telephone_day = hc.injury_telephone_day,
              telephone_evening = hc.injury_telephone_evening,
              email = hc.injury_email,
              solicitor_name = hc.injury_solicitor_name,
              solicitor_address1 = hc.injury_solicitor_address1,
              solicitor_address2 = hc.injury_solicitor_address2,
              solicitor_address3 = hc.injury_solicitor_address3,
              solicitor_address4 = hc.injury_solicitor_address4,
              solicitor_address5 = hc.injury_solicitor_address5,
              solicitor_postcode = substring(solicitor_postcode from 1 for 4),
              solicitor_telephone = hc.injury_solicitor_telephone,
              solicitor_email = hc.injury_solicitor_email
        from hash_claim hc
        where hc.injury_id = injury.id;

        -- Mark claims as hashed
        update claim
          set version = version+1,
              hashed = true,
              hashed_date = now(),
              last_modified_date=now(),
              last_modified_by = 999
        from hash_claim hc
        where hc.id = claim.id;


        --
        -- Add event to event log?
        --
        insert into event_log(version, activity_name, event_name, status, claim_id, chorganisation_id, insurer_id, claim_type, created_by, created_date, last_modified_by, last_modified_date)
          select 0, 'GDPR', 'ClaimHashedEvent', hc.status, hc.id, hc.chorganisation_id, hc.insurer_id, hc.claim_type, 999, now(), 999, now()
          from hash_claim hc;

        drop table hash_claim;
    END;
$BODY$
   LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION hashClaims(integer) to chox_user;


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

CREATE OR REPLACE FUNCTION removeTasks(claimAge integer)
     RETURNS void AS
$BODY$
DECLARE
     cutOff date;
     BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;

        update task
            set description = 'GDPR: description content has been removed.',
                version = task.version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        from claim c
        where task.claim_id = c.id
          and removed_tasks = false and c.hashed = true and c.hashed_date < cutOff;

        update claim
            set removed_tasks = true, removed_tasks_date = now(), version=version+1, last_modified_date=now(), last_modified_by=999
        where removed_tasks = false and hashed = true and hashed_date < cutOff;
     END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION removeTasks(integer) to chox_user;


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


CREATE OR REPLACE FUNCTION deactivateUsers(claimAge integer)
    RETURNS void AS
$BODY$
DECLARE
    cutOff date;
    BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;

        update web_user
            set status = false, deactivated_date = now()
        where status = true and last_login_date < cutOff
          and user_name not like 'admin@%' and id not in (999, 4391, 5454, 3757, 6410, 3893);

        update web_user
            set status = false, deactivated_date = now()
        where status = true and last_login_date is null and created_date < cutOff
          and user_name not like 'admin@%' and id not in (999, 4391, 5454, 3757, 6410, 3893);
    END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION deactivateUsers(integer) to chox_user;


CREATE OR REPLACE FUNCTION hashUsers(claimAge integer, claimAge2 integer)
    RETURNS void AS
$BODY$
DECLARE
    cutOff date;
    cutOff2 date;
    BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;
        cutOff2 := now()::date  - ($2 || ' days')::interval;

        update web_user
            set hashed = true, hashed_date = now(),
                first_name = getHash(first_name, 256),
                last_name = getHash(last_name, 256),
                email = getHash(email, 256),
                version = version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        where status = false and hashed = false and deactivated_date < cutOff;

        update web_user
            set hashed = true, hashed_date = now(),
                first_name = getHash(first_name, 256),
                last_name = getHash(last_name, 256),
                email = getHash(email, 256),
                version = version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        where status = false and hashed = false and deactivated_date is null
          and (last_login_date < cutOff2 or (last_login_date is null and created_date < cutOff2));
    END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION hashUsers(integer, integer) to chox_user;
