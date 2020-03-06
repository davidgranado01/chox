--------------------------------------------------------------------------------
-- 8.7.1 Ability To Change Penalty Charge Percentages By CHO/Insurer
--------------------------------------------------------------------------------

ALTER TABLE bre_band ADD COLUMN allow_gta_penalty_charges_auto boolean not null DEFAULT  true;
ALTER TABLE bre_band ADD COLUMN allow_subscriber_penalty_charges_auto boolean not null DEFAULT  true;
ALTER TABLE bre_band ADD COLUMN allow_fixed_fee_penalty_charges_auto boolean not null DEFAULT  true;
ALTER TABLE bre_band ADD COLUMN allow_collaboration_penalty_charges_auto boolean not null DEFAULT  true;
ALTER TABLE bre_band ADD COLUMN allow_ins_vs_ins_penalty_charges_auto boolean not null DEFAULT  false;
ALTER TABLE bre_band ADD COLUMN allow_tpi_penalty_charges_auto boolean not null DEFAULT  false;

CREATE TABLE bre_penalty_band (
    id serial NOT NULL,
    version integer,
    bre_band_id integer NOT NULL,
    claim_type integer NOT NULL,
    start_date timestamp without time zone NOT NULL,
    hire_30_day numeric(6,1) NOT NULL,
    hire_60_day numeric(6,1) NOT NULL,
    hire_90_day numeric(6,1) NOT NULL,
    hire_apply_90_day_rate boolean not null,
    hire_use_commercial boolean not null,
    repair_30_day numeric(6,1) NOT NULL,
    repair_60_day numeric(6,1) NOT NULL,
    repair_90_day numeric(6,1) NOT NULL,
    repair_apply_90_day_rate boolean not null,
    repair_use_commercial boolean not null,
    created_by integer,
    created_date timestamp without time zone NOT NULL default now(),
    last_modified_by integer,
    last_modified_date timestamp without time zone NOT NULL default now(),
    CONSTRAINT bre_penalty_band_pkey PRIMARY KEY (id),
    CONSTRAINT bre_penalty_band_ukey UNIQUE (bre_band_id, claim_type, start_date),
    CONSTRAINT bre_penalty_band_fkey FOREIGN KEY (bre_band_id)
        REFERENCES bre_band (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT created_by_fkey FOREIGN KEY (created_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT last_modified_by_fkey FOREIGN KEY (last_modified_by)
        REFERENCES web_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE bre_penalty_band TO chox_user;
GRANT SELECT ON TABLE bre_penalty_band TO chox_mi;
GRANT SELECT, UPDATE ON TABLE bre_penalty_band_id_seq TO chox_user;

-- GTA Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 0, 7.5, 15.0, 0.0, true, true, 2.5, 5.0, 0.0, false, false, 999, now(), 999, now()
from bre_band;

INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '2012-06-15', 0, 12.5, 20.0, 0.0, true, true, 2.5, 5.0, 0.0, false, false, 999, now(), 999, now()
from bre_band;

-- Subscriber Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 7, 4.0, 8.0, 12.0, true, false, 0.0, 0.0, 0.0, true, false, 999, now(), 999, now()
from bre_band;

-- Fixed-Fee Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 11, 5.0, 10.0, 15.0, true, false, 5.0, 10.0, 15.0, true, false, 999, now(), 999, now()
from bre_band;

-- Collaboration Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 18, 7.5, 15.0, 0, true, true, 2.5, 5.0, 0.0, false, false, 999, now(), 999, now()
from bre_band;
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '2012-06-15', 18, 12.5, 20.0, 0.0, true, true, 2.5, 5.0, 0.0, false, false, 999, now(), 999, now()
from bre_band;

-- Insurer Vs Insurer Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 4, 7.5, 15.0, 0.0, true, true, 2.5, 5.0, 0.0, false, false, 999, now(), 999, now()
from bre_band;
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '2012-06-15', 4, 12.5, 20.0, 0.0, true, true, 2.5, 5.0, 0.0, false, false, 999, now(), 999, now()
from bre_band;

-- TPI Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 3, 7.5, 15.0, 0.0, true, true, 2.5, 5.0, 0.0, false, false, 999, now(), 999, now()
from bre_band;
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '2012-06-15', 3, 12.5, 20.0, 0.0, true, true, 2.5, 5.0, 0.0, false, false, 999, now(), 999, now()
from bre_band;


-- Manual Rates
--INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
--                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
--                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
--select 0, id, '1950-01-01', 10, 7.50, 15.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
--from bre_band;
--INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
--                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
--                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
--select 0, id, '2012-06-15', 10, 12.50, 20.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
--from bre_band;


--
-- Update to addInvoicePenaltyTask function to check BRE band flags for penalty charges
--
CREATE OR REPLACE FUNCTION addInvoicePenaltyTask(integer)
  RETURNS boolean AS
$BODY$

DECLARE
userId int;

BEGIN

userId=$1;

insert into task(claim_id, due_date, task_type, description, insurer, visibility, visibility_role, created_by, created_date, last_modified_by, last_modified_date, version)
select c.id, now() + interval '15 days', 'Invoice Approaching 90 Days', 'The invoice was uploaded over 75 days ago and may be subject to penalties in 15 days time.',
       true, 2, 'ROLE_INS_CH', userId, now(), userId, now(), 0
from claim c, invoice i, insurer ins, bre_band_organisation bo, bre_band bre
where c.invoice_id = i.id
  and c.chorganisation_id = bo.chorganisation_id
  and bo.band_id = bre.id
  and bre.insurer_id = c.insurer_id
  and i.penalty_band = 90
  and c.insurer_id = ins.id
  and ( (c.claim_type in (0,1,2) and bre.allow_gta_penalty_charges =  true)
        or (c.claim_type = 3 and bre.allow_tpi_penalty_charges =  true)
        or (c.claim_type in (4,5,6) and bre.allow_ins_vs_ins_penalty_charges =  true)
        or (c.claim_type in (7,8,9) and bre.allow_subscriber_penalty_charges =  true)
        or (c.claim_type in (11,12,13) and bre.allow_fixed_fee_penalty_charges =  true)
        or (c.claim_type in (18,19,20) and bre.allow_collaboration_penalty_charges =  true)
      )
  and ins.is_task_management_enable = true
  and c.status NOT IN ('ClaimClosed', 'InvoiceRejectionAccepted', 'PaymentReceived', 'InvoicePaymentLogged', 'InvoiceDataCalculationIncorrect')
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) >= 75
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < 90
  and not exists (select * from task where claim_id = c.id and task_type like 'Invoice Approaching 90 Days%')
  and ((liability_status is null or (liability_status !=5 and liability_status!=6)) or ((liability_status = 5 or liability_status =6 ) and extract(epoch from now() - c.liability_agreed_date)/(3600*24) >= 75));

insert into task(claim_id, due_date, task_type, description, insurer, visibility, visibility_role, created_by, created_date, last_modified_by, last_modified_date, version)
select c.id, now() + interval '5 days', 'Invoice Approaching ' || i.penalty_band || ' Days', 'The invoice was uploaded over ' || i.penalty_band - 5 || ' days ago and may be subject to penalties in 5 days time.',
       true, 2, 'ROLE_INS_CH', userId, now(), userId, now(), 0
from claim c, invoice i, insurer ins, bre_band_organisation bo, bre_band bre
where c.invoice_id = i.id
  and c.chorganisation_id = bo.chorganisation_id
  and bo.band_id = bre.id
  and bre.insurer_id = c.insurer_id
  and i.penalty_band != -1 and i.penalty_band < 90
  and c.insurer_id = ins.id
  and ( (c.claim_type in (0,1,2) and bre.allow_gta_penalty_charges =  true)
        or (c.claim_type = 3 and bre.allow_tpi_penalty_charges =  true)
        or (c.claim_type in (4,5,6) and bre.allow_ins_vs_ins_penalty_charges =  true)
        or (c.claim_type in (7,8,9) and bre.allow_subscriber_penalty_charges =  true)
        or (c.claim_type in (11,12,13) and bre.allow_fixed_fee_penalty_charges =  true)
        or (c.claim_type in (18,19,20) and bre.allow_collaboration_penalty_charges =  true)
      )
  and ins.is_task_management_enable = true
  and c.status NOT IN ('ClaimClosed', 'InvoiceRejectionAccepted', 'PaymentReceived', 'InvoicePaymentLogged', 'InvoiceDataCalculationIncorrect')
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) >= (i.penalty_band - 5)
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < i.penalty_band
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < 90
  and not exists (select * from task where claim_id = c.id and task_type like 'Invoice Approaching%' and now() - created_date < '6 days')
  and ((liability_status is null or (liability_status !=5 and liability_status!=6)) or ((liability_status = 5 or liability_status =6 ) and extract(epoch from now() - c.liability_agreed_date)/(3600*24) > i.penalty_band - 5));


update task set complete = true,
                completed_by = 999,
                completed_date = now(),
                version = version + 1
where due_date < now() and task_type like 'Invoice Approaching%' and complete = false;


return true;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION addInvoicePenaltyTask(integer) TO chox_user;

CREATE OR REPLACE FUNCTION getMainClaimType(claimTypeId integer)
  RETURNS integer AS
$BODY$
 DECLARE
   resultString integer;
 BEGIN
    IF $1 IN (0,1,2) THEN
         resultString = 0;
    ELSIF $1 IN (3) THEN
         resultString = 3;
    ELSIF $1 IN (4,5,6) THEN
         resultString = 4;
    ELSIF $1 IN (7,8,9) THEN
         resultString = 7;
    ELSIF $1 IN (10,14,15,16,17) THEN
         resultString = 10;
    ELSIF $1 IN (11,12,13) THEN
         resultString = 11;
    ELSIF $1 IN (18,19,20) THEN
         resultString = 18;
    ELSE
         resultString = '';
    END IF;
        RETURN resultString;
 END;
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION getMainClaimType(integer) TO chox_user;


--
-- Updated applyAutoPenaltyCharge function
--
CREATE OR REPLACE FUNCTION applyAutoPenaltyCharge(useridnumber integer, claimid integer)
  RETURNS BOOLEAN AS
$BODY$

DECLARE

    claimRecord RECORD;
    hireStartDate DATE;
    hirepenalPerVal NUMERIC(8,4);
    repairpenalPerVal NUMERIC(8,4);
    penaltyAge INTEGER;
    currentPenaltyBand INTEGER;
    nextPenaltyBand INTEGER;

BEGIN

FOR claimRecord IN

SELECT
     *
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
     AND ( (c.claim_type in (0,1,2) and bre.allow_gta_penalty_charges =  true and  bre.allow_gta_penalty_charges_auto = true)
        or (c.claim_type = 3 and bre.allow_tpi_penalty_charges =  true and bre.allow_tpi_penalty_charges_auto =  true)
        or (c.claim_type in (4,5,6) and bre.allow_ins_vs_ins_penalty_charges =  true and bre.allow_ins_vs_ins_penalty_charges_auto =  true)
        or (c.claim_type in (7,8,9) and bre.allow_subscriber_penalty_charges =  true and bre.allow_subscriber_penalty_charges_auto =  true)
        or (c.claim_type in (11,12,13) and bre.allow_fixed_fee_penalty_charges =  true and bre.allow_fixed_fee_penalty_charges_auto =  true)
        or (c.claim_type in (18,19,20) and bre.allow_collaboration_penalty_charges =  true and bre.allow_collaboration_penalty_charges_auto =  true)
      )
     AND bpb.bre_band_id = bre.id
     AND bpb.claim_type = getMainClaimType(c.claim_type)
     AND c.status NOT IN ('ClaimClosed', 'InvoiceRejectionAccepted', 'PaymentReceived', 'InvoicePaymentLogged', 'InvoiceDataCalculationIncorrect')
     AND c.insurer_id = ins.id
     AND c.chorganisation_id = cho.id
     AND bre.insurer_id = ins.id
     AND breorg.chorganisation_id = cho.id
     AND breorg.band_id = bre.id
     AND vc.id = c.vehicle_hire_id
     AND c.auto_penalty_charges = TRUE
     AND cho.auto_penalty_charges = TRUE
     AND (((vc.rental_start IS NULL) AND (bpb.start_date <= i.date_invoiced)) OR ((vc.rental_start IS NOT NULL) AND (bpb.start_date <= vc.rental_start)))
     AND i.penalty_band > -1
     AND (current_date - i.auto_penalty_start::DATE) >= i.penalty_band
     AND ((i.penalty_band = 90 and (bpb.hire_apply_90_day_rate=true or bpb.repair_apply_90_day_rate=true) and hire_use_commercial=false and repair_use_commercial=false) or  i.penalty_band != 90)
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
currentPenaltyBand = CASE WHEN penaltyAge >= 30 and penaltyAge < 60 then 30 ELSE
                    CASE WHEN penaltyAge >= 60 and penaltyAge < 90 then 60 ELSE 90 END END;
nextPenaltyBand = CASE WHEN penaltyAge >= 30 and penaltyAge < 60 then 60 ELSE
                    CASE WHEN penaltyAge >= 60 and penaltyAge < 90 then 90 ELSE -1 END END;
hireStartDate = CASE WHEN claimRecord.rental_start is not null then claimRecord.rental_start else claimRecord.date_invoiced END;
hirepenalPerVal = CASE WHEN currentPenaltyBand = 30 then claimRecord.hire_30_day/100.0 ELSE
                    CASE WHEN currentPenaltyBand = 60 then claimRecord.hire_60_day/100.0 ELSE
                        CASE WHEN currentPenaltyBand = 90 then claimRecord.hire_90_day/100.0 ELSE 0.0 END END END;
repairpenalPerVal = CASE WHEN currentPenaltyBand = 30 then claimRecord.repair_30_day/100.0 ELSE
                    CASE WHEN currentPenaltyBand = 60 then claimRecord.repair_60_day/100.0 ELSE
                        CASE WHEN currentPenaltyBand = 90 then claimRecord.repair_90_day/100.0 ELSE 0.0 END END END;

RAISE NOTICE 'invoice is % days > % days : choRef %    hireStartDate=%    hirepenalPer=%    repairpenalPer=%', penaltyAge, currentPenaltyBand, claimRecord.cho_reference, hireStartDate, hirepenalPerVal*100.0::numeric(4,1), repairpenalPerVal*100.0::numeric(4,1);


    UPDATE
      invoice
    SET
      penalty_band = nextPenaltyBand,
      hire_penalty_charge_applied_date = (CASE WHEN(hire_net > 0) THEN now() ELSE null END),
      repair_penalty_charge_applied_date = (CASE WHEN(repair_net > 0) THEN now() ELSE null END),
      full_total_to_pay = (full_total_to_pay - (hire_penalty_charge + repair_penalty_charge) + (hire_gross * hirepenalPerVal) + (repair_gross * repairpenalPerVal))::NUMERIC(8,2),
      total_to_pay = (CASE WHEN ((claimRecord.liability_status = 5 OR claimRecord.liability_status = 6) AND claimRecord.claim_type NOT IN (7,8,9,11,12,13,18,19,20))
                           THEN ((claimRecord.percentage_liability_accepted/100) * (full_total_to_pay - (hire_penalty_charge + repair_penalty_charge) + (hire_gross * hirepenalPerVal) + (repair_gross * repairpenalPerVal)))::NUMERIC(8,2)
                      WHEN ((claimRecord.liability_status = 4) AND claimRecord.claim_type NOT IN (7,8,9,11,12,13,18,19,20))
                           THEN (0.00)
                      ELSE (full_total_to_pay - (hire_penalty_charge + repair_penalty_charge) + (hire_gross * hirepenalPerVal) + (repair_gross * repairpenalPerVal))::NUMERIC(8,2)
                           END),
      hire_penalty_charge = (hire_gross * hirepenalPerVal)::NUMERIC(8,2),
      repair_penalty_charge = (repair_gross * repairpenalPerVal)::NUMERIC(8,2),
      hire_penalty_percentage = (CASE WHEN(hire_net > 0) THEN hirepenalPerVal*100::numeric(4,1) || '%' ELSE null END),
      repair_penalty_percentage = (CASE WHEN(repair_net > 0) THEN repairpenalPerVal*100::numeric(4,1) || '%' ELSE null END),
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
                                      || ' have been applied to the invoice as the age of the invoice has exceeded ' || currentPenaltyBand || ' days.',
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

END LOOP;

RETURN TRUE;

END;
$BODY$
  LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION applyAutoPenaltyCharge(integer, integer) TO chox_user;

----------------------
-- End of 8.7.1
----------------------


--------------------------------------------------------------------------------
-- 8.7.2 TL Bordereau Task Creation - CHOX Automation
--------------------------------------------------------------------------------
insert into scheduler_job (login_username, login_password, job_name, email_subject, autherised_user, bcc_receiver,
                           error_message_receiver, created_by, created_date, last_modified_by, last_modified_date, version)
       select 'erac_scheduler', 'Ch0xAdm1n1', 'TL_TASK', 'IMS TL Payment Task',
       'chox@imsolutionslimited.co.uk,totallossteam@imsolutionslimited.co.uk,totalloss@imsolutionslimited.co.uk,elliot.roberts@valexa.com,ben.richmond@valexa.com',
       'john.dowson@valexa.com',
       'john.dowson@valexa.com', 999, now(), 999, now(), 0;

-- For test
--insert into scheduler_job (login_username, login_password, job_name, email_subject, autherised_user, bcc_receiver,
--                           error_message_receiver, created_by, created_date, last_modified_by, last_modified_date, version)
--       select 'erac_scheduler', 'C0mpliance', 'TL_TASK', 'IMS TL Payment Task',
--       'john.dowson@valexa.com,elliot.roberts@valexa.com,ben.richmond@valexa.com,bula.raghavan@valexa.com,robert.hon@valexa.com',
--       'john.dowson@valexa.com',
--       'john.dowson@valexa.com', 999, now(), 999, now(), 0;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimUnacknowledgedUnrouted', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimUnacknowledgedRouted', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimUnacknowledgedUnassigned', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimReferredToFNOL', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimReferredToEngineer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimUpdatedByEngineer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.AwaitingCarHireInfo', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.AwaitingInvoiceData', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimRejectionContested', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimRejected', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.SubscriberClaimRejected', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimPending', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.AwaitingLitigationOutcome', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.InvoiceDataCalculationIncorrect', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ContestedInvoiceReferredToCHO', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.AwaitingInvoicePayment', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.InvoiceReferredToEngineer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ContestedInvoiceReferredToInsurer', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.AwaitingLiabilityResolution', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.InvoiceApprovedByBRE', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.InvoiceReferredToClaimsHandler', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.InvoiceEscalated', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.InvoiceEscalatedToHandler', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.InvoicePaymentLogged', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.PaymentReceived', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimClosed', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.ClaimRejectionAccepted', false, false;
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
    SELECT 'activity.TlTaskCreation.InvoiceRejectionAccepted', false, false;


INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHOX_ADMIN', 1
    FROM accessibility
    WHERE name like 'activity.TlTaskCreation.%';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHO_MNG', 1
    FROM accessibility
    WHERE name like 'activity.TlTaskCreation.%';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
    SELECT id, 'ROLE_CHO_OPR', 1
    FROM accessibility
    WHERE name like 'activity.TlTaskCreation.%';

----------------------
-- End of 8.7.2
----------------------

--------------------------------------------------------------------------------
-- 8.7.3 Additional Repair Invoice Fields
--------------------------------------------------------------------------------

ALTER TABLE invoice ADD COLUMN repair_parts numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice ADD COLUMN repair_labour numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice ADD COLUMN repair_materials numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice ADD COLUMN repair_specialist numeric(10, 2) NOT NULL DEFAULT 0.00;

ALTER TABLE invoice_original ADD COLUMN repair_parts numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice_original ADD COLUMN repair_labour numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice_original ADD COLUMN repair_materials numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice_original ADD COLUMN repair_specialist numeric(10, 2) NOT NULL DEFAULT 0.00;

----------------------
-- End of 8.7.3
----------------------


--------------------------------------------------------------------------------
-- 8.7.4 Automated On Hire Task
--------------------------------------------------------------------------------

ALTER TABLE bre_band ADD COLUMN allow_on_hire_auto_tasks boolean not null DEFAULT false;

----------------------
-- End of 8.7.4
----------------------

--------------------------------------------------------------------------------
-- bug#3009 Production - InvoicePaymentLogged activity should not be accessible
--                       for Manual claims
--------------------------------------------------------------------------------
update accessibility set claim_type=0 where name='activity.InvoicePaymentLogged.AwaitingInvoicePayment';
insert into accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
  select 'activity.InvoicePaymentLogged.AwaitingInvoicePayment', true, true, 3;
insert into accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
  select 'activity.InvoicePaymentLogged.AwaitingInvoicePayment', true, true, 4;
insert into accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
  select 'activity.InvoicePaymentLogged.AwaitingInvoicePayment', true, true, 7;
insert into accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
  select 'activity.InvoicePaymentLogged.AwaitingInvoicePayment', true, true, 11;
insert into accessibility(name, is_workgroup_check, is_ownership_check, claim_type)
  select 'activity.InvoicePaymentLogged.AwaitingInvoicePayment', true, true, 18;

insert into accessibility_item(accessibility_id, role, access_right)
  select id, 'ROLE_INS_CH', 2 from accessibility where name='activity.InvoicePaymentLogged.AwaitingInvoicePayment' and claim_type != 0;
insert into accessibility_item(accessibility_id, role, access_right)
  select id, 'ROLE_INS_MNG', 2 from accessibility where name='activity.InvoicePaymentLogged.AwaitingInvoicePayment' and claim_type != 0;
insert into accessibility_item(accessibility_id, role, access_right)
  select id, 'ROLE_CHOX_ADMIN', 2 from accessibility where name='activity.InvoicePaymentLogged.AwaitingInvoicePayment' and claim_type != 0;
insert into accessibility_item(accessibility_id, role, access_right)
  select id, 'ROLE_INS_PC', 2 from accessibility where name='activity.InvoicePaymentLogged.AwaitingInvoicePayment' and claim_type != 0;

----------------------
-- End of bug#3009
----------------------

