--------------------------------------------------------------------------------
-- CHOX-342 - Production - Update Subscriber rates between LV and Enterprise
--------------------------------------------------------------------------------
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 16.18
from vehicle_class where name='S1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.42
from vehicle_class where name='S1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 17.99
from vehicle_class where name='S2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 23.25
from vehicle_class where name='S2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.15
from vehicle_class where name='S3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 26.40
from vehicle_class where name='S3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.15
from vehicle_class where name='S3EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.15
from vehicle_class where name='S3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 23.25
from vehicle_class where name='S4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 28.50
from vehicle_class where name='S4EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 30.60
from vehicle_class where name='S4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 26.40
from vehicle_class where name='S5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F7A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F8A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F9A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 31.65
from vehicle_class where name='S5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 31.65
from vehicle_class where name='S5EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 33.75
from vehicle_class where name='S5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P10';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P11';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P13';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP10';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP11';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP13';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P10A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P11A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P12A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P13A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP9A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP10A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP11A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP12A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP13A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 28.50
from vehicle_class where name='S6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 33.75
from vehicle_class where name='S6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 33.75
from vehicle_class where name='S6EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 35.85
from vehicle_class where name='S6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 36.61
from vehicle_class where name='M';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 36.61
from vehicle_class where name='M1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 36.61
from vehicle_class where name='M2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='MA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P7A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P8A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P9A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.04
from vehicle_class where name='M3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.04
from vehicle_class where name='M4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.04
from vehicle_class where name='M5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.04
from vehicle_class where name='M6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 22.21
from vehicle_class where name='PV1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 35.85
from vehicle_class where name='PV2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 35.85
from vehicle_class where name='PV3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.27
from vehicle_class where name='PV4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 49.09
from vehicle_class where name='PV5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2017-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 49.09
from vehicle_class where name='PV6';

--------------------------------------------------------------------------------
-- CHOX-342 - Production - Update Subscriber rates between QBE and SGSM
--------------------------------------------------------------------------------
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 20.94 from vehicle_class where name='B1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 22.4 from vehicle_class where name='B2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 27.1 from vehicle_class where name='B3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 30.6 from vehicle_class where name='B4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 39 from vehicle_class where name='B5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 40.1 from vehicle_class where name='B6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 39.95 from vehicle_class where name='CM1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 39.95 from vehicle_class where name='CM2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 39.95 from vehicle_class where name='CM3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 46.64 from vehicle_class where name='CP1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 48.79 from vehicle_class where name='CP2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 56.77 from vehicle_class where name='CP3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 49.05 from vehicle_class where name='CS1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 52.33 from vehicle_class where name='CS2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 53.59 from vehicle_class where name='CS3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 59.4 from vehicle_class where name='CS4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 63.44 from vehicle_class where name='CS5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 38.95 from vehicle_class where name='CV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 40.46 from vehicle_class where name='CV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 47.6 from vehicle_class where name='CV3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 69 from vehicle_class where name='CV4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 44.25 from vehicle_class where name='F1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 45.01 from vehicle_class where name='F2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 61.85 from vehicle_class where name='F3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 74.41 from vehicle_class where name='F4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 94.13 from vehicle_class where name='F5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 94.63 from vehicle_class where name='F6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 119.43 from vehicle_class where name='F7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 114.1 from vehicle_class where name='F8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 124.1 from vehicle_class where name='F9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 23.49 from vehicle_class where name='M1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 20.87 from vehicle_class where name='M2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 41.84 from vehicle_class where name='M3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 49.55 from vehicle_class where name='M4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 94.1 from vehicle_class where name='M5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 116.3 from vehicle_class where name='M6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 21.79 from vehicle_class where name='P1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 24.57 from vehicle_class where name='P2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 31.58 from vehicle_class where name='P3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 36.56 from vehicle_class where name='P4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 36.56 from vehicle_class where name='P5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 74.66 from vehicle_class where name='P6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 74.66 from vehicle_class where name='P7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 119.06 from vehicle_class where name='P8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 119.06 from vehicle_class where name='P9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 104.1 from vehicle_class where name='P10';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 412.1 from vehicle_class where name='P11';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 463.1 from vehicle_class where name='P12';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 19.07 from vehicle_class where name='PV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 20.35 from vehicle_class where name='PV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 20.35 from vehicle_class where name='PV3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 24.87 from vehicle_class where name='PV4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 37.43 from vehicle_class where name='PV5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 40.46 from vehicle_class where name='PV6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 72.73 from vehicle_class where name='RV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 101.36 from vehicle_class where name='RV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 15.17 from vehicle_class where name='S1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 15.75 from vehicle_class where name='S2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 17.48 from vehicle_class where name='S3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 18.41 from vehicle_class where name='S4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 21.38 from vehicle_class where name='S5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 22.25 from vehicle_class where name='S6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 20.3 from vehicle_class where name='S7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 54.51 from vehicle_class where name='SP1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 60 from vehicle_class where name='SP2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 88.58 from vehicle_class where name='SP3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 44.1 from vehicle_class where name='SP4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 59.1 from vehicle_class where name='SP5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 91.28 from vehicle_class where name='SP6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 92.05 from vehicle_class where name='SP7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 92.82 from vehicle_class where name='SP8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 99.1 from vehicle_class where name='SP9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 104.1 from vehicle_class where name='SP10';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 109.1 from vehicle_class where name='SP11';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 119.1 from vehicle_class where name='SP12';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 412.1 from vehicle_class where name='SP13';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 65.76 from vehicle_class where name='T1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 76.32 from vehicle_class where name='T2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 65.76 from vehicle_class where name='T3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 59.1 from vehicle_class where name='T4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 71.01 from vehicle_class where name='T5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 95 from vehicle_class where name='T6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 96.1 from vehicle_class where name='T7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 74.1 from vehicle_class where name='T8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 119.1 from vehicle_class where name='T9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 79.1 from vehicle_class where name='T10';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 94.1 from vehicle_class where name='T12';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1016, 99, '2017-05-15'::timestamp without time zone, id, 999, 999, now(), now(), 179.1 from vehicle_class where name='T13';

--------------------------------------------------------------------------------
-- CHOX-342 - Production - Update Subscriber rates between QBE and Proximo
--------------------------------------------------------------------------------
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 26.741 from vehicle_class where name='S1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 30.3195 from vehicle_class where name='S2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 32.334 from vehicle_class where name='S3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 34.6715 from vehicle_class where name='S4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 36.6775 from vehicle_class where name='S5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 39.083 from vehicle_class where name='S6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 54.7995 from vehicle_class where name='S7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 42.721 from vehicle_class where name='M';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 49.3765 from vehicle_class where name='M1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 56.2955 from vehicle_class where name='M2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 66.1725 from vehicle_class where name='M3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 83.946 from vehicle_class where name='M4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 125.9105 from vehicle_class where name='M5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 159.494 from vehicle_class where name='M6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 82.9515 from vehicle_class where name='F1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 88.8845 from vehicle_class where name='F2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 95.8035 from vehicle_class where name='F3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 117.5295 from vehicle_class where name='F4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 158.0065 from vehicle_class where name='F5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 177.769 from vehicle_class where name='F6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 207.3915 from vehicle_class where name='F7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 222.207 from vehicle_class where name='F8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 271.5835 from vehicle_class where name='F9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 69.1305 from vehicle_class where name='P1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 77.0355 from vehicle_class where name='P2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 81.9655 from vehicle_class where name='P3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 99.739 from vehicle_class where name='P4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 124.44 from vehicle_class where name='P5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 148.1465 from vehicle_class where name='P6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 172.822 from vehicle_class where name='P7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 197.4975 from vehicle_class where name='P8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 227.137 from vehicle_class where name='P9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 279.4885 from vehicle_class where name='P10';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 392.5555 from vehicle_class where name='P11';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 587.6135 from vehicle_class where name='P12';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 852.0315 from vehicle_class where name='P13';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 66.5465 from vehicle_class where name='SP1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 77.775 from vehicle_class where name='SP2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 86.904 from vehicle_class where name='SP3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 106.6665 from vehicle_class where name='SP4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 116.535 from vehicle_class where name='SP5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 162.9535 from vehicle_class where name='SP6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 182.7075 from vehicle_class where name='SP7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 202.4615 from vehicle_class where name='SP8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 222.207 from vehicle_class where name='SP9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 254.2945 from vehicle_class where name='SP10';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 306.1445 from vehicle_class where name='SP11';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 402.4495 from vehicle_class where name='SP12';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 587.6135 from vehicle_class where name='SP13';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 32.3085 from vehicle_class where name='PV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 37.4935 from vehicle_class where name='PV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 37.4935 from vehicle_class where name='PV3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 41.5735 from vehicle_class where name='PV4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 43.6815 from vehicle_class where name='PV5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 45.781 from vehicle_class where name='PV6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 46.733 from vehicle_class where name='CV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 47.9485 from vehicle_class where name='CV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 50.405 from vehicle_class where name='CV3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 71.8165 from vehicle_class where name='CV4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 62.441 from vehicle_class where name='RV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 86.36 from vehicle_class where name='RV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 43.962 from vehicle_class where name='CP1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 49.878 from vehicle_class where name='CP2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 56.7885 from vehicle_class where name='CP3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 51.595 from vehicle_class where name='CS1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 55.182 from vehicle_class where name='CS2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 59.262 from vehicle_class where name='CS3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 62.832 from vehicle_class where name='CS4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price) select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 626.912 from vehicle_class where name='CS5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 69.72 from vehicle_class where name='T1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 47.65 from vehicle_class where name='T1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 92.95 from vehicle_class where name='T2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 65.06 from vehicle_class where name='T2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 87.13 from vehicle_class where name='T3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 58.09 from vehicle_class where name='T3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 110.39 from vehicle_class where name='T4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 76.68 from vehicle_class where name='T4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 87.13 from vehicle_class where name='NT3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 58.09 from vehicle_class where name='NT3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 110.39 from vehicle_class where name='NT4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 76.68 from vehicle_class where name='NT4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 79.01 from vehicle_class where name='T5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 55.75 from vehicle_class where name='T5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 106.89 from vehicle_class where name='T6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 79.01 from vehicle_class where name='T6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 90.63 from vehicle_class where name='T7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 63.91 from vehicle_class where name='T7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 118.50 from vehicle_class where name='T8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 86.85 from vehicle_class where name='T8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 232.35 from vehicle_class where name='PT9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 174.29 from vehicle_class where name='PT9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 174.29 from vehicle_class where name='T10';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 127.80 from vehicle_class where name='T10';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 203.32 from vehicle_class where name='T12';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 145.25 from vehicle_class where name='T12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 331.12 from vehicle_class where name='T13';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 244.00 from vehicle_class where name='T13';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 3, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 83.65 from vehicle_class where name='T14';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1025, 99, '2017-05-22'::timestamp without time zone, id, 999, 999, now(), now(), 60.42 from vehicle_class where name='T14';
----------------------
-- End of CHOX-342
----------------------
