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

SELECT 'Total figures across the Insurer' as Grouping,
--COLUMN2


  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND c.created_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND c.insurer_id = params.insurerId) AS "New Cases",
 --COLUMN3


  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.update_date BETWEEN (params.startDate - interval '2 week')::date AND (params.startDate - interval '1 week')::date
          AND a.claim_id = c.id
          AND a.reverted = FALSE
          AND a.new_status IN ('PaymentReceived',
                               'ClaimClosed',
                               'ClaimRejectionAccepted',
                               'InvoiceRejectionAccepted'))) AS "Open Claims Period Start",
 --COLUMN4


  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
          AND a.claim_id = c.id
          AND a.reverted = FALSE
          AND a.new_status IN ('PaymentReceived',
                               'ClaimClosed',
                               'ClaimRejectionAccepted',
                               'InvoiceRejectionAccepted'))) AS "Open Claims Period End",
 --COLUMN5


  (SELECT count(*)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND a.claim_id = c.id
     AND a.new_status IN ('PaymentReceived',
                          'ClaimClosed',
                          'ClaimRejectionAccepted',
                          'InvoiceRejectionAccepted')
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND reverted=FALSE) AS "Settled/Closed Cases",
 --COLUMN6


  (SELECT count(c.id)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND a.claim_id = c.id
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
 --COLUMN7


  (SELECT sum(i.total_to_pay)
   FROM audit_trail a,
                    claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND a.claim_id = c.id
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
 --COLUMN8


  (SELECT count(c.id)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND a.claim_id = c.id
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
 --COLUMN9


  (SELECT sum(i.total_to_pay)
   FROM audit_trail a,
                    claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND a.claim_id = c.id
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
 --COLUMN10


  (SELECT count(c.id)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND a1.reverted = FALSE )) AS "Volume Escalated then Paid",
 --COLUMN11


  (SELECT sum(i.total_to_pay)
   FROM audit_trail a,
                    claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND a1.reverted = FALSE )) AS "Value Escalated then Paid",
 --COLUMN12


  (SELECT count(c.id)
   FROM audit_trail a,
                    claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND a.claim_id = c.id
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN (params.startDate - interval '1 week')::date AND params.startDate
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND a1.reverted = FALSE )) AS "Volume Escalated then Closed",
 --COLUMN13


  (SELECT sum(i.total_to_pay)
   FROM audit_trail a,
                    claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND a.claim_id = c.id
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
and c.chorganisation_id = cho.id

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
