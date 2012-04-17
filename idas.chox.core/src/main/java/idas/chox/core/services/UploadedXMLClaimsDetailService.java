package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.UploadedXMLClaimsDetail;

/**
 *
 * @author seeni
 */
public interface UploadedXMLClaimsDetailService {

    public List<UploadedXMLClaimsDetail> getUploadedXMLClaimsDetailByBordereauId(int bordereauId);

    public void saveUploadedXMLClaimsDetail(UploadedXMLClaimsDetail claimsDetail);
    
    public void saveUploadedXMLClaimsDetails(List<UploadedXMLClaimsDetail> objects);
}
