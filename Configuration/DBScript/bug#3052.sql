--------------------------------------------------------------------------------
-- bug#3052 - Production - Daily Rates for LV vs ERAC
--------------------------------------------------------------------------------
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.04
from vehicle_class where name='M3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.04
from vehicle_class where name='M4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.04
from vehicle_class where name='M5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.04
from vehicle_class where name='M6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P10';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P11';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P13';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 22.21
from vehicle_class where name='PV1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 35.85
from vehicle_class where name='PV2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 35.85
from vehicle_class where name='PV3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.06
from vehicle_class where name='PV4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.06
from vehicle_class where name='PV5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.06
from vehicle_class where name='PV6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 16.18
from vehicle_class where name='S1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 17.99
from vehicle_class where name='S2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.15
from vehicle_class where name='S3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 23.25
from vehicle_class where name='S4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 26.40
from vehicle_class where name='S5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 28.50
from vehicle_class where name='S6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='S7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP10';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP11';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F7A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F8A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F9A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='MA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P7A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P8A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P9A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P10A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P11A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P12A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P13A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 22.21
from vehicle_class where name='PV1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 35.85
from vehicle_class where name='PV2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 35.85
from vehicle_class where name='PV3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.06
from vehicle_class where name='PV4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.06
from vehicle_class where name='PV5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.06
from vehicle_class where name='PV6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.42
from vehicle_class where name='S1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 23.25
from vehicle_class where name='S2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 26.40
from vehicle_class where name='S3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 28.50
from vehicle_class where name='S4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 31.65
from vehicle_class where name='S5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 33.75
from vehicle_class where name='S6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='S7A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP6A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP7A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP8A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP9A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP10A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP11A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP12A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP13A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP1EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP2EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP3EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F1EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F2EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F3EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F4EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F5EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F6EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F7EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F8EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F9EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='MEST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M1EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M2EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M3EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M4EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M5EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M6EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P1EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P2EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P3EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P4EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P5EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P6EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P7EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P8EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P9EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P10EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P11EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P12EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P13EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.42
from vehicle_class where name='S1EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 23.25
from vehicle_class where name='S2EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 26.40
from vehicle_class where name='S3EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 28.50
from vehicle_class where name='S4EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 31.65
from vehicle_class where name='S5EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 33.75
from vehicle_class where name='S6EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='S7EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP1EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP2EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP3EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP4EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP5EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP6EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP7EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP8EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP9EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP10EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP11EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP12EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP13EST';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='CP3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.59
from vehicle_class where name='F2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.85
from vehicle_class where name='F5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F7ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F8ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.29
from vehicle_class where name='F9ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='MESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.73
from vehicle_class where name='M2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.28
from vehicle_class where name='M6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='P5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='P7ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P8ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P9ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P10ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P11ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P12ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='P13ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.42
from vehicle_class where name='S1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 23.25
from vehicle_class where name='S2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 26.40
from vehicle_class where name='S3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 30.60
from vehicle_class where name='S4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 33.75
from vehicle_class where name='S5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 35.85
from vehicle_class where name='S6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='S7ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.88
from vehicle_class where name='SP3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.12
from vehicle_class where name='SP7ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP8ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP9ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP10ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP11ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP12ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1007, 99, '2016-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.69
from vehicle_class where name='SP13ESTA';
----------------------
-- End of bug#3052
----------------------