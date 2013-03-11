package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ChorganisationAlias;

public interface ChorganisationAliasService {

    ChorganisationAlias getChorganisationByAliasName(String aliasName);

    List<ChorganisationAlias> getChorganisationAliasesByChorganisation(int chorganisationId);

    ChorganisationAlias getChorganisationAlias(int chorganisationAliasId);

    void deleteChorganisationAlias(ChorganisationAlias chorganisationAlias);

    void saveChorganisationAlias(ChorganisationAlias chorganisationAlias);

    boolean isChorganisationAliasExist(int chorganisationId, String aliasName);

    void createDefaultRecord(Chorganisation chorganisation);
}
