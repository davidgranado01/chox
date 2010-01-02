package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.EngineerReport;

public interface EngineerReportService {

    public void saveEngineerReportForXMLUploader(final ClaimResult claimResult);

    public EngineerReport getClaimByCHOReferenceNumber(String sClaimReferenceNumber);

    public EngineerReport getEngineerReport(int engineerReportId);

    public void saveEngineerReport(EngineerReport engineerReport);
}
