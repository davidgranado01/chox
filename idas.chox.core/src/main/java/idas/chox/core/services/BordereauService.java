package idas.chox.core.services;

import idas.chox.core.model.Bordereau;

public interface BordereauService {

    public void saveBordereau(Bordereau bordereau);

    public Bordereau getBordereauByFileName(String fileName);
}
