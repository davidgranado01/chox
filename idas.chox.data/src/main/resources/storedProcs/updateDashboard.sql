CREATE OR REPLACE FUNCTION updateDashboard(integer)
  RETURNS boolean AS
$BODY$

DECLARE
currentDate timestamp;
userId int;
timeMarker timestamp;

BEGIN

currentDate=now();
userId=$1;

timeMarker=now();


--RAISE NOTICE 'Starting insert: %1', timeofday();


-- INSERT ALL RECORD PER INSURER / CHO / Workgroup / Claim Owner / CHO Claim Owner
insert into dashboard(process_date, insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id)
select distinct currentDate, insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id from claim;

--RAISE NOTICE 'Finished insert: %1', timeofday();
--RAISE NOTICE 'Total Number of Claim Notifications Submitted: %1', timeofday();


--
-- UPDATE Total Number of Claim Notifications Submitted
--
--   Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();

update dashboard
   set num_claims_submitted_w = t1.claimSubNumWeek
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as claimSubNumWeek
from claim c
where created_date >= SqlGetDayOfWeek() and (claim_type not in (2,6,9,10,14,15,16,17))
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id ) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Monthly
--RAISE NOTICE 'Monthly Start: %1', timeofday();
update dashboard
   set num_claims_submitted_m = t1.claimSubNumMon
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as claimSubNumMon
from claim c 
where created_date >= SqlGetDayOfMonth() and (claim_type not in (2,6,9,10,14,15,16,17))
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_claims_submitted_c = t1.claimSubNumCum
from (
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(*) as claimSubNumCum
from claim c
where claim_type not in (2,6,9,10,14,15,16,17)
group by  c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--RAISE NOTICE 'Total Number of Claim Notifications Accepted: %1', timeofday();
--
-- UPDATE Total Number of Claim Notifications Accepted
--
--  Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();
update dashboard
   set num_claims_accepted_w = t1.numClmAcc
from (
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as numClmAcc
from claim c, audit_trail a
where a.update_date >= SqlGetDayOfWeek()
and c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingCarHireInfo' and a2.update_date > a.update_date)
and a.new_status='AwaitingCarHireInfo'
group by  c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
  where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--   Monthly
--RAISE NOTICE 'Monthly Start: %1', timeofday();
update dashboard
   set num_claims_accepted_m = t1.num
from (
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where a.update_date >= SqlGetDayOfMonth()
and c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingCarHireInfo' and a2.update_date > a.update_date)
and a.new_status='AwaitingCarHireInfo'
  group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();
update dashboard
   set num_claims_accepted_c = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingCarHireInfo' and a2.update_date > a.update_date)
  and a.new_status = 'AwaitingCarHireInfo'
  group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--RAISE NOTICE 'Total Number of Claim Rejections Accepted: %1', timeofday();
--
-- UPDATE Total Number of Claim Rejections Accepted
--
--   Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();
update dashboard
   set num_claimrejections_accepted_w = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where a.update_date >= SqlGetDayOfWeek()
and c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  and (a.new_status = 'ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected'))
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--   Monthly
--RAISE NOTICE 'Monthly Start: %1', timeofday();
update dashboard
   set num_claimrejections_accepted_m = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where a.update_date >= SqlGetDayOfMonth()
and c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  and (a.new_status = 'ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected'))
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();
update dashboard
   set num_claimrejections_accepted_c = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
  where c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
    and (   (c.status = 'ClaimRejectionAccepted' and a.new_status = 'ClaimRejectionAccepted')
         or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected'))
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--RAISE NOTICE 'Number of Claims Awaiting To Be Processed: %1', timeofday();
--
-- UPDATE Number of Claims Awaiting To Be Processed
--           (the number of claims still in a pending state)
-- Only valid for cumulative total
--
--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();
update dashboard
   set num_claims_pending_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where status in  ('ClaimPending', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimUpdatedByEngineer', 'ClaimRejected',
    'ClaimRejectionContested', 'ClaimUnacknowledgedRouted', 'ClaimUnacknowledgedUnrouted', 'SubscriberClaimRejected', 'ClaimUnacknowledgedUnassigned')
    and (claim_type not in (10,14,15,16,17))
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--RAISE NOTICE 'Number of Claims Closed: %1', timeofday();
--
-- UPDATE Number of Claims Closed
--
--   Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();
update dashboard
   set num_claims_closed_w = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfWeek()
and a.new_status = 'ClaimClosed' and c.invoice_id is null
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, a.new_status, c.invoice_id ) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--   Monthly

--RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set num_claims_closed_m = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth()
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
and a.new_status = 'ClaimClosed' and c.invoice_id is null
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_claims_closed_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.id) as num
from claim c
  where status = 'ClaimClosed' and invoice_id is null and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--RAISE NOTICE 'Number of Invoices Submitted: %1', timeofday();
--
-- UPDATE Number of Invoices Submitted
--
--   Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();
update dashboard
   set num_invoices_submitted_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and i.created_date >= SqlGetDayOfWeek() and c.claim_type not in (4,5,6,10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Monthly
--RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set num_invoices_submitted_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));




--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_invoices_submitted_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--RAISE NOTICE 'Value of Invoices Submitted: %1', timeofday();
--
-- UPDATE Value of Invoices Submitted
--
--   Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();

update dashboard
   set val_invoices_submitted_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
and i.invoice_original_id = io.id
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));





--   Monthly
--RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set val_invoices_submitted_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
and i.invoice_original_id = io.id
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set val_invoices_submitted_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
and i.invoice_original_id = io.id
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--RAISE NOTICE 'Number of Invoices Accepted: %1', timeofday();
--
-- UPDATE Number of Invoices Accepted
--
--   Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();

update dashboard
   set num_invoices_accepted_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.invoice_id) as num
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
 and a.update_date >= SqlGetDayOfWeek()
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingInvoicePayment' and a2.update_date > a.update_date)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status='AwaitingInvoicePayment' and a2.new_status in ('InvoiceEscalatedToHandler', 'ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE','InvoiceEscalated','InvoiceReferredToClaimsHandler','InvoiceReferredToEngineer') and a2.update_date > a.update_date)
  and a.new_status = 'AwaitingInvoicePayment' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));





--   Monthly
--RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set num_invoices_accepted_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.invoice_id) as num
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
 and a.update_date >= SqlGetDayOfMonth()
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status='AwaitingInvoicePayment' and a2.update_date > a.update_date)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status='AwaitingInvoicePayment' and a2.new_status in ('InvoiceEscalatedToHandler', 'ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE','InvoiceEscalated','InvoiceReferredToClaimsHandler','InvoiceReferredToEngineer') and a2.update_date > a.update_date)
  and a.new_status = 'AwaitingInvoicePayment' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative

--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_invoices_accepted_c= t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.invoice_id) as num
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
  and a.new_status = 'AwaitingInvoicePayment' and claim_type NOT IN (10,14,15,16,17)
 and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingInvoicePayment' and a2.update_date > a.update_date)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status='AwaitingInvoicePayment' and a2.new_status in ('InvoiceEscalatedToHandler', 'ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE','InvoiceEscalated','InvoiceReferredToClaimsHandler','InvoiceReferredToEngineer') and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--RAISE NOTICE 'Value of Invoices Accepted: %1', timeofday();
--RAISE NOTICE 'Weekly Start: %1', timeofday();
--
-- UPDATE Value of Invoices Accepted
--
--   Weekly

update dashboard
   set val_invoices_accepted_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
 and a.update_date >= SqlGetDayOfWeek()
  and a.new_status = 'AwaitingInvoicePayment' and claim_type NOT IN (10,14,15,16,17)
 and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingInvoicePayment' and a2.update_date > a.update_date)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status='AwaitingInvoicePayment' and a2.new_status in ('InvoiceEscalatedToHandler', 'ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE','InvoiceEscalated','InvoiceReferredToClaimsHandler','InvoiceReferredToEngineer') and a2.update_date > a.update_date)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));





--   Monthly
--RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set val_invoices_accepted_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
 and a.update_date >= SqlGetDayOfMonth()
  and a.new_status = 'AwaitingInvoicePayment' and claim_type NOT IN (10,14,15,16,17)
 and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingInvoicePayment' and a2.update_date > a.update_date)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status='AwaitingInvoicePayment' and a2.new_status in ('InvoiceEscalatedToHandler', 'ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE','InvoiceEscalated','InvoiceReferredToClaimsHandler','InvoiceReferredToEngineer') and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));




--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set val_invoices_accepted_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
  and a.new_status = 'AwaitingInvoicePayment'
and c.id = a.claim_id and a.reverted = false and claim_type NOT IN (10,14,15,16,17)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingInvoicePayment' and a2.update_date > a.update_date)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status='AwaitingInvoicePayment' and a2.new_status in ('InvoiceEscalatedToHandler', 'ContestedInvoiceReferredToInsurer', 'InvoiceApprovedByBRE','InvoiceEscalated','InvoiceReferredToClaimsHandler','InvoiceReferredToEngineer') and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--RAISE NOTICE 'Number of Invoices Rejected: %1', timeofday();
--
-- UPDATE Number of Invoices Rejected
--
--   Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();
update dashboard
   set num_invoices_rejected_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.invoice_id) as num
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
  and a.new_status = 'InvoiceRejectionAccepted' and claim_type NOT IN (10,14,15,16,17)
  and a.update_date >= SqlGetDayOfWeek()
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));




--   Monthly

--RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set num_invoices_rejected_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.invoice_id) as num
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
  and c.id = a.claim_id and a.reverted = false
  and a.update_date >= SqlGetDayOfMonth()
  and a.new_status = 'InvoiceRejectionAccepted' and claim_type NOT IN (10,14,15,16,17)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_invoices_rejected_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.invoice_id) as num
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
  and a.new_status = 'InvoiceRejectionAccepted' and claim_type NOT IN (10,14,15,16,17)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--RAISE NOTICE 'Value of Invoices Rejected: %1', timeofday();
--
-- UPDATE Value of Invoices Rejected
--
--   Weekly
--RAISE NOTICE 'Weekly Start: %1', timeofday();

update dashboard
   set val_invoices_rejected_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
  and a.update_date >= SqlGetDayOfWeek()
  and a.new_status = 'InvoiceRejectionAccepted' and claim_type NOT IN (10,14,15,16,17)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Monthly
--RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set val_invoices_rejected_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
  and a.update_date >= SqlGetDayOfMonth()
  and a.new_status = 'InvoiceRejectionAccepted' and claim_type NOT IN (10,14,15,16,17)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set val_invoices_rejected_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
  and c.id = a.claim_id and a.reverted = false
  and a.new_status = 'InvoiceRejectionAccepted' and claim_type NOT IN (10,14,15,16,17)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--RAISE NOTICE 'Number of Invoices Pending: %1', timeofday();
--
-- UPDATE Number of Invoices Pending
--
--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_invoices_pending_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where status in  ('ContestedInvoiceReferredToCHO',
'ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated',
 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer', 'InvoiceUnassigned')
    and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--RAISE NOTICE 'Value of Invoices Pending: %1', timeofday();
--
-- UPDATE Value of Invoices Pending
--
--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set val_invoices_pending_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c,invoice i
where c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
    and c.status in  ('ContestedInvoiceReferredToCHO',
'ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated',
 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer', 'InvoiceUnassigned')
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

--RAISE NOTICE 'Total Number of Invoices Awaiting Liability Resolution: %1', timeofday();
--
-- UPDATE Number of Invoices Awaiting Liability Resolution
--
--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_invoices_awaiting_liability_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
where invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
    and status = 'AwaitingLiabilityResolution'
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--RAISE NOTICE 'Total Value of Invoices Awaiting Liability Resolution: %1', timeofday();
--
-- UPDATE Value of Invoices Awaiting Liability Resolution
--
--   Cumulative
--RAISE NOTICE 'Cumulative Start: %1', timeofday();
update dashboard
   set val_invoices_awaiting_liability_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i 
where c.invoice_id = i.id
    and c.status = 'AwaitingLiabilityResolution' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Total Number of Invoices Closed: %1', timeofday();
--
-- UPDATE Number of Invoices Closed
--
--   Weekly
-- RAISE NOTICE 'Weekly Start';

update dashboard
   set num_invoices_closed_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and c.status='ClaimClosed' and a.new_status = 'ClaimClosed'
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set num_invoices_closed_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth() and a.new_status = 'ClaimClosed'
and c.status='ClaimClosed' and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_invoices_closed_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where c.status = 'ClaimClosed' and invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



-- RAISE NOTICE 'Total Value of Invoices Closed: %1', timeofday();
--
-- UPDATE Value of Invoices Closed
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update dashboard
   set val_invoices_closed_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id and claim_type NOT IN (10,14,15,16,17)
and c.invoice_id = i.id and a.reverted = false
and c.status='ClaimClosed' and a.new_status='ClaimClosed'
and a.update_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set val_invoices_closed_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id
and c.invoice_id = i.id
  and c.status = 'ClaimClosed' and claim_type NOT IN (10,14,15,16,17)
  and a.new_status = 'ClaimClosed' and a.reverted = false
and a.update_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set val_invoices_closed_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
  and c.status='ClaimClosed' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Number of Invoices Payment Logged: %1', timeofday();
--
-- UPDATE Number of Invoices Payment Logged
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update dashboard
   set num_invoices_logged_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id
  and c.status in ('InvoicePaymentLogged','PaymentReceived')
  and a.new_status = 'InvoicePaymentLogged' and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date and a2.new_status != 'PaymentReceived')
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set num_invoices_logged_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false
  and c.status in ('InvoicePaymentLogged', 'PaymentReceived')
and a.update_date >= SqlGetDayOfMonth()
  and a.new_status = 'InvoicePaymentLogged' and claim_type NOT IN (10,14,15,16,17)
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date and a2.new_status != 'PaymentReceived')
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_invoices_logged_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where c.status in ('InvoicePaymentLogged', 'PaymentReceived') and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



-- RAISE NOTICE 'Value of Invoices Payment Logged: %1', timeofday();
--
-- UPDATE Value of Invoices Payment Logged
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update dashboard
   set val_invoices_logged_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id
  and c.invoice_id = i.id and a.reverted = false
  and c.status in ('InvoicePaymentLogged', 'PaymentReceived')
  and a.new_status = 'InvoicePaymentLogged' and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfWeek()
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date and a2.new_status != 'PaymentReceived')
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();
update dashboard
   set val_invoices_logged_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id
  and c.invoice_id = i.id and a.reverted = false
  and a.new_status = 'InvoicePaymentLogged'
  and c.status in ('InvoicePaymentLogged', 'PaymentReceived') and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth()
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date and a2.new_status != 'PaymentReceived')
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set val_invoices_logged_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
  and c.status in ('InvoicePaymentLogged', 'PaymentReceived') and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Total Number of Invoices Received by CHO: %1', timeofday();
--
-- UPDATE Total Number of Claim Notifications Submitted
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update dashboard
   set num_invoices_received_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false
  and a.new_status = 'PaymentReceived'
  and c.status = 'PaymentReceived' and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfWeek()
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();
update dashboard
   set num_invoices_received_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false
  and a.new_status = 'PaymentReceived'
  and c.status = 'PaymentReceived' and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth()
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1  
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start';

update dashboard
   set num_invoices_received_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where status = 'PaymentReceived' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Value of Payments Received by CHO';
--
-- UPDATE Value of Payments Received by CHO
--
--   Weekly
-- RAISE NOTICE 'Weekly Start';
update dashboard
   set val_invoices_received_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id
  and c.invoice_id = i.id and a.reverted = false
  and a.new_status = 'PaymentReceived'
  and c.status = 'PaymentReceived' and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfWeek()
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start';

update dashboard
   set val_invoices_received_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id
  and c.invoice_id = i.id and a.reverted = false
  and a.new_status = 'PaymentReceived'
  and c.status = 'PaymentReceived' and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth()
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start';

update dashboard
   set val_invoices_received_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
  and c.status = 'PaymentReceived' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Total Value of Penalty Charges Applied';
--
-- UPDATE Total Value of Penalty Charges Applied
--
--   Weekly
-- RAISE NOTICE 'Weekly Start';

update dashboard
   set val_penalty_charges_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge + i.repair_penalty_charge) as val
from claim c, invoice i
where c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
and date(hire_penalty_charge_applied_date) >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start';

update dashboard
   set val_penalty_charges_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge + i.repair_penalty_charge) as val
from claim c, invoice i
where c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
and date(hire_penalty_charge_applied_date) >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start';

update dashboard
   set val_penalty_charges_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_penalty_charge) as val
from claim c, invoice i
where c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Total Value of Penalty Charges Paid';
--
-- UPDATE Total Value of Penalty Charges Paid
--
--   Weekly
-- RAISE NOTICE 'Weekly Start';

update dashboard
   set val_penalty_charges_paid_w = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) as val
from claim c, invoice i, audit_trail a
where c.invoice_id = i.id and c.id = a.claim_id
and a.new_status = 'InvoicePaymentLogged' and a.reverted=false
and date(a.created_date) >= SqlGetDayOfWeek() and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start';

update dashboard
   set val_penalty_charges_paid_m = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) as val
from claim c, invoice i, audit_trail a
where c.invoice_id = i.id and c.id = a.claim_id and claim_type NOT IN (10,14,15,16,17)
and a.new_status = 'InvoicePaymentLogged' and a.reverted=false
and date(a.created_date) >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start';

update dashboard
   set val_penalty_charges_paid_c = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.repair_penalty_charge_paid + i.hire_penalty_charge_paid) as val
from claim c, invoice i, audit_trail a
where c.invoice_id = i.id and c.id = a.claim_id
and a.new_status = 'InvoicePaymentLogged' and a.reverted=false and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Weekly
-- RAISE NOTICE 'Weekly Avg Inv Payment Time Start';

update dashboard
set avg_inv_payment_time_w = t1.total_day
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id,
cast(avg(EXTRACT(DAY FROM(a1.update_date - i.created_date)))as numeric(6,2)) as total_day 
         from claim c, audit_trail a1, invoice i  where  
                    c.id = a1.claim_id 
                    and c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
                    and a1.reverted=false and a1.new_status ='InvoicePaymentLogged'
                    and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id )
                    and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived') and c.id = a3.claim_id and a3.update_date > a1. update_date) 
                    and a1.update_date >= SqlGetDayOfWeek()
                    group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
                    where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Avg Inv Payment Time Start';


update dashboard
set avg_inv_payment_time_m = t1.total_day
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id,
cast(avg(EXTRACT(DAY FROM(a1.update_date - i.created_date)))as numeric(6,2)) as total_day 
           from claim c, audit_trail a1, invoice i  where  
                    c.id = a1.claim_id 
                    and c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
                    and a1.reverted=false and a1.new_status ='InvoicePaymentLogged'
                    and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id )
                    and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived') and c.id = a3.claim_id and a3.update_date > a1. update_date) 
                    and a1.update_date >= SqlGetDayOfMonth()
                    group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
                    where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Avg Inv Payment Time Start';


update dashboard
set avg_inv_payment_time_c = t1.total_day
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id,
cast(avg(EXTRACT(DAY FROM(a1.update_date - i.created_date)))as numeric(6,2)) as total_day 
          from claim c, audit_trail a1, invoice i  where  
                    c.id = a1.claim_id 
                    and c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
                    and a1.reverted=false and a1.new_status ='InvoicePaymentLogged'
                    and not exists (select * from audit_trail a2 where a2.reverted=false and a2.new_status = a1.new_status and a2.update_date < a1.update_date and c.id = a2.claim_id )
                    and not exists (select * from audit_trail a3 where a3.reverted=false and a3.original_status = a1.new_status and a3.new_status not in ('PaymentReceived') and c.id = a3.claim_id and a3.update_date > a1. update_date) 
                    group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
                    where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



update dashboard
   set num_manual_invoices_submitted_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



update dashboard
   set num_manual_invoices_submitted_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


update dashboard
   set num_manual_invoices_submitted_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



update dashboard
   set val_manual_invoices_submitted_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



update dashboard
   set val_manual_invoices_submitted_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


update dashboard
   set val_manual_invoices_submitted_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


update dashboard
   set num_manual_invoices_paid_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

update dashboard
   set num_manual_invoices_paid_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


update dashboard
   set num_manual_invoices_paid_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



update dashboard
   set val_manual_invoices_paid_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



update dashboard
   set val_manual_invoices_paid_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


update dashboard
   set val_manual_invoices_paid_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.status = 'ManualInvoicePaid'
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


update dashboard
   set num_manual_invoices_closed_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

update dashboard
   set num_manual_invoices_closed_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


update dashboard
   set num_manual_invoices_closed_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



update dashboard
   set val_manual_invoices_closed_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



update dashboard
   set val_manual_invoices_closed_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


update dashboard
   set val_manual_invoices_closed_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.status = 'ClaimClosed'
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

-- UPDATE Number of Invoices Awaiting Litigation Outcome
--
--   Weekly
-- RAISE NOTICE 'Weekly Start';

update dashboard
   set num_invoices_awaiting_litigation_outcome_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and c.status='AwaitingLitigationOutcome' and a.new_status = 'AwaitingLitigationOutcome'
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set num_invoices_awaiting_litigation_outcome_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth() and a.new_status = 'AwaitingLitigationOutcome'
and c.status='AwaitingLitigationOutcome' and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set num_invoices_awaiting_litigation_outcome_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where c.status = 'AwaitingLitigationOutcome' and invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--
-- UPDATE Value of Invoices Closed
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update dashboard
   set val_invoices_awaiting_litigation_outcome_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id 
and c.invoice_id = i.id and a.reverted = false and claim_type NOT IN (10,14,15,16,17)
and c.status='AwaitingLitigationOutcome' and a.new_status='AwaitingLitigationOutcome'
and a.update_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update dashboard
   set val_invoices_awaiting_litigation_outcome_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id
and c.invoice_id = i.id
  and c.status = 'AwaitingLitigationOutcome' and claim_type NOT IN (10,14,15,16,17)
  and a.new_status = 'AwaitingLitigationOutcome' and a.reverted = false
and a.update_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update dashboard
   set val_invoices_awaiting_litigation_outcome_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
  and c.status='AwaitingLitigationOutcome' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1 
where t1.insurer_id = dashboard.insurer_id
  and t1.chorganisation_id = dashboard.chorganisation_id
  and (t1.workgroup_id = dashboard.workgroup_id  or (t1.workgroup_id is null and dashboard.workgroup_id is null))
  and (t1.claim_owner_id = dashboard.claim_owner_id or (t1.claim_owner_id is null and dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  dashboard.cho_claim_owner_id is null));

  
-- RAISE NOTICE 'Deleting Previous entries';

--
-- DELETE previous entries
--
delete from dashboard where process_date < currentDate;

update dashboard set complete=true;


-- RAISE NOTICE 'Finished';

return true;

END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION updatedashboard(integer) TO chox_user;
