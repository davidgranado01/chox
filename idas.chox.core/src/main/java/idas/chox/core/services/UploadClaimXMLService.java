package idas.chox.core.services;

import idas.chox.core.xmlValidation.BordereauResult;
import idas.chox.core.xmlValidation.ClaimResult;
import java.io.File;
import java.util.List;
import org.w3c.dom.Document;

public interface UploadClaimXMLService {

   // public BordereauResult processClaimXMLFile(File file, String fileName);

    public List<ClaimResult> formClaimResults(Document document)throws Exception;

    public void doProcessBordereauResult(ClaimResult claimResult);
}
