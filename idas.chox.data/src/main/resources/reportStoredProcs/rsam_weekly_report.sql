drop function rsam_weekly_report(text, int);
create or replace function rsam_weekly_report
(
   dat text, insurerId int
)
returns table
(
   "Grouping" character varying,
   "Type" text,
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
as $$ DECLARE 
datStart date;
datEnd date;
insId int;
BEGIN 
    datStart = dat::date;
    datEnd = (datStart - interval '1 week')::date;
    insId = insurerId;
RETURN QUERY

--Column 1: Grouping
SELECT 'ALL' as Grouping,

--Column 2: Type
  'ALL' as Type,
  
--Column 3: New Cases - Claims uploaded in the past week.
  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND c.created_date BETWEEN datEnd AND datStart
     AND c.insurer_id = insId) AS "New Cases",

--Column 4: Open Claims Period Start - all claims in an open status at the end of the previous week (2359 Sunday minus 1 week).     
  (SELECT count(*)
    FROM audit_trail a, claim c
    JOIN chorganisation cho ON c.chorganisation_id = cho.id
    WHERE cho.insurer_upload_only = FALSE
      AND c.id = a.claim_id
      AND c.insurer_id = insId
      AND c.created_date < datEnd
      AND a.new_status NOT IN ('PaymentReceived',
                               'ClaimClosed',
                               'ClaimRejectionAccepted',
                               'InvoiceRejectionAccepted')
      AND a.id =
	  (SELECT id
	   FROM audit_trail AT
	   WHERE AT.claim_id=c.id
	     AND AT.reverted=FALSE
	     AND AT.created_date =
	       (SELECT max(created_date) AS max_created_date
	        FROM audit_trail a3
	        WHERE a3.claim_id = c.id
	          AND a3.reverted=FALSE
	          AND a3.created_date < datEnd)
	   ORDER BY id DESC LIMIT 1)) as "Open Claims Period Start ",
     
--Column 5: Open Claims Period End - all claims in an open status at the end of the week (2359 Sunday).                               
  (SELECT count(*)
    FROM audit_trail a, claim c
    JOIN chorganisation cho ON c.chorganisation_id = cho.id
    WHERE cho.insurer_upload_only = FALSE
      AND c.id = a.claim_id
      AND c.created_date < datStart
      AND c.insurer_id = insId
      AND a.new_status NOT IN ('PaymentReceived',
                               'ClaimClosed',
                               'ClaimRejectionAccepted',
                               'InvoiceRejectionAccepted')
      AND a.id =
	  (SELECT id
	   FROM audit_trail AT
	   WHERE AT.claim_id=c.id
	     AND AT.reverted=FALSE
	     AND AT.created_date =
	       (SELECT max(created_date) AS max_created_date
	        FROM audit_trail a3
	        WHERE a3.claim_id = c.id
	          AND a3.reverted=FALSE
	          AND a3.created_date < datStart)
	   ORDER BY id DESC LIMIT 1)) as "Open Claims Period End ",
                               
--Column 6: Settled/Closed Claims - all claims that moved to a 'closed' status during the week (any of Claim Closed, Claim Rejection Accepted, Invoice Rejection Accepted or Payment Received).                               
  (SELECT count(*)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.insurer_id = insId
     AND c.created_date < datStart
     AND a.new_status IN ('PaymentReceived',
                          'ClaimClosed',
                          'ClaimRejectionAccepted',
                          'InvoiceRejectionAccepted')
     AND a.update_date BETWEEN datEnd AND datStart
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)) AS "Settled/Closed Cases",
 
--Column 7: Volume Approved By BRE and Paid - all claims that moved into status Payment Received
--          in the past week and have been at status Invoice Approved By BRE but NOT been in status
--          Contested Invoice Referred To CHO previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.insurer_id = insId
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Approved By BRE and Paid",

--Column 8: Value of Approved By BRE and Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.insurer_id = insId
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) ))AS "Value Approved By BRE and Paid",

--Column 9: Volume Approved By BRE, Contested and Paid - all claims that moved into status Payment Received
--          in the past week and have been at status Invoice Approved By BRE AND status Contested Invoice
--          Referred To CHO previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart))
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart)) ) AS "Volume Approved By BRE, Contested and Paid",

--Column 10: Value of Approved By BRE, Contested and Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) ) ) AS "Value Approved By BRE, Contested and Paid",

--Column 11: Volume Escalated then Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Escalated To Handler previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Escalated then Paid",

--Column 12: Value of Escalated then Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Value Escalated then Paid",

--Column 13: Volume Escalated then Closed - all claims that moved into status Invoice Rejection Accepted or Claim Closed in the past week and have been at status Invoice Escalated To Handler previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Escalated then Closed",

--Column 14: Value of Escalated then Closed - as above but to report on Original Full Total Requested (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Value Escalated then Closed"
          
UNION

--Column 1: Grouping
SELECT cho1.name AS Grouping,

--Column 2:Type
  'CHO' as Type,

--Column 3: New Cases - Claims uploaded in the past week.
  (SELECT count(*)
   FROM claim c
   WHERE c.chorganisation_id = cho1.id
     AND c.created_date BETWEEN datEnd AND datStart
     AND c.insurer_id = insId) AS "New Cases",

--Column 4: Open Claims Period Start - all claims in an open status at the end of the previous week (2359 Sunday minus 1 week).     
  (SELECT count(*)
    FROM audit_trail a, claim c
    WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
      AND a.claim_id = c.id
      AND c.created_date < datEnd
      AND a.new_status NOT IN ('PaymentReceived',
                               'ClaimClosed',
                               'ClaimRejectionAccepted',
                               'InvoiceRejectionAccepted')
      AND a.id =
  (SELECT id
   FROM audit_trail AT
   WHERE AT.claim_id=c.id
     AND AT.reverted=FALSE
     AND AT.created_date =
       (SELECT max(created_date) AS max_created_date
        FROM audit_trail a3
        WHERE a3.claim_id = c.id
          AND a3.reverted=FALSE
          AND a3.created_date < datEnd)
   ORDER BY id DESC LIMIT 1)) as "Open Claims Period Start ",
     
--Column 5: Open Claims Period End - all claims in an open status at the end of the week (2359 Sunday).                               
  (SELECT count(*)
    FROM audit_trail a, claim c
    WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
      AND a.claim_id = c.id
      AND c.created_date < datStart
      AND a.new_status NOT IN ('PaymentReceived',
                               'ClaimClosed',
                               'ClaimRejectionAccepted',
                               'InvoiceRejectionAccepted')
      AND a.id =
  (SELECT id
   FROM audit_trail AT
   WHERE AT.claim_id=c.id
     AND AT.reverted=FALSE
     AND AT.created_date =
       (SELECT max(created_date) AS max_created_date
        FROM audit_trail a3
        WHERE a3.claim_id = c.id
          AND a3.reverted=FALSE
          AND a3.created_date < datStart)
   ORDER BY id DESC LIMIT 1)) as "Open Claims Period End ",
                               
--Column 6: Settled/Closed Claims - all claims that moved to a 'closed' status during the week (any of Claim Closed, Claim Rejection Accepted, Invoice Rejection Accepted or Payment Received).                               
  (SELECT count(*)
   FROM audit_trail a, claim c
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND c.created_date < datStart
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status IN ('PaymentReceived',
                          'ClaimClosed',
                          'ClaimRejectionAccepted',
                          'InvoiceRejectionAccepted')
     AND a.update_date BETWEEN datEnd AND datStart) AS "Settled/Closed Cases",
 
--Column 7: Volume Approved By BRE and Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Approved By BRE but NOT been in status Contested Invoice Referred To CHO previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE OR a1.last_modified_date > datStart) )
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Approved By BRE and Paid",

--Column 8: Value of Approved By BRE and Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) ))AS "Value Approved By BRE and Paid",

--Column 9: Volume Approved By BRE, Contested and Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Approved By BRE AND status Contested Invoice Referred To CHO previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart))
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart)) ) AS "Volume Approved By BRE, Contested and Paid",

--Column 10: Value of Approved By BRE, Contested and Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) ) ) AS "Value Approved By BRE, Contested and Paid",

--Column 11: Volume Escalated then Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Escalated To Handler previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE
                     OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Escalated then Paid",

--Column 12: Value of Escalated then Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Value Escalated then Paid",

--Column 13: Volume Escalated then Closed - all claims that moved into status Invoice Rejection Accepted or Claim Closed in the past week and have been at status Invoice Escalated To Handler previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Escalated then Closed",

--Column 14: Value of Escalated then Closed - as aboe but to report on Original Full Total Requested (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   WHERE c.chorganisation_id = cho1.id AND c.insurer_id = insId
     AND a.claim_id = c.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Value Escalated then Closed"

FROM chorganisation cho1, insurer_chorganisation ic
WHERE ic.insurer_id = insId
  AND ic.chorganisation_id = cho1.id
  AND cho1.insurer_upload_only = FALSE 
          

UNION
 
--Column 1: Grouping
SELECT wu.first_name || ' '  || wu.last_name AS Grouping, 

--Column 2:Type
  'Handler' as Type,

--Column 3: New Cases - Claims uploaded in the past week.
  (SELECT count(*)
   FROM claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE 
     AND c.claim_owner_id = wu.id
     AND c.created_date BETWEEN datEnd AND datStart) AS "New Cases",

--Column 4: Open Claims Period Start - all claims in an open status at the end of the previous week (2359 Sunday minus 1 week).     
  (SELECT count(*)
    FROM audit_trail a, claim c
    JOIN chorganisation cho ON c.chorganisation_id = cho.id
    WHERE cho.insurer_upload_only = FALSE
      AND c.id = a.claim_id
      AND c.created_date < datEnd
      AND c.claim_owner_id = wu.id
      AND a.new_status NOT IN ('PaymentReceived',
                               'ClaimClosed',
                               'ClaimRejectionAccepted',
                               'InvoiceRejectionAccepted')
      AND a.id =
  (SELECT id
   FROM audit_trail AT
   WHERE AT.claim_id=c.id
     AND AT.reverted=FALSE
     AND AT.created_date =
       (SELECT max(created_date) AS max_created_date
        FROM audit_trail a3
        WHERE a3.claim_id = c.id
          AND a3.reverted=FALSE
          AND a3.created_date < datEnd)
   ORDER BY id DESC LIMIT 1)) as "Open Claims Period Start ",
     
--Column 5: Open Claims Period End - all claims in an open status at the end of the week (2359 Sunday).                               
  (SELECT count(*)
    FROM audit_trail a, claim c
    JOIN chorganisation cho ON c.chorganisation_id = cho.id
    WHERE cho.insurer_upload_only = FALSE
      AND c.id = a.claim_id
      AND c.created_date < datStart
      AND c.claim_owner_id = wu.id
      AND a.new_status NOT IN ('PaymentReceived',
                               'ClaimClosed',
                               'ClaimRejectionAccepted',
                               'InvoiceRejectionAccepted')
      AND a.id =
  (SELECT id
   FROM audit_trail AT
   WHERE AT.claim_id=c.id
     AND AT.reverted=FALSE
     AND AT.created_date =
       (SELECT max(created_date) AS max_created_date
        FROM audit_trail a3
        WHERE a3.claim_id = c.id
          AND a3.reverted=FALSE
          AND a3.created_date > datStart)
   ORDER BY id DESC LIMIT 1)) as "Open Claims Period End ",
                               
--Column 6: Settled/Closed Claims - all claims that moved to a 'closed' status during the week (any of Claim Closed, Claim Rejection Accepted, Invoice Rejection Accepted or Payment Received).                               
  (SELECT count(*)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND c.created_date < datStart
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status IN ('PaymentReceived',
                          'ClaimClosed',
                          'ClaimRejectionAccepted',
                          'InvoiceRejectionAccepted')
     AND a.update_date BETWEEN datEnd AND datStart
     AND reverted=FALSE) AS "Settled/Closed Cases",
 
--Column 7: Volume Approved By BRE and Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Approved By BRE but NOT been in status Contested Invoice Referred To CHO previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Approved By BRE and Paid",

--Column 8: Value of Approved By BRE and Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )
     AND NOT EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) ))AS "Value Approved By BRE and Paid",

--Column 9: Volume Approved By BRE, Contested and Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Approved By BRE AND status Contested Invoice Referred To CHO previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart))
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart)) ) AS "Volume Approved By BRE, Contested and Paid",

--Column 10: Value of Approved By BRE, Contested and Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'ContestedInvoiceReferredToCHO'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceApprovedByBRE'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) ) ) AS "Value Approved By BRE, Contested and Paid",

--Column 11: Volume Escalated then Paid - all claims that moved into status Payment Received in the past week and have been at status Invoice Escalated To Handler previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Escalated then Paid",

--Column 12: Value of Escalated then Paid - as above but to report on Total To Pay figure (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status = 'PaymentReceived'
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Value Escalated then Paid",

--Column 13: Volume Escalated then Closed - all claims that moved into status Invoice Rejection Accepted or Claim Closed in the past week and have been at status Invoice Escalated To Handler previously.
  (SELECT count(c.id)
   FROM audit_trail a, claim c
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Volume Escalated then Closed",

--Column 14: Value of Escalated then Closed - as aboe but to report on Original Full Total Requested (SUM)
  (SELECT sum(i.total_to_pay)
   FROM audit_trail a, claim c
   JOIN invoice i ON c.invoice_id = i.id
   JOIN chorganisation cho ON c.chorganisation_id = cho.id
   WHERE cho.insurer_upload_only = FALSE
     AND a.claim_id = c.id
     AND c.claim_owner_id = wu.id
     AND (a.reverted=FALSE OR a.last_modified_date > datStart)
     AND a.new_status IN ('InvoiceRejectionAccepted',
                          'ClaimClosed')
     AND a.update_date BETWEEN datEnd AND datStart
     AND EXISTS
       (SELECT *
        FROM audit_trail a1
        WHERE a1.claim_id=c.id
          AND a1.original_status = 'InvoiceEscalatedToHandler'
          AND (a1.reverted=FALSE
                     OR a1.last_modified_date > datStart) )) AS "Value Escalated then Closed"


FROM web_user wu
WHERE wu.insurer_id = insId


ORDER BY Type, Grouping;



END
;
$$ LANGUAGE plpgsql
;
GRANT EXECUTE ON FUNCTION rsam_weekly_report(text, integer) TO chox_user;
GRANT EXECUTE ON FUNCTION rsam_weekly_report(text, integer) TO chox_mi;
