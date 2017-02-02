--
-- CHOX-280: Create Claim Matching Table in DB
--
CREATE TABLE claim_matching (
    id serial NOT NULL,
    "version" integer NOT NULL,
    insurer_id integer NOT NULL,
    claim_id integer,
    third_party_insurer character varying NOT NULL,
    insurer_claim_number character varying NOT NULL,
    third_party_vehicle_registration character varying NOT NULL,
    incident_date timestamp without time zone NOT NULL,
    customer_vehicle_registration character varying NOT NULL,
    liability_insurer numeric(5,2) NOT NULL,
    liability_cho numeric(5,2) NOT NULL,
    liability_status smallint,
    liability_note character varying,
    indemnity_stance character varying NOT NULL,
    cho_name character varying,
    fraud_status character varying,
    solicitor_details character varying,
    non_fault_contact_details character varying,
    created_by integer NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now()
    CONSTRAINT claim_matching_pkey PRIMARY KEY (id),
    CONSTRAINT claim_matching_insurer_fkey FOREIGN KEY (insurer_id)
        REFERENCES insurer (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT claim_matching_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT claim_matching_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE claim_matching TO chox_user;
GRANT SELECT ON TABLE claim_matching TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE claim_matching_id_seq TO chox_user;

CREATE TABLE claim_matching_import (
    insurer_id integer NOT NULL,
    third_party_insurer character varying NOT NULL,
    insurer_claim_number character varying NOT NULL,
    third_party_vehicle_registration character varying NOT NULL,
    incident_date timestamp without time zone NOT NULL,
    customer_vehicle_registration character varying NOT NULL,
    liability_insurer numeric(5,2) NOT NULL,
    liability_cho numeric(5,2) NOT NULL,
    liability_status smallint,
    liability_note character varying,
    indemnity_stance character varying NOT NULL,
    cho_name character varying,
    fraud_status character varying,
    solicitor_details character varying,
    non_fault_contact_details character varying
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE claim_matching_import TO chox_user;
GRANT SELECT ON TABLE claim_matching_import TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE claim_matching_import_id_seq TO chox_user;

--
-- To remove on day 31
--  delete from claim_matching where claim_id is null and now()::date - created_date::date > 30;
-- (add to maintenance/nighly script)


--
-- CHOX-281: Claim Matching Config
--
ALTER TABLE insurer ADD COLUMN enable_claim_matching boolean NOT NULL default false;
ALTER TABLE bre_band ADD COLUMN enable_claim_matching boolean NOT NULL default false;
ALTER TABLE bre_band ADD COLUMN claim_matching_workgroup integer;
ALTER TABLE bre_band ADD COLUMN claim_matching_owner integer;

