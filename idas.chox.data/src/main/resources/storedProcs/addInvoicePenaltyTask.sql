DROP FUNCTION addInvoicePenaltyTask(integer);

CREATE OR REPLACE FUNCTION addInvoicePenaltyTask(integer)
  RETURNS boolean AS
$BODY$

DECLARE
userId int;

BEGIN

userId=$1;

insert into task(claim_id, due_date, task_type, description, insurer, visibility, visibility_role, created_by, created_date, last_modified_by, last_modified_date, version)
select c.id, now() + interval '15 days', 'Invoice Approaching 90 Days', 'The invoice was uploaded over 75 days ago and may be subject to penalties in 15 days time.',
       true, 2, 'ROLE_INS_CH', userId, now(), userId, now(), 0
from claim c, invoice i, insurer ins, bre_band_organisation bo, bre_band bre
where c.invoice_id = i.id
  and c.chorganisation_id = bo.chorganisation_id
  and bo.band_id = bre.id
  and bre.insurer_id = c.insurer_id
  and i.penalty_band = 90
  and c.insurer_id = ins.id
  and ( (c.claim_type in (0,1,2) and bre.allow_gta_penalty_charges =  true)
        or (c.claim_type = 3 and bre.allow_tpi_penalty_charges =  true)
        or (c.claim_type in (4,5,6) and bre.allow_ins_vs_ins_penalty_charges =  true)
        or (c.claim_type in (7,8,9) and bre.allow_subscriber_penalty_charges =  true)
        or (c.claim_type in (11,12,13) and bre.allow_fixed_fee_penalty_charges =  true)
        or (c.claim_type in (18,19,20) and bre.allow_collaboration_penalty_charges =  true)
      )
  and ins.is_task_management_enable = true
  and c.status NOT IN ('ClaimClosed', 'InvoiceRejectionAccepted', 'PaymentReceived', 'InvoicePaymentLogged', 'InvoiceDataCalculationIncorrect')
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) >= 75
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < 90
  and not exists (select * from task where claim_id = c.id and task_type like 'Invoice Approaching 90 Days%')
  and ((liability_status is null or (liability_status !=5 and liability_status!=6)) or ((liability_status = 5 or liability_status =6 ) and extract(epoch from now() - c.liability_agreed_date)/(3600*24) >= 75));

insert into task(claim_id, due_date, task_type, description, insurer, visibility, visibility_role, created_by, created_date, last_modified_by, last_modified_date, version)
select c.id, now() + interval '5 days', 'Invoice Approaching ' || i.penalty_band || ' Days', 'The invoice was uploaded over ' || i.penalty_band - 5 || ' days ago and may be subject to penalties in 5 days time.',
       true, 2, 'ROLE_INS_CH', userId, now(), userId, now(), 0
from claim c, invoice i, insurer ins, bre_band_organisation bo, bre_band bre
where c.invoice_id = i.id
  and c.chorganisation_id = bo.chorganisation_id
  and bo.band_id = bre.id
  and bre.insurer_id = c.insurer_id
  and i.penalty_band != -1 and i.penalty_band < 90
  and c.insurer_id = ins.id
  and ( (c.claim_type in (0,1,2) and bre.allow_gta_penalty_charges =  true)
        or (c.claim_type = 3 and bre.allow_tpi_penalty_charges =  true)
        or (c.claim_type in (4,5,6) and bre.allow_ins_vs_ins_penalty_charges =  true)
        or (c.claim_type in (7,8,9) and bre.allow_subscriber_penalty_charges =  true)
        or (c.claim_type in (11,12,13) and bre.allow_fixed_fee_penalty_charges =  true)
        or (c.claim_type in (18,19,20) and bre.allow_collaboration_penalty_charges =  true)
      )
  and ins.is_task_management_enable = true
  and c.status NOT IN ('ClaimClosed', 'InvoiceRejectionAccepted', 'PaymentReceived', 'InvoicePaymentLogged', 'InvoiceDataCalculationIncorrect')
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) >= (i.penalty_band - 5)
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < i.penalty_band
  and extract(epoch from now() - i.auto_penalty_start)/(3600*24.0) < 90
  and not exists (select * from task where claim_id = c.id and task_type like 'Invoice Approaching%' and now() - created_date < '6 days')
  and ((liability_status is null or (liability_status !=5 and liability_status!=6)) or ((liability_status = 5 or liability_status =6 ) and extract(epoch from now() - c.liability_agreed_date)/(3600*24) > i.penalty_band - 5));


update task set complete = true,
                completed_by = 999,
                completed_date = now(),
                version = version + 1
where due_date < now() and task_type like 'Invoice Approaching%' and complete = false;


return true;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION addInvoicePenaltyTask(integer) TO chox_user;
