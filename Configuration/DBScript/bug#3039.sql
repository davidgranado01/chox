--------------------------------------------------------------------------------
-- bug#3039 - Production - Daily Rates for LV vs Quindell BPS Debt
--------------------------------------------------------------------------------
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 15.17
from vehicle_class where name='S1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 15.75
from vehicle_class where name='S2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 17.48
from vehicle_class where name='S3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 18.41
from vehicle_class where name='S4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.38
from vehicle_class where name='S5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 22.25
from vehicle_class where name='S6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 20.30
from vehicle_class where name='S7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 23.49
from vehicle_class where name='M1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 20.87
from vehicle_class where name='M2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 41.84
from vehicle_class where name='M3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 49.55
from vehicle_class where name='M4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 94.10
from vehicle_class where name='M5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 116.30
from vehicle_class where name='M6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 44.25
from vehicle_class where name='F1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 45.01
from vehicle_class where name='F2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 61.85
from vehicle_class where name='F3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.41
from vehicle_class where name='F4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 94.13
from vehicle_class where name='F5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 94.63
from vehicle_class where name='F6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 119.43
from vehicle_class where name='F7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 114.10
from vehicle_class where name='F8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 124.10
from vehicle_class where name='F9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 21.79
from vehicle_class where name='P1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 24.57
from vehicle_class where name='P2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 31.58
from vehicle_class where name='P3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 36.56
from vehicle_class where name='P4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 36.56
from vehicle_class where name='P5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.66
from vehicle_class where name='P6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.66
from vehicle_class where name='P7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 119.06
from vehicle_class where name='P8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 119.06
from vehicle_class where name='P9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 104.10
from vehicle_class where name='P10';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 412.10
from vehicle_class where name='P11';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 463.10
from vehicle_class where name='P12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 604.10
from vehicle_class where name='P13';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 54.51
from vehicle_class where name='SP1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 60.00
from vehicle_class where name='SP2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 88.58
from vehicle_class where name='SP3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 44.10
from vehicle_class where name='SP4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 59.10
from vehicle_class where name='SP5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 91.28
from vehicle_class where name='SP6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 92.05
from vehicle_class where name='SP7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 92.82
from vehicle_class where name='SP8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 99.10
from vehicle_class where name='SP9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 104.10
from vehicle_class where name='SP10';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 109.10
from vehicle_class where name='SP11';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 119.10
from vehicle_class where name='SP12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 412.10
from vehicle_class where name='SP13';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 19.07
from vehicle_class where name='PV1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 20.35
from vehicle_class where name='PV2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 20.35
from vehicle_class where name='PV3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 24.87
from vehicle_class where name='PV4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 37.43
from vehicle_class where name='PV5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.46
from vehicle_class where name='PV6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 38.95
from vehicle_class where name='CV1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.46
from vehicle_class where name='CV2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 47.60
from vehicle_class where name='CV3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 69.00
from vehicle_class where name='CV4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 72.73
from vehicle_class where name='RV1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 101.36
from vehicle_class where name='RV2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 46.64
from vehicle_class where name='CP1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 48.79
from vehicle_class where name='CP2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.77
from vehicle_class where name='CP3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 49.05
from vehicle_class where name='CS1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 52.33
from vehicle_class where name='CS2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 53.59
from vehicle_class where name='CS3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 59.40
from vehicle_class where name='CS4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 63.44
from vehicle_class where name='CS5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.95
from vehicle_class where name='CM1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.95
from vehicle_class where name='CM2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.95
from vehicle_class where name='CM3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 20.94
from vehicle_class where name='B1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 22.40
from vehicle_class where name='B2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 27.10
from vehicle_class where name='B3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 30.60
from vehicle_class where name='B4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.00
from vehicle_class where name='B5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 40.10
from vehicle_class where name='B6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 65.76
from vehicle_class where name='T1';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 76.32
from vehicle_class where name='T2';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 65.76
from vehicle_class where name='T3';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 59.10
from vehicle_class where name='T4';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 71.01
from vehicle_class where name='T5';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 95.00
from vehicle_class where name='T6';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 96.10
from vehicle_class where name='T7';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.10
from vehicle_class where name='T8';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 119.10
from vehicle_class where name='T9';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 79.10
from vehicle_class where name='T10';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 94.10
from vehicle_class where name='T12';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id, created_by, last_modified_by, created_date, last_modified_date, price)
select 0, 26, 1028, 99, '2010-01-01'::timestamp without time zone, id, 999, 999, now(), now(), 179.10
from vehicle_class where name='T13';

----------------------
-- End of bug#3039
----------------------