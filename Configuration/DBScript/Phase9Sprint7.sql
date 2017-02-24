--
-- Tables to hold new internal events
--
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

--
-- CHOX-280: Create Claim Matching Table in DB
--
DROP TABLE IF EXISTS claim_matching;
CREATE TABLE claim_matching (
    id serial NOT NULL,
    "version" integer NOT NULL,
    claim_id integer,
    insurer_id integer,
    insurer_name character varying NOT NULL,
    claim_number character varying NOT NULL,
    third_party_vehicle_registration character varying NOT NULL,
    incident_date timestamp without time zone NOT NULL,
    liability_insurer numeric(5,2),
    liability_stance character varying NOT NULL,
    indemnity_stance character varying NOT NULL,
    match_status smallint not null default 0,
    created_by integer NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT claim_matching_insurer_fkey FOREIGN KEY (insurer_id)
        REFERENCES insurer (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,    CONSTRAINT claim_matching_pkey PRIMARY KEY (id),
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
CREATE UNIQUE INDEX claim_matching_ux ON claim_matching(insurer_name,claim_number);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE claim_matching TO chox_user;
GRANT SELECT ON TABLE claim_matching TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE claim_matching_id_seq TO chox_user;

DROP TABLE IF EXISTS claim_matching_import;
CREATE TABLE claim_matching_import (
    id serial NOT NULL,
    "version" integer NOT NULL default 0,
    insurer_name character varying NOT NULL,
    claim_number character varying NOT NULL,
    third_party_vehicle_registration character varying NOT NULL,
    incident_date timestamp without time zone NOT NULL,
    indemnity_stance character varying NOT NULL,
    liability_stance character varying NOT NULL,
    liability_insurer numeric(5,2),
    created_date timestamp without time zone NOT NULL DEFAULT now()
)
WITH (
    OIDS=FALSE
);
CREATE UNIQUE INDEX claim_matching_import_ux ON claim_matching_import(insurer_name,claim_number);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE claim_matching_import TO chox_user;
GRANT SELECT ON TABLE claim_matching_import TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE claim_matching_import_id_seq TO chox_user;
-- psql -c "COPY claim_matching_import(insurer_name,third_party_vehicle_registration,incident_date,indemnity_stance,liability_stance,liability_insurer) FROM '/Users/john/lv-claimMatching.csv' delimiter ',' csv;" chox_p9s7
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

CREATE TABLE claim_matching_band (
    id serial NOT NULL,
    version integer,
    bre_band_id integer NOT NULL,
    claim_type integer NOT NULL,
    liability_percentage numeric(5,2) NOT NULL,
    auto_acknowledge boolean NOT NULL default false,
    b_class boolean NOT NULL default false,
    cm_class boolean NOT NULL default false,
    cp_class boolean NOT NULL default false,
    cs_class boolean NOT NULL default false,
    cv_class boolean NOT NULL default false,
    f_class boolean NOT NULL default false,
    m_class boolean NOT NULL default false,
    nt_class boolean NOT NULL default false,
    p_class boolean NOT NULL default false,
    pt_class boolean NOT NULL default false,
    pv_class boolean NOT NULL default false,
    rv_class boolean NOT NULL default false,
    s_class boolean NOT NULL default false,
    sp_class boolean NOT NULL default false,
    t_class boolean NOT NULL default false,
    u_class boolean NOT NULL default false,
    created_by integer,
    created_date timestamp without time zone NOT NULL default now(),
    last_modified_by integer,
    last_modified_date timestamp without time zone NOT NULL default now(),
    CONSTRAINT claim_matching_band_pkey PRIMARY KEY (id),
    CONSTRAINT claim_matching_band_fkey FOREIGN KEY (bre_band_id)
        REFERENCES bre_band (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT claim_matching_created_by_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT claim_matching_last_modified_by_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE claim_matching_band TO chox_user;
GRANT SELECT, UPDATE ON TABLE claim_matching_band_id_seq TO chox_user;

--
-- Set-up Scheduler Job and user
--
insert into scheduler_job (login_username, login_password, job_name, email_subject, autherised_user, bcc_receiver,
                             error_message_receiver, created_by, created_date, last_modified_by, last_modified_date, version)
    select 'claimMatcher.lv', 'Val1dusSmasher', 'CLAIM_MATCHING', 'no enaill subject for claim matching',
         'john.dowson@valexa.com', 'john.dowson@valexa.com', 'john.dowson@valexa.com', 999, now(), 999, now(), 0;
insert into web_user(email, first_name, last_name, password, created_by, created_date, last_modified_by, last_modified_date, insurer_id,
                        status, is_expired, user_name, version, telephone, show_browser_warning, password_last_modified_date)
    select 'john.dowson@valexa.com', 'Claim', 'Matcher', 'ac9ebb5c9eaa6f6674d68bb220f609c3', 999, now(), 999, now(), 26,
                        true, false, 'claimMatcher.lv', 0, '07702 904147', false, now();

insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='claimMatcher.lv' and r.name='ROLE_INS';

--
-- Add accessibility for ClaimMatching actvity
--
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ClaimMatching.ClaimPending',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ClaimMatching.ClaimUnacknowledgedRouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ClaimMatching.ClaimRejectionContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ClaimMatching.ClaimUnacknowledgedUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ClaimMatching.ClaimUnacknowledgedUnrouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ClaimMatching.ClaimUpdatedByEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ClaimMatching.ClaimReferredToFNOL',FALSE,FALSE);

INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO', 2, id FROM accessibility WHERE name like 'activity.ClaimMatching.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS', 2, id FROM accessibility WHERE name like 'activity.ClaimMatching.%';

--
-- Add match status to claim
--
ALTER TABLE claim ADD COLUMN match_status integer not null default 0;
