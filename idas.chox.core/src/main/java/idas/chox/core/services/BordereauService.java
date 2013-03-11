package idas.chox.core.services;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.WebUser;
import idas.chox.core.search.SearchResult;

public interface BordereauService {

    void saveBordereau(Bordereau bordereau);

    Bordereau getBordereauByFileName(String fileName);

    Bordereau getBordereauById(int bordereauId);

    boolean deleteBordereau(Bordereau bordereau);

    SearchResult getUploadedFiles(WebUser webUser,int defaultDays,String sort,String dir, int start, int limit);

}
