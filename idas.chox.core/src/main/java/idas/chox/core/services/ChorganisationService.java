package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ChorganisationAlias;
import idas.chox.core.security.SecurityInfoProvider;

public interface ChorganisationService {

    Chorganisation getChorganisation(int chorganisationId);

    List<Chorganisation> getChorganisations(String order);
    List<Chorganisation> getNonManualChorganisations(String order);

    Chorganisation updateChorganisation(Chorganisation chorganisation);

    List<Chorganisation> getAvailableChorganisationsByInsurer(int insurerId);

    List<Chorganisation> getActiveChorganisationsByInsurerWithoutBreBand(int insurerId);

    boolean isActiveChorganisationsByInsurerCreditHireWithBreBand(int insurerId, int chorganisationId);

    boolean isCreditHireWithBreBand(int chorganisationId);

    List<Chorganisation> getActiveChorganisation();

    boolean isChorgNameExist(String chorganisationName);

    void setSecurityInfoProvider(SecurityInfoProvider securityInfoProvider);

    SecurityInfoProvider getSecurityInfoProvider();

    ChorganisationAlias getChoAliasName(String aliasName);

    Chorganisation getChorgByName(String choName);
    
    int getClaimCount(int choId, String supplierRef);
}

