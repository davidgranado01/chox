package idas.chox.core.services;

import idas.chox.core.xmlValidation.ClaimResult;
import idas.chox.core.model.EngineerReport;

public interface EngineerReportService {

    void saveEngineerReportForXMLUploader(final ClaimResult claimResult);

    EngineerReport getClaimByCHOReferenceNumber(String sClaimReferenceNumber);

    EngineerReport getEngineerReport(int engineerReportId);

    void saveEngineerReport(EngineerReport engineerReport);
}
