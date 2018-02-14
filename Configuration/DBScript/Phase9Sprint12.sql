--
-- CHOX-482: New BRE Rule - Impecunious Check
--
ALTER TABLE bre_band ADD COLUMN impecunious_check boolean NOT NULL DEFAULT false;
ALTER TABLE bre_band ADD COLUMN impecunious_start_date timestamp without time zone;
--
-- End of CHOX-482
--

--
-- CHOX-469: New Extras field required: VED Charge
--
ALTER TABLE invoice ADD COLUMN ved_fee numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice ADD COLUMN ved_qty smallint NOT NULL DEFAULT 0;
ALTER TABLE invoice_original ADD COLUMN ved_fee numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice_original ADD COLUMN ved_qty smallint NOT NULL DEFAULT 0;
--
-- End of CHOX-469
--
