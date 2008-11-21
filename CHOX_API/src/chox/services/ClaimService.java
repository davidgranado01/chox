package chox.services;

import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import chox.model.Claim;

public interface ClaimService {
    
    public List listAllClaims();
    public List listClaimsByStatus(String status);
    public Long getCountByStatus(String status);
    public ArrayList<XMLParseResult> processClaimXMLFile(File claimXMLFile, String sUpdateType, Boolean isAllowPartialUpload);
    public XMLParseResult saveClaimForXMLUploader(XMLParseResult xmlParseResult);
    
    // CARLSON
    public Boolean isClaimReferenceNumberExist(String sClaimReferenceNumber);
    public Claim getClaimByCHOReferenceNumber(String sClaimReferenceNumber);
}
