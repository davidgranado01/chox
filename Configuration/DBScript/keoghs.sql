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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE keoghs_request TO chox_user;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE keoghs_request_id_seq TO chox_user;

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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE keoghs_request_score_message TO chox_user;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE keoghs_request_score_message_id_seq TO chox_user;


ALTER TABLE claim ADD COLUMN fraud_check_status int not null DEFAULT 0; -- 0 = not requested, 1 = queued, 2 = pending, 3 = available, -1 = error
--ALTER TABLE claim ADD COLUMN keoghs_client_batch_reference character varying;
ALTER TABLE claim ADD COLUMN keoghs_request_id integer;
ALTER TABLE claim ADD COLUMN sent_to_keoghs boolean not null default false;
ALTER TABLE claim ADD COLUMN fraud_result_acknowledged boolean not null default false;
ALTER TABLE claim ADD CONSTRAINT claim_keoghs_request_fkey FOREIGN KEY (keoghs_request_id) REFERENCES keoghs_request(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE bre_band ADD COLUMN fraud_check_enable boolean not null default false;

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimPending',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.AwaitingLitigationOutcome',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoiceDataCalculationIncorrect',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ContestedInvoiceReferredToCHO',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.AwaitingCarHireInfo',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.AwaitingInvoicePayment',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimUnacknowledgedRouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.SubscriberClaimRejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimRejectionContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoiceReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimUnacknowledgedUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ContestedInvoiceReferredToInsurer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.AwaitingLiabilityResolution',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimUnacknowledgedUnrouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimUpdatedByEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoiceApprovedByBRE',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoiceReferredToClaimsHandler',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoicePaymentLogged',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ManualInvoicePaid',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ManualInvoiceBREApproved',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoiceEscalated',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ManualInvoiceBRERejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimRejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.AwaitingInvoiceData',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ManualInvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ManualInvoiceContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoiceEscalatedToHandler',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimReferredToFNOL',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.PaymentReceived',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.InvoiceRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.ReferFraudCheck.ClaimClosed',FALSE,FALSE);

INSERT INTO accessibility_item (role,access_right,accessibility_id)
  SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name like 'activity.ReferFraudCheck.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
  SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name like 'activity.ReferFraudCheck.%';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimPending',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.AwaitingLitigationOutcome',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoiceDataCalculationIncorrect',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ContestedInvoiceReferredToCHO',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.AwaitingCarHireInfo',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.AwaitingInvoicePayment',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimUnacknowledgedRouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.SubscriberClaimRejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimRejectionContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoiceReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimUnacknowledgedUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ContestedInvoiceReferredToInsurer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.AwaitingLiabilityResolution',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimUnacknowledgedUnrouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimUpdatedByEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoiceApprovedByBRE',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoiceReferredToClaimsHandler',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoicePaymentLogged',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ManualInvoicePaid',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ManualInvoiceBREApproved',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoiceEscalated',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ManualInvoiceBRERejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimRejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.AwaitingInvoiceData',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ManualInvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ManualInvoiceContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoiceEscalatedToHandler',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimReferredToFNOL',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.PaymentReceived',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.InvoiceRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.RunFraudCheck.ClaimClosed',FALSE,FALSE);

INSERT INTO accessibility_item (role,access_right,accessibility_id)
  SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name like 'activity.RunFraudCheck.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
  SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name like 'activity.RunFraudCheck.%';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimPending',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.AwaitingLitigationOutcome',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoiceDataCalculationIncorrect',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ContestedInvoiceReferredToCHO',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.AwaitingCarHireInfo',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.AwaitingInvoicePayment',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimUnacknowledgedRouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.SubscriberClaimRejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimRejectionContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoiceReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimUnacknowledgedUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ContestedInvoiceReferredToInsurer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.AwaitingLiabilityResolution',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimUnacknowledgedUnrouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimUpdatedByEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoiceApprovedByBRE',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoiceReferredToClaimsHandler',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoicePaymentLogged',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ManualInvoicePaid',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ManualInvoiceBREApproved',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoiceEscalated',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ManualInvoiceBRERejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimRejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.AwaitingInvoiceData',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ManualInvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ManualInvoiceContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoiceEscalatedToHandler',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimReferredToFNOL',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.PaymentReceived',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.InvoiceRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('activity.AcknowledgeFraudCheck.ClaimClosed',FALSE,FALSE);

INSERT INTO accessibility_item (role,access_right,accessibility_id)
  SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name like 'activity.AcknowledgeFraudCheck.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
  SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name like 'activity.AcknowledgeFraudCheck.%';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimPending',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.AwaitingLitigationOutcome',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoiceDataCalculationIncorrect',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ContestedInvoiceReferredToCHO',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.AwaitingCarHireInfo',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.AwaitingInvoicePayment',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimUnacknowledgedRouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.SubscriberClaimRejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimRejectionContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoiceReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimUnacknowledgedUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ContestedInvoiceReferredToInsurer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.AwaitingLiabilityResolution',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimUnacknowledgedUnrouted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimUpdatedByEngineer',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoiceApprovedByBRE',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoiceReferredToClaimsHandler',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoicePaymentLogged',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ManualInvoicePaid',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ManualInvoiceBREApproved',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoiceEscalated',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ManualInvoiceBRERejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimRejected',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.AwaitingInvoiceData',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ManualInvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ManualInvoiceContested',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoiceEscalatedToHandler',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimReferredToFNOL',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.PaymentReceived',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.InvoiceRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check)
  values ('extraAction.fraudCheck.ClaimClosed',FALSE,FALSE);

INSERT INTO accessibility_item (role,access_right,accessibility_id)
  SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name like 'extraAction.fraudCheck.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
  SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name like 'extraAction.fraudCheck.%';
