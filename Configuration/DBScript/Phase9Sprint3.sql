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

