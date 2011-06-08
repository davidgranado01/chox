package idas.chox.core.services;

import idas.chox.core.model.Claim;
import idas.chox.core.xmlValidation.ClaimResult;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;

public interface UploadClaimXMLService {

   // public BordereauResult processClaimXMLFile(File file, String fileName);

    public List<ClaimResult> formClaimResults(Document document)throws Exception;

    public boolean doProcessBordereauResult(ClaimResult claimResult, List<String> choReferences);

    public void evictClaim(Claim claim);

    public boolean validateFile(File uploadedFile);

    public boolean processFile(int bordereauId, Map session);
    
    public String getSuccessMessage();
    
    public String getErrorMessage();
    
    public boolean saveUploadedFile(File uploadedFile, String uploadedFileFileName);
}
