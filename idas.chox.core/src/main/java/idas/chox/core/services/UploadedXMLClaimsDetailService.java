package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.UploadedXMLClaimsDetail;

/**
 *
 * @author seeni
 */
public interface UploadedXMLClaimsDetailService {

    List<UploadedXMLClaimsDetail> getUploadedXMLClaimsDetailByBordereauId(int bordereauId);

    void saveUploadedXMLClaimsDetail(UploadedXMLClaimsDetail claimsDetail);
    
    void saveUploadedXMLClaimsDetails(List<UploadedXMLClaimsDetail> objects);
}
