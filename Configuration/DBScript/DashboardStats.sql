drop table if exists dashboard_kbbs;
create table dashboard_kbbs (
          id serial not null,
          claim_id integer NOT NULL REFERENCES claim(id),
          insurer_id int not null,
          cho_id int not null,
          claim_type_id int not null,
          original_claim_type_id int not null,
          workgroup_id int,
          handler_id int,
          cho_handler_id int,
          status character varying not null,
          is_supplementary boolean not null,
          claim_review_days int,
          invoice_review_days int,
          claim_upload_date date not null,
          invoice_upload_date date,
          protocol_status character varying,
          liability_status character varying not null,
          hire_claimed numeric(10,2),
          hire_paid numeric(10,2),
          is_hire boolean,
          hire_days_claimed int,
          hire_days_paid int,
          paid_date date,
          repair_claimed numeric(10,2),
          repair_paid numeric(10,2),
          is_repair boolean,
          car_park character varying(2),
          daily_rate numeric(8,2),
          full_total_requested numeric(10,2),
          bre_status character varying,
          acknowledge_days int,
          acknowledge_date date,
          liability_days int,
          liability_date date,
          lifecycle_days int,
          payment_days int,
          CONSTRAINT dashboard_kbbs_pkey PRIMARY KEY (id),
          CONSTRAINT dashboard_kbbs_ukey UNIQUE (claim_id)
);



CREATE OR REPLACE FUNCTION getDashboardClaimType(claimTypeId integer)
  RETURNS integer AS
$BODY$
 DECLARE
   resultString integer;
 BEGIN
    IF $1 IN (0,1,2,3,4,5,6) THEN
         resultString = 0;
    ELSIF $1 IN (7,8,9) THEN
         resultString = 7;
    ELSIF $1 IN (10,14,15,16,17) THEN
         resultString = 17;
    ELSIF $1 IN (11,12,13) THEN
         resultString = 11;
    ELSIF $1 IN (18,19,20) THEN
         resultString = 18;
    ELSE
         resultString = '';
    END IF;
        RETURN resultString;
 END;
 $BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;



CREATE OR REPLACE FUNCTION generateDashboardStats()
    RETURNS boolean AS
$BODY$

  BEGIN
    truncate table dashboard_kbbs;

insert into dashboard_kbbs(claim_id, insurer_id, cho_id, claim_type_id, original_claim_type_id, workgroup_id, handler_id, cho_handler_id, status,
                            is_supplementary, claim_upload_date, invoice_upload_date, liability_status,
                            hire_claimed, hire_paid, is_hire, repair_claimed, repair_paid, is_repair, full_total_requested)
    select c.id, c.insurer_id, c.chorganisation_id, getDashboardClaimType(c.claim_type), getDashboardClaimType(c.claim_type), c.workgroup_id, c.claim_owner_id, c.cho_claim_owner_id,
            c.status, case when c.claim_type in (2,6,9,13,16,20) then true else false end,
            c.created_date, i.created_date,
            case when c.liability_status in (1,5,6) then 'Agreed' else case when c.liability_status=4 then 'Repudiated' else 'Pending' end end,
            case when io.hire_gross is null or io.hire_gross = 0 then i.hire_gross else io.hire_gross end,
            case when i.final_payment is not null then i.hire_gross_paid + i.hire_penalty_charge_paid else i.hire_gross + i.hire_penalty_charge end,
            case when i.hire_net - i.admin_fee > 0 then true else false end,
            case when io.repair_gross is null or io.repair_gross = 0 then i.repair_gross else io.repair_gross end,
            case when i.final_payment is not null then i.repair_gross_paid + i.repair_penalty_charge_paid else i.repair_gross + i.repair_penalty_charge end,
            case when i.repair_gross > 0 then true else false end,
            i.full_total_to_pay
    from claim c left outer join invoice i on (c.invoice_id = i.id)
                 left outer join invoice_original io on (i.invoice_original_id = io.id);


update dashboard_kbbs d
    set claim_review_days = now()::date - d.claim_upload_date::date + 1
from claim c
where c.id = d.claim_id
  and c.status in ('ClaimUnacknowledgedUnassigned','ClaimPending','ClaimUnacknowledgedUnrouted','ClaimReferredToEngineer','ClaimReferredToFNOL','ClaimUnacknowledgedRouted');

update dashboard_kbbs d
    set invoice_review_days = now()::date - d.invoice_upload_date + 1
from claim c, invoice i
where c.invoice_id = i.id and c.id = d.claim_id
  and c.status in ('InvoiceApprovedByBRE','InvoiceEscalatedToHandler','InvoiceReferredToEngineer','InvoiceReferredToClaimsHandler','InvoiceUnassigned');

update dashboard_kbbs d
    set protocol_status = 'Accepted'
where claim_type_id in (7,11,18)
  and exists (select * from audit_trail at where at.claim_id=d.claim_id and at.new_status='AwaitingCarHireInfo' and at.reverted=false)
  and not exists (select * from comment where comment.claim_id=d.claim_id and comment.comment like '%failed to respond to the % notification within the % day SLA%' and comment.reverted = false);

update dashboard_kbbs d
    set protocol_status = 'Defaulted'
from claim c
where c.id = d.claim_id and d.claim_type_id in (7,11,18)
  and exists (select * from audit_trail at where at.claim_id=d.claim_id and at.new_status='AwaitingCarHireInfo' and at.reverted=false)
  and (exists (select * from comment where comment.claim_id=d.claim_id and comment.comment like '%failed to respond to the % notification within the % day SLA%' and comment.reverted = false)
        or (d.status in ('ClaimPending','ClaimReferredToEngineer','ClaimReferredToFNOL','ClaimRejected','ClaimRejectionContested','ClaimUnacknowledgedRouted','ClaimUnacknowledgedUnassigned','ClaimUnacknowledgedUnrouted','SubscriberClaimRejected') and c.remaining_sla_days <= 0));

update dashboard_kbbs d
    set protocol_status = 'Pending'
where protocol_status is null and d.claim_type_id in (7,11,18)
  and d.status in ('ClaimPending','ClaimReferredToEngineer','ClaimReferredToFNOL','ClaimRejected','ClaimRejectionContested','ClaimUnacknowledgedRouted','ClaimUnacknowledgedUnassigned','ClaimUnacknowledgedUnrouted','SubscriberClaimRejected')
  and not exists(select * from comment where comment.claim_id=d.id and comment.comment like '%failed to respond to the % notification within the % day SLA%' and comment.reverted = false);

update dashboard_kbbs d
    set protocol_status = 'Rejected', original_claim_type_id=7
where protocol_status is null and d.claim_type_id=0
  and exists (select * from comment co where co.claim_id = d.claim_id and co.comment like 'Claim switched from Subscriber to GTA.' and co.reverted = false);


update dashboard_kbbs d
    set protocol_status = 'Rejected', original_claim_type_id=11
where protocol_status is null and d.claim_type_id=0
  and exists(select * from comment co where co.claim_id = d.claim_id and co.comment like 'Claim switched from Fixed Fee to GTA.' and co.reverted = false);


update dashboard_kbbs d
    set protocol_status = 'Rejected'
where protocol_status is null
  and d.claim_type_id in (7,11,18)
  and d.status in ('ClaimRejectionAccepted'); -- ClaimClosed?

update dashboard_kbbs d
    set hire_days_claimed = case when vh.days_original is null or vh.days_original = 0 then vh.days else vh.days_original end,
        hire_days_paid = vh.days,
        paid_date = at.created_date
from claim c, vehicle_hire vh, audit_trail at
where d.claim_id = c.id and c.vehicle_hire_id = vh.id
  and at.claim_id = d.claim_id and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid');

update dashboard_kbbs d
    set car_park = 'B'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'B%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'S'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'S%' and vc.name not like 'SP%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'M'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'M%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'F'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'F%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'P'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'P%' and vc.name not like 'PV%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'T'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'T%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'SP'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'SP%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'PV'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'PV%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'RV'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'RV%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'CV'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'CV%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'CP'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'CP%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'CS'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'CS%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'CM'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'CM%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set car_park = 'NT'
from claim c, vehicle_hire vh, vehicle_class vc
where d.claim_id = c.id and c.vehicle_hire_id = vh.id and vh.vehicle_class_id = vc.id and vc.name like 'NT%' and vh.days != 0 and car_park is null
  and d.status not in ('ClaimClosed','InvoiceRejectionAccepted');

update dashboard_kbbs d
    set daily_rate = (i.hire_net - i.miscellaneous_fee - i.collaboration_fee - i.automatic_fee -i.additional_driver_fee -i.sat_nav_fee -i.estate_fee -i.baby_seat_fee -i.tow_bars_fee
                   - i.non_standard_insurance_premium_fee -i.admin_fee -i.roof_rack_fee -i.dual_control_fee -i.delivery_collection_fee) / vh.days
from claim c, invoice i, vehicle_hire vh
where  d.claim_id = c.id and c.vehicle_hire_id = vh.id and c.invoice_id = i.id
    and d.status not in ('ClaimClosed','InvoiceRejectionAccepted')
    and vh.days != 0
    and i.hire_gross > 0;

update dashboard_kbbs d
    set daily_rate = i.hire_rate_charged_per_day
from claim c, invoice i
where d.claim_id = c.id and c.invoice_id = i.id
  and d.daily_rate < 0;

update dashboard_kbbs d
    set bre_status = 'Passed Not Contested'
from audit_trail at
where at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.new_status in ('InvoiceApprovedByBRE','ManualInvoiceBREApproved'))
  and not exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set bre_status = 'Passed Not Contested'
from audit_trail at
where at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.original_status = 'InvoiceDataCalculationIncorrect' and at2.new_status='AwaitingInvoicePayment')
  and not exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set bre_status = 'Passed Contested'
from audit_trail at
where at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.new_status in ('InvoiceApprovedByBRE','ManualInvoiceBREApproved'))
  and exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set bre_status = 'Failed Not Contested'
from audit_trail at
where at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.new_status in ('InvoiceEscalated','InvoiceEscalatedToHandler','ManualInvoiceBRERejected'))
  and not exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set bre_status = 'Failed Contested'
from audit_trail at
where at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.new_status in ('InvoiceEscalated','InvoiceEscalatedToHandler','ManualInvoiceBRERejected'))
  and exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set bre_status = 'Failed Contested'
from audit_trail at
where bre_status is null and at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.original_status in ('InvoiceEscalated','InvoiceEscalatedToHandler'))
  and exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set bre_status = 'Failed Not Contested'
from audit_trail at
where bre_status is null and at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.original_status in ('InvoiceEscalated','InvoiceEscalatedToHandler'))
  and not exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set bre_status = 'Passed Not Contested'
from audit_trail at
where bre_status is null and at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.original_status = 'InvoiceApprovedByBRE')
  and not exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set bre_status = 'Passed Contested'
from audit_trail at
where bre_status is null and at.claim_id = d.claim_id
  and at.reverted = false and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid')
  and exists (select * from audit_trail at2 where at2.claim_id = d.claim_id and at2.reverted=false and at2.original_status = 'InvoiceApprovedByBRE')
  and exists (select * from audit_trail at3 where at3.claim_id = d.claim_id and at3.reverted=false and at3.new_status in ('ContestedInvoiceReferredToCHO','ManualInvoiceContested'));

update dashboard_kbbs d
    set acknowledge_days = at.created_date::date - d.claim_upload_date::date + 1,
        acknowledge_date = at.created_date::date
from audit_trail at
where d.claim_id = at.claim_id
  and at.new_status in ('AwaitingCarHireInfo','ClaimRejected','SubscriberClaimRejected') and at.reverted=false
  and not exists (select * from audit_trail at2 where at2.claim_id=d.claim_id and at2.new_status in ('AwaitingCarHireInfo','ClaimRejected','SubscriberClaimRejected') and at.reverted=false and at2.created_date < at.created_date);

update dashboard_kbbs d
    set liability_days = co.created_date::date - d.claim_upload_date::date + 1,
        liability_date = co.created_date::date
from comment co, claim c
where d.claim_id = co.claim_id and d.claim_id=c.id
  and co.comment like 'Liability status changed%'
  and ((c.liability_status = 1 and co.comment like E'%to \'Full Liability Accepted%')
       or (c.liability_status = 4 and co.comment like E'%to \'Liability Repudiated%')
       or (c.liability_status = 5 and co.comment like E'%to \'Liability Split%')
       or (c.liability_status = 6 and co.comment like E'%to \'Proceed Without Prejudice%'))
  and not exists (select * from comment co2 where co2.claim_id = d.claim_id and co2.comment like 'Liability status changed%' and co2.created_date > co.created_date);

update dashboard_kbbs d
    set liability_days = c.liability_agreed_date::date - d.claim_upload_date::date + 1,
        liability_date = c.liability_agreed_date::date
from claim c
where d.claim_id=c.id and d.liability_date is null;

update dashboard_kbbs d
    set lifecycle_days = at.created_date::date - d.claim_upload_date::date + 1
from audit_trail at
where d.claim_id = at.claim_id
  and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted=false;

  update dashboard_kbbs d
      set payment_days = at.created_date::date - d.invoice_upload_date::date + 1
  from audit_trail at
  where d.claim_id = at.claim_id
    and at.new_status in ('InvoicePaymentLogged','ManualInvoicePaid') and at.reverted=false;


RETURN TRUE;
  END;
$BODY$
    LANGUAGE plpgsql VOLATILE
    COST 100;

