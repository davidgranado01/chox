/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.EngineerReport;

public interface EngineerReportService {

    public void saveObjectForXMLUploader(final ClaimResult claimResult);

    public EngineerReport getClaimByCHOReferenceNumber(String sClaimReferenceNumber);

    public EngineerReport getObject(int id);

    public void updateObject(EngineerReport engineerReport);
}
