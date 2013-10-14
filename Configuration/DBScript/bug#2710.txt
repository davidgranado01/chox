ALTER TABLE invoice DROP COLUMN acquisition_fee;
ALTER TABLE invoice DROP COLUMN acquisition_qty;
ALTER TABLE invoice DROP COLUMN overhead_fee;
ALTER TABLE invoice DROP COLUMN overhead_qty;

ALTER TABLE invoice ADD COLUMN collaboration_fee numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice ADD COLUMN collaboration_qty smallint NOT NULL DEFAULT 0;

ALTER TABLE invoice_original DROP COLUMN acquisition_fee;
ALTER TABLE invoice_original DROP COLUMN acquisition_qty;
ALTER TABLE invoice_original DROP COLUMN overhead_fee;
ALTER TABLE invoice_original DROP COLUMN overhead_qty;

ALTER TABLE invoice_original ADD COLUMN collaboration_fee numeric(10, 2) NOT NULL DEFAULT 0.00;
ALTER TABLE invoice_original ADD COLUMN collaboration_qty smallint NOT NULL DEFAULT 0;
