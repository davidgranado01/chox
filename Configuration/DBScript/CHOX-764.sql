UPDATE web_user_role SET show_workgroup_disabled=TRUE, show_ownership_disabled=TRUE WHERE name='ROLE_CHO_MI';

ALTER TABLE web_user ALTER COLUMN password TYPE character varying;
UPDATE web_user set password='{MD5}'||password where password not like '{MD5}%' and password not like '{bcrypt}%';

ALTER TABLE password_history ALTER COLUMN password TYPE character varying;
UPDATE password_history set password='{MD5}'||password where password not like '{MD5}%' and password not like '{bcrypt}%';