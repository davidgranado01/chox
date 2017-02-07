package idas.chox.core.services;

import java.util.List;
import java.util.Date;

import idas.chox.core.model.BrePenaltyBand;
import idas.chox.core.model.Claim;

/**
 *
 * @author John
 */
public interface BrePenaltyBandService {

    List<BrePenaltyBand> getBrePenaltyBands(int breBandId);

    void saveBrePenaltyBand(BrePenaltyBand brePenaltyBand);

    void deleteBrePenaltyBand(BrePenaltyBand brePenaltyBand);

    BrePenaltyBand getBrePenaltyBand(int brePenaltyBandId);
        
    BrePenaltyBand getBrePenaltyBand(Claim claim, Date startDate);
}
