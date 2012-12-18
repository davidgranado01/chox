drop function subscriber_task_report(int);
create or replace function subscriber_task_report
(
   insid int
)
returns table
(
   Handler text,
   "Workgroup Name" character varying,
   "New Task for today's date" numeric(10,2),
   "Tasks Outstanding In Total" numeric(10,2),
   "Tasks Received This week" numeric(10,2),
   "Tasks up to 7 days" numeric(10,2),
   "Tasks up to 8-14 days" numeric(10,2),
   "Tasks up to 15-30 days" numeric(10,2),
   "Tasks up to 31-60 days" numeric(10,2),
   "Tasks up to 61-90 days" numeric(10,2),
   "Tasks 90 + days" numeric(10,2)
)
as $$ DECLARE 
BEGIN 
RETURN QUERY

SELECT CASE WHEN c.claim_owner_id IS NULL THEN '0 OWNERS ASSIGNED' ELSE wu.first_name || ' ' || wu.last_name END AS "Handler",
       CASE WHEN c.workgroup_id IS NULL THEN '0 WORKGROUPS ASSIGNED' ELSE wk.name END AS "Workgroup Name",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
              AND t.visibility = 3
              AND t.visibility_role = 'ROLE_INS_CH'
              AND t.created_date BETWEEN now() - interval '1 day' AND now())) AS "New Task for today's date",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
              AND t.visibility = 3
              AND t.visibility_role = 'ROLE_INS_CH'
              AND t.created_date BETWEEN now() - interval '1 day' AND now()
              AND t.complete = FALSE)) AS "Tasks Outstanding In Total",
       sum((SELECT count(*)
                  FROM task t
                  WHERE t.claim_id = c.id
                  AND t.visibility = 3
                  AND t.visibility_role = 'ROLE_INS_CH'
                  AND (((SELECT EXTRACT(DOW FROM now()) = 1) AND (t.created_date BETWEEN now() - interval '1 day' AND now()))
                       OR ((SELECT EXTRACT(DOW FROM now()) = 2) AND (t.created_date BETWEEN now() - interval '2 day' AND now()))
                       OR ((SELECT EXTRACT(DOW FROM now()) = 3) AND (t.created_date BETWEEN now() - interval '3 day' AND now()))
                       OR ((SELECT EXTRACT(DOW FROM now()) = 4) AND (t.created_date BETWEEN now() - interval '4 day' AND now()))
                       OR ((SELECT EXTRACT(DOW FROM now()) = 5) AND (t.created_date BETWEEN now() - interval '5 day' AND now()))
                       OR ((SELECT EXTRACT(DOW FROM now()) = 6) AND (t.created_date BETWEEN now() - interval '6 day' AND now()))
                       OR ((SELECT EXTRACT(DOW FROM now()) = 7) AND (t.created_date BETWEEN now() - interval '7 day' AND now()))) 
             )) AS "Tasks Received This week",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
              AND t.visibility = 3
              AND t.visibility_role = 'ROLE_INS_CH'
              AND t.created_date BETWEEN now() - interval '7 days' AND now()
              AND t.complete = FALSE)) AS "Tasks up to 7 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
              AND t.visibility = 3
              AND t.visibility_role = 'ROLE_INS_CH'
              AND t.created_date BETWEEN now() - interval '8 day' AND now() - interval '14 day'
              AND t.complete = FALSE)) AS "Tasks 7-14 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
              AND t.visibility = 3
              AND t.visibility_role = 'ROLE_INS_CH'
              AND t.created_date BETWEEN now() - interval '15 day' AND now() - interval '30 day'
              AND t.complete = FALSE)) AS "Tasks 15-30 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
              AND t.visibility = 3
              AND t.visibility_role = 'ROLE_INS_CH'
              AND t.created_date BETWEEN now() - interval '31 day' AND now() - interval '60 day'
              AND t.complete = FALSE)) AS "Tasks 31-60 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
              AND t.visibility = 3
              AND t.visibility_role = 'ROLE_INS_CH'
              AND t.created_date BETWEEN now() - interval '61 day' AND now() - interval '90 day'
              AND t.complete = FALSE)) AS "Tasks 61-90 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
              AND t.visibility = 3
              AND t.visibility_role = 'ROLE_INS_CH'
              AND t.created_date <= now() - interval '91 day'
              AND t.complete = FALSE)) AS "90+ days"
FROM claim c
LEFT OUTER JOIN web_user wu ON (wu.id = c.claim_owner_id)
LEFT OUTER JOIN workgroup wk ON (wk.id = c.workgroup_id)
WHERE c.insurer_id = insid
AND c.claim_type IN (7,8,9)
GROUP BY 1,
         2
         ORDER BY 2, 1 ASC;
END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION subscriber_task_report(int) TO chox_user;
GRANT EXECUTE ON FUNCTION subscriber_task_report(int) TO chox_mi;
