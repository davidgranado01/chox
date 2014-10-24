DROP FUNCTION open_claims_report(IN choIDs INT[]);

CREATE OR REPLACE FUNCTION open_claims_report(IN choIDs INT[])
  RETURNS TABLE("CHO Name" varchar(128), "Supplier Reference" varchar(128), "Insurer Claim Number" varchar(128), "Claim Type" text, "Current Claim Status" varchar(128), "Status Modified Date" timestamp, "Liability Status" text,
                "Liability % Agreed CHO" numeric(5), "Liability % Agreed Insurer" numeric(5), "Customer (non-fault) First Name" varchar(128), "Customer surname" varchar(64), "Customers insurer" varchar(128), "Third Party (at fault) first name" varchar(128),
                "Third Party Surname" varchar, "Third Party vehicle reg" varchar(16)) AS
$BODY$
DECLARE
BEGIN 

RETURN QUERY 
select cho.name as "CHO Name", 
       c.cho_reference as "Supplier Reference",
       c.claim_number as "Insurer Claim Number", 
       getClaimType(c.claim_type) as "Claim Type", 
       c.status as "Current Claim Status",
       c.status_modified_date as "Status Modified Date",
       getLiabilityStatus(c.liability_status) as "Liability Status",
       round(c.percentage_liability_cho,1) as "Liability % Agreed CHO",
       round(c.percentage_liability_accepted,1) as "Liability % Agreed Insurer", 
       cu.first_name as "Customer (non-fault) First Name",
       cu.last_name as "Customer surname",
       ins.name as "Customers insurer",
       th.first_name as "Third Party (at fault) first name",
       th.last_name as "Third Party Surname",
       th.vehicle_registration as "Third Party vehicle reg"
from chorganisation cho,
     insurer ins,
     claim c left outer join third_party th on (c.third_party_id = th.id)
             left outer join customer cu on (c.customer_id = cu.id)
where c.chorganisation_id = cho.id 
     and c.insurer_id = ins.id
     and c.invoice_id is null
     and c.status not in ('ClaimRejectionAccepted', 'ClaimClosed')
     and cho.id = ANY (choIDs) 
order by cho.name, 
         c.cho_reference;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION open_claims_report(IN choIDs INT[]) TO chox_user;
GRANT EXECUTE ON FUNCTION open_claims_report(IN choIDs INT[]) TO chox_mi;
