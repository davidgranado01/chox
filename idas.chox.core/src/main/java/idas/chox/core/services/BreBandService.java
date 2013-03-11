package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Insurer;

public interface BreBandService {

    BreBand getBreBand(int breBandId);

    void saveBreBand(BreBand breBand);

    List<BreBand> getInsurerBreBandsByInsurer(int insurerId);

    BreBand getBreBand(int orgId, int insurerId);

    boolean isBreBandOccupied(BreBand breBand);

    boolean isBreBandNameExist(BreBand breBand);

    void createDefaultRecord(Insurer insurer);

    void deleteBreBand(BreBand breBand);

    boolean isSupplierRatesActivated(int orgId, int insurerId);
}
