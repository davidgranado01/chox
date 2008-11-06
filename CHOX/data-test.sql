
insert into country(id,name,currency,vat_rate) values (1000,'United Kingdom','GBP',17.50);

insert into insurer(id,name) values(1000,'RSA');
insert into insurer(id,name) values(1001,'Highway');
insert into insurer(id,name) values(1002,'RBS');
insert into insurer(id,name) values(1003,'Diamond');
insert into insurer(id,name) values(1004,'Norwich Union');
insert into insurer(id,name) values(1005,'Admiral');
insert into insurer(id,name) values(1006,'Direct Line');

insert into insurer_country(id,insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode)
values (1000,1000,1000,'RSA','c/o Trident Payment Services Ltd.','Suite 21','Cherry Orchard North','Kembrey Park','Swindon','SN2 8UH');

insert into insurer_country(id,insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode)
values (1001,1001,1000,'Highway','address1','address2','','','','');

insert into insurer_country(id,insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode)
values (1002,1002,1000,'RBS','address1','address2','','','','');

insert into insurer_country(id,insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode)
values (1003,1003,1000,'Diamond','address1','address2','','','','');

insert into insurer_country(id,insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode)
values (1004,1004,1000,'Norwich Union','address1','address2','','','','');

insert into insurer_country(id,insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode)
values (1005,1005,1000,'Admiral','address1','address2','','','','');

insert into insurer_country(id,insurer_id,country_id,name,address1,address2,address3,address4,address5,postcode)
values (1006,1006,1000,'Direct Line','address1','address2','','','','');

insert into insurer_country_location(insurer_country_id,name,catch_all) select id,'Default Location','y' from insurer_country;
insert into insurer_country_product(insurer_country_id,name,catch_all) select id,'Default Product','y' from insurer_country;




insert into supplier(id,name,address1,address2,address3,address4,address5,postcode,country_id,vat_no,company_no)
values (1000,'Drive Assist UK Ltd.','Tamworth House','Ventura Park','Tamworth','Staffordshire',null,'B78 3LY',1000,'TBA','2737288');

insert into supplier(id,name,address1,address2,address3,address4,address5,postcode,country_id,vat_no,company_no)
values (1001,'Accident Exchange Limited','Alpha 1','Canton Lane','Hams Hall','Coleshill','West Midlands','B46 1GA',1000,'TBA','04141140');

insert into supplier(id,name,address1,address2,address3,address4,address5,postcode,country_id,vat_no,company_no)
values (999,'Credit Hire Org','address1','address2','','','','',1000,'TBA','123456');

insert into web_user(id,email,firstnames,lastname,password) values(1000,'stu@idasnetwork.com','Stu','Coates','snom360');

insert into web_user_security(web_user_id,user_class) values(1000,'root');

insert into vehicle_class(code) values('S1');
insert into vehicle_class(code) values('S2');
insert into vehicle_class(code) values('S3');
insert into vehicle_class(code) values('S4');
insert into vehicle_class(code) values('S5');
insert into vehicle_class(code) values('S6');
insert into vehicle_class(code) values('S7');
insert into vehicle_class(code) values('M1');
insert into vehicle_class(code) values('M2');
insert into vehicle_class(code) values('M3');
insert into vehicle_class(code) values('M4');
insert into vehicle_class(code) values('M5');
insert into vehicle_class(code) values('M6');
insert into vehicle_class(code) values('F1');
insert into vehicle_class(code) values('F2');
insert into vehicle_class(code) values('F3');
insert into vehicle_class(code) values('F4');
insert into vehicle_class(code) values('F5');
insert into vehicle_class(code) values('F6');
insert into vehicle_class(code) values('F7');
insert into vehicle_class(code) values('F8');
insert into vehicle_class(code) values('F9');
insert into vehicle_class(code) values('P1');
insert into vehicle_class(code) values('P2');
insert into vehicle_class(code) values('P3');
insert into vehicle_class(code) values('P4');
insert into vehicle_class(code) values('P5');
insert into vehicle_class(code) values('P6');
insert into vehicle_class(code) values('P7');
insert into vehicle_class(code) values('P8');
insert into vehicle_class(code) values('P9');
insert into vehicle_class(code) values('P10');
insert into vehicle_class(code) values('P11');
insert into vehicle_class(code) values('P12');
insert into vehicle_class(code) values('SP1');
insert into vehicle_class(code) values('SP2');
insert into vehicle_class(code) values('SP3');
insert into vehicle_class(code) values('SP4');
insert into vehicle_class(code) values('SP5');
insert into vehicle_class(code) values('SP6');
insert into vehicle_class(code) values('SP7');
insert into vehicle_class(code) values('SP8');
insert into vehicle_class(code) values('SP9');
insert into vehicle_class(code) values('SP10');
insert into vehicle_class(code) values('SP11');
insert into vehicle_class(code) values('SP12');
insert into vehicle_class(code) values('SP13');
insert into vehicle_class(code) values('PV1');
insert into vehicle_class(code) values('PV2');
insert into vehicle_class(code) values('PV3');
insert into vehicle_class(code) values('PV4');
insert into vehicle_class(code) values('PV5');
insert into vehicle_class(code) values('PV6');
insert into vehicle_class(code) values('CV1');
insert into vehicle_class(code) values('CV2');
insert into vehicle_class(code) values('CV3');
insert into vehicle_class(code) values('CV4');
insert into vehicle_class(code) values('RV1');
insert into vehicle_class(code) values('RV2');
insert into vehicle_class(code) values('CP1');
insert into vehicle_class(code) values('CP2');
insert into vehicle_class(code) values('CP3');
insert into vehicle_class(code) values('CS1');
insert into vehicle_class(code) values('CS2');
insert into vehicle_class(code) values('CS3');
insert into vehicle_class(code) values('CS4');
insert into vehicle_class(code) values('CS5');
insert into vehicle_class(code) values('CM1');
insert into vehicle_class(code) values('CM2');
insert into vehicle_class(code) values('CM3');
insert into vehicle_class(code) values('T1');
insert into vehicle_class(code) values('T2');
insert into vehicle_class(code) values('T3');
insert into vehicle_class(code) values('T4');
insert into vehicle_class(code) values('T5');
insert into vehicle_class(code) values('T6');
insert into vehicle_class(code) values('T7');
insert into vehicle_class(code) values('T8');
insert into vehicle_class(code) values('T9');
insert into vehicle_class(code) values('T10');
insert into vehicle_class(code) values('T11');
insert into vehicle_class(code) values('T12');
insert into vehicle_class(code) values('T13');
insert into vehicle_class(code) values('T14');
insert into vehicle_class(code) values('B1');
insert into vehicle_class(code) values('B2');
insert into vehicle_class(code) values('B3');
insert into vehicle_class(code) values('B4');
insert into vehicle_class(code) values('B5');
insert into vehicle_class(code) values('B6');

insert into extra (id,code) values(1000,'CDW');
insert into extra (id,code) values(1001,'Automatic');
insert into extra (id,code) values(1002,'Sat Nav');
insert into extra (id,code) values(1003,'Estate');
insert into extra (id,code) values(1004,'Baby Seat');
insert into extra (id,code) values(1005,'Tow Bars');
insert into extra (id,code) values(1006,'Non-standard Risk Insurance Premium');
insert into extra (id,code) values(1007,'Roof Rack');
insert into extra (id,code) values(1008,'Delivery Collection');
insert into extra (id,code) values(1009,'Admin Fee');
insert into extra (id,code) values(1010,'Dual Control');
insert into extra (id,code) values(1011,'Admin');

insert into rental_extra_rate(insurer_country_id,supplier_id,extra_id,cost) select i.id,s.id,e.id,5.00 from insurer_country i,supplier s,extra e where e.id in (1000,1001,1002,1003,1004,1005,1006,1007,1008,1009,1011);
insert into rental_extra_rate(insurer_country_id,supplier_id,extra_id,cost) select i.id,s.id,e.id,12.00 from insurer_country i,supplier s,extra e where e.id in (1010);

insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,28.02 from insurer_country i,supplier s,vehicle_class c where c.code='S1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,31.77 from insurer_country i,supplier s,vehicle_class c where c.code='S2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,33.90 from insurer_country i,supplier s,vehicle_class c where c.code='S3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,36.33 from insurer_country i,supplier s,vehicle_class c where c.code='S4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,38.42 from insurer_country i,supplier s,vehicle_class c where c.code='S5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,40.95 from insurer_country i,supplier s,vehicle_class c where c.code='S6';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,57.44 from insurer_country i,supplier s,vehicle_class c where c.code='S7';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,51.75 from insurer_country i,supplier s,vehicle_class c where c.code='M1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,59.00 from insurer_country i,supplier s,vehicle_class c where c.code='M2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,69.35 from insurer_country i,supplier s,vehicle_class c where c.code='M3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,87.98 from insurer_country i,supplier s,vehicle_class c where c.code='M4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,131.96 from insurer_country i,supplier s,vehicle_class c where c.code='M5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,167.15 from insurer_country i,supplier s,vehicle_class c where c.code='M6';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,86.94 from insurer_country i,supplier s,vehicle_class c where c.code='F1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,93.15 from insurer_country i,supplier s,vehicle_class c where c.code='F2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,100.40 from insurer_country i,supplier s,vehicle_class c where c.code='F3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,123.17 from insurer_country i,supplier s,vehicle_class c where c.code='F4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,165.60 from insurer_country i,supplier s,vehicle_class c where c.code='F5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,186.30 from insurer_country i,supplier s,vehicle_class c where c.code='F6';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,217.35 from insurer_country i,supplier s,vehicle_class c where c.code='F7';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,232.88 from insurer_country i,supplier s,vehicle_class c where c.code='F8';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,284.63 from insurer_country i,supplier s,vehicle_class c where c.code='F9';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,72.45 from insurer_country i,supplier s,vehicle_class c where c.code='P1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,80.73 from insurer_country i,supplier s,vehicle_class c where c.code='P2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,85.91 from insurer_country i,supplier s,vehicle_class c where c.code='P3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,104.54 from insurer_country i,supplier s,vehicle_class c where c.code='P4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,130.41 from insurer_country i,supplier s,vehicle_class c where c.code='P5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,155.25 from insurer_country i,supplier s,vehicle_class c where c.code='P6';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,181.13 from insurer_country i,supplier s,vehicle_class c where c.code='P7';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,207.00 from insurer_country i,supplier s,vehicle_class c where c.code='P8';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,238.05 from insurer_country i,supplier s,vehicle_class c where c.code='P9';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,292.91 from insurer_country i,supplier s,vehicle_class c where c.code='P10';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,411.41 from insurer_country i,supplier s,vehicle_class c where c.code='P11';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,615.83 from insurer_country i,supplier s,vehicle_class c where c.code='P12';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,69.74 from insurer_country i,supplier s,vehicle_class c where c.code='SP1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,81.51 from insurer_country i,supplier s,vehicle_class c where c.code='SP2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,91.08 from insurer_country i,supplier s,vehicle_class c where c.code='SP3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,111.78 from insurer_country i,supplier s,vehicle_class c where c.code='SP4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,122.13 from insurer_country i,supplier s,vehicle_class c where c.code='SP5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,170.78 from insurer_country i,supplier s,vehicle_class c where c.code='SP6';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,191.48 from insurer_country i,supplier s,vehicle_class c where c.code='SP7';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,212.18 from insurer_country i,supplier s,vehicle_class c where c.code='SP8';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,232.88 from insurer_country i,supplier s,vehicle_class c where c.code='SP9';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,266.51 from insurer_country i,supplier s,vehicle_class c where c.code='SP10';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,320.85 from insurer_country i,supplier s,vehicle_class c where c.code='SP11';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,421.76 from insurer_country i,supplier s,vehicle_class c where c.code='SP12';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,615.83 from insurer_country i,supplier s,vehicle_class c where c.code='SP13';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,33.87 from insurer_country i,supplier s,vehicle_class c where c.code='PV1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,39.30 from insurer_country i,supplier s,vehicle_class c where c.code='PV2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,39.30 from insurer_country i,supplier s,vehicle_class c where c.code='PV3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,43.57 from insurer_country i,supplier s,vehicle_class c where c.code='PV4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,45.78 from insurer_country i,supplier s,vehicle_class c where c.code='PV5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,47.97 from insurer_country i,supplier s,vehicle_class c where c.code='PV6';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,48.97 from insurer_country i,supplier s,vehicle_class c where c.code='CV1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,50.26 from insurer_country i,supplier s,vehicle_class c where c.code='CV2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,52.83 from insurer_country i,supplier s,vehicle_class c where c.code='CV3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,75.27 from insurer_country i,supplier s,vehicle_class c where c.code='CV4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,65.46 from insurer_country i,supplier s,vehicle_class c where c.code='RV1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,90.51 from insurer_country i,supplier s,vehicle_class c where c.code='RV2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,46.06 from insurer_country i,supplier s,vehicle_class c where c.code='CP1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,52.27 from insurer_country i,supplier s,vehicle_class c where c.code='CP2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,59.51 from insurer_country i,supplier s,vehicle_class c where c.code='CP3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,54.08 from insurer_country i,supplier s,vehicle_class c where c.code='CS1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,57.84 from insurer_country i,supplier s,vehicle_class c where c.code='CS2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,62.10 from insurer_country i,supplier s,vehicle_class c where c.code='CS3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,65.86 from insurer_country i,supplier s,vehicle_class c where c.code='CS4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,70.12 from insurer_country i,supplier s,vehicle_class c where c.code='CS5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,59.91 from insurer_country i,supplier s,vehicle_class c where c.code='CM1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,69.31 from insurer_country i,supplier s,vehicle_class c where c.code='CM2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,78.50 from insurer_country i,supplier s,vehicle_class c where c.code='CM3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,62.10 from insurer_country i,supplier s,vehicle_class c where c.code='T1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,82.80 from insurer_country i,supplier s,vehicle_class c where c.code='T2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,77.63 from insurer_country i,supplier s,vehicle_class c where c.code='T3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,98.33 from insurer_country i,supplier s,vehicle_class c where c.code='T4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,70.38 from insurer_country i,supplier s,vehicle_class c where c.code='T5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,95.22 from insurer_country i,supplier s,vehicle_class c where c.code='T6';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,80.73 from insurer_country i,supplier s,vehicle_class c where c.code='T7';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,105.57 from insurer_country i,supplier s,vehicle_class c where c.code='T8';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,207.00 from insurer_country i,supplier s,vehicle_class c where c.code='T9';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,155.25 from insurer_country i,supplier s,vehicle_class c where c.code='T10';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,181.13 from insurer_country i,supplier s,vehicle_class c where c.code='T12';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,294.98 from insurer_country i,supplier s,vehicle_class c where c.code='T13';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,74.52 from insurer_country i,supplier s,vehicle_class c where c.code='T14';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,30.79 from insurer_country i,supplier s,vehicle_class c where c.code='B1';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,35.97 from insurer_country i,supplier s,vehicle_class c where c.code='B2';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,50.72 from insurer_country i,supplier s,vehicle_class c where c.code='B3';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,61.07 from insurer_country i,supplier s,vehicle_class c where c.code='B4';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,70.38 from insurer_country i,supplier s,vehicle_class c where c.code='B5';
insert into rental_rate(insurer_country_id,supplier_id,vehicle_class_id,cost) select i.id,s.id,c.id,72.45 from insurer_country i,supplier s,vehicle_class c where c.code='B6';

insert into search_fields(fieldname) values('supplier_reference');
insert into search_fields(fieldname) values('supplier_id');
insert into search_fields(fieldname) values('first_contact');
insert into search_fields(fieldname) values('customer_policy_number');
insert into search_fields(fieldname) values('customer_claim_reference');
insert into search_fields(fieldname) values('customer_name');
insert into search_fields(fieldname) values('customer_vrn');
insert into search_fields(fieldname) values('customer_manufacturer');
insert into search_fields(fieldname) values('customer_model');
insert into search_fields(fieldname) values('customer_class_id');
insert into search_fields(fieldname) values('customer_insurer_id');
insert into search_fields(fieldname) values('tp_policy_number');
insert into search_fields(fieldname) values('tp_claim_reference');
insert into search_fields(fieldname) values('tp_name');
insert into search_fields(fieldname) values('tp_vrn');
insert into search_fields(fieldname) values('tp_manufacturer');
insert into search_fields(fieldname) values('tp_model');
insert into search_fields(fieldname) values('tp_class_id');
insert into search_fields(fieldname) values('tp_insurer_id');
insert into search_fields(fieldname) values('incident_date');
insert into search_fields(fieldname) values('proposed_rental_class_id');
insert into search_fields(fieldname) values('driver_name');
insert into search_fields(fieldname) values('rental_vehicle_registration');
insert into search_fields(fieldname) values('rental_vehicle_manufacturer');
insert into search_fields(fieldname) values('rental_vehicle_model');
insert into search_fields(fieldname) values('engineer_name');
insert into search_fields(fieldname) values('engineer_labour_amount');
insert into search_fields(fieldname) values('engineer_days');
insert into search_fields(fieldname) values('invoice_net');
insert into search_fields(fieldname) values('invoice_vat');
insert into search_fields(fieldname) values('invoice_gross');
insert into search_fields(fieldname) values('authorisation_status');


insert into decision_making(decision_type,supplier_id,insurer_country_id,java_class)
select 'invoice pay'::varchar,s.id,i.id,'chox.decision.PaySimple'::varchar from supplier s,insurer_country i;

insert into decision_making_parameter(decision_id,name,numeric_value)
select d.id,'allowed extra rental days'::varchar,5 from decision_making d;

insert into decision_making_parameter(decision_id,name,numeric_value)
select d.id,'allowed net amount percentage uplift'::varchar,10 from decision_making d;

insert into claim_handling_rate(insurer_country_id,supplier_id,net_amount,vat_amount,gross_amount)
select i.id,s.id,100.00,0.00,100.00 from insurer_country i,supplier s;




