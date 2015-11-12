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
