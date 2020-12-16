--
-- CHOX-250: Add new field within Admin/Insurer/Details/Workflow Parameters config
--
ALTER TABLE insurer ADD COLUMN allow_default_hm_updates boolean not null default false;

--
-- CHOX-253: Add new field within Admin/Insurer/Details/Workflow Parameters config
--
ALTER TABLE insurer ADD COLUMN enable_manual_lou_dates boolean not null default false;

--
-- CHOX-277: Claim Summary
--
ALTER TABLE claim ADD COLUMN indemnity_stance character varying;

--
-- CHOX-255: Replicate the Insurer Hire Monitoring Details tab on Manual claims
--
INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ManualInvoiceUnassigned', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceUnassigned';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceUnassigned';


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ManualInvoiceContested', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceContested';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceContested';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceContested';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceContested';

INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ManualInvoiceBRERejected', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceBRERejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceBRERejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceBRERejected';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceBRERejected';


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ManualInvoiceBREApproved', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_CH', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceBREApproved';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_MNG', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceBREApproved';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS_SUP', 2, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceBREApproved';
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoiceBREApproved';


INSERT INTO accessibility(name, is_workgroup_check, is_ownership_check, check_workgroup_enabled,
                        check_claimownership_enabled, check_fnol_enabled, check_engineer_enabled,
                        check_supplier_ownership)
    SELECT 'tab.InsurerHireMonitoring.ManualInvoicePaid', false, false, false, false, false, false, false;
INSERT INTO accessibility_item(role, access_right, accessibility_id)
    SELECT 'ROLE_INS', 1, id FROM accessibility WHERE name ='tab.InsurerHireMonitoring.ManualInvoicePaid';
