package chox.services;

import chox.model.ChoBandOrganisation;
import java.util.List;

public interface ChoBandOrganisationService{
    public boolean isChoBandOccupied(int bandId);
    public ChoBandOrganisation getObject(int id);
    public void updateObject(ChoBandOrganisation object);
    public boolean deleteChoBandOrganisationByBandId(int bandId);
    public boolean deleteChoBandOrganisationByChorganisationId(int chorganisationId, int insurerId);
    public void deleteObject(ChoBandOrganisation object);
    public List<ChoBandOrganisation> getChoBandChorganisationsByChoBandId(int bandId);
    public List<ChoBandOrganisation> getChoBandChorganisationsByChoOrgId(int choOrgid);
    public boolean isActiveChorganisationWithBand(int choOrgid, int insOrgId);
    public boolean isActiveChorganisationWithBand(int choOrgid);
}
