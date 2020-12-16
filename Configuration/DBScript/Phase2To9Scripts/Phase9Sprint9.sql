--
-- CHOX-15: New BRE Rule: ECD vs Repair Completion Date
--
ALTER TABLE bre_band ADD COLUMN ecd_vs_repair_completion_date_check boolean NOT NULL default false;

--
-- CHOX-407: Add 'Copley' questions to Insurer Hire Monitoring
--
ALTER TABLE insurer_hire_monitoring_detail ADD COLUMN copley_offer_made boolean;
ALTER TABLE insurer_hire_monitoring_detail ADD COLUMN copley_offer_made_date timestamp without time zone;

--
-- CHOX-408: Add BRE Rule: Full Total Requested
--
ALTER TABLE bre_band ADD COLUMN full_total_requested_tolerance numeric(10,2);
ALTER TABLE bre_band ADD COLUMN full_total_requested_ceiling_check boolean NOT NULL default false;

DROP TABLE IF EXISTS paid_invoices_import;
CREATE TABLE paid_invoices_import (
    id serial NOT NULL,
    insurer_name character varying,
    cho_reference character varying,
    claim_number character varying,
    created_date timestamp without time zone NOT NULL DEFAULT now()
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE paid_invoices_import TO chox_user;
GRANT SELECT ON TABLE paid_invoices_import TO chox_mi;

DROP TABLE IF EXISTS paid_invoices;
CREATE TABLE paid_invoices (
    id serial NOT NULL,
    "version" integer NOT NULL default 0,
    insurer_name character varying NOT NULL,
    cho_reference character varying NOT NULL,
    claim_number character varying NOT NULL,
    created_by integer,
    created_date timestamp without time zone NOT NULL default now(),
    last_modified_by integer,
    last_modified_date timestamp without time zone NOT NULL default now(),
    CONSTRAINT paid_invoices_pkey PRIMARY KEY (id),
    CONSTRAINT paid_invoices_created_by_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT paid_invoices_last_modified_by_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
CREATE UNIQUE INDEX paid_invoices_ux ON paid_invoices(insurer_name,cho_reference,claim_number);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE paid_invoices TO chox_user;
GRANT SELECT ON TABLE paid_invoices TO chox_mi;


--
-- Set-up Scheduler Job and user
--
insert into scheduler_job (login_username, login_password, job_name, email_subject, autherised_user, bcc_receiver,
                             error_message_receiver, created_by, created_date, last_modified_by, last_modified_date, version)
    select 'paidInvoices.lv', 'Val1dusSmasher', 'PAID_INVOICES', 'not email job',
         'john.dowson@valexa.com', 'john.dowson@valexa.com', 'john.dowson@valexa.com', 999, now(), 999, now(), 0;
insert into web_user(email, first_name, last_name, password, created_by, created_date, last_modified_by, last_modified_date, insurer_id,
                        status, is_expired, user_name, version, telephone, show_browser_warning, password_last_modified_date)
    select 'john.dowson@valexa.com', 'Paid Invoices', '(via SFTP)', 'ac9ebb5c9eaa6f6674d68bb220f609c3', 999, now(), 999, now(), 26,
                        true, false, 'paidInvoices.lv', 0, '07702 904147', false, now();

insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='paidInvoices.lv' and r.name='ROLE_INS';
insert into web_user_user_role(web_user_id, web_user_role_id, created_by, last_modified_by, version)
    select w.id, r.id, 999,999,0 from web_user w, web_user_role r where w.user_name='paidInvoices.lv' and r.name='ROLE_INS_CH';

