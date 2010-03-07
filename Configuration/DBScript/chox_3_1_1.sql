alter table claim alter column percentage_liability_accepted drop not null;
alter table claim add column percentage_liability_cho numeric;
alter table claim add column liability_agreed_date timestamp;
alter table claim add column liability_status smallint;

alter table invoice add column total_to_pay_split_liability numeric DEFAULT 0.00;
------------------------

insert into accessibility(name,is_workgroup_check,is_ownership_check) values
( 'extraAction.updateLiability.AwaitingCarHireInfo',true,true);

insert into accessibility_item (role, access_right, accessibility_id) values
('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.AwaitingCarHireInfo'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.AwaitingCarHireInfo'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.AwaitingCarHireInfo'));

----------------

insert into accessibility(name,is_workgroup_check,is_ownership_check) values
( 'extraAction.updateLiability.InvoiceApprovedByBRE',true,true);

insert into accessibility_item (role, access_right, accessibility_id) values
('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.InvoiceApprovedByBRE'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceApprovedByBRE'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceApprovedByBRE'));

----------------------



insert into accessibility(name,is_workgroup_check,is_ownership_check) values
( 'extraAction.updateLiability.AwaitingInvoiceData',true,true);

insert into accessibility_item (role, access_right, accessibility_id) values
('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.AwaitingInvoiceData'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.AwaitingInvoiceData'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.AwaitingInvoiceData'));

--------------------------
--ClaimReferredToEngineer

---ContestedInvoiceReferredToInsurer

--ClaimUpdatedByEngineer

--InvoiceApprovedByBRE

--InvoiceReferredToClaimsHandler

--InvoiceEscalated

--InvoiceEscalatedToHandler

InvoiceReferredToEngineer


---------------------------

insert into accessibility(name,is_workgroup_check,is_ownership_check) values
( 'filter.AwaitingLiabilityResolution',false,false);

insert into accessibility_item (role, access_right, accessibility_id) values
('ALL', 0, (select id from accessibility where name='filter.AwaitingLiabilityResolution'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_CH', 1, (select id from accessibility where name='filter.AwaitingLiabilityResolution'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_MNG', 1, (select id from accessibility where name='filter.AwaitingLiabilityResolution'));

---------------------

insert into accessibility(name,is_workgroup_check,is_ownership_check) values
( 'tab.ClaimDetail.AwaitingLiabilityResolution',false,false);

insert into accessibility_item (role, access_right, accessibility_id) values
('ALL', 1, (select id from accessibility where name='tab.ClaimDetail.AwaitingLiabilityResolution'));

--------------------------------------------------------------
/*
insert into accessibility(name,is_workgroup_check,is_ownership_check) values
('action.updateLiability.AwaitingLiabilityResolution',false,false);

insert into accessibility_item (role, access_right, accessibility_id) values
('ALL', 0, (select id from accessibility where name='action.updateLiability.AwaitingLiabilityResolution'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_CH', 2, (select id from accessibility where name='action.updateLiability.AwaitingLiabilityResolution'));

insert into accessibility_item (role, access_right, accessibility_id) values
('ROLE_INS_MNG', 2, (select id from accessibility where name='action.updateLiability.AwaitingLiabilityResolution'));
*/



----------------------------------------------------------------
CREATE TABLE billing_cho
(
  id serial NOT NULL,
  cho_id integer NOT NULL,
  schedule_name character varying(255) NOT NULL,
  date_from timestamp without time zone NOT NULL,
  date_to timestamp without time zone NOT NULL,
  invoice_amount numeric(10,2),
  amount_received numeric(10,2),
  manual boolean NOT NULL DEFAULT false,
  reconciled boolean NOT NULL DEFAULT false,
  created_by integer NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by integer NOT NULL,
  last_modified_date timestamp without time zone DEFAULT now(),
  CONSTRAINT billing_cho_pkey PRIMARY KEY (id),
  CONSTRAINT billing_cho_ref_cho_fkey FOREIGN KEY (cho_id)
      REFERENCES chorganisation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_cho_ref_webuser_created_fkey FOREIGN KEY (created_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_cho_ref_webuser_modified_fkey FOREIGN KEY (last_modified_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE billing_cho_detail
(
  id serial NOT NULL,
  billing_cho_id integer NOT NULL,
  claim_reference_id integer NOT NULL,
  received_date timestamp without time zone,
  "comment" character varying(128),
  net_claim_cost numeric(8,2) NOT NULL,
  amount_received numeric(8,2) NOT NULL,
  reconciled boolean NOT NULL DEFAULT false,
  created_by integer NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by integer NOT NULL,
  last_modified_date timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT billing_cho_detail_pkey PRIMARY KEY (id),
  CONSTRAINT billing_cho_detail_ref_billing_cho_fkey FOREIGN KEY (billing_cho_id)
      REFERENCES billing_cho (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_cho_detail_ref_claim_fkey FOREIGN KEY (claim_reference_id)
      REFERENCES claim (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_cho_detail_ref_webuser_created_fkey FOREIGN KEY (created_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_cho_detail_ref_webuser_modified_fkey FOREIGN KEY (last_modified_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE billing_cho_rate
(
  id serial NOT NULL,
  chorganisation_id integer NOT NULL,
  min_volume integer NOT NULL,
  max_volume integer,
  fee numeric(5,2) NOT NULL,
  created_by integer NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by integer NOT NULL,
  last_modified_date timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT billing_cho_rate_pkey PRIMARY KEY (id),
  CONSTRAINT bcr_webuser_created_fkey FOREIGN KEY (created_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT bcr_webuser_modified_fkey FOREIGN KEY (last_modified_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT chochargingrates_fkey FOREIGN KEY (chorganisation_id)
      REFERENCES chorganisation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE billing_insurer
(
  id serial NOT NULL,
  insurer_id integer NOT NULL,
  schedule_name character varying(255) NOT NULL,
  date_from timestamp without time zone NOT NULL,
  date_to timestamp without time zone NOT NULL,
  invoice_amount numeric(10,2),
  amount_received numeric(10,2),
  manual boolean NOT NULL DEFAULT false,
  reconciled boolean NOT NULL DEFAULT false,
  created_by integer NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by integer NOT NULL,
  last_modified_date timestamp without time zone DEFAULT now(),
  CONSTRAINT billing_insurer_pkey PRIMARY KEY (id),
  CONSTRAINT billing_insurer_ref_insurer_fkey FOREIGN KEY (insurer_id)
      REFERENCES insurer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_insurer_ref_webuser_created_fkey FOREIGN KEY (created_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_insurer_ref_webuser_modified_fkey FOREIGN KEY (last_modified_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE billing_insurer_detail
(
  id serial NOT NULL,
  billing_insurer_id integer NOT NULL,
  claim_reference_id integer NOT NULL,
  received_date timestamp without time zone,
  "comment" character varying(128),
  net_claim_cost numeric(8,2) NOT NULL,
  amount_received numeric(8,2) NOT NULL,
  reconciled boolean NOT NULL DEFAULT false,
  created_by integer NOT NULL,
  created_date timestamp without time zone NOT NULL DEFAULT now(),
  last_modified_by integer NOT NULL,
  last_modified_date timestamp without time zone NOT NULL DEFAULT now(),
  CONSTRAINT billing_insurer_detail_pkey PRIMARY KEY (id),
  CONSTRAINT billing_insurer_detail_ref_billing_insurer_fkey FOREIGN KEY (billing_insurer_id)
      REFERENCES billing_insurer (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_insurer_detail_ref_claim_fkey FOREIGN KEY (claim_reference_id)
      REFERENCES claim (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_insurer_detail_ref_webuser_created_fkey FOREIGN KEY (created_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT billing_insurer_detail_ref_webuser_modified_fkey FOREIGN KEY (last_modified_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
