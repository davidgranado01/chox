--
-- CHOX-483: Copley Question
--
ALTER TABLE insurer ADD COLUMN copley_question boolean NOT NULL DEFAULT false;
ALTER TABLE claim ADD COLUMN copley_offer_made boolean;
ALTER TABLE claim ADD COLUMN copley_offer_made_date timestamp without time zone;

UPDATE claim set copley_offer_made = hmd.copley_offer_made,
                 copley_offer_made_date = hmd.copley_offer_made_date
FROM insurer_hire_monitoring_detail hmd
WHERE claim.insurer_hire_monitoring_detail_id=hmd.id;

ALTER TABLE insurer_hire_monitoring_detail DROP COLUMN copley_offer_made;
ALTER TABLE insurer_hire_monitoring_detail DROP COLUMN copley_offer_made_date;
--
-- End of CHOX-483
--

--
-- CHOX-484: New BRE Rule: Copley Offer Made Check
--
ALTER TABLE bre_band ADD COLUMN copley_offer_made_check boolean NOT NULL DEFAULT false;
--
-- End of CHOX-484
--

--
-- CHOX-481: New Export Role to control access to grid export
--
INSERT INTO web_user_role (name, created_by, last_modified_by, description,type_id, is_workgroup_related, is_ownership_related, version )
    values ('ROLE_CHO_GRIDEXPORT',999, 999, 'Grid Export', 3, false, false, 0);
INSERT INTO web_user_role (name, created_by, last_modified_by, description,type_id, is_workgroup_related, is_ownership_related, version )
    values ('ROLE_INS_GRIDEXPORT',999, 999, 'Grid Export', 2, false, false, 0);
--
-- End of CHOX-481
--

--
-- CHOX-471: Remove 'Payments Team - Payment Disputes' queue
--
DELETE FROM accessibility_item WHERE accessibility_id = (select id from accessibility where name='filter.PaymentTeamDispute');
DELETE FROM accessibility WHERE name='filter.PaymentTeamDispute';
--DELETE FROM accessibility_item WHERE accessibility_id = (select id from accessibility where name='filter.InvoicePaymentDispute') and role='ROLE_INS_PC';
--
-- End of CHOX-471
--
