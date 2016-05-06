-- Function: applyautopenaltycharge(integer, integer)

-- DROP FUNCTION applyautopenaltycharge(integer, integer);

CREATE OR REPLACE FUNCTION applyAutoPenaltyCharge(useridnumber integer, claimid integer)
  RETURNS BOOLEAN AS
$BODY$

DECLARE

    claimRecord RECORD;
    hireStartDate DATE;
    hirepenalPerVal NUMERIC(8,4);
    repairpenalPerVal NUMERIC(8,4);
    penaltyAge INTEGER;
    nextPenaltyBand INTEGER;
    useNextCommercial BOOLEAN;

BEGIN

FOR claimRecord IN

SELECT
     c.id as claim_claim_id, *
FROM
     claim c,
     invoice i,
     bre_band bre,
     bre_band_organisation breorg,
     bre_penalty_band bpb,
     insurer ins,
     chorganisation cho,
     vehicle_hire vc
WHERE
     c.invoice_id = i.id
     AND (c.id = $2 OR $2 = -1)
     AND ( (c.claim_type in (0,1,2) and bre.allow_gta_penalty_charges = true and bre.allow_gta_penalty_charges_auto = true)
        or (c.claim_type = 3 and bre.allow_tpi_penalty_charges = true and bre.allow_tpi_penalty_charges_auto = true)
        or (c.claim_type in (4,5,6) and bre.allow_ins_vs_ins_penalty_charges = true and bre.allow_ins_vs_ins_penalty_charges_auto = true)
        or (c.claim_type in (7,8,9) and bre.allow_subscriber_penalty_charges = true and bre.allow_subscriber_penalty_charges_auto = true)
        or (c.claim_type in (10,14,15,16,17) and bre.allow_manual_inv_penalty_charges = true and bre.allow_manual_inv_penalty_charges_auto = true)
        or (c.claim_type in (11,12,13) and bre.allow_fixed_fee_penalty_charges = true and bre.allow_fixed_fee_penalty_charges_auto = true)
        or (c.claim_type in (18,19,20) and bre.allow_collaboration_penalty_charges = true and bre.allow_collaboration_penalty_charges_auto = true)
      )
     AND bpb.bre_band_id = bre.id
     AND bpb.claim_type = getMainClaimType(c.claim_type)
     AND c.status NOT IN ('ClaimClosed', 'InvoiceRejectionAccepted','PaymentReceived','InvoicePaymentLogged','InvoiceDataCalculationIncorrect','ManualInvoicePaid')
     AND c.insurer_id = ins.id
     AND c.chorganisation_id = cho.id
     AND bre.insurer_id = ins.id
     AND breorg.chorganisation_id = cho.id
     AND breorg.band_id = bre.id
     AND vc.id = c.vehicle_hire_id
     AND c.auto_penalty_charges = TRUE
     AND cho.auto_penalty_charges = TRUE
     AND (((vc.rental_start IS NULL) AND (bpb.start_date <= i.date_invoiced)) OR ((vc.rental_start IS NOT NULL) AND (bpb.start_date <= vc.rental_start)))
     AND i.penalty_band > 0
     AND (current_date - i.auto_penalty_start::DATE) >= i.penalty_band
     -- Subquery to exclude the older penalty band entries
     AND NOT EXISTS (SELECT
                          bpb1.id
                     FROM 
                          bre_penalty_band bpb1
                     WHERE
                          bpb1.claim_type = bpb.claim_type
                          AND bpb1.bre_band_id = bpb.bre_band_id
                          AND (((vc.rental_start IS NULL) AND (bpb1.start_date <= i.date_invoiced)) OR ((vc.rental_start IS NOT NULL) AND (bpb1.start_date <= vc.rental_start)))
                          AND bpb1.start_date > bpb.start_date)
LOOP

penaltyAge = current_date - claimRecord.auto_penalty_start::DATE;

hireStartDate = CASE WHEN claimRecord.rental_start is not null then claimRecord.rental_start else claimRecord.date_invoiced END;

-- Determine hire and/or repar percentages from penalty band
hirepenalPerVal = CASE WHEN claimRecord.penalty_band = claimRecord.hire_period_start_day_1 and claimRecord.use_commercial_day_1 != true then claimRecord.hire_day_1/100.0 ELSE
                  CASE WHEN claimRecord.penalty_band = claimRecord.hire_period_start_day_2 and claimRecord.use_commercial_day_2 != true then claimRecord.hire_day_2/100.0 ELSE
                  CASE WHEN claimRecord.penalty_band = claimRecord.hire_period_start_day_3 and claimRecord.use_commercial_day_3 != true then claimRecord.hire_day_3/100.0 ELSE 0.0 END END END;

repairpenalPerVal = CASE WHEN claimRecord.penalty_band = claimRecord.repair_period_start_day_1 and claimRecord.use_commercial_day_1 != true then claimRecord.repair_day_1/100.0 ELSE
                    CASE WHEN claimRecord.penalty_band = claimRecord.repair_period_start_day_2 and claimRecord.use_commercial_day_2 != true then claimRecord.repair_day_2/100.0 ELSE
                    CASE WHEN claimRecord.penalty_band = claimRecord.repair_period_start_day_3 and claimRecord.use_commercial_day_3 != true then claimRecord.repair_day_3/100.0 ELSE 0.0 END END END;

-- Determine next penalty Band
useNextCommercial = CASE WHEN penaltyAge >= claimRecord.hire_period_start_day_2 and penaltyAge < claimRecord.hire_period_start_day_3 and claimRecord.hire_period_start_day_2 > 0 and claimRecord.hire_period_start_day_3 > 0 THEN claimRecord.use_commercial_day_3 ELSE
                  CASE WHEN penaltyAge >= claimRecord.hire_period_start_day_2 and penaltyAge < claimRecord.hire_period_start_day_3 and claimRecord.hire_period_start_day_2 > 0 and claimRecord.hire_period_start_day_3 <= 0 then claimRecord.use_commercial_day_3 ELSE
                  CASE WHEN penaltyAge >= claimRecord.hire_period_start_day_1 and penaltyAge < claimRecord.hire_period_start_day_2 and claimRecord.hire_period_start_day_1 > 0 and claimRecord.hire_period_start_day_2 > 0 THEN claimRecord.use_commercial_day_2 ELSE
                  CASE WHEN penaltyAge >= claimRecord.hire_period_start_day_1 and penaltyAge < claimRecord.hire_period_start_day_2 and claimRecord.hire_period_start_day_1 > 0 and claimRecord.hire_period_start_day_2 <= 0 THEN claimRecord.use_commercial_day_2 ELSE true END END END END;

nextPenaltyBand = CASE WHEN penaltyAge >= claimRecord.hire_period_start_day_2 and penaltyAge < claimRecord.hire_period_start_day_3 and claimRecord.hire_period_start_day_2 > 0 and claimRecord.hire_period_start_day_3 > 0 THEN claimRecord.hire_period_start_day_3 ELSE
                  CASE WHEN penaltyAge >= claimRecord.hire_period_start_day_2 and penaltyAge < claimRecord.hire_period_start_day_3 and claimRecord.hire_period_start_day_2 > 0 and claimRecord.hire_period_start_day_3 <= 0 then claimRecord.repair_period_start_day_3 ELSE
                  CASE WHEN penaltyAge >= claimRecord.hire_period_start_day_1 and penaltyAge < claimRecord.hire_period_start_day_2 and claimRecord.hire_period_start_day_1 > 0 and claimRecord.hire_period_start_day_2 > 0 THEN claimRecord.hire_period_start_day_2 ELSE
                  CASE WHEN penaltyAge >= claimRecord.hire_period_start_day_1 and penaltyAge < claimRecord.hire_period_start_day_2 and claimRecord.hire_period_start_day_1 > 0 and claimRecord.hire_period_start_day_2 <= 0 THEN claimRecord.repair_period_start_day_2 ELSE -1 END END END END;

-- if the next penalty band is commercial, then disable automatic penalty charges (for the next iteration)
IF useNextCommercial = true THEN
    UPDATE claim
        SET auto_penalty_charges = false
    WHERE claim.id = claimRecord.claim_claim_id;
END IF;

IF hirepenalPerVal != 0.0 or repairpenalPerVal != 0.0 THEN
RAISE NOTICE 'invoice is % days > % days : choRef %    hireStartDate=%    hirepenalPer=%    repairpenalPer=%', penaltyAge, claimRecord.penalty_band, claimRecord.cho_reference, hireStartDate, hirepenalPerVal*100.0::numeric(4,1), repairpenalPerVal*100.0::numeric(4,1);

    UPDATE
      invoice
    SET
      penalty_band = nextPenaltyBand,
      hire_penalty_charge_applied_date = (CASE WHEN(hirepenalPerVal > 0.0) THEN now() ELSE null END),
      repair_penalty_charge_applied_date = (CASE WHEN(repairpenalPerVal > 0.0) THEN now() ELSE null END),
      full_total_to_pay = (full_total_to_pay - gta_discount - (hire_penalty_charge + repair_penalty_charge) + (hire_gross * hirepenalPerVal) + (repair_gross * repairpenalPerVal))::NUMERIC(8,2),
      total_to_pay = (CASE WHEN ((claimRecord.liability_status = 5 OR claimRecord.liability_status = 6) AND claimRecord.claim_type NOT IN (7,8,9,11,12,13,18,19,20))
                           THEN ((claimRecord.percentage_liability_accepted/100) * (full_total_to_pay - (hire_penalty_charge + repair_penalty_charge) + (hire_gross * hirepenalPerVal) + (repair_gross * repairpenalPerVal)))::NUMERIC(8,2)
                      WHEN ((claimRecord.liability_status = 4) AND claimRecord.claim_type NOT IN (7,8,9,11,12,13,18,19,20))
                           THEN (0.00)
                      ELSE (full_total_to_pay - (hire_penalty_charge + repair_penalty_charge) + (hire_gross * hirepenalPerVal) + (repair_gross * repairpenalPerVal))::NUMERIC(8,2)
                           END),
      hire_penalty_charge = (hire_gross * hirepenalPerVal)::NUMERIC(8,2),
      repair_penalty_charge = (repair_gross * repairpenalPerVal)::NUMERIC(8,2),
      gta_discount = 0.00,
      hire_penalty_percentage = (CASE WHEN(hire_net > 0) THEN (hirepenalPerVal*100.0)::numeric(6,2) || '%' ELSE null END),
      repair_penalty_percentage = (CASE WHEN(repair_net > 0) THEN (repairpenalPerVal*100.0)::numeric(6,2) || '%' ELSE null END),
      total_penalty_charge = ((hire_gross * hirepenalPerVal) + (repair_gross * repairpenalPerVal))::NUMERIC(8,2),
      last_modified_by = $1,
      last_modified_date = now(),
      version = version + 1
   WHERE
     claimRecord.invoice_id = invoice.id;

 -- Insurer Discount will be applied to the claim

   UPDATE invoice
   SET
      hire_gross_insurer_discount = (CASE WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.hire_gross + invoice.hire_penalty_charge)*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = FALSE) THEN (invoice.hire_gross*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ),
      repair_gross_insurer_discount = (CASE WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.repair_gross + invoice.repair_penalty_charge)*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = FALSE) THEN (invoice.repair_gross*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ),
      total_gross_insurer_discount = (CASE WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.total_gross + invoice.total_penalty_charge)*(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = FALSE) THEN (invoice.total_gross *(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ),
      insurer_discount = ((CASE WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.hire_gross + invoice.hire_penalty_charge)*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = FALSE) THEN (invoice.hire_gross*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ) + 
                           (CASE WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.repair_gross + invoice.repair_penalty_charge)*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = FALSE) THEN (invoice.repair_gross*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ) +
                           (CASE WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.total_gross + invoice.total_penalty_charge)*(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = FALSE) THEN (invoice.total_gross *(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END )),
      full_total_to_pay = ((invoice.full_total_to_pay - invoice.insurer_discount) + (
                    (CASE WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.hire_gross + invoice.hire_penalty_charge)*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = FALSE) THEN (invoice.hire_gross*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ) + 
                    (CASE WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.repair_gross + invoice.repair_penalty_charge)*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = FALSE) THEN (invoice.repair_gross*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ) + 
                    (CASE WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.total_gross + invoice.total_penalty_charge)*(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = FALSE) THEN (invoice.total_gross *(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END )
                    ))::NUMERIC(8,2),
      total_to_pay = (CASE WHEN ((claimRecord.liability_status = 5 OR claimRecord.liability_status = 6) AND claimRecord.claim_type NOT IN (7,8,9,11,12,13,18,19,20))
                           THEN ((c.percentage_liability_accepted/100) * ((invoice.full_total_to_pay - invoice.insurer_discount + (
                                (CASE WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.hire_gross + invoice.hire_penalty_charge)*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = FALSE) THEN (invoice.hire_gross*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ) + 
                                (CASE WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.repair_gross + invoice.repair_penalty_charge)*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = FALSE) THEN (invoice.repair_gross*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ) + 
                                (CASE WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.total_gross + invoice.total_penalty_charge)*(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = FALSE) THEN (invoice.total_gross *(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END )
                                 ))))::NUMERIC(8,2)
                           WHEN ((claimRecord.liability_status = 4) AND claimRecord.claim_type NOT IN (7,8,9,11,12,13,18,19,20))
                           THEN (0.00)
                           ELSE (((invoice.full_total_to_pay - invoice.insurer_discount) + (
                               (CASE WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.hire_gross + invoice.hire_penalty_charge)*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (hireInsDis IS NOT NULL AND hireInsDis.is_applied_to_penalties = FALSE) THEN (invoice.hire_gross*(hireInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ) + 
                               (CASE WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.repair_gross + invoice.repair_penalty_charge)*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (repairInsDis IS NOT NULL AND repairInsDis.is_applied_to_penalties = FALSE) THEN (invoice.repair_gross*(repairInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END ) + 
                               (CASE WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = TRUE) THEN ((invoice.total_gross + invoice.total_penalty_charge)*(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) WHEN (totalInsDis IS NOT NULL AND totalInsDis.is_applied_to_penalties = FALSE) THEN (invoice.total_gross *(totalInsDis.discount_percentage/100)*-1)::NUMERIC(8,2) ELSE 0.00 END )
                    )))::NUMERIC(8,2)
                    END ),
      last_modified_by = $1,
      last_modified_date = now(),
      version = invoice.version + 1
   FROM
      claim c inner join insurer ins on (ins.id = c.insurer_id AND ins.is_insurer_discount_enable = true)
         inner join chorganisation cho on cho.id = c.chorganisation_id
         inner join invoice i on i.id = c.invoice_id
         left outer join insurer_discount hireInsDis on (hireInsDis.insurer_id = ins.id AND hireInsDis.chorganisation_id = cho.id
              AND hireInsDis.discount_type = 0 AND hireInsDis.date_from <= i.created_date
              AND hireInsDis.date_to >= i.created_date)
         left outer join insurer_discount repairInsDis on (repairInsDis.insurer_id = ins.id AND repairInsDis.chorganisation_id = cho.id
              AND repairInsDis.discount_type = 1 AND repairInsDis.date_from <= i.created_date
              AND repairInsDis.date_to >= i.created_date)
         left outer join insurer_discount totalInsDis on (totalInsDis.insurer_id = ins.id AND totalInsDis.chorganisation_id = cho.id
              AND totalInsDis.discount_type = 2 AND totalInsDis.date_from <= i.created_date
              AND totalInsDis.date_to >= i.created_date)
   WHERE
      claimRecord.id = c.id
      AND invoice.id = c.invoice_id
      AND (hireInsDis IS NOT NULL OR repairInsDis IS NOT NULL OR totalInsDis IS NOT NULL);


-- Apply Penalty Comment

   INSERT INTO comment
      (claim_id,created_by,created_date,last_modified_by,last_modified_date,visibility_type,comment,version)
   SELECT
      claimRecord.id,
      $1,
      now() + interval '0.1 sec',
      $1,
      now() + interval '0.1 sec',
      0,
      'Automatic penalty charges of £' ||  ((i.hire_gross * hirepenalPerVal) + (i.repair_gross * repairpenalPerVal))::NUMERIC(10,2)
                                      || ' have been applied to the invoice as the age of the invoice has exceeded ' || claimRecord.penalty_band || ' days.',
     0
   FROM
     invoice i
   WHERE
     i.id = claimRecord.invoice_id;


-- Apply Hire Gross Insurer Discount Comment

   INSERT INTO comment
      (claim_id,created_by,created_date,last_modified_by,last_modified_date,visibility_type,version,comment)
   SELECT
      c.id,
      $1,
      now() + interval '0.1 sec',
      $1,
      now() + interval '0.1 sec',
      0,0,
      'A discount of £' ||  (i.hire_gross_insurer_discount*-1)::NUMERIC(10,2)
                                      || ' ('
                                      || hireInsDis.discount_percentage
                                      || '%) has been applied to the Hire on this invoice based on the discount contract in place.'
   FROM
     claim c inner join insurer ins on (ins.id = c.insurer_id AND ins.is_insurer_discount_enable = true)
             inner join chorganisation cho on cho.id = c.chorganisation_id
             inner join invoice i on (i.id = c.invoice_id AND i.hire_gross > 0)
             inner join insurer_discount hireInsDis on (hireInsDis.insurer_id = ins.id AND hireInsDis.chorganisation_id = cho.id
                            AND hireInsDis.discount_type = 0 AND hireInsDis.date_from <= i.created_date
                            AND hireInsDis.date_to >= i.created_date)
   WHERE
      claimRecord.id = c.id;

-- Apply Repair Gross Insurer Discount Comment
   INSERT INTO comment
      (claim_id,created_by,created_date,last_modified_by,last_modified_date,visibility_type,version,comment)
   SELECT
      c.id,
      $1,
      now() + interval '0.1 sec',
      $1,
      now() + interval '0.1 sec',
      0,0,
      'A discount of £' ||  (i.repair_gross_insurer_discount*-1)::NUMERIC(10,2)
                                      || ' ('
                                      || repairInsDis.discount_percentage
                                      || '%) has been applied to the Repair on this invoice based on the discount contract in place.'
   FROM
     claim c inner join insurer ins on (ins.id = c.insurer_id AND ins.is_insurer_discount_enable = true)
             inner join chorganisation cho on cho.id = c.chorganisation_id
             inner join invoice i on (i.id = c.invoice_id AND i.repair_gross > 0)
             inner join insurer_discount repairInsDis on (repairInsDis.insurer_id = ins.id AND repairInsDis.chorganisation_id = cho.id
                            AND repairInsDis.discount_type = 1 AND repairInsDis.date_from <= i.created_date
                            AND repairInsDis.date_to >= i.created_date)
   WHERE
      claimRecord.id = c.id;

-- Apply Total Gross Insurer Discount Comment
   INSERT INTO comment
      (claim_id,created_by,created_date,last_modified_by,last_modified_date,visibility_type,version,comment)
   SELECT
      c.id,
      $1,
      now() + interval '0.1 sec',
      $1,
      now() + interval '0.1 sec',
      0,0,
      'A discount of £' ||  (i.total_gross_insurer_discount*-1)::NUMERIC(10,2)
                                      || ' ('
                                      || totalInsDis.discount_percentage
                                      || '%) has been applied to the Total on this invoice based on the discount contract in place.'
   FROM
      claim c inner join insurer ins on (ins.id = c.insurer_id AND ins.is_insurer_discount_enable = true)
             inner join chorganisation cho on cho.id = c.chorganisation_id
             inner join invoice i on (i.id = c.invoice_id AND i.total_gross > 0)
             inner join insurer_discount totalInsDis on (totalInsDis.insurer_id = ins.id AND totalInsDis.chorganisation_id = cho.id
                            AND totalInsDis.discount_type = 2 AND totalInsDis.date_from <= i.created_date
                            AND totalInsDis.date_to >= i.created_date)
   WHERE
      claimRecord.id = c.id;
ELSE
RAISE NOTICE '** No Penalties ** invoice is % days > % days : choRef %    hireStartDate=%    hirepenalPer=%    repairpenalPer=%', penaltyAge, claimRecord.penalty_band, claimRecord.cho_reference, hireStartDate, hirepenalPerVal*100.0::numeric(4,1), repairpenalPerVal*100.0::numeric(4,1);
END IF;

END LOOP;

--
-- Now remove GTA discounts on invoices over 30 days old
--
update invoice
  set full_total_to_pay = full_total_to_pay - gta_discount,
      total_to_pay = total_to_pay - gta_discount*percentage_liability_accepted/100.0,
      gta_discount = 0.00,
      version = invoice.version + 1
from claim c
where c.invoice_id = invoice.id
  and gta_discount != 0.0
  and c.status not in ('PaymentReceived','InvoicePaymentLogged','InvoiceRejectionAccepted','ClaimClosed')
  and now()::date - invoice.created_date::date + 1 > 30 ;

RETURN TRUE;

END;
$BODY$
  LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION applyAutoPenaltyCharge(integer, integer) TO chox_user;
