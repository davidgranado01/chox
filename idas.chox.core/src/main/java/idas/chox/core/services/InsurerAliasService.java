package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;

public interface InsurerAliasService {

    InsurerAlias getInsurerByAliasName(String aliasName);

    List<InsurerAlias> getInsurerAliasesByInsurer(int insurerId);

    InsurerAlias getInsurerAlias(int insurerAliasId);

    void deleteInsurerAlias(InsurerAlias insurerAlias);

    void saveInsurerAlias(InsurerAlias insurerAlias);

    boolean isInsurerAliasExist(int insurerId, String AliasName);

    void createDefaultRecord(Insurer insurer);
}
