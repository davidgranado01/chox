CREATE OR REPLACE FUNCTION deactivateUsers(claimAge integer)
    RETURNS void AS
$BODY$
DECLARE
    cutOff date;
    BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;

        update web_user
            set status = false, deactivated_date = now()
        where status = true and last_login_date < cutOff
          and user_name not like 'admin@%' and id not in (999, 4391, 5454, 3757, 6410, 3893, 6148, 5002, 5, 2002, 1766, 6637, 5075, 46, 4878, 1767, 3191, 6694, 6747, 6693, 7828, 7435);

        update web_user
            set status = false, deactivated_date = now()
        where status = true and last_login_date is null and created_date < cutOff
          and user_name not like 'admin@%' and id not in (999, 4391, 5454, 3757, 6410, 3893, 6148, 5002, 5, 2002, 1766, 6637, 5075, 46, 4878, 1767, 3191, 6694, 6747, 6693, 7828, 7435);
    END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION deactivateUsers(integer) to chox_user;
