drop function protocol_management_running_totals_report(text, int[]);

set client_encoding to 'latin1';

create or replace function protocol_management_running_totals_report (
    beginDate text,
    choid int[]
)
returns table
(
   row_title text,
   supergroup_1 varchar(12),
   supergroup_2 varchar(12),
   supergroup_3 varchar(12),
   other varchar(12)
)
as $$
DECLARE
    end_date date;
    insId integer;
    supergroup1 integer[];
    supergroup2 integer[];
    supergroup3 integer[];
    allSupergroups integer[];
BEGIN
    end_date = beginDate::Date;
    insId = '3'::integer;
-- PMC: Damage Halifax, Damage Birmingham, More Than Not Allocated, Pro-active Halifax, UKP Not Allocated
--  14 | Damage - Halifax
--   1 | Damage - Birmingham
--   5 | More Than - Not Allocated
-- 106 | ProActive - Halifax
--  48 | UKP - Not Allocated
    supergroup1 := '{14, 1, 5, 106, 48}'::integer[];
-- Commercial: Bespoke Glasgow, Bespoke Manchester, Claims Promise Manchester, Damage Glasgow, Damage Manchester, Foreign Manchester, Motor Trade Manchester, Risk Solution Chelmsford, Risk Solutions Global, UKC Not Allocated
--   8 | Bespoke - Glasgow
--  11 | Bespoke - Manchester
--  59 | Commercial Credit Hire – Manchester
--  13 | Damage - Glasgow
--  15 | Damage - Manchester
--  49 | Foreign - Manchester
--  23 | Motor Trade - Manchester
--  27 | Risk Solution - Large - Chelmsford
--  84 | Risk Solutions Global
--  46 | UKC - Not Allocated
    supergroup2 := '{8, 11, 59, 13, 15, 49, 23, 27, 84, 46}'::integer[];
-- Care: RSA Care Birmingham Team 2, RSA Care Halifax Team 2, RSA Care Horsham Team 2, RSA Care Chelmsford Team 2,
--       RSA Care Manchester Team 2
--  30 | RSA Care - Birmingham - Team 2
--  32 | RSA Care - Chelmsford - Team 2
--  36 | RSA Care - Halifax - Team 2
--  38 | RSA Care - Horsham - Team 2
--  40 | RSA Care - Manchester - Team 2
    supergroup3 := '{30, 32, 36, 38, 40}'::integer[];
    allSupergroups := '{14, 1, 5, 106, 48, 8, 11, 59, 13, 15, 49, 23, 27, 84, 46, 30, 32, 36, 38, 40}'::integer[];
RETURN QUERY

select '01. Uploaded claims' as row_title ,
-- all claims uploaded by the relevant CHO across the various time periods.

(select count(*)::varchar from claim c, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy')
        and end_date) as supergroup_1,

(select count(*)::varchar from claim c, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id 
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy')
        and end_date) as supergroup_2,

(select count(*)::varchar from claim c, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id 
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy')
        and end_date) as supergroup_3,

(select count(*)::varchar from claim c, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id 
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date) as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '02. Accepted claims %' as row_title ,
--  worked out from claims moved to Awaiting Car Hire Info divided by uploaded claims less those claims
--  that were rejected for reason 'Not Our Policyholder'.
(select (100.0*(select count(*) from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and a.claim_id=c.id and a.new_status='AwaitingCarHireInfo' and a.reverted=false
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)
     /  NULLIF((select count(*) from claim c, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and not exists (select * from audit_trail a2, reason_of_rejection ror
                        where a2.claim_id=c.id and a2.new_status='ClaimRejectionAccepted' and a2.reverted=false
                          and c.reason_of_rejection_id = ror.id and ror.name ilike '%Not Our Policyholder%')
        and not exists (select * from audit_trail a2, comment cm, reason_of_rejection ror
                        where cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
                          and a2.claim_id=c.id and a2.new_status='ClaimRejected' and a2.reverted=false
                          and c.reason_of_rejection_id = ror.id and ror.name ilike '%Not Our Policyholder%')
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date), 0))::numeric(5,2)::varchar)  as supergroup_1,

(select (100.0*(select count(*) from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and a.claim_id=c.id and a.new_status='AwaitingCarHireInfo' and a.reverted=false
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)
     /  NULLIF((select count(*) from claim c, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and not exists (select * from audit_trail a2, reason_of_rejection ror
                        where a2.claim_id=c.id and a2.new_status='ClaimRejectionAccepted' and a2.reverted=false
                          and c.reason_of_rejection_id = ror.id and ror.name ilike '%Not Our Policyholder%')
        and not exists (select * from audit_trail a2, comment cm, reason_of_rejection ror
                        where cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
                          and a2.claim_id=c.id and a2.new_status='ClaimRejected' and a2.reverted=false
                          and c.reason_of_rejection_id = ror.id and ror.name ilike '%Not Our Policyholder%')
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date), 0))::numeric(5,2)::varchar)   as supergroup_2,

(select (100.0*(select count(*) from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and a.claim_id=c.id and a.new_status='AwaitingCarHireInfo' and a.reverted=false
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)
     /  NULLIF((select count(*) from claim c, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and not exists (select * from audit_trail a2, reason_of_rejection ror
                        where a2.claim_id=c.id and a2.new_status='ClaimRejectionAccepted' and a2.reverted=false
                          and c.reason_of_rejection_id = ror.id and ror.name ilike '%Not Our Policyholder%')
        and not exists (select * from audit_trail a2, comment cm, reason_of_rejection ror
                        where cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
                          and a2.claim_id=c.id and a2.new_status='ClaimRejected' and a2.reverted=false
                          and c.reason_of_rejection_id = ror.id and ror.name ilike '%Not Our Policyholder%')
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date), 0))::numeric(5,2)::varchar)  as supergroup_3,

(select (100.0*(select count(*) from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and a.claim_id=c.id and a.new_status='AwaitingCarHireInfo' and a.reverted=false
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)
     /  NULLIF((select count(*) from claim c, chorganisation cho
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and (c.chorganisation_id = ANY(params.chorgId))
        and not exists (select * from audit_trail a2, reason_of_rejection ror
                        where a2.claim_id=c.id and a2.new_status='ClaimRejectionAccepted' and a2.reverted=false
                          and c.reason_of_rejection_id = ror.id and ror.name ilike '%Not Our Policyholder%')
        and not exists (select * from audit_trail a2, comment cm, reason_of_rejection ror
                        where cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
                          and a2.claim_id=c.id and a2.new_status='ClaimRejected' and a2.reverted=false
                          and c.reason_of_rejection_id = ror.id and ror.name ilike '%Not Our Policyholder%')
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date), 0))::numeric(5,2)::varchar)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '03. Average Time To Accept ' as row_title ,
-- time from claim upload to Awaiting Car Hire Info status.

(select avg((a.created_date::DATE - c.created_date::Date)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id = c.id and a.new_status='AwaitingCarHireInfo' and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::Date)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id = c.id and a.new_status='AwaitingCarHireInfo' and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::Date)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id = c.id and a.new_status='AwaitingCarHireInfo' and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::Date)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id = c.id and a.new_status='AwaitingCarHireInfo' and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '04. Rejected Claims To GTA' as row_title ,
-- volume rejected and subsequently converted to a GTA claim.

(select count(*)::varchar
   from claim c, chorganisation cho, comment cm, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, comment cm, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, comment cm, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, comment cm, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '05. Average Time To Reject GTA claims' as row_title ,
-- time from claim upload to Claim Rejected/Subscriber Claim Rejected when claim subsequently switches
-- to a GTA claim (1st instance of rejection if more than 1 rejection)
(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, comment cm, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status in ('ClaimRejected','SubscriberClaimRejected') and a2.reverted=false and a2.created_date < a.created_date)
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, comment cm, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status in ('ClaimRejected','SubscriberClaimRejected') and a2.reverted=false and a2.created_date < a.created_date)
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, comment cm, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status in ('ClaimRejected','SubscriberClaimRejected') and a2.reverted=false and a2.created_date < a.created_date)
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, comment cm, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and cm.claim_id = c.id and cm.comment ilike 'Claim switched % to GTA.'
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status in ('ClaimRejected','SubscriberClaimRejected') and a2.reverted=false and a2.created_date < a.created_date)
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '06. Rejected Claims To Claim Rejection Accepted' as row_title ,
--volume rejected and moved to status Claim Rejection Accepted.

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status = 'ClaimRejectionAccepted' and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status = 'ClaimRejectionAccepted' and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status = 'ClaimRejectionAccepted' and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status = 'ClaimRejectionAccepted' and a.reverted=false
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '07. Average Time To Reject Claim Closed' as row_title ,
-- time from claim upload to Claim Rejected/Subscriber Claim Rejected when claim subsequently
-- moves to Claim Rejection Accepted.

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and exists(select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted=false and a2.new_status='ClaimRejectionAccepted')
        and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status in ('ClaimRejected','SubscriberClaimRejected') and a2.reverted=false and a2.created_date < a.created_date)
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and exists(select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted=false and a2.new_status='ClaimRejectionAccepted')
        and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status in ('ClaimRejected','SubscriberClaimRejected') and a2.reverted=false and a2.created_date < a.created_date)
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and exists(select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted=false and a2.new_status='ClaimRejectionAccepted')
        and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status in ('ClaimRejected','SubscriberClaimRejected') and a2.reverted=false and a2.created_date < a.created_date)
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected','SubscriberClaimRejected') and a.reverted=false
        and exists(select * from audit_trail a2 where a2.claim_id=c.id and a2.reverted=false and a2.new_status='ClaimRejectionAccepted')
        and not exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status in ('ClaimRejected','SubscriberClaimRejected') and a2.reverted=false and a2.created_date < a.created_date)
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '08. Volume Rejected - Reason: Indemnity and Liability Issues' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity and Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity and Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity and Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity and Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '09. Time to Reject - Reason: Indemnity and Liability Issues' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity and Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity and Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity and Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity and Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '10. Volume Rejected - Reason: Indemnity Issues' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '11. Time to Reject - Reason: Indemnity Issues' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '12. Volume Rejected - Reason: Insurer vs Insurer' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '13. Time to Reject - Reason: Insurer vs Insurer' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '14. Volume Rejected - Reason: Intervention' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Intervention'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Intervention'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Intervention'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Intervention'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '15. Time to Reject - Reason: Intervention' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Intervention'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Intervention'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Intervention'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Intervention'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '16. Volume Rejected - Reason: Liability Issues' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '17. Time to Reject - Reason: Liability Issues' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '18. Volume Rejected - Reason: Not our Policyholder' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '19. Time to Reject - Reason: Not our Policyholder' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '20. Volume Rejected - Reason: Other' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Other'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Other'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Other'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Other'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '21. Time to Reject - Reason: Other' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Other'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Other'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Other'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Other'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '22. Volume Rejected - Reason: Out of Scope' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '23. Time to Reject - Reason: Out of Scope' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '24. Volume Rejected - Reason: Out Of Scope - Channel Islands' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '25. Time to Reject - Reason: Out Of Scope - Channel Islands' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '26. Volume Rejected - Reason: Out Of Scope - Foreign' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '27. Time to Reject - Reason: Out Of Scope - Foreign' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '28. Volume Rejected - Reason: Out Of Scope - Isle Of Man' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '29. Time to Reject - Reason: Out Of Scope - Isle Of Man' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '30. Volume Rejected - Reason: Out Of Scope - MIB' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '31. Time to Reject - Reason: Out Of Scope - MIB' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '32. Volume Rejected - Reason: Out Of Scope - Schemes' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '33. Time to Reject - Reason: Out Of Scope - Schemes' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '34. Volume Rejected - Reason: Out Of Scope - Self Insured' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '35. Time to Reject - Reason: Out Of Scope - Self Insured' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '36. Volume Rejected - Reason: Out Of Scope - Tower' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope – Tower'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope – Tower'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope – Tower'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope – Tower'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '37. Time to Reject - Reason: Out Of Scope - Tower' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope – Tower'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope – Tower'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope – Tower'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Out of Scope – Tower'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '38. Volume Rejected - Reason: Quantum' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Quantum'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Quantum'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Quantum'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Quantum'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '39. Time to Reject - Reason: Quantum' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Quantum'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Quantum'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Quantum'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Quantum'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '40. Volume Rejected - Reason: Subscriber Bank Holiday Weekend' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber Bank Holiday Weekend'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber Bank Holiday Weekend'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber Bank Holiday Weekend'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber Bank Holiday Weekend'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '41. Time to Reject - Reason: Subscriber Bank Holiday Weekend' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber Bank Holiday Weekend'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber Bank Holiday Weekend'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber Bank Holiday Weekend'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber Bank Holiday Weekend'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '42. Volume Rejected - Reason: Subscriber - Fraud Issues' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '43. Time to Reject - Reason: Subscriber - Fraud Issues' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '44. Volume Rejected - Reason: Subscriber - Indemnity Issues' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '45. Time to Reject - Reason: Subscriber - Indemnity Issues' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '46. Volume Rejected - Reason: Subscriber - Liability Issues' as row_title ,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select count(*)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

UNION

select '47. Time to Reject - Reason: Subscriber - Liability Issues' as row_title ,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup1)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_1,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup2)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as supergroup_2,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id = ANY(supergroup3)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)  as supergroup_3,

(select avg((a.created_date::DATE - c.created_date::DATE)+1)::numeric(5,2)::varchar
   from claim c, chorganisation cho, audit_trail a, reason_of_rejection ror
    where (c.insurer_id = params.insurerId or params.insurerId = -1)
        and c.chorganisation_id = cho.id
        and a.claim_id=c.id and a.new_status in ('ClaimRejected', 'SubscriberClaimRejected') and a.reverted=false and a.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
        and (   (exists (select * from audit_trail a2 where a2.claim_id=c.id and a2.new_status = 'ClaimRejectionAccepted' and a2.reverted=false and a2.created_date > a.created_date ))
             or (exists (select * from comment where comment.claim_id = c.id and comment like 'Claim switched % to GTA.')))
        and (c.chorganisation_id = ANY(params.chorgId))
        and c.workgroup_id != ANY(allSupergroups)
        and c.created_date between to_date('01-01-' || to_char(params.endDate - interval '1 day', 'yyyy'), 'mm-dd-yyyy') and end_date)   as other

from (select end_date as endDate, choid as chorgId, insId as insurerId) params

order by row_title;

END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON FUNCTION protocol_management_running_totals_report(text, int[]) TO chox_user;
GRANT EXECUTE ON FUNCTION protocol_management_running_totals_report(text, int[]) TO chox_mi;
