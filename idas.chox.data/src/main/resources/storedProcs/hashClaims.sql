CREATE OR REPLACE FUNCTION hashClaims(closedRetentionDays integer)
    RETURNS void AS
$BODY$
DECLARE
    closedCutOff date;
--    openCutOff date;
BEGIN
    closedCutOff := now()::date  - ($1 || ' days')::interval;
--    openCutOff := now()::date  - ($2 || ' days')::interval;

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
