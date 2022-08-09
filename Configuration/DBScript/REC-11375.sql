--REC-11375 : Adding the access to CHO Managers to Update Suppier Reference
INSERT INTO accessibility_item(accessibility_id, role, access_right)
SELECT id, 'ROLE_CHO_MNG', 2 FROM accessibility WHERE name like 'extraAction.updateSupplierReferenceNumber.%';

INSERT INTO accessibility_item(accessibility_id, role, access_right)
SELECT id, 'ROLE_CHO_MNG', 2 FROM accessibility WHERE name like 'activity.UpdateSupplierReference.%';
