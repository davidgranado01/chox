package idas.chox.service.admin;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.service.ActionResponse;
import idas.chox.data.services.DataService;
import java.util.ArrayList;
import java.util.List;

public class AdminInsurerAutomaticRoutingService extends DataService {

    private ActionResponse actionResponse;
    private InsurerService insurerService;
    private WorkgroupService workgroupService;
    private AutomaticRoutingService automaticRoutingService;

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    public AutomaticRouting getInsurerAutomaticRouting(int automaticRoutingId) {
        return automaticRoutingService.getAutomaticRouting(automaticRoutingId);
    }
    
    public List<AutomaticRouting> getInsurerAutomaticRoutings(int insurerId) {
        return automaticRoutingService.getAutomaticRoutings(insurerId, -1);
    }

    public List getAvailableWorkgroups(int insurerId){

        List items = new ArrayList<IdLookupItem>();
        List<Workgroup> availableWorkgroups = workgroupService.getAvailableAutoRoutingWorkgroupsByInsurer(insurerId);
        
        for (Workgroup s : availableWorkgroups) {
            items.add(new IdLookupItem(s.getId(), s.getName()));
        }

        return items;
    }

    public ActionResponse addNewAutomaticRouting(Integer insurerId, Integer workgroupId, String regExpression) {
        this.actionResponse = new ActionResponse();

        if (insurerId > 0 && workgroupId > 0 && !regExpression.equalsIgnoreCase("")) {
            AutomaticRouting automaticRouting = new AutomaticRouting();
            automaticRouting.setExpression(regExpression);
            automaticRouting.setInsurer(insurerService.getInsurer(insurerId));
            automaticRouting.setWorkgroup(workgroupService.getWorkgroup(workgroupId));
            automaticRoutingService.saveAutomaticRouting(automaticRouting);
        } else {
            this.actionResponse.AddError("Incorrect Insurer and Workgroup");
        }

        return this.actionResponse;
    }

    public ActionResponse updateAutomaticRouting(AutomaticRouting automaticRouting) {
        this.actionResponse = new ActionResponse();

        automaticRoutingService.saveAutomaticRouting(automaticRouting);

        /*
        if (insurerId > 0 && workgroupId > 0 && !regExpression.equalsIgnoreCase("")) {
            AutomaticRouting automaticRouting = automaticRoutingService.getAutomaticRouting(insurerId, workgroupId);
            automaticRouting.setExpression(regExpression);
            automaticRoutingService.saveAutomaticRouting(automaticRouting);
        } else {
            this.actionResponse.AddError("Incorrect Insurer and Workgroup");
        }
        */
        
        return this.actionResponse;
    }

    public ActionResponse deleteAutomaticRouting(Integer automaticRoutingId) {
        this.actionResponse = new ActionResponse();

        if (automaticRoutingId > 0 && automaticRoutingId != null) {
            AutomaticRouting automaticRouting = automaticRoutingService.getAutomaticRouting(automaticRoutingId);
            automaticRoutingService.deleteAutomaticRouting(automaticRouting);
        } else {
            this.actionResponse.AddError("Incorrect Automatic Routing Record");
        }

        return this.actionResponse;
    }

    public void setAutomaticRoutingService(AutomaticRoutingService automaticRoutingService) {
        this.automaticRoutingService = automaticRoutingService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }
}
