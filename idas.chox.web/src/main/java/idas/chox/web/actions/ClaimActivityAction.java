package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Claim;
import idas.chox.core.services.ClaimService;
import idas.chox.core.workflow.Activity;
import idas.chox.service.workflow.ActivityFactory;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.StaleObjectStateException;

public class ClaimActivityAction extends BaseAction implements ModelDriven<Activity>, Preparable {

    private ActivityFactory activityFactory;
    private ClaimService claimService;
    private Activity activity;
    private Claim claim;
    private String name;
    private int id;
    private Integer currentVersion;
    private List<Integer> selectedClaimIdList;

    public Activity getModel() {
        return activity;
    }

    public void prepare() throws Exception {

        if (id > 0) {
            claim = claimService.getClaim(id);
            checkVersion();
        }

        activity = activityFactory.getActivity(name);
    }

    public String processMultipleClaims() {

        if (activity != null && selectedClaimIdList.size() > 0) {

            try {

                for (Integer selectedClaimId : selectedClaimIdList) {

                    claim = claimService.getClaim(selectedClaimId);
                    checkVersion();
                    activity.process(claim);
                }

            } catch (Exception ex) {
                handleException(ex);
                return ERROR;
            }
            return SUCCESS;
        }

        return ERROR;
    }

    @Override
    public String execute() {
        if (activity != null) {
            try {
                activity.process(claim);
            } catch (Exception ex) {
                handleException(ex);
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

    private void checkVersion() {
        if (currentVersion != null && !claim.getVersion().equals(currentVersion)) {
            StaleObjectStateException ex = new StaleObjectStateException(claim.getClass().getName(), claim.getId());
            this.handleException(ex);
            throw ex;
        }
    }

    public Integer getVersion() {
        return claim.getVersion();
    }

    public void setCurrentVersion(Integer currentVersion) {
        this.currentVersion = currentVersion;
    }

    public void setSelectedClaimIds(String ids) {
        String[] list = ids.split(",");
        selectedClaimIdList = new ArrayList<Integer>();
        for (String s : list) {
            Integer selectedId = Integer.parseInt(s.trim());
            selectedClaimIdList.add(selectedId);
        }
    }
}
