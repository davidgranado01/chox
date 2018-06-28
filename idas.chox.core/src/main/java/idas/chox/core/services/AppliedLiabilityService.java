package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.BreAppliedLiability;
import idas.chox.core.model.ClaimType;

/**
 *
 * @author john
 */
public interface AppliedLiabilityService {
    List<BreAppliedLiability> getAppliedLiabilities(int breBandId);

    void saveAppliedLiability(BreAppliedLiability appliedLiability);

    void deleteAppliedLiability(BreAppliedLiability appliedLiability);

    BreAppliedLiability getAppliedLiability(int appliedLiabilityId);
        
    BreAppliedLiability getAppliedLiability(int breBandId, ClaimType claimType);
}
