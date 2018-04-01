CREATE OR REPLACE FUNCTION removeTasks(claimAge integer)
     RETURNS void AS
$BODY$
DECLARE
     cutOff date;
     BEGIN
        cutOff := now()::date  - ($1 || ' days')::interval;

        update task
            set description = 'GDPR: description content has been removed.',
                version = task.version + 1,
                last_modified_by = 999,
                last_modified_date = now()
        from claim c
        where task.claim_id = c.id
          and removed_tasks = false and c.hashed = true and c.hashed_date < cutOff;

        update claim
            set removed_tasks = true, removed_tasks_date = now(), version=version+1, last_modified_date=now(), last_modified_by=999
        where removed_tasks = false and hashed = true and hashed_date < cutOff;
     END;

$BODY$
LANGUAGE plpgsql;
GRANT EXECUTE on FUNCTION removeTasks(integer) to chox_user;
