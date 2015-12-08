--------------------------------------------------------------------------------
-- 8.8.1 SLA Days Remaining On Claim Grid
--------------------------------------------------------------------------------
ALTER TABLE claim ADD COLUMN remaining_sla_days_str character varying(5);
ALTER TABLE claim ADD COLUMN remaining_sla_days int;

drop function get_days_in_status(IN claimId INT, IN statuses CHARACTER VARYING(40)[]);

create or replace function get_days_in_status(IN claimId INT, IN statuses CHARACTER VARYING(40)[], IN ignoreBankHolidays BOOLEAN)
RETURNS INT AS
$BODY$
DECLARE
    days INT;
    lastDayCounted INT;
    dayInstatus INT;
    noBankHolidays INT;
    dayOutstatus INT;
    previousStatus CHARACTER VARYING(40);
    statusStart timestamp without time zone;
    auditTrailRecord record;
BEGIN
    days = 0;
    lastDayCounted = 0;
    previousStatus = '';
    statusStart = null;

    FOR auditTrailRecord  IN
        select * from get_reconstructed_audit_trail(claimId) order by update_date, id
    LOOP
        IF (statusStart is null and statuses @> ARRAY[auditTrailRecord.new_status]) THEN
            statusStart = auditTrailRecord.update_date;
--            RAISE NOTICE 'Status % start at %', auditTrailRecord.new_status, auditTrailRecord.update_date;
        ELSEIF (statusStart is not null and not statuses @> ARRAY[auditTrailRecord.new_status]) THEN
            -- Determine time in status
            dayInStatus = date_part('doy', statusStart);
            dayOutStatus = date_part('doy', auditTrailRecord.update_date);
            IF (not (lastDayCounted = dayInStatus and dayInStatus = dayOutStatus)) THEN
                days = days + (auditTrailRecord.update_date::date - statusStart::date) + 1;
--                RAISE NOTICE 'In status for %', date_part('doy', auditTrailRecord.update_date) - dayInStatus + 1;
                IF (ignoreBankHolidays) THEN
                    noBankHolidays = (select count(*) from bank_holidays bh where bh.bank_holiday::Date >= statusStart::date and bh.bank_holiday::Date <= auditTrailRecord.update_date::date);
--                    RAISE NOTICE 'Subtracting % bank holiday days', noBankHolidays;
                    days = days - noBankHolidays;
                END IF;
            END IF;
            lastDayCounted = dayOutStatus;
            statusStart = null;
--        ELSE
--            RAISE NOTICE 'Nothing to do for status % (statusStart=%)', auditTrailRecord.new_status,statusStart;
        END IF;
--        RAISE NOTICE 'Total Days: %', days;
    END LOOP;

    IF (statusStart is not null) THEN
        -- We must currently be in the status, so count days until now()
        days = days + (now()::date - statusStart::date);
        IF (ignoreBankHolidays) THEN
            noBankHolidays = (select count(*) from bank_holidays bh where bh.bank_holiday::Date >= statusStart::date and bh.bank_holiday::Date <= now()::date);
--          RAISE NOTICE 'Subtracting % bank holiday days', noBankHolidays;
            days = days - noBankHolidays;
        END IF;
        dayInStatus = date_part('doy', statusStart);
--        RAISE NOTICE 'dayInStatus=%, statusStart=%, days added=%', dayInStatus, statusStart, (now()::date - statusStart::date);
        IF (lastDayCounted != dayInStatus) THEN
            days = days +  1;
--            RAISE NOTICE '1 day added';
        END IF;
    END IF;

    RETURN days;
END;
$BODY$
LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION get_days_in_status(IN claimId INT, IN statuses CHARACTER VARYING(40)[], IN ignoreBankHolidays BOOLEAN) TO chox_user;
GRANT EXECUTE ON FUNCTION get_days_in_status(IN claimId INT, IN statuses CHARACTER VARYING(40)[], IN ignoreBankHolidays BOOLEAN) TO chox_mi;


CREATE OR REPLACE FUNCTION updateRemainingSlaDays()
  RETURNS boolean AS
$BODY$

BEGIN

-- First, set all to null
update claim
  set remaining_sla_days = null, remaining_sla_days_str = null
where remaining_sla_days is not null;

update claim
  set remaining_sla_days = sla_ext_days + bre.subscriber_sla_days - get_days_in_status(claim.id, '{"ClaimUnacknowledgedUnassigned","ClaimUnacknowledgedUnrouted","ClaimUnacknowledgedRouted",
                    "ClaimPending","ClaimReferredToFNOL","ClaimReferredToEngineer","ClaimUpdatedByEngineer","ClaimRejectionContested"}', bre.pause_subscriber_sla_clock)
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = claim.insurer_id
  AND bre.subscriber_sla_days != 0
  AND claim.claim_type IN (7,8,9)
  AND claim.status in ('ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted',
        'ClaimPending', 'ClaimReferredToFNOL', 'ClaimReferredToEngineer', 'ClaimUpdatedByEngineer',
        'ClaimRejectionContested', 'ClaimRejected', 'SubscriberClaimRejected');

update claim
  set remaining_sla_days = sla_ext_days + bre.fixedfee_sla_days - get_days_in_status(claim.id, '{"ClaimUnacknowledgedUnassigned","ClaimUnacknowledgedUnrouted","ClaimUnacknowledgedRouted",
                    "ClaimPending","ClaimReferredToFNOL","ClaimReferredToEngineer","ClaimUpdatedByEngineer","ClaimRejectionContested"}', bre.pause_fixedfee_sla_clock)
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.fixedfee_sla_days != 0
  AND bre.insurer_id = claim.insurer_id
  AND claim.claim_type IN (11,12,13)
  AND claim.status in ('ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted',
        'ClaimPending', 'ClaimReferredToFNOL', 'ClaimReferredToEngineer', 'ClaimUpdatedByEngineer',
        'ClaimRejectionContested', 'ClaimRejected');

update claim
  set remaining_sla_days_str = remaining_sla_days::varchar(5)
where remaining_sla_days is not null;

update claim
  set remaining_sla_days_str = '0'
where remaining_sla_days < 0;

update claim
  set remaining_sla_days_str = '-'
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = claim.insurer_id
  AND bre.subscriber_sla_days = 0
  AND claim.claim_type IN (7,8,9)
  AND claim.status in ('ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted',
        'ClaimPending', 'ClaimReferredToFNOL', 'ClaimReferredToEngineer', 'ClaimUpdatedByEngineer',
        'ClaimRejectionContested', 'ClaimRejected', 'SubscriberClaimRejected');

update claim
  set remaining_sla_days_str = '-'
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.fixedfee_sla_days = 0
  AND bre.insurer_id = claim.insurer_id
  AND claim.claim_type IN (11,12,13)
  AND claim.status in ('ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted',
        'ClaimPending', 'ClaimReferredToFNOL', 'ClaimReferredToEngineer', 'ClaimUpdatedByEngineer',
        'ClaimRejectionContested', 'ClaimRejected');

update claim
  set remaining_sla_days_str = bre.subscriber_time_cut_off
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = claim.insurer_id
  AND claim.claim_type IN (7,8,9)
  AND remaining_sla_days = 0;

update claim
  set remaining_sla_days_str = bre.fixedfee_time_cut_off
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = claim.insurer_id
  AND claim.claim_type IN (11,12,13)
  AND remaining_sla_days = 0;

return true;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION updateRemainingSlaDays() TO chox_user;


create or replace function remaining_sla_days_report(IN insurerids integer[])
returns table
(
   "Supplier Reference" character varying(128),
   "Insurer Claim Number" character varying(128),
   "Current Status" character varying(128),
   "Workgroup" character varying(128),
   "Insurer Claim Owner" text,
   "CHO Name" character varying(128),
   "Claim Type" text,
   "Insurer Name" character varying(128),
   "SLA Days Remaining" character varying(5)
)
as $BODY$

BEGIN

RETURN QUERY

SELECT c.cho_reference as "Supplier Reference",
       c.claim_number as "Insurer Claim Number",
       c.status as "Current Status",
       w.name as "Workgroup",
       wu.first_name || ' ' || wu.last_name AS "Insurer Claim Owner",
       ch.name as "CHO Name",
       (CASE WHEN c.claim_type IN (7,8,9) THEN 'Subscriber'
             ELSE 'Fixed Fee' END) as "Claim Type",
       ins.name as "Insurer Name",
       c.remaining_sla_days_str as "SLA Days Remaining"
FROM claim c LEFT OUTER JOIN workgroup w ON c.workgroup_id = w.id,
     web_user wu,
     chorganisation ch,
     insurer ins,
     bre_band bre,
     bre_band_organisation bbo
WHERE (insurerids is null or ins.id = ANY(insurerids)) -- restricted to insurers
  AND c.chorganisation_id = ch.id
  AND c.insurer_id = ins.id
  AND c.claim_owner_id = wu.id
  AND bbo.chorganisation_id = ch.id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = ins.id
  AND c.status in ('ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted', 'ClaimPending', 'ClaimReferredToEngineer',
                    'ClaimUpdatedByEngineer', 'ClaimReferredToFNOL', 'SubscriberClaimRejected', 'ClaimRejected',
                    'ClaimRejectionContested', 'ClaimUnacknowledgedUnassigned')
  AND c.claim_type in (7,8,9,11,12,13)
  AND c.remaining_sla_days_str is not null
--  AND NOT EXISTS (select * from comment co where co.claim_id=c.id and co.comment ilike '%failed to respond to the % notification within the % day SLA%' and co.reverted = false)
ORDER BY "SLA Days Remaining"
;

END;
$BODY$
LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION remaining_sla_days_report(IN insurerids integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION remaining_sla_days_report(IN insurerids integer[]) TO chox_mi;

drop function remaining_sla_days(IN insurerids integer[]);

----------------------
-- End of 8.8.1
----------------------


--------------------------------------------------------------------------------
-- 8.8.2 Automatic Routing CHO Assignment
--------------------------------------------------------------------------------
ALTER TABLE insurer ADD COLUMN automatic_routing_strategy integer NOT NULL default 0;
ALTER TABLE insurer ADD COLUMN complete_routing boolean NOT NULL default false;

update insurer set automatic_routing_strategy = 1 where is_auto_routing_enable = true;
update insurer set automatic_routing_strategy = 2 where is_auto_routing_enable_price = true;

ALTER TABLE insurer DROP COLUMN is_auto_routing_enable;
ALTER TABLE insurer DROP COLUMN is_auto_routing_enable_price;

CREATE TABLE auto_routing_cho_workgroup_assignment (
  id serial NOT NULL,
  version integer,
  workgroup_id integer NOT NULL,
  chorganisation_id integer NOT NULL,
  created_by integer,
  created_date timestamp without time zone NOT NULL default now(),
  last_modified_by integer,
  last_modified_date timestamp without time zone NOT NULL default now(),
  CONSTRAINT auto_routing_cho_workgroup_assignment_pkey PRIMARY KEY (id),
  CONSTRAINT auto_routing_cho_workgroup_assignment_ukey UNIQUE (chorganisation_id, workgroup_id),
  CONSTRAINT auto_routing_cho_workgroup_assignment_wfkey FOREIGN KEY (workgroup_id)
      REFERENCES workgroup (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT auto_routing_cho_workgroup_assignment_cfkey FOREIGN KEY (chorganisation_id)
      REFERENCES chorganisation (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT created_by_fkey FOREIGN KEY (created_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT last_modified_by_fkey FOREIGN KEY (last_modified_by)
      REFERENCES web_user (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE auto_routing_cho_workgroup_assignment TO chox_user;
GRANT SELECT ON TABLE auto_routing_cho_workgroup_assignment TO chox_mi;
GRANT SELECT, UPDATE ON TABLE auto_routing_cho_workgroup_assignment_id_seq TO chox_user;

----------------------
-- End of 8.8.2
----------------------


--------------------------------------------------------------------------------
-- 8.8.3 New Queue - Payment Disputes
--------------------------------------------------------------------------------
ALTER TABLE insurer ADD COLUMN is_payment_disputes_enable boolean NOT NULL DEFAULT false;
ALTER TABLE claim ADD COLUMN is_payment_dispute boolean NOT NULL DEFAULT false;

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'filter.InvoicePaymentDispute', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_CH' FROM accessibility WHERE name='filter.InvoicePaymentDispute';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_PC' FROM accessibility WHERE name='filter.InvoicePaymentDispute';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_MNG' FROM accessibility WHERE name='filter.InvoicePaymentDispute';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_MI' FROM accessibility WHERE name='filter.InvoicePaymentDispute';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHOX_ADMIN' FROM accessibility WHERE name='filter.InvoicePaymentDispute';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'filter.PaymentTeamDispute', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_PC' FROM accessibility WHERE name='filter.PaymentTeamDispute';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_MNG' FROM accessibility WHERE name='filter.PaymentTeamDispute';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_INS_MI' FROM accessibility WHERE name='filter.PaymentTeamDispute';
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ROLE_CHOX_ADMIN' FROM accessibility WHERE name='filter.PaymentTeamDispute';


----------------------
-- End of 8.8.3
----------------------

--------------------------------------------------------------------------------
-- 8.8.4 Breakout Discount By Claim Type
--------------------------------------------------------------------------------
ALTER TABLE insurer_discount ADD COLUMN claim_type INTEGER NOT NULL DEFAULT 0;
----------------------
-- End of 8.8.4
----------------------

--------------------------------------------------------------------------------
-- 8.8.5 Subscriber Bank Holiday SLA Clock
--------------------------------------------------------------------------------
ALTER TABLE bre_band ADD COLUMN pause_subscriber_sla_clock boolean NOT NULL DEFAULT false;
ALTER TABLE bre_band ADD COLUMN pause_fixedfee_sla_clock boolean NOT NULL DEFAULT false;
create table bank_holidays (
  id serial NOT NULL,
  bank_holiday timestamp without time zone NOT NULL,
  CONSTRAINT bank_holidays_pkey PRIMARY KEY (id),
  CONSTRAINT bank_holidays_ukey UNIQUE (bank_holiday)
);
GRANT SELECT ON TABLE bank_holidays TO chox_user;
GRANT SELECT ON TABLE bank_holidays TO chox_mi;

insert into bank_holidays(bank_holiday) select '2015-01-01';
insert into bank_holidays(bank_holiday) select '2015-04-03';
insert into bank_holidays(bank_holiday) select '2015-04-06';
insert into bank_holidays(bank_holiday) select '2015-05-04';
insert into bank_holidays(bank_holiday) select '2015-05-25';
insert into bank_holidays(bank_holiday) select '2015-08-31';
insert into bank_holidays(bank_holiday) select '2015-12-25';
insert into bank_holidays(bank_holiday) select '2015-12-28';

insert into bank_holidays(bank_holiday) select '2016-01-01';
insert into bank_holidays(bank_holiday) select '2016-03-25';
insert into bank_holidays(bank_holiday) select '2016-03-28';
insert into bank_holidays(bank_holiday) select '2016-05-02';
insert into bank_holidays(bank_holiday) select '2016-05-30';
insert into bank_holidays(bank_holiday) select '2016-08-29';
insert into bank_holidays(bank_holiday) select '2016-12-26';
insert into bank_holidays(bank_holiday) select '2016-12-27';

insert into bank_holidays(bank_holiday) select '2017-01-02';
insert into bank_holidays(bank_holiday) select '2017-04-14';
insert into bank_holidays(bank_holiday) select '2017-04-17';
insert into bank_holidays(bank_holiday) select '2017-05-01';
insert into bank_holidays(bank_holiday) select '2017-05-29';
insert into bank_holidays(bank_holiday) select '2017-08-28';
insert into bank_holidays(bank_holiday) select '2017-12-25';
insert into bank_holidays(bank_holiday) select '2017-12-26';

----------------------
-- End of 8.8.5
----------------------

--------------------------------------------------------------------------------
-- bug#3027 - Production - 'History' tab should be accessible in claim status
-- ManualInvoiceUnassigned
--------------------------------------------------------------------------------
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_manual_inv_workgroup_enabled, check_manual_inv_claimownership_enabled)
    SELECT 'tab.History.ManualInvoiceUnassigned', false, false, false, false;
INSERT INTO accessibility_item(accessibility_id, access_right, role)
    SELECT id, 1, 'ALL' FROM accessibility WHERE name='tab.History.ManualInvoiceUnassigned';

----------------------
-- End of bug#3027
----------------------

--------------------------------------------------------------------------------
-- Minor correction to applyAutoPenaltyCharge() function:
--    Invorrectr condition for 90 day penalty record selection, following change applied:
-- 58c58
-- <      AND ((i.penalty_band = 90 and (bpb.hire_apply_90_day_rate=true or bpb.repair_apply_90_day_rate=true) and hire_use_commercial=false and repair_use_commercial=false) or  i.penalty_band != 90)
---
-- >      AND ((i.penalty_band = 90 and ((bpb.hire_apply_90_day_rate=true and bpb.hire_use_commercial=false) or (bpb.repair_apply_90_day_rate=true and bpb.repair_use_commercial=false))) or  i.penalty_band != 90)
--------------------------------------------------------------------------------
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
     AND ((i.penalty_band = 90 and ((bpb.hire_apply_90_day_rate=true and bpb.hire_use_commercial=false) or (bpb.repair_apply_90_day_rate=true and bpb.repair_use_commercial=false))) or i.penalty_band != 90)
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
