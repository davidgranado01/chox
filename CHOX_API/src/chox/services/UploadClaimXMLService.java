/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.XMLParseResult;
import chox.xmlValidation.result.ParseResult;
import java.io.File;
import java.util.ArrayList;

public interface UploadClaimXMLService {
    public ArrayList<XMLParseResult> processXML(File claimXMLFile, Boolean isAllowPartialUpload);
    public ParseResult processClaimXMLFile(File file, String fileName);
}
