CREATE OR REPLACE FUNCTION updateRemainingSlaDays()
  RETURNS boolean AS
$BODY$

BEGIN

-- First, set all to null
update claim
  set remaining_sla_days = null, remaining_sla_days_str = null
where remaining_sla_days is not null or remaining_sla_days_str is not null;

update claim
  set remaining_sla_days = sla_ext_days + bre.subscriber_sla_days - get_days_in_status(claim.id, '{"ClaimUnacknowledgedUnassigned","ClaimUnacknowledgedUnrouted","ClaimUnacknowledgedRouted",
                    "ClaimPending","ClaimReferredToFNOL","ClaimReferredToEngineer","ClaimUpdatedByEngineer","ClaimRejectionContested"}', bre.pause_subscriber_sla_clock)
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = claim.insurer_id
  AND bre.subscriber_sla_days != 0
  AND claim.claim_type IN (7,8,9)
  AND claim.status in ('ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted',
        'ClaimPending', 'ClaimReferredToFNOL', 'ClaimReferredToEngineer', 'ClaimUpdatedByEngineer',
        'ClaimRejectionContested', 'ClaimRejected', 'SubscriberClaimRejected');

update claim
  set remaining_sla_days = sla_ext_days + bre.fixedfee_sla_days - get_days_in_status(claim.id, '{"ClaimUnacknowledgedUnassigned","ClaimUnacknowledgedUnrouted","ClaimUnacknowledgedRouted",
                    "ClaimPending","ClaimReferredToFNOL","ClaimReferredToEngineer","ClaimUpdatedByEngineer","ClaimRejectionContested"}', bre.pause_fixedfee_sla_clock)
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.fixedfee_sla_days != 0
  AND bre.insurer_id = claim.insurer_id
  AND claim.claim_type IN (11,12,13)
  AND claim.status in ('ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted',
        'ClaimPending', 'ClaimReferredToFNOL', 'ClaimReferredToEngineer', 'ClaimUpdatedByEngineer',
        'ClaimRejectionContested', 'ClaimRejected');

update claim
  set remaining_sla_days_str = remaining_sla_days::varchar(5)
where remaining_sla_days is not null;

update claim
  set remaining_sla_days_str = '0'
where remaining_sla_days < 0;

update claim
  set remaining_sla_days_str = '-'
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = claim.insurer_id
  AND bre.subscriber_sla_days = 0
  AND claim.claim_type IN (7,8,9)
  AND claim.status in ('ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted',
        'ClaimPending', 'ClaimReferredToFNOL', 'ClaimReferredToEngineer', 'ClaimUpdatedByEngineer',
        'ClaimRejectionContested', 'ClaimRejected', 'SubscriberClaimRejected');

update claim
  set remaining_sla_days_str = '-'
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.fixedfee_sla_days = 0
  AND bre.insurer_id = claim.insurer_id
  AND claim.claim_type IN (11,12,13)
  AND claim.status in ('ClaimUnacknowledgedUnassigned', 'ClaimUnacknowledgedUnrouted', 'ClaimUnacknowledgedRouted',
        'ClaimPending', 'ClaimReferredToFNOL', 'ClaimReferredToEngineer', 'ClaimUpdatedByEngineer',
        'ClaimRejectionContested', 'ClaimRejected');

update claim
  set remaining_sla_days_str = bre.subscriber_time_cut_off
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = claim.insurer_id
  AND claim.claim_type IN (7,8,9)
  AND remaining_sla_days = 0;

update claim
  set remaining_sla_days_str = bre.fixedfee_time_cut_off
from bre_band bre,
     bre_band_organisation bbo
where bbo.chorganisation_id = claim.chorganisation_id
  AND bbo.band_id = bre.id
  AND bre.insurer_id = claim.insurer_id
  AND claim.claim_type IN (11,12,13)
  AND remaining_sla_days = 0;

return true;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

GRANT EXECUTE ON FUNCTION updateRemainingSlaDays() TO chox_user;
