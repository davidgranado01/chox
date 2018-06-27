--
-- CHOX-486: Applied Liability Config for updating Total To Pay
--
ALTER TABLE bre_band ADD COLUMN enable_applied_liability boolean NOT NULL DEFAULT false;

CREATE TABLE bre_applied_liability (
    id serial NOT NULL,
    version integer,
    bre_band_id integer NOT NULL,
    claim_type integer NOT NULL,
    applied_liability numeric(5,2) NOT NULL,
    applies_to_repudiated boolean NOT NULL,
    created_by integer,
    created_date timestamp without time zone NOT NULL default now(),
    last_modified_by integer,
    last_modified_date timestamp without time zone NOT NULL default now(),
    CONSTRAINT bre_applied_liability_pkey PRIMARY KEY (id),
    CONSTRAINT bre_applied_liability_ukey UNIQUE (bre_band_id, claim_type),
    CONSTRAINT bre_applied_liability_fkey FOREIGN KEY (bre_band_id)
        REFERENCES bre_band (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT created_by_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT last_modified_by_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE bre_applied_liability TO chox_user;
GRANT SELECT ON TABLE bre_applied_liability TO chox_mi;
GRANT SELECT, UPDATE ON TABLE bre_applied_liability_id_seq TO chox_user;
