--
-- CHOX-148: Visibility of Notes
--
ALTER TABLE comment ADD COLUMN review_required boolean;
ALTER TABLE comment ADD COLUMN task_id integer;
