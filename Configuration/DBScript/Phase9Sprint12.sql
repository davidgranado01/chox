--
-- CHOX-482: New BRE Rule - Impecunious Check
--
ALTER TABLE bre_band ADD COLUMN impecunious_check boolean NOT NULL DEFAULT false;
ALTER TABLE bre_band ADD COLUMN impecunious_start_date timestamp without time zone;
--
-- End of CHOX-482
--
