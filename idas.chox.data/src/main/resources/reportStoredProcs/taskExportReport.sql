DROP FUNCTION taskExportReport(character varying, numeric, IN integer[]);
CREATE OR REPLACE FUNCTION taskExportReport(IN org character varying, IN orgid numeric, IN integer[])
  RETURNS TABLE("Supplier Reference" character varying,
                "Workgroup" character varying,
                "Current CHOX Status" character varying,   
                "Status Of Claim When Task Created" character varying, 
                "Due Date" timestamp without time zone,
                "Task Type" character varying,
                "Description" character varying,
                "Created Date" timestamp without time zone,
                "Created By" text,
                "Owner" text,
                "Role Assigned To" character varying) AS
$BODY$
DECLARE
  org ALIAS FOR $1;
  orgId ALIAS FOR $2;
BEGIN

IF org ILIKE 'INS' THEN

 RETURN QUERY

   select 
    c.cho_reference as "Supplier Reference",
    wkgp.name as "Workgroup",
    c.status as "Current CHOX Status",
    a.new_status "Status Of Claim When Task Created",
    t.due_date as "Due Date",
    t.task_type as "Task Type", 
    t.description as "Description",
    t.created_date as "Created Date", 
    case when (ins is null and cho is null) then 'System' 
         when cho is null then w.first_name || ' ' || w.last_name || ' (' || ins.name || ')' 
         else w.first_name || ' ' || w.last_name || ' (' || cho.name || ')' end as "Created By", 
    case when (t.visibility_role = 'ROLE_INS_CH') then w2.first_name || ' ' || w2.last_name
         else 'N/A' end  as "Owner",
    wur.description as "Role Assigned To"
  from 
    task t inner join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
           left outer join workgroup wkgp on (wkgp.id = c.workgroup_id)
           inner join audit_trail a on (a.claim_id = c.id)
  where 
    t.complete = false 
    and ((t.visibility = 2 and t.insurer is true) or (t.visibility = 3 and t.insurer is false))
    and (c.insurer_id = orgId or (c.insurer_id is null and w.insurer_id = orgId))
    and ((-1 = ANY ($3)) OR (c.claim_type = ANY ($3)))
    and a.id = (select id from audit_trail at where at.claim_id=c.id and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and ((a3.created_date <= t.created_date and reverted = false) or (a3.created_date <= t.created_date and reverted = true and last_modified_date > t.created_date))) order by id desc limit 1)
  order by c.cho_reference, t.created_date;


ELSIF org ILIKE 'CHO' THEN

 RETURN QUERY

    select 
    c.cho_reference as "Supplier Reference", 
    '-' as "Workgroup",
    c.status as "Current CHOX Status", 
    a.new_status "Status Of Claim When Task Created",
    t.due_date as "Due Date",
    t.task_type as "Task Type",
    t.description as "Description",
    t.created_date as "Created Date",
    case when (ins is null and cho is null) then 'System' 
         when cho is null then w.first_name || ' ' || w.last_name || ' (' || ins.name || ')' 
         else w.first_name || ' ' || w.last_name || ' (' || cho.name || ')' end as "Created By", 
    w2.first_name || ' ' || w2.last_name as "Owner", 
    wur.description as "Role Assigned To"
  from 
    task t inner join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.cho_claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
           inner join audit_trail a on (a.claim_id = c.id)
  where
    t.complete = false 
    and ((t.visibility = 2 and t.insurer is false) or (t.visibility = 3 and t.insurer is true))
    and (c.chorganisation_id = orgId or (c.chorganisation_id is null and w.chorganisation_id = orgId))
    and ((-1 = ANY ($3)) OR (c.claim_type = ANY ($3)))
    and a.id = (select id from audit_trail at where at.claim_id=c.id and at.created_date = (select max(created_date) as max_created_date from audit_trail a3 where a3.claim_id = c.id and ((a3.created_date <= t.created_date and reverted = false) or (a3.created_date <= t.created_date and reverted = true and last_modified_date > t.created_date))) order by id desc limit 1)
    order by c.cho_reference, t.created_date;      


ELSE

 RAISE NOTICE 'Usage example : select * from taskExport("CHO", 01, "{-1}"::INT[]); (or) select * from taskExport("INS", 01, "{-1}"::INT[]);';

END IF; 

RETURN;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;

GRANT EXECUTE ON FUNCTION taskExportReport(character varying, numeric, IN integer[]) TO chox_user;
GRANT EXECUTE ON FUNCTION taskExportReport(character varying, numeric, IN integer[]) TO chox_mi;
