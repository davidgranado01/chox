--
-- CHOX-?:
--
CREATE TABLE insurer_billing_band (
    id serial NOT NULL,
    insurer_id integer NOT NULL,
    band_name character varying(32) NOT NULL,
    cost_per_claim numeric(5,2) NOT NULL,
    exclude_supplementary boolean NOT NULL,
    trigger_status character varying(40) NOT NULL,
    "version" integer NOT NULL,
    created_by integer NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT insurer_billing_band_pkey PRIMARY KEY (id),
    CONSTRAINT insurer_billing_band_insurer_id_fkey FOREIGN KEY (insurer_id)
        REFERENCES insurer (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_billing_band_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_billing_band_webuser_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE insurer_billing_band TO chox_user;
GRANT SELECT ON TABLE insurer_billing_band TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE insurer_billing_band_id_seq TO chox_user;
CREATE UNIQUE INDEX insurer_billing_band_ux on insurer_billing_band(insurer_id, band_name);

CREATE TABLE cho_billing_band (
    id serial NOT NULL,
    chorganisation_id integer NOT NULL,
    band_name character varying(32) NOT NULL,
    cost_per_claim numeric(5,2) NOT NULL,
    exclude_supplementary boolean NOT NULL,
    trigger_status character varying(40) NOT NULL,
    "version" integer NOT NULL,
    created_by integer NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT cho_billing_band_pkey PRIMARY KEY (id),
    CONSTRAINT cho_billing_band_insurer_id_fkey FOREIGN KEY (chorganisation_id)
        REFERENCES chorganisation (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cho_billing_band_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cho_billing_band_webuser_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE cho_billing_band TO chox_user;
GRANT SELECT ON TABLE cho_billing_band TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE cho_billing_band_id_seq TO chox_user;
CREATE UNIQUE INDEX cho_billing_band_ux on cho_billing_band(chorganisation_id, band_name);


CREATE TABLE insurer_billing_band_mapping (
    id serial NOT NULL,
    insurer_billing_band_id integer NOT NULL,
    chorganisation_id integer NOT NULL,
    claim_type integer NOT NULL,
    "version" integer NOT NULL,
    created_by integer NOT NULL,
    completed_by integer,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT insurer_billing_band_mapping_pkey PRIMARY KEY (id),
    CONSTRAINT insurer_billing_band_mapping_band_id_fkey FOREIGN KEY (insurer_billing_band_id)
        REFERENCES insurer_billing_band (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_billing_band_mapping_chorganisation_id_fkey FOREIGN KEY (chorganisation_id)
        REFERENCES chorganisation (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_billing_band_mapping_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_billing_band_mapping_webuser_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE insurer_billing_band_mapping TO chox_user;
GRANT SELECT ON TABLE insurer_billing_band_mapping TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE insurer_billing_band_mapping_id_seq TO chox_user;


CREATE TABLE cho_billing_band_mapping (
    id serial NOT NULL,
    cho_billing_band_id integer NOT NULL,
    insurer_id integer NOT NULL,
    claim_type integer NOT NULL,
    "version" integer NOT NULL,
    created_by integer NOT NULL,
    completed_by integer,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT cho_billing_band_mapping_pkey PRIMARY KEY (id),
    CONSTRAINT cho_billing_band_mapping_band_id_fkey FOREIGN KEY (cho_billing_band_id)
        REFERENCES cho_billing_band (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cho_billing_band_mapping_insurer_id_fkey FOREIGN KEY (insurer_id)
        REFERENCES insurer (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cho_billing_band_mapping_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT cho_billing_band_mapping_webuser_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE cho_billing_band_mapping TO chox_user;
GRANT SELECT ON TABLE cho_billing_band_mapping TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE cho_billing_band_mapping_id_seq TO chox_user;


ALTER TABLE insurer DROP COLUMN cho_agreed_benefit_value;
ALTER TABLE insurer DROP COLUMN scs_agreed_benefit_share_value;
ALTER TABLE insurer DROP COLUMN is_fixed_transactional_fee;
ALTER TABLE insurer DROP COLUMN fixed_transactional_fee_value;

ALTER TABLE chorganisation DROP COLUMN is_fixed_transactional_fee;
ALTER TABLE chorganisation DROP COLUMN fixed_transactional_fee_value;

DROP TABLE billing_cho_rate;

ALTER TABLE billing_cho DROP COLUMN is_fixed_transactional_fee;
ALTER TABLE billing_cho DROP COLUMN fixed_transactional_fee_value;
ALTER TABLE billing_cho DROP COLUMN charge_rate;
ALTER TABLE billing_cho_detail ADD COLUMN trigger_point character varying(22) NOT NULL default 'PaymentReceived';
ALTER TABLE billing_cho_detail RENAME COLUMN received_date to trigger_date;


ALTER TABLE billing_insurer_detail RENAME COLUMN received_date to trigger_date;
ALTER TABLE billing_insurer_detail ADD COLUMN trigger_point character varying(22) NOT NULL default '';

UPDATE billing_insurer_detail set trigger_point=bi.trigger_point
FROM billing_insurer bi
WHERE billing_insurer_detail.billing_insurer_id = bi.id;

ALTER TABLE billing_insurer DROP COLUMN fixed_transaction;
ALTER TABLE billing_insurer DROP COLUMN fixed_transaction_fee;
ALTER TABLE billing_insurer DROP COLUMN benefit_share;
ALTER TABLE billing_insurer DROP COLUMN benefit_value;
ALTER TABLE billing_insurer DROP COLUMN trigger_point;
