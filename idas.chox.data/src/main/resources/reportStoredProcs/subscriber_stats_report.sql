-- drop function subscriber_stats_report(choId int, claim_uploaded_date_from VARCHAR, claim_upload_date_to VARCHAR);

create or replace function subscriber_stats_report(
    choId int,
    claim_uploaded_date_from VARCHAR,
    claim_upload_date_to VARCHAR)
returns table
(
    "Insurer Name" character varying,
    "Total Uploaded" bigint,
    "Total Accepted" bigint,
    "Rejected and Became GTA" bigint,
    "Rejected To GTA - Reason: Insurer vs Insurer" bigint,
    "Rejected To GTA - Reason: Intervention" bigint,
    "Rejected To GTA - Reason: Not our Policyholder" bigint,
    "Rejected To GTA - Reason: Other" bigint,
    "Rejected To GTA - Reason: Out Of Scope - Channel Islands" bigint,
    "Rejected To GTA - Reason: Out Of Scope - Foreign" bigint,
    "Rejected To GTA - Reason: Out Of Scope - Isle Of Man" bigint,
    "Rejected To GTA - Reason: Out Of Scope - MIB" bigint,
    "Rejected To GTA - Reason: Out Of Scope - Schemes" bigint,
    "Rejected To GTA - Reason: Out Of Scope - Self Insured" bigint,
    "Rejected To GTA - Reason: Out Of Scope - Tower" bigint,
    "Rejected To GTA - Reason: Subsciber Bank Holiday Weekend" bigint,
    "Rejected To GTA - Reason: Subscriber - Fraud Issues" bigint,
    "Rejected To GTA - Reason: Subscriber - Indemnity Issues" bigint,
    "Rejected To GTA - Reason: Subscriber - Liability Issues" bigint,
    "Total Closed After Rejection" bigint,
    "Total Closed - Reason: Insurer vs Insurer" bigint,
    "Total Closed - Reason: Intervention" bigint,
    "Total Closed - Reason: Not our Policyholder" bigint,
    "Total Closed - Reason: Other" bigint,
    "Total Closed - Reason: Out Of Scope - Channel Islands" bigint,
    "Total Closed - Reason: Out Of Scope - Foreign" bigint,
    "Total Closed - Reason: Out Of Scope - Isle Of Man" bigint,
    "Total Closed - Reason: Out Of Scope - MIB" bigint,
    "Total Closed - Reason: Out Of Scope - Schemes" bigint,
    "Total Closed - Reason: Out Of Scope - Self Insured" bigint,
    "Total Closed - Reason: Out Of Scope - Tower" bigint,
    "Total Closed - Reason: Subsciber Bank Holiday Weekend" bigint,
    "Total Closed - Reason: Subscriber - Fraud Issues" bigint,
    "Total Closed - Reason: Subscriber - Indemnity Issues" bigint,
    "Total Closed - Reason: Subscriber - Liability Issues" bigint,
    "Total Closed Before Agreed or Rejected" bigint,
    "Total Uploaded But Not Yet Accepted" bigint,
    "Total Accepted But Closed Later" bigint
)
as $$
DECLARE
    DATE_FROM DATE;
    DATE_TO DATE;
    insRecord RECORD;
BEGIN
 
    DATE_FROM = $2::DATE;
    DATE_TO = $3::DATE;
 
    FOR insRecord IN
        select * from insurer ins where allow_subscriber_claims = true and ins.id in
                    (select insurer_id from insurer_chorganisation
                      where chorganisation_id = $1 or $1=-1) order by ins.name asc
    LOOP
    RETURN QUERY
 
     select (select name from insurer where id = insRecord.id) as "Insurer Name",
            (select count(*)
             from claim c
                inner join insurer ins on ins.id = c.insurer_id
                inner join chorganisation cho on cho.id = c.chorganisation_id
             where (cho.id = $1 or $1 = -1)
               and ins.id = insRecord.id
               and (c.claim_type in (7,8,9) or (c.id in (select a.claim_id from audit_trail a
                                                         where a.claim_id = c.id and a.reverted = false
                                                           and a.new_status = 'SubscriberClaimRejected')))
               and c.created_date between DATE_FROM and DATE_TO) as "Total Uploaded",
            (select count(*)
             from claim c
                inner join insurer ins on ins.id = c.insurer_id
                inner join chorganisation cho on cho.id = c.chorganisation_id
             where (cho.id = $1 or $1 = -1)
               and ins.id = insRecord.id
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO     
               and c.id in (select a.claim_id from audit_trail a
                            where a.claim_id = c.id and a.reverted = false
                              and a.new_status = 'AwaitingCarHireInfo')) as "Total Accepted",
            (select count(*)
             from claim c, audit_trail a
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.created_date between DATE_FROM and DATE_TO    
               and c.claim_type not in (7,8,9)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Total Rejected and Became GTA",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Insurer vs Insurer",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Intervention'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Intervention",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Not our Policyholder",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Other'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Other",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Out Of Scope - Channel Islands",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Out Of Scope - Foreign",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Out Of Scope - Isle Of Man",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Out Of Scope - MIB",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Out Of Scope - Schemes",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Out Of Scope - Self Insured",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Tower'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Out Of Scope - Tower",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Subsciber Bank Holiday Weekend'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Subsciber Bank Holiday Weekend",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Subscriber - Fraud Issues",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Subscriber - Indemnity Issues",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and a.original_status = 'SubscriberClaimRejected'
               and c.created_date between DATE_FROM and DATE_TO
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and c.claim_type not in (7,8,9)
               and a2.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
               and a2.claim_id = c.id and a2.reverted=false
               and a2.new_status = 'SubscriberClaimRejected'
               and a2.created_date < a.created_date
               and not exists (select * from audit_trail a3 where a3.claim_id=c.id and a3.reverted=false
                                  and a3.new_status = 'SubscriberClaimRejected'
                                  and a3.created_date > a2.created_date
                                  and a3.created_date < a.created_date)
               and not exists (select * from audit_trail a2
                               where a2.claim_id = c.id and a2.reverted=false
                                 and a2.original_status = 'SubscriberClaimRejected'
                                 and a2.created_date > a.created_date)) as "Rejected To GTA - Reason: Subscriber - Liability Issues",
            (select count(*)
             from claim c, audit_trail a
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and exists (select * from audit_trail a2
                           where a2.claim_id = c.id and a2.reverted=false
                             and a2.new_status = 'SubscriberClaimRejected')) as "Total Closed After Rejection",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Insurer vs Insurer'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Insurer vs Insurer",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Intervention'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Intervention",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Not our Policyholder'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Not our Policyholder",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Other'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Other",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Channel Islands'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Out Of Scope - Channel Islands",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Foreign'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Out Of Scope - Foreign",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Isle Of Man'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Out Of Scope - Isle Of Man",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - MIB'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Out Of Scope - MIB",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Schemes'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Out Of Scope - Schemes",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Self Insured'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Out Of Scope - Self Insured",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Out Of Scope - Tower'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Out Of Scope - Tower",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Bank Holiday Weekend'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Subsciber Bank Holiday Weekend",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Fraud Issues'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Subscriber - Fraud Issues",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Indemnity Issues'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Subscriber - Indemnity Issues",
            (select count(*)
             from claim c, audit_trail a, audit_trail a2, reason_of_rejection ror
             where a.claim_id = c.id and a.reverted = false
               and (c.chorganisation_id = $1 or $1 = -1)
               and c.insurer_id = insRecord.id
               and a.new_status = 'ClaimClosed'
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and a2.claim_id = c.id and a2.reverted = false
               and a2.claim_reason_of_rejection = ror.id and ror.name='Subscriber - Liability Issues'
               and a2.new_status = 'SubscriberClaimRejected' 
               and not exists (select * from audit_trail a3
                               where a3.claim_id=a2.claim_id and a3.reverted = false
                                 and a3.new_status = 'SubscriberClaimRejected'
                                 and a3.created_date > a2.created_date)) as "Total Closed - Reason: Subscriber - Liability Issues",
            (select count(*)
             from claim c
                inner join insurer ins on ins.id = c.insurer_id
                inner join chorganisation cho on cho.id = c.chorganisation_id
                inner join audit_trail a1 on (a1.claim_id = c.id and a1.reverted = false and a1.new_status = 'ClaimClosed')
             where (cho.id = $1 or $1 = -1)
               and ins.id = insRecord.id
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and c.id not in (select a.claim_id from audit_trail a
                                where a.claim_id = c.id and a.reverted = false
                                  and a.new_status = 'AwaitingCarHireInfo')
               and c.id not in (select a.claim_id from audit_trail a
                                where a.claim_id = c.id and a.reverted = false
                                  and a.new_status = 'SubscriberClaimRejected')) as "Total Closed Before Agreed or Rejected",
            (select count(*)
             from claim c
                inner join insurer ins on ins.id = c.insurer_id
                inner join chorganisation cho on cho.id = c.chorganisation_id
             where (cho.id = $1 or $1 = -1)
               and ins.id = insRecord.id
               and c.claim_type in (7,8,9)
               and c.created_date between DATE_FROM and DATE_TO
               and c.id not in (select a.claim_id from audit_trail a
                                where a.claim_id = c.id and a.reverted = false
                                  and a.new_status = 'ClaimClosed')
               and c.id not in (select a.claim_id from audit_trail a
                                where a.claim_id = c.id and a.reverted = false
                                  and a.new_status = 'AwaitingCarHireInfo')) as "Total Uploaded But Not Yet Accepted",
            (select count(*)
             from claim c
                inner join insurer ins on ins.id = c.insurer_id
                inner join chorganisation cho on cho.id = c.chorganisation_id
                inner join audit_trail a1 on (a1.claim_id = c.id and a1.reverted = false and a1.new_status = 'ClaimClosed')
             where (cho.id = $1 or $1 = -1)
               and ins.id = insRecord.id
               and c.created_date between DATE_FROM and DATE_TO
               and c.id in (select a.claim_id from audit_trail a
                            where a.claim_id = c.id and a.reverted = false
                              and a.new_status = 'AwaitingCarHireInfo')
               and c.id in (select a.claim_id from audit_trail a
                            where a.claim_id = c.id and a.reverted = false
                              and a.new_status = 'SubscriberClaimRejected')) as "Total Accepted But Closed Later";
 
END LOOP;
 
END;
$$ LANGUAGE plpgsql;
