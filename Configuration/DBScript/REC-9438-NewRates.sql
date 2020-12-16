-- CHOX - Update Manual Supplier Rate
-- version is set to 0, which is an arbitrary new value
-- age is set up to 99.00 arbitrarily
-- created_by and last_modified_by are set up to 999
-- created_date and last_modified_date are set up to now

DO
$$
    DECLARE
        var_ver                     integer := 0;
        DECLARE var_auxillis        integer;
        DECLARE var_date_motability date    := '01/11/2020';
        DECLARE var_motability      integer;
        DECLARE var_B1              integer;
        DECLARE var_B2              integer;
        DECLARE var_B3              integer;
        DECLARE var_B4              integer;
        DECLARE var_B5              integer;
        DECLARE var_B6              integer;
        DECLARE var_CM1             integer;
        DECLARE var_CM2             integer;
        DECLARE var_CM3             integer;
        DECLARE var_CP1             integer;
        DECLARE var_CP2             integer;
        DECLARE var_CP3             integer;
        DECLARE var_CS1             integer;
        DECLARE var_CS2             integer;
        DECLARE var_CS3             integer;
        DECLARE var_CS4             integer;
        DECLARE var_CS5             integer;
        DECLARE var_CV1             integer;
        DECLARE var_CV2             integer;
        DECLARE var_CV3             integer;
        DECLARE var_CV4             integer;
        DECLARE var_F1              integer;
        DECLARE var_F2              integer;
        DECLARE var_F3              integer;
        DECLARE var_F4              integer;
        DECLARE var_F5              integer;
        DECLARE var_F6              integer;
        DECLARE var_F7              integer;
        DECLARE var_F8              integer;
        DECLARE var_F9              integer;
        DECLARE var_M               integer;
        DECLARE var_M1              integer;
        DECLARE var_M2              integer;
        DECLARE var_M3              integer;
        DECLARE var_M4              integer;
        DECLARE var_M5              integer;
        DECLARE var_M6              integer;
        DECLARE var_P1              integer;
        DECLARE var_P10             integer;
        DECLARE var_P11             integer;
        DECLARE var_P12             integer;
        DECLARE var_P13             integer;
        DECLARE var_P2              integer;
        DECLARE var_P3              integer;
        DECLARE var_P4              integer;
        DECLARE var_P5              integer;
        DECLARE var_P6              integer;
        DECLARE var_P7              integer;
        DECLARE var_P8              integer;
        DECLARE var_P9              integer;
        DECLARE var_PT13            integer;
        DECLARE var_PT9             integer;
        DECLARE var_PV1             integer;
        DECLARE var_PV2             integer;
        DECLARE var_PV3             integer;
        DECLARE var_PV4             integer;
        DECLARE var_PV5             integer;
        DECLARE var_PV6             integer;
        DECLARE var_RV1             integer;
        DECLARE var_RV2             integer;
        DECLARE var_S1              integer;
        DECLARE var_S2              integer;
        DECLARE var_S3              integer;
        DECLARE var_S4              integer;
        DECLARE var_S5              integer;
        DECLARE var_S6              integer;
        DECLARE var_S7              integer;
        DECLARE var_SP1             integer;
        DECLARE var_SP10            integer;
        DECLARE var_SP11            integer;
        DECLARE var_SP12            integer;
        DECLARE var_SP13            integer;
        DECLARE var_SP2             integer;
        DECLARE var_SP3             integer;
        DECLARE var_SP4             integer;
        DECLARE var_SP5             integer;
        DECLARE var_SP6             integer;
        DECLARE var_SP7             integer;
        DECLARE var_SP8             integer;
        DECLARE var_SP9             integer;
        DECLARE var_T6              integer;
        DECLARE var_T7              integer;
        DECLARE var_T8              integer;
        DECLARE var_T10             integer;
        DECLARE var_T12             integer;
        DECLARE var_T14             integer;


    BEGIN
        SELECT id from chorganisation where name = 'Auxillis' INTO var_auxillis;
        SELECT id from insurer where name = 'Motability' INTO var_motability;
        SELECT id from vehicle_class where name = 'B1' INTO var_B1;
        SELECT id from vehicle_class where name = 'B2' INTO var_B2;
        SELECT id from vehicle_class where name = 'B3' INTO var_B3;
        SELECT id from vehicle_class where name = 'B4' INTO var_B4;
        SELECT id from vehicle_class where name = 'B5' INTO var_B5;
        SELECT id from vehicle_class where name = 'B6' INTO var_B6;
        SELECT id from vehicle_class where name = 'CM1' INTO var_CM1;
        SELECT id from vehicle_class where name = 'CM2' INTO var_CM2;
        SELECT id from vehicle_class where name = 'CM3' INTO var_CM3;
        SELECT id from vehicle_class where name = 'CP1' INTO var_CP1;
        SELECT id from vehicle_class where name = 'CP2' INTO var_CP2;
        SELECT id from vehicle_class where name = 'CP3' INTO var_CP3;
        SELECT id from vehicle_class where name = 'CS1' INTO var_CS1;
        SELECT id from vehicle_class where name = 'CS2' INTO var_CS2;
        SELECT id from vehicle_class where name = 'CS3' INTO var_CS3;
        SELECT id from vehicle_class where name = 'CS4' INTO var_CS4;
        SELECT id from vehicle_class where name = 'CS5' INTO var_CS5;
        SELECT id from vehicle_class where name = 'CV1' INTO var_CV1;
        SELECT id from vehicle_class where name = 'CV2' INTO var_CV2;
        SELECT id from vehicle_class where name = 'CV3' INTO var_CV3;
        SELECT id from vehicle_class where name = 'CV4' INTO var_CV4;
        SELECT id from vehicle_class where name = 'S1' INTO var_S1;
        SELECT id from vehicle_class where name = 'S2' INTO var_S2;
        SELECT id from vehicle_class where name = 'S3' INTO var_S3;
        SELECT id from vehicle_class where name = 'P1' INTO var_P1;
        SELECT id from vehicle_class where name = 'P2' INTO var_P2;
        SELECT id from vehicle_class where name = 'P3' INTO var_P3;
        SELECT id from vehicle_class where name = 'S7' INTO var_S7;
        SELECT id from vehicle_class where name = 'SP1' INTO var_SP1;
        SELECT id from vehicle_class where name = 'SP2' INTO var_SP2;
        SELECT id from vehicle_class where name = 'S4' INTO var_S4;
        SELECT id from vehicle_class where name = 'S5' INTO var_S5;
        SELECT id from vehicle_class where name = 'F6' INTO var_F6;
        SELECT id from vehicle_class where name = 'F7' INTO var_F7;
        SELECT id from vehicle_class where name = 'F8' INTO var_F8;
        SELECT id from vehicle_class where name = 'F9' INTO var_F9;
        SELECT id from vehicle_class where name = 'P8' INTO var_P8;
        SELECT id from vehicle_class where name = 'P9' INTO var_P9;
        SELECT id from vehicle_class where name = 'PT9' INTO var_PT9;
        SELECT id from vehicle_class where name = 'PT13' INTO var_PT13;
        SELECT id from vehicle_class where name = 'P10' INTO var_P10;
        SELECT id from vehicle_class where name = 'P11' INTO var_P11;
        SELECT id from vehicle_class where name = 'P12' INTO var_P12;
        SELECT id from vehicle_class where name = 'P13' INTO var_P13;
        SELECT id from vehicle_class where name = 'SP10' INTO var_SP10;
        SELECT id from vehicle_class where name = 'SP11' INTO var_SP11;
        SELECT id from vehicle_class where name = 'SP12' INTO var_SP12;
        SELECT id from vehicle_class where name = 'SP13' INTO var_SP13;
        SELECT id from vehicle_class where name = 'SP7' INTO var_SP7;
        SELECT id from vehicle_class where name = 'SP8' INTO var_SP8;
        SELECT id from vehicle_class where name = 'SP9' INTO var_SP9;
        SELECT id from vehicle_class where name = 'S6' INTO var_S6;
        SELECT id from vehicle_class where name = 'F3' INTO var_F3;
        SELECT id from vehicle_class where name = 'F4' INTO var_F4;
        SELECT id from vehicle_class where name = 'F5' INTO var_F5;
        SELECT id from vehicle_class where name = 'M' INTO var_M;
        SELECT id from vehicle_class where name = 'M1' INTO var_M1;
        SELECT id from vehicle_class where name = 'M2' INTO var_M2;
        SELECT id from vehicle_class where name = 'P6' INTO var_P6;
        SELECT id from vehicle_class where name = 'P7' INTO var_P7;
        SELECT id from vehicle_class where name = 'SP5' INTO var_SP5;
        SELECT id from vehicle_class where name = 'SP6' INTO var_SP6;
        SELECT id from vehicle_class where name = 'M3' INTO var_M3;
        SELECT id from vehicle_class where name = 'M4' INTO var_M4;
        SELECT id from vehicle_class where name = 'M5' INTO var_M5;
        SELECT id from vehicle_class where name = 'M6' INTO var_M6;
        SELECT id from vehicle_class where name = 'F1' INTO var_F1;
        SELECT id from vehicle_class where name = 'F2' INTO var_F2;
        SELECT id from vehicle_class where name = 'P4' INTO var_P4;
        SELECT id from vehicle_class where name = 'P5' INTO var_P5;
        SELECT id from vehicle_class where name = 'SP3' INTO var_SP3;
        SELECT id from vehicle_class where name = 'SP4' INTO var_SP4;
        SELECT id from vehicle_class where name = 'PV1' INTO var_PV1;
        SELECT id from vehicle_class where name = 'PV2' INTO var_PV2;
        SELECT id from vehicle_class where name = 'PV3' INTO var_PV3;
        SELECT id from vehicle_class where name = 'PV4' INTO var_PV4;
        SELECT id from vehicle_class where name = 'PV5' INTO var_PV5;
        SELECT id from vehicle_class where name = 'PV6' INTO var_PV6;
        SELECT id from vehicle_class where name = 'RV1' INTO var_RV1;
        SELECT id from vehicle_class where name = 'RV2' INTO var_RV2;
        SELECT id from vehicle_class where name = 'T10' INTO var_T10;
        SELECT id from vehicle_class where name = 'T12' INTO var_T12;
        SELECT id from vehicle_class where name = 'T14' INTO var_T14;
        SELECT id from vehicle_class where name = 'T6' INTO var_T6;
        SELECT id from vehicle_class where name = 'T7' INTO var_T7;
        SELECT id from vehicle_class where name = 'T8' INTO var_T8;

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_B1, var_motability, var_auxillis, 32.67, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_B1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_B2, var_motability, var_auxillis, 38.16, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_B2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_B3, var_motability, var_auxillis, 53.80, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_B3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_B4, var_motability, var_auxillis, 64.79, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_B4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_B5, var_motability, var_auxillis, 75.71, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_B5
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_B6, var_motability, var_auxillis, 76.86, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_B6
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CM1, var_motability, var_auxillis, 63.55, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CM1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CM2, var_motability, var_auxillis, 73.53, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CM2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CM3, var_motability, var_auxillis, 83.29, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CM3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CP1, var_motability, var_auxillis, 48.88, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CP2, var_motability, var_auxillis, 55.45, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CP3, var_motability, var_auxillis, 63.14, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CP3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CS1, var_motability, var_auxillis, 57.37, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CS1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CS2, var_motability, var_auxillis, 61.35, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CS2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CS3, var_motability, var_auxillis, 65.89, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CS3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CS4, var_motability, var_auxillis, 69.86, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CS4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CS5, var_motability, var_auxillis, 74.39, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CS5
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CV1, var_motability, var_auxillis, 51.96, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CV1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CV2, var_motability, var_auxillis, 53.31, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CV2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CV3, var_motability, var_auxillis, 56.04, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CV3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_CV4, var_motability, var_auxillis, 79.84, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_CV4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F1, var_motability, var_auxillis, 87.83, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F2, var_motability, var_auxillis, 94.11, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F3, var_motability, var_auxillis, 101.44, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F4, var_motability, var_auxillis, 124.44, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F5, var_motability, var_auxillis, 167.30, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F5
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F6, var_motability, var_auxillis, 188.23, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F6
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F7, var_motability, var_auxillis, 219.59, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F7
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F8, var_motability, var_auxillis, 235.28, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F8
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_F9, var_motability, var_auxillis, 287.56, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_F9
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_M, var_motability, var_auxillis, 47.49, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_M1, var_motability, var_auxillis, 54.89, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_M2, var_motability, var_auxillis, 62.59, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_M3, var_motability, var_auxillis, 73.57, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_M4, var_motability, var_auxillis, 93.33, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_M5, var_motability, var_auxillis, 139.99, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M5
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_M6, var_motability, var_auxillis, 177.32, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_M6
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P1, var_motability, var_auxillis, 73.20, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P10, var_motability, var_auxillis, 281.13, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P10
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P11, var_motability, var_auxillis, 394.87, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P11
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P12, var_motability, var_auxillis, 591.07, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P12
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P13, var_motability, var_auxillis, 857.04, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P13
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P2, var_motability, var_auxillis, 81.57, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P3, var_motability, var_auxillis, 86.79, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P4, var_motability, var_auxillis, 105.61, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P5, var_motability, var_auxillis, 131.76, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P5
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P6, var_motability, var_auxillis, 149.02, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P6
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P7, var_motability, var_auxillis, 173.84, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P7
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P8, var_motability, var_auxillis, 198.66, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P8
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_P9, var_motability, var_auxillis, 228.47, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_P9
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_PT13, var_motability, var_auxillis, 298.01, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PT13
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_PT9, var_motability, var_auxillis, 209.12, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PT9
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_PV1, var_motability, var_auxillis, 35.92, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_PV2, var_motability, var_auxillis, 41.69, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_PV3, var_motability, var_auxillis, 41.69, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_PV4, var_motability, var_auxillis, 46.22, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_PV5, var_motability, var_auxillis, 48.56, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV5
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_PV6, var_motability, var_auxillis, 50.90, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_PV6
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_RV1, var_motability, var_auxillis, 69.42, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_RV1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_RV2, var_motability, var_auxillis, 96.01, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_RV2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_S1, var_motability, var_auxillis, 29.73, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_S2, var_motability, var_auxillis, 33.71, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_S3, var_motability, var_auxillis, 35.95, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_S4, var_motability, var_auxillis, 38.55, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_S5, var_motability, var_auxillis, 40.78, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S5
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_S6, var_motability, var_auxillis, 43.45, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S6
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_S7, var_motability, var_auxillis, 60.92, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_S7
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP1, var_motability, var_auxillis, 70.46, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP1
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP10, var_motability, var_auxillis, 255.79, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP10
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP11, var_motability, var_auxillis, 307.94, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP11
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP12, var_motability, var_auxillis, 404.82, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP12
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP13, var_motability, var_auxillis, 591.07, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP13
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP2, var_motability, var_auxillis, 82.35, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP2
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP3, var_motability, var_auxillis, 92.02, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP3
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP4, var_motability, var_auxillis, 112.94, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP4
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP5, var_motability, var_auxillis, 123.39, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP5
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP6, var_motability, var_auxillis, 163.91, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP6
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP7, var_motability, var_auxillis, 183.78, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP7
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP8, var_motability, var_auxillis, 203.65, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP8
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_SP9, var_motability, var_auxillis, 223.52, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_SP9
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_T10, var_motability, var_auxillis, 156.86, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_T10
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_T12, var_motability, var_auxillis, 182.99, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_T12
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_T14, var_motability, var_auxillis, 75.29, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_T14
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_T6, var_motability, var_auxillis, 71.11, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_T6
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_T7, var_motability, var_auxillis, 57.52, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_T7
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

        INSERT INTO vehicle_class_price_special_rate (version, vehicle_class_id, insurer_id, chorganisation_id, price,
                                                      start_date, created_by, created_date, last_modified_by,
                                                      last_modified_date, age)
            SELECT var_ver, var_T8, var_motability, var_auxillis, 78.17, var_date_motability, 999, now(), 999, now(), 99.00
            WHERE NOT EXISTS(SELECT id
                             FROM vehicle_class_price_special_rate
                             WHERE vehicle_class_price_special_rate.vehicle_class_id = var_T8
                               AND vehicle_class_price_special_rate.insurer_id = var_motability
                               AND vehicle_class_price_special_rate.chorganisation_id = var_auxillis
                               AND vehicle_class_price_special_rate.start_date >= var_date_motability);

    END
$$;