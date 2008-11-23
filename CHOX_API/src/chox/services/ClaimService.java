package chox.services;

import chox.data.ClaimSearchCriteria;
import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import chox.model.Claim;

public interface ClaimService {
    
    public Claim getClaim(int id);
    public List listAllClaims();
    public List listClaimsByStatus(String status);
    public Long getCountByStatus(String status);
    public List searchClaims(ClaimSearchCriteria searchCriteria);
    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, Boolean isAllowPartialUpload);
    public XMLParseResult saveClaimForXMLUploader(XMLParseResult xmlParseResult);
    
    // CARLSON
    public Boolean isClaimReferenceNumberExist(String sClaimReferenceNumber);
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);
}
