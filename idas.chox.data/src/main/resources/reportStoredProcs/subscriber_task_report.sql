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


SELECT wu.first_name || ' ' || wu.last_name AS "Handler",
       wk.name AS "Workgroup Name",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now())) AS "New Task for today's date",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now()
                AND t.complete = TRUE)) AS "Tasks Outstanding In Total",
       sum(CASE WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 1) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 2) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '2 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 3) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '3 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 4) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '4 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 5) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '5 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 6) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '6 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 7) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '7 day' AND now()) ELSE 0 END) AS "Tasks Received This week",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '7 days' AND now())) AS "Tasks up to 7 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '8 day' AND now() - interval '14 day'
                AND t.complete = FALSE)) AS "Tasks 7-14 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '15 day' AND now() - interval '30 day'
                AND t.complete = FALSE)) AS "Tasks 15-30 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '31 day' AND now() - interval '60 day'
                AND t.complete = FALSE)) AS "Tasks 31-60 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '61 day' AND now() - interval '90 day'
                AND t.complete = FALSE)) AS "Tasks 61-90 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date <= now() - interval '91 day'
                AND t.complete = FALSE)) AS "90+ days"
FROM web_user wu
LEFT OUTER JOIN claim c ON (wu.id = c.claim_owner_id)
LEFT OUTER JOIN workgroup wk ON (wk.id = c.workgroup_id)
WHERE c.insurer_id = insid
GROUP BY 1,
         2
UNION
SELECT '0 OWNERS ASSIGNED' AS "Handler",
       wk.name AS "Workgroup Name",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now())) AS "New Task for today's date",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now()
                AND t.complete = TRUE)) AS "Tasks Outstanding In Total",
       sum(CASE WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 1) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 2) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '2 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 3) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '3 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 4) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '4 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 5) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '5 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 6) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '6 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 7) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '7 day' AND now()) ELSE 0 END) AS "Tasks Received This week",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '7 days' AND now())) AS "Tasks up to 7 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '8 day' AND now() - interval '14 day'
                AND t.complete = FALSE)) AS "Tasks 7-14 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '15 day' AND now() - interval '30 day'
                AND t.complete = FALSE)) AS "Tasks 15-30 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '31 day' AND now() - interval '60 day'
                AND t.complete = FALSE)) AS "Tasks 31-60 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '61 day' AND now() - interval '90 day'
                AND t.complete = FALSE)) AS "Tasks 61-90 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date <= now() - interval '91 day'
                AND t.complete = FALSE)) AS "90+ days"
FROM task t1,
     claim c,
     workgroup wk
WHERE c.insurer_id = insid
  AND wk.id = c.claim_owner_id
  AND t1.claim_id = c.id
  AND c.claim_owner_id IS NULL
GROUP BY 1,
         2
UNION
SELECT wu.first_name || ' ' || wu.last_name AS "Handler",
       '0 WORKGROUPS ASSIGNED' AS "Workgroup Name",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now())) AS "New Task for today's date",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now()
                AND t.complete = TRUE)) AS "Tasks Outstanding In Total",
       sum(CASE WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 1) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 2) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '2 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 3) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '3 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 4) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '4 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 5) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '5 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 6) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '6 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 7) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '7 day' AND now()) ELSE 0 END) AS "Tasks Received This week",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '7 days' AND now())) AS "Tasks up to 7 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '8 day' AND now() - interval '14 day'
                AND t.complete = FALSE)) AS "Tasks 7-14 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '15 day' AND now() - interval '30 day'
                AND t.complete = FALSE)) AS "Tasks 15-30 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '31 day' AND now() - interval '60 day'
                AND t.complete = FALSE)) AS "Tasks 31-60 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '61 day' AND now() - interval '90 day'
                AND t.complete = FALSE)) AS "Tasks 61-90 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date <= now() - interval '91 day'
                AND t.complete = FALSE)) AS "90+ days"
FROM task t1,
     claim c,
     web_user wu
WHERE c.insurer_id = insid
  AND wu.id = c.claim_owner_id
  AND t1.claim_id = c.id
  AND c.workgroup_id IS NULL
GROUP BY 1,
         2
UNION
SELECT '0 OWNERS ASSIGNED' AS "Handler",
       '0 WORKGROUPS ASSIGNED' AS "Workgroup Name",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now())) AS "New Task for today's date",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now()
                AND t.complete = TRUE)) AS "Tasks Outstanding In Total",
       sum(CASE WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 1) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '1 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 2) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '2 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 3) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '3 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 4) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '4 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 5) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '5 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 6) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '6 day' AND now()) WHEN
             (SELECT EXTRACT(DOW
                             FROM now()) = 7) THEN
             (SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '7 day' AND now()) ELSE 0 END) AS "Tasks Received This week",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '7 days' AND now())) AS "Tasks up to 7 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '8 day' AND now() - interval '14 day'
                AND t.complete = FALSE)) AS "Tasks 7-14 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '15 day' AND now() - interval '30 day'
                AND t.complete = FALSE)) AS "Tasks 15-30 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '31 day' AND now() - interval '60 day'
                AND t.complete = FALSE)) AS "Tasks 31-60 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date BETWEEN now() - interval '61 day' AND now() - interval '90 day'
                AND t.complete = FALSE)) AS "Tasks 61-90 days",
       sum((SELECT count(*)
              FROM task t
              WHERE t.claim_id = c.id
                AND t.created_date <= now() - interval '91 day'
                AND t.complete = FALSE)) AS "90+ days"
FROM task t1,
     claim c
WHERE c.claim_owner_id IS NULL
  AND c.workgroup_id IS NULL
  AND t1.claim_id = c.id
  AND c.insurer_id = insid
GROUP BY 1,
         2
ORDER BY 2, 1 ASC;


END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION subscriber_task_report(int) TO chox_user;
GRANT EXECUTE ON FUNCTION subscriber_task_report(int) TO chox_mi;