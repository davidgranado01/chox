--------------------------------------------------------------------------------
-- 8.2.2 Merge Queues and Inbox tabs
--------------------------------------------------------------------------------

INSERT INTO accessibility  (name,is_workgroup_check,is_ownership_check) VALUES ('filter.AllClaims',false,false);
INSERT INTO accessibility_item (role,access_right,accessibility_id) SELECT 'ALL',1, id FROM accessibility WHERE name = 'filter.AllClaims';

----------------------
-- End of 8.2.2
----------------------

