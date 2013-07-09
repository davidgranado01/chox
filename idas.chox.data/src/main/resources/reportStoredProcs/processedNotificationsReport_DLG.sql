DROP function processedNotificationsReportDLG(
    IN choId INTEGER,
    IN insIds INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR);

CREATE OR REPLACE FUNCTION processedNotificationsReportDLG(
    IN choId INTEGER,
    IN insIds INTEGER[],
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR)

RETURNS TABLE("AX Unique Ref" VARCHAR,
              "Team ID" VARCHAR,
              "Branch / Scanning Centre" VARCHAR,
              "Policy Number" VARCHAR,
              "Claim Number" VARCHAR,
              "Policy Holder" TEXT,
              "Customer Registration Number" VARCHAR,
              "Date of Accident" timestamp without time zone,
              "Accident Circumstances" TEXT,
              "Hire Firm Reference Number" VARCHAR,
              "Method of Claimants Repair / Replacement" VARCHAR,
              "Insurer to contact claimant to arrange inspection/repairs" VARCHAR,
              "Is Claimants vehicle roadworthy after the accident" VARCHAR,
              "Hire Firm Customer" text,
              "Hire Firm Customer Address" text,
              "TP Repairer Name" VARCHAR,
              "TP Repairer Phone Number" VARCHAR,
              "Vehicle Location" VARCHAR,
              "Vehicle Location Details" VARCHAR,
              "Claimant Vehicle Registration" VARCHAR,
              "Vehicle Damage" VARCHAR,
              "Injury" VARCHAR,
              "Hire Commenced On (If Applicable)" timestamp without time zone,
              "RBS Insurance Brand" VARCHAR,
              "Liability Status" text,
              "If wrong insurer, please provide correct info" VARCHAR,
              "Fraud - Reason For Rejection" VARCHAR,
              "In Protocol" VARCHAR)
AS

$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $3::DATE;
DATE_TO = $4::DATE;

RETURN QUERY

SELECT ''::varchar as "AX Unique Ref",
       ''::varchar as "Team ID",
       case when exists(select * from audit_trail at where at.claim_id = c.id and at.new_status='AwaitingCarHireInfo'
          and at.created_date >=  DATE_FROM and at.created_date < DATE_TO and reverted = false ) then 'CHOX'::varchar else 'Birmingham'::varchar end as "Branch / Scanning Centre",
       tp.policy_number as "Policy Number",
       c.claim_number as "Claim Number",
       tp.title || ' ' || tp.first_name || ' ' || tp.last_name as "Policy Holder",
       tp.vehicle_registration as "Customer Registration Number",
       inc.date as "Date of Accident",
       inc.incident_description  as "Accident Circumstances",
       c.cho_reference as "Hire Firm reference number",
       ''::varchar  as "Method of Claimants repair / replacement",
       ''::varchar  as "Insurer to contact claimant to arrange inspection/repairs",
       case when cust.is_usable is true then 'Yes'::varchar else case when cust.is_usable is false then 'No'::varchar else '-'::varchar end end as "Is Claimants vehicle roadworthy after the accident",
       cust.title || ' ' || cust.first_name || ' ' || cust.last_name   as "Hire Firm Customer",
       cust.address1 || ' ' || cust.address2  || ' ' || cust.address3 || ' '
          || cust.address4 || ' ' || cust.address5 || ' ' || cust.postcode  as "Hire Firm Customer Address",
       hmd.name_of_repairer as "TP Repairer Name",
       hmd.name_of_repairer as "TP Repairer Phone Number",
       cust.location as "Vehicle Location",
       cust.location as "Vehicle Location Details",
       cust.vehicle_registration as "Claimant Vehicle Registration",
       cust.damage as "Vehicle Damage",
       ''::varchar as "Injury",
       vh.rental_start as "Hire Commenced On (If applicable)",
       tp.insurer_brand as "RBS Insurance Brand",
       getLiabilityStatus(c.liability_status) as "Liability status",
       (select comment from comment where claim_id=c.id and comment like 'Supporting Liability Notes%' order by id desc limit 1) as "If wrong insurer, please provide correct info",
       case when exists (select * from audit_trail at where at.claim_id=c.id and at.created_date >= DATE_FROM and at.created_date < DATE_TO
              and new_status = 'ClaimRejected' and reverted=false) then ror.name else ''::varchar end as "Fraud - Reason For Rejection",
       case when exists(select * from audit_trail at where at.claim_id = c.id and at.new_status='AwaitingCarHireInfo'
          and at.created_date >=  DATE_FROM and at.created_date < DATE_TO and reverted = false ) then 'Yes'::varchar else 'No'::varchar end as "In Protocol"
FROM claim c
  left outer join third_party tp on (c.third_party_id = tp.id)
  left outer join incident inc on (c.incident_id = inc.id)
  left outer join customer cust on (c.customer_id = cust.id)
  left outer join hire_monitoring_detail hmd on (c.hire_monitoring_detail_id = hmd.id)
  left outer join vehicle_hire vh on (c.vehicle_hire_id = vh.id)
  left outer join reason_of_rejection ror on (c.reason_of_rejection_id = ror.id)
WHERE (   exists (select * from audit_trail at where at.claim_id=c.id and at.created_date >= DATE_FROM and at.created_date < DATE_TO
              and at.new_status = 'ClaimClosed' and at.reverted=false)
       or exists (select * from audit_trail at where at.claim_id=c.id and at.created_date >= DATE_FROM and at.created_date < DATE_TO
              and at.new_status = 'AwaitingCarHireInfo' and at.reverted=false))
  AND c.chorganisation_id = choId
  AND (case when array_length(insIds, 1) > 0 then c.insurer_id = ANY(insIds) else true end)
  AND c.claim_type in (10,14,15,16,17);

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION processedNotificationsReportDLG(
                                              IN choId INTEGER,
                                              IN insIds INTEGER[],
                                              IN startPeriod VARCHAR,
                                              IN endPeriod VARCHAR)
TO chox_user;

GRANT EXECUTE ON FUNCTION processedNotificationsReportDLG(
                                              IN choId INTEGER,
                                              IN insIds INTEGER[],
                                              IN startPeriod VARCHAR,
                                              IN endPeriod VARCHAR)
TO chox_mi;

/* select * from processedNotificationsReportDLG(1132, null, array[11,12,13], '2013-06-03', '2013-06-04'); */


