package idas.chox.core.services;

import idas.chox.core.xmlValidation.BordereauResult;
import java.io.File;

public interface UploadClaimXMLService {

    public BordereauResult processClaimXMLFile(File file, String fileName);

    public BordereauResult processBordereau(File file, String fileName);
}
