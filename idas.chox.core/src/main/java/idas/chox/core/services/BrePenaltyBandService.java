package idas.chox.core.services;

import java.util.List;
import idas.chox.core.model.BrePenaltyBand;

/**
 *
 * @author John
 */
public interface BrePenaltyBandService {

    List<BrePenaltyBand> getBrePenaltyBands(int breBandId);

    void saveBrePenaltyBand(BrePenaltyBand brePenaltyBand);

    void deleteBrePenaltyBand(BrePenaltyBand brePenaltyBand);

    BrePenaltyBand getBrePenaltyBand(int brePenaltyBandId);

}
