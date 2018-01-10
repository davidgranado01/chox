DROP FUNCTION If EXISTS keoghs_daily_report(integer,integer[],character varying,text,text);

CREATE OR REPLACE function keoghs_daily_report(
    IN choId integer,
    IN insurerIds integer[],
    IN reportStatus character varying(40),
    IN startPeriod text,
    IN endPeriod text)
RETURNS table (
    "LV Policy Number" character varying(32),  -- Third Party Policy Number
    "LV Claim Number" character varying(64),  -- Third Party Claim Number
    "LV Policy Holder" text,  -- Third Party Title + Third Party First Name + Third Surname
    "LV Customer Registration Number" character varying(16),  -- Third Party Vehicle Registration Number
    "Date of Accident" timestamp without time zone,  -- Incident Date/Time
    "Accident Circumstances" text,  -- Incident Description
    "Insurance Brand" character varying(64),  -- Insurer Brand
    "Indemnity Stance" character varying,  -- Indemnity Stance
    "Liability Status" text,  -- Liability Status 
    "Supporting Liability" text,  -- Supporting Liability Notes (latest only)
    "Rejection Reason" character varying -- Rejection Reason (will be empty if the status is not ‘ClaimRejected')
)
AS $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

    select tp.policy_number, c.claim_number, tp.title || ' ' || tp.first_name || ' ' || tp.last_name,
            tp.vehicle_registration, i.date, i.incident_description, tp.insurer_brand, c.indemnity_stance,
            getLiabilityStatus(c.liability_status),
            (select regexp_replace(comment, '[\n\r]+', ' ', 'g' ) from comment co where co.claim_id = c.id and co.comment like 'Supporting Liability%'
                and not exists (select * from comment co2 where co2.claim_id=c.id and co2.comment like 'Supporting Liability%' and co2.created_date > co.created_date)),
            case when reportStatus != 'ClaimRejected' then null else (select name from reason_of_rejection where id=c.reason_of_rejection_id) end
    from claim c, third_party tp, audit_trail at, incident i
    where c.third_party_id = tp.id and c.id = at.claim_id
      and at.reverted = false and at.new_status = reportStatus and at.created_date >= startDate and at.created_date < endDate
      and not exists (select * from audit_trail at2 where at2.claim_id=c.id and at.new_status = reportStatus and at2.created_date < at2.created_date)
      and c.incident_id = i.id and (c.chorganisation_id = choId or choId is null) and (insurerIds is null or c.insurer_id = ANY(insurerIds));

END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION keoghs_daily_report(integer, integer[], character varying(40), text, text) TO chox_user;
GRANT EXECUTE ON FUNCTION keoghs_daily_report(integer, integer[], character varying(40), text, text) TO chox_mi;
-- select * from keoghs_daily_report(1788, array[26], 'ClaimRejected', '2018-01-04', '2018-01-05');
-- select * from keoghs_daily_report(1788, array[26], 'AwaitingCarHireInfo', '2018-01-04', '2018-01-05');
