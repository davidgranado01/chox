--
-- CHOX-148: Visibility of Notes
--
ALTER TABLE comment ADD COLUMN review_required boolean;
ALTER TABLE comment ADD COLUMN task_id integer;


--
-- CHOX-152: Usable question for CHO/Insurer upload
--
ALTER TABLE customer ALTER COLUMN is_usable DROP NOT NULL;
ALTER TABLE customer ALTER COLUMN is_usable DROP default;

--
-- CHOX-149: Insurer Last Review Date
--
ALTER TABLE claim ADD COLUMN last_review_date timestamp without time zone;

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.AwaitingCarHireInfo', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.AwaitingInvoiceData', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.AwaitingInvoicePayment', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.AwaitingLiabilityResolution', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.AwaitingLitigationOutcome', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimPending', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimReferredToEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimReferredToFNOL', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimRejected', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimRejectionContested', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimUnacknowledgedRouted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimUnacknowledgedUnassigned', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimUnacknowledgedUnrouted', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ClaimUpdatedByEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ContestedInvoiceReferredToCHO', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ContestedInvoiceReferredToInsurer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.InvoiceApprovedByBRE', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.InvoiceDataCalculationIncorrect', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.InvoiceEscalatedToHandler', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.InvoiceReferredToClaimsHandler', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.InvoiceReferredToEngineer', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ManualInvoiceBREApproved', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ManualInvoiceBRERejected', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.ManualInvoiceContested', false, false, false, false);
INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.LastReviewDate.SubscriberClaimRejected', false, false, false, false);


INSERT INTO accessibility_item (role,access_right,accessibility_id)
   SELECT 'ALL',0, id FROM accessibility WHERE name like 'activity.LastReviewDate.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CH',1, id FROM accessibility WHERE name like 'activity.LastReviewDate.%';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_MNG',1, id FROM accessibility WHERE name like 'activity.LastReviewDate.%';

--
-- CHOX-153: Ability To Reject A Manual Claim
--

-- Queue Visibility
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                          check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                          check_supplier_ownership)
    SELECT 'filter.RejectedManualClaims', false, false, false, false, false, false, false;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ALL', 0, id FROM accessibility WHERE name ='filter.RejectedManualClaims';

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 1, id FROM accessibility WHERE name ='filter.RejectedManualClaims';

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 1, id FROM accessibility WHERE name ='filter.RejectedManualClaims';

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 1, id FROM accessibility WHERE name ='filter.RejectedManualClaims';

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 1, id FROM accessibility WHERE name ='filter.RejectedManualClaims';

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 1, id FROM accessibility WHERE name ='filter.RejectedManualClaims';

-- Activity accessibility
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                              check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                              check_supplier_ownership, claim_type)
    SELECT 'activity.ClaimRejection.ClaimPending', false, false, false, false, false, false, false, 17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimPending' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimPending' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimPending' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimPending' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimPending' and claim_type=17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                              check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                              check_supplier_ownership, claim_type)
    SELECT 'activity.ClaimRejection.ClaimRejectionContested', false, false, false, false, false, false, false, 17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimRejectionContested' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimRejectionContested' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimRejectionContested' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimRejectionContested' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimRejectionContested' and claim_type=17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                                  check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                                  check_supplier_ownership, claim_type)
    SELECT 'activity.ClaimRejection.ClaimUnacknowledgedRouted', false, false, false, false, false, false, false, 17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedRouted' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedRouted' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedRouted' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedRouted' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedRouted' and claim_type=17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                                  check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                                  check_supplier_ownership, claim_type)
    SELECT 'activity.ClaimRejection.ClaimUnacknowledgedUnassigned', false, false, false, false, false, false, false, 17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnassigned' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnassigned' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnassigned' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnassigned' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnassigned' and claim_type=17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                                      check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                                      check_supplier_ownership, claim_type)
    SELECT 'activity.ClaimRejection.ClaimUnacknowledgedUnrouted', false, false, false, false, false, false, false, 17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnrouted' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnrouted' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnrouted' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnrouted' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUnacknowledgedUnrouted' and claim_type=17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                                      check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                                      check_supplier_ownership, claim_type)
    SELECT 'activity.ClaimRejection.ClaimUpdatedByEngineer', false, false, false, false, false, false, false, 17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUpdatedByEngineer' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUpdatedByEngineer' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUpdatedByEngineer' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUpdatedByEngineer' and claim_type=17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 1, id FROM accessibility WHERE name ='activity.ClaimRejection.ClaimUpdatedByEngineer' and claim_type=17;


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership, claim_type)
        SELECT 'activity.ClaimRejectionContest.ClaimRejected', false, false, false, false, false, false, false, 17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionContest.ClaimRejected' and claim_type=17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionContest.ClaimRejected' and claim_type=17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionContest.ClaimRejected' and claim_type=17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionContest.ClaimRejected' and claim_type=17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionContest.ClaimRejected' and claim_type=17;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership, claim_type)
    SELECT 'activity.ClaimRejectionAccept.ClaimRejected', false, false, false, false, false, false, false, 17;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionAccept.ClaimRejected' and claim_type=17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionAccept.ClaimRejected' and claim_type=17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionAccept.ClaimRejected' and claim_type=17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CR', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionAccept.ClaimRejected' and claim_type=17;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_COM', 2, id FROM accessibility WHERE name ='activity.ClaimRejectionAccept.ClaimRejected' and claim_type=17;


--
-- CHOX-150: Insurer LOU Dates
--
ALTER TABLE insurer ADD COLUMN enable_lou_dates boolean not null default false;
ALTER TABLE claim ADD COLUMN insurer_hire_monitoring_detail_id integer;
CREATE TABLE insurer_hire_monitoring_detail (
    id serial not null,
    inspection_booked_date timestamp without time zone DEFAULT null,
    inspection_date timestamp without time zone DEFAULT null,
    repair_authorised_date timestamp without time zone DEFAULT null,
    repair_book_in_date timestamp without time zone DEFAULT null,
    repair_commenced_date timestamp without time zone DEFAULT null,
    repair_completion_date timestamp without time zone DEFAULT null,
    total_loss_offer_made timestamp without time zone DEFAULT null,
    total_loss_offer_accepted timestamp without time zone DEFAULT null,
    total_loss_check_issued timestamp without time zone DEFAULT null,
    total_loss_check_received timestamp without time zone DEFAULT null,
    labour_rate numeric(10,2),
    labour_hour numeric(8,2),
    labour_cost numeric(10,2),
    claimant_impecunious boolean,
    who_managed_repair varchar(32),
    "version" integer NOT NULL,
    created_by integer NOT NULL,
    completed_by integer,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT insurer_hire_monitoring_pkey PRIMARY KEY (id),
    CONSTRAINT insurer_hire_monitoring_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_hire_monitoring_webuser_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE insurer_hire_monitoring_detail TO chox_user;
GRANT SELECT ON TABLE insurer_hire_monitoring_detail TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE insurer_hire_monitoring_detail_id_seq TO chox_user;

ALTER TABLE claim ADD CONSTRAINT insurer_hire_monitoring_detail_id_fkey FOREIGN KEY (insurer_hire_monitoring_detail_id) REFERENCES insurer_hire_monitoring_detail(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.AwaitingCarHireInfo', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingCarHireInfo';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingCarHireInfo';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingCarHireInfo';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingCarHireInfo';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.AwaitingInvoiceData', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingInvoiceData';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingInvoiceData';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingInvoiceData';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingInvoiceData';


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.AwaitingInvoicePayment', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingInvoicePayment';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingInvoicePayment';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingInvoicePayment';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingInvoicePayment';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.AwaitingLiabilityResolution', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingLiabilityResolution';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingLiabilityResolution';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingLiabilityResolution';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.AwaitingLiabilityResolution';


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimPending', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimPending';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimPending';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimPending';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimPending';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimReferredToEngineer', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimReferredToEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimReferredToEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimReferredToEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimReferredToEngineer';


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimReferredToFNOL', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimReferredToFNOL';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimReferredToFNOL';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimReferredToFNOL';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimReferredToFNOL';

 INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimRejected', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejected';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimRejectionContested', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejectionContested';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejectionContested';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejectionContested';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejectionContested';


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimUnacknowledgedRouted', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedRouted';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedRouted';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedRouted';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedRouted';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimUnacknowledgedUnassigned', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedUnassigned';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimUnacknowledgedUnrouted', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedUnrouted';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedUnrouted';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedUnrouted';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUnacknowledgedUnrouted';


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimUpdatedByEngineer', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUpdatedByEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUpdatedByEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUpdatedByEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimUpdatedByEngineer';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ContestedInvoiceReferredToCHO', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ContestedInvoiceReferredToCHO';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ContestedInvoiceReferredToCHO';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ContestedInvoiceReferredToCHO';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ContestedInvoiceReferredToCHO';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ContestedInvoiceReferredToInsurer', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ContestedInvoiceReferredToInsurer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ContestedInvoiceReferredToInsurer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ContestedInvoiceReferredToInsurer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ContestedInvoiceReferredToInsurer';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoiceApprovedByBRE', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceApprovedByBRE';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceApprovedByBRE';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceApprovedByBRE';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceApprovedByBRE';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoiceDataCalculationIncorrect', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceDataCalculationIncorrect';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceDataCalculationIncorrect';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceDataCalculationIncorrect';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceDataCalculationIncorrect';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoiceEscalated', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceEscalated';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceEscalated';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceEscalated';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceEscalated';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoiceEscalatedToHandler', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceEscalatedToHandler';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceEscalatedToHandler';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceEscalatedToHandler';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceEscalatedToHandler';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoiceReferredToClaimsHandler', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceReferredToClaimsHandler';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceReferredToClaimsHandler';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceReferredToClaimsHandler';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceReferredToClaimsHandler';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoiceReferredToEngineer', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
     SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceReferredToEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceReferredToEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceReferredToEngineer';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceReferredToEngineer';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoiceUnassigned', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceUnassigned';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.SubscriberClaimRejected', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.SubscriberClaimRejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.SubscriberClaimRejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.SubscriberClaimRejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.SubscriberClaimRejected';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.PaymentReceived', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.PaymentReceived';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimClosed', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimClosed';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoiceRejectionAccepted', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoiceRejectionAccepted';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.InvoicePaymentLogged', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.InvoicePaymentLogged';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                       check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                       check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ClaimRejectionAccepted', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ClaimRejectionAccepted';


CREATE TABLE insurer_hire_monitoring_ecd (
    id serial not null,
    claim_id integer,
    ecd_date timestamp without time zone NOT NULL,
    sequence integer NOT NULL,
    reason character varying(50),
    supporting_note character varying(1152),
    "version" integer NOT NULL,
    created_by integer NOT NULL,
    completed_by integer,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT insurer_hire_monitoring_ecd_pkey PRIMARY KEY (id),
    CONSTRAINT insurer_hire_monitoring_ecd_claim_id_fkey FOREIGN KEY (claim_id)
        REFERENCES claim (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_hire_monitoring_ecd_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_hire_monitoring_ecd_webuser_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE insurer_hire_monitoring_ecd TO chox_user;
GRANT SELECT ON TABLE insurer_hire_monitoring_ecd TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE insurer_hire_monitoring_ecd_id_seq TO chox_user;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ClaimUnacknowledgedUnrouted', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ClaimUnacknowledgedRouted', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
     SELECT 'activity.InsurerEcdUpdate.ClaimUnacknowledgedUnassigned', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ClaimReferredToFNOL', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ClaimReferredToEngineer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ClaimUpdatedByEngineer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.AwaitingCarHireInfo', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ClaimRejectionContested', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ClaimRejected', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.SubscriberClaimRejected', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ClaimPending', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.AwaitingCarHireInfo', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.AwaitingInvoiceData', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.AwaitingInvoicePayment', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.AwaitingLiabilityResolution', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ContestedInvoiceReferredToCHO', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.ContestedInvoiceReferredToInsurer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.InvoiceApprovedByBRE', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.InvoiceDataCalculationIncorrect', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.InvoiceEscalated', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.InvoiceEscalatedToHandler', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.InvoiceReferredToClaimsHandler', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.InvoiceReferredToEngineer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.InvoiceUnassigned', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.SubscriberClaimRejected', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.InsurerEcdUpdate.', false, false;

INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 1, id FROM accessibility WHERE name like 'activity.InsurerEcdUpdate.%';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 1, id FROM accessibility WHERE name like 'activity.InsurerEcdUpdate.%';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 1, id FROM accessibility WHERE name like 'activity.InsurerEcdUpdate.%';


CREATE TABLE insurer_vehicle_hire (
    id serial not null,
    rental_start timestamp without time zone,
    vehicle_class_id integer,
    "version" integer NOT NULL,
    created_by integer NOT NULL,
    completed_by integer,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT insurer_vehicle_hire_pkey PRIMARY KEY (id),
    CONSTRAINT insurer_vehicle_hire_vehicle_class_fkey FOREIGN KEY (vehicle_class_id)
        REFERENCES vehicle_class (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_vehicle_hire_webuser_created_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT insurer_vehicle_hire_webuser_modified_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS=FALSE
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE insurer_vehicle_hire TO chox_user;
GRANT SELECT ON TABLE insurer_vehicle_hire TO chox_mi;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE insurer_vehicle_hire_id_seq TO chox_user;
ALTER TABLE claim ADD COLUMN insurer_vehicle_hire_id integer;

ALTER TABLE claim ADD CONSTRAINT insurer_vehicle_hire_id_fkey FOREIGN KEY (insurer_vehicle_hire_id) REFERENCES insurer_vehicle_hire(id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
