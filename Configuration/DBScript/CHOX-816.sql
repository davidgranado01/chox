ALTER TABLE bre_band
    ADD COLUMN delivery_collection_charge_check boolean not null default false,
    ADD COLUMN repair_charge_check boolean not null default false;
