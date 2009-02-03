/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.web.actions;

import chox.services.DataService;
import chox.services.LookupService;
import chox.web.dashboard.InsurerDashboardBuilder;
import chox.web.dashboard.viewdata.InsurerBoardViewData;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.ParameterAware;

/**
 *
 * @author Emmanuel
 */
public class DashboardAction extends BaseAction implements ParameterAware{    
    private InsurerBoardViewData monthToDateInsurerBoardViewData;
    private InsurerBoardViewData weekToDateInsurerBoardViewData;
    private InsurerBoardViewData cumulativeInsurerBoardViewData;
    
    private DataService dataService;
    private LookupService lookupService;
    private List suppliers;
    private Map extParameters;
    
    public String showInsurerBoardHeader()
    {        
        return SUCCESS;
    }
    
    public String showInsurerBoard()
    {
        InsurerDashboardBuilder builder = new InsurerDashboardBuilder();        
        builder.setDataService(dataService);
        builder.setExtParameters(extParameters);
        builder.setInsurer(this.getAuthenticatedUser().getUser().getInsurer());
        monthToDateInsurerBoardViewData = builder.getMonthToDate();
        weekToDateInsurerBoardViewData = builder.getWeekToDate();
        cumulativeInsurerBoardViewData = builder.getCumulative();
        
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

    public InsurerBoardViewData getMonthToDateInsurerBoardViewData() {
        return monthToDateInsurerBoardViewData;
    }

    public InsurerBoardViewData getWeekToDateInsurerBoardViewData() {
        return weekToDateInsurerBoardViewData;
    }

    public InsurerBoardViewData getCumulativeInsurerBoardViewData() {
        return cumulativeInsurerBoardViewData;
    }

}
