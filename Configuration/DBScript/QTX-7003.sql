
-- insurer id 25 (QBE)
-- newly inserted user id above = 6680
-- role id 10 => ROLE_INS
-- role id 6  => ROLE_INS_CH

DO $$
DECLARE new_user_name TEXT;
DECLARE new_user_id integer;
DECLARE new_job_id integer;
DECLARE qbe_id integer;
DECLARE role_ins_id integer;
DECLARE role_ins_ch_id integer;
BEGIN

new_user_name := 'paidinvoices.qbe';

-- no permission to delete
--
--DELETE FROM web_user_user_role WHERE web_user_id IN
--  (SELECT id FROM web_user WHERE user_name = new_user_name);
--
--DELETE FROM web_user WHERE user_name = new_user_name;
--
--DELETE FROM scheduler_job WHERE login_username = new_user_name AND job_name = 'PAID_INVOICES';

SELECT id FROM web_user_role WHERE name = 'ROLE_INS' INTO role_ins_id;
SELECT id FROM web_user_role WHERE name = 'ROLE_INS_CH' INTO role_ins_ch_id;
SELECT id from insurer WHERE name = 'QBE' INTO qbe_id;

INSERT INTO web_user (email, first_name, last_name, password, created_by, created_date, last_modified_by, last_modified_date,
  insurer_id, status, is_expired, user_name, version, show_browser_warning, password_last_modified_date, blocked, failed_login_attempts, hashed)
VALUES ('dl-ukbr-audatex-penguin-reporting@audatex.com', 'Paid Invoices', '(via SFTP)', '{bcrypt}$2a$10$tOlpJVr.Q6TYCKKv8/LgHOaGRQWxgwMkZKP8q.zWGgMeqpt3yvPfS', 999, NOW(), 999, NOW(),
  qbe_id, 't', 'f', new_user_name, 0, 'f', NOW(), 'f', 0, 'f')
RETURNING id INTO new_user_id;

INSERT INTO web_user_user_role (web_user_id, web_user_role_id, created_by, created_date, last_modified_by, last_modified_date, is_active, version)
VALUES (new_user_id, role_ins_id, 999, NOW(), 999, NOW(), 't', 0),
(new_user_id, role_ins_ch_id, 999, NOW(), 999, NOW(), 't', 0);

INSERT INTO scheduler_job (login_username, login_password, job_name, active, version, created_by, created_date, last_modified_by, last_modified_date)
VALUES (new_user_name, '', 'PAID_INVOICES', 't', 0, 999, NOW(), 999, NOW())
RETURNING id INTO new_job_id;

RAISE NOTICE 'New user id: %, and new job id: %', new_user_id, new_job_id;
END $$;

