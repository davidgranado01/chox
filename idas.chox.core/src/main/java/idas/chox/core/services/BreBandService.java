package idas.chox.core.services;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.Insurer;
import java.util.List;

public interface BreBandService {

    public BreBand getBreBand(int breBandId);

    public void saveBreBand(BreBand breBand);

    public List<BreBand> getInsurerBreBandsByInsurer(int insurerId);

    BreBand getBreBand(int orgId, int insurerId);

    public boolean isBreBandOccupied(BreBand breBand);

    public boolean isBreBandNameExist(BreBand breBand);

    public void createDefaultRecord(Insurer insurer);

    public void deleteBreBand(BreBand breBand);

    public boolean isSupplierRatesActivated(int orgId, int insurerId);
}
