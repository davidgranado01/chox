/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.XMLParseResult;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Emmanuel
 */
public interface UploadClaimXMLService {
    
    public ArrayList<XMLParseResult> processXML(File claimXMLFile, Boolean isAllowPartialUpload);

}
