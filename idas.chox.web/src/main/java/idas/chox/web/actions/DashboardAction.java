package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.ParameterAware;

import net.sf.json.JSONArray;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.LookupItem;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.UserService;
import idas.chox.data.services.BaseDataService;
import idas.chox.service.dashboard.ChoDashboardBuilder;
import idas.chox.service.dashboard.DashBoardViewData;
import idas.chox.service.dashboard.InsurerDashboardBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DashboardAction extends BaseAction implements ParameterAware {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardAction.class);
    

    private DashBoardViewData monthToDateInsurerBoardViewData;
    private DashBoardViewData weekToDateInsurerBoardViewData;
    private DashBoardViewData cumulativeInsurerBoardViewData;
    private BaseDataService baseDataService;
    private LookupService lookupService;
    private UserService userService;
   // private List suppliers;
   // private List insurers;
    private Map extParameters;
    private Long numberOfActiveUser;
    private Long numberOfClaimPending;
    private List<Chorganisation> suppliers;
    private List<Insurer> insurers;
    

    public String getLastProcessDate() {

        String query = "select to_char(max(process_date), 'YYYY-MM-DD HH24:MI:SS') as last_process_date from dashboard";
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
            weekToDateInsurerBoardViewData = builder.getWeekToDate();
            monthToDateInsurerBoardViewData = builder.getMonthToDate();
            cumulativeInsurerBoardViewData = builder.getCumulative();

        } catch (Exception ex) {
            LOG.error("Exception",ex);
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
            LOG.error("Exception",ex);
        }
        return SUCCESS;
    }

    public void setDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public List getSuppliers() {
        if (suppliers == null) {
            suppliers = this.lookupService.getSuppliers(false); // inlucde manual CHO.
        }
        return suppliers;
    }

    public String getSuppliersJsonString() {
            List<LookupItem> luItems = new ArrayList<LookupItem>(getSuppliers().size());
            for (Chorganisation supplier : suppliers) {
                luItems.add(new LookupItem(supplier.getId().toString(), supplier.getName()));
            }
           
           LOG.debug("Insurers json is :" + JSONArray.fromObject(luItems).toString());
           return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }

    public List getInsurers() {

        if (insurers == null) {
            insurers = this.lookupService.getInsurers();
        }
        return insurers;
    }

     public String getInsurersJsonString() {
            List<LookupItem> luItems = new ArrayList<LookupItem>(getInsurers().size());
            for (Insurer insurer : insurers) {
                luItems.add(new LookupItem(insurer.getId().toString(), insurer.getName()));
            }
//           System.out.println("Insurers json is :" + JSONArray.fromObject(luItems).toString());
           return "{totalCount:" + luItems.size() + ", results:" + JSONArray.fromObject(luItems).toString() + "}";
    }


    @Override
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

    public boolean isUploadEnabled() {
        if(this.getAuthenticatedUser().isAnInsurer()){
            return this.getAuthenticatedUser().getInsurer().isInvoiceUploadEnabled();
        }else{
            return false;
        }
    }

}
