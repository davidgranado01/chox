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
    hire_30_day numeric(6,2) NOT NULL,
    hire_60_day numeric(6,2) NOT NULL,
    hire_90_day numeric(6,2) NOT NULL,
    hire_apply_90_day_rate boolean not null,
    hire_use_commercial boolean not null,
    repair_30_day numeric(6,2) NOT NULL,
    repair_60_day numeric(6,2) NOT NULL,
    repair_90_day numeric(6,2) NOT NULL,
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
select 0, id, '1950-01-01', 0, 7.50, 15.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
from bre_band;

INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '2012-06-15', 0, 12.50, 20.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
from bre_band;

-- Subscriber Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 7, 4.00, 8.00, 12.00, true, false, 0.00, 0.00, 0.00, true, false, 999, now(), 999, now()
from bre_band;

-- Fixed-Fee Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 11, 5.00, 10.00, 15.00, true, false, 5.00, 10.00, 15.00, true, false, 999, now(), 999, now()
from bre_band;

-- Collaboration Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 18, 7.50, 15.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
from bre_band;
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '2012-06-15', 18, 12.50, 20.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
from bre_band;

-- Insurer Vs Insurer Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 4, 7.50, 15.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
from bre_band;
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '2012-06-15', 4, 12.50, 20.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
from bre_band;

-- TPI Rates
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '1950-01-01', 3, 7.50, 15.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
from bre_band;
INSERT INTO bre_penalty_band(version, bre_band_id, start_date, claim_type, hire_30_day, hire_60_day, hire_90_day, hire_apply_90_day_rate,
                             hire_use_commercial, repair_30_day, repair_60_day, repair_90_day, repair_apply_90_day_rate,
                             repair_use_commercial, created_by, created_date, last_modified_by, last_modified_date)
select 0, id, '2012-06-15', 3, 12.50, 20.00, 0.00, true, true, 2.50, 5.00, 0.00, false, false, 999, now(), 999, now()
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

----------------------
-- End of 8.7.1
----------------------
