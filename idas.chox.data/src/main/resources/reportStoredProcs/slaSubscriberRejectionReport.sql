DROP FUNCTION slaSubscriberRejectionReport(IN startdate TEXT, IN enddate TEXT, IN insurerid INTEGER);

CREATE OR REPLACE FUNCTION slaSubscriberRejectionReport(IN startdate TEXT, IN enddate TEXT, IN insurerid INTEGER)
  RETURNS TABLE("Workgroup/Claim Owner" TEXT, "Rejection Reason" VARCHAR, "Total # rejected" BIGINT, "# rejected on day 1" BIGINT, "# rejected on day 2" BIGINT, "# rejected on day 3" BIGINT, "# rejected on day 4" BIGINT, "# rejected on day 5" BIGINT, "# rejected on day 6" BIGINT, "# rejected on day 7" BIGINT, "Supplier Reference Number(s)" TEXT, "Insurer Claim Number(s)" TEXT) AS
$BODY$ 

DECLARE

totalRecord BIGINT;
totalRejectionReasonRecord BIGINT;
workgroupRecord RECORD;
reasonOfRejection RECORD;
claimOwnerRecord RECORD;

BEGIN

     IF (SELECT is_workgroup_enable FROM insurer WHERE id = $3) THEN
     
--        RAISE NOTICE 'Workgroup Enabled.';

        FOR i IN 1..2 LOOP

            IF i=1 THEN
               totalRecord = 1;
            ELSE
               totalRecord = (SELECT COUNT(*) FROM workgroup WHERE insurer_id = $3 AND status = TRUE);
            END IF;

            FOR workgroupRecord IN SELECT * FROM workgroup WHERE insurer_id = $3 AND status = TRUE ORDER BY name LIMIT totalRecord LOOP

                FOR j IN 1..2 LOOP

                    IF j=1 THEN
                       totalRejectionReasonRecord = (SELECT COUNT(*) FROM reason_of_rejection WHERE insurer_id = $3 AND type ILIKE 'Claim' AND subscriber_active = TRUE);
                    ELSE
                       totalRejectionReasonRecord = 1;
                    END IF;

                    FOR reasonOfRejection IN SELECT * FROM reason_of_rejection WHERE insurer_id = $3 AND type ILIKE 'Claim' AND subscriber_active = TRUE ORDER BY name LIMIT totalRejectionReasonRecord LOOP

                        RETURN QUERY

                             SELECT CASE WHEN i=1 THEN 'Not Yet Routed' ELSE workgroupRecord.name::TEXT END AS "Name",

                             (SELECT CASE WHEN j=2 THEN 'ALL' ELSE reasonOfRejection.name END) AS "Rejection Reason",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND a.new_status = 'SubscriberClaimRejected') AS "Total # rejected",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 1) AS "# rejected on day 1",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 2) AS "# rejected on day 2",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 3) AS "# rejected on day 3",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 4) AS "# rejected on day 4",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 5) AS "# rejected on day 5",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 6) AS "# rejected on day 6",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 7) AS "# rejected on day 7",

                            (SELECT
                                    STRING_AGG(c.cho_reference, ', ')
                             FROM
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND a.new_status = 'SubscriberClaimRejected') AS "Supplier Reference Number(s)",
                            (SELECT
                                    STRING_AGG(coalesce(c.claim_number, '-'), ', ')
                             FROM
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.workgroup_id = workgroupRecord.id) OR (i=1 AND c.workgroup_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND a.new_status = 'SubscriberClaimRejected') AS "Insurer Claim Number(s)";

                    END LOOP; 
                END LOOP; 
            END LOOP;
        END LOOP;
     
     ELSIF (SELECT is_claim_ownership_enable FROM insurer WHERE id = $3) THEN

--        RAISE NOTICE 'Claim Ownership Enabled.';

        FOR i IN 1..2 LOOP

            IF i=1 THEN
               totalRecord = 1;
            ELSE
               totalRecord = (SELECT COUNT(*) FROM web_user wu WHERE wu.insurer_id = $3 AND wu.status = TRUE);
            END IF;

            FOR claimOwnerRecord IN SELECT * FROM web_user wu WHERE wu.insurer_id = $3 AND wu.status = TRUE ORDER BY wu.first_name || ' ' || wu.last_name LIMIT totalRecord LOOP

                FOR j IN 1..2 LOOP

                    IF j=1 THEN
                       totalRejectionReasonRecord = (SELECT COUNT(*) FROM reason_of_rejection WHERE insurer_id = $3 AND type ILIKE 'Claim' AND subscriber_active = TRUE);
                    ELSE
                       totalRejectionReasonRecord = 1;
                    END IF;

                    FOR reasonOfRejection IN SELECT * FROM reason_of_rejection WHERE insurer_id = $3 AND type ILIKE 'Claim' AND subscriber_active = TRUE ORDER BY name LIMIT totalRejectionReasonRecord LOOP

                        RETURN QUERY

                             SELECT CASE WHEN i=1 THEN 'Not Yet Assigned' ELSE claimOwnerRecord.first_name || ' ' || claimOwnerRecord.last_name END AS "Name",

                             (SELECT CASE WHEN j=2 THEN 'ALL' ELSE reasonOfRejection.name END) AS "Rejection Reason",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND a.new_status = 'SubscriberClaimRejected') AS "Total # rejected",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 1) AS "# rejected on day 1",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 2) AS "# rejected on day 2",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 3) AS "# rejected on day 3",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 4) AS "# rejected on day 4",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND c.status = a.new_status
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 5) AS "# rejected on day 5",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND c.status = a.new_status
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 6) AS "# rejected on day 6",

                             (SELECT
                                    COUNT(*)
                             FROM 
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND c.status = a.new_status
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.new_status = 'SubscriberClaimRejected'
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND ((a.created_date::date - c.created_date::date) + 1) = 7) AS "# rejected on day 7",

                            (SELECT
                                    STRING_AGG(c.cho_reference, ', ')
                             FROM
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND a.new_status = 'SubscriberClaimRejected') AS "Supplier Reference Number(s)",
                            (SELECT
                                    STRING_AGG(coalesce(c.claim_number, '-'), ', ')
                             FROM
                                    claim c,
                                    audit_trail a
                             WHERE 
                                    c.insurer_id = $3
                                    AND ((i=2 AND c.claim_owner_id = claimOwnerRecord.id) OR (i=1 AND c.claim_owner_id IS NULL))
                                    AND a.claim_id = c.id
                                    AND a.reverted = FALSE
                                    AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE)
                                    AND ((j=1 AND a.claim_reason_of_rejection = reasonOfRejection.id) OR (j=2))
                                    AND a.created_date between $1::DATE AND $2::DATE
                                    AND a.new_status = 'SubscriberClaimRejected') AS "Insurer Claim Number(s)";

                    END LOOP; 
                END LOOP; 
            END LOOP;
        END LOOP;
          
     ELSE

     RAISE NOTICE 'Neither Workgroup nor Claim Ownership Enabled - report cannot be generated.';
     
     END IF;
END; 
$BODY$
LANGUAGE plpgsql;
