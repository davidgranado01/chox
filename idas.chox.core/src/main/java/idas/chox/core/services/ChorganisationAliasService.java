package idas.chox.core.services;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ChorganisationAlias;
import java.util.List;

public interface ChorganisationAliasService {

    public ChorganisationAlias getChorganisationByAliasName(String aliasName);

    public List<ChorganisationAlias> getChorganisationAliasesByChorganisation(int chorganisationId);

    public ChorganisationAlias getChorganisationAlias(int chorganisationAliasId);

    public void deleteChorganisationAlias(ChorganisationAlias chorganisationAlias);

    public void saveChorganisationAlias(ChorganisationAlias chorganisationAlias);

    public boolean isChorganisationAliasExist(int chorganisationId, String aliasName);

    public void createDefaultRecord(Chorganisation chorganisation);
}
