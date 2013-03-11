package idas.chox.core.services;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

import idas.chox.core.model.Claim;
import idas.chox.core.model.UploadedXMLClaimsDetail;
import idas.chox.core.xmlValidation.ClaimResult;

public interface UploadClaimXMLService {

    List<ClaimResult> formClaimResults(Document document)throws Exception;

    boolean doProcessBordereauResult(ClaimResult claimResult, List<String> choReferences);

    void evictClaim(Claim claim);

    boolean validateFile(File uploadedFile);

    boolean processFile(int bordereauId, Map session);
    
    String getSuccessMessage();
    
    String getErrorMessage();
    
    boolean saveUploadedFile(File uploadedFile, String uploadedFileFileName);
    
    UploadedXMLClaimsDetail processWebServiceClaim(InputStream stream);
    
}
