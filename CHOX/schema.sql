
------------------------------------------------------------------------------

create sequence pk_country start with 1000000;

create table country (
  id               integer not null primary key default nextval('pk_country'),
  name             varchar(128) not null unique,
  currency         char(3) not null,
  vat_rate         numeric not null
);

------------------------------------------------------------------------------

create sequence pk_insurer start with 1000000;

create table insurer (
  id               integer not null primary key default nextval('pk_insurer'),
  name             varchar(128) not null unique
);

------------------------------------------------------------------------------

create sequence pk_insurer_country start with 1000000;

create table insurer_country (
  id               integer not null primary key default nextval('pk_insurer_country'),
  insurer_id       integer not null references insurer(id),
  country_id       integer not null references country(id),
  name             varchar(128) not null,
  address1         varchar(128) not null,
  address2         varchar(128) not null,
  address3         varchar(128),
  address4         varchar(128),
  address5         varchar(128),
  postcode         varchar(128) not null
);

create index insurer_country_i1 on insurer_country(insurer_id,country_id);

------------------------------------------------------------------------------

create sequence pk_insurer_country_alias start with 1000000;

create table insurer_country_alias (
  id               integer not null primary key default nextval('pk_insurer_country_alias'),
  insurer_country_id  integer not null references insurer_country(id),
  alias            varchar(128) not null
);

------------------------------------------------------------------------------

create sequence pk_insurer_country_location start with 1000000;

create table insurer_country_location (
  id               integer not null primary key default nextval('pk_insurer_country_location'),
  insurer_country_id integer not null references insurer_country(id),
  name             varchar(64) not null,
  catch_all        char(1) not null check(catch_all in ('y','n'))
);

------------------------------------------------------------------------------

create sequence pk_insurer_country_product start with 1000000;

create table insurer_country_product (
  id               integer not null primary key default nextval('pk_insurer_country_product'),
  insurer_country_id integer not null references insurer_country(id),
  name             varchar(64) not null,
  catch_all        char(1) not null check(catch_all in ('y','n'))
);

------------------------------------------------------------------------------

create sequence pk_supplier start with 1000000;

create table supplier (
  id               integer not null primary key default nextval('pk_supplier'),
  name             varchar(128) not null unique,
  address1         varchar(128) not null,
  address2         varchar(128) not null,
  address3         varchar(128),
  address4         varchar(128),
  address5         varchar(128),
  postcode         varchar(128) not null,
  country_id       integer not null references country(id),
  vat_no           varchar(32) not null,
  company_no       varchar(32) not null
);

create index supplier_i1 on supplier(name);

------------------------------------------------------------------------------

create sequence pk_supplier_alias start with 1000000;

create table supplier_alias (
  id               integer not null primary key default nextval('pk_supplier_alias'),
  supplier_id      integer not null references supplier(id),
  alias            varchar(128) not null
);

------------------------------------------------------------------------------

create sequence pk_web_user start with 1000000;

create table web_user (
  id               integer not null primary key default nextval('pk_web_user'),
  uuid             varchar(64) not null unique,
  email            varchar(256) not null unique,
  firstnames       varchar(256) not null,
  lastname         varchar(256) not null,
  password         varchar(256) not null,
  status_id        integer not null default 1 check (status_id in (1,2))
);

create unique index web_user_i1 on web_user(email);
create unique index web_user_i2 on web_user(email,password);

insert into web_user(id,email,firstnames,lastname,password,status_id,uuid) values(999,'admin@idasnetwork.com','System','System','default password',2,'2osg5UM66rycnZggDrOe1WzSeQnFkdf7rE3l5DgIXZ80VfVyPPbg7GjAJvTKVrcZ');

------------------------------------------------------------------------------

create sequence pk_web_user_session start with 1000000;

create table web_user_session (
  id                integer not null primary key default nextval('pk_web_user_session'),
  session_key       varchar(32) not null unique,
  web_user_id       integer not null references web_user(id),
  client_ip         varchar(256) not null,
  created           timestamp not null default current_timestamp,
  last_used         timestamp not null default current_timestamp,
  status_id         integer not null default 1 check(status_id in (1,2))
);

create index web_user_session_i1 on web_user_session(session_key,status_id);
create index web_user_session_i2 on web_user_session(web_user_id);

insert into web_user_session(id,session_key,web_user_id,client_ip) values(999,'',999,'127.0.0.1');

------------------------------------------------------------------------------

create sequence pk_web_user_security start with 1000000;

create table web_user_security (
  id               integer not null primary key default nextval('pk_web_user_security'),
  web_user_id      integer not null references web_user(id),
  user_class       varchar(16) not null check(user_class in ('root','admin','power user','user')),
  insurer_country_id integer references insurer_country(id),
  insurer_country_location_id integer references insurer_country_location(id),
  insurer_country_product_id integer references insurer_country_product(id),
  supplier_id integer references supplier(id),
  status_id        integer not null default 1 check(status_id in (1,2))
);

create index web_user_security_i1 on web_user_security(web_user_id);
create index web_user_security_i2 on web_user_security(supplier_id);
create index web_user_security_i3 on web_user_security(insurer_country_id);
create index web_user_security_i4 on web_user_security(insurer_country_location_id);
create index web_user_security_i5 on web_user_security(insurer_country_product_id);

------------------------------------------------------------------------------

create or replace view web_session_security as
select distinct
ses.id as session_id,
ses.session_key,
sec.user_class,
sec.insurer_country_id,
sec.insurer_country_location_id,
sec.insurer_country_product_id,
sec.supplier_id
from web_user_session ses,
web_user_security sec,
web_user u
where 
ses.web_user_id=sec.web_user_id
and sec.web_user_id=u.id
and ses.status_id=1
and sec.status_id=1
and u.status_id=1

union

select distinct ses.id as session_id,ses.session_key,sec.user_class,ic.id as insurer_country_id,null::integer as insurer_country_location_id,null::integer as insurer_country_product_id,null::integer as supplier_id
from web_user_session ses,web_user_security sec,insurer_country ic,web_user u
where ses.web_user_id=sec.web_user_id
and sec.web_user_id=u.id
and sec.user_class='root'
and ses.status_id=1
and sec.status_id=1
and u.status_id=1

union

select distinct ses.id as session_id,ses.session_key,sec.user_class,null::integer as insurer_country_id,null::integer as insurer_country_location_id,null::integer as insurer_country_product_id, s.id as supplier_id
from web_user_session ses,web_user_security sec,supplier s,web_user u
where ses.web_user_id=sec.web_user_id
and sec.web_user_id=u.id
and sec.user_class='root'
and ses.status_id=1
and sec.status_id=1
and u.status_id=1
;

------------------------------------------------------------------------------

create sequence pk_message start with 1000000;

create table message (
  id                    integer not null primary key default nextval('pk_message'),
  message               text not null,
  received              timestamp not null default current_timestamp,
  session_id            integer references web_user_session(id),
  status_id             integer not null default 3
);

------------------------------------------------------------------------------

create sequence pk_vehicle_class start with 1000000;

create table vehicle_class (
  id                    integer not null primary key default nextval('pk_vehicle_class'),
  code                  varchar(4) not null unique
);

create unique index vehicle_class_i1 on vehicle_class(code);

------------------------------------------------------------------------------

create sequence pk_rental start with 1000000;

create table rental (
  id                    integer not null primary key default nextval('pk_rental'),
  uuid                  varchar(64) not null unique,
  supplier_id           integer not null references supplier(id),
  supplier_reference    varchar(128) not null unique,
  rental_status         varchar(32) not null default 'Pending' check(rental_status in ('Pending','InProgress','Complete','Cancelled')),
  first_contact         timestamp not null default current_timestamp,
  created               timestamp not null default current_timestamp
);

create unique index rental_i1 on rental(supplier_id,supplier_reference);
create unique index rental_i2 on rental(uuid);

------------------------------------------------------------------------------

create sequence pk_invoice start with 1000000;

create table invoice (
  id                    integer not null primary key default nextval('pk_invoice'),
  rental_id             integer not null references rental(id) unique,
  net                   numeric not null,
  vat                   numeric not null,
  gross                 numeric not null
);

create unique index invoice_i1 on invoice(rental_id);

------------------------------------------------------------------------------

create sequence pk_rental_message start with 1000000;

create table rental_message (
  id                    integer not null primary key default nextval('pk_rental_message'),
  rental_id             integer not null references rental(id),
  message_id            integer not null references message(id)
);

create index rental_message_i1 on rental_message(rental_id);

------------------------------------------------------------------------------

create sequence pk_rental_note start with 1000000;

create table rental_note (
  id                    integer not null primary key default nextval('pk_rental_note'),
  uuid                  varchar(64) not null unique,
  rental_id             integer not null references rental(id),
  note                  text not null,
  created               timestamp not null default current_timestamp,
  file_name             varchar(1024),
  file_mimetype         varchar(128),
  file_blob             bytea,
  session_id            integer references web_user_session(id)
);

create index rental_note_i1 on rental_note(session_id);
create index rental_note_i2 on rental_note(rental_id);
create unique index rental_note_i3 on rental_note(uuid);

------------------------------------------------------------------------------

create sequence pk_rental_vehicle start with 1000000;

create table rental_vehicle (
  id                    integer not null primary key default nextval('pk_rental_vehicle'),
  uuid                  varchar(64) not null unique,
  rental_id             integer not null references rental(id),
  vehicle_registration  varchar(16) not null,
  vehicle_manufacturer  varchar(32) not null,
  vehicle_model         varchar(64) not null,
  vehicle_class_id      integer not null references vehicle_class(id),
  rental_start          timestamp not null,
  rental_end            timestamp,
  days                  numeric
);

create index rental_vehicle_i1 on rental_vehicle(rental_id);
create unique index rental_vehicle_i2 on rental_vehicle(uuid);

------------------------------------------------------------------------------

create sequence pk_driver start with 1000000;

create table driver (
  id                    integer not null primary key default nextval('pk_driver'),
  uuid                  varchar(64) not null unique,
  title                 varchar(8) not null check(title in ('Mr','Mrs','Ms','Miss','Dr','Rev','Lord')),
  firstnames            varchar(128) not null,
  lastname              varchar(64) not null,
  address1              varchar(128),
  address2              varchar(128),
  address3              varchar(128),
  address4              varchar(128),
  address5              varchar(128),
  postcode              varchar(16),
  telephone_day         varchar(16),
  telephone_evening     varchar(16),
  email                 varchar(64),
  primary_driver        char(1) not null default 'y' check(primary_driver in ('y','n')),
  rental_id             integer not null references rental(id)
);

create index driver_i1 on driver(rental_id);
create unique index driver_i2 on driver(uuid);

------------------------------------------------------------------------------

create sequence pk_claim start with 1000000;

create table claim (
  id                    integer not null primary key default nextval('pk_claim'),
  uuid                  varchar(64) not null unique,
  rental_id             integer not null references rental(id),

  insurer_country_id    integer references insurer_country(id),
  policy_number         varchar(32),
  claim_reference       varchar(64),
  comprehensive         char(1) default 'y' check (comprehensive in ('y','n')),
  policy_holder_name    varchar(128) not null,

  vehicle_registration  varchar(16) not null,
  vehicle_manufacturer  varchar(32) not null,
  vehicle_model         varchar(64) not null,
  vehicle_class_id      integer not null references vehicle_class(id),

  usable                char(1) not null default 'n' check(usable in ('y','n')),

  damage_description    text,

  tp_insurer_country_id integer not null references insurer_country(id),
  tp_policy_number      varchar(32),
  tp_claim_reference    varchar(64),
  
  tp_vehicle_registration varchar(16),
  tp_vehicle_manufacturer varchar(32),
  tp_vehicle_model        varchar(64),
  tp_vehicle_class_id     integer references vehicle_class(id),

  tp_name               varchar(128),
  tp_address1           varchar(128),
  tp_address2           varchar(128),
  tp_address3           varchar(128),
  tp_address4           varchar(128),
  tp_address5           varchar(128),
  tp_postcode           varchar(16),
  tp_telephone_day      varchar(16),
  tp_telephone_evening  varchar(16),
  tp_email              varchar(64),

  incident_date         timestamp not null,
  location              text,
  police_involved       char(1) not null default 'n' check(police_involved in ('y','n')),
  incident_description  text,

  vehicle_location      text,

  proposed_rental_class_id integer references vehicle_class(id),

  claim_status          varchar(32) not null default 'Awaiting Authorization' check(claim_status in ('Awaiting Authorization','Authorized'))
);

create unique index claim_i1 on claim(rental_id);
create index claim_i2 on claim(insurer_country_id);
create index claim_i3 on claim(tp_insurer_country_id);
create index claim_i4 on claim(tp_policy_number);
create index claim_i5 on claim(tp_claim_reference);
create index claim_i6 on claim(tp_vehicle_registration);
create index claim_i7 on claim(vehicle_registration);
create unique index claim_i8 on claim(uuid);


------------------------------------------------------------------------------

create sequence pk_witness start with 1000000;

create table witness (
  id                    integer not null primary key default nextval('pk_witness'),
  uuid                  varchar(64) not null unique,
  claim_id              integer not null references claim(id),
  name                  varchar(128),
  address1              varchar(128),
  address2              varchar(128),
  address3              varchar(128),
  address4              varchar(128),
  address5              varchar(128),
  postcode              varchar(16),
  telephone_day         varchar(16),
  telephone_evening     varchar(16),
  email                 varchar(64)
);

create index witness_i1 on witness(claim_id);
create unique index witness_i2 on witness(uuid);

------------------------------------------------------------------------------

create sequence pk_injured start with 1000000;

create table injured (
  id                    integer not null primary key default nextval('pk_injured'),
  uuid                  varchar(64) not null unique,
  claim_id              integer not null references claim(id),
  name                  varchar(128),
  address1              varchar(128),
  address2              varchar(128),
  address3              varchar(128),
  address4              varchar(128),
  address5              varchar(128),
  postcode              varchar(16),
  telephone_day         varchar(16),
  telephone_evening     varchar(16),
  email                 varchar(64),
  solicitor_appointed   char(1) not null check(solicitor_appointed in ('y','n')),
  solicitor_name        varchar(128),
  solicitor_address1    varchar(128),
  solicitor_address2    varchar(128),
  solicitor_address3    varchar(128),
  solicitor_address4    varchar(128),
  solicitor_address5    varchar(128),
  solicitor_postcode    varchar(16),
  solicitor_telephone   varchar(16),
  solicitor_email       varchar(64)
);

create index injured_i1 on injured(claim_id);
create unique index injured_i2 on injured(uuid);

------------------------------------------------------------------------------

create sequence pk_rental_cost start with 1000000;

create table rental_cost (
  id                    integer not null primary key default nextval('pk_rental_cost'),
  rental_id             integer not null references rental(id),
  net_rental            numeric not null,
  rental_vat            numeric not null,
  gross_rental          numeric not null
);

create index rental_cost_i1 on rental_cost(rental_id);

------------------------------------------------------------------------------

create sequence pk_repair_cost start with 1000000;

create table repair_cost (
  id                    integer not null primary key default nextval('pk_repair_cost'),
  claim_id              integer not null references claim(id),
  net_repair            numeric not null,
  repair_vat            numeric not null,
  gross_repair          numeric not null
);

create index repair_cost_i1 on repair_cost(claim_id);

------------------------------------------------------------------------------

create sequence pk_storage_recovery_cost start with 1000000;

create table storage_recovery_cost (
  id                    integer not null primary key default nextval('pk_storage_recovery_cost'),
  claim_id              integer not null references claim(id),
  net_storage_recovery  numeric not null,
  storage_recovery_vat  numeric not null,
  gross_storage_recovery numeric not null
);

create index storage_recovery_cost_i1 on storage_recovery_cost(claim_id);

------------------------------------------------------------------------------

create sequence pk_engineer_cost start with 1000000;

create table engineer_cost (
  id                    integer not null primary key default nextval('pk_engineer_cost'),
  claim_id              integer not null references claim(id),
  net_fee               numeric not null,
  fee_vat               numeric not null,
  gross_fee             numeric not null
);

create index engineer_cost_i1 on engineer_cost(claim_id);

------------------------------------------------------------------------------

create sequence pk_handling_fee start with 1000000;

create table handling_fee (
  id                    integer not null primary key default nextval('pk_handling_fee'),
  rental_id             integer not null references rental(id),
  net_fee               numeric not null,
  fee_vat               numeric not null,
  gross_fee             numeric not null
);

create index handling_fee_i1 on handling_fee(rental_id);

------------------------------------------------------------------------------

create sequence pk_engineer_report start with 1000000;

create table engineer_report (
  id                    integer not null primary key default nextval('pk_engineer_report'),
  claim_id              integer not null references claim(id),
  labour_amount         numeric not null,
  total_amount          numeric not null,
  days                  numeric not null,
  usable                char(1) not null default 'n' check(usable in ('y','n')),
  name                  varchar(128),
  company               varchar(128),
  address1              varchar(128),
  address2              varchar(128),
  address3              varchar(128),
  address4              varchar(128),
  address5              varchar(128),
  postcode              varchar(16),
  telephone             varchar(16),
  email                 varchar(64) 
);

create index engineer_report_i1 on engineer_report(claim_id);

------------------------------------------------------------------------------

create sequence pk_extra start with 1000000;

create table extra (
  id                    integer not null primary key default nextval('pk_extra'),
  code                  varchar(64) not null unique
);

create unique index extra_i1 on extra(code);

------------------------------------------------------------------------------

create sequence pk_rental_extra start with 1000000;

create table rental_extra (
  id                    integer not null primary key default nextval('pk_rental_extra'),
  rental_id             integer not null references rental(id),
  extra_id              integer not null references extra(id),
  quantity              numeric not null,
  item_amount           numeric not null
);

create index rental_extra_i1 on rental_extra(rental_id);

------------------------------------------------------------------------------

create sequence pk_rental_vehicle_extra start with 1000000;
  
create table rental_vehicle_extra (
  id                    integer not null primary key default nextval('pk_rental_vehicle_extra'),
  rental_vehicle_id     integer not null references rental_vehicle(id),
  extra_id              integer not null references extra(id)
);

create index rental_vehicle_extra_i1 on rental_vehicle_extra(rental_vehicle_id);

------------------------------------------------------------------------------

create sequence pk_rental_rate start with 1000000;

create table rental_rate (
  id                    integer not null primary key default nextval('pk_rental_rate'),
  insurer_country_id    integer not null references insurer_country(id),
  supplier_id           integer not null references supplier(id),
  vehicle_class_id      integer not null references vehicle_class(id),
  cost                  numeric not null
);

create unique index rental_rate_i1 on rental_rate(insurer_country_id,supplier_id,vehicle_class_id);

------------------------------------------------------------------------------

create sequence pk_rental_extra_rate start with 1000000;

create table rental_extra_rate (
  id                    integer not null primary key default nextval('pk_rental_extra_rate'),
  insurer_country_id    integer not null references insurer_country(id),
  supplier_id           integer not null references supplier(id),
  extra_id              integer not null references extra(id),
  cost                  numeric not null
);

create unique index rental_extra_rate_i1 on rental_extra_rate(insurer_country_id,supplier_id,extra_id);

------------------------------------------------------------------------------

create sequence pk_rental_authorisation start with 1000000;

create table rental_authorisation (
  id                    integer not null primary key default nextval('pk_rental_authorisation'),
  rental_id             integer not null references rental(id),
  updated               timestamp not null default current_timestamp,
  session_id            integer not null references web_user_session(id),
  status                varchar(32) not null check(status in ('reset','awaiting authorisation','authorised','self authorised','in progress','cancelled','disputed','rental ended','invoiced','invoice disputed','invoice authorised','finished')),
  ack_session_id        integer references web_user_session(id)
);

create index rental_authorisation_i1 on rental_authorisation(rental_id);
create index rental_authorisation_i2 on rental_authorisation(rental_id,updated);
create index rental_authorisation_i3 on rental_authorisation(session_id);
create index rental_authorisation_i4 on rental_authorisation(ack_session_id);

------------------------------------------------------------------------------

create sequence pk_rental_note_acknowledgement start with 1000000;

create table rental_note_acknowledgement (
  id                    integer not null primary key default nextval('pk_rental_note_acknowledgement'),
  note_id               integer not null references rental_note(id),
  session_id            integer not null references web_user_session(id)
);

create index rental_note_acknowledgement_i1 on rental_note_acknowledgement(note_id);
create index rental_note_acknowledgement_i2 on rental_note_acknowledgement(session_id);

------------------------------------------------------------------------------

create sequence pk_rental_insurer_location start with 1000000;

create table rental_insurer_location (
  id                    integer not null primary key default nextval('pk_rental_insurer_location'),
  rental_id             integer not null references rental(id),
  insurer_country_location_id integer not null references insurer_country_location(id)
);

create index rental_insurer_location_i1 on rental_insurer_location(rental_id);
create index rental_insurer_location_i2 on rental_insurer_location(insurer_country_location_id);

------------------------------------------------------------------------------

create sequence pk_rental_insurer_product start with 1000000;

create table rental_insurer_product (
  id                    integer not null primary key default nextval('pk_rental_insurer_product'),
  rental_id             integer not null references rental(id),
  insurer_country_product_id integer not null references insurer_country_product(id)
);

create index rental_insurer_product_i1 on rental_insurer_product(rental_id);
create index rental_insurer_product_i2 on rental_insurer_product(insurer_country_product_id);

------------------------------------------------------------------------------

create sequence pk_rental_estimated_close start with 1000000;

create table rental_estimated_close (
  id                    integer not null primary key default nextval('pk_rental_estimated_close'),
  estimated_close       timestamp not null,
  rental_note_id        integer not null references rental_note(id)
);

create index rental_estimated_close_i1 on rental_estimated_close(rental_note_id);

------------------------------------------------------------------------------

create or replace view latest_rental_authorisation_tmp as
select rental_id as rental_id,max(updated) as updated
from rental_authorisation
group by rental_id;

create or replace view latest_rental_authorisation_status as
select a.id as auth_id,l.rental_id as rental_id,a.status,l.updated
from latest_rental_authorisation_tmp l,rental_authorisation a
where l.rental_id=a.rental_id and l.updated=a.updated;

create or replace view new_rentals as
select r.id as rental_id,r.supplier_id as supplier_id,r.created as creation_date
from rental r,latest_rental_authorisation_status a
where r.id=a.rental_id and a.status='reset';

create or replace view new_rentals_secure as
select s.session_id,r.rental_id,r.supplier_id,r.creation_date as creation_date
from new_rentals r,web_session_security s where r.supplier_id=s.supplier_id;

create or replace view rental_interest as
select n.rental_id,s.web_user_id as user_id from rental_note n,web_user_session s where n.session_id=s.id
union
select a.rental_id,s.web_user_id as user_id from rental_authorisation a,web_user_session s where a.session_id=s.id or a.ack_session_id=s.id ;

create or replace view unacknowledge_notes as
select n.id as note_id,i.user_id from rental_note n,rental_interest i where n.rental_id=i.rental_id and n.id not in 
(select note_id from rental_note_acknowledgement a,web_user_session s where a.session_id=s.id and s.web_user_id=i.user_id);

create or replace view unacknowledged_authorisation_change as
select a.auth_id,i.user_id,a.status,a.updated from latest_rental_authorisation_status a,rental_interest i where a.rental_id=i.rental_id;

------------------------------------------------------------------------------

create sequence pk_rental_payment_request start with 1000000;

create table rental_payment_request (
  id                    integer not null primary key default nextval('pk_rental_payment_request'),
  uuid                  varchar(64) not null unique,
  rental_id             integer not null references rental(id),
  insurer_country_id    integer not null references insurer_country(id),
  supplier_id           integer not null references supplier(id),
  invoice_type          varchar(32) not null check(invoice_type in ('claim handling','rental')),
  net_amount            numeric not null,
  vat_amount            numeric not null,
  gross_amount          numeric not null,
  updated               timestamp not null default current_timestamp,
  ack_session_id        integer references web_user_session(id),
  ack_date              timestamp default current_timestamp
);

create index rental_payment_request_i1 on rental_payment_request(rental_id);
create index rental_payment_request_i2 on rental_payment_request(insurer_country_id);
create index rental_payment_request_i3 on rental_payment_request(supplier_id);
create index rental_payment_request_i4 on rental_payment_request(uuid);
create index rental_payment_request_i5 on rental_payment_request(ack_session_id);

------------------------------------------------------------------------------

create sequence pk_claim_handling_rate start with 1000000;

create table claim_handling_rate (
  id                    integer not null primary key default nextval('pk_claim_handling_rate'),
  insurer_country_id    integer not null references insurer_country(id),
  supplier_id           integer not null references supplier(id),
  net_amount            numeric not null,
  vat_amount            numeric not null,
  gross_amount          numeric not null
);

create index claims_handling_rate_i1 on claim_handling_rate(insurer_country_id,supplier_id);

------------------------------------------------------------------------------

create sequence pk_chox_usage_rate start with 1000000;

create table chox_usage_rate (
  id                    integer not null primary key default nextval('pk_chox_usage_rate'),
  fee_type              varchar(32) not null check(fee_type in ('upload rental','request authorisation','self authorisation','authorise rental','acknowledge authorisation','dispute rental','rental ended','invoice','dispute invoice','authorise invoice','cancel')),
  insurer_country_id    integer references insurer_country(id),
  supplier_id           integer references supplier(id),
  net_amount            numeric not null,
  vat_amount            numeric not null,
  gross_amount          numeric not null
);

create index chox_usage_rate_i1 on chox_usage_rate(fee_type);
create index chox_usage_rate_i2 on chox_usage_rate(insurer_country_id,fee_type);
create index chox_usage_rate_i3 on chox_usage_rate(supplier_id,fee_type);

------------------------------------------------------------------------------

create sequence pk_chox_usage_invoice start with 1000000; 
 
create table chox_usage_invoice (
  id                    integer not null primary key default nextval('pk_chox_usage_invoice'),
  uuid                  varchar(64) not null unique,
  insurer_country_id    integer references insurer_country(id),
  supplier_id           integer references supplier(id),
  net_amount            numeric not null,
  vat_amount            numeric not null,
  gross_amount          numeric not null,
  ack_session_id        integer references web_user_session(id),
  ack_date              timestamp default current_timestamp,
  updated               timestamp not null default current_timestamp,
  pdf                   bytea
);

create index chox_usage_invoice_i1 on chox_usage_invoice(insurer_country_id);
create index chox_usage_invoice_i2 on chox_usage_invoice(supplier_id);

------------------------------------------------------------------------------

create sequence pk_chox_usage_fee start with 1000000;

create table chox_usage_fee (
  id                    integer not null primary key default nextval('pk_chox_usage_rate'),
  fee_type              varchar(32) not null check(fee_type in ('upload rental','request authorisation','self authorisation','authorise rental','acknowledge authorisation','dispute rental','rental ended','invoice','dispute invoice','authorise invoice','cancel')),
  insurer_country_id    integer references insurer_country(id),
  supplier_id           integer references supplier(id),
  net_amount            numeric not null,
  vat_amount            numeric not null,
  gross_amount          numeric not null,
  rental_id             integer not null references rental(id),
  session_id            integer not null references web_user_session(id),
  updated               timestamp not null default current_timestamp,
  invoice_id            integer references chox_usage_invoice(id)
);

create index chox_usage_fee_i1 on chox_usage_fee(insurer_country_id);
create index chox_usage_fee_i2 on chox_usage_fee(supplier_id);
create index chox_usage_fee_i3 on chox_usage_fee(invoice_id);

------------------------------------------------------------------------------


create or replace view rental_security as
select distinct s.session_id,r.id as rental_id
from web_session_security s,rental r,claim c,
rental_insurer_location ril,rental_insurer_product rip,
insurer_country_location icl,insurer_country_product icp
where
r.id=c.rental_id
and ril.rental_id=r.id
and rip.rental_id=r.id
and icl.insurer_country_id=c.tp_insurer_country_id
and icp.insurer_country_id=c.tp_insurer_country_id
and ril.insurer_country_location_id=icl.id
and rip.insurer_country_product_id=icp.id
and (s.supplier_id=r.supplier_id
or (s.insurer_country_id=c.tp_insurer_country_id
and s.insurer_country_location_id=icl.id
and s.insurer_country_product_id=icp.id)
);

create or replace view secure_latest_rental_authorisation_status as
select s.session_id,a.auth_id,a.rental_id,a.status,a.updated
from latest_rental_authorisation_status a,rental_security s
where a.rental_id=s.rental_id;


------------------------------------------------------------------------------

create sequence pk_search_fields start with 1000000;

create table search_fields (
  id                    integer not null primary key default nextval('pk_search_fields'),
  fieldname             varchar(64) unique
);

------------------------------------------------------------------------------

create sequence pk_search_materialised start with 1000000;

create table search_materialised (
  id                    integer not null primary key default nextval('pk_search_materialised'),
  rental_id             integer not null references rental(id),
  field_id              integer not null references search_fields(id),
  string_value          varchar(1024),
  lcase_string_value    varchar(1024),
  integer_value         integer,
  date_value            timestamp,
  numeric_value         numeric
);

create index search_materialised_i1 on search_materialised(rental_id);
create index search_materialised_i2 on search_materialised(field_id);
create index search_materialised_i3 on search_materialised(field_id,string_value);
create index search_materialised_i4 on search_materialised(field_id,lcase_string_value);
create index search_materialised_i5 on search_materialised(field_id,integer_value);
create index search_materialised_i6 on search_materialised(field_id,date_value);
create index search_materialised_i7 on search_materialised(field_id,numeric_value);

------------------------------------------------------------------------------

create or replace view search_source as
select r.id as rental_id, f.id as field_id,r.supplier_reference as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from rental r,search_fields f where f.fieldname='supplier_reference'
union all
select r.id as rental_id,f.id as field_id,null::varchar as string_value,r.supplier_id as integer_value,null::timestamp as date_value,null::numeric as numeric_value from rental r,search_fields f where f.fieldname='supplier_id'
union all
select r.id as rental_id,f.id as field_id,null::varchar as string_value,null::integer as integer_value,first_contact as date_value,null::numeric as numeric_value from rental r,search_fields f where f.fieldname='first_contact'
union all
select c.rental_id as rental_id,f.id as field_id,c.policy_number as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='customer_policy_number'
union all
select c.rental_id as rental_id,f.id as field_id,c.claim_reference as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='customer_claim_reference'
union all
select c.rental_id as rental_id,f.id as field_id,c.policy_holder_name as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='customer_name'
union all
select c.rental_id as rental_id,f.id as field_id,c.vehicle_registration as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='customer_vrn'
union all
select c.rental_id as rental_id,f.id as field_id,c.vehicle_manufacturer as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='customer_manufacturer'
union all
select c.rental_id as rental_id,f.id as field_id,c.vehicle_model as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='customer_model'
union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,c.vehicle_class_id::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='customer_class_id'
union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,c.insurer_country_id::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='customer_insurer_id'

union all
select c.rental_id as rental_id,f.id as field_id,c.tp_policy_number as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='tp_policy_number'
union all
select c.rental_id as rental_id,f.id as field_id,c.tp_claim_reference as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='tp_claim_reference'
union all
select c.rental_id as rental_id,f.id as field_id,c.tp_name as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='tp_name'
union all
select c.rental_id as rental_id,f.id as field_id,c.tp_vehicle_registration as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='tp_vrn'
union all
select c.rental_id as rental_id,f.id as field_id,c.tp_vehicle_manufacturer as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='tp_manufacturer'
union all
select c.rental_id as rental_id,f.id as field_id,c.tp_vehicle_model as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='tp_model'
union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,c.tp_vehicle_class_id::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='tp_class_id'
union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,c.tp_insurer_country_id::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='tp_insurer_id'

union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,null::integer as integer_value,c.incident_date as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='incident_date'

union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,c.proposed_rental_class_id::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from claim c,search_fields f where f.fieldname='proposed_rental_class_id'

union all
select d.rental_id as rental_id,f.id as field_id,d.firstnames || ' ' || d.lastname as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from driver d,search_fields f where f.fieldname='driver_name'

union all
select v.rental_id as rental_id,f.id as field_id,v.vehicle_registration as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from rental_vehicle v,search_fields f where f.fieldname='rental_vehicle_registration'

union all
select v.rental_id as rental_id,f.id as field_id,v.vehicle_manufacturer as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from rental_vehicle v,search_fields f where f.fieldname='rental_vehicle_manufacturer'

union all
select v.rental_id as rental_id,f.id as field_id,v.vehicle_model as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from rental_vehicle v,search_fields f where f.fieldname='rental_vehicle_model'

union all
select c.rental_id as rental_id,f.id as field_id,e.name as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from engineer_report e,claim c,search_fields f where f.fieldname='engineer_name' and c.id=e.claim_id

union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,null::integer as integer_value,null::timestamp as date_value,e.labour_amount as numeric_value from engineer_report e,claim c,search_fields f where f.fieldname='engineer_labour_amount' and c.id=e.claim_id

union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,null::integer as integer_value,null::timestamp as date_value,e.total_amount as numeric_value from engineer_report e,claim c,search_fields f where f.fieldname='engineer_total_amount' and c.id=e.claim_id

union all
select c.rental_id as rental_id,f.id as field_id,null::varchar as string_value,null::integer as integer_value,null::timestamp as date_value,e.days as numeric_value from engineer_report e,claim c,search_fields f where f.fieldname='engineer_days' and c.id=e.claim_id

union all
select i.rental_id as rental_id,f.id as field_id,null::varchar as string_value,null::integer as integer_value,null::timestamp as date_value,i.net as numeric_value from invoice i,search_fields f where f.fieldname='invoice_net'

union all
select i.rental_id as rental_id,f.id as field_id,null::varchar as string_value,null::integer as integer_value,null::timestamp as date_value,i.vat as numeric_value from invoice i,search_fields f where f.fieldname='invoice_vat'

union all
select i.rental_id as rental_id,f.id as field_id,null::varchar as string_value,null::integer as integer_value,null::timestamp as date_value,i.gross as numeric_value from invoice i,search_fields f where f.fieldname='invoice_gross'

union all
select a.rental_id as rental_id,f.id as field_id,a.status as string_value,null::integer as integer_value,null::timestamp as date_value,null::numeric as numeric_value from latest_rental_authorisation_status a,search_fields f where f.fieldname='authorisation_status'

;

------------------------------------------------------------------------------

create view secure_search as
select rs.session_id,f.fieldname,s.rental_id,s.string_value,s.lcase_string_value,s.integer_value,s.date_value,s.numeric_value
from rental_security rs,search_fields f,search_materialised s
where rs.rental_id=s.rental_id
and f.id=s.field_id
;

------------------------------------------------------------------------------

create sequence pk_decision_making start with 1000000;

create table decision_making (
  id                    integer not null primary key default nextval('pk_decision_making'),
  decision_type         varchar(64) not null check(decision_type in('invoice pay')),
  supplier_id           integer not null references supplier(id),
  insurer_country_id    integer not null references insurer_country(id),
  java_class            varchar(256) not null,
  auto_authorise        char(1) not null default 'n' check(auto_authorise in ('y','n'))
);

create index decision_making_i1 on decision_making(decision_type,supplier_id,insurer_country_id);

------------------------------------------------------------------------------

create sequence pk_decision_making_parameter start with 1000000;

create table decision_making_parameter (
  id                    integer not null primary key default nextval('pk_decision_making_parameter'),
  decision_id           integer not null references decision_making(id),
  name                  varchar(64) not null,
  numeric_value         numeric,
  string_value          varchar(1024),
  date_value            timestamp
);

create index decision_making_parameter_i1 on decision_making_parameter(decision_id,name);

------------------------------------------------------------------------------

create sequence pk_rental_processor start with 1000000;

create table rental_processor (
  id                    integer not null primary key default nextval('pk_rental_processor'),
  tp_insurer_country_id integer references insurer_country(id),
  supplier_id           integer references supplier(id),
  seq                   integer not null default 0,
  java_class            varchar(256) not null
);

------------------------------------------------------------------------------

create or replace view can_auto_authorise as
select ra.rental_id,dm.id as decision_id
from latest_rental_authorisation_status ra,
rental r,
claim c,
decision_making dm
where ra.rental_id=r.id
and c.rental_id=r.id
and dm.supplier_id=r.supplier_id
and dm.insurer_country_id=c.tp_insurer_country_id
and dm.auto_authorise='y'
and ra.status='invoiced';








