package idas.chox.core.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import java.util.List;

public interface InsurerAliasService {

    public InsurerAlias getInsurerByAliasName(String aliasName);

    public List<InsurerAlias> getInsurerAliasesByInsurer(int insurerId);

    public InsurerAlias getInsurerAlias(int insurerAliasId);

    public void deleteInsurerAlias(InsurerAlias insurerAlias);

    public void saveInsurerAlias(InsurerAlias insurerAlias);

    public boolean isInsurerAliasExist(int insurerId, String AliasName);

    public void createDefaultRecord(Insurer insurer);
}
