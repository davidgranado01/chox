/*
 * CHOX-725: Invoice Details Report
 *  Example usage:
 *      select * from invoiceDetailsReport(array[6], null::integer[], null::integer[], '2017-01-01', '2017-01-01', array['ClaimClosed','PaymentReceived','ManualInvoicePaid','ClaimRejectionAccepted','InvoiceRejectionAccepted'], array['ClaimRejected']);
 */
DROP FUNCTION invoiceDetailsReport(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN claimUploadDate VARCHAR,
    IN closedClaimDate VARCHAR),
    IN closedClaimStatuses VARCHAR[],
    IN openClaimStatuses VARCHAR[]);


CREATE OR REPLACE FUNCTION invoiceDetailsReport(
    IN insIds INTEGER[],
    IN choIds INTEGER[],
    IN claimTypes INTEGER[],
    IN claimUploadDate VARCHAR,
    IN closedClaimDate VARCHAR,
    IN closedClaimStatuses VARCHAR[],
    IN openClaimStatuses VARCHAR[])
RETURNS TABLE(
                "Claim Status" VARCHAR,
                "Supplier Reference" VARCHAR,
                "CHO Name" VARCHAR,
                "Insurer Name" VARCHAR,
                "Claim Number" VARCHAR,
		"Invoice Upload Date" text,
		"Penalty Charge Start Date" text,
		"Collaboration Protocol Fee" numeric(8,2),
		"Collaboration Protocol Quantity" smallint,
		"Miscellaneous Costs" numeric(8,2),
		"Automatic Fee" numeric(8,2),
		"Automatic Quantity" smallint,
		"Additional Driver Fee" numeric(8,2),
		"Additional Driver Quantity" smallint,
		"Sat Nav Fee" numeric(8,2),
		"Sat Nav Quantity" smallint,
		"Estate Fee" numeric(8,2),
		"Estate Quantity" smallint,
		"Baby Seat Fee" numeric(8,2),
		"Baby Seat Quantity" smallint,
		"Tow Bar Fee" numeric(8,2),
		"Tow Bar Quantity" smallint,
		"Non-Standard Insurance Premium Fee" numeric(8,2),
		"Non-Standard Insurance Premium Quantity" smallint,
                "VED Charge" numeric(8,2),
                "VED Charge Qty" smallint,
		"Is Cover Note Required?" text,
		"Admin Fee" numeric(8,2),
		"Admin Quantity" smallint,
		"Roof-Rack Fee" numeric(8,2),
		"Roof-Rack Quantity" smallint,
		"Dual Control Fee" numeric(8,2),
		"Dual Control Quantity" smallint,
		"Collection/Delivery Fee" numeric(8,2),
		"Collection/Delivery Quantity" smallint,
                "Repair Admin Fee" numeric(8,2),
                "Repair Acquisition Fee" numeric(8,2),
                "Repair Parts" numeric(8,2),
                "Repair Labour" numeric(8,2),
                "Repair Materials" numeric(8,2),
                "Repair Specialist" numeric(8,2),
		"Excess Collected" numeric(8,2),
		"VAT Collected" numeric(8,2),
		"Supplier Claims Handling Invoice No." VARCHAR,
		"Claims Handling Invoice Amount" numeric(10,2),
		"Supplier Claim Invoice No" VARCHAR,
		"Hire Rate Charged per Day" numeric(10,2),
		"Hire Net" numeric(10,2),
		"Hire Vat" numeric(10,2),
		"Hire Gross" numeric(10,2),
		"Repair Net" numeric(10,2),
		"Repair Vat" numeric(10,2),
		"Repair Gross" numeric(10,2),
		"Engineer Fee Net" numeric(10,2),
		"Engineer Fee Vat" numeric(10,2),
		"Engineer Fee Gross" numeric(10,2),
		"Total Loss Net" numeric(10,2),
		"Total Loss Vat" numeric(10,2),
		"Total Loss Gross" numeric(10,2),
		"Storage Recovery Net" numeric(10,2),
		"Storage Recovery Vat" numeric(10,2),
		"Storage Recovery Gross" numeric(10,2),
		"Deduction For Claim Handling Fee" numeric(10,2),
		"Hire Penalty Charge" numeric(10,2),
		"Hire Penalty %" varchar,
		"Repair Penalty Charge" numeric(10,2),
		"Repair Penalty %" varchar,
		"Total Penalty Charge" numeric(10,2),
		"Total Net" numeric(10,2),
		"Total VAT" numeric(10,2),
		"Total Gross" numeric(10,2),
		"Less Discount" numeric(10,2),
		"Insurer Discount" numeric(10,2),
		"GTA Discount" numeric(10,2),
		"Full Total To Pay" numeric(10,2),
		"Original Full total to Pay" numeric(10,2),
		"Total To Pay" numeric(10,2),
		"Original Total To Pay" numeric(10,2),
		"Interim Payment Made" numeric(10,2),
		"Interim Payment Received" numeric(10,2),
		"Date Invoiced" timestamp without time zone,
		"Hire Gross Paid" numeric(10,2),
		"Repair Gross Paid" numeric(10,2),
		"Engineer Fee Gross Paid" numeric(10,2),
		"Total Loss Fee Gross Paid" numeric(10,2),
		"Storage Recovery Gross Paid" numeric(10,2),
		"Hire Penalty Charge Paid" numeric(10,2),
		"Repair Penalty Charge Paid" numeric(10,2),
		"Claim Handling Charge Gross Paid" numeric(10,2),
		"Deduction For Claim Handling Charge Paid" numeric(10,2),
                "CHO Discount Fee Paid" numeric(10,2),
		"Insurer Discount Fee Paid" numeric(10,2),
		"Final Payment" numeric(10,2),
		"With Payments Team?" text
) AS $BODY$
BEGIN
RETURN QUERY

select c.status, c.cho_reference, cho.name, ins.name, c.claim_number,
    to_char(i.created_date, 'dd/mm/yyyy hh24:mm') as createddate, to_char(i.auto_penalty_start, 'dd/mm/yyyy hh24:mm') as autopenaltystart,
    i.collaboration_fee as collaborationfee, i.collaboration_qty as collaborationqty, i.miscellaneous_fee as miscellaneousfee,
    i.automatic_fee as automaticfee, i.automatic_qty as automaticqty, i.additional_driver_fee as additionaldriverfee,
    i.additional_driver_qty as additionaldriverqty, i.sat_nav_fee as satnavfee, i.sat_nav_qty as satnavqty,
    i.estate_fee as estatefee, i.estate_qty as estateqty, i.baby_seat_fee as babyseatfee, i.baby_seat_qty as babyseatqty,
    i.tow_bars_fee as towbarsfee, i.tow_bars_qty as towbarsqty, i.non_standard_insurance_premium_fee as nonstandardinsurancepremiumfee,
    i.non_standard_insurance_premium_qty as nonstandardinsurancepremiumqty,
    i.ved_fee, i.ved_qty,
    case when i.cover_note_required is null then '' else case when i.cover_note_required then 'Yes' else 'No' end end as covernoterequired,
    i.admin_fee as adminfee, i.admin_qty as adminqty, i.roof_rack_fee as roofrackfee, i.roof_rack_qty as roofrackqty,
    i.dual_control_fee as dualcontrolfee, i.dual_control_qty as dualcontrolqty, i.delivery_collection_fee as deliverycollectionfee,
    i.delivery_collection_qty as deliverycollectionqty, i.repair_admin_fee,
    i.repair_acquisition_fee, i.repair_parts, i.repair_labour, i.repair_materials, i.repair_specialist,
    i.excess_amount_collected as excessamountcollected,
    i.vat_amount_collected as vatamountcollected, i.handling_invoice_no as handlinginvoiceno,
    i.claims_handling_invoice_amount as  claimshandlinginvoiceamount, i.claim_invoice_no as claiminvoiceno,
    i.hire_rate_charged_per_day as hireratechargedperday, i.hire_net as hirenet, i.hire_vat as hirevat,
    i.hire_gross as hiregross, i.repair_net as repairnet, i.repair_vat as repairvat, i.repair_gross as repairgross,
    i.engineer_fee_net as engineerfeenet, i.engineer_fee_vat as engineerfeevat, i.engineer_fee_gross as engineerfeegross,
    i.total_loss_net as totallossfeenet, i.total_loss_vat as totallossfeevat, i.total_loss_gross as totallossfeegross,
    i.storage_recovery_net as storagerecoverynet, i.storage_recovery_vat as storagerecoveryvat, i.storage_recovery_gross as storagerecoverygross,
    i.deduction_for_claims_handling_fee as deductionforclaimshandlingfee, i.hire_penalty_charge as hirepenaltycharge,
    i.hire_penalty_percentage as hirepenaltypercentage, i.repair_penalty_charge as repairpenaltycharge,
    i.repair_penalty_percentage as repairpenaltypercentage, i.total_penalty_charge as totalpenaltycharge,
    i.total_net as totalnet, i.total_vat as totalvat, i.total_gross as totalgross, i.discount as discount,
    i.insurer_discount as insurerdiscount,
    i.gta_discount,
    i.full_total_to_pay as fulltotaltopay, io.full_total_to_pay as original_fulltotaltopay,
    i.total_to_pay as totaltopay, io.total_to_pay as original_totaltopay, i.interim_payment_made as interimpaymentmade,
    i.interim_payment_received as interimpaymentreceived, i.date_invoiced as dateinvoiced, i.hire_gross_paid as hiregrosspaid,
    i.repair_gross_paid as repairgrosspaid, i.engineer_fee_gross_paid as engineerfeegrosspaid, i.total_loss_fee_gross_paid as totallossfeegrosspaid,
    i.storage_recovery_gross_paid as storagerecoverygrosspaid,
    i.hire_penalty_charge_paid as hirepenaltychargepaid, i.repair_penalty_charge_paid as repairpenaltychargepaid, i.claim_handler_charge_paid as claimhandlerchargepaid,
    i.deduction_claim_handler_fee_paid as deductionclaimhandlerfeepaid, i.cho_discount_fee_paid as chodiscountfeepaid,
    i.insurer_discount_fee_paid as insurerdiscountfeepaid, i.final_payment as finalpayment,
    case when i.payment_team then 'Yes' else 'No' end as paymentTeam
from claim c
    join invoice i on (c.invoice_id = i.id)
    join invoice_original io on (i.invoice_original_id = io.id)
    join chorganisation cho on (c.chorganisation_id = cho.id)
    join insurer ins on (c.insurer_id = ins.id)
where (insIds is null or c.insurer_id = ANY(insIds))
    and (choIds is null or c.chorganisation_id = ANY(choIds))
    and (claimTypes is null or c.claim_type = ANY(claimTypes))
    and c.created_date >= claimUploadDate::Date
    and ((c.status!=ALL(closedClaimStatuses) and (openClaimStatuses is null or c.status!=ALL(openClaimStatuses)))
            or ((c.status=ANY(closedClaimStatuses) or (openClaimStatuses is not null and c.status=ANY(openClaimStatuses))) and c.status_modified_date >= closedClaimDate::Date))
order by c.created_date, c.cho_reference;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;


GRANT EXECUTE ON FUNCTION invoiceDetailsReport(
                            IN insIds INTEGER[],
                            IN choIds INTEGER[],
                            IN claimTypes INTEGER[],
                            IN claimUploadDate VARCHAR,
                            IN closedClaimDate VARCHAR),
                            IN closedClaimStatuses VARCHAR[],
                            IN openClaimStatuses VARCHAR[])
TO chox_user;
GRANT EXECUTE ON FUNCTION invoiceDetailsReport(
                            IN insIds INTEGER[],
                            IN choIds INTEGER[],
                            IN claimTypes INTEGER[],
                            IN claimUploadDate VARCHAR,
                            IN closedClaimDate VARCHAR),
                            IN closedClaimStatuses VARCHAR[],
                            IN openClaimStatuses VARCHAR[])
TO chox_mi;
