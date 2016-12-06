--
-- Phase 9 Sprint 4 : BRE Falilure Saving Reason
--
-- Replave invoice.invoice_saving_rule with 1-n relationship
CREATE TABLE invoice_saving_rule (
    id serial NOT NULL,
    invoice_id integer NOT NULL,
    invoice_saving_rule character(3) NOT NULL,
    saving_group integer NOT NULL,
    "version" integer NOT NULL,
    created_by integer NOT NULL,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT invoice_saving_rule_pkey PRIMARY KEY (id),
    CONSTRAINT invoice_saving_rule_invoice_id_fkey FOREIGN KEY (invoice_id)
        REFERENCES invoice (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT iinvoice_saving_rule_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT invoice_saving_rule_webuser_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE invoice_saving_rule TO chox_user;
GRANT SELECT ON TABLE invoice_saving_rule TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE invoice_saving_rule_id_seq TO chox_user;
CREATE UNIQUE INDEX invoice_saving_rule_ux on invoice_saving_rule(invoice_id, invoice_saving_rule);

--
-- Transfer across existing saving rules to new table
--
insert into invoice_saving_rule(invoice_id,invoice_saving_rule,saving_group,"version",created_by,last_modified_by)
    select id, invoice_saving_rule, 3, 0, 5, 5
    from invoice where invoice_saving_rule is not null;

ALTER TABLE invoice DROP COLUMN invoice_saving_rule;
ALTER TABLE invoice_original DROP COLUMN invoice_saving_rule;
