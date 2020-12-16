--------------------------------------------------------------------------------
-- 8.5.1 Assign invoice saving to a failed Business Rule
--------------------------------------------------------------------------------
ALTER TABLE bre_band ADD COLUMN bre_invoice_saving_active boolean DEFAULT false NOT NULL;
ALTER TABLE invoice ADD COLUMN invoice_saving_rule character varying(3);
ALTER TABLE invoice_original ADD COLUMN invoice_saving_rule character varying(3);

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.InvoiceSaving.ContestedInvoiceReferredToInsurer',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
   SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ContestedInvoiceReferredToInsurer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_MNG',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ContestedInvoiceReferredToInsurer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CH',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ContestedInvoiceReferredToInsurer';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ContestedInvoiceReferredToInsurer';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check)
    VALUES ('activity.InvoiceSaving.InvoiceReferredToClaimsHandler',FALSE,FALSE);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.InvoiceSaving.InvoiceReferredToClaimsHandler';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_MNG',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.InvoiceReferredToClaimsHandler';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CH',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.InvoiceReferredToClaimsHandler';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.InvoiceReferredToClaimsHandler';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, claim_type)
    VALUES ('activity.InvoiceSaving.ManualInvoiceBRERejected',FALSE,FALSE, 17);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
   SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ManualInvoiceBRERejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_MNG',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ManualInvoiceBRERejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CH',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ManualInvoiceBRERejected';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ManualInvoiceBRERejected';

INSERT INTO accessibility(name,is_workgroup_check,is_ownership_check, claim_type)
    VALUES ('activity.InvoiceSaving.ManualInvoiceContested',FALSE,FALSE, 17);
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ALL',0, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ManualInvoiceContested';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_MNG',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ManualInvoiceContested';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_INS_CH',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ManualInvoiceContested';
INSERT INTO accessibility_item (role,access_right,accessibility_id)
    SELECT 'ROLE_CHOX_ADMIN',1, id FROM accessibility WHERE name = 'activity.InvoiceSaving.ManualInvoiceContested';

----------------------
-- End of 8.5.1
----------------------
