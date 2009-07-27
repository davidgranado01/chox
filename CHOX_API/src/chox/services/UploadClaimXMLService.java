/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.xmlValidation.model.BordereauResult;
import java.io.File;

public interface UploadClaimXMLService {
    public BordereauResult processClaimXMLFile(File file, String fileName);
}
