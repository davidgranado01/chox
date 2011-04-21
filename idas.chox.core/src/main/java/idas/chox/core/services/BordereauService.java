package idas.chox.core.services;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.model.WebUser;
import idas.chox.core.search.SearchResult;

public interface BordereauService {

    public void saveBordereau(Bordereau bordereau);

    public Bordereau getBordereauByFileName(String fileName);

    //public List<Bordereau> getBordereauByUserIdUploadedToday(WebUser webUser);

    public Bordereau getBordereauById(int bordereauId);

    public boolean deleteBordereau(Bordereau bordereau);

    public SearchResult getUploadedFiles(WebUser webUser,int defaultDays,String sort,String dir, int start, int limit);

    public UploadedXMLClaimsDetail getUploadedClaimDetailsBySupplierReference(String supplierReference);
}
