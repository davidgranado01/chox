DROP VIEW rpt_claim_invoice;

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
