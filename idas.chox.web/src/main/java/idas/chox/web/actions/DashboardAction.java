/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.web.actions;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.UserService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.dashboard.ChoDashboardBuilder;
import idas.chox.service.dashboard.DashBoardViewData;
import idas.chox.service.dashboard.InsurerDashboardBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.ParameterAware;

public class DashboardAction extends BaseAction implements ParameterAware {

    private DashBoardViewData monthToDateInsurerBoardViewData;
    private DashBoardViewData weekToDateInsurerBoardViewData;
    private DashBoardViewData cumulativeInsurerBoardViewData;
    private BaseDataService baseDataService;
    private LookupService lookupService;
    private UserService userService;
    private ClaimService claimService;
    private List suppliers;
    private List insurers;
    private Map extParameters;
    private Long numberOfActiveUser;
    private Long numberOfClaimPending;
    private int insurerId;
    private int supplierId;

    public String getLastProcessDate() {

        String query = "select to_char(max(process_date), 'YYYY-MM-DD HH24:MI:SS') as last_process_date from claim_summary_process";
        List result = baseDataService.externalQuery(query, new HashMap());

        if (!result.isEmpty()) {
            Map data = (Map) result.get(0);
            return data.get("last_process_date".toLowerCase()).toString();
        }

        return "";

    }

    public String showInsurerBoardHeader() {
        Insurer currentInsurer = this.getAuthenticatedUser().getInsurer();
        numberOfActiveUser = userService.getNumInsActiveUser(currentInsurer.getId());
        return SUCCESS;
    }

    public String showChoBoardHeader() {
        Chorganisation currentCho = this.getAuthenticatedUser().getChorganisation();
        numberOfActiveUser = userService.getNumChoActiveUser(currentCho.getId());
        return SUCCESS;
    }

    public String showInsurerBoard() {

        try {

            Insurer currentInsurer = this.getAuthenticatedUser().getInsurer();
            InsurerDashboardBuilder builder = new InsurerDashboardBuilder(baseDataService, currentInsurer, extParameters);
            monthToDateInsurerBoardViewData = builder.getMonthToDate();
            weekToDateInsurerBoardViewData = builder.getWeekToDate();
            cumulativeInsurerBoardViewData = builder.getCumulative();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SUCCESS;
    }

    public String showChoBoard() {

        try {

            Chorganisation currentChorganisation = this.getAuthenticatedUser().getChorganisation();
            ChoDashboardBuilder builder = new ChoDashboardBuilder(baseDataService, currentChorganisation, extParameters);
            monthToDateInsurerBoardViewData = builder.getMonthToDate();
            weekToDateInsurerBoardViewData = builder.getWeekToDate();
            cumulativeInsurerBoardViewData = builder.getCumulative();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SUCCESS;
    }

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public List getSuppliers() {
        if (suppliers == null) {
            suppliers = this.lookupService.getAllSuppliers();
        }
        return suppliers;
    }

    public List getInsurers() {

        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }
        return insurers;
    }

    public void setParameters(Map extParameters) {
        this.extParameters = extParameters;
    }

    public DashBoardViewData getM2DData() {
        return monthToDateInsurerBoardViewData;
    }

    public DashBoardViewData getW2DData() {
        return weekToDateInsurerBoardViewData;
    }

    public DashBoardViewData getCData() {
        return cumulativeInsurerBoardViewData;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Long getNumberOfActiveUser() {
        return numberOfActiveUser;
    }

    public Long getNumberOfClaimPending() {
        return numberOfClaimPending;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
}
