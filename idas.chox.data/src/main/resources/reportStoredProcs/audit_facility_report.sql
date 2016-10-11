-- SELECT * FROM audit_facility(3, -1, '2016-01-01', '2016-01-31');

DROP FUNCTION audit_facility(IN insurerid integer, IN chorgId integer, IN startdate text, IN enddate text);
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
                AND ar.audit_completed_date BETWEEN startdate::date AND enddate::date;
END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION audit_facility(IN insurerid integer, IN chorgId integer, IN startdate text, IN enddate text) TO chox_user;
GRANT EXECUTE ON FUNCTION audit_facility(IN insurerid integer, IN chorgId integer, IN startdate text, IN enddate text) TO chox_mi;
