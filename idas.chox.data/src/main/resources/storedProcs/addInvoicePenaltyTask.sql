--
-- Function: addInvoicePenaltyTask(integer)
--
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
from claim c, invoice i, insurer ins
where c.invoice_id = i.id
  and i.penalty_alert_qty = 2
  and c.insurer_id = ins.id
  and c.claim_type != 3
  and ins.is_task_management_enable = true
  and c.status not in ('InvoicePaymentLogged', 'ClaimClosed', 'InvoiceRejectionAccepted', 'PaymentReceived', 'InvoiceDataCalculationIncorrect')
  and extract(epoch from now() - i.created_date)/(3600*24.0) >= 75
  and extract(epoch from now() - i.created_date)/(3600*24.0) < 90
  and not exists (select * from task where claim_id = c.id and task_type like 'Invoice Approaching%' and now() - created_date < '10 days')
  and ((liability_status is null or (liability_status !=5 and liability_status!=6)) or ((liability_status = 5 or liability_status =6 ) and extract(epoch from now() - c.liability_agreed_date)/(3600*24) >= 75));

insert into task(claim_id, due_date, task_type, description, insurer, visibility, visibility_role, created_by, created_date, last_modified_by, last_modified_date, version)
select c.id, now() + interval '5 days', 'Invoice Approaching ' || 30*(i.penalty_alert_qty+1) || ' Days', 'The invoice was uploaded over ' || 30*(i.penalty_alert_qty+1)-5 || ' days ago and may be subject to penalties in 5 days time.',
       true, 2, 'ROLE_INS_CH', userId, now(), userId, now(), 0
from claim c, invoice i, insurer ins
where c.invoice_id = i.id
  and i.penalty_alert_qty >= 0 and i.penalty_alert_qty < 2
  and c.insurer_id = ins.id
  and c.claim_type != 3
  and ins.is_task_management_enable = true
  and c.status not in ('InvoicePaymentLogged', 'ClaimClosed', 'InvoiceRejectionAccepted', 'PaymentReceived', 'InvoiceDataCalculationIncorrect')
  and extract(epoch from now() - i.created_date)/(3600*24.0) >= ((i.penalty_alert_qty+1)*30 - 5)
  and extract(epoch from now() - i.created_date)/(3600*24.0) < 90
  and not exists (select * from task where claim_id = c.id and task_type like 'Invoice Approaching%' and now() - created_date < '29 days')
  and ((liability_status is null or (liability_status !=5 and liability_status!=6)) or ((liability_status = 5 or liability_status =6 ) and extract(epoch from now() - c.liability_agreed_date)/(3600*24) > ((i.penalty_alert_qty+1)*30)-5));


update task set complete = true,
                completed_by = 999,
                completed_date = now()
where due_date < now() and task_type like 'Invoice Approaching%' and complete = false;


return true;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION addinvoicepenaltytask(integer) TO chox_user;
