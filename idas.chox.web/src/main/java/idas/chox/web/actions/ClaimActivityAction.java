package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.services.ClaimService;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;

public class ClaimActivityAction extends BaseAction implements ModelDriven<Activity>, Preparable {

    private ActivityFactory activityFactory;
    private ClaimService claimService;
    private Activity activity;
    private Claim claim;
    private String name;
    private int id;

    public Activity getModel() {
        return activity;
    }

    public void prepare() throws Exception {
        activity = activityFactory.getActivity(name);
        claim = claimService.getClaim(id);
    }

    @Override
    public String execute() {
        if (activity != null) {

            try {
                activity.process(claim);
            } catch (Exception ex) {
                handleException(this, ex);
                return ERROR;
            }
            return SUCCESS;
        }
        return ERROR;
    }

    // <editor-fold defaultstate="collapsed" desc="Parameters">
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Services">
    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }
    // </editor-fold>
}
