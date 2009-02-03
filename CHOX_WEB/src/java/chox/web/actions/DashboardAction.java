/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chox.web.actions;

import chox.model.Insurer;
import chox.services.DataService;
import chox.services.LookupService;
import chox.services.UserService;
import chox.web.dashboard.InsurerDashboardBuilder;
import chox.web.dashboard.viewdata.InsurerBoardViewData;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.ParameterAware;

/**
 *
 * @author Emmanuel
 */
public class DashboardAction extends BaseAction implements ParameterAware {

    private InsurerBoardViewData monthToDateInsurerBoardViewData;
    private InsurerBoardViewData weekToDateInsurerBoardViewData;
    private InsurerBoardViewData cumulativeInsurerBoardViewData;
    private DataService dataService;
    private LookupService lookupService;
    private UserService userService;
    private List suppliers;
    private Map extParameters;
    private Long numberOfActiveUser;

    public String showInsurerBoardHeader() {
        Insurer currentInsurer = this.getAuthenticatedUser().getUser().getInsurer();
        numberOfActiveUser = userService.getNumInsActiveUser(currentInsurer.getId());
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
        
        return SUCCESS;
    }

    public void setDataService(DataService dataService) {
        this.dataService = dataService;
    }

    public void setLookupService(LookupService service) {
        this.lookupService = service;
    }

    public List getSuppliers() {
        if (suppliers == null) {
            suppliers = this.lookupService.getSuppliers();
        }
        return suppliers;
    }

    public void setParameters(Map extParameters) {
        this.extParameters = extParameters;
    }

    public InsurerBoardViewData getM2DData() {
        return monthToDateInsurerBoardViewData;
    }

    public InsurerBoardViewData getW2DData() {
        return weekToDateInsurerBoardViewData;
    }

    public InsurerBoardViewData getCData() {
        return cumulativeInsurerBoardViewData;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Long getNumberOfActiveUser() {
        return numberOfActiveUser;
    }
}
