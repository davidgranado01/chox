package idas.chox.core.services;

import java.util.Date;
import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingEntry;
import idas.chox.core.model.ClaimMatchingImportEntry;

/**
 *
 * @author john
 */
public interface ClaimMatchingService extends DataService {
    List<ClaimMatchingImportEntry> getClaimMatchingImportEntries(String insurerName);
    Claim getClaimMatch(Date incidentDate, String thirdPartyVehicleRehistration);
    ClaimMatchingEntry getClaimMatchingEntry(Date incidentDate, String thirdPartyVehicleRehistration);
    ClaimMatchingEntry getClaimMatchingEntry(String insurerNumber);
    void save(ClaimMatchingEntry entry);
    void delete(ClaimMatchingImportEntry entry);
}
