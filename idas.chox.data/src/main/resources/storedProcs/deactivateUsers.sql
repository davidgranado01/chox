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
          and user_name not like 'admin@%' and id not in (999, 4391, 5454, 3757, 6410, 3893);

        update web_user
            set status = false, deactivated_date = now()
        where status = true and last_login_date is null and created_date < cutOff
          and user_name not like 'admin@%' and not in (999, 4391, 5454, 3757, 6410, 3893);
    END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION deactivateUsers(integer) to chox_user;
