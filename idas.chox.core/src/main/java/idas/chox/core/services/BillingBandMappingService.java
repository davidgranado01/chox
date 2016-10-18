package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.ChoBillingBandMapping;
import idas.chox.core.model.InsurerBillingBandMapping;

/**
 *
 * @author john
 */
public interface BillingBandMappingService {
    List<InsurerBillingBandMapping> getInsurerBillingBandMappings(int insurerId, int bandId);
    List<InsurerBillingBandMapping> getInsurerBillingBandMappings(int insurerId);
    List<InsurerBillingBandMapping> getAvailableInsurerBillingBandMappings(int insurerId);
    List<ChoBillingBandMapping> getChoBillingBandMappings(int choId, int bandId);
    List<ChoBillingBandMapping> getChoBillingBandMappings(int choId);
    List<ChoBillingBandMapping> getAvailableChoBillingBandMappings(int choId);
}
