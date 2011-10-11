package idas.chox.service.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import idas.chox.core.model.Claim;
import idas.chox.core.model.WebUser;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.reports.viewdata.ClaimFileBreData;
import idas.chox.service.reports.viewdata.ClaimFileEcdData;
import idas.chox.service.reports.viewdata.ClaimFileNoteData;
import idas.chox.service.reports.viewdata.ClaimFileReportData;
import java.io.ByteArrayOutputStream;


/**
 *
 * @author John
 */
public class ClaimFileReport implements Report {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimFileReport.class);

    Map externalParameter;
//    List<String> reportParameterNames;
    private BaseDataService baseDataService;
//    private WebUser user = new WebUser();

    @Override
    public void setExternalParameter(Map parameters) {
        this.externalParameter = parameters;
    }

    @Override
    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    @Override
    public HashMap getReportParameters() {
        boolean showInsurer = false;
        boolean showCHO = false;
        HashMap reportParameters = new HashMap();

        int claimId = Integer.parseInt(((String[]) externalParameter.get("claimId"))[0]);

        Claim claim = (Claim)baseDataService.get(Claim.class, claimId);
        LOG.debug("Generating report for claimId={} ('{}')", claimId, claim.getChoReference());

        ClaimFileReportData claimReport = new ClaimFileReportData(claim);

        List<ClaimFileEcdData> claimEcds = ClaimFileEcdData.getClaimFileEcdData(claim);
        
        WebUser currentUser = ((WebUser) externalParameter.get("CurrentUser"));
        if (currentUser.isAnInsurer() || currentUser.isCHOXAdmin())
            showInsurer = true;
        if (!currentUser.isAnInsurer() || currentUser.isCHOXAdmin())
            showCHO = true;
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
    public ByteArrayOutputStream build() {
        ReportBuilder builder = getReportBuilder();
        return builder.buildReport(this);
    }

    protected ReportBuilder getReportBuilder() {
        return new ExcelReportBuilder();
    }

    public String getReportCode() {
        return "RPT100";
    }

}
