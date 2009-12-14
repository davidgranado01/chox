package idas.chox.core.services;

import idas.chox.core.model.BreBandOrganisation;
import java.util.List;

public interface BreBandOrganisationService {

    public boolean isBreBandOccupied(int bandId);

    public BreBandOrganisation getObject(int id);

    public void updateObject(BreBandOrganisation object);

    public boolean deleteBreBandOrganisationByBandId(int bandId);

    public boolean deleteBreBandOrganisationByChorganisationId(int chorganisationId, int insurerId);

    public void deleteObject(BreBandOrganisation object);

    public List<BreBandOrganisation> getBreBandChorganisationsByBreBandId(int bandId);

    public List<BreBandOrganisation> getBreBandChorganisationsByChoOrgId(int choOrgid);

    public boolean isActiveChorganisationWithBand(int choOrgid, int insOrgId);

    public boolean isActiveChorganisationWithBand(int choOrgid);
}
