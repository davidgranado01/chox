--------------------------------------------------------------------------------
-- bug#2859 - Production -  Fixed Fee Daily Rate Change
--------------------------------------------------------------------------------
DELETE FROM vehicle_class_price_special_rate where insurer_id in (3,19) and chorganisation_id=1010 and start_date='2012-12-01';

INSERT INTO vehicle_class_price_special_rate(version, vehicle_class_id, insurer_id, chorganisation_id, start_date, age, price, created_by, last_modified_by, created_date, last_modified_date)
    SELECT 0, vehicle_class_id, insurer_id, chorganisation_id, '2014-07-28', 99.99, 54.50, 999, 999, now(), now()
    FROM vehicle_class_price_special_rate
    WHERE insurer_id in (3,19) and chorganisation_id=1010;

----------------------
-- End of bug#2859
----------------------
