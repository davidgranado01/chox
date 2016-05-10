--
-- bug#3090 - CHOX Admin to update Supplier Reference
--
INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimPending',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimPending';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.AwaitingLitigationOutcome',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.AwaitingLitigationOutcome';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoiceDataCalculationIncorrect',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoiceDataCalculationIncorrect';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ContestedInvoiceReferredToCHO',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ContestedInvoiceReferredToCHO';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimReferredToEngineer';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.AwaitingCarHireInfo',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.AwaitingCarHireInfo';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.AwaitingInvoicePayment',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.AwaitingInvoicePayment';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimUnacknowledgedRouted',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimUnacknowledgedRouted';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.SubscriberClaimRejected',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.SubscriberClaimRejected';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimRejectionContested',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimRejectionContested';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoiceReferredToEngineer',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoiceReferredToEngineer';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimUnacknowledgedUnassigned',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimUnacknowledgedUnassigned';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimRejectionAccepted';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ContestedInvoiceReferredToInsurer',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ContestedInvoiceReferredToInsurer';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.AwaitingLiabilityResolution',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.AwaitingLiabilityResolution';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimUnacknowledgedUnrouted',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimUnacknowledgedUnrouted';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimUpdatedByEngineer',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimUpdatedByEngineer';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimClosed',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimClosed';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoiceApprovedByBRE',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoiceApprovedByBRE';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoiceReferredToClaimsHandler',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoiceReferredToClaimsHandler';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoicePaymentLogged',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoicePaymentLogged';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ManualInvoicePaid',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ManualInvoicePaid';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ManualInvoiceBREApproved',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ManualInvoiceBREApproved';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoiceEscalated',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoiceEscalated';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoiceRejectionAccepted',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoiceRejectionAccepted';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoiceUnassigned';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.PaymentReceived',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.PaymentReceived';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ManualInvoiceBRERejected',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ManualInvoiceBRERejected';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimRejected',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimRejected';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.AwaitingInvoiceData',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.AwaitingInvoiceData';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ManualInvoiceUnassigned',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ManualInvoiceUnassigned';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ManualInvoiceContested',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ManualInvoiceContested';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.InvoiceEscalatedToHandler',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.InvoiceEscalatedToHandler';

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) values ('extraAction.updateSupplierReferenceNumber.ClaimReferredToFNOL',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ROLE_CHOX_ADMIN',2, id FROM accessibility WHERE name = 'extraAction.updateSupplierReferenceNumber.ClaimReferredToFNOL';
