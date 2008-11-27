package chox.services;

import chox.data.ClaimSearchCriteria;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import chox.model.*;

public interface ClaimService {
    
    public Claim getClaim(int id);
    public List listAllClaims();
    public List listClaimsByStatus(String status);
    public Long getCountByStatus(String status);
    public List searchClaims(ClaimSearchCriteria searchCriteria);
    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, Boolean isAllowPartialUpload);
    public XMLParseResult saveClaimForXMLUploader(XMLParseResult xmlParseResult);
    public void updateClaim(Claim claim);
    public void updateIncident(Incident incident);
    // CARLSON
    public Boolean isClaimReferenceNumberExist(String sClaimReferenceNumber);
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);
}
