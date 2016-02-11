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
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE claim_audit_review TO chox_user;
GRANT SELECT, UPDATE, USAGE ON SEQUENCE claim_audit_review_id_seq TO chox_user;

ALTER TABLE claim ADD COLUMN audit_review_id integer;
ALTER TABLE claim ADD CONSTRAINT audit_review_id_fkey FOREIGN KEY (audit_review_id) REFERENCES claim_audit_review (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE INDEX fki_audit_review_id_fkey ON claim USING btree (audit_review_id);

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
-- TO DO ITEM : 8.10.3 Report for Audit Facility
-----------------------------------------------------

CREATE OR REPLACE FUNCTION audit_facility
(
   IN insurerid integer, IN chorgId integer, IN startdate text, IN enddate text
)
RETURNS TABLE
(
    "Supplier Reference" character varying,
        "Insurer Claim Number" character varying,
        "CHO Name" character varying,
        "Date and Time at which claim went into audit" text,
        "Date and Time Audit Completed" text,
        "Handler who completed the audit" text,
        "Claim Type" character varying,
        "Who managed repair?" character varying,
        "Total Loss?" text,
        "Customers Vehicle Class" character varying,
        "Hire Vehicle Class" character varying,
        "Hire Duration" numeric,
        "Hire Duration Acceptable" text,
        "Reason for Hire Duration Not Acceptable" character varying,
        "Total Hire Costs" numeric,
        "Hire Leakage?" text,
        "If Yes, by how much ?" numeric,
        "Total Repair Cost" numeric,
        "Repair Cost Exceeds Engineers Recommendations?" text,
        "If Yes, by how much?" numeric,
        "Penalty Charges paid" numeric,
        "Were penalty charges avoidable?" text,
        "How were the penalty charges avoidable?" character varying,
        "Repair labour rate within ABP guidelines?" text,
        "If No how much was charged (hourly rate)" numeric,
        "Storage Claimed?" text,
        "If Yes, correctly so?" text,
        "Recovery Claimed?" text,
        "If Yes, correctly so ?" text
)
AS $$ DECLARE
BEGIN
        RETURN QUERY
            SELECT
                c.cho_reference,
                c.claim_number,
                cho.name,
                to_char(ar.created_date, 'dd/mm/yyyy hh24:mi:ss'),
                to_char(ar.audit_completed_date, 'dd/mm/yyyy hh24:mi:ss'),
                wu.first_name || ' ' || wu.last_name,
                ar.claim_type,
                ar.who_managed_repair,
                (CASE WHEN ar.total_loss IS NULL THEN '' ELSE (CASE WHEN ar.total_loss = TRUE THEN 'Yes' ELSE 'No' END) END),
                cvc.name,
                hvc.name,
                ar.hire_duration,
                (CASE WHEN ar.hire_duration_acceptable IS NULL THEN '' ELSE (CASE WHEN ar.hire_duration_acceptable = TRUE THEN 'Yes' ELSE 'No' END) END),
                ar.hire_duration_not_acceptable_reason,
                ar.total_hire_cost,
                (CASE WHEN ar.hire_leakage IS NULL THEN '' ELSE (CASE WHEN ar.hire_leakage = TRUE THEN 'Yes' ELSE 'No' END) END),
                ar.hire_leakage_cost,
                ar.total_repair_cost,
                (CASE WHEN ar.repair_cost_exceeds_eng_rec IS NULL THEN '' ELSE (CASE WHEN ar.repair_cost_exceeds_eng_rec = TRUE THEN 'Yes' ELSE 'No' END) END),
                ar.exceeded_repair_cost,
                ar.penalty_charges_paid,
                (CASE WHEN ar.penalty_charge_avoidable IS NULL THEN '' ELSE (CASE WHEN ar.penalty_charge_avoidable = TRUE THEN 'Yes' ELSE 'No' END) END),
                ar.penalty_charge_avoidable_note,
                (CASE WHEN ar.within_abp_guidelines IS NULL THEN '' ELSE (CASE WHEN ar.within_abp_guidelines = TRUE THEN 'Yes' ELSE 'No' END) END),
                ar.non_abp_guideline_repair_labour_rate,
                (CASE WHEN ar.storage_claimed IS NULL THEN '' ELSE (CASE WHEN ar.storage_claimed = TRUE THEN 'Yes' ELSE 'No' END) END),
                (CASE WHEN ar.storage_claimed_correctly IS NULL THEN '' ELSE (CASE WHEN ar.storage_claimed_correctly = TRUE THEN 'Yes' ELSE 'No' END) END),
                (CASE WHEN ar.recovery_claimed IS NULL THEN '' ELSE (CASE WHEN ar.recovery_claimed = TRUE THEN 'Yes' ELSE 'No' END) END),
                (CASE WHEN ar.recovery_claimed_correctly IS NULL THEN '' ELSE (CASE WHEN ar.recovery_claimed_correctly = TRUE THEN 'Yes' ELSE 'No' END) END)
            FROM
                claim c,
                claim_audit_review ar
                LEFT JOIN web_user wu ON wu.id = ar.completed_by
                LEFT JOIN vehicle_class cvc ON cvc.id = ar.customer_vehicle_class_id
                LEFT JOIN vehicle_class hvc ON hvc.id = ar.hire_vehicle_class_id,
                chorganisation cho
            WHERE
                c.audit_review_id = ar.id
                AND c.chorganisation_id = cho.id
--                 AND c.audit_review_id IS NOT NULL
                AND (chorgId = -1 or c.chorganisation_id = chorgId)
                AND c.insurer_id = insurerid
                AND ar.audit_completed_date BETWEEN startdate::date AND enddate::date + interval '1 day';
END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION audit_facility(IN insurerid integer, IN chorgId integer, IN startdate text, IN enddate text) TO chox_user;
GRANT EXECUTE ON FUNCTION audit_facility(IN insurerid integer, IN chorgId integer, IN startdate text, IN enddate text) TO chox_mi;


----------------------
-- End of 8.10.3
----------------------


-----------------------------------------------------
-- TO DO ITEM : 8.10.4 Audit Setup in BRE Bands
-----------------------------------------------------

ALTER TABLE bre_band ADD COLUMN enable_claim_audit boolean not null DEFAULT FALSE;
ALTER TABLE bre_band ADD COLUMN audit_process_percentage numeric(6,2) not null DEFAULT 0;

ALTER TABLE insurer ADD COLUMN claim_audit_review_enable boolean not null DEFAULT  false;

----------------------
-- End of 8.10.4
----------------------

--------------------------------------------------------------------------------
-- 8.10.5 Updated Rejection Reasons Panel
--------------------------------------------------------------------------------
ALTER TABLE reason_of_rejection ALTER COLUMN type TYPE varchar(17);
ALTER TABLE reason_of_rejection ALTER COLUMN name TYPE character varying;
UPDATE reason_of_rejection set type = 'Claim Rejection' where type='Claim';
UPDATE reason_of_rejection set type = 'Invoice Rejection' where type='Invoice';
ALTER TABLE reason_of_rejection_template ALTER COLUMN type TYPE varchar(17);
ALTER TABLE reason_of_rejection_template ALTER COLUMN name TYPE character varying;
UPDATE reason_of_rejection_template set type = 'Claim Rejection' where type='Claim';
UPDATE reason_of_rejection_template set type = 'Invoice Rejection' where type='Invoice';
----------------------
-- End of 8.10.5
----------------------

--------------------------------------------------------------------------------
-- 8.10.6 Acceptance Reason Functionality
--------------------------------------------------------------------------------
ALTER TABLE insurer ADD COLUMN acceptance_reason_enable boolean not null default false;
UPDATE insurer set acceptance_reason_enable = true where name='LV=';
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 26, 'Accepted - Without Prejudice', 'Acceptance', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 26, 'Accepted - Without Prejudice - Unable To Validate', 'Acceptance', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 26, 'Accepted - Without Prejudice - Liability In Dispute', 'Acceptance', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 26, 'Accepted - Quantum Dispute - Without Prejudice', 'Acceptance', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0;

----------------------
-- End of 8.10.6
----------------------

--------------------------------------------------------------------------------
-- 8.10.7 Claim Closure Note
--------------------------------------------------------------------------------
-- advance sequence number (for some reason, its out of sync)
select nextval('reason_of_rejection_template_id_seq'::regclass);
-- Add template closure reasons
INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Accepted Interim Payment As Full & Final', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'No Longer Pursuing Claim', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Incorrect At-Fault Insurer', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Litigating', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Other', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Out Of Scope', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Payment Received In Full', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Pursued Outside Of CHOX', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Write Off - Liability', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Write Off - Indemnity', 'Closure', '', true, true, true, true, true, true, true, false;

INSERT INTO reason_of_rejection_template(name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active, insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted)
    SELECT 'Write Off - Claim Validation', 'Closure', '', true, true, true, true, true, true, true, false;

-- Add closure reasons for existing insurers
ALTER TABLE reason_of_rejection ALTER COLUMN name TYPE character varying;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Accepted Interim Payment As Full & Final', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'No Longer Pursuing Claim', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Incorrect At-Fault Insurer', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Litigating', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Out Of Scope', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Payment Received In Full', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Pursued Outside Of CHOX', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Write Off - Liability', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Write Off - Indemnity', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;
INSERT INTO reason_of_rejection(insurer_id, name, type, description, gta_active, insurer_vs_insurer_active, subscriber_active,
                insurer_upload_active, tpi_active, fixed_fee_active, collaboration_active, restricted,
                created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT id, 'Write Off - Claim Validation', 'Closure', '', true, true, true, true, true, true, true, false, 999, now(), 999, now(), 0 from insurer;

----------------------
-- End of 8.10.7
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

--------------------------------------------------------------------------------
-- bug#3601 - Production - incorrect audit trail for manual invoices
--------------------------------------------------------------------------------
update audit_trail
  set original_status='AwaitingInvoiceData', version=version+1
where original_status='InvoiceApprovedByBRE' and new_status='ManualInvoiceBREApproved';
----------------------
-- End of bug#3601
--------------------------------------------------------------------------------
