drop function protocol_comparison_report(varchar, varchar, integer, integer[]);
create or replace function protocol_comparison_report (
    IN startPeriod VARCHAR,
    IN endPeriod VARCHAR,
    IN insid INTEGER,
    IN choIds INTEGER[])
returns table
(
   index integer,
   header varchar,
   "Subscriber" varchar,
   "Fixed Fee" varchar,
   "Collaboration Protocol" varchar,
   "GTA" varchar,
   "Outside Protocol" varchar
)
AS

$BODY$

DECLARE

DATE_FROM DATE;
DATE_TO DATE;

BEGIN

DATE_FROM = $1::DATE;
DATE_TO = $2::DATE;

RETURN QUERY

select 0 as index, 'Caseload'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 1 as index, 'New Claims'::varchar as header,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 2 as index, 'Closed Claims'::varchar as header,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9) and invoice_id is null
       and c.status in ('ClaimClosed','ClaimRejectionAccepted')) as subscriber,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13) and invoice_id is null
       and c.status in ('ClaimClosed','ClaimRejectionAccepted')) as fixed_fee,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20) and invoice_id is null
       and c.status in ('ClaimClosed','ClaimRejectionAccepted')) as collaboration_protocol,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2) and invoice_id is null
       and c.status in ('ClaimClosed','ClaimRejectionAccepted')) as GTA,
    ''::varchar as outside_protocol

UNION

select 3 as index, 'Open Claims'::varchar as header,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9) and invoice_id is null
       and c.status not in ('ClaimClosed','ClaimRejectionAccepted')) as subscriber,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13) and invoice_id is null
       and c.status not in ('ClaimClosed','ClaimRejectionAccepted')) as fixed_fee,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20) and invoice_id is null
       and c.status not in ('ClaimClosed','ClaimRejectionAccepted')) as collaboration_protocol,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2) and invoice_id is null
       and c.status not in ('ClaimClosed','ClaimRejectionAccepted')) as GTA,
    ''::varchar as outside_protocol

UNION

select 4 as index, 'New Invoices'::varchar as header,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9) and invoice_id is not null) as subscriber,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13) and invoice_id is not null) as fixed_fee,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20) and invoice_id is not null) as collaboration_protocol,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2) and invoice_id is not null) as GTA,
    ''::varchar as outside_protocol

UNION

select 5 as index, 'Invoices Withdrawn By CHO'::varchar as header,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and invoice_id is not null and status in ('ClaimClosed','InvoiceRejectionAccepted')) as subscriber,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and invoice_id is not null and status in ('ClaimClosed','InvoiceRejectionAccepted')) as fixed_fee,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and invoice_id is not null and status in ('ClaimClosed','InvoiceRejectionAccepted')) as collaboration_protocol,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and invoice_id is not null and status in ('ClaimClosed','InvoiceRejectionAccepted')) as GTA,
    ''::varchar as outside_protocol

UNION

select 6 as index, 'Invoices Paid'::varchar as header,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and invoice_id is not null and status in ('InvoicePaymentLogged','PaymentReceived')) as subscriber,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and invoice_id is not null and status in ('InvoicePaymentLogged','PaymentReceived')) as fixed_fee,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and invoice_id is not null and status in ('InvoicePaymentLogged','PaymentReceived')) as collaboration_protocol,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and invoice_id is not null and status in ('InvoicePaymentLogged','PaymentReceived')) as GTA,
    ''::varchar as outside_protocol

UNION

select 7 as index, 'Open Invoices'::varchar as header,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and invoice_id is not null and status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')) as subscriber,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and invoice_id is not null and status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')) as fixed_fee,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and invoice_id is not null and status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')) as collaboration_protocol,
    (select count(*)::varchar from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and invoice_id is not null and status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')) as GTA,
    ''::varchar as outside_protocol

UNION

select 8 as index, 'Invoices Open < 30 days'::varchar as header,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 30) as subscriber,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 30) as fixed_fee,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 30) as collaboration_protocol,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 30) as GTA,
    ''::varchar as outside_protocol

UNION

select 9 as index, 'Invoices Open 30-60 days'::varchar as header,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 30
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 60) as subscriber,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 30
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 60) as fixed_fee,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 30
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 60) as collaboration_protocol,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 30
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 60) as GTA,
    ''::varchar as outside_protocol

UNION

select 10 as index, 'Invoices Open 60-90 days'::varchar as header,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 60
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 90) as subscriber,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 60
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 90) as fixed_fee,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 60
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 90) as collaboration_protocol,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 60
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) < 90) as GTA,
    ''::varchar as outside_protocol

UNION

select 11 as index, 'Invoices Open 90+ days'::varchar as header,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 90) as subscriber,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 90) as fixed_fee,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 90) as collaboration_protocol,
    (select count(*)::varchar from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2) and c.invoice_id=i.id
       and c.status not in ('Claimclosed','InvoicePaymentLogged','InvoiceRejectionAccepted','PaymentReceived')
       and (i.auto_penalty_start::Date - c.created_date::Date + 1) >= 90) as GTA,
    ''::varchar as outside_protocol

UNION

select 12 as index, 'Invoices Paid < 30 days'::varchar as header,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) < 30) as subscriber,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) < 30) as fixed_fee,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) < 30) as collaboration_protocol,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) < 30) as GTA,
    ''::varchar as outside_protocol

UNION

select 13 as index, 'Invoices Paid 30-60 days'::varchar as header,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 30
       and (a.created_date::Date - c.created_date::Date + 1) < 60) as subscriber,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 30
       and (a.created_date::Date - c.created_date::Date + 1) < 60) as fixed_fee,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 30
       and (a.created_date::Date - c.created_date::Date + 1) < 60) as collaboration_protocol,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 30
       and (a.created_date::Date - c.created_date::Date + 1) < 60) as GTA,
    ''::varchar as outside_protocol

UNION

select 14 as index, 'Invoices Paid 60-90 days'::varchar as header,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 60
       and (a.created_date::Date - c.created_date::Date + 1) < 90) as subscriber,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 60
       and (a.created_date::Date - c.created_date::Date + 1) < 90) as fixed_fee,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 60
       and (a.created_date::Date - c.created_date::Date + 1) < 90) as collaboration_protocol,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 60
       and (a.created_date::Date - c.created_date::Date + 1) < 90) as GTA,
    ''::varchar as outside_protocol

UNION

select 15 as index, 'Invoices Paid 90+ days'::varchar as header,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 90) as subscriber,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 90) as fixed_fee,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 90) as collaboration_protocol,
    (select count(*)::varchar from claim c, audit_trail a
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.status in ('InvoicePaymentLogged','PaymentReceived')
       and a.claim_id = c.id and a.new_status='InvoicePaymentLogged' and a.reverted=false
       and (a.created_date::Date - c.created_date::Date + 1) >= 90) as GTA,
    ''::varchar as outside_protocol

UNION

select 16 as index, 'Liability < 100%'::varchar as header,
    (select (100.0*SUM(CASE WHEN percentage_liability_accepted < 100.0 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)) as subscriber,
    (select (100.0*SUM(CASE WHEN percentage_liability_accepted < 100.0 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select (100.0*SUM(CASE WHEN percentage_liability_accepted < 100.0 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN percentage_liability_accepted < 100.0 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 17 as index, 'Credit Repair Ratio'::varchar as header,
    (select (100.0*SUM(CASE WHEN repair_net > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id) as subscriber,
    (select (100.0*SUM(CASE WHEN repair_net > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id) as fixed_fee,
    (select (100.0*SUM(CASE WHEN repair_net > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN repair_net > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 18 as index, 'Total Loss Ratio'::varchar as header,
    (select (100.0*SUM(CASE WHEN is_total_loss  then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, customer cu
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.customer_id = cu.id) as subscriber,
    (select (100.0*SUM(CASE WHEN is_total_loss then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, customer cu
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.customer_id = cu.id) as fixed_fee,
    (select (100.0*SUM(CASE WHEN is_total_loss then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, customer cu
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.customer_id = cu.id) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN is_total_loss then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, customer cu
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.customer_id = cu.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 19 as index, 'Acquisition Fee Total Paid'::varchar as header,
    (select sum(i.miscellaneous_fee)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.invoice_id = i.id) as subscriber,
    ''::varchar as fixed_fee,
    ''::varchar as collaboration_protocol,
    ''::varchar as GTA,
    ''::varchar as outside_protocol

UNION

select 20 as index, 'Acquisition Fee Average Paid'::varchar as header,
    (select avg(i.miscellaneous_fee)::numeric(6,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.invoice_id = i.id) as subscriber,
    ''::varchar as fixed_fee,
    ''::varchar as collaboration_protocol,
    ''::varchar as GTA,
    ''::varchar as outside_protocol

UNION

select 21 as index, 'Admin Fee Total Paid (excluding repair)'::varchar as header,
    (select sum(i.admin_fee)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and i.repair_net = 0.0) as subscriber,
    (select sum(i.admin_fee)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and i.repair_net = 0.0) as fixed_fee,
    (select sum(i.admin_fee)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and i.repair_net = 0.0) as collaboration_protocol,
    (select sum(i.admin_fee)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and i.repair_net = 0.0) as GTA,
    ''::varchar as outside_protocol

UNION

select 22 as index, 'Admin Fee Average Paid (excluding repair)'::varchar as header,
    (select avg(i.admin_fee)::numeric(6,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and i.repair_net = 0.0) as subscriber,
    (select avg(i.admin_fee)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and i.repair_net = 0.0) as fixed_fee,
    (select avg(i.admin_fee)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and i.repair_net = 0.0) as collaboration_protocol,
    (select avg(i.admin_fee)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and i.repair_net = 0.0) as GTA,
    ''::varchar as outside_protocol

UNION

select 23 as index, 'Car Class % of Invoiced'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 24 as index, 'S Class'::varchar as header,
    (select (100.0*SUM(CASE WHEN vc.name like 'S%' and name not like 'SP%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as subscriber,
    (select (100.0*SUM(CASE WHEN vc.name like 'S%' and name not like 'SP%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as subscriber,
    (select (100.0*SUM(CASE WHEN vc.name like 'S%' and name not like 'SP%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN vc.name like 'S%' and name not like 'SP%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.claim_type in (0,1,2)
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 25 as index, 'Prestige'::varchar as header,
    (select (100.0*SUM(CASE WHEN vc.name like 'P%' and name not like 'PV%' and name not like 'PT%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as subscriber,
    (select (100.0*SUM(CASE WHEN vc.name like 'P%' and name not like 'PV%' and name not like 'PT%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as fixed_fee,
    (select (100.0*SUM(CASE WHEN vc.name like 'P%' and name not like 'PV%' and name not like 'PT%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN vc.name like 'P%' and name not like 'PV%' and name not like 'PT%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 26 as index, 'MPV'::varchar as header,
    (select (100.0*SUM(CASE WHEN vc.name like 'M%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as subscriber,
    (select (100.0*SUM(CASE WHEN vc.name like 'M%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as fixed_fee,
    (select (100.0*SUM(CASE WHEN vc.name like 'M%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN vc.name like 'M%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 27 as index, '4x4'::varchar as header,
    (select (100.0*SUM(CASE WHEN vc.name like 'F%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as subscriber,
    (select (100.0*SUM(CASE WHEN vc.name like 'F%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as fixed_fee,
    (select (100.0*SUM(CASE WHEN vc.name like 'F%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN vc.name like 'F%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 28 as index, 'Sports'::varchar as header,
    (select (100.0*SUM(CASE WHEN vc.name like 'SP%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as subscriber,
    (select (100.0*SUM(CASE WHEN vc.name like 'SP%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as fixed_fee,
    (select (100.0*SUM(CASE WHEN vc.name like 'SP%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN vc.name like 'SP%' then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 29 as index, 'Other'::varchar as header,
    (select (100.0*SUM(CASE WHEN vc.name like 'PV%' or vc.name like 'CV%' or vc.name like 'RV%'
                                or vc.name like 'CP%' or vc.name like 'CS%' or vc.name like 'CM%'
                                or vc.name like 'T%' or vc.name like 'PT%' or vc.name like 'B%'
                                or vc.name like 'NT%' -- or vc.name='UNATTACHED'
                     THEN 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as subscriber,
    (select (100.0*SUM(CASE WHEN vc.name like 'PV%' or vc.name like 'CV%' or vc.name like 'RV%'
                                or vc.name like 'CP%' or vc.name like 'CS%' or vc.name like 'CM%'
                                or vc.name like 'T%' or vc.name like 'PT%' or vc.name like 'B%'
                                or vc.name like 'NT%' -- or vc.name='UNATTACHED'
                     THEN 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as fixed_fee,
    (select (100.0*SUM(CASE WHEN vc.name like 'PV%' or vc.name like 'CV%' or vc.name like 'RV%'
                                or vc.name like 'CP%' or vc.name like 'CS%' or vc.name like 'CM%'
                                or vc.name like 'T%' or vc.name like 'PT%' or vc.name like 'B%'
                                or vc.name like 'NT%' -- or vc.name='UNATTACHED'
                     THEN 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN vc.name like 'PV%' or vc.name like 'CV%' or vc.name like 'RV%'
                                or vc.name like 'CP%' or vc.name like 'CS%' or vc.name like 'CM%'
                                or vc.name like 'T %' or vc.name like 'PT%' or vc.name like 'B%'
                                or vc.name like 'NT%' -- or vc.name='UNATTACHED'
                     THEN 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, vehicle_hire vh, vehicle_class vc, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id and i.hire_net > 37.0
       and c.vehicle_hire_id = vh.id
       and vh.vehicle_class_id = vc.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 30 as index, 'Period'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 31 as index, 'Average Hire Days Invoiced'::varchar as header,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 32 as index, 'Average Hire Days Paid'::varchar as header,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 33 as index, 'Average Non Total Loss Hire Days Invoiced'::varchar as header,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = false
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = false
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = false
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = false
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 34 as index, 'Average Non Total Loss Hire Days Paid'::varchar as header,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = false
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = false
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = false
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = false
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 35 as index, 'Average Total Loss Hire Days Invoiced'::varchar as header,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = true
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = true
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = true
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when vh.days_original is not null then vh.days_original else vh.days end)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = true
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 36 as index, 'Average Total Loss Hire Days Paid'::varchar as header,
    (select avg(vh.days)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = true
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(vh.days)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = true
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(vh.days)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = true
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(vh.days)::numeric(8,2)::varchar
     from claim c, invoice i, vehicle_hire vh, customer cu
     where c.invoice_id=i.id and c.vehicle_hire_id = vh.id 
       and c.created_date between DATE_FROM and DATE_TO
       and vh.days > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.customer_id = cu.id and cu.is_total_loss = true
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 37 as index, 'Hire Rate'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 38 as index, 'Average Hire Rate Invoiced'::varchar as header,
    (select avg(io.hire_rate_charged_per_day)::numeric(10,2)::varchar 
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and io.hire_rate_charged_per_day > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id and i.invoice_original_id = io.id) as subscriber,
    (select avg(io.hire_rate_charged_per_day)::numeric(10,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and io.hire_rate_charged_per_day > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id and i.invoice_original_id = io.id) as fixed_fee,
    (select avg(io.hire_rate_charged_per_day)::numeric(10,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and io.hire_rate_charged_per_day > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id and i.invoice_original_id = io.id) as collaboration_protocol,
    (select avg(io.hire_rate_charged_per_day)::numeric(10,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and io.hire_rate_charged_per_day > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id and i.invoice_original_id = io.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 39 as index, 'Average Hire Rate Paid'::varchar as header,
    (select avg(i.hire_rate_charged_per_day)::numeric(10,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and i.hire_rate_charged_per_day > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (7,8,9)
       and c.invoice_id = i.id) as subscriber,
    (select avg(i.hire_rate_charged_per_day)::numeric(10,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and i.hire_rate_charged_per_day > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (11,12,13)
       and c.invoice_id = i.id) as fixed_fee,
    (select avg(i.hire_rate_charged_per_day)::numeric(10,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and i.hire_rate_charged_per_day > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (18,19,20)
       and c.invoice_id = i.id) as collaboration_protocol,
    (select avg(i.hire_rate_charged_per_day)::numeric(10,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and i.hire_rate_charged_per_day > 0
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (0,1,2)
       and c.invoice_id = i.id) as GTA,
    ''::varchar as outside_protocol

UNION

select 40 as index, 'Hire'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 41 as index, 'Total No. Invoices Uploaded'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_gross > 0.0
       and i.hire_net > 37.0
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_gross > 0.0
       and i.hire_net > 37.0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_gross > 0.0
       and i.hire_net > 37.0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_gross > 0.0
       and i.hire_net > 37.0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 42 as index, 'Total No. Invoices Paid'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_gross > 0.0
       and i.hire_net > 37.0
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_gross > 0.0
       and i.hire_net > 37.0
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_gross > 0.0
       and i.hire_net > 37.0
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_gross > 0.0
       and i.hire_net > 37.0
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 43 as index, 'Total Hire Paid'::varchar as header,
    (select sum(case when hire_gross_paid is null then hire_gross else hire_gross_paid end)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and i.hire_net > 37.0
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select sum(case when hire_gross_paid is null then hire_gross else hire_gross_paid end)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and i.hire_net > 37.0
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select sum(case when hire_gross_paid is null then hire_gross else hire_gross_paid end)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and i.hire_net > 37.0
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select sum(case when hire_gross_paid is null then hire_gross else hire_gross_paid end)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and i.hire_net > 37.0
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 44 as index, 'Average Hire Value Invoiced'::varchar as header,
    (select avg(io.hire_gross)::numeric(8,2)::varchar 
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and io.hire_gross > 0.0 and io.hire_net > 37.0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(io.hire_gross)::numeric(8,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and io.hire_gross > 0.0 and io.hire_net > 37.0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(io.hire_gross)::numeric(8,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and io.hire_gross > 0.0 and io.hire_net > 37.0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(io.hire_gross)::numeric(8,2)::varchar
     from claim c, invoice i, invoice_original io 
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and io.hire_gross > 0.0 and io.hire_net > 37.0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 45 as index, 'Average Hire Value Paid (exc. Pens)'::varchar as header,
    (select avg(case when hire_gross_paid is null then hire_gross else hire_gross_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged') and i.hire_gross > 0.0 and i.hire_net > 37.0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when hire_gross_paid is null then hire_gross else hire_gross_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged') and i.hire_gross > 0.0 and i.hire_net > 37.0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when hire_gross_paid is null then hire_gross else hire_gross_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged') and i.hire_gross > 0.0 and i.hire_net > 37.0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when hire_gross_paid is null then hire_gross else hire_gross_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged') and i.hire_gross > 0.0 and i.hire_net > 37.0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 46 as index, 'Average Hire Value Paid'::varchar as header,
    (select avg(case when hire_gross_paid is null then hire_gross else hire_gross_paid end + case when hire_penalty_charge_paid is null then hire_penalty_charge else hire_penalty_charge_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged') and i.hire_gross > 0.0 and i.hire_net > 37.0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when hire_gross_paid is null then hire_gross else hire_gross_paid end + case when hire_penalty_charge_paid is null then hire_penalty_charge else hire_penalty_charge_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged') and i.hire_gross > 0.0 and i.hire_net > 37.0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when hire_gross_paid is null then hire_gross else hire_gross_paid end + case when hire_penalty_charge_paid is null then hire_penalty_charge else hire_penalty_charge_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged') and i.hire_gross > 0.0 and i.hire_net > 37.0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when hire_gross_paid is null then hire_gross else hire_gross_paid end + case when hire_penalty_charge_paid is null then hire_penalty_charge else hire_penalty_charge_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived', 'InvoicePaymentLogged') and i.hire_gross > 0.0 and i.hire_net > 37.0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 47 as index, 'Total No. Invoices Uploaded with an Extra'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and (io.automatic_fee > 0 or io.estate_fee > 0 or io.additional_driver_fee > 0
            or io.sat_nav_fee > 0 or io.baby_seat_fee > 0 or io.tow_bars_fee > 0
            or io.non_standard_insurance_premium_fee > 0 or io.roof_rack_fee > 0
            or io.dual_control_fee > 0 or io.delivery_collection_fee > 0)
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and (io.automatic_fee > 0 or io.estate_fee > 0 or io.additional_driver_fee > 0
            or io.sat_nav_fee > 0 or io.baby_seat_fee > 0 or io.tow_bars_fee > 0
            or io.non_standard_insurance_premium_fee > 0 or io.roof_rack_fee > 0
            or io.dual_control_fee > 0 or io.delivery_collection_fee > 0)
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and (io.automatic_fee > 0 or io.estate_fee > 0 or io.additional_driver_fee > 0
            or io.sat_nav_fee > 0 or io.baby_seat_fee > 0 or io.tow_bars_fee > 0
            or io.non_standard_insurance_premium_fee > 0 or io.roof_rack_fee > 0
            or io.dual_control_fee > 0 or io.delivery_collection_fee > 0)
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and (io.automatic_fee > 0 or io.estate_fee > 0 or io.additional_driver_fee > 0
            or io.sat_nav_fee > 0 or io.baby_seat_fee > 0 or io.tow_bars_fee > 0
            or io.non_standard_insurance_premium_fee > 0 or io.roof_rack_fee > 0
            or io.dual_control_fee > 0 or io.delivery_collection_fee > 0)
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 48 as index, 'Total No. Invoices Paid with an Extra'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and (i.automatic_fee > 0 or i.estate_fee > 0 or i.additional_driver_fee > 0
            or i.sat_nav_fee > 0 or i.baby_seat_fee > 0 or i.tow_bars_fee > 0
            or i.non_standard_insurance_premium_fee > 0 or i.roof_rack_fee > 0
            or i.dual_control_fee > 0 or i.delivery_collection_fee > 0)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and (i.automatic_fee > 0 or i.estate_fee > 0 or i.additional_driver_fee > 0
            or i.sat_nav_fee > 0 or i.baby_seat_fee > 0 or i.tow_bars_fee > 0
            or i.non_standard_insurance_premium_fee > 0 or i.roof_rack_fee > 0
            or i.dual_control_fee > 0 or i.delivery_collection_fee > 0)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and (i.automatic_fee > 0 or i.estate_fee > 0 or i.additional_driver_fee > 0
            or i.sat_nav_fee > 0 or i.baby_seat_fee > 0 or i.tow_bars_fee > 0
            or i.non_standard_insurance_premium_fee > 0 or i.roof_rack_fee > 0
            or i.dual_control_fee > 0 or i.delivery_collection_fee > 0)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and (i.automatic_fee > 0 or i.estate_fee > 0 or i.additional_driver_fee > 0
            or i.sat_nav_fee > 0 or i.baby_seat_fee > 0 or i.tow_bars_fee > 0
            or i.non_standard_insurance_premium_fee > 0 or i.roof_rack_fee > 0
            or i.dual_control_fee > 0 or i.delivery_collection_fee > 0)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 49 as index, 'Average Extra Value Uploaded'::varchar as header,
    (select avg(io.automatic_fee + io.estate_fee + io.additional_driver_fee
                    + io.sat_nav_fee + io.baby_seat_fee + io.tow_bars_fee
                    + io.non_standard_insurance_premium_fee + io.roof_rack_fee
                    + io.dual_control_fee + io.delivery_collection_fee)::numeric(6,2)::varchar 
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and (io.automatic_fee > 0 or io.estate_fee > 0 or io.additional_driver_fee > 0
            or io.sat_nav_fee > 0 or io.baby_seat_fee > 0 or io.tow_bars_fee > 0
            or io.non_standard_insurance_premium_fee > 0 or io.roof_rack_fee > 0
            or io.dual_control_fee > 0 or io.delivery_collection_fee > 0)
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(io.automatic_fee + io.estate_fee + io.additional_driver_fee
                    + io.sat_nav_fee + io.baby_seat_fee + io.tow_bars_fee
                    + io.non_standard_insurance_premium_fee + io.roof_rack_fee
                    + io.dual_control_fee + io.delivery_collection_fee)::numeric(6,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and (io.automatic_fee > 0 or io.estate_fee > 0 or io.additional_driver_fee > 0
            or io.sat_nav_fee > 0 or io.baby_seat_fee > 0 or io.tow_bars_fee > 0
            or io.non_standard_insurance_premium_fee > 0 or io.roof_rack_fee > 0
            or io.dual_control_fee > 0 or io.delivery_collection_fee > 0)
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(io.automatic_fee + io.estate_fee + io.additional_driver_fee
                    + io.sat_nav_fee + io.baby_seat_fee + io.tow_bars_fee
                    + io.non_standard_insurance_premium_fee + io.roof_rack_fee
                    + io.dual_control_fee + io.delivery_collection_fee)::numeric(6,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and (io.automatic_fee > 0 or io.estate_fee > 0 or io.additional_driver_fee > 0
            or io.sat_nav_fee > 0 or io.baby_seat_fee > 0 or io.tow_bars_fee > 0
            or io.non_standard_insurance_premium_fee > 0 or io.roof_rack_fee > 0
            or io.dual_control_fee > 0 or io.delivery_collection_fee > 0)
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(io.automatic_fee + io.estate_fee + io.additional_driver_fee
                    + io.sat_nav_fee + io.baby_seat_fee + io.tow_bars_fee
                    + io.non_standard_insurance_premium_fee + io.roof_rack_fee
                    + io.dual_control_fee + io.delivery_collection_fee)::numeric(6,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and (io.automatic_fee > 0 or io.estate_fee > 0 or io.additional_driver_fee > 0
            or io.sat_nav_fee > 0 or io.baby_seat_fee > 0 or io.tow_bars_fee > 0
            or io.non_standard_insurance_premium_fee > 0 or io.roof_rack_fee > 0
            or io.dual_control_fee > 0 or io.delivery_collection_fee > 0)
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 50 as index, 'Average Extra Value Paid'::varchar as header,
    (select avg(i.automatic_fee + i.estate_fee + i.additional_driver_fee
                    + i.sat_nav_fee + i.baby_seat_fee + i.tow_bars_fee
                    + i.non_standard_insurance_premium_fee + i.roof_rack_fee
                    + i.dual_control_fee + i.delivery_collection_fee)::numeric(6,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and (i.automatic_fee > 0 or i.estate_fee > 0 or i.additional_driver_fee > 0
            or i.sat_nav_fee > 0 or i.baby_seat_fee > 0 or i.tow_bars_fee > 0
            or i.non_standard_insurance_premium_fee > 0 or i.roof_rack_fee > 0
            or i.dual_control_fee > 0 or i.delivery_collection_fee > 0)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(i.automatic_fee + i.estate_fee + i.additional_driver_fee
                    + i.sat_nav_fee + i.baby_seat_fee + i.tow_bars_fee
                    + i.non_standard_insurance_premium_fee + i.roof_rack_fee
                    + i.dual_control_fee + i.delivery_collection_fee)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and (i.automatic_fee > 0 or i.estate_fee > 0 or i.additional_driver_fee > 0
            or i.sat_nav_fee > 0 or i.baby_seat_fee > 0 or i.tow_bars_fee > 0
            or i.non_standard_insurance_premium_fee > 0 or i.roof_rack_fee > 0
            or i.dual_control_fee > 0 or i.delivery_collection_fee > 0)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(i.automatic_fee + i.estate_fee + i.additional_driver_fee
                    + i.sat_nav_fee + i.baby_seat_fee + i.tow_bars_fee
                    + i.non_standard_insurance_premium_fee + i.roof_rack_fee
                    + i.dual_control_fee + i.delivery_collection_fee)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and (i.automatic_fee > 0 or i.estate_fee > 0 or i.additional_driver_fee > 0
            or i.sat_nav_fee > 0 or i.baby_seat_fee > 0 or i.tow_bars_fee > 0
            or i.non_standard_insurance_premium_fee > 0 or i.roof_rack_fee > 0
            or i.dual_control_fee > 0 or i.delivery_collection_fee > 0)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(i.automatic_fee + i.estate_fee + i.additional_driver_fee
                    + i.sat_nav_fee + i.baby_seat_fee + i.tow_bars_fee
                    + i.non_standard_insurance_premium_fee + i.roof_rack_fee
                    + i.dual_control_fee + i.delivery_collection_fee)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and (i.automatic_fee > 0 or i.estate_fee > 0 or i.additional_driver_fee > 0
            or i.sat_nav_fee > 0 or i.baby_seat_fee > 0 or i.tow_bars_fee > 0
            or i.non_standard_insurance_premium_fee > 0 or i.roof_rack_fee > 0
            or i.dual_control_fee > 0 or i.delivery_collection_fee > 0)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 51 as index, 'Repair'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 52 as index, 'Total No. Invoices Uploaded'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_gross > 0.0
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_gross > 0.0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_gross > 0.0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_gross > 0.0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 53 as index, 'Total No. Invoices Paid'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 54 as index, 'Total Repair Paid'::varchar as header,
    (select sum(case when repair_gross_paid is null then repair_gross else repair_gross_paid end)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select sum(case when repair_gross_paid is null then repair_gross else repair_gross_paid end)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select sum(case when repair_gross_paid is null then repair_gross else repair_gross_paid end)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select sum(case when repair_gross_paid is null then repair_gross else repair_gross_paid end)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 55 as index, 'Average Repair Value Invoiced'::varchar as header,
    (select avg(io.repair_gross)::numeric(8,2)::varchar 
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and io.repair_gross > 0.0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(io.repair_gross)::numeric(8,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and io.repair_gross > 0.0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(io.repair_gross)::numeric(8,2)::varchar
     from claim c, invoice i, invoice_original io
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and io.repair_gross > 0.0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(io.repair_gross)::numeric(8,2)::varchar
     from claim c, invoice i, invoice_original io 
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.invoice_original_id = io.id
       and io.repair_gross > 0.0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 56 as index, 'Average Repair Value Paid (exc. Pens)'::varchar as header,
    (select avg(case when repair_gross_paid is null then repair_gross else repair_gross_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged') and i.repair_gross > 0.0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when repair_gross_paid is null then repair_gross else repair_gross_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged') and i.repair_gross > 0.0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when repair_gross_paid is null then repair_gross else repair_gross_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged') and i.repair_gross > 0.0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when repair_gross_paid is null then repair_gross else repair_gross_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged') and i.repair_gross > 0.0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 57 as index, 'Average Repair Value Paid'::varchar as header,
    (select avg(case when repair_gross_paid is null then repair_gross else repair_gross_paid end + case when repair_penalty_charge_paid is null then repair_penalty_charge else repair_penalty_charge_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged') and i.repair_gross > 0.0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when repair_gross_paid is null then repair_gross else repair_gross_paid end + case when repair_penalty_charge_paid is null then repair_penalty_charge else repair_penalty_charge_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged') and i.repair_gross > 0.0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when repair_gross_paid is null then repair_gross else repair_gross_paid end + case when repair_penalty_charge_paid is null then repair_penalty_charge else repair_penalty_charge_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged') and i.repair_gross > 0.0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when repair_gross_paid is null then repair_gross else repair_gross_paid end + case when repair_penalty_charge_paid is null then repair_penalty_charge else repair_penalty_charge_paid end)::numeric(8,2)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged') and i.repair_gross > 0.0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 58 as index, 'Other costs'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 59 as index, 'Total No. Invoices Where Engineers Fee Paid'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.engineer_fee_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.engineer_fee_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.engineer_fee_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.engineer_fee_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 60 as index, '% Invoices With Engineers Fee Paid'::varchar as header,
    (select (100.0*SUM(CASE WHEN i.engineer_fee_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select (100.0*SUM(CASE WHEN i.engineer_fee_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select (100.0*SUM(CASE WHEN i.engineer_fee_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN i.engineer_fee_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 61 as index, 'Average Engineers Fee Paid'::varchar as header,
    (select avg(case when i.engineer_fee_gross_paid is null then i.engineer_fee_gross else i.engineer_fee_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.engineer_fee_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when i.engineer_fee_gross_paid is null then i.engineer_fee_gross else i.engineer_fee_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.engineer_fee_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when i.engineer_fee_gross_paid is null then i.engineer_fee_gross else i.engineer_fee_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.engineer_fee_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when i.engineer_fee_gross_paid is null then i.engineer_fee_gross else i.engineer_fee_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.engineer_fee_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 62 as index, 'Total No. Invoices Where Storage & Recovery Fee Paid'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.storage_recovery_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.storage_recovery_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.storage_recovery_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.storage_recovery_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 63 as index, '% Invoices With Storage & Recovery Fee Paid'::varchar as header,
    (select (100.0*SUM(CASE WHEN i.storage_recovery_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select (100.0*SUM(CASE WHEN i.storage_recovery_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select (100.0*SUM(CASE WHEN i.storage_recovery_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN i.storage_recovery_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 64 as index, 'Average Storage & Recovery Fee Paid'::varchar as header,
    (select avg(case when i.storage_recovery_gross_paid is null then i.storage_recovery_gross else i.storage_recovery_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.storage_recovery_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when i.storage_recovery_gross_paid is null then i.storage_recovery_gross else i.storage_recovery_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.storage_recovery_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when i.storage_recovery_gross_paid is null then i.storage_recovery_gross else i.storage_recovery_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.storage_recovery_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when i.storage_recovery_gross_paid is null then i.storage_recovery_gross else i.storage_recovery_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.storage_recovery_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 65 as index, 'Total No. Invoices Where Total Loss Fee Paid'::varchar as header,
    (select count(*)::varchar 
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_loss_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_loss_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_loss_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_loss_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 66 as index, '% Invoices With A Total Loss Fee Paid'::varchar as header,
    (select (100.0*SUM(CASE WHEN i.total_loss_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select (100.0*SUM(CASE WHEN i.total_loss_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select (100.0*SUM(CASE WHEN i.total_loss_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select (100.0*SUM(CASE WHEN i.total_loss_gross > 0.00 then 1 else 0 END) / count(*))::numeric(5,1)::varchar || '%'
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 67 as index, 'Average Total Loss Fee Paid'::varchar as header,
    (select avg(case when i.total_loss_fee_gross_paid is null then i.total_loss_gross else i.total_loss_fee_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_loss_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when i.total_loss_fee_gross_paid is null then i.total_loss_gross else i.total_loss_fee_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_loss_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when i.total_loss_fee_gross_paid is null then i.total_loss_gross else i.total_loss_fee_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_loss_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when i.total_loss_fee_gross_paid is null then i.total_loss_gross else i.total_loss_fee_gross_paid end)::numeric(6,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_loss_gross > 0.0
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 68 as index, 'Penalty Leakage'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 69 as index, 'Total Value Of Penalties Paid'::varchar as header,
    (select sum(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end
                    + case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and c.claim_type in (7,8,9)) as subscriber,
    (select sum(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end
                    + case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select sum(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end
                    + case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select sum(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end
                    + case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id 
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 70 as index, 'Average Penalty Payment Charged'::varchar as header,
    (select avg(i.total_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_penalty_charge > 0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(i.total_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_penalty_charge > 0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(i.total_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_penalty_charge > 0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(i.total_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.total_penalty_charge > 0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 71 as index, 'Average Penalty Payment Paid'::varchar as header,
    (select avg(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end
                    + case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id and i.hire_penalty_charge_paid + i.repair_penalty_charge_paid > 0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end
                    + case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id and i.hire_penalty_charge_paid + i.repair_penalty_charge_paid > 0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end
                    + case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id and i.hire_penalty_charge_paid + i.repair_penalty_charge_paid > 0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end
                    + case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id and i.hire_penalty_charge_paid + i.repair_penalty_charge_paid > 0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 72 as index, 'Hire Penalty Leakage'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 73 as index, 'Total No. Hire Claims With Penalty Payments Charged'::varchar as header,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_penalty_charge > 0
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_penalty_charge > 0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_penalty_charge > 0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_penalty_charge > 0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 74 as index, 'Total No. Hire Claims With Penalty Payments Paid'::varchar as header,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.hire_penalty_charge > 0)
            or (penalty_charges_paid = true and hire_penalty_charge_paid > 0)) 
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.hire_penalty_charge > 0)
            or (penalty_charges_paid = true and hire_penalty_charge_paid > 0)) 
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.hire_penalty_charge > 0)
            or (penalty_charges_paid = true and hire_penalty_charge_paid > 0)) 
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.hire_penalty_charge > 0)
            or (penalty_charges_paid = true and hire_penalty_charge_paid > 0)) 
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 75 as index, 'Average Hire Penalty Payment Charged'::varchar as header,
    (select avg(i.hire_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_penalty_charge > 0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(i.hire_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_penalty_charge > 0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(i.hire_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_penalty_charge > 0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(i.hire_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.hire_penalty_charge > 0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 76 as index, 'Average Hire Penalty Payment Paid'::varchar as header,
    (select avg(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.hire_penalty_charge > 0)
            or (penalty_charges_paid = true and hire_penalty_charge_paid > 0)) 
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.hire_penalty_charge > 0)
            or (penalty_charges_paid = true and hire_penalty_charge_paid > 0)) 
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.hire_penalty_charge > 0)
            or (penalty_charges_paid = true and hire_penalty_charge_paid > 0)) 
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when i.hire_penalty_charge_paid is null then i.hire_penalty_charge else i.hire_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.hire_penalty_charge > 0)
            or (penalty_charges_paid = true and hire_penalty_charge_paid > 0)) 
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 77 as index, 'Repair Penalty Leakage'::varchar as header, ''::varchar,''::varchar,''::varchar,''::varchar,''::varchar

UNION

select 78 as index, 'Total No. Repair Claims With Penalty Payments Charged'::varchar as header,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_penalty_charge > 0
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_penalty_charge > 0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_penalty_charge > 0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_penalty_charge > 0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 79 as index, 'Total No. Repair Claims With Penalty Payments Paid'::varchar as header,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.repair_penalty_charge > 0)
            or (penalty_charges_paid = true and repair_penalty_charge_paid > 0)) 
       and c.claim_type in (7,8,9)) as subscriber,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.repair_penalty_charge > 0)
            or (penalty_charges_paid = true and repair_penalty_charge_paid > 0)) 
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.repair_penalty_charge > 0)
            or (penalty_charges_paid = true and repair_penalty_charge_paid > 0)) 
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select count(*)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.repair_penalty_charge > 0)
            or (penalty_charges_paid = true and repair_penalty_charge_paid > 0)) 
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 80 as index, 'Average Repair Penalty Payment Charged'::varchar as header,
    (select avg(i.repair_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_penalty_charge > 0
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(i.repair_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_penalty_charge > 0
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(i.repair_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_penalty_charge > 0
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(i.repair_penalty_charge)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.invoice_id = i.id and i.repair_penalty_charge > 0
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

UNION

select 81 as index, 'Average Repair Penalty Payment Paid'::varchar as header,
    (select avg(case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.repair_penalty_charge > 0)
            or (penalty_charges_paid = true and repair_penalty_charge_paid > 0)) 
       and c.claim_type in (7,8,9)) as subscriber,
    (select avg(case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.repair_penalty_charge > 0)
            or (penalty_charges_paid = true and repair_penalty_charge_paid > 0)) 
       and c.claim_type in (11,12,13)) as fixed_fee,
    (select avg(case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.repair_penalty_charge > 0)
            or (penalty_charges_paid = true and repair_penalty_charge_paid > 0)) 
       and c.claim_type in (18,19,20)) as collaboration_protocol,
    (select avg(case when i.repair_penalty_charge_paid is null then i.repair_penalty_charge else i.repair_penalty_charge_paid end)::numeric(8,2)::varchar
     from claim c, invoice i
     where c.created_date between DATE_FROM and DATE_TO
       and c.insurer_id = insid and c.chorganisation_id = ANY(choIds)
       and c.status in ('PaymentReceived','InvoicePaymentLogged')
       and c.invoice_id = i.id
       and ((i.final_payment is null and i.repair_penalty_charge > 0)
            or (penalty_charges_paid = true and repair_penalty_charge_paid > 0)) 
       and c.claim_type in (0,1,2)) as GTA,
    ''::varchar as outside_protocol

order by index;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

/* select * from protocol_comparison_report('2009-01-01', '2013-06-01', 3, array[1132, 1013, 1014, 1006, 1009, 1010, 1028, 1029, 1007, 1030, 1032]); */
/* select * from protocol_comparison_report('2013-01-01', '2013-04-01', 3, array[1010, 1007, 1016]); */
