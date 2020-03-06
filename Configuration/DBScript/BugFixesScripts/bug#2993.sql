-----------------------------------------------------------------------
-- bug#2993 - Production - Add supplier rates between ERAC and QBE   --
-----------------------------------------------------------------------

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 60.53 from vehicle_class where name='CM1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 65.03 from vehicle_class where name='CM1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 70.03 from vehicle_class where name='CM2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.53 from vehicle_class where name='CM2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 79.32 from vehicle_class where name='CM3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 83.82 from vehicle_class where name='CM3A';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 46.55 from vehicle_class where name='CP1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 51.05 from vehicle_class where name='CP1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 52.81 from vehicle_class where name='CP2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 57.31 from vehicle_class where name='CP2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 60.13 from vehicle_class where name='CP3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 64.63 from vehicle_class where name='CP3A';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 54.63 from vehicle_class where name='CS1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 59.13 from vehicle_class where name='CS1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 58.43 from vehicle_class where name='CS2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 62.93 from vehicle_class where name='CS2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 62.75 from vehicle_class where name='CS3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.25 from vehicle_class where name='CS3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 66.53 from vehicle_class where name='CS4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 71.03 from vehicle_class where name='CS4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 70.85 from vehicle_class where name='CS5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 75.35 from vehicle_class where name='CS5A';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 49.48 from vehicle_class where name='CV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 53.98 from vehicle_class where name='CV1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.77 from vehicle_class where name='CV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 55.27 from vehicle_class where name='CV2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 53.37 from vehicle_class where name='CV3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 57.87 from vehicle_class where name='CV3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 76.04 from vehicle_class where name='CV4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 80.54 from vehicle_class where name='CV4A';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 87.83 from vehicle_class where name='F1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 92.33 from vehicle_class where name='F1A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 92.33 from vehicle_class where name='F1EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 96.83 from vehicle_class where name='F1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 94.11 from vehicle_class where name='F2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 98.61 from vehicle_class where name='F2A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 98.61 from vehicle_class where name='F2EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 103.11 from vehicle_class where name='F2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 101.44 from vehicle_class where name='F3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 105.94 from vehicle_class where name='F3A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 105.94 from vehicle_class where name='F3EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 110.44 from vehicle_class where name='F3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 124.44 from vehicle_class where name='F4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 128.94 from vehicle_class where name='F4A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 128.94 from vehicle_class where name='F4EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 133.44 from vehicle_class where name='F4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 167.30 from vehicle_class where name='F5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 171.80 from vehicle_class where name='F5A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 171.80 from vehicle_class where name='F5EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 176.30 from vehicle_class where name='F5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 188.23 from vehicle_class where name='F6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 192.73 from vehicle_class where name='F6A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 192.73 from vehicle_class where name='F6EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 197.23 from vehicle_class where name='F6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 219.59 from vehicle_class where name='F7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 224.09 from vehicle_class where name='F7A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 224.09 from vehicle_class where name='F7EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 228.59 from vehicle_class where name='F7ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 235.28 from vehicle_class where name='F8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 239.78 from vehicle_class where name='F8A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 239.78 from vehicle_class where name='F8EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 244.28 from vehicle_class where name='F8ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 287.56 from vehicle_class where name='F9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 292.06 from vehicle_class where name='F9A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 292.06 from vehicle_class where name='F9EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 296.56 from vehicle_class where name='F9ESTA';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 45.23 from vehicle_class where name='M';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 52.28 from vehicle_class where name='M1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.78 from vehicle_class where name='M1A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 56.78 from vehicle_class where name='M1EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 61.28 from vehicle_class where name='M1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 59.61 from vehicle_class where name='M2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 64.11 from vehicle_class where name='M2A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 64.11 from vehicle_class where name='M2EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 68.61 from vehicle_class where name='M2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 70.07 from vehicle_class where name='M3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.57 from vehicle_class where name='M3A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.57 from vehicle_class where name='M3EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 79.07 from vehicle_class where name='M3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 88.88 from vehicle_class where name='M4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 93.38 from vehicle_class where name='M4A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 93.38 from vehicle_class where name='M4EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 97.88 from vehicle_class where name='M4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 133.32 from vehicle_class where name='M5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 137.82 from vehicle_class where name='M5A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 137.82 from vehicle_class where name='M5EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 142.32 from vehicle_class where name='M5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 168.88 from vehicle_class where name='M6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 173.38 from vehicle_class where name='M6A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 173.38 from vehicle_class where name='M6EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 177.88 from vehicle_class where name='M6ESTA';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 49.73 from vehicle_class where name='MA';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 49.73 from vehicle_class where name='MEST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 54.23 from vehicle_class where name='MESTA';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 73.20 from vehicle_class where name='P1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 77.70 from vehicle_class where name='P1A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 77.70 from vehicle_class where name='P1EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 82.20 from vehicle_class where name='P1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 81.57 from vehicle_class where name='P2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 86.07 from vehicle_class where name='P2A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 86.07 from vehicle_class where name='P2EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 90.57 from vehicle_class where name='P2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 86.79 from vehicle_class where name='P3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 91.29 from vehicle_class where name='P3A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 91.29 from vehicle_class where name='P3EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 95.79 from vehicle_class where name='P3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 105.61 from vehicle_class where name='P4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 110.11 from vehicle_class where name='P4A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 110.11 from vehicle_class where name='P4EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 114.61 from vehicle_class where name='P4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 131.76 from vehicle_class where name='P5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 136.26 from vehicle_class where name='P5A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 136.26 from vehicle_class where name='P5EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 140.76 from vehicle_class where name='P5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 156.86 from vehicle_class where name='P6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 161.36 from vehicle_class where name='P6A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 161.36 from vehicle_class where name='P6EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 165.86 from vehicle_class where name='P6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 182.99 from vehicle_class where name='P7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 187.49 from vehicle_class where name='P7A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 187.49 from vehicle_class where name='P7EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 191.99 from vehicle_class where name='P7ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 209.12 from vehicle_class where name='P8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 213.62 from vehicle_class where name='P8A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 213.62 from vehicle_class where name='P8EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 218.12 from vehicle_class where name='P8ESTA';

    insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                                 created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 240.50 from vehicle_class where name='P9';
    insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                                 created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 245.00 from vehicle_class where name='P9A';
    insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                                 created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 245.00 from vehicle_class where name='P9EST';
    insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                                 created_by, last_modified_by, created_date, last_modified_date, price)
        select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 249.50 from vehicle_class where name='P9ESTA';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 295.93 from vehicle_class where name='P10';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 300.43 from vehicle_class where name='P10A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 300.43 from vehicle_class where name='P10EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 304.93 from vehicle_class where name='P10ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 415.65 from vehicle_class where name='P11';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 420.15 from vehicle_class where name='P11A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 420.15 from vehicle_class where name='P11EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 424.65 from vehicle_class where name='P11ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 622.18 from vehicle_class where name='P12';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 626.68 from vehicle_class where name='P12A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 626.68 from vehicle_class where name='P12EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 631.18 from vehicle_class where name='P12ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 902.15 from vehicle_class where name='P13';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 906.65 from vehicle_class where name='P13A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 906.65 from vehicle_class where name='P13EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 911.15 from vehicle_class where name='P13ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 34.21 from vehicle_class where name='PV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 38.71 from vehicle_class where name='PV1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.70 from vehicle_class where name='PV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 44.20 from vehicle_class where name='PV2A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 39.70 from vehicle_class where name='PV3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 44.20 from vehicle_class where name='PV3A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 44.02 from vehicle_class where name='PV4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 48.52 from vehicle_class where name='PV4A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 46.25 from vehicle_class where name='PV5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.75 from vehicle_class where name='PV5A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 48.47 from vehicle_class where name='PV6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 52.97 from vehicle_class where name='PV6A';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 66.11 from vehicle_class where name='RV1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 70.61 from vehicle_class where name='RV1A';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 91.44 from vehicle_class where name='RV2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 95.94 from vehicle_class where name='RV2A';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 28.31 from vehicle_class where name='S1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 32.81 from vehicle_class where name='S1A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 32.81 from vehicle_class where name='S1EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 37.31 from vehicle_class where name='S1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 32.10 from vehicle_class where name='S2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 36.60 from vehicle_class where name='S2A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 36.60 from vehicle_class where name='S2EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 41.10 from vehicle_class where name='S2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 34.24 from vehicle_class where name='S3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 38.74 from vehicle_class where name='S3A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 38.74 from vehicle_class where name='S3EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 43.24 from vehicle_class where name='S3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 36.71 from vehicle_class where name='S4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 41.21 from vehicle_class where name='S4A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 41.21 from vehicle_class where name='S4EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 45.71 from vehicle_class where name='S4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 38.84 from vehicle_class where name='S5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 43.34 from vehicle_class where name='S5A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 43.34 from vehicle_class where name='S5EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 47.84 from vehicle_class where name='S5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 41.38 from vehicle_class where name='S6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 45.88 from vehicle_class where name='S6A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 45.88 from vehicle_class where name='S6EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 50.38 from vehicle_class where name='S6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 58.02 from vehicle_class where name='S7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 62.52 from vehicle_class where name='S7A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 62.52 from vehicle_class where name='S7EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 67.02 from vehicle_class where name='S7ESTA';


insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 70.46 from vehicle_class where name='SP1';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.96 from vehicle_class where name='SP1A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 74.96 from vehicle_class where name='SP1EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 79.46 from vehicle_class where name='SP1ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 82.35 from vehicle_class where name='SP2';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 86.85 from vehicle_class where name='SP2A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 86.85 from vehicle_class where name='SP2EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 91.35 from vehicle_class where name='SP2ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 92.02 from vehicle_class where name='SP3';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 96.52 from vehicle_class where name='SP3A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 96.52 from vehicle_class where name='SP3EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 101.02 from vehicle_class where name='SP3ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 112.94 from vehicle_class where name='SP4';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 117.44 from vehicle_class where name='SP4A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 117.44 from vehicle_class where name='SP4EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 121.94 from vehicle_class where name='SP4ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 123.39 from vehicle_class where name='SP5';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 127.89 from vehicle_class where name='SP5A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 127.89 from vehicle_class where name='SP5EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 132.39 from vehicle_class where name='SP5ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 172.54 from vehicle_class where name='SP6';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 177.04 from vehicle_class where name='SP6A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 177.04 from vehicle_class where name='SP6EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 181.54 from vehicle_class where name='SP6ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 193.46 from vehicle_class where name='SP7';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 197.96 from vehicle_class where name='SP7A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 197.96 from vehicle_class where name='SP7EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 202.46 from vehicle_class where name='SP7ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 214.37 from vehicle_class where name='SP8';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 218.87 from vehicle_class where name='SP8A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 218.87 from vehicle_class where name='SP8EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 223.37 from vehicle_class where name='SP8ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 235.28 from vehicle_class where name='SP9';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 239.78 from vehicle_class where name='SP9A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 239.78 from vehicle_class where name='SP9EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 244.28 from vehicle_class where name='SP9ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 269.25 from vehicle_class where name='SP10';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 273.75 from vehicle_class where name='SP10A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 273.75 from vehicle_class where name='SP10EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 278.25 from vehicle_class where name='SP10ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 324.15 from vehicle_class where name='SP11';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 328.65 from vehicle_class where name='SP11A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 328.65 from vehicle_class where name='SP11EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 333.15 from vehicle_class where name='SP11ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 426.12 from vehicle_class where name='SP12';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 430.62 from vehicle_class where name='SP12A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 430.62 from vehicle_class where name='SP12EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 435.12 from vehicle_class where name='SP12ESTA';

insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 622.18 from vehicle_class where name='SP13';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 626.68 from vehicle_class where name='SP13A';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 626.68 from vehicle_class where name='SP13EST';
insert into vehicle_class_price_special_rate(version, insurer_id, chorganisation_id, age, start_date, vehicle_class_id,
                                             created_by, last_modified_by, created_date, last_modified_date, price)
    select 0, 25, 1007, 99, '2014-07-01'::timestamp without time zone, id, 999, 999, now(), now(), 631.18 from vehicle_class where name='SP13ESTA';

