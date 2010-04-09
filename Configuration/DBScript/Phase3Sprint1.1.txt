alter table claim alter column percentage_liability_accepted drop not null;
alter table claim alter column percentage_liability_accepted type numeric(5,2);
alter table claim add column percentage_liability_cho numeric(5,2)  ;
alter table claim add column liability_agreed_date timestamp;
alter table claim add column liability_status smallint;

alter table invoice rename column total_to_pay to full_total_to_pay;
alter table invoice rename column original_total_to_pay to original_full_total_to_pay;
alter table invoice add column total_to_pay numeric(10,2) not null DEFAULT 0.00;
alter table invoice add column original_total_to_pay numeric(10,2) not null DEFAULT 0.00;

--------------------------------------------------------------------------------
--admin.Billing
--------------------------------------------------------------------------------
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('admin.Billing',false,false);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='admin.Billing'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHOX_ADMIN', 2, (select id from accessibility where name='admin.Billing'));



--------------------------------------------------------------------------------
--extraAction.updateLiability
--------------------------------------------------------------------------------
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateInsurerClaimNumber.AwaitingLiabilityResolution',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateInsurerClaimNumber.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateInsurerClaimNumber.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateInsurerClaimNumber.AwaitingLiabilityResolution'));

----------------AwaitingCarHireInfo
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.AwaitingCarHireInfo',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.AwaitingCarHireInfo'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.AwaitingCarHireInfo'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.AwaitingCarHireInfo'));

----------------AwaitingInvoiceData
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.AwaitingInvoiceData',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.AwaitingInvoiceData'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.AwaitingInvoiceData'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.AwaitingInvoiceData'));

----------------ContestedInvoiceReferredToCHO
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.ContestedInvoiceReferredToCHO',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.ContestedInvoiceReferredToCHO'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.ContestedInvoiceReferredToCHO'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.ContestedInvoiceReferredToCHO'));

----------------ContestedInvoiceReferredToInsurer
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.ContestedInvoiceReferredToInsurer',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.ContestedInvoiceReferredToInsurer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.ContestedInvoiceReferredToInsurer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.ContestedInvoiceReferredToInsurer'));

----------------InvoiceEscalated
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.InvoiceEscalated',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.InvoiceEscalated'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceEscalated'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceEscalated'));

----------------InvoiceEscalatedToHandler
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.InvoiceEscalatedToHandler',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.InvoiceEscalatedToHandler'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceEscalatedToHandler'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceEscalatedToHandler'));

----------------InvoiceReferredToEngineer
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.InvoiceReferredToEngineer',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.InvoiceReferredToEngineer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceReferredToEngineer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceReferredToEngineer'));

----------------InvoiceApprovedByBRE
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.InvoiceApprovedByBRE',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.InvoiceApprovedByBRE'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceApprovedByBRE'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceApprovedByBRE'));
----------------AwaitingCarHireInfo
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.ClaimRejected',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.ClaimRejected'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.ClaimRejected'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.ClaimRejected'));

----------------InvoiceReferredToClaimsHandler
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.InvoiceReferredToClaimsHandler',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.InvoiceReferredToClaimsHandler'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceReferredToClaimsHandler'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceReferredToClaimsHandler'));

----------------InvoiceDataCalculationIncorrect
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('extraAction.updateLiability.InvoiceDataCalculationIncorrect',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='extraAction.updateLiability.InvoiceDataCalculationIncorrect'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceDataCalculationIncorrect'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='extraAction.updateLiability.InvoiceDataCalculationIncorrect'));

--------------------------------------------------------------------------------
--action.updateLiability
--------------------------------------------------------------------------------
----------------action.updateLiability
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('action.updateLiability.AwaitingLiabilityResolution',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='action.updateLiability.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='action.updateLiability.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='action.updateLiability.AwaitingLiabilityResolution'));

--------------------------
--extraAction.updateLiability Not required
--AwaitingInvoicePayment
--ClaimClosed
--ClaimPending
--ClaimReferredToEngineer
--ClaimReferredToFNOL

--ClaimRejectionAccepted
--ClaimRejectionContested
--ClaimUnacknowledgedRouted
--ClaimUnacknowledgedUnassigned
--ClaimUpdatedByEngineer
--InvoicePaymentLogged
--InvoiceRejectionAccepted


---------------------------New queue Awaiting Liability Resolution

insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('filter.AwaitingLiabilityResolution',false,false);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='filter.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 1, (select id from accessibility where name='filter.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 1, (select id from accessibility where name='filter.AwaitingLiabilityResolution'));

--------------------- Claim details visibility

insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('tab.ClaimDetail.AwaitingLiabilityResolution',false,false);
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('tab.History.AwaitingLiabilityResolution',false,false);
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('tab.PaymentPack.AwaitingLiabilityResolution',false,false);
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('tab.HireMonitoring.AwaitingLiabilityResolution',false,false);
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('tab.Notes.AwaitingLiabilityResolution',false,false);
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('tab.InvoiceDetail.AwaitingLiabilityResolution',false,false);
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('tab.AuditTrail.AwaitingLiabilityResolution',false,false);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 1, (select id from accessibility where name='tab.ClaimDetail.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 1, (select id from accessibility where name='tab.History.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 1, (select id from accessibility where name='tab.PaymentPack.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 1, (select id from accessibility where name='tab.HireMonitoring.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 1, (select id from accessibility where name='tab.Notes.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 1, (select id from accessibility where name='tab.InvoiceDetail.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 1, (select id from accessibility where name='tab.AuditTrail.AwaitingLiabilityResolution'));


---------------------------New queue Liability Update

insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('filter.LiabilityUpdate',false,false);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='filter.LiabilityUpdate'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 1, (select id from accessibility where name='filter.LiabilityUpdate'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 1, (select id from accessibility where name='filter.LiabilityUpdate'));



--------------------- Liability Update notification accessibility entry
insert into accessibility(name,is_workgroup_check,is_ownership_check) values ('notification.NotificationNotesNotification.AwaitingLiabilityResolution',true,true);
insert into accessibility_item (role, access_right, accessibility_id) values ('ALL', 0, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingLiabilityResolution'));

insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimUnacknowledgedUnrouted'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimUnacknowledgedRouted'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimRejected'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimRejectionAccepted'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimRejectionContested'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingCarHireInfo'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingInvoiceData'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceDataCalculationIncorrect'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceApprovedByBRE'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceEscalated'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceEscalatedToHandler'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ContestedInvoiceReferredToInsurer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ContestedInvoiceReferredToCHO'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceRejectionAccepted'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingInvoicePayment'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoicePaymentLogged'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimReferredToEngineer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimReferredToFNOL'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimClosed'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimPending'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceReferredToClaimsHandler'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.PaymentReceived'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimUpdatedByEngineer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceReferredToEngineer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_OPR', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingLiabilityResolution'));



insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimUnacknowledgedUnrouted'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimUnacknowledgedRouted'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimRejected'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimRejectionAccepted'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimRejectionContested'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingCarHireInfo'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingInvoiceData'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceDataCalculationIncorrect'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceApprovedByBRE'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceEscalated'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceEscalatedToHandler'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ContestedInvoiceReferredToInsurer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ContestedInvoiceReferredToCHO'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceRejectionAccepted'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingInvoicePayment'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoicePaymentLogged'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimReferredToEngineer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimReferredToFNOL'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimClosed'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimPending'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceReferredToClaimsHandler'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.PaymentReceived'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.ClaimUpdatedByEngineer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.InvoiceReferredToEngineer'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_CHO_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingLiabilityResolution'));



insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_CH', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingLiabilityResolution'));
insert into accessibility_item (role, access_right, accessibility_id) values ('ROLE_INS_MNG', 2, (select id from accessibility where name='notification.NotificationNotesNotification.AwaitingLiabilityResolution'));
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

-- billing_cho_rate 

INSERT INTO billing_cho_rate ( chorganisation_id, min_volume, max_volume, fee, created_by, last_modified_by) 
VALUES ( (select id from chorganisation where name = 'Drive Assist UK Ltd'), 0, 200, 1.20, 
(select id from web_user where email = 'admin@chox.com'),  (select id from web_user where email = 'admin@chox.com' ));

INSERT INTO billing_cho_rate ( chorganisation_id, min_volume, max_volume, fee, created_by, last_modified_by)  
VALUES ( (select id from chorganisation where name = 'Drive Assist UK Ltd'), 200, 400, 1.10, 
(select id from web_user where email = 'admin@chox.com'),  (select id from web_user where email = 'admin@chox.com' ));

INSERT INTO billing_cho_rate ( chorganisation_id, min_volume, max_volume, fee, created_by, last_modified_by)  
VALUES ( (select id from chorganisation where name = 'Drive Assist UK Ltd'), 400, 600, 1.00, 
(select id from web_user where email = 'admin@chox.com'),  (select id from web_user where email = 'admin@chox.com' ));

INSERT INTO billing_cho_rate ( chorganisation_id, min_volume, max_volume, fee, created_by, last_modified_by)  
VALUES ( (select id from chorganisation where name = 'Drive Assist UK Ltd'), 600, 800, 0.90, 
(select id from web_user where email = 'admin@chox.com'),  (select id from web_user where email = 'admin@chox.com' ));

INSERT INTO billing_cho_rate ( chorganisation_id, min_volume, max_volume, fee, created_by, last_modified_by)  
VALUES ( (select id from chorganisation where name = 'Drive Assist UK Ltd'), 800, NULL, 0.80, 
(select id from web_user where email = 'admin@chox.com'),  (select id from web_user where email = 'admin@chox.com' ));


-- DROP VIEW ---------------------------------------------------

DROP VIEW rpt_claim_invoice;

-- CREATE VIEW -------------------------------------------------

CREATE OR REPLACE VIEW rpt_claim_invoice AS
 SELECT invoice.id, invoice.date_invoiced, invoice.handling_invoice_no, invoice.claim_invoice_no, invoice.cdw_qty,
 invoice.automatic_qty, invoice.sat_nav_qty, invoice.estate_qty, invoice.baby_seat_qty, invoice.tow_bars_qty, invoice.non_standard_insurance_premium_qty,
 invoice.admin_qty, invoice.roof_rack_qty, invoice.dual_control_qty, invoice.delivery_collection_qty, invoice.created_by, invoice.created_date,
 invoice.last_modified_by, invoice.last_modified_date, invoice.hire_net, invoice.hire_vat, invoice.hire_gross, invoice.repair_net,
 invoice.repair_vat, invoice.repair_gross, invoice.engineer_fee_net, invoice.engineer_fee_vat, invoice.engineer_fee_gross, invoice.storage_recovery_net,
 invoice.storage_recovery_vat, invoice.storage_recovery_gross, invoice.total_net, invoice.total_vat, invoice.total_gross, invoice.claims_handling_invoice_amount,
 invoice.deduction_for_claims_handling_fee, invoice.discount, invoice.total_to_pay, invoice.cdw_fee, invoice.automatic_fee, invoice.sat_nav_fee, invoice.estate_fee,
 invoice.baby_seat_fee, invoice.tow_bars_fee, invoice.non_standard_insurance_premium_fee, invoice.admin_fee, invoice.roof_rack_fee, invoice.dual_control_fee,
 invoice.delivery_collection_fee, invoice.is_payment_mode, invoice.is_engineer_decision_approved, invoice.engineer_invoice_review_notes, invoice.rejection_reason,
 invoice.hire_rate_charged_per_day, invoice.excess_amount_collected, invoice.vat_amount_collected, invoice.penalty_charge AS penalty_charge, invoice.penalty_alert_qty AS penalty_alert_qty,
 invoice.penalty_charge_applied_date, claim.id AS claim_id, claim.status, claim.cho_reference, claim.claim_number, claim.insurer_id, claim.chorganisation_id,
 third_party.first_name AS policy_holder_first_name, third_party.last_name AS policy_holder_surname_name, third_party.vehicle_registration AS vehicle_registration_number,
 claim.created_date AS claim_created_date, claim.vehicle_hire_id AS claim_vehicle_hire_id, workgroup.name AS workgroup, claim.workgroup_id, claim.claim_owner_id AS owner,
 invoice.original_total_to_pay,claim.percentage_liability_accepted,claim.percentage_liability_cho,invoice.original_full_total_to_pay
   FROM claim claim
   JOIN invoice invoice ON claim.invoice_id = invoice.id
   LEFT JOIN workgroup workgroup ON workgroup.id = claim.workgroup_id
   LEFT JOIN third_party third_party ON third_party.id = claim.third_party_id;

-- DROP FUNCTION ---------------------------------------------------

DROP FUNCTION sqlrunstatusreport(integer);

-- CREATE FUNCTION -------------------------------------------------

CREATE OR REPLACE FUNCTION sqlrunstatusreport(integer)
  RETURNS boolean AS
$BODY$

DECLARE
currDate timestamp;
isSuccess boolean;
userId int;

BEGIN
currDate=now();
isSuccess = false;
userId=$1;

	-- INSERT ALL RECORD PER INSURER / CH HIRE / STATUS
	-- WITH TOTAL ACCEPT / REJECTED COUNT FOR {Accumulative Section}
	BEGIN

		insert into claim_summary (process_date, insurer_id, chorganisation_id, status, a_total_claim_count, a_total_inv_count, created_by, created_date)
		select currDate as process_date, claim.insurer_id, claim.chorganisation_id, audit.new_status as status ,
		count(distinct claim.id) as a_total_claim_count, count(distinct invoice.id) as a_total_inv_count, userId as created_by, currDate as created_date
		from audit_trail audit
		inner join claim claim on audit.claim_id=claim.id
		left outer join invoice invoice on claim.invoice_id=invoice.id
		group by claim.insurer_id, claim.chorganisation_id, audit.new_status;

		isSuccess = true;

	END;

	BEGIN
	-- UPDATE TOTAL ACCEPT / REJECTED COUNT FOR {Week Section}

		update claim_summary
		set
		w_total_claim_count = a.w_total_claim_count,
		w_total_inv_count = a.w_total_inv_count
		from (
			select claim.insurer_id as insurer_id, claim.chorganisation_id as chorganisation_id, audit.new_status as status,
			count(distinct claim.id) as w_total_claim_count, count(distinct invoice.id) as w_total_inv_count
			from audit_trail audit
			inner join claim claim on audit.claim_id=claim.id
			left outer join invoice invoice on claim.invoice_id=invoice.id
			where date(audit.update_date) between SqlGetDayOfWeek() and date(now())
			group by claim.insurer_id, claim.chorganisation_id, audit.new_status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;

		isSuccess = true;

	END;

	BEGIN
	-- UPDATE TOTAL ACCEPT / REJECTED COUNT FOR {Month Section}

		update claim_summary
		set
		m_total_claim_count = a.m_total_claim_count,
		m_total_inv_count = a.m_total_inv_count
		from (
			select claim.insurer_id as insurer_id, claim.chorganisation_id as chorganisation_id, audit.new_status as status,
			count(distinct claim.id) as m_total_claim_count, count(distinct invoice.id) as m_total_inv_count
			from audit_trail audit
			inner join claim claim on audit.claim_id=claim.id
			left outer join invoice invoice on claim.invoice_id=invoice.id
			where date(audit.update_date) between SqlGetDayOfMonth() and date(now())
			group by claim.insurer_id, claim.chorganisation_id, audit.new_status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;

		isSuccess = true;

	END;

	-- ACCUMULATIVE
	BEGIN
		update claim_summary set
		a_count = a.a_count,
		a_inv_count = a.a_inv_count,
		a_total_to_pay = a.a_total_to_pay,
		a_penalty_charge = a.a_penalty_charge
		from (
			select claim.insurer_id as insurer_id, claim.chorganisation_id as chorganisation_id, audit.new_status as status,
			count(*) as a_count,
			count(invoice.id) as a_inv_count,
			case when sum(invoice.total_to_pay) is null then 0 else sum(invoice.total_to_pay) end as a_total_to_pay,
			case when sum(invoice.penalty_charge) is null then 0 else sum(invoice.penalty_charge) end as a_penalty_charge
			from audit_trail audit
			inner join (select a.claim_id, max(a.update_date) as max_update_date from audit_trail a group by a.claim_id) max_audit on audit.claim_id=max_audit.claim_id and audit.update_date=max_audit.max_update_date
			inner join claim claim on audit.claim_id = claim.id
			left outer join invoice invoice on claim.invoice_id = invoice.id
			group by claim.insurer_id, claim.chorganisation_id, audit.new_status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;

		isSuccess = true;
	END;

	-- ACCUMULATIVE correction for status 'AwaitingInvoicePayment - need to count all claims passing through this state
	BEGIN
		update claim_summary set
		a_count = a.a_count,
		a_inv_count = a.a_inv_count,
		a_total_to_pay = a.a_total_to_pay,
		a_penalty_charge = a.a_penalty_charge
		from (
			select claim.insurer_id as insurer_id, claim.chorganisation_id as chorganisation_id, audit.new_status as status,
			count(*) as a_count,
			count(invoice.id) as a_inv_count,
			case when sum(invoice.total_to_pay) is null then 0 else sum(invoice.total_to_pay) end as a_total_to_pay,
			case when sum(invoice.penalty_charge) is null then 0 else sum(invoice.penalty_charge) end as a_penalty_charge
			from audit_trail audit
			inner join claim claim on audit.claim_id = claim.id
			left outer join invoice invoice on claim.invoice_id = invoice.id
			where audit.new_status in ('AwaitingInvoicePayment', 'InvoicePaymentLogged')
			group by claim.insurer_id, claim.chorganisation_id, audit.new_status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;
	END;

	-- WEEKLY
	BEGIN
		update claim_summary set
		w_count = vw_claim_weekly_summary.w_count,
		w_inv_count = vw_claim_weekly_summary.w_inv_count,
		w_total_to_pay = vw_claim_weekly_summary.w_total_to_pay,
		w_penalty_charge = vw_claim_weekly_summary.w_penalty_charge
		from vw_claim_weekly_summary
		where claim_summary.insurer_id=vw_claim_weekly_summary.insurer_id
		and claim_summary.chorganisation_id=vw_claim_weekly_summary.chorganisation_id
		and claim_summary.status = vw_claim_weekly_summary.status;
	END;
	-- WEEKLY correction for status 'AwaitingInvoicePayment - need to count all claims passing through this state
	BEGIN
		update claim_summary set
		w_count = a.w_count,
		w_inv_count = a.w_inv_count,
		w_total_to_pay = a.w_total_to_pay,
		w_penalty_charge = a.w_penalty_charge
		from
			(select claim.insurer_id as insurer_id, claim.chorganisation_id as chorganisation_id, audit.new_status as status, count(*) as w_count,
			count(invoice.id) as w_inv_count,
			case when sum(invoice.total_to_pay) is null then 0 else sum(invoice.total_to_pay) end as w_total_to_pay,
			case when sum(invoice.penalty_charge) is null then 0 else sum(invoice.penalty_charge) end as w_penalty_charge
			 from audit_trail audit
			 inner join claim claim on audit.claim_id = claim.id
			left outer join invoice invoice on claim.invoice_id = invoice.id
			 where date(audit.update_date) between SqlGetDayOfWeek() and date(now())
			   and audit.new_status in ('AwaitingInvoicePayment', 'InvoicePaymentLogged')
			 group by claim.insurer_id, claim.chorganisation_id, audit.new_status
			) a
		where claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;
	END;

	-- MONTH
	BEGIN

		update claim_summary set
		m_count = vw_claim_monthly_summary.m_count,
		m_inv_count = vw_claim_monthly_summary.m_inv_count,
		m_total_to_pay = vw_claim_monthly_summary.m_total_to_pay,
		m_penalty_charge = vw_claim_monthly_summary.m_penalty_charge
		from vw_claim_monthly_summary
		where claim_summary.insurer_id=vw_claim_monthly_summary.insurer_id
		and claim_summary.chorganisation_id=vw_claim_monthly_summary.chorganisation_id
		and claim_summary.status = vw_claim_monthly_summary.status;

	END;
	-- MONTH  correction for status 'AwaitingInvoicePayment - need to count all claims passing through this state
	BEGIN
		update claim_summary set
		m_count = a.m_count,
		m_inv_count = a.m_inv_count,
		m_total_to_pay = a.m_total_to_pay,
		m_penalty_charge = a.m_penalty_charge
		from
			(select claim.insurer_id as insurer_id, claim.chorganisation_id as chorganisation_id,  audit.new_status as status, count(*) as m_count,
			count(invoice.id) as m_inv_count,
			case when sum(invoice.total_to_pay) is null then 0 else sum(invoice.total_to_pay) end as m_total_to_pay,
			case when sum(invoice.penalty_charge) is null then 0 else sum(invoice.penalty_charge) end as m_penalty_charge
			 from audit_trail audit
			 inner join claim claim on audit.claim_id = claim.id
			left outer join invoice invoice on claim.invoice_id = invoice.id
			 where date(audit.update_date) between SqlGetDayOfMonth() and date(now())
			   and audit.new_status in ('AwaitingInvoicePayment', 'InvoicePaymentLogged')
			 group by claim.insurer_id, claim.chorganisation_id,audit.new_status
			) a
		where claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;
	END;

	/** UPDATE CLAIM CREATED INFORMATION - START **/

	-- WEEKLY
	BEGIN
		update claim_summary set w_claim_created_count = a.claim_created_count
		from (
			select insurer_id, chorganisation_id, status, count(*) as claim_created_count
			from claim
			where date(created_date) between SqlGetDayOfWeek() and date(now())
			group by insurer_id, chorganisation_id, status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;
	END;

	-- MONTH
	BEGIN

		update claim_summary set m_claim_created_count = a.claim_created_count
		from (
			select insurer_id, chorganisation_id, status, count(*) as claim_created_count
			from claim
			where date(created_date) between SqlGetDayOfMonth() and date(now())
			group by insurer_id, chorganisation_id, status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;

	END;

	-- ACCUMULATE
	BEGIN
		update claim_summary set a_claim_created_count = a.claim_created_count
		from (
			select insurer_id, chorganisation_id, status, count(*) as claim_created_count
			from claim
			group by insurer_id, chorganisation_id, status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;
	END;

	/** UPDATE INVOICE CREATED INFORMATION - START **/
	-- WEEKLY
	BEGIN
		update claim_summary
		set
		w_inv_created_count = a.inv_created_count,
		w_inv_created_amt = a.inv_created_amt
		from (
			select insurer_id, chorganisation_id, status, count(*) as inv_created_count,
			case when sum(original_total_to_pay) is null then 0.00 else sum(original_total_to_pay) end as inv_created_amt
			from rpt_claim_invoice
			where date(created_date) between SqlGetDayOfWeek() and date(now())
			group by insurer_id, chorganisation_id, status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;

	END;

	-- MONTH
	BEGIN
		update claim_summary
		set
		m_inv_created_count = a.inv_created_count,
		m_inv_created_amt = a.inv_created_amt
		from (
			select insurer_id, chorganisation_id, status, count(*) as inv_created_count,
			case when sum(original_total_to_pay) is null then 0.00 else sum(original_total_to_pay) end as inv_created_amt
			from rpt_claim_invoice
			where date(created_date) between SqlGetDayOfMonth() and date(now())
			group by insurer_id, chorganisation_id, status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;

	END;

	BEGIN
		update claim_summary
		set
		a_inv_created_count = a.inv_created_count,
		a_inv_created_amt = a.inv_created_amt
		from (
			select insurer_id, chorganisation_id, status, count(*) as inv_created_count,
			case when sum(original_total_to_pay) is null then 0.00 else sum(original_total_to_pay) end as inv_created_amt
			from rpt_claim_invoice
			group by insurer_id, chorganisation_id, status
		) a
		where
		claim_summary.insurer_id=a.insurer_id
		and claim_summary.chorganisation_id=a.chorganisation_id
		and claim_summary.status = a.status;

	END;

	/** UPDATE INVOICE CREATED INFORMATION - END **/

	BEGIN

		insert into claim_summary_process (process_date, created_date, created_by) values (currDate, currDate, userId);

	END;

	BEGIN

		delete from claim_summary where process_date<currDate;

	END;

return isSuccess;

END;
$BODY$
  LANGUAGE 'plpgsql' VOLATILE
  COST 100;
