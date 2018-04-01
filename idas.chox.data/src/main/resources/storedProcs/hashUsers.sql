CREATE OR REPLACE FUNCTION hashUsers(claimAge integer, claimAge2 integer)
    RETURNS void AS
$BODY$
DECLARE
    cutOff date;
    cutOff2 date;
    BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;
        cutOff2 := now()::date  - ($2 || ' days')::interval;

        update web_user
            set hashed = true, hashed_date = now(),
                first_name = getHash(first_name, 256),
                last_name = getHash(last_name, 256),
                email = getHash(email, 256),
                version = version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        where status = false and hashed = false and deactivated_date < cutOff;

        update web_user
            set hashed = true, hashed_date = now(),
                first_name = getHash(first_name, 256),
                last_name = getHash(last_name, 256),
                email = getHash(email, 256),
                version = version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        where status = false and hashed = false and deactivated_date is null
          and (last_login_date < cutOff2 or (last_login_date is null and created_date < cutOff2));
    END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION hashUsers(integer, integer) to chox_user;
