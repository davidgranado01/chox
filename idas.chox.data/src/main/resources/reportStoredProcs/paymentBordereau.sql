--DROP FUNCTION paymentBordereau(IN insId INTEGER, IN choIds INTEGER[], IN startPeriod VARCHAR, IN endPeriod VARCHAR);

CREATE OR REPLACE FUNCTION paymentBordereau(
    IN insId INTEGER,
    IN choIds INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR)


RETURNS TABLE(
    "Insurer" VARCHAR,
    "Claim Number" VARCHAR,
    "CHO Reference" VARCHAR,
    "Claimant VRN" VARCHAR,
    "Loss Date/Time" TEXT,
    "Claim Type" TEXT,
    "CHO Name" VARCHAR,
    "Liability Status" TEXT,
    "Date Invoiced" TEXT,
    "Hire Gross (Inc LPPs)" numeric(8,2),
    "Repair Gross (Inc LPPs)" numeric(8,2),
    "Storage Recovery Gross (inc total loss fees)" numeric(8,2),
    "Total Gross" numeric(8,2),
    "Liability % Agreed (Insurer)" numeric(5,2),
    "Claimant Title" VARCHAR,
    "Claimant First Name" VARCHAR,
    "Claimant Surname" VARCHAR,
    "TP Insurer" VARCHAR,
    "Claimant Vehicle Class" VARCHAR,
    "Damage Description" TEXT,
    "Usable?" TEXT,
    "Hire Vehicle Class" VARCHAR,
    "Hire Start Date" TEXT,
    "Hire End Date" TEXT,
    "No. Days Hire" numeric(4,0) ,
    "Name of Repairer" VARCHAR,
    "Repair Book-in Date" TEXT,
    "Date Repair Authorised" TEXT,
    "Date Repair Commenced" TEXT,
    "Inspection Booked Date" TEXT,
    "Inspection Date" TEXT,
    "Repair Completion Date" TEXT,
    "Date Total Loss Offer Made" TEXT,
    "Date Total Loss Offer Accepted" TEXT,
    "Date Total Loss Cheque Issued" TEXT,
    "Date Total Loss Cheque Received" TEXT,
    "Is the Claimant Impecunious?" TEXT,
    "Who Managed the Repair?" VARCHAR(32))

AS $BODY$

    DECLARE

        DATE_FROM DATE;
        DATE_TO DATE;

    BEGIN

        DATE_FROM = $3::DATE;
        DATE_TO = $4::DATE;

        RETURN QUERY
        SELECT
            ins.name as "Insurer",
            c.claim_number as "Claim Number",
            c.cho_reference as "CHO Reference",
            cu.vehicle_registration as "Claimant VRN",
            case when inc.date is null then null else to_char(inc.date, 'DD/MM/YYYY') || ' ' || time end as "Loss Date/Time",
            getClaimTypeName(c.claim_type) as "Claim Type",
            cho.name as "CHO Name",
            getLiabilityStatus(c.liability_status) as "Liability Status",
            to_char(inv.created_date, 'DD/MM/YYYY') as "Date Invoiced",
            ((inv.hire_gross + inv.hire_penalty_charge)*c.percentage_liability_accepted/100.0)::numeric(8,2) as "Hire Gross (Inc LPPs)",
            ((inv.repair_gross + inv.repair_penalty_charge + inv.engineer_fee_gross)*c.percentage_liability_accepted/100.0)::numeric(8,2) as "Repair Gross (Inc LPPs)",
            ((storage_recovery_gross + total_loss_gross)*c.percentage_liability_accepted/100.0)::numeric(8,2) as "Storage Recovery Gross (inc total loss fees)", 
            (total_gross*c.percentage_liability_accepted/100.0)::numeric(8,2) as "Total Gross",
            c.percentage_liability_accepted as "Liability % Agreed (Insurer)",
            cu.title as "Claimant Title",
            cu.first_name as "Claimant First Name",
            cu.last_name as "Claimant Surname",
            cu.insurer_name as "TP Insurer",
            cuvc.name as "Claimant Vehicle Class",
            replace(cu.damage, ',', '') as "Damage Description",
            case when cu.is_usable then 'Yes' else 'No' end as "Usable?",
            vhvc.name as "Hire Vehicle Class",
            to_char(vh.rental_start, 'DD/MM/YYYY') as "Hire Start Date",
            to_char(vh.rental_end, 'DD/MM/YYYY') as "Hire End Date",
            vh.days as "No. Days Hire",
            hmd.name_of_repairer as "Name of Repairer",
            to_char(hmd.repair_book_in_date, 'DD/MM/YYYY') as "Repair Book-in Date",
            to_char(hmd.repair_authorised_date, 'DD/MM/YYYY') as "Date Repair Authorised",
            to_char(hmd.repair_commenced_date, 'DD/MM/YYYY') as "Date Repair Commenced",
            to_char(hmd.inspection_booked_date, 'DD/MM/YYYY') as "Inspection Booked Date",
            to_char(hmd.inspection_date, 'DD/MM/YYYY') as "Inspection Date",
            to_char(hmd.repair_completion_date, 'DD/MM/YYYY') as "Repair Completion Date",
            to_char(hmd.total_loss_offer_made, 'DD/MM/YYYY') as "Date Total Loss Offer Made",
            to_char(hmd.total_loss_offer_accepted, 'DD/MM/YYYY') as "Date Total Loss Offer Accepted",
            to_char(hmd.total_loss_check_issued, 'DD/MM/YYYY') as "Date Total Loss Cheque Issued",
            to_char(hmd.total_loss_check_received, 'DD/MM/YYYY') as "Date Total Loss Cheque Received",
            case when ihmd.claimant_impecunious then 'Yes' else 'No' end  as "Is the Claimant Impecunious?",
            ihmd.who_managed_repair as "Who Managed the Repair?"
        FROM claim c
            LEFT OUTER JOIN hire_monitoring_detail hmd ON (c.hire_monitoring_detail_id = hmd.id)
            LEFT OUTER JOIN insurer_hire_monitoring_detail ihmd ON (c.insurer_hire_monitoring_detail_id = ihmd.id)
            LEFT OUTER JOIN vehicle_hire vh ON (c.vehicle_hire_id = vh.id)
            LEFT OUTER JOIN vehicle_class vhvc on (vh.vehicle_class_id = vhvc.id),
            insurer ins, chorganisation cho, audit_trail at, incident inc, invoice inv, customer cu
            LEFT OUTER JOIN vehicle_class cuvc on (cu.vehicle_class_id = cuvc.id)
        WHERE c.insurer_id = ins.id AND c.chorganisation_id = cho.id AND (cho.id = ANY(choIds) OR choIds is null)
          AND ins.id = insId AND c.customer_id = cu.id AND c.incident_id = inc.id AND c.invoice_id = inv.id
          AND c.id = at.claim_id and at.new_status = 'AwaitingInvoicePayment' and at.reverted = false and at.created_date >= DATE_FROM and at.created_date < DATE_TO;

    END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION paymentBordereau(IN insIds INTEGER, IN choIds INTEGER[], IN startPeriod VARCHAR, IN endPeriod VARCHAR)
TO chox_user;

GRANT EXECUTE ON FUNCTION paymentBordereau(IN insIds INTEGER, IN choIds INTEGER[], IN startPeriod VARCHAR, IN endPeriod VARCHAR)
TO chox_mi;

/* select * from paymentBordereau(26, null, '2017-06-26', '2017-06-27'); */
/* \copy (select * from paymentBordereau(26, null, '2017-06-26', '2017-06-27')) TO '${DUMPFILE}' (format CSV); */
/* \copy (select * from paymentBordereau(${INS_ID}, null, '${START_DATE}', '${END_DATE}')) TO '${DUMPFILE}' (format CSV); */
