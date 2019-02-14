package idas.chox.core.services;

import java.util.Date;
import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingEntry;

/**
 *
 * @author john
 */
public interface ClaimMatchingService extends DataService {
    List<ClaimMatchingEntry> getClaimMatchingEntries(String insurerName);
    Claim getClaimMatch(Date incidentDate, String thirdPartyVehicleRehistration);
    ClaimMatchingEntry getClaimMatchingEntry(Date incidentDate, String thirdPartyVehicleRehistration);
    ClaimMatchingEntry getClaimMatchingEntry(String insurerNumber);
    void save(ClaimMatchingEntry entry);
}
