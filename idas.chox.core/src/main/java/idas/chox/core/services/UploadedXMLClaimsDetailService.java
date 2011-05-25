/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.services;

import idas.chox.core.model.Bordereau;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import java.util.List;

/**
 *
 * @author seeni
 */
public interface UploadedXMLClaimsDetailService {

    public List<UploadedXMLClaimsDetail> getUploadedXMLClaimsDetailByBordereauId(int bordereauId);

    public void saveUploadedXMLClaimsDetail(UploadedXMLClaimsDetail claimsDetail);
}
