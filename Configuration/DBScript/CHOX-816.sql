ALTER TABLE bre_band
    ADD COLUMN IF NOT EXISTS delivery_collection_charge_check boolean not null default false,
    ADD COLUMN IF NOT EXISTS repair_charge_check boolean not null default false;
