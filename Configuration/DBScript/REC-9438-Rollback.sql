DO
$$
    DECLARE var_manual_auxillis integer;
        DECLARE var_date_motability date    := '01/11/2020';
        DECLARE var_motability integer;

    BEGIN
        SELECT id from chorganisation where name = 'Auxillis' INTO var_manual_auxillis;
        SELECT id from insurer where name = 'Motability' INTO var_motability;

        DELETE from vehicle_class_price_special_rate
        WHERE vehicle_class_price_special_rate.insurer_id = var_motability
          AND vehicle_class_price_special_rate.chorganisation_id = var_manual_auxillis
          AND vehicle_class_price_special_rate.start_date >= var_date_motability;

    END
$$;