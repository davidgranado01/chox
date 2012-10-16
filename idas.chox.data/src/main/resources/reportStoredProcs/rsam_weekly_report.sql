drop function rsam_weekly_report(text, text, int, int, int);
create or replace function rsam_weekly_report
(
   startDate text, endDate text, choId int, ownerId int, insurerId int
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
dat2 date;
BEGIN 
	dat1 = startDate::Date;
	dat2 = endDate::Date;
RETURN QUERY

SELECT
  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND c.created_date BETWEEN params.startDate AND params.endDate
     AND c.insurer_id = params.insurerId) AS "New Cases",

  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND c.created_date BETWEEN (params.startDate - interval '1 week')::date AND (params.endDate - interval '1 week')::date
     AND c.status NOT IN ('PaymentReceived',
                          'ClaimClosed',
                          'ClaimRejectionAccepted',
                          'InvoiceRejectionAccepted')) AS "Open Claims Period Start",

  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND c.created_date BETWEEN params.startDate AND params.endDate
     AND c.status NOT IN ('PaymentReceived',
                          'ClaimClosed',
                          'ClaimRejectionAccepted',
                          'InvoiceRejectionAccepted')) AS "Open Claims Period End",

  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND c.created_date BETWEEN params.startDate AND params.endDate
     AND c.status IN ('PaymentReceived',
                      'ClaimClosed',
                      'ClaimRejectionAccepted',
                      'InvoiceRejectionAccepted')
     AND EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id = c.id
          AND a.new_status=c.status
          AND a.update_date BETWEEN params.startDate AND params.endDate
          AND reverted=FALSE)) AS "Settled/Closed Cases",

  (SELECT count(c.id)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id=c.id
          AND a.new_status = 'PaymentReceived'
          AND reverted = FALSE
          AND a.update_date BETWEEN params.startDate AND params.endDate
          AND a.original_status IN ('InvoiceApprovedByBRE'))
     AND insurer_id = params.insurerId) AS "Volume Approved By BRE and Paid",

  (SELECT sum(i.total_to_pay)
   FROM claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id=c.id
          AND a.new_status = 'PaymentReceived'
          AND reverted = FALSE
          AND a.update_date BETWEEN params.startDate AND params.endDate
          AND a.original_status IN ('InvoiceApprovedByBRE'))
     AND insurer_id = params.insurerId)AS "Value Approved By BRE and Paid",

  (SELECT count(c.id)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id=c.id
          AND a.original_status IN ('ContestedInvoiceReferredToCHO',
                                    'InvoiceApprovedByBRE')
          AND a.new_status = 'PaymentReceived'
          AND reverted = FALSE
          AND a.update_date BETWEEN params.startDate AND params.endDate )) AS "Volume Approved By BRE, Contested and Paid",

  (SELECT sum(i.total_to_pay)
   FROM claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id=c.id
          AND a.original_status IN ('ContestedInvoiceReferredToCHO',
                                    'InvoiceApprovedByBRE')
          AND a.new_status = 'PaymentReceived'
          AND reverted = FALSE
          AND a.update_date BETWEEN params.startDate AND params.endDate )) AS "Value Approved By BRE, Contested and Paid",

  (SELECT count(c.id)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id=c.id
          AND a.original_status = 'InvoiceEscalatedToHandler'
          AND a.new_status = 'PaymentReceived'
          AND reverted = FALSE
          AND a.update_date BETWEEN params.startDate AND params.endDate )) AS "Volume Escalated then Paid",

  (SELECT sum(i.total_to_pay)
   FROM claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id=c.id
          AND a.original_status = 'InvoiceEscalatedToHandler'
          AND a.new_status = 'PaymentReceived'
          AND reverted = FALSE
          AND a.update_date BETWEEN params.startDate AND params.endDate )) AS "Value Escalated then Paid",

  (SELECT count(c.id)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id=c.id
          AND a.original_status = 'InvoiceEscalatedToHandler'
          AND a.new_status IN ('InvoiceRejectionAccepted',
                               'ClaimClosed')
          AND reverted = FALSE
          AND a.update_date BETWEEN params.startDate AND params.endDate )) AS "Volume Escalated then Closed",

  (SELECT sum(i.total_to_pay)
   FROM claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE (cho.id = params.choId
          OR params.choId = -1)
     AND (c.id = params.ownerId
          OR params.ownerId = -1)
     AND insurer_id = params.insurerId
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a
        WHERE a.claim_id=c.id
          AND a.original_status = 'InvoiceEscalatedToHandler'
          AND a.new_status IN ('InvoiceRejectionAccepted',
                               'ClaimClosed')
          AND reverted = FALSE
          AND a.update_date BETWEEN params.startDate AND params.endDate )) AS "Volume Escalated then Closed"
FROM
  (SELECT dat1 AS startDate,
          dat2 AS endDate,
          choId AS choId,
          ownerId AS ownerId,
          insurerId AS insurerId) params ;

END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION rsam_weekly_report(text, text, integer, integer, integer) TO chox_user;
GRANT EXECUTE ON FUNCTION rsam_weekly_report(text, text, integer, integer, integer) TO chox_mi;
