package idas.chox.core.services;

import java.math.BigDecimal;
import java.util.List;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingBand;
import idas.chox.core.model.ClaimType;

/**
 *
 * @author john
 */
public interface ClaimMatchingBandService {
    List<ClaimMatchingBand> getClaimMatchingBands(int breBandId);

    void saveClaimMatchingBand(ClaimMatchingBand claimMatchingBand);

    void deleteClaimMatchingBand(ClaimMatchingBand claimMatchingBand);

    ClaimMatchingBand getClaimMatchingBand(int claimMatchingBandId);
        
    ClaimMatchingBand getClaimMatchingBand(int breBandId, ClaimType claimType, String vehicleClass, BigDecimal liabilityPercentage);

}
