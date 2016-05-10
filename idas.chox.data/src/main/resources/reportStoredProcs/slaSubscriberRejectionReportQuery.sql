DROP FUNCTION slaSubscriberRejectionReportQuery(IN insurerid INTEGER, IN startdate TEXT, IN enddate TEXT, IN noRejectionDays INTEGER);

CREATE OR REPLACE FUNCTION slaSubscriberRejectionReportQuery(IN insurerid INTEGER, IN startdate TEXT, IN enddate TEXT, IN noRejectionDays INTEGER)
  RETURNS TEXT AS

$BODY$

DECLARE

    totalRecord BIGINT;
    totalRejectionReasonRecord BIGINT;
    workgroupRecord RECORD;
    reasonOfRejection RECORD;
    claimOwnerRecord RECORD;
    queryString TEXT;

BEGIN
    queryString = '';
    IF (SELECT is_workgroup_enable FROM insurer WHERE id = $1) THEN

        FOR i IN 1..2 LOOP

            IF i=1 THEN
               totalRecord = 1;
            ELSE
               totalRecord = (SELECT COUNT(*) FROM workgroup WHERE insurer_id = $1 AND status = TRUE);
            END IF;

            FOR workgroupRecord IN SELECT * FROM workgroup WHERE insurer_id = $1 AND status = TRUE ORDER BY name LIMIT totalRecord LOOP

                FOR j IN 1..2 LOOP

                    IF j=1 THEN
                       totalRejectionReasonRecord = (SELECT COUNT(*) FROM reason_of_rejection WHERE insurer_id = $1 AND type ILIKE 'Claim Rejection' AND subscriber_active = TRUE);
                    ELSE
                       totalRejectionReasonRecord = 1;
                    END IF;

                    FOR reasonOfRejection IN SELECT * FROM reason_of_rejection WHERE insurer_id = $1 AND type ILIKE 'Claim Rejection' AND subscriber_active = TRUE ORDER BY name LIMIT totalRejectionReasonRecord LOOP

                        IF i=1 THEN
                            queryString = queryString || 'select ''Not Yet Routed'' as "Name",';
                        ELSE
                            queryString = queryString || 'select ''' || workgroupRecord.name || ''' as "Name",';
                        END IF;


                        IF j=2 THEN
                            queryString = queryString || '''ALL'' as "Rejection Reason",';
                        ELSE
                        queryString = queryString || '''' || reasonOfRejection.name || ''' as "Rejection Reason",';
                        END IF;


                        queryString = queryString || '(SELECT COUNT(*) FROM claim c,audit_trail a WHERE c.insurer_id = ' || $1 || ' AND a.claim_id = c.id AND a.reverted = FALSE ';
                        queryString = queryString || 'AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE) ';
                        queryString = queryString || 'AND a.created_date between ''' || $2 || '''::DATE AND ''' || $3 || '''::DATE ';
                        queryString = queryString || 'AND a.new_status = ''SubscriberClaimRejected'' ';

                        IF i=1 THEN
                            queryString = queryString || 'AND c.workgroup_id IS NULL ';
                        ELSE
                            queryString = queryString || 'AND c.workgroup_id = ' || workgroupRecord.id || ' ';
                        END IF;

                        IF j=1 THEN
                            queryString = queryString || 'AND a.claim_reason_of_rejection = ' || reasonOfRejection.id || ' ';
                        END IF;

                        queryString = queryString || ') AS "Total # rejected",';

                        FOR k in 1..$4 LOOP
                            queryString = queryString || '(SELECT COUNT(*) FROM claim c, audit_trail a WHERE c.insurer_id = ' || $1 || ' AND a.claim_id = c.id AND a.reverted = FALSE ';
                            queryString = queryString || 'AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE) ';
                            queryString = queryString || 'AND a.new_status = ''SubscriberClaimRejected'' ';
                            queryString = queryString || 'AND a.created_date between ''' || $2 || '''::DATE AND ''' || $3 || '''::DATE ';

                            IF i=1 THEN
                                queryString = queryString || 'AND c.workgroup_id IS NULL ';
                            ELSE
                                queryString = queryString || 'AND c.workgroup_id = '|| workgroupRecord.id || ' ';
                            END IF;
                            IF j=1 THEN
                                queryString = queryString || 'AND a.claim_reason_of_rejection = ' || reasonOfRejection.id || ' ';
                            END IF;

                            queryString = queryString || 'AND ((a.created_date::date - c.created_date::date) + 1) = ' || k || ') AS "# rejected on day ' || k || '",';
                        END LOOP;

                        queryString = queryString || '(SELECT STRING_AGG(c.cho_reference, '', '') FROM claim c,audit_trail a WHERE c.insurer_id = ' || $1 || ' ';
                        queryString = queryString || 'AND a.claim_id = c.id AND a.reverted = FALSE AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE) ';
                        queryString = queryString || 'AND a.created_date between ''' || $2 || '''::DATE AND ''' || $3 || '''::DATE ';
                        queryString = queryString || 'AND a.new_status = ''SubscriberClaimRejected'' ';

                        IF i=1 THEN
                            queryString = queryString || 'AND c.workgroup_id IS NULL ';
                        ELSE
                            queryString = queryString || 'AND c.workgroup_id = ' || workgroupRecord.id || ' ';
                        END IF;

                        IF j=1 THEN
                            queryString = queryString || 'AND a.claim_reason_of_rejection = ' || reasonOfRejection.id || ' ';
                        END IF;

                        queryString = queryString || ') AS "Supplier Reference Number(s)",';

                        queryString = queryString || '(SELECT STRING_AGG(coalesce(c.claim_number, ''-''), '', '') FROM claim c,audit_trail a WHERE c.insurer_id = ' || $1 || ' ';
                        queryString = queryString || 'AND a.claim_id = c.id AND a.reverted = FALSE AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE) ';
                        queryString = queryString || 'AND a.created_date between ''' || $2 || '''::DATE AND ''' || $3 || '''::DATE ';
                        queryString = queryString || 'AND a.new_status = ''SubscriberClaimRejected'' ';

                        IF i=1 THEN
                            queryString = queryString || 'AND c.workgroup_id IS NULL ';
                        ELSE
                            queryString = queryString || 'AND c.workgroup_id = ' || workgroupRecord.id || ' ';
                        END IF;

                        IF j=1 THEN
                            queryString = queryString || 'AND a.claim_reason_of_rejection = ' || reasonOfRejection.id || ' ';
                        END IF;

                        queryString = queryString || ') AS "Insurer Claim Number(s)";';

                    END LOOP;
                END LOOP;
            END LOOP;
        END LOOP;

     ELSIF (SELECT is_claim_ownership_enable FROM insurer WHERE id = $1) THEN

        FOR i IN 1..2 LOOP

            IF i=1 THEN
               totalRecord = 1;
            ELSE
               totalRecord = (SELECT COUNT(*) FROM web_user wu WHERE wu.insurer_id = $1 AND wu.status = TRUE);
            END IF;

            FOR claimOwnerRecord IN SELECT * FROM web_user wu WHERE wu.insurer_id = $1 AND wu.status = TRUE ORDER BY wu.first_name || ' ' || wu.last_name LIMIT totalRecord LOOP

                FOR j IN 1..2 LOOP

                    IF j=1 THEN
                       totalRejectionReasonRecord = (SELECT COUNT(*) FROM reason_of_rejection WHERE insurer_id = $1 AND type ILIKE 'Claim Rejection' AND subscriber_active = TRUE);
                    ELSE
                       totalRejectionReasonRecord = 1;
                    END IF;

                    FOR reasonOfRejection IN SELECT * FROM reason_of_rejection WHERE insurer_id = $1 AND type ILIKE 'Claim Rejection' AND subscriber_active = TRUE ORDER BY name LIMIT totalRejectionReasonRecord LOOP

                        IF i=1 THEN
                            queryString = queryString || 'select ''Not Yet Assigned'' as "Name", ';
                        ELSE
                            queryString = queryString || 'select ''' || claimOwnerRecord.first_name || ' ' || claimOwnerRecord.last_name || '''as "Name", ';
                        END IF;


                        IF j=2 THEN
                            queryString = queryString || '''ALL'' as "Rejection Reason",';
                        ELSE
                            queryString = queryString || '''' || reasonOfRejection.name || ''' as "Rejection Reason",';
                        END IF;

                        queryString = queryString || '(SELECT COUNT(*) FROM claim c,audit_trail a WHERE c.insurer_id = ' || $1 || ' AND a.claim_id = c.id AND a.reverted = FALSE ';
                        queryString = queryString || 'AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE) ';
                        queryString = queryString || 'AND a.created_date between ''' || $2 || '''::DATE AND ''' || $3 || '''::DATE ';
                        queryString = queryString || 'AND a.new_status = ''SubscriberClaimRejected'' ';

                        IF i=1 THEN
                            queryString = queryString || 'AND c.claim_owner_id IS NULL ';
                        ELSE
                            queryString = queryString || 'AND c.claim_owner_id = ' || claimOwnerRecord.id || ' ';
                        END IF;

                        IF j=1 THEN
                            queryString = queryString || 'AND a.claim_reason_of_rejection = ' || reasonOfRejection.id || ' ';
                        END IF;

                        queryString = queryString || ') AS "Total # rejected",';

                        FOR k in 1..$4 LOOP
                            queryString = queryString || '(SELECT COUNT(*) FROM claim c, audit_trail a WHERE c.insurer_id = ' || $1 || ' AND a.claim_id = c.id AND a.reverted = FALSE ';
                            queryString = queryString || 'AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE) ';
                            queryString = queryString || 'AND a.new_status = ''SubscriberClaimRejected'' ';
                            queryString = queryString || 'AND a.created_date between ''' || $2 || '''::DATE AND ''' || $3 || '''::DATE ';

                            IF i=1 THEN
                                queryString = queryString || 'AND c.claim_owner_id IS NULL ';
                            ELSE
                                queryString = queryString || 'AND c.claim_owner_id = ' || claimOwnerRecord.id || ' ';
                            END IF;
                            IF j=1 THEN
                                queryString = queryString || 'AND a.claim_reason_of_rejection = ' || reasonOfRejection.id || ' ';
                            END IF;

                            queryString = queryString || 'AND ((a.created_date::date - c.created_date::date) + 1) = ' || k || ') AS "# rejected on day ' || k || '",';
                        END LOOP;

                        queryString = queryString || '(SELECT STRING_AGG(c.cho_reference, '', '') FROM claim c,audit_trail a WHERE c.insurer_id = ' || $1 || ' ';
                        queryString = queryString || 'AND a.claim_id = c.id AND a.reverted = FALSE AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE) ';
                        queryString = queryString || 'AND a.created_date between ''' || $2 || '''::DATE AND ''' || $3 || '''::DATE ';
                        queryString = queryString || 'AND a.new_status = ''SubscriberClaimRejected'' ';

                        IF i=1 THEN
                            queryString = queryString || 'AND c.claim_owner_id IS NULL ';
                        ELSE
                            queryString = queryString || 'AND c.claim_owner_id = ' || claimOwnerRecord.id || ' ';
                        END IF;

                        IF j=1 THEN
                            queryString = queryString || 'AND a.claim_reason_of_rejection = ' || reasonOfRejection.id || ' ';
                        END IF;

                        queryString = queryString || ') AS "Supplier Reference Number(s)",';

                        queryString = queryString || '(SELECT STRING_AGG(coalesce(c.claim_number, ''-''), '', '') FROM claim c,audit_trail a WHERE c.insurer_id = ' || $1 || ' ';
                        queryString = queryString || 'AND a.claim_id = c.id AND a.reverted = FALSE AND NOT EXISTS (SELECT * FROM audit_trail a1 WHERE a1.claim_id = c.id AND a1.new_status = a.new_status AND a1.created_date < a.created_date AND a1.reverted=FALSE) ';
                        queryString = queryString || 'AND a.created_date between ''' || $2 || '''::DATE AND ''' || $3 || '''::DATE ';
                        queryString = queryString || 'AND a.new_status = ''SubscriberClaimRejected'' ';

                        IF i=1 THEN
                            queryString = queryString || 'AND c.claim_owner_id IS NULL ';
                        ELSE
                            queryString = queryString || 'AND c.claim_owner_id = ' || claimOwnerRecord.id || ' ';
                        END IF;

                        IF j=1 THEN
                            queryString = queryString || 'AND a.claim_reason_of_rejection = ' || reasonOfRejection.id || ' ';
                        END IF;

                        queryString = queryString || ') AS "Insurer Claim Number(s)";';

                    END LOOP;
                END LOOP;
            END LOOP;
        END LOOP;

     ELSE

     queryString = 'select ''Neither Workgroup nor Claim Ownership Enabled - report cannot be generated.'' as "Report Generation Error"';

     END IF;

     RETURN queryString;
END;
$BODY$
LANGUAGE plpgsql;
