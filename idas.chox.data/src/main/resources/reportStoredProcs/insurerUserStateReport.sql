CREATE OR REPLACE FUNCTION insurerUserStateReport(IN insId integer)
  RETURNS table (
   "User Name" varchar,
   "Name" text,
   "Active" text,
   "Role(s)" text,
   "Password Expired?" text,
   "Last Login Date" text
) AS $$
DECLARE
     ins_id INTEGER = insId::INTEGER;
     insPassChange integer ;
BEGIN
       insPassChange = (select force_password_change from insurer where id = ins_id);
RETURN QUERY
select w.user_name , w.first_name || ' ' || w.last_name, 
    (case when w.status then 'Yes' else 'No' end), 
    (select array_to_string(array_agg(wr.description),', ')
     from web_user_role wr
     JOIN web_user_user_role wuur on wr.id = wuur.web_user_role_id
                                 and wuur.web_user_id = w.id
                                 and wr.name != 'ROLE_INS'),
    (case when w.is_expired then 'Yes' else 
    (case when insPassChange > 0 then 
        (case when (select extract(day from (now() - w.password_last_modified_date)) >= (insPassChange)) then 'Yes' else 'No' end)
                else 'No' end) end),
    to_char(w.last_login_date, 'yyyy-mm-dd hh24:mi:ss') 
from web_user w
where insurer_id = ins_id 
group by w.user_name , w.first_name, w.last_name, w.status, w.id
order by w.user_name;

END;
$$
LANGUAGE plpgsql VOLATILE
COST 100;

GRANT EXECUTE ON FUNCTION insurerUserStateReport(IN insId integer) TO chox_user;
GRANT EXECUTE ON FUNCTION insurerUserStateReport(IN insId integer) TO chox_mi;
