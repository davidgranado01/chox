-- Function: equity_weekly_action_management_report_part1()

-- DROP FUNCTION equity_weekly_action_management_report_part1();

CREATE OR REPLACE FUNCTION equity_weekly_action_management_report_part1()
  RETURNS TABLE("Name" text, total bigint, "0_3_days" bigint, "3_7_days" bigint, over_7_days bigint, date_of_oldest timestamp without time zone, "Heading" text, catagory text) AS
$BODY$ 
DECLARE
wgsitename text;
claimstatus text;
headingname text;
webuserRecord record;
    
     BEGIN
   --  RAISE NOTICE 'Generating 1st part of the report';
       FOR i IN 1..2 LOOP
        IF i=1 THEN
            claimstatus = 'ClaimUnacknowledgedUnassigned';
            headingname = 'New Claims to be Assigned';
        ELSE
            claimstatus = 'ClaimUnacknowledgedRouted';
            headingname = 'Claims Awaiting Acknowledgement';
        END IF;
         
         FOR i IN 1..2 LOOP
         IF i=1 THEN
            wgsitename = 'Cat 1 Swansea';
         ELSE
            wgsitename = 'Cat 2 Swansea';
         END IF;
         RETURN QUERY
 
           select substring(wgsitename from 1 for 5) as "Name",
            
           (select 
               count(*)
            from 
              claim c inner join insurer ins on c.insurer_id = ins.id 
              inner join workgroup wg on wg.insurer_id = ins.id
              inner join audit_trail a on a.claim_id = c.id
           where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and ins.id = 20 
   and wg.site = wgsitename
   and c.workgroup_id = wg.id) as total,
   
(select 
    count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and ins.id = 20 
   and wg.site = wgsitename
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) <= 3) as "0_3_days",
   
(select 
    count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and ins.id = 20 
   and wg.site = wgsitename
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) > 3
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) <= 7) as "3_7_days",
   
(select 
    count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and ins.id = 20 
   and wg.site = wgsitename
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) > 7) as over_7_days,
   
(select 
    min(a.created_date)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and ins.id = 20 
   and wg.site = wgsitename
   and c.workgroup_id = wg.id) as date_of_oldest,
      
(select headingname) as headingName,

(select ''::text) as catagory;

         END LOOP; 
      END LOOP;
 -- RAISE NOTICE 'Generating 2nd part of the report';
     FOR i IN 1..2 LOOP
       IF i=1 THEN
            wgsitename = 'Cat 1 Swansea';
       ELSE
            wgsitename = 'Cat 2 Swansea';
       END IF;
 -- RAISE NOTICE 'Generating for %',workgroupcatagory;
       FOR i IN 1..5 LOOP
        IF i=1 THEN
            claimstatus = 'InvoiceApprovedByBRE';
            headingname = 'Invoices Approved by BRE';
        ELSIF i=2 THEN
            claimstatus = 'InvoiceEscalatedToHandler';
            headingname = 'Invoices Escalated to Handler';
        ELSIF i=3 THEN
            claimstatus = 'AwaitingInvoicePayment';
            headingname = 'Approved Invoices Awaiting Payment';
        ELSIF i=4 THEN
            claimstatus = 'ManualInvoiceBREApproved';
            headingname = 'Manual Invoices Approved by BRE';
        ELSIF i=5 THEN
            claimstatus = 'ManualInvoiceBRERejected';
            headingname = 'Manual Invoices Rejected by BRE';
        END IF;
   -- RAISE NOTICE 'Generating for claim status %',headingname;
         FOR webuserRecord IN 
         select * from web_user wu where wu.id in (select wuwg.user_id from web_user_workgroup wuwg where wuwg.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = 20 and wg.site = wgsitename)) order by wu.last_name
         LOOP
         
         RETURN QUERY
 
          select webuserRecord.first_name || ' ' || webuserRecord.last_name as "Name",
            
          (select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id) as total,

(select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) <= 3) as "0_3_days",

(select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) > 3
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) <= 7) as "3_7_days",

(select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) > 7) as over_7_days,

(select 
   min(a.created_date)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id) as date_of_oldest,
      
(select headingname) as headingName,

(select substring(wgsitename from 1 for 5)) as catagory;

         END LOOP; 
      END LOOP;
     END LOOP;
 -- RAISE NOTICE 'Report Generation finished.';
    END; 
    $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION equity_weekly_action_management_report_part1()
  OWNER TO chox;



-------------------------------------------------------------------------------------------------------------------------

                                   --------------     SECOND PART   --------------

-------------------------------------------------------------------------------------------------------------------------


-- Function: equity_weekly_action_management_report_part2()

-- DROP FUNCTION equity_weekly_action_management_report_part2();

CREATE OR REPLACE FUNCTION equity_weekly_action_management_report_part2()
  RETURNS TABLE("Name" text, total bigint, "0_30_days" bigint, "30_60_days" bigint, "60_90_days" bigint, over_90_days bigint, "Heading" text, catagory text) AS
$BODY$ 
DECLARE
wgsitename text;
claimstatus text;
headingname text;
webuserRecord record;
    
     BEGIN
  
     FOR i IN 1..2 LOOP
       IF i=1 THEN
            wgsitename = 'Cat 1 Swansea';
       ELSE
            wgsitename = 'Cat 2 Swansea';
       END IF;
 -- RAISE NOTICE 'Generating for %',workgroupcatagory;
       FOR i IN 1..2 LOOP
        
        IF i=1 THEN
            claimstatus = 'AwaitingLiabilityResolution';
            headingname = 'Approved Invoices Liability Resolution';
   ELSIF i=2 THEN
            claimstatus = 'ContestedInvoiceReferredToInsurer';
            headingname = 'Contested Invoices Referred to Insurer';
        END IF;
   -- RAISE NOTICE 'Generating for claim status %',headingname;
         FOR webuserRecord IN 
         select * from web_user wu where wu.id in (select wuwg.user_id from web_user_workgroup wuwg where wuwg.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = 20 and wg.site = wgsitename)) order by wu.last_name
         LOOP
         
         RETURN QUERY
 
          select webuserRecord.first_name || ' ' || webuserRecord.last_name as "Name",
            
          (select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id) as total,

(select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) <= 30) as "0_30_days",

(select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) > 30
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) <= 60) as "30_60_days",

          (select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) > 60
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) <= 90) as "60_90_days",
   

(select 
   count(*)
from 
   claim c inner join insurer ins on c.insurer_id = ins.id 
   inner join workgroup wg on wg.insurer_id = ins.id
   inner join audit_trail a on a.claim_id = c.id
where 
   c.status = claimstatus
   and a.new_status = c.status
   and a.reverted = false
   and not exists (select * FROM audit_trail a2 where a2.claim_id = a.claim_id and a2.reverted = false and a2.created_date > a.created_date and a2.new_status = a.new_status)
   and ins.id = 20 
   and wg.site = wgsitename
   and c.claim_owner_id = webuserRecord.id
   and c.workgroup_id = wg.id
   and ((extract (epoch from (now() - a.created_date)))/(3600*24)) > 90) as over_90_days,


(select headingname) as headingName,

(select substring(wgsitename from 1 for 5)) as catagory;

         END LOOP; 
      END LOOP;
     END LOOP;
 -- RAISE NOTICE 'Report Generation finished.';
    END; 
    $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION equity_weekly_action_management_report_part2()
  OWNER TO chox;


-------------------------------------------------------------------------------------------------------------------------

                                   --------------     THIRD PART   --------------

-------------------------------------------------------------------------------------------------------------------------



-- Function: equity_weekly_action_management_report_part3()

-- DROP FUNCTION equity_weekly_action_management_report_part3();

CREATE OR REPLACE FUNCTION equity_weekly_action_management_report_part3()
  RETURNS TABLE("Name" text, total bigint, "0_3_days" bigint, "3_7_days" bigint, over_7_days bigint, date_of_oldest timestamp without time zone, "Heading" text, catagory text) AS
$BODY$ 
DECLARE
wgsitename text;
headingname text;
webuserRecord record;
    
     BEGIN
     
     headingname = 'Tasks Requiring Action';
 -- RAISE NOTICE 'Generating 2nd part of the report';
     FOR i IN 1..2 LOOP
       IF i=1 THEN
            wgsitename = 'Cat 1 Swansea';
       ELSE
            wgsitename = 'Cat 2 Swansea';
       END IF;
 -- RAISE NOTICE 'Generating for %',workgroupcatagory;
       
   -- RAISE NOTICE 'Generating for claim status %',headingname;
         FOR webuserRecord IN 
         select * from web_user wu where wu.id in (select wuwg.user_id from web_user_workgroup wuwg where wuwg.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = 20 and wg.site = wgsitename)) order by wu.last_name
         LOOP
         
         RETURN QUERY
 
          select webuserRecord.first_name || ' ' || webuserRecord.last_name as "Name",
            
(select 
    count(*)
from 
   task t left outer join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
           left outer join (select web_user_role_id, web_user_id from web_user_user_role wuur where wuur.web_user_id = webuserRecord.id) as wuur on wuur.web_user_role_id = wur.id
 where 
    t.complete = false 
    and ((t.visibility = 1 and t.insurer is true) or (t.visibility = 2 and t.insurer is true) or (t.visibility = 3 and t.insurer is false))
    and ((c.insurer_id = 20 and c.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = c.insurer_id and wg.site = wgsitename)) 
          or (c.insurer_id is null and w.insurer_id = 20 and w.id in (select wuw.user_id from web_user_workgroup wuw where wuw.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = w.insurer_id and wg.site = wgsitename)))
          or (c.insurer_id is null and w.insurer_id = 20 ))
    and ((t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)  -- external task assigned to the claim owner 
          -- external task assigned to the user who's role is same as visibility role of the task. taken from TaskServiceImpl.java class.
         or (t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))  
          -- internal task created by the same insurer, linked to the claim and assigned to the claim owner
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)
          -- internal task created by the same insurer, linked to the claim and assigned to the user who's role is same as visibility role of the task.
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_MNG','ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))
          -- internal task created by the same insurer, not linked to the claim and assigned to the user whose role is same as visibility role fo the task
         or (t.visibility = 2 and c.id is null and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null) 
          -- private task raised by the user
         or (t.visibility = 1 and (w.id = webuserRecord.id or w.id = 999)))) as total,


(select 
    count(*)
from 
   task t left outer join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
           left outer join (select web_user_role_id, web_user_id from web_user_user_role wuur where wuur.web_user_id = webuserRecord.id) as wuur on wuur.web_user_role_id = wur.id
 where 
    t.complete = false 
    and ((t.visibility = 1 and t.insurer is true) or (t.visibility = 2 and t.insurer is true) or (t.visibility = 3 and t.insurer is false))
    and ((c.insurer_id = 20 and c.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = c.insurer_id and wg.site = wgsitename)) 
          or (c.insurer_id is null and w.insurer_id = 20 and w.id in (select wuw.user_id from web_user_workgroup wuw where wuw.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = w.insurer_id and wg.site = wgsitename)))
          or (c.insurer_id is null and w.insurer_id = 20 ))
    and ((t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)  -- external task assigned to the claim owner 
          -- external task assigned to the user who's role is same as visibility role of the task. taken from TaskServiceImpl.java class.
         or (t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))  
          -- internal task created by the same insurer, linked to the claim and assigned to the claim owner
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)
          -- internal task created by the same insurer, linked to the claim and assigned to the user who's role is same as visibility role of the task.
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_MNG','ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))
          -- internal task created by the same insurer, not linked to the claim and assigned to the user whose role is same as visibility role fo the task
         or (t.visibility = 2 and c.id is null and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null) 
          -- private task raised by the user
         or (t.visibility = 1 and (w.id = webuserRecord.id or w.id = 999)))
    and ((extract (epoch from (now() - t.created_date)))/(3600*24)) <= 3) as "0_3_days",
         


(select 
    count(*)
from 
   task t left outer join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
           left outer join (select web_user_role_id, web_user_id from web_user_user_role wuur where wuur.web_user_id = webuserRecord.id) as wuur on wuur.web_user_role_id = wur.id
 where 
    t.complete = false 
    and ((t.visibility = 1 and t.insurer is true) or (t.visibility = 2 and t.insurer is true) or (t.visibility = 3 and t.insurer is false))
    and ((c.insurer_id = 20 and c.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = c.insurer_id and wg.site = wgsitename)) 
          or (c.insurer_id is null and w.insurer_id = 20 and w.id in (select wuw.user_id from web_user_workgroup wuw where wuw.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = w.insurer_id and wg.site = wgsitename)))
          or (c.insurer_id is null and w.insurer_id = 20 ))
    and ((t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)  -- external task assigned to the claim owner 
          -- external task assigned to the user who's role is same as visibility role of the task. taken from TaskServiceImpl.java class.
         or (t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))  
          -- internal task created by the same insurer, linked to the claim and assigned to the claim owner
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)
          -- internal task created by the same insurer, linked to the claim and assigned to the user who's role is same as visibility role of the task.
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_MNG','ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))
          -- internal task created by the same insurer, not linked to the claim and assigned to the user whose role is same as visibility role fo the task
         or (t.visibility = 2 and c.id is null and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null) 
          -- private task raised by the user
         or (t.visibility = 1 and (w.id = webuserRecord.id or w.id = 999)))
    and ((extract (epoch from (now() - t.created_date)))/(3600*24)) > 3
    and ((extract (epoch from (now() - t.created_date)))/(3600*24)) <= 7) as "3_7_days",
    

(select 
    count(*)
from 
   task t left outer join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
           left outer join (select web_user_role_id, web_user_id from web_user_user_role wuur where wuur.web_user_id = webuserRecord.id) as wuur on wuur.web_user_role_id = wur.id
 where 
    t.complete = false 
    and ((t.visibility = 1 and t.insurer is true) or (t.visibility = 2 and t.insurer is true) or (t.visibility = 3 and t.insurer is false))
    and ((c.insurer_id = 20 and c.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = c.insurer_id and wg.site = wgsitename)) 
          or (c.insurer_id is null and w.insurer_id = 20 and w.id in (select wuw.user_id from web_user_workgroup wuw where wuw.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = w.insurer_id and wg.site = wgsitename)))
          or (c.insurer_id is null and w.insurer_id = 20 ))
    and ((t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)  -- external task assigned to the claim owner 
          -- external task assigned to the user who's role is same as visibility role of the task. taken from TaskServiceImpl.java class.
         or (t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))  
          -- internal task created by the same insurer, linked to the claim and assigned to the claim owner
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)
          -- internal task created by the same insurer, linked to the claim and assigned to the user who's role is same as visibility role of the task.
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_MNG','ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))
          -- internal task created by the same insurer, not linked to the claim and assigned to the user whose role is same as visibility role fo the task
         or (t.visibility = 2 and c.id is null and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null) 
          -- private task raised by the user
         or (t.visibility = 1 and (w.id = webuserRecord.id or w.id = 999)))
    and ((extract (epoch from (now() - t.created_date)))/(3600*24)) > 7) as over_7_days,


(select 
   min(t.created_date)
from 
   task t left outer join claim c on (c.id = t.claim_id)
           left outer join web_user w on ( w.id = t.created_by)
           left outer join web_user w2 on ( w2.id = c.claim_owner_id)
           left outer join insurer ins on (ins.id = w.insurer_id)
           left outer join chorganisation cho on (cho.id = w.chorganisation_id)
           left outer join web_user_role wur on (wur.name = t.visibility_role)
           left outer join (select web_user_role_id, web_user_id from web_user_user_role wuur where wuur.web_user_id = webuserRecord.id) as wuur on wuur.web_user_role_id = wur.id
where 
    t.complete = false 
    and ((t.visibility = 1 and t.insurer is true) or (t.visibility = 2 and t.insurer is true) or (t.visibility = 3 and t.insurer is false))
    and ((c.insurer_id = 20 and c.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = c.insurer_id and wg.site = wgsitename)) 
          or (c.insurer_id is null and w.insurer_id = 20 and w.id in (select wuw.user_id from web_user_workgroup wuw where wuw.workgroup_id in (select wg.id from workgroup wg where wg.insurer_id = w.insurer_id and wg.site = wgsitename)))
          or (c.insurer_id is null and w.insurer_id = 20 ))
    and ((t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)  -- external task assigned to the claim owner 
          -- external task assigned to the user who's role is same as visibility role of the task. taken from TaskServiceImpl.java class.
         or (t.visibility = 3 and c.insurer_id = 20 and wur.id is not null and w.insurer_id is null and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))  
          -- internal task created by the same insurer, linked to the claim and assigned to the claim owner
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and w2.id = webuserRecord.id and wuur.web_user_role_id is not null)
          -- internal task created by the same insurer, linked to the claim and assigned to the user who's role is same as visibility role of the task.
         or (t.visibility = 2 and c.insurer_id = 20 and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null and wur.name in ('ROLE_INS_MNG','ROLE_INS_PC', 'ROLE_INS_SCR', 'ROLE_INS_CR', 'ROLE_INS_FNOL'))
          -- internal task created by the same insurer, not linked to the claim and assigned to the user whose role is same as visibility role fo the task
         or (t.visibility = 2 and c.id is null and wur.id is not null and (w.insurer_id = 20 or w.id = 999 ) and wuur.web_user_role_id is not null) 
          -- private task raised by the user
         or (t.visibility = 1 and (w.id = webuserRecord.id or w.id = 999)))) as date_of_oldest,
      
(select headingname) as headingName,

(select substring(wgsitename from 1 for 5)) as catagory;

         END LOOP; 
      
     END LOOP;
 -- RAISE NOTICE 'Report Generation finished.';
    END; 
    $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION equity_weekly_action_management_report_part3()
  OWNER TO chox;