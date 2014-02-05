--drop function attachment_report(integer, integer[], character varying[], text, text, character varying[]);
-- To use, e.g.
-- select * from attachment_report(3, array[]::integer[], array[]::varchar[],
--                             '2013-01-01','2013-02-01',array[]::varchar[]);
--
-- select * from attachment_report(3, array[]::integer[], array['Engineer''s Reports']::varchar[],
--                             '2013-01-01','2014-02-01',array[]::varchar[]);
-- 
-- select * from attachment_report(3, array[]::integer[], array['Engineer''s Reports']::varchar[],
--                             '2014-01-27','2014-02-03',array['AwaitingCarHireInfo','ClaimUnacknowledgedUnrouted',
--                                                               'ClaimUnacknowledgedRouted','ClaimUnacknowledgedUnassigned',
--                                                               'ClaimPending','ClaimReferredToFNOL',
--                                                               'ClaimReferredToEngineer','ClaimUpdatedByEngineer',
--                                                               'ClaimRejectionContested']::varchar[]);
create or replace function attachment_report
(
    IN insurerId integer, IN choIds integer[], IN attachmentTypes character varying[],
    IN startPeriod text, IN endPeriod text, IN claimStatus character varying[]
)
returns table
(
   "Supplier Reference" character varying(128),
   "Attachment Type" character varying(28),
   "Description" text,
   "Date Attachment Uploaded" text,
   "Uploaded By" text,
   "Task Created" text,
   "Task Completed" text,
   "CHO Name" character varying(128),
   "Insurer Workgroup Name" character varying,
   "Insurer Claim Owner" text,
   "Current Claim Status" character varying(40)
)
as $$ DECLARE 
    startDate date;
    endDate date;
BEGIN 
    startDate = startPeriod::Date;
    endDate = endPeriod::Date;
RETURN QUERY

select  c.cho_reference as "Supplier Reference",
        a.category as "Attachment Type",
        substring(remarks from 0 for 40) as "Description",
        to_char(a.created_date, 'dd/mm/yyyy') as "Date Attachment Uploaded",
        (case when wu2.insurer_id is null then 'CHO' else 'Insurer' end) as "Uploaded By",
        (case when exists(select 1 from task t where t.claim_id=c.id and t.created_by = a.created_by and t.task_type='Attachment' and t.created_date between a.created_date - interval '1 second' and a.created_date + interval '1 second') then 'Yes' else 'No' end) as "Task Created",
        (case when exists(select 1 from task t where t.claim_id=c.id and t.created_by = a.created_by and t.task_type='Attachment' and t.created_date between a.created_date - interval '1 second' and a.created_date + interval '1 second') then (case when exists(select 1 from task t where t.claim_id=c.id and t.created_by = a.created_by and t.task_type='Attachment' and t.complete = true and t.created_date between a.created_date - interval '1 second' and a.created_date + interval '1 second') then 'Yes' else 'No' end) else '-' end) as "Task Completed",
        cho.name as "CHO Name",
        w.name as "Insurer Workgroup Name",
        wu.first_name || ' ' || wu.last_name as "Insurer Claim Owner",
        c.status as "Current Claim Status"
from attachment a, chorganisation cho, web_user wu, web_user wu2, claim c
  left outer join workgroup w on w.id = c.workgroup_id
where c.id=a.claim_id and c.chorganisation_id=cho.id
  and c.claim_owner_id = wu.id and a.created_by=wu2.id
  and c.insurer_id = insurerId
  and (case when array_length(choIds, 1) > 0 then c.chorganisation_id = ANY(choIds) else true end)
  and (case when array_length(attachmentTypes, 1) > 0 then a.category = ANY(attachmentTypes) else true end)
  and (case when array_length(claimStatus, 1) > 0 then c.status = ANY(claimStatus) else true end)
  and a.created_date between startDate and endDate
;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION attachment_report(integer, integer[], character varying[], text, text, character varying[]) TO chox_user;
GRANT EXECUTE ON FUNCTION attachment_report(integer, integer[], character varying[], text, text, character varying[]) TO chox_mi;
