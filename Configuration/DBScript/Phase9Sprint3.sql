--
-- CHOX-148: Visibility of Notes
--
ALTER TABLE comment ADD COLUMN review_required boolean;
ALTER TABLE comment ADD COLUMN task_id integer;


--
-- CHOX-152: Usable question for CHO/Insurer upload
--
ALTER TABLE customer ALTER COLUMN is_usable DROP NOT NULL;
ALTER TABLE customer ALTER COLUMN is_usable DROP default;
