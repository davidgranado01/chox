/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emmanuel
 */
public interface ClaimService {
    
    public List listAllClaims();
    public List listClaimsByStatus(String status);
    public Long getCountByStatus(String status);
    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, String sUpdateType, Boolean isAllowPartialUpload);

}
