package idas.chox.core.services;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ChorganisationAlias;
import idas.chox.core.security.SecurityInfoProvider;
import java.util.List;

public interface ChorganisationService {

    public Chorganisation getChorganisation(int chorganisationId);

    public List<Chorganisation> getChorganisations(String order);

    public Chorganisation updateChorganisation(Chorganisation chorganisation);

    public List<Chorganisation> getAvailableChorganisationsByInsurer(int insurerId);

    public List<Chorganisation> getActiveChorganisationsByInsurerWithoutBreBand(int insurerId);

    public boolean isActiveChorganisationsByInsurerCreditHireWithBreBand(int insurerId, int chorganisationId);

    public boolean isCreditHireWithBreBand(int chorganisationId);

    public List<Chorganisation> getActiveChorganisation();

    public boolean isChorgNameExist(String chorganisationName);

    public void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider);

    public SecurityInfoProvider getSecurityInfoProvider();

    ChorganisationAlias getChoAliasName(String aliasName);

    Chorganisation getChorgByName(String choName);
}

