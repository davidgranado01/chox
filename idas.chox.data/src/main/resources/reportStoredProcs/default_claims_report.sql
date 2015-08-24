drop function default_claims_reports(IN insurerids integer[], IN startdate text, IN enddate text);
create or replace function default_claims_reports
(
   IN insurerids integer[], IN startdate text, IN enddate text
)
returns table
(
   "Supplier Reference" character varying(128),
   "Insurer Claim Number" character varying(128),
   "Workgroup" character varying(128),
   "Insurer Claim Owner" text,
   "CHO Name" character varying(128),
   "Claim Type" text,
   "Insurer Name" character varying(128),
   "Date Claim Defaulted" text,
   defaulted_date timestamp without time zone
)
as $$ DECLARE 
BEGIN 
	
	FOR i IN array_lower(insurerids, 1) .. array_upper(insurerids, 1)
    LOOP
	
RETURN QUERY

SELECT c.cho_reference as "Supplier Reference", 
c.claim_number as "Insurer Claim Number", 
w.name as "Workgroup",
wu.first_name || ' ' || wu.last_name AS "Insurer Claim Owner", 
ch.name as "CHO Name", 
(CASE WHEN c.claim_type IN (7, 8, 9) THEN 'Subscriber'
WHEN c.claim_type IN (11, 12,13) THEN 'Fixed Fee' END) as "Claim Type" , 
ins.name as "Insurer Name",
to_char(co.created_date, 'dd/mm/yyyy') as "Date Claim Defaulted",
co.created_date as defaulted_date
FROM claim c LEFT OUTER JOIN workgroup w ON c.workgroup_id = w.id,
     web_user wu,
     audit_trail a1,
     chorganisation ch,
     insurer ins,
     comment co
WHERE ins.id IN (insurerids[i]::int) -- restricted to insurers
  AND c.id = a1.claim_id
  AND c.created_date BETWEEN startdate::date AND enddate::date -- period restriction
  AND c.chorganisation_id = ch.id
  AND c.insurer_id = ins.id
  AND c.id = co.claim_id
  AND c.claim_owner_id = wu.id
  AND a1.new_status = 'AwaitingCarHireInfo' 
  AND a1.reverted = FALSE
  AND ((c.claim_type in (11,12,13) AND co.comment ILIKE ('%failed to respond to the Fixed Fee notification within the % day SLA%') and co.reverted = false)
            OR
            (c.claim_type in (7,8,9) AND co.comment ILIKE ('%failed to respond to the Subscriber notification within the % day SLA%') and co.reverted = false));
END LOOP;
END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION default_claims_reports(IN insurerids integer[], IN startdate text, IN enddate text) TO chox_user;
GRANT EXECUTE ON FUNCTION default_claims_reports(IN insurerids integer[], IN startdate text, IN enddate text) TO chox_mi;
