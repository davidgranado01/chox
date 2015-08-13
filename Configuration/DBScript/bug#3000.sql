--
-- bug#3000 - Add Supplier Rates between DLG and Manual Helphire
--

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, vehicle_class_id, price, start_date,
                                             created_by, created_date, last_modified_by, last_modified_date)
select 0, 6, 1123, 99.99, id, 60.00, '2015-01-01', 999, now(), 999, now()
from vehicle_class;


