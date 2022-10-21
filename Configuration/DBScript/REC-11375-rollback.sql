DO $pendingRollback$
BEGIN

DELETE from accessibility_item where role='ROLE_CHO_MNG' and accessibility_id in (select id from accessibility where name like 'extraAction.updateSupplierReferenceNumber.%');

DELETE from accessibility_item where role='ROLE_CHO_MNG' and accessibility_id in (select id from accessibility where name like 'activity.UpdateSupplierReference.%');

END $pendingRollback$;