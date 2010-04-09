-- 
update insurer set scs_agreed_benefit_share_value = 12.50 where name = 'RSA'
--update invoice table
update invoice set total_to_pay = full_total_to_pay,original_total_to_pay = original_full_total_to_pay;
--
update claim set percentage_liability_cho = 0.00 where percentage_liability_cho is null;

--ClaimRejectionAccepted
update claim set liability_status = 3 where status = 'ClaimRejectionAccepted' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Out of Scope');
update claim set liability_status = 2 where status = 'ClaimRejectionAccepted' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Liability Issues');
update claim set liability_status = 2 where status = 'ClaimRejectionAccepted' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Indemnity and Liability Issues');
update claim set liability_status = 6 where status = 'ClaimRejectionAccepted' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Indemnity Issues');
update claim set liability_status = 4 where status = 'ClaimRejectionAccepted' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Not our Policyholder');
update claim set liability_status = 3 where status = 'ClaimRejectionAccepted' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Other');
update claim set liability_status = 3 where status = 'ClaimRejectionAccepted' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Quantum');
--PaymentReceived
update claim set liability_status = 1 where status = 'PaymentReceived';
--ClaimClosed
update claim set liability_status = 1 where status = 'ClaimClosed';
--AwaitingInvoiceData
update claim set liability_status = 1 where status = 'AwaitingInvoiceData';
--InvoicePaymentLogged
update claim set liability_status = 1 where status = 'InvoicePaymentLogged';
--AwaitingCarHireInfo
update claim set liability_status = 1 where status = 'AwaitingCarHireInfo';
--InvoiceRejectionAccepted
update claim set liability_status = 1 where status = 'InvoiceRejectionAccepted';
--InvoiceEscalated
update claim set liability_status = 1 where status = 'InvoiceEscalated';
--ClaimPending
update claim set liability_status = 3 where status = 'ClaimPending';
--InvoiceReferredToEngineer
update claim set liability_status = 1 where status = 'InvoiceReferredToEngineer';
--ClaimRejected
update claim set liability_status = 3 where status = 'ClaimRejected' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Out of Scope');
update claim set liability_status = 2 where status = 'ClaimRejected' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Indemnity and Liability Issues');
update claim set liability_status = 2 where status = 'ClaimRejected' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Liability Issues');
update claim set liability_status = 6 where status = 'ClaimRejected' and reason_of_rejection_id = (select id from reason_of_rejection where type = 'Claim' and name = 'Indemnity Issues');
--InvoiceApprovedByBRE
update claim set liability_status = 1 where status = 'InvoiceApprovedByBRE';
--InvoiceEscalatedToHandler
update claim set liability_status = 1 where status = 'InvoiceEscalatedToHandler';
--ContestedInvoiceReferredToCHO
update claim set liability_status = 1 where status = 'ContestedInvoiceReferredToCHO';
--InvoiceDataCalculationIncorrect
update claim set liability_status = 1 where status = 'InvoiceDataCalculationIncorrect';
--ClaimReferredToEngineer
update claim set liability_status = 1 where status = 'ClaimReferredToEngineer';
--ContestedInvoiceReferredToInsurer
update claim set liability_status = 1 where status = 'ContestedInvoiceReferredToInsurer';
--AwaitingInvoicePayment
update claim set liability_status = 1 where status = 'AwaitingInvoicePayment';

