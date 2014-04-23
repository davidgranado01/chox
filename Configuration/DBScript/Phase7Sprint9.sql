--------------------------------------------------------------------------------
-- 7.9.1 IMS Email/Task Connectivity
--------------------------------------------------------------------------------
ALTER TABLE scheduler_job ADD COLUMN reply_to_sender boolean not null DEFAULT true;

INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver, reply_to_sender,
                           created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 'admin@erac.com', 'Ch0xAdm1n1', 'TOTALLOSS_NOTIFICATION', 'IMS TL Notification',
           'elliot.roberts@sherwoodts.co.uk,ben.richmond@sherwoodts.co.uk',
           'john.dowson@sherwoodts.co.uk,seeni.shanmugam@sherwoodts.co.uk', 'john.dowson@sherwoodts.co.uk,seeni.shanmugam@sherwoodts.co.uk', false,
           999, now(), 999, now(), 0;


INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver, reply_to_sender,
                           created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 'admin@erac.com', 'Ch0xAdm1n1', 'TOTALLOSS_PACK', 'IMS TL Pack',
           'elliot.roberts@sherwoodts.co.uk,ben.richmond@sherwoodts.co.uk',
           'john.dowson@sherwoodts.co.uk,seeni.shanmugam@sherwoodts.co.uk', 'john.dowson@sherwoodts.co.uk,seeni.shanmugam@sherwoodts.co.uk', false,
           999, now(), 999, now(), 0;


INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver, reply_to_sender,
                           created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 'admin@erac.com', 'Ch0xAdm1n1', 'TOTALLOSS_CHASE_TASK', 'not used',
           'john.dowson@sherwoodts.co.uk',
           'john.dowson@sherwoodts.co.uk,seeni.shanmugam@sherwoodts.co.uk', 'john.dowson@sherwoodts.co.uk,seeni.shanmugam@sherwoodts.co.uk', false,
           999, now(), 999, now(), 0;


INSERT INTO scheduler_job (login_username, login_password, job_name, email_subject,
                           autherised_user, bcc_receiver, error_message_receiver, reply_to_sender,
                           created_by, created_date, last_modified_by, last_modified_date, version)
    SELECT 'admin@erac.com', 'Ch0xAdm1n1', 'TOTALLOSS_STOP_CHASE_TASK', 'IMS TL Stop Chase Request',
           'elliot.roberts@sherwoodts.co.uk,ben.richmond@sherwoodts.co.uk',
           'john.dowson@sherwoodts.co.uk,seeni.shanmugam@sherwoodts.co.uk', 'john.dowson@sherwoodts.co.uk,seeni.shanmugam@sherwoodts.co.uk', false,
           999, now(), 999, now(), 0;


ALTER TABLE claim ADD COLUMN is_total_loss_chase boolean not null DEFAULT false;
ALTER TABLE claim ADD COLUMN liability_modified_date timestamp without time zone;
ALTER TABLE claim ADD COLUMN liability_status_modified_date timestamp without time zone;

----------------------
-- End of 7.9.1
----------------------


--------------------------------------------------------------------------------
-- 7.9.2 New BREs
--------------------------------------------------------------------------------
ALTER TABLE bre_band ADD COLUMN total_loss_net_ceiling_check boolean not null DEFAULT false;
ALTER TABLE bre_band ADD COLUMN total_loss_storage_fee_check boolean not null DEFAULT false;
ALTER TABLE bre_band ADD COLUMN max_allowed_total_loss_net numeric(8,2) not null DEFAULT 0.00;

----------------------
-- End of 7.9.2
----------------------

--
-- Clean-up type specification
--
ALTER TABLE reason_of_rejection ALTER COLUMN name TYPE varchar(32);
ALTER TABLE claim ALTER COLUMN indeminty_amount TYPE numeric(8,2);
ALTER TABLE vehicle_hire ALTER COLUMN days TYPE numeric(4,0);
ALTER TABLE vehicle_hire ALTER COLUMN days_original TYPE numeric(4,0);
ALTER TABLE vehicle_hire ALTER COLUMN hpi_error TYPE varchar(256);
ALTER TABLE customer ALTER COLUMN hpi_error TYPE varchar(256);
ALTER TABLE history ALTER COLUMN narrative TYPE varchar(320);
ALTER TABLE history ALTER COLUMN rule_id TYPE varchar(3);
ALTER TABLE hire_monitoring_ecd ALTER COLUMN supporting_note TYPE varchar(1152);
ALTER TABLE hire_monitoring_detail ALTER COLUMN non_provision_reason TYPE varchar(64);
ALTER TABLE engineer_report ALTER COLUMN days TYPE numeric(5,0);
DROP VIEW rpt_claim_invoice;
DROP VIEW rpt_all_claim_with_invoice;
ALTER TABLE invoice ALTER COLUMN excess_amount_collected TYPE numeric(10,2);
ALTER TABLE invoice ALTER COLUMN vat_amount_collected TYPE numeric(10,2);
ALTER TABLE invoice ALTER COLUMN engineer_invoice_review_notes TYPE varchar(256);
ALTER TABLE invoice_original ALTER COLUMN excess_amount_collected TYPE numeric(10,2);
ALTER TABLE invoice_original ALTER COLUMN vat_amount_collected TYPE numeric(10,2);
ALTER TABLE invoice_original ALTER COLUMN engineer_invoice_review_notes TYPE varchar(256);
ALTER TABLE task ALTER COLUMN description TYPE varchar(6000);
ALTER TABLE third_party ALTER COLUMN last_name TYPE varchar(128);
ALTER TABLE workgroup ALTER COLUMN name TYPE varchar(40);
ALTER TABLE workgroup ALTER COLUMN site TYPE varchar(32);
ALTER TABLE workgroup ALTER COLUMN team TYPE varchar(32);
ALTER TABLE attachment ALTER COLUMN category TYPE varchar(32);
ALTER TABLE attachment ALTER COLUMN file_type TYPE varchar(4);

CREATE OR REPLACE VIEW rpt_claim_invoice AS
 SELECT invoice.id, invoice.date_invoiced, invoice.handling_invoice_no, invoice.claim_invoice_no,
        invoice.miscellaneous_qty AS cdw_qty, invoice.automatic_qty, invoice.sat_nav_qty,
        invoice.estate_qty, invoice.baby_seat_qty, invoice.tow_bars_qty,
        invoice.non_standard_insurance_premium_qty, invoice.admin_qty,
        invoice.roof_rack_qty, invoice.dual_control_qty, invoice.delivery_collection_qty,
        invoice.created_by, invoice.created_date, invoice.last_modified_by,
        invoice.last_modified_date, invoice.hire_net, invoice.hire_vat, invoice.hire_gross,
        invoice.repair_net, invoice.repair_vat, invoice.repair_gross,
        invoice.engineer_fee_net, invoice.engineer_fee_vat, invoice.engineer_fee_gross,
        invoice.storage_recovery_net, invoice.storage_recovery_vat, invoice.storage_recovery_gross,
        invoice.total_net, invoice.total_vat, invoice.total_gross, invoice.claims_handling_invoice_amount,
        invoice.deduction_for_claims_handling_fee, invoice.discount, invoice.total_to_pay,
        invoice.miscellaneous_fee AS cdw_fee, invoice.automatic_fee, invoice.sat_nav_fee,
        invoice.estate_fee, invoice.baby_seat_fee, invoice.tow_bars_fee,
        invoice.non_standard_insurance_premium_fee, invoice.admin_fee, invoice.roof_rack_fee,
        invoice.dual_control_fee, invoice.delivery_collection_fee, invoice.is_payment_mode,
        invoice.is_engineer_decision_approved, invoice.engineer_invoice_review_notes,
        invoice.hire_rate_charged_per_day, invoice.excess_amount_collected, invoice.vat_amount_collected,
        invoice.total_penalty_charge, invoice.hire_penalty_charge_applied_date,
        invoice.repair_penalty_charge_applied_date, claim.id AS claim_id, claim.status,
        claim.cho_reference, claim.claim_number, claim.insurer_id, claim.chorganisation_id,
        third_party.first_name AS policy_holder_first_name,
        third_party.last_name AS policy_holder_surname_name,
        third_party.vehicle_registration AS vehicle_registration_number,
        claim.created_date AS claim_created_date, claim.vehicle_hire_id AS claim_vehicle_hire_id,
        workgroup.name AS workgroup, claim.workgroup_id, claim.claim_owner_id AS owner,
        io.total_to_pay AS original_total_to_pay, claim.percentage_liability_accepted,
        claim.percentage_liability_cho, io.full_total_to_pay AS original_full_total_to_pay
   FROM invoice_original io, claim claim
   JOIN invoice invoice ON claim.invoice_id = invoice.id
   LEFT JOIN workgroup workgroup ON workgroup.id = claim.workgroup_id
   LEFT JOIN third_party third_party ON third_party.id = claim.third_party_id
   WHERE invoice.invoice_original_id = io.id;

GRANT SELECT ON TABLE rpt_claim_invoice TO chox_user;
GRANT SELECT ON TABLE rpt_claim_invoice TO chox_mi;


CREATE OR REPLACE VIEW rpt_all_claim_with_invoice AS
 SELECT claim.id AS claim_id, claim.workgroup_id, claim.claim_owner_id as owner,
        claim.status, claim.cho_reference, claim.claim_number, claim.insurer_id,
        claim.chorganisation_id, claim.created_date as claim_created_date,
        claim.claim_type, invoice.id as invoice_id, invoice.date_invoiced,
        invoice.handling_invoice_no, invoice.claim_invoice_no,
        invoice.miscellaneous_qty as cdw_qty, invoice.automatic_qty,
        invoice.sat_nav_qty, invoice.estate_qty, invoice.baby_seat_qty,
        invoice.tow_bars_qty, invoice.non_standard_insurance_premium_qty,
        invoice.admin_qty, invoice.roof_rack_qty, invoice.dual_control_qty,
        invoice.delivery_collection_qty, invoice.created_by,
        invoice.created_date as invoice_created_date,
        invoice.last_modified_by as last_nodified_by,
        invoice.last_modified_date, invoice.hire_net, invoice.hire_vat,
        invoice.hire_gross, invoice.repair_net, invoice.repair_vat,
        invoice.repair_gross, invoice.engineer_fee_net,
        invoice.engineer_fee_vat, invoice.engineer_fee_gross,
        invoice.storage_recovery_net, invoice.storage_recovery_vat,
        invoice.storage_recovery_gross, invoice.total_net, invoice.total_vat,
        invoice.total_gross, invoice.claims_handling_invoice_amount,
        invoice.deduction_for_claims_handling_fee, invoice.discount,
        invoice.full_total_to_pay as total_to_pay,
        invoice.miscellaneous_fee as cdw_fee, invoice.automatic_fee,
        invoice.sat_nav_fee, invoice.estate_fee, invoice.baby_seat_fee,
        invoice.tow_bars_fee, invoice.non_standard_insurance_premium_fee,
        invoice.admin_fee, invoice.roof_rack_fee, invoice.dual_control_fee,
        invoice.delivery_collection_fee, invoice.is_payment_mode,
        invoice.is_engineer_decision_approved, invoice.engineer_invoice_review_notes,
        invoice.hire_rate_charged_per_day, invoice.excess_amount_collected,
        invoice.vat_amount_collected, invoice.hire_penalty_charge as panalty_charge,
        invoice.hire_penalty_charge_applied_date as penalty_charge_applied_date
   FROM claim claim
   LEFT JOIN invoice invoice on claim.invoice_id = invoice.id;

GRANT SELECT ON rpt_all_claim_with_invoice TO chox_user;
GRANT SELECT ON rpt_all_claim_with_invoice TO chox_mi;

