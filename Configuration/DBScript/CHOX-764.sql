ALTER TABLE web_user ALTER COLUMN password TYPE character varying(37);
UPDATE web_user set password='{MD5}'||password where password not like '{MD5}%';

ALTER TABLE web_user ALTER COLUMN password TYPE character varying;

ALTER TABLE password_history ALTER COLUMN password TYPE character varying;
UPDATE password_history set password='{MD5}'||password where password not like '{%';
