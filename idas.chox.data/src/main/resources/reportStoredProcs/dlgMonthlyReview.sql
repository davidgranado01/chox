--drop function dlg_monthly_review(integer, text, text);

create or replace function dlg_monthly_review (
   IN insurerId integer, IN startPeriod text, IN endPeriod text
)
returns table
(
    "Customer VRN" character varying(16),
    "Customer Name" text,
    "CHO name" character varying(128),
    "Workgroup" character varying(40),
    "Date of Loss" timestamp without time zone,
    "Date of Hire" timestamp without time zone,
    "DLG Claim Number" character varying(128),
    "CHOX System Status" character varying(40),
    "CHOX Claim Rejection Reason" character varying(32),
    "CHOX Invoice Rejection Reason" character varying(32),
    "Miscellaneous Costs" numeric(10,2),
    "Automatic Fee" numeric(10,2),
    "Additional Driver Fee" numeric(10,2),
    "Sat Nav Fee" numeric(10,2),
    "Estate Fee" numeric(10,2),
    "Baby Seat Fee" numeric(10,2),
    "Tow Bars Fee" numeric(10,2),
    "Non-Standard Risk Ins. Premium Fee" numeric(10,2),
    "Admin Fee" numeric(10,2),
    "Roof Rack Fee" numeric(10,2),
    "Dual Control Fee" numeric(10,2),
    "Delivery Collection Fee" numeric(10,2),
    "Hire Net" numeric(10,2),
    "Repair Net" numeric(10,2),
    "Engineer Fee Net" numeric(10,2),
    "Total Loss Fee Net" numeric(10,2),
    "Storage Recovery Net" numeric(10,2),
    "Total Net" numeric(10,2),
    "Hire Penalty Charge" numeric(10,2),
    "Repair Penalty Charge" numeric(10,2),
    "Full Total Requested" numeric(10,2),
    "Total to Pay" numeric(10,2),
    "Interim Payment Received" numeric(10,2),
    "Interim Payment Accepted as 'Full and Final'?" text,
    "Credit Hire Paid Amount" numeric(10,2),
    "Credit Repair Paid Amount" numeric(10,2),
    "Sherwood Fee Amount" text
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select
        cus.vehicle_registration as "Customer VRN",
        cus.first_name || ' ' || cus.last_name as "Customer Name",
        cho.name as "CHO name",
	w.name as "Workgroup",
        inc.date as "Date of Loss",
        vh.rental_start as "Date of Hire",
        c.claim_number as "DLG Claim Number",
        c.status as "CHOX System Status",
        ror.name as "CHOX Claim Rejection Reason",
        rori.name as "CHOX Invoice Rejection Reason",
        (select inv.miscellaneous_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Miscellaneous Costs",
        (select inv.automatic_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Automatic Fee",
        (select inv.additional_driver_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Additional Driver Fee",
        (select inv.sat_nav_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Sat Nav Fee",
        (select inv.estate_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Estate Fee",
        (select inv.baby_seat_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Baby Seat Fee",
        (select inv.tow_bars_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Tow Bars Fee",
        (select inv.non_standard_insurance_premium_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Non-Standard Risk Ins. Premium Fee",
        (select inv.admin_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Admin Fee",
        (select inv.roof_rack_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Roof Rack Fee",
        (select inv.dual_control_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Dual Control Fee",
        (select inv.delivery_collection_fee
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Delivery Collection Fee",
        (select inv.hire_net
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Hire Net",
        (select inv.repair_net
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Repair Net",
        (select inv.engineer_fee_net
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Engineer Fee Net",
        (select inv.total_loss_net
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Total Loss Fee Net",
        (select inv.storage_recovery_net
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Storage Recovery Net",
        (select inv.total_net
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Total Net",
        (select inv.hire_penalty_charge
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Hire Penalty Charge",
        (select inv.repair_penalty_charge
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Repair Penalty Charge",
        (select inv.full_total_to_pay
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Full Total Requested",
        (select inv.total_to_pay
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Total to Pay",
        (select inv.interim_payment_received
         from invoice inv
         where inv.id = c.invoice_id and inv.interim_payment_received > 0.0 ) as "Interim Payment Received",
        (select case when inv.interim_payment_received_full_final then 'Yes' else 'No' end
         from invoice inv
         where inv.id = c.invoice_id and inv.interim_payment_received_full_final is not null and inv.interim_payment_received > 0.0) as "Interim Payment Accepted as 'Full and Final'?",
        (select inv.hire_gross_paid
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Credit Hire Paid Amount",
        (select inv.repair_gross_paid
         from invoice inv, audit_trail a
         where inv.id = c.invoice_id and a.claim_id = c.id
             and a.new_status = 'InvoicePaymentLogged' and a.reverted=false) as "Credit Repair Paid Amount ",
        (case when c.status = 'PaymentReceived' then '£20.00' else '-' end) as "Sherwood Fee Amount"
from claim c join chorganisation cho on c.chorganisation_id = cho.id
        left join incident inc on c.incident_id = inc.id
        left join vehicle_hire vh on c.vehicle_hire_id = vh.id
        left join customer cus on c.customer_id = cus.id
        left join reason_of_rejection ror on c.reason_of_rejection_id = ror.id
        left join invoice inv on c.invoice_id = inv.id
        left join reason_of_rejection rori on inv.reason_of_rejection_id = rori.id
	left outer join workgroup w on c.workgroup_id = w.id
where c.insurer_id = insurerId and c.created_date > startDate and c.created_date < endDate;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION dlg_monthly_review(integer, text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION dlg_monthly_review(integer, text, text) TO chox_mi;
