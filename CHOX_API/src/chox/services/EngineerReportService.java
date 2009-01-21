/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.services;

import chox.model.EngineerReport;
import chox.model.XMLParseResult;

public interface EngineerReportService {

    XMLParseResult saveEngineerReportForXMLUploader(XMLParseResult xmlParseResult);
    
    public EngineerReport getClaimByCHOReferenceNumber(String sClaimReferenceNumber);

    public EngineerReport getObject(int id);

    public void updateObject(EngineerReport engineerReport);
}
