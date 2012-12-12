DROP VIEW rpt_all_claim_with_invoice;

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
