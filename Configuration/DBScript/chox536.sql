--
-- CHOX-536 : Add new rate for Manual Auxillis - for DLG
--
insert into vehicle_class_price_special_rate(insurer_id, chorganisation_id, vehicle_class_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, version)
    select insurer_id, chorganisation_id, vehicle_class_id, 59.00, '2018-04-24', 999, now(), 999, now(), 0
    from vehicle_class_price_special_rate
    where start_date='2015-01-01'
      and chorganisation_id = 1123 and insurer_id=6;
