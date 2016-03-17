--
-- bug#3089 - Production - additional BRE rule Storage and Recovery
--
ALTER TABLE bre_band add column storage_recovery_net_ceiling_check boolean not null default false;
ALTER TABLE bre_band add column storage_recovery_net_ceiling numeric(6,2) not null default 0.0;
