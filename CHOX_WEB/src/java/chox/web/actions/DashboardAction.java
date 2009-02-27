/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Chorganisation;
import chox.model.Insurer;
import chox.services.ClaimService;
import chox.services.DataService;
import chox.services.LookupService;
import chox.services.UserService;
import chox.web.dashboard.ChoDashboardBuilder;
import chox.web.dashboard.InsurerDashboardBuilder;
import chox.web.dashboard.viewdata.DashBoardViewData;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.ParameterAware;

/**
 *
 * @author Emmanuel
 */
public class DashboardAction extends BaseAction implements ParameterAware {

    private DashBoardViewData monthToDateInsurerBoardViewData;
    private DashBoardViewData weekToDateInsurerBoardViewData;
    private DashBoardViewData cumulativeInsurerBoardViewData;
    private DataService dataService;
    private LookupService lookupService;
    private UserService userService;
    private ClaimService claimService;
    private List suppliers;
    private List insurers;
    private Map extParameters;
    private Long numberOfActiveUser;
    private Long numberOfClaimPending;
    private int insurerId;
    private int choragnisationId;

    public String showInsurerBoardHeader() {
        Insurer currentInsurer = this.getAuthenticatedUser().getUser().getInsurer();
        numberOfActiveUser = userService.getNumInsActiveUser(currentInsurer.getId());
        return SUCCESS;
    }
    
    public String showChoBoardHeader() {
        Chorganisation currentCho = this.getAuthenticatedUser().getUser().getChorganisation();
        numberOfActiveUser = userService.getNumChoActiveUser(currentCho.getId());
        return SUCCESS;
    }

    public String showInsurerBoard() {
        try {
            InsurerDashboardBuilder builder = new InsurerDashboardBuilder();
            builder.setDataService(dataService);
            builder.setExtParameters(extParameters);
            Insurer currentInsurer = this.getAuthenticatedUser().getUser().getInsurer();
            builder.setInsurer(currentInsurer);
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
            ChoDashboardBuilder builder = new ChoDashboardBuilder();
            builder.setDataService(dataService);
            builder.setExtParameters(extParameters);
            Chorganisation currentChorganisation = this.getAuthenticatedUser().getUser().getChorganisation();
            builder.setChorganisation(currentChorganisation);
            monthToDateInsurerBoardViewData = builder.getMonthToDate();
            weekToDateInsurerBoardViewData = builder.getWeekToDate();
            cumulativeInsurerBoardViewData = builder.getCumulative();            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return SUCCESS;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public List getSuppliers() {
        if (suppliers == null) {
            Insurer currentInsurer = this.getAuthenticatedUser().getUser().getInsurer();
            suppliers = this.lookupService.getSuppliers(currentInsurer.getId());
        }
        return suppliers;
    }
    
    public List getInsurers() {
         
        if (insurers == null) {
            Chorganisation currentCho = this.getAuthenticatedUser().getUser().getChorganisation();
            insurers = this.lookupService.getInsurers(currentCho.getId());
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

    public int getChoragnisationId() {
        return choragnisationId;
    }

    public void setChoragnisationId(int choragnisationId) {
        this.choragnisationId = choragnisationId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    
    
}
