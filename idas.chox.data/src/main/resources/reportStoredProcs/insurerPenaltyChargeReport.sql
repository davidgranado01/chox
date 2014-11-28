DROP FUNCTION insurer_penalty_charge_report(IN insIDs INT[], IN workgroupIDs INT[], IN maxDays INT, IN minDays INT);

CREATE OR REPLACE FUNCTION insurer_penalty_charge_report(IN insIDs INT[], IN workgroupIDs INT[], IN maxDays INT, IN minDays INT)
RETURNS TABLE (
   "Supplier Reference" character varying(128),
   "Insurer Claim Number" character varying(128),
   "Claim Status" character varying(40),
   "Claim Type" text,
   "CHO Name" varchar(128),
   "Insurer Name" varchar(128),
   "Workgroup" varchar(128),
   "Insurer Claim Handler" text,
   "CHO Claim Handler" text,
   "Total Loss?" text,
   "Customer Vehicle Class" varchar(10),
   "Interim Payment Made?" text,
   "Liability Status" text,
   "Invoice Upload Date" timestamp without time zone,
   "Days Since Invoice Upload" integer
)
AS
$BODY$
DECLARE
BEGIN 

RETURN QUERY 

select c.cho_reference, c.claim_number, c.status, getClaimType(c.claim_type), cho.name, ins.name, w.name,
    wu1.first_name || ' ' || wu1.last_name as claims_handler, wu2.first_name || ' ' || wu2.last_name as cho_claims_handler,
    case when cu.is_total_loss then 'Yes' else 'No' end as total_loss, vc.name,
    case when i.interim_payment_made is null then 'No' else case when i.interim_payment_made = 0.0 then 'No' else 'Yes' end end,
    getLiabilityStatus(c.liability_status),
    i.created_date,
    extract(day from now() - i.created_date)::integer
--    (current_date - i.created_date::date)+1
from claim c
  left outer join workgroup w on (w.id = c.workgroup_id)
  left outer join web_user wu1 on (wu1.id = c.claim_owner_id)
  left outer join web_user wu2 on (wu2.id = c.cho_claim_owner_id),
  insurer ins, chorganisation cho, customer cu, vehicle_hire vh, vehicle_class vc, invoice i, bre_band bre, bre_band_organisation bbo
where c.insurer_id = ins.id and c.chorganisation_id = cho.id and c.invoice_id = i.id
  and c.customer_id = cu.id and c.vehicle_hire_id = vh.id
  and bre.insurer_id = c.insurer_id and bbo.chorganisation_id = c.chorganisation_id and bbo.band_id = bre.id
  and ((bre.allow_gta_penalty_charges = TRUE AND c.claim_type in (0,1,2))
        OR (bre.allow_subscriber_penalty_charges = TRUE AND c.claim_type in (7,8,9))
        OR (bre.allow_fixed_fee_penalty_charges = TRUE AND c.claim_type in (11,12,13))
        OR (bre.allow_collaboration_penalty_charges = TRUE AND c.claim_type in (18,19,20))
        OR (bre.allow_tpi_penalty_charges = TRUE AND c.claim_type in (3))
        OR (bre.allow_ins_vs_ins_penalty_charges = TRUE AND c.claim_type in (4,5,6))
        OR (bre.allow_manual_inv_penalty_charges = TRUE AND c.claim_type in (10,14,15,16,17)))
  and cu.vehicle_class_id = vc.id
  and (insIDs is null or c.insurer_id = ANY(insIDs))
  and (workgroupIDs is null or c.workgroup_id is null or c.workgroup_id = ANY(workgroupIDs))
  and c.status in ('InvoiceApprovedByBRE','InvoiceEscalated','InvoiceEscalatedToHandler',
                    'ContestedInvoiceReferredToCHO','ContestedInvoiceReferredToInsurer',
                    'AwaitingInvoicePayment','InvoiceReferredToClaimsHandler',
                    'InvoiceReferredToEngineer')
--  and c.claim_type NOT IN (3,4,5,6,10,14,15,16,17)
  and c.liability_status != 4
  and ((extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) >= 90 - maxDays
        and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < 90 - minDays)
      or (extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) >= 60 - maxDays
        and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < 60 - minDays)
      or (extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) >= 30 - maxDays
        and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < 30 - minDays))

order by claims_handler, i.created_date asc;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION insurer_penalty_charge_report(IN insIDs INT[], IN workgroupIDs INT[], IN maxDays INT, IN minDays INT) TO chox_user;
GRANT EXECUTE ON FUNCTION insurer_penalty_charge_report(IN insIDs INT[], IN workgroupIDs INT[], IN maxDays INT, IN minDays INT) TO chox_mi;

-- select * from insurer_penalty_charge_report(array[3,19], array[14,123,15], 7, 5); -- 7 and 6 days away
-- select * from insurer_penalty_charge_report(array[3,19], array[14,123,15], 7, 4); -- 7, 6 and 5 days away
-- select * from insurer_penalty_charge_report(array[3,19], array[14,123,15], 7, 6); -- 7 days away only
-- select * from insurer_penalty_charge_report(array[3,19], array[14,123,15], 7, 0); -- 7 days away and less
-- For Monday, use (7,3), and Thursday (7,4)
