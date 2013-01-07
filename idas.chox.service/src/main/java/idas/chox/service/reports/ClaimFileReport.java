package idas.chox.service.reports;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import idas.chox.core.services.ReportDataService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.ClaimFileBreData;
import idas.chox.service.reports.viewdata.ClaimFileEcdData;
import idas.chox.service.reports.viewdata.ClaimFileNoteData;
import idas.chox.service.reports.viewdata.ClaimFileReportData;


/**
 *
 * @author John
 */
public class ClaimFileReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimFileReport.class);

    Map externalParameter;
    private BaseDataService baseDataService;
    private ReportDataService reportDataService;

    @Override
    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }
    
    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setReportDataService(ReportDataService reportDataService) {
        this.reportDataService = reportDataService;
    }

    @Override
    public HashMap getReportParameters() {
        boolean showInsurer = false;
        boolean showCHO = false;
        HashMap reportParameters = new HashMap();

        int claimId = Integer.parseInt(((String[]) externalParameter.get("claimId"))[0]);

        Claim claim = (Claim)baseDataService.get(Claim.class, claimId);
        LOG.debug("Generating report for claimId={} ('{}')", claimId, claim.getChoReference());

        WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
        
        ClaimFileReportData claimReport = new ClaimFileReportData(claim, currentUser);

        List<ClaimFileEcdData> claimEcds = ClaimFileEcdData.getClaimFileEcdData(claim);
        
        if (currentUser.isAnInsurer() || currentUser.isCHOXAdmin()) {
            showInsurer = true;
        }
        if (!currentUser.isAnInsurer() || currentUser.isCHOXAdmin()) {
            showCHO = true;
        }
        List<ClaimFileNoteData> claimNotes = ClaimFileNoteData.getClaimFileNoteData(claim, showInsurer, showCHO);
        List<ClaimFileBreData> breMessages = ClaimFileBreData.getClaimFileBreData(claim, showInsurer);
        
        reportParameters.put("claim", claimReport);
        reportParameters.put("ecds", claimEcds);
        reportParameters.put("notes", claimNotes);
        reportParameters.put("bremessages", breMessages);
        return reportParameters;
    }

    @Override
    public String getReportTemplateFileName() {
        return "template_ClaimFileReport.xls";
    }

    @Override
    public ByteArrayOutputStream build() throws Exception {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    @Override
    public String getReportCode() {
        return "RPT100";
    }
    
    @Override
    public short[] getColumnsToHide() {
        return null;
    }

}
