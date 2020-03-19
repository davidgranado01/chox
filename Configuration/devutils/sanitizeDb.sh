#!/bin/bash
/usr/local/pgsql/bin/psql -l | awk -F "[|,+]" '{print $1}'
read -p "Enter the DB Name from the above list? " answer;
/usr/local/pgsql/bin/psql -U chox -d $answer << --EOF--
BEGIN;
SELECT 'Updating login password.......' as " ";
update web_user set password = '6b53da3955e5fdaf93656fc15a0f0318', password_last_modified_date = now();
--update web_user set password = 'd681ec3aab59e181c42f544e2abb8d3c' where user_name='admin@dlg.com';

SELECT 'Updating scheduler_job login_password, autherised_user, bcc_receiver.......' as " ";
UPDATE
    gmail_scheduler_job
SET
    login_password = 'C0mpliance',
    authorised_user = 'john.dowson@audatex.co.uk,robert.hon@audatex.co.uk,John.Strawhorne@audatex.co.uk,Melvin.Lai@audatex.co.uk',
    bcc_receiver = 'john.dowson@audatex.co.uk'
WHERE
    job_name != 'DB_REFERENCE_UPDATE';

UPDATE
    gmail_scheduler_job
SET
    login_password = 'C0mpliance',
    authorised_user = 'john.dowson@audatex.co.uk',
    active = false,
    bcc_receiver = 'john.dowson@audatex.co.uk'
WHERE
    job_name = 'DB_REFERENCE_UPDATE';

UPDATE scheduler_job SET login_password = 'C0mpliance';

truncate table queued_ticket;

UPDATE bre_band set fraud_check_enable = false;

SELECT 'Updating web_user first_name.......' as " ";
update web_user set first_name =  (ARRAY['Seeni', 'Stu', 'John', 'Balaji', 'Abrar', 'Paul', 'Dempsey', 'Mark', 'Shiva', 'Andy', 'Elliot', 'Ben', 'Brian', 'Patrick', 'Richard', 'Morris', 'Jim', 'Billy', 'Mary','Jane','Candice','Esther','Samntha'])[floor(random() * 22.0) + 1] where first_name is not null and first_name not like '~~%';
SELECT 'Updating web_user last_name.......' as " ";
update web_user set last_name =  (ARRAY['Shan', 'Coats', 'Dowson', 'Shengani', 'Malang', 'Boyel', 'Ubeku', 'Alcock', 'Kurumurthy', 'Douglos', 'Robert', 'Richmond', 'Whitfield', 'Chagam', 'Bego', 'Smith', 'Jones', 'Butcher', 'Dawson','Machin','Cleese'])[floor(random() * 20.0) + 1] where last_name is not null and last_name not like '~~%';
SELECT 'Updating web_user email.......' as " ";
update web_user set email = 'nobody@nowhere';
SELECT 'Updating web_user telephone.......' as " ";
update web_user set telephone = '07702 904147' where telephone is not null;

SELECT 'Updating customer first_name.......' as " ";
update customer set first_name =  (ARRAY['Seeni', 'Stu', 'John', 'Balaji', 'Abrar', 'Paul', 'Dempsey', 'Mark', 'Shiva', 'Andy', 'Elliot', 'Ben', 'Brian', 'Patrick', 'Richard', 'Morris', 'Jim', 'Billy', 'Mary','Jane','Candice','Esther','Samntha'])[floor(random() * 22.0) + 1] where first_name is not null and first_name not like '~~%';
SELECT 'Updating customer last_name.......' as " ";
update customer set last_name =  (ARRAY['Shan', 'Coats', 'Dowson', 'Shengani', 'Malang', 'Boyel', 'Ubeku', 'Alcock', 'Kurumurthy', 'Douglos', 'Robert', 'Richmond', 'Whitfield', 'Chagam', 'Bego', 'Smith', 'Jones', 'Butcher', 'Dawson','Machin','Cleese'])[floor(random() * 20.0) + 1] where last_name is not null and last_name not like '~~%';
SELECT 'Updating customer email.......' as " ";
update customer set email = null where email is not null and email not like '~~%';
SELECT 'Updating customer telephone_day.......' as " ";
update customer set telephone_day = null where telephone_day is not null and telephone_day not like '~~%';
SELECT 'Updating customer telephone_evening.......' as " ";
update customer set telephone_evening = null where telephone_evening is not null and telephone_evening not like '~~%';
update customer set address1 = 'Unknown' where address1 is not null and address1 not like '~~%';
update customer set address2 = null where address2 is not null and address2 not like '~~%';
update customer set address3 = null where address3 is not null and address3 not like '~~%';
update customer set address4 = null where address4 is not null and address4 not like '~~%';
update customer set address5 = null where address5 is not null and address5 not like '~~%';
update customer set occupation = null where occupation is not null and occupation not like '~~%';

SELECT 'Updating third_party first_name.......' as " ";
update third_party set first_name =  (ARRAY['Seeni', 'Stu', 'John', 'Balaji', 'Abrar', 'Paul', 'Dempsey', 'Mark', 'Shiva', 'Andy', 'Elliot', 'Ben', 'Brian', 'Patrick', 'Richard', 'Morris', 'Jim', 'Billy', 'Mary','Jane','Candice','Esther','Samntha'])[floor(random() * 22.0) + 1] where first_name is not null and first_name not like '~~%';
SELECT 'Updating third_party last_name.......' as " ";
update third_party set last_name =  (ARRAY['Shan', 'Coats', 'Dowson', 'Shengani', 'Malang', 'Boyel', 'Ubeku', 'Alcock', 'Kurumurthy', 'Douglos', 'Robert', 'Richmond', 'Whitfield', 'Chagam', 'Bego', 'Smith', 'Jones', 'Butcher', 'Dawson','Machin','Cleese'])[floor(random() * 20.0) + 1] where last_name is not null and last_name not like '~~%';
SELECT 'Updating third_party email.......' as " ";
update third_party set email = null where email is not null and email not like '~~%';
SELECT 'Updating third_party telephone_day.......' as " ";
update third_party set telephone_day = null where telephone_day is not null and telephone_day not like '~~%';
SELECT 'Updating third_party telephone_evening.......' as " ";
update third_party set telephone_evening = null where telephone_evening is not null and telephone_evening not like '~~%';
update third_party set address1 = 'Unknown' where address1 is not null and address1 not like '~~%';
update third_party set address2 = null where address2 is not null and address2 not like '~~%';
update third_party set address3 = null where address3 is not null and address3 not like '~~%';
update third_party set address4 = null where address4 is not null and address4 not like '~~%';
update third_party set address5 = null where address5 is not null and address5 not like '~~%';

SELECT 'Updating witness name.......' as " ";
update witness set name =  (ARRAY['Seeni', 'Stu', 'John', 'Balaji', 'Abrar', 'Paul', 'Dempsey', 'Mark', 'Shiva', 'Andy', 'Elliot', 'Ben', 'Brian', 'Patrick', 'Richard', 'Morris', 'Jim', 'Billy', 'Mary','Jane','Candice','Esther','Samntha'])[floor(random() * 22.0) + 1] where name is not null and name not like '~~%';
SELECT 'Updating witness email.......' as " ";
update witness set email = null where email is not null and email not like '~~%';
SELECT 'Updating witness telephone_day.......' as " ";
update witness set telephone_day = null where telephone_day is not null and telephone_day not like '~~%';
SELECT 'Updating witness telephone_evening.......' as " ";
update witness set telephone_evening = null where telephone_evening is not null and telephone_evening not like '~~%';
update witness set address1 = 'Unknown' where address1 is not null and address1 not like '~~%';
update witness set address2 = null where address2 is not null and address2 not like '~~%';
update witness set address3 = null where address3 is not null and address3 not like '~~%';
update witness set address4 = null where address4 is not null and address4 not like '~~%';
update witness set address5 = null where address5 is not null and address5 not like '~~%';

SELECT 'Updating injury name.......' as " ";
update injury set name =  (ARRAY['Seeni', 'Stu', 'John', 'Balaji', 'Abrar', 'Paul', 'Dempsey', 'Mark', 'Shiva', 'Andy', 'Elliot', 'Ben', 'Brian', 'Patrick', 'Richard', 'Morris', 'Jim', 'Billy', 'Mary','Jane','Candice','Esther','Samntha'])[floor(random() * 22.0) + 1] where name is not null and name not like '~~%';
SELECT 'Updating injury email.......' as " ";
update injury set email = null where email is not null and email not like '~~%';
SELECT 'Updating injury telephone_day.......' as " ";
update injury set telephone_day = null where telephone_day is not null and telephone_day not like '~~%';
SELECT 'Updating injury telephone_evening.......' as " ";
update injury set telephone_evening = null where telephone_evening is not null and telephone_evening not like '~~%';
update injury set address1 = 'Unknown' where address1 is not null and address1 not like '~~%';
update injury set address2 = null where address2 is not null and address2 not like '~~%';
update injury set address3 = null where address3 is not null and address3 not like '~~%';
update injury set address4 = null where address4 is not null and address4 not like '~~%';
update injury set address5 = null where address5 is not null and address5 not like '~~%';

SELECT 'Updating injury solicitor_name.......' as " ";
update injury set solicitor_name =  (ARRAY['Seeni', 'Stu', 'John', 'Balaji', 'Abrar', 'Paul', 'Dempsey', 'Mark', 'Shiva', 'Andy', 'Elliot', 'Ben', 'Brian', 'Patrick', 'Richard', 'Morris', 'Jim', 'Billy', 'Mary','Jane','Candice','Esther','Samntha'])[floor(random() * 22.0) + 1] where solicitor_name is not null and solicitor_name not like '~~%';
SELECT 'Updating injury email.......' as " ";
update injury set solicitor_email = null where solicitor_email is not null and solicitor_email not like '~~%';
SELECT 'Updating injury telephone.......' as " ";
update injury set solicitor_telephone = null where solicitor_telephone is not null and solicitor_telephone not like '~~%';
update injury set solicitor_address1 = 'Unknown' where solicitor_address1 is not null and solicitor_address1 not like '~~%';
update injury set solicitor_address2 = null where solicitor_address2 is not null and solicitor_address2 not like '~~%';
update injury set solicitor_address3 = null where solicitor_address3 is not null and solicitor_address3 not like '~~%';
update injury set solicitor_address4 = null where solicitor_address4 is not null and solicitor_address4 not like '~~%';
update injury set solicitor_address5 = null where solicitor_address5 is not null and solicitor_address5 not like '~~%';


SELECT 'Updating attachment_file file_buffer.......' as " ";
update attachment_file set file_buffer = '3c 3f 78';
SELECT 'Removing IP White-list.....' as " ";
update insurer set enable_ip_whitelist = false where enable_ip_whitelist = true;
update insurer set force_password_change = 0 where force_password_change != 0;
update chorganisation set enable_ip_whitelist = false where enable_ip_whitelist = true;
update chorganisation set force_password_change = 0 where force_password_change != 0;
SELECT 'Updating password policy....' as " ";
update chorganisation set unique_password_history = 1 where unique_password_history != 1;
update insurer set unique_password_history = 1 where unique_password_history != 1;
COMMIT;

BEGIN;

SELECT 'Updating passwords for EHI users.......' as " ";
update web_user set password='5fbd82ef9d62bd1aa52d727408f83cd4', blocked=false, blocked_date=null, password_last_modified_date=now(), is_expired=false, failed_login_attempts=0 where id=4580;
update web_user set password='9373fcada86d9152910a051990ccfd6a', blocked=false, blocked_date=null, password_last_modified_date=now(), is_expired=false, failed_login_attempts=0  where id in (4014,4653,4581);

SELECT 'Updating password for IG users.......' as " ";
update web_user set password='fc0b7c0f50bb8e1f63213075df32f256', blocked=false, blocked_date=null, password_last_modified_date=now(), is_expired=false, failed_login_attempts=0 where id=4661;

SELECT 'Adding mapping from ERS to IG and setting BRE Band.......' as " ";
insert into insurer_chorganisation(insurer_id, chorganisation_id, created_by, last_modified_by, created_date, last_modified_date, version)
select 20, 1626, 999, 999, now(), now(), 0;

insert into bre_band_organisation(band_id, chorganisation_id, created_by, created_date, last_modified_by, last_modified_date, version)
select 56, 1626, 999, now(), 999, now(), 0;

SELECT 'Adding mapping from RSA to IG and setting BRE Band.......' as " ";
insert into insurer_chorganisation(insurer_id, chorganisation_id, created_by, last_modified_by, created_date, last_modified_date, version)
select 3, 1626, 999, 999, now(), now(), 0;

insert into bre_band_organisation(band_id, chorganisation_id, created_by, created_date, last_modified_by, last_modified_date, version)
select 2, 1626, 999, now(), 999, now(), 0;

COMMIT;


REINDEX DATABASE $answer

--EOF--
