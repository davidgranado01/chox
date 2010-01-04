package idas.chox.core.services;

import idas.chox.core.model.BreBandOrganisation;
import java.util.List;

public interface BreBandOrganisationService {

    public boolean isBreBandOccupied(int bandId);

    public BreBandOrganisation getBreBandOrganisation(int breBandOrganisationId);

    public void saveBreBandOrganisation(BreBandOrganisation breBandOrganisation);

    public boolean deleteBreBandOrganisationByBandId(int bandId);

    public void deleteBreBandOrganisationByChorganisationId(int chorganisationId, int insurerId);

    public void deleteBreBandOrganisation(BreBandOrganisation breBandOrganisation);

    public List<BreBandOrganisation> getBreBandChorganisationsByBreBandId(int bandId);

    public List<BreBandOrganisation> getBreBandChorganisationsByChoOrgId(int choOrgid);

    public boolean isActiveChorganisationWithBand(int choOrgid, int insOrgId);

    public boolean isActiveChorganisationWithBand(int choOrgid);
}
