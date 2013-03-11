package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.BreBandOrganisation;

public interface BreBandOrganisationService {

    boolean isBreBandOccupied(int bandId);

    BreBandOrganisation getBreBandOrganisation(int breBandOrganisationId);

    void saveBreBandOrganisation(BreBandOrganisation breBandOrganisation);

    boolean deleteBreBandOrganisationByBandId(int bandId);

    void deleteBreBandOrganisationByChorganisationId(int chorganisationId, int insurerId);

    void deleteBreBandOrganisation(BreBandOrganisation breBandOrganisation);

    List<BreBandOrganisation> getBreBandChorganisationsByBreBandId(int bandId);

    List<BreBandOrganisation> getBreBandChorganisationsByChoOrgId(int choOrgid);

}
