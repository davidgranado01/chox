DROP FUNCTION taskexportreport(org character varying, orgid numeric, IN integer[]);

CREATE OR REPLACE FUNCTION taskExportReport(IN org character varying, IN orgid numeric, IN integer[])
  RETURNS TABLE(   "Supplier Reference" character varying,
                                    "Current CHOX Status" character varying,                                 
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
    c.status as "Current CHOX Status",
    t.due_date as "Due Date",
    t.task_type as "Task Type", 
    t.description as "Description",
    t.created_date as "Created Date", 
    case when (ins.name is null and cho.name is null) then 'System' 
              when cho.name is null then w.first_name || ' ' || w.last_name || ' (' || ins.name || ')' 
              else w.first_name || ' ' || w.last_name || ' (' || cho.name || ')' end as "Created By", 
    case when (t.visibility_role = 'ROLE_INS_CH' and c.id is not null) then w2.first_name || ' ' || w2.last_name else 'N/A' end  as "Owner",
    wur.description as "Role Assigned To"
  from 
    task t left outer join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
  where 
    t.complete = false 
    and ((t.visibility = 1 and t.insurer is true) or (t.visibility = 2 and t.insurer is true) or (t.visibility = 3 and t.insurer is false))
    and (c.insurer_id = orgId or (c.insurer_id is null and w.insurer_id = orgId))
    and ((c.id IS NULL) OR (-1 = ANY ($3)) OR (c.claim_type = ANY ($3)))
  order by c.cho_reference, t.created_date;


ELSIF org ILIKE 'CHO' THEN

 RETURN QUERY

    select 
    c.cho_reference as "Supplier Reference", 
    c.status as "Current CHOX Status",    
    t.due_date as "Due Date",
    t.task_type as "Task Type",
    t.description as "Description",
    t.created_date as "Created Date",
    case when (ins.name is null and cho.name is null) then 'System' 
              when cho.name is null then w.first_name || ' ' || w.last_name || ' (' || ins.name || ')' 
              else w.first_name || ' ' || w.last_name || ' (' || cho.name || ')' end as "Created By", 
    w2.first_name || ' ' || w2.last_name as "Owner", 
    wur.description as "Role Assigned To"
  from 
    task t left outer join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.cho_claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
  where
    t.complete = false 
    and ((t.visibility = 1 and t.insurer is false) or (t.visibility = 2 and t.insurer is false) or (t.visibility = 3 and t.insurer is true))
    and (c.chorganisation_id = orgId or (c.chorganisation_id is null and w.chorganisation_id = orgId))
    and ((c.id IS NULL) OR (-1 = ANY ($3)) OR (c.claim_type = ANY ($3)))
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