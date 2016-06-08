--------------------------------------------------------------------------------
-- Keoghs integration sprint updates
--------------------------------------------------------------------------------
create table keoghs_request (
    id serial not null,
    claim_id integer,
    client_batch_reference character varying NOT NULL,
    batch_status integer,
    claim_status integer,
    check_type character varying,
    result_status character varying,
    rag_result character varying,
    total_score int,
    response_message_debug character varying,
    version integer,
    created_by integer NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT keoghs_request_pkey PRIMARY KEY (id),
    CONSTRAINT keoghs_request_web_user_fkey1 FOREIGN KEY (created_by) REFERENCES web_user(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT keoghs_request_web_user_fkey2 FOREIGN KEY (last_modified_by) REFERENCES web_user(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT keoghs_request_claim_fkey FOREIGN KEY (claim_id) REFERENCES claim(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table keoghs_request_score_message (
    id serial not null,
    keoghs_request_id integer not null,
    score_message_heading character varying not null,
    score_message_detail character varying not null,
    version integer,
    created_by integer NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone NOT NULL DEFAULT now(),
    CONSTRAINT keoghs_request_score_message_pkey PRIMARY KEY (id),
    CONSTRAINT keoghs_request_score_message_fkey FOREIGN KEY (keoghs_request_id) REFERENCES keoghs_request(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT keoghs_request_score_message_web_user_fkey1 FOREIGN KEY (created_by) REFERENCES web_user(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT keoghs_request_score_message_web_user_fkey2 FOREIGN KEY (last_modified_by) REFERENCES web_user(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);


ALTER TABLE claim ADD COLUMN fraud_check_status int not null DEFAULT 0; -- 0 = not requested, 1 = queued, 2 = pending, 3 = available, -1 = error
--ALTER TABLE claim ADD COLUMN keoghs_client_batch_reference character varying;
ALTER TABLE claim ADD COLUMN keoghs_request_id integer;
ALTER TABLE claim ADD COLUMN sent_to_keoghs boolean not null default false;
ALTER TABLE claim ADD COLUMN fraud_result_acknowledged boolean;
ALTER TABLE claim ADD CONSTRAINT claim_keoghs_request_fkey FOREIGN KEY (keoghs_request_id) REFERENCES keoghs_request(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE bre_band ADD COLUMN fraud_check_enable boolean not null default false;
