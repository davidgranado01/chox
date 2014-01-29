-----------------------------------------------------------------------
-- ToDo 7.8.1  - Ability to agree quantum on 'Insurer Upload' Claims --
--               and approve claim for payment                       --
-----------------------------------------------------------------------

-- First, change current accessibility of UpdateManualInvoicePaid activity to new UpdateManualInvoiceAgreeQuantum activity
UPDATE accessibility set name = 'activity.UpdateManualInvoiceAgreeQuantum.ManualInvoiceBREApproved'
WHERE name = 'activity.UpdateManualInvoicePaid.ManualInvoiceBREApproved';

UPDATE accessibility set name = 'activity.UpdateManualInvoiceAgreeQuantum.ManualInvoiceBRERejected'
WHERE name = 'activity.UpdateManualInvoicePaid.ManualInvoiceBRERejected';

UPDATE accessibility set name = 'activity.UpdateManualInvoiceAgreeQuantum.ManualInvoiceContested'
WHERE name = 'activity.UpdateManualInvoicePaid.ManualInvoiceContested';

-- Finally, add new accessibility entries for UpdateManualInvoicePaid activity
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check)
SELECT 'activity.UpdateManualInvoicePaid.AwaitingInvoicePayment', false, false;

INSERT INTO accessibility_item(accessibility_id, role, access_right)
SELECT id, 'ROLE_INS_CH', 2
FROM accessibility where name = 'activity.UpdateManualInvoicePaid.AwaitingInvoicePayment';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
SELECT id, 'ROLE_INS_MNG', 2
FROM accessibility where name = 'activity.UpdateManualInvoicePaid.AwaitingInvoicePayment';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
SELECT id, 'ROLE_INS_PC', 2
FROM accessibility where name = 'activity.UpdateManualInvoicePaid.AwaitingInvoicePayment';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
SELECT id, 'ROLE_CHOX_ADMIN', 2
FROM accessibility where name = 'activity.UpdateManualInvoicePaid.AwaitingInvoicePayment';

-----------------------------------------------------------------------
-- End of ToDo item 7.8.1                                            --
-----------------------------------------------------------------------



-----------------------------------------------------------------------
-- ToDo 7.8.5  - Report & Dashboard updates required for change in   --
--               Insurer Upload workflow                             --
-----------------------------------------------------------------------
ALTER TABLE dashboard ADD COLUMN num_manual_invoices_accepted_w integer;
ALTER TABLE dashboard ADD COLUMN num_manual_invoices_accepted_m integer;
ALTER TABLE dashboard ADD COLUMN num_manual_invoices_accepted_c integer;
ALTER TABLE dashboard ADD COLUMN val_manual_invoices_accepted_w numeric(10,2) not null default 0.00;
ALTER TABLE dashboard ADD COLUMN val_manual_invoices_accepted_m numeric(10,2) not null default 0.00;
ALTER TABLE dashboard ADD COLUMN val_manual_invoices_accepted_c numeric(10,2) not null default 0.00;
ALTER TABLE dashboard ADD COLUMN avg_manual_inv_payment_time_w numeric(10,2);
ALTER TABLE dashboard ADD COLUMN avg_manual_inv_payment_time_m numeric(10,2);
ALTER TABLE dashboard ADD COLUMN avg_manual_inv_payment_time_c numeric(10,2);

ALTER TABLE dashboard ADD COLUMN num_invoices_awaiting_liability_w integer;
ALTER TABLE dashboard ADD COLUMN val_invoices_awaiting_liability_w numeric(10,2) not null default 0.00;
ALTER TABLE dashboard ADD COLUMN num_invoices_awaiting_liability_m integer;
ALTER TABLE dashboard ADD COLUMN val_invoices_awaiting_liability_m numeric(10,2) not null default 0.00;

ALTER TABLE dashboard ADD COLUMN num_manual_invoices_awaiting_liability_w integer;
ALTER TABLE dashboard ADD COLUMN val_manual_invoices_awaiting_liability_w numeric(10,2) not null default 0.00;
ALTER TABLE dashboard ADD COLUMN num_manual_invoices_awaiting_liability_m integer;
ALTER TABLE dashboard ADD COLUMN val_manual_invoices_awaiting_liability_m numeric(10,2) not null default 0.00;
ALTER TABLE dashboard ADD COLUMN num_manual_invoices_awaiting_liability_c integer;
ALTER TABLE dashboard ADD COLUMN val_manual_invoices_awaiting_liability_c numeric(10,2) not null default 0.00;

ALTER TABLE dashboard ADD COLUMN val_manual_invoices_penalty_charges_paid_w numeric(10,2) not null default 0.00;
ALTER TABLE dashboard ADD COLUMN val_manual_invoices_penalty_charges_paid_m numeric(10,2) not null default 0.00;
ALTER TABLE dashboard ADD COLUMN val_manual_invoices_penalty_charges_paid_c numeric(10,2) not null default 0.00;



CREATE OR REPLACE FUNCTION updateDashboard(integer)
  RETURNS boolean AS
$BODY$

DECLARE
currentDate timestamp;
userId int;

BEGIN

currentDate=now();
userId=$1;

-- Create new (temporary) dashboard table
create temp table tmp_dashboard as
select * from dashboard where 1=2;

-- RAISE NOTICE 'Starting insert into temporary dashboard table: %1', timeofday();


-- INSERT ALL RECORD PER INSURER / CHO / Workgroup / Claim Owner / CHO Claim Owner
insert into tmp_dashboard(process_date, insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id)
select distinct currentDate, insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id from claim;


-- RAISE NOTICE 'Created indexes: %1', timeofday();

--
-- UPDATE Total Number of Claim Notifications Submitted
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set num_claims_submitted_w = t1.claimSubNumWeek
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as claimSubNumWeek
from claim c
where created_date >= SqlGetDayOfWeek() and (claim_type not in (2,6,9,10,14,15,16,17,20))
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id ) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();
update tmp_dashboard
   set num_claims_submitted_m = t1.claimSubNumMon
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as claimSubNumMon
from claim c
where created_date >= SqlGetDayOfMonth() and (claim_type not in (2,6,9,10,14,15,16,17,20))
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_claims_submitted_c = t1.claimSubNumCum
from (select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(*) as claimSubNumCum
from claim c
where claim_type not in (2,6,9,10,14,15,16,17,20)
group by  c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Total Number of Claim Notifications Accepted: %1', timeofday();
--
-- UPDATE Total Number of Claim Notifications Accepted
--
--  Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update tmp_dashboard
   set num_claims_accepted_w = t1.numClmAcc
from (
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as numClmAcc
from claim c, audit_trail a
where a.update_date >= SqlGetDayOfWeek()
and c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingCarHireInfo' and a2.update_date > a.update_date)
and a.new_status='AwaitingCarHireInfo'
group by  c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
  where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();
update tmp_dashboard
   set num_claims_accepted_m = t1.num
from (
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where a.update_date >= SqlGetDayOfMonth()
and c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingCarHireInfo' and a2.update_date > a.update_date)
and a.new_status='AwaitingCarHireInfo'
  group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();
update tmp_dashboard
   set num_claims_accepted_c = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.new_status='AwaitingCarHireInfo' and a2.update_date > a.update_date)
  and a.new_status = 'AwaitingCarHireInfo'
  group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Total Number of Claim Rejections Accepted: %1', timeofday();
--
-- UPDATE Total Number of Claim Rejections Accepted
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update tmp_dashboard
   set num_claimrejections_accepted_w = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where a.update_date >= SqlGetDayOfWeek()
and c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  and (a.new_status = 'ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected'))
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();
update tmp_dashboard
   set num_claimrejections_accepted_m = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where a.update_date >= SqlGetDayOfMonth()
and c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  and (a.new_status = 'ClaimRejectionAccepted' or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected'))
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();
update tmp_dashboard
   set num_claimrejections_accepted_c = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
  where c.id = a.claim_id and a.reverted = false and (claim_type not in (10,14,15,16,17))
    and (   (c.status = 'ClaimRejectionAccepted' and a.new_status = 'ClaimRejectionAccepted')
         or (a.new_status = 'AwaitingCarHireInfo' and a.original_status='SubscriberClaimRejected'))
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Number of Claims Awaiting To Be Processed: %1', timeofday();
--
-- UPDATE Number of Claims Awaiting To Be Processed
--           (the number of claims still in a pending state)
-- Only valid for cumulative total
--
--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();
update tmp_dashboard
   set num_claims_pending_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where status in  ('ClaimPending', 'ClaimReferredToEngineer', 'ClaimReferredToFNOL', 'ClaimUpdatedByEngineer', 'ClaimRejected',
    'ClaimRejectionContested', 'ClaimUnacknowledgedRouted', 'ClaimUnacknowledgedUnrouted', 'SubscriberClaimRejected', 'ClaimUnacknowledgedUnassigned')
    and (claim_type not in (10,14,15,16,17))
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Number of Claims Closed: %1', timeofday();
--
-- UPDATE Number of Claims Closed
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update tmp_dashboard
   set num_claims_closed_w = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfWeek()
and a.new_status = 'ClaimClosed' and c.invoice_id is null
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, a.new_status, c.invoice_id ) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

--   Monthly

-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_claims_closed_m = t1.num
from(
select c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id, count(distinct c.id) as num
from claim c, audit_trail a
where c.id = a.claim_id and a.reverted = false and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth()
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
and a.new_status = 'ClaimClosed' and c.invoice_id is null
group by c.insurer_id, c.chorganisation_id, c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_claims_closed_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.id) as num
from claim c
  where status = 'ClaimClosed' and invoice_id is null and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



-- RAISE NOTICE 'Number of Invoices Submitted: %1', timeofday();
--
-- UPDATE Number of Invoices Submitted
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update tmp_dashboard
   set num_invoices_submitted_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and i.created_date >= SqlGetDayOfWeek() and c.claim_type not in (4,5,6,10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_submitted_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));




--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_submitted_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Value of Invoices Submitted: %1', timeofday();
--
-- UPDATE Value of Invoices Submitted
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_submitted_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
and i.invoice_original_id = io.id
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));





--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_submitted_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
and i.invoice_original_id = io.id
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_submitted_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id and c.claim_type not in (4,5,6,10,14,15,16,17)
and i.invoice_original_id = io.id
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



-- RAISE NOTICE 'Number of Invoices Accepted: %1', timeofday();
--
-- UPDATE Number of Invoices Accepted
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));





--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



-- RAISE NOTICE 'Value of Invoices Accepted: %1', timeofday();
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
--
-- UPDATE Value of Invoices Accepted
--
--   Weekly

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Number of Invoices Rejected: %1', timeofday();
--
-- UPDATE Number of Invoices Rejected
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_rejected_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(distinct c.invoice_id) as num
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
and c.id = a.claim_id and a.reverted = false
  and a.new_status = 'InvoiceRejectionAccepted' and claim_type NOT IN (10,14,15,16,17)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Value of Invoices Rejected: %1', timeofday();
--
-- UPDATE Value of Invoices Rejected
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_rejected_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id=i.id
  and c.id = a.claim_id and a.reverted = false
  and a.new_status = 'InvoiceRejectionAccepted' and claim_type NOT IN (10,14,15,16,17)
  and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted = false and a2.original_status=a.new_status and a2.update_date > a.update_date)
  group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Number of Invoices Pending: %1', timeofday();
--
-- UPDATE Number of Invoices Pending
--
--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_pending_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where status in  ('ContestedInvoiceReferredToCHO',
'ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated',
 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer', 'InvoiceUnassigned')
    and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Value of Invoices Pending: %1', timeofday();
--
-- UPDATE Value of Invoices Pending
--
--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_pending_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c,invoice i
where c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
    and c.status in  ('ContestedInvoiceReferredToCHO',
'ContestedInvoiceReferredToInsurer','InvoiceApprovedByBRE','InvoiceDataCalculationIncorrect','InvoiceEscalated',
 'InvoiceReferredToClaimsHandler', 'InvoiceEscalatedToHandler', 'InvoiceReferredToEngineer', 'InvoiceUnassigned')
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Total Number of Invoices Awaiting Liability Resolution: %1', timeofday();
--
-- UPDATE Number of Invoices Awaiting Liability Resolution
--
--   Weekly
-- RAISE NOTICE 'Weekly Start';

update tmp_dashboard
   set num_invoices_awaiting_liability_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and c.status='AwaitingLiabilityResolution' and a.new_status = 'AwaitingLiabilityResolution'
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status ='AwaitingLiabilityResolution' and a2.created_date > a.created_date)
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_awaiting_liability_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth() and a.new_status = 'AwaitingLiabilityResolution'
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status ='AwaitingLiabilityResolution' and a2.created_date > a.created_date)
and c.status='AwaitingLiabilityResolution' and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

--
--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_awaiting_liability_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
where invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
    and status = 'AwaitingLiabilityResolution'
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Total Value of Invoices Awaiting Liability Resolution: %1', timeofday();
--
-- UPDATE Value of Invoices Awaiting Liability Resolution
---
--   Weekly
-- RAISE NOTICE 'Weekly Start';

update tmp_dashboard
   set val_invoices_awaiting_liability_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id and c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
and c.status='AwaitingLiabilityResolution' and a.new_status = 'AwaitingLiabilityResolution'
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status ='AwaitingLiabilityResolution' and a2.created_date > a.created_date)
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_awaiting_liability_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id = i.id and c.id = a.claim_id and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth() and a.new_status = 'AwaitingLiabilityResolution'
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status ='AwaitingLiabilityResolution' and a2.created_date > a.created_date)
and c.status='AwaitingLiabilityResolution' and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--
--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();
update tmp_dashboard
   set val_invoices_awaiting_liability_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
    and c.status = 'AwaitingLiabilityResolution' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Total Number of Invoices Closed: %1', timeofday();
--
-- UPDATE Number of Invoices Closed
--
--   Weekly
-- RAISE NOTICE 'Weekly Start';

update tmp_dashboard
   set num_invoices_closed_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and c.status='ClaimClosed' and a.new_status = 'ClaimClosed'
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_closed_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth() and a.new_status = 'ClaimClosed'
and c.status='ClaimClosed' and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_closed_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where c.status = 'ClaimClosed' and invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



-- RAISE NOTICE 'Total Value of Invoices Closed: %1', timeofday();
--
-- UPDATE Value of Invoices Closed
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_closed_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, audit_trail a, invoice i, invoice_original io
where c.id = a.claim_id and claim_type NOT IN (10,14,15,16,17)
and c.invoice_id = i.id and a.reverted = false and i.invoice_original_id = io.id
and c.status='ClaimClosed' and a.new_status='ClaimClosed'
and a.update_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_closed_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, audit_trail a, invoice i, invoice_original io
where c.id = a.claim_id
and c.invoice_id = i.id
  and c.status = 'ClaimClosed' and claim_type NOT IN (10,14,15,16,17)
  and a.new_status = 'ClaimClosed' and a.reverted = false and i.invoice_original_id = io.id
and a.update_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_closed_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id = i.id and i.invoice_original_id = io.id
  and c.status='ClaimClosed' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Number of Invoices Payment Logged: %1', timeofday();
--
-- UPDATE Number of Invoices Payment Logged
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_logged_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where c.status in ('InvoicePaymentLogged', 'PaymentReceived') and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



-- RAISE NOTICE 'Value of Invoices Payment Logged: %1', timeofday();
--
-- UPDATE Value of Invoices Payment Logged
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();
update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_logged_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
  and c.status in ('InvoicePaymentLogged', 'PaymentReceived') and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- -- RAISE NOTICE 'Total Number of Invoices Received by CHO: %1', timeofday();
--
-- UPDATE Total Number of Claim Notifications Submitted
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();
update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_received_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where status = 'PaymentReceived' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- -- RAISE NOTICE 'Value of Payments Received by CHO';
--
-- UPDATE Value of Payments Received by CHO
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_received_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
  and c.status = 'PaymentReceived' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Total Value of Penalty Charges Applied';
--
-- UPDATE Total Value of Penalty Charges Applied
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_penalty_charges_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge + i.repair_penalty_charge) as val
from claim c, invoice i
where c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
and date(hire_penalty_charge_applied_date) >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_penalty_charges_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge + i.repair_penalty_charge) as val
from claim c, invoice i
where c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
and date(hire_penalty_charge_applied_date) >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_penalty_charges_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_penalty_charge) as val
from claim c, invoice i
where c.invoice_id = i.id and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Total Value of Penalty Charges Paid';
--
-- UPDATE Total Value of Penalty Charges Paid
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_penalty_charges_paid_w = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) as val
from claim c, invoice i, audit_trail a
where c.invoice_id = i.id and c.id = a.claim_id
and a.new_status = 'InvoicePaymentLogged' and a.reverted=false
and date(a.created_date) >= SqlGetDayOfWeek() and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_penalty_charges_paid_m = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge_paid + i.repair_penalty_charge_paid) as val
from claim c, invoice i, audit_trail a
where c.invoice_id = i.id and c.id = a.claim_id and claim_type NOT IN (10,14,15,16,17)
and a.new_status = 'InvoicePaymentLogged' and a.reverted=false
and date(a.created_date) >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_penalty_charges_paid_c = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.repair_penalty_charge_paid + i.hire_penalty_charge_paid) as val
from claim c, invoice i, audit_trail a
where c.invoice_id = i.id and c.id = a.claim_id
and a.new_status = 'InvoicePaymentLogged' and a.reverted=false and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Weekly
-- RAISE NOTICE 'Weekly Avg Inv Payment Time Start: %1', timeofday();

update tmp_dashboard
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
                    where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Avg Inv Payment Time Start: %1', timeofday();


update tmp_dashboard
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
                    where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Avg Inv Payment Time Start: %1', timeofday();


update tmp_dashboard
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
                    where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Num insurer claims submitted Start';

-- RAISE NOTICE 'Weekly Start: %1', timeofday();
update tmp_dashboard
   set num_insurer_claims_submitted_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
where c.claim_type IN (14,15)
and c.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_insurer_claims_submitted_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
where c.claim_type IN (14,15)
and c.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_insurer_claims_submitted_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
where c.claim_type IN (14,15)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Num manual invoices submitted Start';

-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_submitted_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_submitted_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_submitted_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_submitted_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and i.created_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_submitted_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and i.created_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_submitted_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_paid_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
and c.status_modified_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_paid_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
and c.status_modified_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_paid_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_paid_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
and c.status_modified_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_paid_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ManualInvoicePaid'
and c.status_modified_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_paid_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.status = 'ManualInvoicePaid'
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_accepted_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'AwaitingInvoicePayment'
and c.status_modified_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_accepted_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'AwaitingInvoicePayment'
and c.status_modified_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_accepted_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'AwaitingInvoicePayment'
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_accepted_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'AwaitingInvoicePayment'
and c.status_modified_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_accepted_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'AwaitingInvoicePayment'
and c.status_modified_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_accepted_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id=i.id
and c.status = 'AwaitingInvoicePayment'
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_closed_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
and c.status_modified_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_closed_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
and c.status_modified_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_closed_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_awaiting_liability_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type IN (10,14,15,16,17)
and c.status='AwaitingLiabilityResolution' and a.new_status = 'AwaitingLiabilityResolution'
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status ='AwaitingLiabilityResolution' and a2.created_date > a.created_date)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_manual_invoices_awaiting_liability_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type IN (10,14,15,16,17)
and c.status='AwaitingLiabilityResolution' and a.new_status = 'AwaitingLiabilityResolution'
and a.update_date >= SqlGetDayOfMonth() and a.reverted = false
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status ='AwaitingLiabilityResolution' and a2.created_date > a.created_date)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Cumulative Start: %1', timeofday();
update tmp_dashboard
   set num_manual_invoices_awaiting_liability_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c , invoice i
where c.invoice_id=i.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'AwaitingLiabilityResolution'
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_closed_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
and c.status_modified_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_closed_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.claim_type IN (10,14,15,16,17)
and c.status = 'ClaimClosed'
and c.status_modified_date >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_closed_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(io.full_total_to_pay) as val
from claim c, invoice i, invoice_original io
where c.invoice_id=i.id
and i.invoice_original_id = io.id
and c.status = 'ClaimClosed'
and c.claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));

-- UPDATE Value of Invoices Awaiting Liability Resolution
---
--   Weekly
-- RAISE NOTICE 'Weekly Start';

update tmp_dashboard
   set val_manual_invoices_awaiting_liability_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id = i.id and c.id = a.claim_id and c.invoice_id is not null and claim_type IN (10,14,15,16,17)
and c.status='AwaitingLiabilityResolution' and a.new_status = 'AwaitingLiabilityResolution'
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status ='AwaitingLiabilityResolution' and a2.created_date > a.created_date)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_awaiting_liability_m = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.invoice_id = i.id and c.id = a.claim_id and c.invoice_id is not null and claim_type IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth() and a.new_status = 'AwaitingLiabilityResolution'
and c.status='AwaitingLiabilityResolution' and a.reverted = false
and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status ='AwaitingLiabilityResolution' and a2.created_date > a.created_date)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--
--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();
update tmp_dashboard
   set val_manual_invoices_awaiting_liability_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
    and c.status = 'AwaitingLiabilityResolution' and claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- UPDATE Number of Invoices Awaiting Litigation Outcome
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_awaiting_litigation_outcome_w = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and c.status='AwaitingLitigationOutcome' and a.new_status = 'AwaitingLitigationOutcome'
and a.update_date >= SqlGetDayOfWeek() and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_awaiting_litigation_outcome_m = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c, audit_trail a
where c.id = a.claim_id and c.invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
and a.update_date >= SqlGetDayOfMonth() and a.new_status = 'AwaitingLitigationOutcome'
and c.status='AwaitingLitigationOutcome' and a.reverted = false
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set num_invoices_awaiting_litigation_outcome_c = t1.num
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, count(*) as num
from claim c
  where c.status = 'AwaitingLitigationOutcome' and invoice_id is not null and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--
-- UPDATE Value of Invoices Closed
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_awaiting_litigation_outcome_w = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, audit_trail a, invoice i
where c.id = a.claim_id
and c.invoice_id = i.id and a.reverted = false and claim_type NOT IN (10,14,15,16,17)
and c.status='AwaitingLitigationOutcome' and a.new_status='AwaitingLitigationOutcome'
and a.update_date >= SqlGetDayOfWeek()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
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
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_invoices_awaiting_litigation_outcome_c = t1.val
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.total_to_pay) as val
from claim c, invoice i
where c.invoice_id = i.id
  and c.status='AwaitingLitigationOutcome' and claim_type NOT IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Weekly
-- RAISE NOTICE 'Weekly Avg Manual Inv Payment Time Start: %1', timeofday();

update tmp_dashboard
set avg_manual_inv_payment_time_w = t1.total_day
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id,
cast(avg(EXTRACT(DAY FROM(a1.update_date - i.created_date)))as numeric(6,2)) as total_day
         from claim c, audit_trail a1, invoice i
         where c.id = a1.claim_id
           and c.invoice_id = i.id
           and a1.reverted=false and a1.new_status ='ManualInvoicePaid'
           and a1.update_date >= SqlGetDayOfWeek()
         group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Avg Inv Payment Time Start: %1', timeofday();


update tmp_dashboard
set avg_manual_inv_payment_time_m = t1.total_day
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id,
cast(avg(EXTRACT(DAY FROM(a1.update_date - i.created_date)))as numeric(6,2)) as total_day
           from claim c, audit_trail a1, invoice i
           where c.id = a1.claim_id
             and c.invoice_id = i.id
             and a1.reverted=false and a1.new_status ='ManualInvoicePaid'
             and a1.update_date >= SqlGetDayOfMonth()
           group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Avg Inv Payment Time Start: %1', timeofday();


update tmp_dashboard
set avg_manual_inv_payment_time_c = t1.total_day
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id,
cast(avg(EXTRACT(DAY FROM(a1.update_date - i.created_date)))as numeric(6,2)) as total_day
          from claim c, audit_trail a1, invoice i
          where c.id = a1.claim_id
            and c.invoice_id = i.id
            and a1.reverted=false and a1.new_status ='ManualInvoicePaid'
          group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


-- UPDATE Total Value of Penalty Charges Paid got Insurer Invoices
--
--   Weekly
-- RAISE NOTICE 'Weekly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_penalty_charges_paid_w = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge + i.repair_penalty_charge) as val
from claim c, invoice i, audit_trail a
where c.invoice_id = i.id and c.id = a.claim_id
and a.new_status = 'ManualInvoicePaid' and a.reverted=false
and date(a.created_date) >= SqlGetDayOfWeek() and claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Monthly
-- RAISE NOTICE 'Monthly Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_penalty_charges_paid_m = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.hire_penalty_charge + i.repair_penalty_charge) as val
from claim c, invoice i, audit_trail a
where c.invoice_id = i.id and c.id = a.claim_id and claim_type IN (10,14,15,16,17)
and a.new_status = 'ManualInvoicePaid' and a.reverted=false
and date(a.created_date) >= SqlGetDayOfMonth()
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));


--   Cumulative
-- RAISE NOTICE 'Cumulative Start: %1', timeofday();

update tmp_dashboard
   set val_manual_invoices_penalty_charges_paid_c = coalesce(t1.val, 0.00)
from (
select c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id, sum(i.repair_penalty_charge + i.hire_penalty_charge) as val
from claim c, invoice i
where c.invoice_id = i.id
and c.status = 'ManualInvoicePaid' and claim_type IN (10,14,15,16,17)
group by  c.insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id) t1
where t1.insurer_id = tmp_dashboard.insurer_id
  and t1.chorganisation_id = tmp_dashboard.chorganisation_id
  and (t1.workgroup_id = tmp_dashboard.workgroup_id  or (t1.workgroup_id is null and tmp_dashboard.workgroup_id is null))
  and (t1.claim_owner_id = tmp_dashboard.claim_owner_id or (t1.claim_owner_id is null and tmp_dashboard.claim_owner_id is null))
  and (t1.cho_claim_owner_id = tmp_dashboard.cho_claim_owner_id or (t1.cho_claim_owner_id is null and  tmp_dashboard.cho_claim_owner_id is null));



-- RAISE NOTICE 'Truncating dashboard tabel: %1', timeofday();
truncate table dashboard;


-- RAISE NOTICE 'Inserting into dashboard: %1', timeofday();
insert into dashboard( process_date, insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id,
                       num_claims_submitted_w, num_claims_submitted_m, num_claims_submitted_c,
                       num_claims_accepted_w, num_claims_accepted_m, num_claims_accepted_c,
                       num_claimrejections_accepted_w, num_claimrejections_accepted_m, num_claimrejections_accepted_c,
                       num_claims_pending_c,
                       num_claims_closed_w, num_claims_closed_m, num_claims_closed_c,
                       num_invoices_submitted_w, num_invoices_submitted_m, num_invoices_submitted_c,
                       val_invoices_submitted_w, val_invoices_submitted_m, val_invoices_submitted_c,
                       num_invoices_accepted_w, num_invoices_accepted_m, num_invoices_accepted_c,
                       val_invoices_accepted_w, val_invoices_accepted_m, val_invoices_accepted_c,
                       num_invoices_rejected_w, num_invoices_rejected_m, num_invoices_rejected_c,
                       val_invoices_rejected_w, val_invoices_rejected_m, val_invoices_rejected_c,
                       num_invoices_pending_c, val_invoices_pending_c,
                       num_invoices_awaiting_liability_w, num_invoices_awaiting_liability_m, num_invoices_awaiting_liability_c,
                       val_invoices_awaiting_liability_w, val_invoices_awaiting_liability_m, val_invoices_awaiting_liability_c,
                       num_invoices_closed_w, num_invoices_closed_m, num_invoices_closed_c,
                       val_invoices_closed_w, val_invoices_closed_m, val_invoices_closed_c,
                       num_invoices_logged_w, num_invoices_logged_m, num_invoices_logged_c,
                       val_invoices_logged_w, val_invoices_logged_m, val_invoices_logged_c,
                       num_invoices_received_w, num_invoices_received_m, num_invoices_received_c,
                       val_invoices_received_w, val_invoices_received_m, val_invoices_received_c,
                       val_penalty_charges_w, val_penalty_charges_m, val_penalty_charges_c,
                       avg_inv_payment_time_w, avg_inv_payment_time_m, avg_inv_payment_time_c,
                       num_manual_invoices_submitted_w, num_manual_invoices_submitted_m, num_manual_invoices_submitted_c,
                       val_manual_invoices_submitted_w, val_manual_invoices_submitted_m, val_manual_invoices_submitted_c,
                       num_manual_invoices_paid_w, num_manual_invoices_paid_m, num_manual_invoices_paid_c,
                       val_manual_invoices_paid_w, val_manual_invoices_paid_m, val_manual_invoices_paid_c,
                       num_manual_invoices_closed_w, num_manual_invoices_closed_m, num_manual_invoices_closed_c,
                       val_manual_invoices_closed_w, val_manual_invoices_closed_m, val_manual_invoices_closed_c,
                       num_invoices_awaiting_litigation_outcome_w, num_invoices_awaiting_litigation_outcome_m, num_invoices_awaiting_litigation_outcome_c,
                       val_invoices_awaiting_litigation_outcome_w, val_invoices_awaiting_litigation_outcome_m, val_invoices_awaiting_litigation_outcome_c,
                       val_manual_invoices_penalty_charges_paid_w, val_manual_invoices_penalty_charges_paid_m, val_manual_invoices_penalty_charges_paid_c,
                       num_insurer_claims_submitted_w, num_insurer_claims_submitted_m, num_insurer_claims_submitted_c,
                       avg_manual_inv_payment_time_w, avg_manual_inv_payment_time_m, avg_manual_inv_payment_time_c,
                       num_manual_invoices_accepted_w, num_manual_invoices_accepted_m, num_manual_invoices_accepted_c,
                       val_manual_invoices_accepted_w, val_manual_invoices_accepted_m, val_manual_invoices_accepted_c,
                       num_manual_invoices_awaiting_liability_w, num_manual_invoices_awaiting_liability_m, num_manual_invoices_awaiting_liability_c,
                       val_manual_invoices_awaiting_liability_w, val_manual_invoices_awaiting_liability_m, val_manual_invoices_awaiting_liability_c,
                       complete)
select process_date, insurer_id, chorganisation_id, workgroup_id, claim_owner_id, cho_claim_owner_id,
       num_claims_submitted_w, num_claims_submitted_m, num_claims_submitted_c,
       num_claims_accepted_w, num_claims_accepted_m, num_claims_accepted_c,
       num_claimrejections_accepted_w, num_claimrejections_accepted_m, num_claimrejections_accepted_c,
       num_claims_pending_c,
       num_claims_closed_w, num_claims_closed_m, num_claims_closed_c,
       num_invoices_submitted_w, num_invoices_submitted_m, num_invoices_submitted_c,
       coalesce(val_invoices_submitted_w, 0.00), coalesce(val_invoices_submitted_m, 0.00), coalesce(val_invoices_submitted_c, 0.00),
       num_invoices_accepted_w, num_invoices_accepted_m, num_invoices_accepted_c,
       coalesce(val_invoices_accepted_w, 0.00), coalesce(val_invoices_accepted_m, 0.00), coalesce(val_invoices_accepted_c, 0.00),
       num_invoices_rejected_w, num_invoices_rejected_m, num_invoices_rejected_c,
       coalesce(val_invoices_rejected_w, 0.00), coalesce(val_invoices_rejected_m, 0.00), coalesce(val_invoices_rejected_c, 0.00),
       num_invoices_pending_c, coalesce(val_invoices_pending_c, 0.00),
       num_invoices_awaiting_liability_w, num_invoices_awaiting_liability_m, num_invoices_awaiting_liability_c,
       coalesce(val_invoices_awaiting_liability_w, 0.00), coalesce(val_invoices_awaiting_liability_m, 0.00), coalesce(val_invoices_awaiting_liability_c, 0.00),
       num_invoices_closed_w, num_invoices_closed_m, num_invoices_closed_c,
       coalesce(val_invoices_closed_w, 0.00), coalesce(val_invoices_closed_m, 0.00), coalesce(val_invoices_closed_c, 0.00),
       num_invoices_logged_w, num_invoices_logged_m, num_invoices_logged_c,
       coalesce(val_invoices_logged_w, 0.00), coalesce(val_invoices_logged_m, 0.00), coalesce(val_invoices_logged_c, 0.00),
       num_invoices_received_w, num_invoices_received_m, num_invoices_received_c,
       coalesce(val_invoices_received_w, 0.00), coalesce(val_invoices_received_m, 0.00), coalesce(val_invoices_received_c, 0.00),
       coalesce(val_penalty_charges_w, 0.00), coalesce(val_penalty_charges_m, 0.00), coalesce(val_penalty_charges_c, 0.00),
       avg_inv_payment_time_w, avg_inv_payment_time_m, avg_inv_payment_time_c,
       num_manual_invoices_submitted_w, num_manual_invoices_submitted_m, num_manual_invoices_submitted_c,
       coalesce(val_manual_invoices_submitted_w, 0.00), coalesce(val_manual_invoices_submitted_m, 0.00), coalesce(val_manual_invoices_submitted_c, 0.00),
       num_manual_invoices_paid_w, num_manual_invoices_paid_m, num_manual_invoices_paid_c,
       coalesce(val_manual_invoices_paid_w, 0.00), coalesce(val_manual_invoices_paid_m, 0.00), coalesce(val_manual_invoices_paid_c, 0.00),
       num_manual_invoices_closed_w, num_manual_invoices_closed_m, num_manual_invoices_closed_c,
       coalesce(val_manual_invoices_closed_w, 0.00), coalesce(val_manual_invoices_closed_m, 0.00), coalesce(val_manual_invoices_closed_c, 0.00),
       num_invoices_awaiting_litigation_outcome_w, num_invoices_awaiting_litigation_outcome_m, num_invoices_awaiting_litigation_outcome_c,
       coalesce(val_invoices_awaiting_litigation_outcome_w, 0.00), coalesce(val_invoices_awaiting_litigation_outcome_m, 0.00), coalesce(val_invoices_awaiting_litigation_outcome_c, 0.00),
       coalesce(val_manual_invoices_penalty_charges_paid_w, 0.00), coalesce(val_manual_invoices_penalty_charges_paid_m, 0.00), coalesce(val_manual_invoices_penalty_charges_paid_c, 0.00),
       num_insurer_claims_submitted_w, num_insurer_claims_submitted_m, num_insurer_claims_submitted_c,
       avg_manual_inv_payment_time_w, avg_manual_inv_payment_time_m, avg_manual_inv_payment_time_c,
       num_manual_invoices_accepted_w, num_manual_invoices_accepted_m, num_manual_invoices_accepted_c,
       coalesce(val_manual_invoices_accepted_w, 0.00), coalesce(val_manual_invoices_accepted_m, 0.00), coalesce(val_manual_invoices_accepted_c, 0.00),
       num_manual_invoices_awaiting_liability_w, num_manual_invoices_awaiting_liability_m, num_manual_invoices_awaiting_liability_c,
       coalesce(val_manual_invoices_awaiting_liability_w, 0.00), coalesce(val_manual_invoices_awaiting_liability_m, 0.00), coalesce(val_manual_invoices_awaiting_liability_c, 0.00),
       true
from tmp_dashboard;

-- RAISE NOTICE 'Dropping temporary table: %1', timeofday();

truncate table tmp_dashboard;
drop table tmp_dashboard;

-- RAISE NOTICE 'Finished: %1', timeofday();

return true;

END;

$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION updateDashboard(integer) TO chox_user;

-----------------------------------------------------------------------
-- End of ToDo item 7.8.5                                            --
-----------------------------------------------------------------------

-----------------------------------------------------------------------
-- ToDo 7.8.6 - New Business Rules                                   --
-----------------------------------------------------------------------
ALTER TABLE bre_band ADD COLUMN vehicle_class_hire_provision_6_to_8_SP boolean NOT NULL default true;
ALTER TABLE bre_band ADD COLUMN vehicle_class_hire_provision_8_to_9_SP boolean NOT NULL default true;
ALTER TABLE bre_band ADD COLUMN vehicle_class_hire_provision_over_9_SP boolean NOT NULL default true;
ALTER TABLE bre_band ADD COLUMN maximum_labour_rate_standard_check boolean NOT NULL default true;
ALTER TABLE bre_band ADD COLUMN maximum_labour_rate_prestige_check boolean NOT NULL default true;
ALTER TABLE bre_band ADD COLUMN client_vat_registered_check boolean NOT NULL default true;

ALTER TABLE bre_band ADD COLUMN max_allowed_labour_standard_rate numeric(6,2) not null default 0.00;
ALTER TABLE bre_band ADD COLUMN max_allowed_labour_prestige_rate numeric(6,2) not null default 0.00;

ALTER TABLE bre_band ADD COLUMN engineer_net_fee_check boolean NOT NULL default false;
ALTER TABLE bre_band ADD COLUMN max_allowed_engineer_net_fee numeric(6,2) not null default 50.00;

-----------------------------------------------------------------------
-- End of ToDo item 7.8.6                                            --
-----------------------------------------------------------------------

-----------------------------------------------------------------------
-- bug#2754 - Production - remove liability update in status         -—
--            AwaitingInvoicePayment                                 --
-----------------------------------------------------------------------
delete from accessibility_item where accessibility_id in
(select id from accessibility where name = 'extraAction.updateLiability.AwaitingInvoicePayment');

delete from accessibility where name = 'extraAction.updateLiability.AwaitingInvoicePayment';

-- 1. from 'AwaitingInvoicePayment', the only possible next status should be: ClaimClosed, InvoicePaymentLogged
update audit_trail set reverted=true
from audit_trail at2
where audit_trail.claim_id = at2.claim_id
  and audit_trail.new_status = 'AwaitingInvoicePayment' and audit_trail.reverted=false
  and at2.original_status = audit_trail.new_status and at2.reverted=false and at2.new_status = 'AwaitingLiabilityResolution'
  and audit_trail.created_date < at2.created_date
  and not exists (select * from audit_trail at4 where at4.claim_id = audit_trail.claim_id
  and at4.created_date > audit_trail.created_date and at4.created_date < at2.created_date and at4.reverted = false);

delete from audit_trail
where original_status='AwaitingInvoicePayment' and new_status='AwaitingLiabilityResolution' and reverted = false
  and exists (select 1 from audit_trail at1 where at1.claim_id=audit_trail.claim_id and at1.reverted=true
  and at1.original_status='AwaitingLiabilityResolution'
  and at1.new_status = audit_trail.original_status
  and at1.created_date < audit_trail.created_date
  and not exists (select * from audit_trail at2 where at2.claim_id=at1.claim_id
                  and at2.reverted=false and at2.created_date>at1.created_date
                  and at2.created_date < audit_trail.created_date));

update audit_trail
  set original_status = a.original_status
from audit_trail a
where audit_trail.original_status='AwaitingInvoicePayment' and audit_trail.new_status='AwaitingLiabilityResolution' and audit_trail.reverted = false
  and a.claim_id = audit_trail.claim_id and a.reverted=true and a.original_status!='AwaitingLiabilityResolution'
  and a.new_status = audit_trail.original_status
  and a.created_date < audit_trail.created_date
  and not exists (select * from audit_trail at2 where at2.claim_id=a.claim_id
                     and at2.reverted=false and at2.created_date>a.created_date
                     and at2.created_date < audit_trail.created_date);

update audit_trail
  set reverted=true, last_modified_date='2011-01-10 12:30:00'
where id=138102;
update audit_trail set original_status='InvoiceEscalatedToHandler' where id=143440;

update audit_trail
  set reverted=true, last_modified_date=at2.created_date
from audit_trail at2
where audit_trail.original_status='ContestedInvoiceReferredToInsurer' and audit_trail.new_status='AwaitingInvoicePayment' and audit_trail.reverted=false
  and at2.claim_id=audit_trail.claim_id and at2.original_status='AwaitingInvoicePayment' and at2.new_status='ContestedInvoiceReferredToInsurer' and at2.reverted=false
  and at2.update_date > audit_trail.update_date
  and not exists (select * from audit_trail at3 where at3.claim_id=at2.claim_id
                     and at3.reverted=false and at3.created_date > audit_trail.created_date
                     and at3.created_date < at2.created_date);

delete from audit_trail
  where audit_trail.original_status='AwaitingInvoicePayment' and audit_trail.new_status='ContestedInvoiceReferredToInsurer' and audit_trail.reverted=false
    and exists (select * from audit_trail at2 where audit_trail.claim_id = at2.claim_id
    and at2.original_status='ContestedInvoiceReferredToInsurer' and at2.new_status='AwaitingInvoicePayment' and at2.reverted=true
    and at2.created_date < audit_trail.created_date
    and not exists (select * from audit_trail at3 where at3.claim_id=at2.claim_id and at3.reverted=false
                       and at3.created_date < audit_trail.created_date and at3.created_date > at2.created_date));

delete from audit_trail where id=140747;
update audit_trail set reverted=true, last_modified_date = '2011-01-05 16:56:12.827' where id=122440;
update audit_trail set reverted=true, last_modified_date = '2011-01-05 16:56:12.827' where id=88675;
update audit_trail set original_status='InvoiceReferredToClaimsHandler' where id=151844;

update audit_trail
      set reverted=true, last_modified_date=at2.created_date
from audit_trail at2
where audit_trail.original_status='InvoiceApprovedByBRE' and audit_trail.new_status='AwaitingInvoicePayment' and audit_trail.reverted=false
      and at2.claim_id=audit_trail.claim_id and at2.original_status='AwaitingInvoicePayment' and at2.new_status='InvoiceApprovedByBRE' and at2.reverted=false
      and at2.update_date > audit_trail.update_date
      and not exists (select * from audit_trail at3 where at3.claim_id=at2.claim_id and at3.reverted=false
                         and at3.created_date > audit_trail.created_date and at3.created_date < at2.created_date);

delete from audit_trail
where audit_trail.original_status='AwaitingInvoicePayment' and audit_trail.new_status='InvoiceApprovedByBRE' and audit_trail.reverted=false
  and exists (select * from audit_trail at2 where audit_trail.claim_id = at2.claim_id
  and at2.original_status='InvoiceApprovedByBRE' and at2.new_status='AwaitingInvoicePayment' and at2.reverted=true
  and at2.created_date < audit_trail.created_date
  and not exists (select * from audit_trail at3 where at3.claim_id=at2.claim_id and at3.reverted=false
                     and at3.created_date < audit_trail.created_date and at3.created_date > at2.created_date));

update audit_trail
        set reverted=true, last_modified_date=at2.created_date
  from audit_trail at2
  where audit_trail.original_status='InvoiceEscalatedToHandler' and audit_trail.new_status='AwaitingInvoicePayment' and audit_trail.reverted=false
        and at2.claim_id=audit_trail.claim_id and at2.original_status='AwaitingInvoicePayment' and at2.new_status='InvoiceEscalatedToHandler' and at2.reverted=false
        and at2.update_date > audit_trail.update_date
        and not exists (select * from audit_trail at3 where at3.claim_id=at2.claim_id and at3.reverted=false
                           and at3.created_date > audit_trail.created_date and at3.created_date < at2.created_date);

delete from audit_trail
  where audit_trail.original_status='AwaitingInvoicePayment' and audit_trail.new_status='InvoiceEscalatedToHandler' and audit_trail.reverted=false
    and exists (select * from audit_trail at2 where audit_trail.claim_id = at2.claim_id
    and at2.original_status='InvoiceEscalatedToHandler' and at2.new_status='AwaitingInvoicePayment' and at2.reverted=true
    and at2.created_date < audit_trail.created_date
    and not exists (select * from audit_trail at3 where at3.claim_id=at2.claim_id and at3.reverted=false
                       and at3.created_date < audit_trail.created_date and at3.created_date > at2.created_date));

update audit_trail
         set reverted=true, last_modified_date=at2.created_date
   from audit_trail at2
   where audit_trail.original_status='InvoiceReferredToClaimsHandler' and audit_trail.new_status='AwaitingInvoicePayment' and audit_trail.reverted=false
         and at2.claim_id=audit_trail.claim_id and at2.original_status='AwaitingInvoicePayment' and at2.new_status='InvoiceReferredToClaimsHandler' and at2.reverted=false
         and at2.update_date > audit_trail.update_date
         and not exists (select * from audit_trail at3 where at3.claim_id=at2.claim_id and at3.reverted=false
                            and at3.created_date > audit_trail.created_date and at3.created_date < at2.created_date);

delete from audit_trail
   where audit_trail.original_status='AwaitingInvoicePayment' and audit_trail.new_status='InvoiceReferredToClaimsHandler' and audit_trail.reverted=false
     and exists (select * from audit_trail at2 where audit_trail.claim_id = at2.claim_id
     and at2.original_status='InvoiceReferredToClaimsHandler' and at2.new_status='AwaitingInvoicePayment' and at2.reverted=true
     and at2.created_date < audit_trail.created_date
     and not exists (select * from audit_trail at3 where at3.claim_id=at2.claim_id and at3.reverted=false
                        and at3.created_date < audit_trail.created_date and at3.created_date > at2.created_date));

-- 2. claims should only ever be at 'AwaitingInvoicePayment' once (if possible)
update audit_trail set reverted=true, last_modified_date=a.last_modified_date - interval '100 milliseconds'
from audit_trail a
where audit_trail.claim_id in (24573,43642,56857,36942,55037,22563,55683,52574,73909,13572,62004) and a.claim_id=audit_trail.claim_id and a.reverted = false
  and audit_trail.new_status='AwaitingInvoicePayment' and audit_trail.reverted=false
  and not exists (select 1 from audit_trail a2 where a2.claim_id=audit_trail.claim_id and a2.reverted=false
                     and a2.new_status='AwaitingInvoicePayment' and a2.update_date < audit_trail.update_date)
  and not exists (select 1 from audit_trail a2 where a2.claim_id=audit_trail.claim_id and a2.reverted=false
                     and a2.update_date > audit_trail.update_date and a2.update_date < a.update_date);

update audit_trail set reverted=true where id in (1683383, 1681271, 1682734);
update audit_trail set last_modified_date='2013-12-19 15:30:10' where id=1683383;
update audit_trail set last_modified_date='2013-12-19 15:30:10' where id=1681271;
update audit_trail set last_modified_date='2013-12-19 15:30:10' where id=1682734;
delete from audit_trail where id in (1685858, 1686072, 1686075);


-- 3. claims should only ever be at 'AwaitingLiabilityResolution' once (if possible)
update audit_trail set reverted=true where id in (253213, 195319, 150989, 277465, 291898);
update audit_trail set last_modified_date='2011-06-15 10:44:19.059' where id=253213;
update audit_trail set last_modified_date='2011-03-17 17:11:59.688' where id=195319;
update audit_trail set last_modified_date='2011-04-05 12:13:14.029' where id=150989;
update audit_trail set last_modified_date='2011-06-06 17:08:03.75' where id=277465;
update audit_trail set last_modified_date='2011-06-17 16:44:48.759' where id=291898;
delete from audit_trail where id in (292923, 201987, 220577, 288455, 301868);
update audit_trail set reverted=true, last_modified_date='2011-01-04 10:38:13' where id in (124675, 133493, 133817, 106268, 114391);
update audit_trail set original_status='InvoiceEscalatedToHandler' where id in (138944, 138924, 138931, 138940);
update audit_trail set original_status='ContestedInvoiceReferredToInsurer' where id in (138937,141467);
update audit_trail set reverted=true, last_modified_date='2011-01-06 15:49:00' where id=139914;


-----------------------------------------------------------------------
-- End of bug#2754                                                   --
-----------------------------------------------------------------------
