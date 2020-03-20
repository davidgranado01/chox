SELECT
claim.status, 
chorganisation.name as CHO,
claim.cho_reference as Supplier_Ref, 
claim.claim_number,
insurer.name as insurer, 
claim.claim_type, 
claim.liability_status, 
claim.percentage_liability_accepted,
invoice.created_date as invoice_upload_date,
audit_trail.created_date as paid_date, 
invoice_original.hire_net as original_hire_net, 
invoice_original.hire_gross as original_hire_gross, 
invoice.hire_net,  
invoice.hire_gross,  
invoice_original.repair_net as original_hire_net, 
invoice_original.repair_gross as original_hire_gross, 
invoice.repair_net, 
invoice.repair_gross,
invoice.total_penalty_charge, --LPP requested
invoice.penalty_charges_paid,
invoice_original.total_to_pay as original_total_to_pay,  
invoice.total_to_pay,
invoice.gta_discount, 
vehicle_hire.days_original as original_hire_days,
vehicle_hire.days as hire_days,
vco.name as original_veh_class,
vc.name as veh_class,
invoice_original.hire_rate_charged_per_day, 
invoice.hire_rate_charged_per_day
from claim 
JOIN chorganisation ON claim.chorganisation_id = chorganisation.id
LEFT JOIN invoice ON invoice.id = claim.invoice_id
LEFT JOIN invoice_original ON invoice_original.id = invoice.invoice_original_id
LEFT JOIN vehicle_hire ON vehicle_hire.id = claim.vehicle_hire_id
LEFT JOIN insurer ON claim.insurer_id = insurer.id
JOIN vehicle_class vc ON vc.id = vehicle_hire.vehicle_class_id
JOIN vehicle_class vco ON vco.id = vehicle_hire.vehicle_class_original_id
JOIN audit_trail ON claim.id = audit_trail.claim_id
WHERE 
audit_trail.reverted = false 
AND audit_trail.created_date >= '2019-01-01'  AND audit_trail.created_date < '2020-01-01' 
AND (audit_trail.new_status = 'ManualInvoicePaid'  OR audit_trail.new_status = 'InvoicePaymentLogged')
AND (claim.status = 'ManualInvoicePaid' OR claim.status = 'InvoicePaymentLogged' OR claim.status = 'PaymentReceived');
