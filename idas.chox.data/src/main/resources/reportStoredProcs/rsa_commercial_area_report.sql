-- DROP function rsa_commercial_area_report(IN insId int, IN choId text, IN startDate text, IN endDate text);

CREATE OR REPLACE FUNCTION rsa_commercial_area_report(IN insId int, IN choId text, IN startDate text, IN endDate text)
  RETURNS TABLE("CHO Reference" varchar, "Insurer Claim Number" varchar, "Original Hire Gross" numeric, "Current Hire Gross" numeric, "Hire Gross Paid" numeric, "Current Hire Penalty Charge" numeric, 
  "Hire Penalty Charege Paid" numeric, "Days from Claim Upload Date to Payment Received" float, "Days from Invoice Upload Date to Payment Received" float,
  "Days from Invoice Upload Date to Invoice Payment Logged" float, "No. Claim Touch Points" bigint, "No. Invoice Touch Points" bigint,
  "Day Rate Of Hire Paid" numeric, "No. Days Hire" numeric, "Current Claim Status" varchar, "Claim Type" text, "CHO Name" varchar, "Workgroup Area" text, 
  "Insurer Claim Owner" text, "Last Status Modified Date" timestamp, "Liability Status" text, "Date Claim Uploaded into CHOX" timestamp, "3rd Party Vehicle Reg Number" varchar) AS
$BODY$

DECLARE
   cho_id INT[] = choId::INT[];
   END_DATE date;
   START_DATE date;
   personal int[];
   commercial int[];
   injury int[];
   outOfScope int[];
   notAllocated int[];
BEGIN
   END_DATE = endDate::Date;
   START_DATE = startDate::Date;
   -- Personal: 'Damage - Birmingham', 'Damage - Halifax', 'ProActive - Halifax'
   --   1 | Damage - Birmingham
   --  14 | Damage - Halifax
   -- 106 | ProActive - Halifax
   personal := '{1, 14, 106}'::int[];
   -- Commercial: 'Bespoke - Glasgow', 'Bespoke - Halifax', 'Bespoke - Manchester', 'Commercial Credit Hire - Manchester', 'Damage - Glasgow', 
   --             'Damage - Manchester', 'Motor Trade - Manchester', 'Risk Solution - Chelmsford', 'Taxi - Chelmsford'
   --   8 | Bespoke - Glasgow
   --   9 | Bespoke - Halifax
   --  11 | Bespoke - Manchester
   --  59 | Commercial Credit Hire – Manchester
   --  13 | Damage - Glasgow
   --  15 | Damage - Manchester
   --  23 | Motor Trade - Manchester
   --  24 | Risk Solution - Chelmsford
   --  47 | Taxi - Chelmsford
   commercial := '{8, 9, 11, 59, 13, 15, 23, 24, 47}'::integer[];
   -- Injury: Bespoke Injury - Horsham, Existing Injury - Chelmsford, Injury - Birmingham, RSA Care - Birmingham Team 1, RSA Care - Birmingham Team 2, 
   --         RSA Care - Chelmsford Team1, RSA Care - Chelmsford - Team 2, RSA Care - Croydon Team 1, RSA Care - Croydon Team 2, RSA Care - Halifax Team 2, 
   --         RSA Care - Horsham Team 2, RSA Care - ManchesterTeam 1, RSA Care - Manchester Team 2, RSA Care - Not Allocated 
   --  54 | Bespoke Injury - Horsham
   --  56 | Existing Injury - Chelmsford
   --  16 | Injury - Birmingham
   --  61 | RSA Care - Birmingham - Team 1
   --  30 | RSA Care - Birmingham - Team 2
   --  31 | RSA Care - Chelmsford - Team 1
   --  32 | RSA Care - Chelmsford - Team 2 
   --  33 | RSA Care - Croydon - Team 1
   --  34 | RSA Care - Croydon - Team 2
   --  36 | RSA Care - Halifax - Team 2
   --  38 | RSA Care - Horsham - Team 2 
   --  39 | RSA Care - Manchester - Team 1
   --  40 | RSA Care - Manchester - Team 2
   --  41 | RSA Care - Not allocated
   injury := '{54, 56, 16, 61, 30, 31, 32, 33, 34, 36, 38, 39, 40, 41}'::integer[];
   -- Out of Scope: 'Belfast - Out Of Scope', 'BRE Pass', 'Complex - Horsham', 'Damage - Belfast', 'Foreign - Manchester', 'Large - Manchester', 'Legal - Halifax', 'Motability - Out Of Scope', 
   --               'Risk Solution - Large - Chelmsford', 'Risk Solutions Global', 'RSA Care - Halifax - CTU', 'RSA Care- Birmingham - CTU', 'RSA Legal - Manchester', 'SIH - Birmingham', 
   --               'SIH - Chelmsford', 'SIH - Halifax', 'SIH - Manchester', 'Tower - IOM - Out Of Scope'
   --   6 | Belfast - Out Of Scope
   --  80 | BRE Pass
   --  82 | Complex - Horsham
   -- 104 | Damage - Belfast
   --  49 | Foreign - Manchester
   --  20 | Large - Manchester
   --  83 | Legal - Halifax
   --  22 | Motability - Out Of Scope
   --  27 | Risk Solution - Large - Chelmsford
   --  84 | Risk Solutions Global
   -- 115 | RSA Care - Halifax - CTU
   -- 116 | RSA Care- Birmingham - CTU
   --  97 | RSA Legal - Manchester
   --  42 | SIH - Birmingham
   --  52 | SIH - Chelmsford
   --  43 | SIH - Halifax
   --  50 | SIH - Manchester
   --  44 | Tower - IOM - Out Of Scope
   outOfScope := '{6, 80, 82, 104, 49, 20, 83, 22, 27, 84, 115, 116, 97, 42, 52, 43, 50, 44}'::integer[];
   -- Commercial: 'E-Choice - Not Allocated', 'More Than - Not Allocated', 'UKC - Not Allocated', 'UKP - Not Allocated' 
   --  60 | E-Choice - Not Allocated
   --  5  | More Than - Not Allocated
   --  46 | UKC - Not Allocated 
   --  48 | UKP - Not Allocated
   notAllocated := '{60, 5, 46, 48}'::integer[];
   
RETURN QUERY

SELECT
     c.cho_reference,
     c.claim_number,
     io.hire_gross,
     i.hire_gross,
     CASE WHEN i.final_payment IS NOT null THEN i.hire_gross_paid ELSE i.hire_gross END,
     i.hire_penalty_charge,
     CASE WHEN i.final_payment IS NOT null THEN i.hire_penalty_charge_paid ELSE i.hire_penalty_charge END,
     CASE WHEN a1.id IS null THEN null ELSE EXTRACT(DAY FROM (a1.created_date - c.created_date)) END,
     CASE WHEN a1.id IS null THEN null ELSE EXTRACT(DAY FROM (a1.created_date - i.created_date)) END,
     EXTRACT(DAY FROM (a.created_date - i.created_date)),
     ((SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'ClaimUnacknowledgedRouted' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'ClaimRejectionContested' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'ClaimPending' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'ClaimUpdatedByEngineer' AND a2.reverted = false )) AS "No. Claim Touch Points",
     ((SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'InvoiceApprovedByBRE' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'InvoiceEscalated' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'InvoiceEscalatedToHandler' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'InvoiceReferredToClaimsHandler' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'InvoiceReferredToEngineer' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'InvoiceUnassigned' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'ContestedInvoiceReferredToInsurer' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'AwaitingInvoicePayment' AND a2.reverted = false )
      +(SELECT COUNT(*) FROM audit_trail a2 WHERE a2.claim_id = c.id AND a2.new_status = 'AwaitingLiabilityResolution' AND a2.reverted = false )) AS "No. Invoice Touch Points",
      CASE WHEN (vh.days IS null OR vh.days = 0) THEN 0 
            ELSE ((i.hire_net - (i.miscellaneous_fee + i.automatic_fee + i.additional_driver_fee + i.sat_nav_fee + i.estate_fee 
                    + i.baby_seat_fee + i.tow_bars_fee + i.non_standard_insurance_premium_fee + i.admin_fee + i.roof_rack_fee 
                    + i.dual_control_fee + i.delivery_collection_fee)) / vh.days)::numeric(8,2) END,
      vh.days,
      c.status,
      getclaimtype(c.claim_type),
      cho.name,
      CASE WHEN wg.id = ANY (personal) THEN 'Personal' 
           WHEN wg.id = ANY (commercial) THEN 'Commercial' 
           WHEN wg.id = ANY (injury) THEN 'Injury' 
           WHEN wg.id = ANY (outOfScope) THEN 'Out of Scope' 
           WHEN wg.id = ANY (notAllocated) THEN 'Not Allocated' ELSE '' END,
      (wu.last_name || wu.first_name), 
      c.status_modified_date,
      getliabilitystatus(c.liability_status),
      c.created_date,
      tp.vehicle_registration 
FROM
     claim c 
     INNER JOIN invoice i ON c.invoice_id = i.id
     INNER JOIN invoice_original io ON io.id = i.invoice_original_id 
     INNER JOIN vehicle_hire vh ON vh.id = c.vehicle_hire_id
     INNER JOIN chorganisation cho ON cho.id = c.chorganisation_id
     INNER JOIN third_party tp ON tp.id = c.third_party_id
     INNER JOIN audit_trail a on (a.claim_id = c.id and a.reverted = false and a.new_Status = 'InvoicePaymentLogged')
     LEFT JOIN audit_trail a1 on (a1.claim_id = c.id and a1.reverted = false and a1.new_Status = 'PaymentReceived')
     LEFT JOIN web_user wu ON c.claim_owner_id = wu.id
     LEFT JOIN workgroup wg ON c.workgroup_id = wg.id
WHERE
     c.insurer_id = $1
     AND (c.chorganisation_id != ANY (cho_id) OR -1 = ANY (cho_id))
     AND a.created_date between START_DATE and END_DATE
     ORDER BY 18;

END;
$BODY$
LANGUAGE plpgsql VOLATILE;
ALTER FUNCTION rsa_commercial_area_report(IN insId int, IN choId text, IN startDate text, IN endDate text)
OWNER TO chox;
