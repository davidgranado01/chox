drop function rsam_weekly_report(text, int);
create or replace function rsam_weekly_report
(
   startDate text, insurerId int
)
returns table
(
   "New Cases" bigint,
   "Open Claims Period Start" bigint,
   "Open Claims Period End" bigint,
   "Settled/Closed Cases" bigint,
   "Volume Approved By BRE and Paid" bigint,
   "Value Approved By BRE and Paid" numeric(10,2),
   "Volume Approved By BRE, Contested and Paid" bigint,
   "Value Approved By BRE, Contested and Paid" numeric(10,2),
   "Volume Escalated then Paid" bigint,
   "Value Escalated then Paid" numeric(10,2),
   "Volume Escalated then Closed" bigint,
   "Value Escalated then Closed" numeric(10,2)
)
as $$ DECLARE dat1 date;
BEGIN 
	dat1 = startDate::Date;
RETURN QUERY

Column 1: Grouping
SELECT 'Total figures across the Insurer' as Grouping,
 
--Column 2: New Cases - Claims uploaded in the past week.
  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.created_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND c.insurer_id = params.insurerId) AS "New Cases",

--Column 3: Open Claims Period Start - all claims in an open status at the end of the previous week (2359 Sunday minus 1 week).     
  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.insurer_id = params.insurerId
     AND NOT EXISTS
      (SELECT *
       FROM audit_trail a
       WHERE a.claim_id = c.id
         AND a.reverted = FALSE
         AND a.update_date < (params.startDate - interval '2 week')::date 
         ORDER BY a.update_date LIMIT 1) ) AS "Open Claims Period Start",
     
--Column 4: Open Claims Period End - all claims in an open status at the end of the week (2359 Sunday).                               
  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.insurer_id = params.insurerId
     AND NOT EXISTS
      (SELECT *
       FROM audit_trail a
       WHERE a.claim_id = c.id
         AND a.reverted = FALSE
         AND a.update_date < (params.startDate - interval '1 week')::date 
         ORDER BY a.update_date LIMIT 1) ) AS "Open Claims Period Start",
                               
--Column 5: Settled/Closed Claims - all claims that moved to a 'closed' status during the week (any of Claim Closed, Claim Rejection Accepted, Invoice Rejection Accepted or Payment Received).                               
  (SELECT count(*)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND a.claim_id = c.id
     AND c.insurer_id = params.insurerId
     AND a.reverted = FALSE
     AND a.new_status IN ('PaymentReceived',
                          'ClaimClosed',
                          'ClaimRejectionAccepted',
                          'InvoiceRejectionAccepted')
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND reverted=FALSE) AS "Settled/Closed Cases",
 
--Column 6: Volume Approved By BRE and Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Approved By BRE but NOT been in status Contested Invoice Referred To CHO previously.
  (SELECT count(c.id)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND a.claim_id = c.id
     AND c.insurer_id = params.insurerId
     AND a.reverted = FALSE
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND a1.reverted = FALSE )
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND a1.reverted = FALSE )) AS "Volume Approved By BRE and Paid",

--Column 7: Value of Approved By BRE and Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a,
                    claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND a.claim_id = c.id
     AND c.insurer_id = params.insurerId
     AND a.reverted = FALSE
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND a1.reverted = FALSE )
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND a1.reverted = FALSE ))AS "Value Approved By BRE and Paid",

--Column 8: Volume Approved By BRE, Contested and Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Approved By BRE AND status Contested Invoice Referred To CHO previously.
  (SELECT count(c.id)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.reverted = FALSE
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND a1.reverted = FALSE)
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND a1.reverted = FALSE) ) AS "Volume Approved By BRE, Contested and Paid",

--Column 9: Value of Approved By BRE, Contested and Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a,
                    claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.reverted = FALSE
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND a1.reverted = FALSE )
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND a1.reverted = FALSE ) ) AS "Value Approved By BRE, Contested and Paid",

--Column 10: Volume Escalated then Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Escalated To Handler previously.
  (SELECT count(c.id)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.reverted = FALSE
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND a1.reverted = FALSE )) AS "Volume Escalated then Paid",

--Column 11: Value of Escalated then Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a,
                    claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.reverted = FALSE
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND a1.reverted = FALSE )) AS "Value Escalated then Paid",

--Column 12: Volume Escalated then Closed - all claims that moved into status Invoice Rejection Accepted or Claim Closed in the past week and have been at status Invoice Escalated To Handler previously.
  (SELECT count(c.id)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.reverted = FALSE
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND a1.reverted = FALSE )) AS "Volume Escalated then Closed",

--Column 13: Value of Escalated then Closed - as aboe but to report on Original Full Total Requested (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a,
                    claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.claim_owner_id = params.ownerId
          OR params.ownerId = -1)
     AND c.insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.reverted = FALSE
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND a1.reverted = FALSE )) AS "Value Escalated then Closed"
FROM
  (SELECT dat1 AS startDate,
          -1 AS choId,
          -1 AS ownerId,
          insurerId AS insurerId) params 
          
          
UNION

select cho.name 
TODO:add query
from chorganisation cho, claim cl (SELECT dat1 AS startDate,
          cho.id AS choId,
          -1 AS ownerId,
          insurerId AS insurerId) params 
where cl.isnurer_id = insurerId
and cl.chorganisation_id = cho.id

UNION

select ow.first_name || ow.last_name
TODO:add query
from web_user ow, claim cl (SELECT dat1 AS startDate,
          -1 AS choId,
          ow.id AS ownerId,
          insurerId AS insurerId) params 
where cl.isnurer_id = insurerId
and c.claim_owner_id = ow.id

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION rsam_weekly_report(text, integer) TO chox_user;
GRANT EXECUTE ON FUNCTION rsam_weekly_report(text, integer) TO chox_mi;
