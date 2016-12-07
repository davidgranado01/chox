--
-- CHOX-234 : Update Rates for Auxillis vs RSA and Motability
--
insert into vehicle_class_price_special_rate(insurer_id, chorganisation_id, vehicle_class_id, price, start_date, created_by, created_date, last_modified_by, last_modified_date, version)
    select insurer_id, chorganisation_id, vehicle_class_id, 57.25, '2016-11-01', 999, now(), 999, now(), 0
    from vehicle_class_price_special_rate
    where start_date='2014-07-28'
      and chorganisation_id = 1010 and insurer_id in (3,19);
