package idas.chox.core.services;

import idas.chox.core.model.UploadedXMLClaimsDetail;
import java.util.List;

/**
 *
 * @author seeni
 */
public interface UploadedXMLClaimsDetailService {

    public List<UploadedXMLClaimsDetail> getUploadedXMLClaimsDetailByBordereauId(int bordereauId);

    public void saveUploadedXMLClaimsDetail(UploadedXMLClaimsDetail claimsDetail);
    
    public void saveUploadedXMLClaimsDetails(List<? extends Object> objects);
}
