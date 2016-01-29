-----------------------------------------------------
-- TO DO ITEM : 8.10.1 Audit Facility on Claims
-----------------------------------------------------

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) 
    values ('filter.ClaimsRequiringAudit',TRUE,TRUE);
INSERT into accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',1,id from accessibility where name ='filter.ClaimsRequiringAudit';
INSERT into accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ROLE_INS_CH',1,id from accessibility where name ='filter.ClaimsRequiringAudit';
INSERT into accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ROLE_INS_MNG',1,id from accessibility where name ='filter.ClaimsRequiringAudit';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SaveOrSubmitClaimAuditReview.PaymentReceived',TRUE,TRUE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SaveOrSubmitClaimAuditReview.PaymentReceived';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_MNG',2, id FROM accessibility WHERE name = 'activity.SaveOrSubmitClaimAuditReview.PaymentReceived';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CH',2, id FROM accessibility WHERE name = 'activity.SaveOrSubmitClaimAuditReview.PaymentReceived';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SaveOrSubmitClaimAuditReview.PaymentReceived';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.SaveOrSubmitClaimAuditReview.ManualInvoicePaid',TRUE,TRUE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.SaveOrSubmitClaimAuditReview.ManualInvoicePaid';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_MNG',2, id FROM accessibility WHERE name = 'activity.SaveOrSubmitClaimAuditReview.ManualInvoicePaid';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CH',2, id FROM accessibility WHERE name = 'activity.SaveOrSubmitClaimAuditReview.ManualInvoicePaid';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'activity.SaveOrSubmitClaimAuditReview.ManualInvoicePaid';


CREATE TABLE claim_audit_review (
    id serial not null,
    "version" integer NOT NULL,
    claim_type character varying(64),
    who_managed_repair character varying(64),
    hire_duration_not_acceptable_reason character varying(256),
    penalty_charge_avoidable_note character varying(256),
    total_loss boolean,
    hire_duration_acceptable boolean,
    repair_cost_exceeds_eng_rec boolean,
    within_abp_guidelines boolean,
    storage_claimed boolean,
    recovery_claimed boolean,
    hire_leakage boolean,
    penalty_charge_avoidable boolean,
    storage_claimed_correctly boolean,
    recovery_claimed_correctly boolean,
    claim_audit_review_completed boolean NOT NULL DEFAULT FALSE,
    hire_duration numeric(4,0),
    total_hire_cost numeric(10,2),
    total_repair_cost numeric(10,2),
    penalty_charges_paid numeric(10,2),
    hire_leakage_cost numeric(10,2),
    exceeded_repair_cost numeric(10,2),
    non_abp_guideline_repair_labour_rate numeric(10,2),
    audit_completed_date timestamp without time zone,
    customer_vehicle_class_id integer,
    hire_vehicle_class_id integer,
    created_by integer NOT NULL,
    completed_by integer,
    created_date timestamp without time zone NOT NULL DEFAULT now(),
    last_modified_by integer NOT NULL,
    last_modified_date timestamp without time zone DEFAULT now(),
    CONSTRAINT claim_audit_review_pkey PRIMARY KEY (id),
    CONSTRAINT claim_audit_review_customer_vehicle_class_fkey FOREIGN KEY (customer_vehicle_class_id)
            REFERENCES vehicle_class (id) MATCH SIMPLE
            ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT claim_audit_review_hire_vehicle_class_fkey FOREIGN KEY (hire_vehicle_class_id)
            REFERENCES vehicle_class (id) MATCH SIMPLE
            ON UPDATE NO ACTION ON DELETE NO ACTION,
     CONSTRAINT claim_audit_review_webuser_completed_fkey FOREIGN KEY (completed_by)
            REFERENCES web_user (id) MATCH SIMPLE
            ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT claim_audit_review_webuser_created_fkey FOREIGN KEY (created_by)
            REFERENCES web_user (id) MATCH SIMPLE
            ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT claim_audit_review_webuser_modified_fkey FOREIGN KEY (last_modified_by)
            REFERENCES web_user (id) MATCH SIMPLE
            ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
GRANT SELECT ON TABLE claim_audit_review TO chox_user;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE claim_audit_review_id_seq TO chox_user;

ALTER TABLE claim ADD COLUMN audit_review_id integer;
ALTER TABLE claim ADD CONSTRAINT audit_review_id_fkey FOREIGN KEY (audit_review_id) REFERENCES claim_audit_review (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;


----------------------
-- End of 8.10.1
----------------------

-----------------------------------------------------
-- TO DO ITEM : 8.10.2 More Actions Audit Options
-----------------------------------------------------

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) 
    VALUES ('extraAction.reviewClaimAudit.PaymentReceived',TRUE,TRUE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ROLE_INS_MNG',2, id FROM accessibility WHERE name = 'extraAction.reviewClaimAudit.PaymentReceived';
INSERT INTO accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ROLE_INS_CH',2, id FROM accessibility WHERE name = 'extraAction.reviewClaimAudit.PaymentReceived';
INSERT INTO accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.reviewClaimAudit.PaymentReceived';
INSERT INTO accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'extraAction.reviewClaimAudit.PaymentReceived';


INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) 
    VALUES ('extraAction.reviewClaimAudit.ManualInvoicePaid',TRUE,TRUE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ROLE_INS_MNG',2, id FROM accessibility WHERE name = 'extraAction.reviewClaimAudit.ManualInvoicePaid';
INSERT INTO accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ROLE_INS_CH',2, id FROM accessibility WHERE name = 'extraAction.reviewClaimAudit.ManualInvoicePaid';
INSERT INTO accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.reviewClaimAudit.ManualInvoicePaid';
INSERT INTO accessibility_item (role,access_right,accessibility_id) 
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'extraAction.reviewClaimAudit.ManualInvoicePaid';

----------------------
-- End of 8.10.2
----------------------

-----------------------------------------------------
-- TO DO ITEM : 8.10.4 Audit Setup in BRE Bands
-----------------------------------------------------

ALTER TABLE bre_band ADD COLUMN enable_claim_audit boolean not null DEFAULT  false;
ALTER TABLE bre_band ADD COLUMN audit_process_percentage numeric(6,2) not null default 0;

----------------------
-- End of 8.10.4
----------------------

--------------------------------------------------------------------------------
-- 8.10.8 Potential PI Case/With Clients Solicitor Pot
--------------------------------------------------------------------------------
ALTER TABLE claim ADD COLUMN with_solicitor boolean not null default false;
ALTER TABLE claim ADD COLUMN date_marked_with_solicitor timestamp without time zone;
ALTER TABLE claim ADD COLUMN user_marked_with_solicitor varchar(512);

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'filter.CaseWithClientsSolicitor', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHO' FROM accessibility WHERE name='filter.CaseWithClientsSolicitor';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHOX_ADMIN' FROM accessibility WHERE name='filter.CaseWithClientsSolicitor';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'extraAction.markCaseWithClientsSolicitor.ContestedInvoiceReferredToCHO', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 2, 'ROLE_CHO' FROM accessibility WHERE name='extraAction.markCaseWithClientsSolicitor.ContestedInvoiceReferredToCHO';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    VALUES ('activity.UpdateCaseWithSolicitor.ContestedInvoiceReferredToCHO', false, false, false, false);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
   SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.UpdateCaseWithSolicitor.ContestedInvoiceReferredToCHO';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHO',1, id FROM accessibility WHERE name = 'activity.UpdateCaseWithSolicitor.ContestedInvoiceReferredToCHO';

ALTER TABLE chorganisation ADD COLUMN is_solicitor_enable boolean not null default false;
----------------------
-- End of 8.10.8
----------------------

-- bug#3601 - Production - incorrect audit trail for manual invoices
--------------------------------------------------------------------------------
update audit_trail
  set original_status='AwaitingInvoiceData', version=version+1
where original_status='InvoiceApprovedByBRE' and new_status='ManualInvoiceBREApproved';
----------------------
-- End of bug#3601
