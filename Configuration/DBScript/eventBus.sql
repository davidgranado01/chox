CREATE TABLE event_log (
    id serial NOT NULL,
    "version" integer NOT NULL,
    activity_name character varying NOT NULL,
    event_name character varying NOT NULL,
    status character varying NOT NULL,
    claim_id integer NOT NULL,
    chorganisation_id integer NOT NULL,
    insurer_id integer NOT NULL,
    claim_type integer NOT NULL,
    created_by integer NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT event_log_pkey PRIMARY KEY (id),
    CONSTRAINT event_log_claim_fkey FOREIGN KEY (claim_id)
            REFERENCES claim (id) MATCH SIMPLE
            ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT event_log_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT event_log_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE event_log TO chox_user;
GRANT SELECT ON TABLE event_log TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE event_log_id_seq TO chox_user;

CREATE TABLE event_attributes (
    id serial NOT NULL,
    event_log_id integer NOT NULL,
    key character varying NOT NULL,
    value character varying NOT NULL,
    CONSTRAINT event_attributes_pkey PRIMARY KEY (id),
    CONSTRAINT event_attributes_event_log_fkey FOREIGN KEY (event_log_id)
            REFERENCES event_log (id) MATCH SIMPLE
            ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE event_attributes TO chox_user;
GRANT SELECT ON TABLE event_attributes TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE event_attributes_id_seq TO chox_user;
