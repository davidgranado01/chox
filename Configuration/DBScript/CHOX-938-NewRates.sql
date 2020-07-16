
-- CHOX-938 (CHOX): adding new set of ERAC special rates for LV
-- version is set to 0, which is an arbitrary new value
-- age is set up to 99.00 arbitrarily
-- created_by and last_modified_by are set up to 999
-- created_date and last_modified_date are set up to now
-- Expected organisations ids:
--    erac (cho) id  = 1007
--    lv id = 26

-- Number of new entries expected for lv : 227

DO $$
DECLARE var_ver integer := 0;
DECLARE var_erac integer;
DECLARE var_date_lv date := '01/06/2020';
DECLARE var_lv integer;

DECLARE var_CP1 integer;
DECLARE var_CP1A integer;
DECLARE var_CP1EST integer;
DECLARE var_CP1ESTA integer;
DECLARE var_CP2 integer;
DECLARE var_CP2A integer;
DECLARE var_CP2EST integer;
DECLARE var_CP2ESTA integer;
DECLARE var_CP3 integer;
DECLARE var_CP3A integer;
DECLARE var_CP3EST integer;
DECLARE var_CP3ESTA integer;
DECLARE var_S1 integer;
DECLARE var_S1A integer;
DECLARE var_S2 integer;
DECLARE var_S2A integer;
DECLARE var_S3 integer;
DECLARE var_S3A integer;
DECLARE var_S3EST integer;
DECLARE var_S3ESTA integer;
DECLARE var_P1ESTA integer;
DECLARE var_P2ESTA integer;
DECLARE var_P3ESTA integer;
DECLARE var_S7ESTA integer;
DECLARE var_SP1ESTA integer;
DECLARE var_SP2ESTA integer;
DECLARE var_P1EST integer;
DECLARE var_P2EST integer;
DECLARE var_P3EST integer;
DECLARE var_S7EST integer;
DECLARE var_SP1EST integer;
DECLARE var_SP2EST integer;
DECLARE var_P1 integer;
DECLARE var_P2 integer;
DECLARE var_P3 integer;
DECLARE var_S7 integer;
DECLARE var_SP1 integer;
DECLARE var_SP2 integer;
DECLARE var_P1A integer;
DECLARE var_P2A integer;
DECLARE var_P3A integer;
DECLARE var_S7A integer;
DECLARE var_SP1A integer;
DECLARE var_SP2A integer;
DECLARE var_S4 integer;
DECLARE var_S4A integer;
DECLARE var_S4EST integer;
DECLARE var_S4ESTA integer;
DECLARE var_S5 integer;
DECLARE var_F6 integer;
DECLARE var_F7 integer;
DECLARE var_F8 integer;
DECLARE var_F9 integer;
DECLARE var_F6A integer;
DECLARE var_F7A integer;
DECLARE var_F8A integer;
DECLARE var_F9A integer;
DECLARE var_F6EST integer;
DECLARE var_F7EST integer;
DECLARE var_F8EST integer;
DECLARE var_F9EST integer;
DECLARE var_F6ESTA integer;
DECLARE var_F7ESTA integer;
DECLARE var_F8ESTA integer;
DECLARE var_F9ESTA integer;
DECLARE var_S5A integer;
DECLARE var_S5EST integer;
DECLARE var_S5ESTA integer;
DECLARE var_P10EST integer;
DECLARE var_P11EST integer;
DECLARE var_P12EST integer;
DECLARE var_P13EST integer;
DECLARE var_P8EST integer;
DECLARE var_P9EST integer;
DECLARE var_SP10EST integer;
DECLARE var_SP11EST integer;
DECLARE var_SP12EST integer;
DECLARE var_SP13EST integer;
DECLARE var_SP7EST integer;
DECLARE var_SP8EST integer;
DECLARE var_SP9EST integer;
DECLARE var_P10ESTA integer;
DECLARE var_P11ESTA integer;
DECLARE var_P12ESTA integer;
DECLARE var_P13ESTA integer;
DECLARE var_P8ESTA integer;
DECLARE var_P9ESTA integer;
DECLARE var_SP10ESTA integer;
DECLARE var_SP11ESTA integer;
DECLARE var_SP12ESTA integer;
DECLARE var_SP13ESTA integer;
DECLARE var_SP7ESTA integer;
DECLARE var_SP8ESTA integer;
DECLARE var_SP9ESTA integer;
DECLARE var_P10 integer;
DECLARE var_P11 integer;
DECLARE var_P12 integer;
DECLARE var_P13 integer;
DECLARE var_P8 integer;
DECLARE var_P9 integer;
DECLARE var_SP10 integer;
DECLARE var_SP11 integer;
DECLARE var_SP12 integer;
DECLARE var_SP13 integer;
DECLARE var_SP7 integer;
DECLARE var_SP8 integer;
DECLARE var_SP9 integer;
DECLARE var_P10A integer;
DECLARE var_P11A integer;
DECLARE var_P12A integer;
DECLARE var_P13A integer;
DECLARE var_P8A integer;
DECLARE var_P9A integer;
DECLARE var_SP10A integer;
DECLARE var_SP11A integer;
DECLARE var_SP12A integer;
DECLARE var_SP13A integer;
DECLARE var_SP7A integer;
DECLARE var_SP8A integer;
DECLARE var_SP9A integer;
DECLARE var_S6 integer;
DECLARE var_S6A integer;
DECLARE var_S6EST integer;
DECLARE var_S6ESTA integer;
DECLARE var_F3 integer;
DECLARE var_F4 integer;
DECLARE var_F5 integer;
DECLARE var_F3A integer;
DECLARE var_F4A integer;
DECLARE var_F5A integer;
DECLARE var_F3EST integer;
DECLARE var_F4EST integer;
DECLARE var_F5EST integer;
DECLARE var_F3ESTA integer;
DECLARE var_F4ESTA integer;
DECLARE var_F5ESTA integer;
DECLARE var_M integer;
DECLARE var_M1 integer;
DECLARE var_M2 integer;
DECLARE var_M1A integer;
DECLARE var_M2A integer;
DECLARE var_MA integer;
DECLARE var_M1EST integer;
DECLARE var_M2EST integer;
DECLARE var_MEST integer;
DECLARE var_M1ESTA integer;
DECLARE var_M2ESTA integer;
DECLARE var_MESTA integer;
DECLARE var_P6EST integer;
DECLARE var_P7EST integer;
DECLARE var_SP5EST integer;
DECLARE var_SP6EST integer;
DECLARE var_P6ESTA integer;
DECLARE var_P7ESTA integer;
DECLARE var_SP5ESTA integer;
DECLARE var_SP6ESTA integer;
DECLARE var_P6 integer;
DECLARE var_P7 integer;
DECLARE var_SP5 integer;
DECLARE var_SP6 integer;
DECLARE var_P6A integer;
DECLARE var_P7A integer;
DECLARE var_SP5A integer;
DECLARE var_SP6A integer;
DECLARE var_M3 integer;
DECLARE var_M4 integer;
DECLARE var_M5 integer;
DECLARE var_M6 integer;
DECLARE var_M3A integer;
DECLARE var_M4A integer;
DECLARE var_M5A integer;
DECLARE var_M6A integer;
DECLARE var_M3EST integer;
DECLARE var_M4EST integer;
DECLARE var_M5EST integer;
DECLARE var_M6EST integer;
DECLARE var_M3ESTA integer;
DECLARE var_M4ESTA integer;
DECLARE var_M5ESTA integer;
DECLARE var_M6ESTA integer;
DECLARE var_F1 integer;
DECLARE var_F2 integer;
DECLARE var_F1A integer;
DECLARE var_F2A integer;
DECLARE var_F1EST integer;
DECLARE var_F2EST integer;
DECLARE var_F1ESTA integer;
DECLARE var_F2ESTA integer;
DECLARE var_P4EST integer;
DECLARE var_P5EST integer;
DECLARE var_SP3EST integer;
DECLARE var_SP4EST integer;
DECLARE var_P4ESTA integer;
DECLARE var_P5ESTA integer;
DECLARE var_SP3ESTA integer;
DECLARE var_SP4ESTA integer;
DECLARE var_P4 integer;
DECLARE var_P5 integer;
DECLARE var_SP3 integer;
DECLARE var_SP4 integer;
DECLARE var_P4A integer;
DECLARE var_P5A integer;
DECLARE var_SP3A integer;
DECLARE var_SP4A integer;
DECLARE var_PV1 integer;
DECLARE var_PV1A integer;
DECLARE var_PV1EST integer;
DECLARE var_PV1ESTA integer;
DECLARE var_PV2 integer;
DECLARE var_PV2A integer;
DECLARE var_PV3A integer;
DECLARE var_PV2EST integer;
DECLARE var_PV3EST integer;
DECLARE var_PV2ESTA integer;
DECLARE var_PV3ESTA integer;
DECLARE var_PV3 integer;
DECLARE var_PV4 integer;
DECLARE var_PV4A integer;
DECLARE var_PV4EST integer;
DECLARE var_PV4ESTA integer;
DECLARE var_PV5 integer;
DECLARE var_PV6 integer;
DECLARE var_PV5A integer;
DECLARE var_PV6A integer;
DECLARE var_PV5EST integer;
DECLARE var_PV6EST integer;
DECLARE var_PV5ESTA integer;
DECLARE var_PV6ESTA integer;

BEGIN
SELECT id from chorganisation where name = 'Enterprise Rent-A-Car UK Ltd' INTO var_erac;

SELECT id from insurer where name like 'LV=%' INTO var_lv;
SELECT id from vehicle_class where name = 'CP1' INTO var_CP1;
SELECT id from vehicle_class where name = 'CP1A' INTO var_CP1A;
SELECT id from vehicle_class where name = 'CP1EST' INTO var_CP1EST;
SELECT id from vehicle_class where name = 'CP1ESTA' INTO var_CP1ESTA;
SELECT id from vehicle_class where name = 'CP2' INTO var_CP2;
SELECT id from vehicle_class where name = 'CP2A' INTO var_CP2A;
SELECT id from vehicle_class where name = 'CP2EST' INTO var_CP2EST;
SELECT id from vehicle_class where name = 'CP2ESTA' INTO var_CP2ESTA;
SELECT id from vehicle_class where name = 'CP3' INTO var_CP3;
SELECT id from vehicle_class where name = 'CP3A' INTO var_CP3A;
SELECT id from vehicle_class where name = 'CP3EST' INTO var_CP3EST;
SELECT id from vehicle_class where name = 'CP3ESTA' INTO var_CP3ESTA;
SELECT id from vehicle_class where name = 'S1' INTO var_S1;
SELECT id from vehicle_class where name = 'S1A' INTO var_S1A;
SELECT id from vehicle_class where name = 'S2' INTO var_S2;
SELECT id from vehicle_class where name = 'S2A' INTO var_S2A;
SELECT id from vehicle_class where name = 'S3' INTO var_S3;
SELECT id from vehicle_class where name = 'S3A' INTO var_S3A;
SELECT id from vehicle_class where name = 'S3EST' INTO var_S3EST;
SELECT id from vehicle_class where name = 'S3ESTA' INTO var_S3ESTA;
SELECT id from vehicle_class where name = 'P1ESTA' INTO var_P1ESTA;
SELECT id from vehicle_class where name = 'P2ESTA' INTO var_P2ESTA;
SELECT id from vehicle_class where name = 'P3ESTA' INTO var_P3ESTA;
SELECT id from vehicle_class where name = 'S7ESTA' INTO var_S7ESTA;
SELECT id from vehicle_class where name = 'SP1ESTA' INTO var_SP1ESTA;
SELECT id from vehicle_class where name = 'SP2ESTA' INTO var_SP2ESTA;
SELECT id from vehicle_class where name = 'P1EST' INTO var_P1EST;
SELECT id from vehicle_class where name = 'P2EST' INTO var_P2EST;
SELECT id from vehicle_class where name = 'P3EST' INTO var_P3EST;
SELECT id from vehicle_class where name = 'S7EST' INTO var_S7EST;
SELECT id from vehicle_class where name = 'SP1EST' INTO var_SP1EST;
SELECT id from vehicle_class where name = 'SP2EST' INTO var_SP2EST;
SELECT id from vehicle_class where name = 'P1' INTO var_P1;
SELECT id from vehicle_class where name = 'P2' INTO var_P2;
SELECT id from vehicle_class where name = 'P3' INTO var_P3;
SELECT id from vehicle_class where name = 'S7' INTO var_S7;
SELECT id from vehicle_class where name = 'SP1' INTO var_SP1;
SELECT id from vehicle_class where name = 'SP2' INTO var_SP2;
SELECT id from vehicle_class where name = 'P1A' INTO var_P1A;
SELECT id from vehicle_class where name = 'P2A' INTO var_P2A;
SELECT id from vehicle_class where name = 'P3A' INTO var_P3A;
SELECT id from vehicle_class where name = 'S7A' INTO var_S7A;
SELECT id from vehicle_class where name = 'SP1A' INTO var_SP1A;
SELECT id from vehicle_class where name = 'SP2A' INTO var_SP2A;
SELECT id from vehicle_class where name = 'S4' INTO var_S4;
SELECT id from vehicle_class where name = 'S4A' INTO var_S4A;
SELECT id from vehicle_class where name = 'S4EST' INTO var_S4EST;
SELECT id from vehicle_class where name = 'S4ESTA' INTO var_S4ESTA;
SELECT id from vehicle_class where name = 'S5' INTO var_S5;
SELECT id from vehicle_class where name = 'F6' INTO var_F6;
SELECT id from vehicle_class where name = 'F7' INTO var_F7;
SELECT id from vehicle_class where name = 'F8' INTO var_F8;
SELECT id from vehicle_class where name = 'F9' INTO var_F9;
SELECT id from vehicle_class where name = 'F6A' INTO var_F6A;
SELECT id from vehicle_class where name = 'F7A' INTO var_F7A;
SELECT id from vehicle_class where name = 'F8A' INTO var_F8A;
SELECT id from vehicle_class where name = 'F9A' INTO var_F9A;
SELECT id from vehicle_class where name = 'F6EST' INTO var_F6EST;
SELECT id from vehicle_class where name = 'F7EST' INTO var_F7EST;
SELECT id from vehicle_class where name = 'F8EST' INTO var_F8EST;
SELECT id from vehicle_class where name = 'F9EST' INTO var_F9EST;
SELECT id from vehicle_class where name = 'F6ESTA' INTO var_F6ESTA;
SELECT id from vehicle_class where name = 'F7ESTA' INTO var_F7ESTA;
SELECT id from vehicle_class where name = 'F8ESTA' INTO var_F8ESTA;
SELECT id from vehicle_class where name = 'F9ESTA' INTO var_F9ESTA;
SELECT id from vehicle_class where name = 'S5A' INTO var_S5A;
SELECT id from vehicle_class where name = 'S5EST' INTO var_S5EST;
SELECT id from vehicle_class where name = 'S5ESTA' INTO var_S5ESTA;
SELECT id from vehicle_class where name = 'P10EST' INTO var_P10EST;
SELECT id from vehicle_class where name = 'P11EST' INTO var_P11EST;
SELECT id from vehicle_class where name = 'P12EST' INTO var_P12EST;
SELECT id from vehicle_class where name = 'P13EST' INTO var_P13EST;
SELECT id from vehicle_class where name = 'P8EST' INTO var_P8EST;
SELECT id from vehicle_class where name = 'P9EST' INTO var_P9EST;
SELECT id from vehicle_class where name = 'SP10EST' INTO var_SP10EST;
SELECT id from vehicle_class where name = 'SP11EST' INTO var_SP11EST;
SELECT id from vehicle_class where name = 'SP12EST' INTO var_SP12EST;
SELECT id from vehicle_class where name = 'SP13EST' INTO var_SP13EST;
SELECT id from vehicle_class where name = 'SP7EST' INTO var_SP7EST;
SELECT id from vehicle_class where name = 'SP8EST' INTO var_SP8EST;
SELECT id from vehicle_class where name = 'SP9EST' INTO var_SP9EST;
SELECT id from vehicle_class where name = 'P10ESTA' INTO var_P10ESTA;
SELECT id from vehicle_class where name = 'P11ESTA' INTO var_P11ESTA;
SELECT id from vehicle_class where name = 'P12ESTA' INTO var_P12ESTA;
SELECT id from vehicle_class where name = 'P13ESTA' INTO var_P13ESTA;
SELECT id from vehicle_class where name = 'P8ESTA' INTO var_P8ESTA;
SELECT id from vehicle_class where name = 'P9ESTA' INTO var_P9ESTA;
SELECT id from vehicle_class where name = 'SP10ESTA' INTO var_SP10ESTA;
SELECT id from vehicle_class where name = 'SP11ESTA' INTO var_SP11ESTA;
SELECT id from vehicle_class where name = 'SP12ESTA' INTO var_SP12ESTA;
SELECT id from vehicle_class where name = 'SP13ESTA' INTO var_SP13ESTA;
SELECT id from vehicle_class where name = 'SP7ESTA' INTO var_SP7ESTA;
SELECT id from vehicle_class where name = 'SP8ESTA' INTO var_SP8ESTA;
SELECT id from vehicle_class where name = 'SP9ESTA' INTO var_SP9ESTA;
SELECT id from vehicle_class where name = 'P10' INTO var_P10;
SELECT id from vehicle_class where name = 'P11' INTO var_P11;
SELECT id from vehicle_class where name = 'P12' INTO var_P12;
SELECT id from vehicle_class where name = 'P13' INTO var_P13;
SELECT id from vehicle_class where name = 'P8' INTO var_P8;
SELECT id from vehicle_class where name = 'P9' INTO var_P9;
SELECT id from vehicle_class where name = 'SP10' INTO var_SP10;
SELECT id from vehicle_class where name = 'SP11' INTO var_SP11;
SELECT id from vehicle_class where name = 'SP12' INTO var_SP12;
SELECT id from vehicle_class where name = 'SP13' INTO var_SP13;
SELECT id from vehicle_class where name = 'SP7' INTO var_SP7;
SELECT id from vehicle_class where name = 'SP8' INTO var_SP8;
SELECT id from vehicle_class where name = 'SP9' INTO var_SP9;
SELECT id from vehicle_class where name = 'P10A' INTO var_P10A;
SELECT id from vehicle_class where name = 'P11A' INTO var_P11A;
SELECT id from vehicle_class where name = 'P12A' INTO var_P12A;
SELECT id from vehicle_class where name = 'P13A' INTO var_P13A;
SELECT id from vehicle_class where name = 'P8A' INTO var_P8A;
SELECT id from vehicle_class where name = 'P9A' INTO var_P9A;
SELECT id from vehicle_class where name = 'SP10A' INTO var_SP10A;
SELECT id from vehicle_class where name = 'SP11A' INTO var_SP11A;
SELECT id from vehicle_class where name = 'SP12A' INTO var_SP12A;
SELECT id from vehicle_class where name = 'SP13A' INTO var_SP13A;
SELECT id from vehicle_class where name = 'SP7A' INTO var_SP7A;
SELECT id from vehicle_class where name = 'SP8A' INTO var_SP8A;
SELECT id from vehicle_class where name = 'SP9A' INTO var_SP9A;
SELECT id from vehicle_class where name = 'S6' INTO var_S6;
SELECT id from vehicle_class where name = 'S6A' INTO var_S6A;
SELECT id from vehicle_class where name = 'S6EST' INTO var_S6EST;
SELECT id from vehicle_class where name = 'S6ESTA' INTO var_S6ESTA;
SELECT id from vehicle_class where name = 'F3' INTO var_F3;
SELECT id from vehicle_class where name = 'F4' INTO var_F4;
SELECT id from vehicle_class where name = 'F5' INTO var_F5;
SELECT id from vehicle_class where name = 'F3A' INTO var_F3A;
SELECT id from vehicle_class where name = 'F4A' INTO var_F4A;
SELECT id from vehicle_class where name = 'F5A' INTO var_F5A;
SELECT id from vehicle_class where name = 'F3EST' INTO var_F3EST;
SELECT id from vehicle_class where name = 'F4EST' INTO var_F4EST;
SELECT id from vehicle_class where name = 'F5EST' INTO var_F5EST;
SELECT id from vehicle_class where name = 'F3ESTA' INTO var_F3ESTA;
SELECT id from vehicle_class where name = 'F4ESTA' INTO var_F4ESTA;
SELECT id from vehicle_class where name = 'F5ESTA' INTO var_F5ESTA;
SELECT id from vehicle_class where name = 'M' INTO var_M;
SELECT id from vehicle_class where name = 'M1' INTO var_M1;
SELECT id from vehicle_class where name = 'M2' INTO var_M2;
SELECT id from vehicle_class where name = 'M1A' INTO var_M1A;
SELECT id from vehicle_class where name = 'M2A' INTO var_M2A;
SELECT id from vehicle_class where name = 'MA' INTO var_MA;
SELECT id from vehicle_class where name = 'M1EST' INTO var_M1EST;
SELECT id from vehicle_class where name = 'M2EST' INTO var_M2EST;
SELECT id from vehicle_class where name = 'MEST' INTO var_MEST;
SELECT id from vehicle_class where name = 'M1ESTA' INTO var_M1ESTA;
SELECT id from vehicle_class where name = 'M2ESTA' INTO var_M2ESTA;
SELECT id from vehicle_class where name = 'MESTA' INTO var_MESTA;
SELECT id from vehicle_class where name = 'P6EST' INTO var_P6EST;
SELECT id from vehicle_class where name = 'P7EST' INTO var_P7EST;
SELECT id from vehicle_class where name = 'SP5EST' INTO var_SP5EST;
SELECT id from vehicle_class where name = 'SP6EST' INTO var_SP6EST;
SELECT id from vehicle_class where name = 'P6ESTA' INTO var_P6ESTA;
SELECT id from vehicle_class where name = 'P7ESTA' INTO var_P7ESTA;
SELECT id from vehicle_class where name = 'SP5ESTA' INTO var_SP5ESTA;
SELECT id from vehicle_class where name = 'SP6ESTA' INTO var_SP6ESTA;
SELECT id from vehicle_class where name = 'P6' INTO var_P6;
SELECT id from vehicle_class where name = 'P7' INTO var_P7;
SELECT id from vehicle_class where name = 'SP5' INTO var_SP5;
SELECT id from vehicle_class where name = 'SP6' INTO var_SP6;
SELECT id from vehicle_class where name = 'P6A' INTO var_P6A;
SELECT id from vehicle_class where name = 'P7A' INTO var_P7A;
SELECT id from vehicle_class where name = 'SP5A' INTO var_SP5A;
SELECT id from vehicle_class where name = 'SP6A' INTO var_SP6A;
SELECT id from vehicle_class where name = 'M3' INTO var_M3;
SELECT id from vehicle_class where name = 'M4' INTO var_M4;
SELECT id from vehicle_class where name = 'M5' INTO var_M5;
SELECT id from vehicle_class where name = 'M6' INTO var_M6;
SELECT id from vehicle_class where name = 'M3A' INTO var_M3A;
SELECT id from vehicle_class where name = 'M4A' INTO var_M4A;
SELECT id from vehicle_class where name = 'M5A' INTO var_M5A;
SELECT id from vehicle_class where name = 'M6A' INTO var_M6A;
SELECT id from vehicle_class where name = 'M3EST' INTO var_M3EST;
SELECT id from vehicle_class where name = 'M4EST' INTO var_M4EST;
SELECT id from vehicle_class where name = 'M5EST' INTO var_M5EST;
SELECT id from vehicle_class where name = 'M6EST' INTO var_M6EST;
SELECT id from vehicle_class where name = 'M3ESTA' INTO var_M3ESTA;
SELECT id from vehicle_class where name = 'M4ESTA' INTO var_M4ESTA;
SELECT id from vehicle_class where name = 'M5ESTA' INTO var_M5ESTA;
SELECT id from vehicle_class where name = 'M6ESTA' INTO var_M6ESTA;
SELECT id from vehicle_class where name = 'F1' INTO var_F1;
SELECT id from vehicle_class where name = 'F2' INTO var_F2;
SELECT id from vehicle_class where name = 'F1A' INTO var_F1A;
SELECT id from vehicle_class where name = 'F2A' INTO var_F2A;
SELECT id from vehicle_class where name = 'F1EST' INTO var_F1EST;
SELECT id from vehicle_class where name = 'F2EST' INTO var_F2EST;
SELECT id from vehicle_class where name = 'F1ESTA' INTO var_F1ESTA;
SELECT id from vehicle_class where name = 'F2ESTA' INTO var_F2ESTA;
SELECT id from vehicle_class where name = 'P4EST' INTO var_P4EST;
SELECT id from vehicle_class where name = 'P5EST' INTO var_P5EST;
SELECT id from vehicle_class where name = 'SP3EST' INTO var_SP3EST;
SELECT id from vehicle_class where name = 'SP4EST' INTO var_SP4EST;
SELECT id from vehicle_class where name = 'P4ESTA' INTO var_P4ESTA;
SELECT id from vehicle_class where name = 'P5ESTA' INTO var_P5ESTA;
SELECT id from vehicle_class where name = 'SP3ESTA' INTO var_SP3ESTA;
SELECT id from vehicle_class where name = 'SP4ESTA' INTO var_SP4ESTA;
SELECT id from vehicle_class where name = 'P4' INTO var_P4;
SELECT id from vehicle_class where name = 'P5' INTO var_P5;
SELECT id from vehicle_class where name = 'SP3' INTO var_SP3;
SELECT id from vehicle_class where name = 'SP4' INTO var_SP4;
SELECT id from vehicle_class where name = 'P4A' INTO var_P4A;
SELECT id from vehicle_class where name = 'P5A' INTO var_P5A;
SELECT id from vehicle_class where name = 'SP3A' INTO var_SP3A;
SELECT id from vehicle_class where name = 'SP4A' INTO var_SP4A;
SELECT id from vehicle_class where name = 'PV1' INTO var_PV1;
SELECT id from vehicle_class where name = 'PV1A' INTO var_PV1A;
SELECT id from vehicle_class where name = 'PV1EST' INTO var_PV1EST;
SELECT id from vehicle_class where name = 'PV1ESTA' INTO var_PV1ESTA;
SELECT id from vehicle_class where name = 'PV2' INTO var_PV2;
SELECT id from vehicle_class where name = 'PV2A' INTO var_PV2A;
SELECT id from vehicle_class where name = 'PV3A' INTO var_PV3A;
SELECT id from vehicle_class where name = 'PV2EST' INTO var_PV2EST;
SELECT id from vehicle_class where name = 'PV3EST' INTO var_PV3EST;
SELECT id from vehicle_class where name = 'PV2ESTA' INTO var_PV2ESTA;
SELECT id from vehicle_class where name = 'PV3ESTA' INTO var_PV3ESTA;
SELECT id from vehicle_class where name = 'PV3' INTO var_PV3;
SELECT id from vehicle_class where name = 'PV4' INTO var_PV4;
SELECT id from vehicle_class where name = 'PV4A' INTO var_PV4A;
SELECT id from vehicle_class where name = 'PV4EST' INTO var_PV4EST;
SELECT id from vehicle_class where name = 'PV4ESTA' INTO var_PV4ESTA;
SELECT id from vehicle_class where name = 'PV5' INTO var_PV5;
SELECT id from vehicle_class where name = 'PV6' INTO var_PV6;
SELECT id from vehicle_class where name = 'PV5A' INTO var_PV5A;
SELECT id from vehicle_class where name = 'PV6A' INTO var_PV6A;
SELECT id from vehicle_class where name = 'PV5EST' INTO var_PV5EST;
SELECT id from vehicle_class where name = 'PV6EST' INTO var_PV6EST;
SELECT id from vehicle_class where name = 'PV5ESTA' INTO var_PV5ESTA;
SELECT id from vehicle_class where name = 'PV6ESTA' INTO var_PV6ESTA;

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP1, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP1 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP1A, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP1A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP1EST, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP1EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP1ESTA, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP1ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP2, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP2 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP2A, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP2A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP2EST, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP2EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP2ESTA, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP2ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP3, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP3 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP3A, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP3A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP3EST, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP3EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_CP3ESTA, var_lv, var_erac, 45.9, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP3ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S1, var_lv, var_erac, 16.64, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S1 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S1A, var_lv, var_erac, 22.21, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S1A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S2, var_lv, var_erac, 18.58, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S2 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S2A, var_lv, var_erac, 24.16, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S2A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S3, var_lv, var_erac, 21.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S3 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S3A, var_lv, var_erac, 27.5, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S3A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S3EST, var_lv, var_erac, 27.5, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S3EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S3ESTA, var_lv, var_erac, 27.5, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S3ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P1ESTA, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P1ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P2ESTA, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P2ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P3ESTA, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P3ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S7ESTA, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S7ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP1ESTA, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP1ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP2ESTA, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP2ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P1EST, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P1EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P2EST, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P2EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P3EST, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P3EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S7EST, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S7EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP1EST, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP1EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP2EST, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP2EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P1, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P1 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P2, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P2 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P3, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P3 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S7, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S7 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP1, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP1 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP2, var_lv, var_erac, 40.59, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP2 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P1A, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P1A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P2A, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P2A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P3A, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P3A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S7A, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S7A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP1A, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP1A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP2A, var_lv, var_erac, 44.12, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP2A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S4, var_lv, var_erac, 23.11, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S4 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S4A, var_lv, var_erac, 29.72, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S4A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S4EST, var_lv, var_erac, 29.72, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S4EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S4ESTA, var_lv, var_erac, 31.94, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S4ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S5, var_lv, var_erac, 27.5, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S5 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F6, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F6 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F7, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F7 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F8, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F8 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F9, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F9 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F6A, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F6A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F7A, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F7A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F8A, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F8A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F9A, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F9A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F6EST, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F6EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F7EST, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F7EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F8EST, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F8EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F9EST, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F9EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F6ESTA, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F6ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F7ESTA, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F7ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F8ESTA, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F8ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F9ESTA, var_lv, var_erac, 90.88, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F9ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S5A, var_lv, var_erac, 33.06, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S5A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S5EST, var_lv, var_erac, 33.06, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S5EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S5ESTA, var_lv, var_erac, 35.3, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S5ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P10EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P10EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P11EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P11EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P12EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P12EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P13EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P13EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P8EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P8EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P9EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P9EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP10EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP10EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP11EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP11EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP12EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP12EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP13EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP13EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP7EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP7EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP8EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP8EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP9EST, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP9EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P10ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P10ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P11ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P11ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P12ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P12ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P13ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P13ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P8ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P8ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P9ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P9ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP10ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP10ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP11ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP11ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP12ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP12ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP13ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP13ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP7ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP7ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP8ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP8ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP9ESTA, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP9ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P10, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P10 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P11, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P11 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P12, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P12 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P13, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P13 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P8, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P8 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P9, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P9 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP10, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP10 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP11, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP11 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP12, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP12 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP13, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP13 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP7, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP7 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP8, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP8 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP9, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP9 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P10A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P10A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P11A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P11A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P12A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P12A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P13A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P13A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P8A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P8A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P9A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P9A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP10A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP10A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP11A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP11A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP12A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP12A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP13A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP13A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP7A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP7A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP8A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP8A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP9A, var_lv, var_erac, 114.99, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP9A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S6, var_lv, var_erac, 29.72, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S6 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S6A, var_lv, var_erac, 35.3, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S6A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S6EST, var_lv, var_erac, 35.3, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S6EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_S6ESTA, var_lv, var_erac, 37.52, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S6ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F3, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F3 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F4, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F4 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F5, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F5 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F3A, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F3A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F4A, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F4A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F5A, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F5A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F3EST, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F3EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F4EST, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F4EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F5EST, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F5EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F3ESTA, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F3ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F4ESTA, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F4ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F5ESTA, var_lv, var_erac, 58.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F5ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M, var_lv, var_erac, 38.32, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M1, var_lv, var_erac, 38.32, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M1 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M2, var_lv, var_erac, 38.32, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M2 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M1A, var_lv, var_erac, 41.63, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M1A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M2A, var_lv, var_erac, 41.63, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M2A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_MA, var_lv, var_erac, 41.63, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_MA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M1EST, var_lv, var_erac, 38.32, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M1EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M2EST, var_lv, var_erac, 38.32, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M2EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_MEST, var_lv, var_erac, 38.32, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_MEST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M1ESTA, var_lv, var_erac, 41.63, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M1ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M2ESTA, var_lv, var_erac, 41.63, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M2ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_MESTA, var_lv, var_erac, 41.63, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_MESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P6EST, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P6EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P7EST, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P7EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP5EST, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP5EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP6EST, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP6EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P6ESTA, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P6ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P7ESTA, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P7ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP5ESTA, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP5ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP6ESTA, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP6ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P6, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P6 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P7, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P7 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP5, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP5 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP6, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP6 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P6A, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P6A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P7A, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P7A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP5A, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP5A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP6A, var_lv, var_erac, 73.56, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP6A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M3, var_lv, var_erac, 55.36, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M3 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M4, var_lv, var_erac, 55.36, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M4 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M5, var_lv, var_erac, 55.36, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M5 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M6, var_lv, var_erac, 55.36, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M6 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M3A, var_lv, var_erac, 60.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M3A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M4A, var_lv, var_erac, 60.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M4A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M5A, var_lv, var_erac, 60.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M5A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M6A, var_lv, var_erac, 60.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M6A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M3EST, var_lv, var_erac, 55.36, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M3EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M4EST, var_lv, var_erac, 55.36, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M4EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M5EST, var_lv, var_erac, 55.36, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M5EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M6EST, var_lv, var_erac, 55.36, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M6EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M3ESTA, var_lv, var_erac, 60.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M3ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M4ESTA, var_lv, var_erac, 60.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M4ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M5ESTA, var_lv, var_erac, 60.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M5ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_M6ESTA, var_lv, var_erac, 60.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M6ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F1, var_lv, var_erac, 53, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F1 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F2, var_lv, var_erac, 53, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F2 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F1A, var_lv, var_erac, 53, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F1A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F2A, var_lv, var_erac, 53, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F2A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F1EST, var_lv, var_erac, 53, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F1EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F2EST, var_lv, var_erac, 53, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F2EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F1ESTA, var_lv, var_erac, 53, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F1ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_F2ESTA, var_lv, var_erac, 53, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F2ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P4EST, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P4EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P5EST, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P5EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP3EST, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP3EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP4EST, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP4EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P4ESTA, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P4ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P5ESTA, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P5ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP3ESTA, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP3ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP4ESTA, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP4ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P4, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P4 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P5, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P5 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP3, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP3 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP4, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP4 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P4A, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P4A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_P5A, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P5A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP3A, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP3A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_SP4A, var_lv, var_erac, 65.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP4A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV1, var_lv, var_erac, 25.14, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV1 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV1A, var_lv, var_erac, 25.14, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV1A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV1EST, var_lv, var_erac, 25.14, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV1EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV1ESTA, var_lv, var_erac, 25.14, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV1ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV2, var_lv, var_erac, 36.27, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV2 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV2A, var_lv, var_erac, 36.27, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV2A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV3A, var_lv, var_erac, 40.74, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV3A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV2EST, var_lv, var_erac, 36.27, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV2EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV3EST, var_lv, var_erac, 40.74, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV3EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV2ESTA, var_lv, var_erac, 36.27, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV2ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV3ESTA, var_lv, var_erac, 40.74, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV3ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV3, var_lv, var_erac, 40.74, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV3 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV4, var_lv, var_erac, 40.74, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV4 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV4A, var_lv, var_erac, 40.74, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV4A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV4EST, var_lv, var_erac, 40.74, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV4EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV4ESTA, var_lv, var_erac, 40.74, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV4ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV5, var_lv, var_erac, 50.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV5 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV6, var_lv, var_erac, 66.2, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV6 AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV5A, var_lv, var_erac, 50.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV5A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV6A, var_lv, var_erac, 66.2, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV6A AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV5EST, var_lv, var_erac, 50.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV5EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV6EST, var_lv, var_erac, 66.2, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV6EST AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV5ESTA, var_lv, var_erac, 50.92, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV5ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

INSERT INTO vehicle_class_price_special_rate ( version, vehicle_class_id, insurer_id, chorganisation_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, age )
SELECT var_ver, var_PV6ESTA, var_lv, var_erac, 66.2, var_date_lv, 999, now(), 999, now(), 99.00
WHERE  NOT EXISTS ( SELECT id FROM vehicle_class_price_special_rate WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV6ESTA AND vehicle_class_price_special_rate.insurer_id = var_lv AND vehicle_class_price_special_rate.chorganisation_id = var_lv AND vehicle_class_price_special_rate.start_date  >= var_date_lv);

END $$;

