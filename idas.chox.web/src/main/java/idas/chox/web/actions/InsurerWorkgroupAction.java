package idas.chox.web.actions;

import idas.chox.web.viewdata.WorkgroupViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.service.ActionResponse;

public class InsurerWorkgroupAction extends BaseAction implements ModelDriven<Workgroup>, Preparable {

    protected int insurerId;
    protected int workgroupId = -1;
    protected String workgroupName;
    private Workgroup model;
    protected List<WorkgroupViewData> workgroups;
    protected WorkgroupService workgroupService;
    protected InsurerService insurerService;

    public Workgroup getModel() {
        return model;
    }

    public void setModel(Workgroup model) {
        this.model = model;
    }

    public void prepare() throws Exception {
        if (Integer.valueOf(workgroupId) <= 0) {
            model = new Workgroup();
        } else {
            model = workgroupService.getWorkgroup(workgroupId);
        }
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.workgroups);
        return "{totalCount:" + this.workgroups.size() + ",results:" + jObject.toString() + "}";
    }
    
    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public List<WorkgroupViewData> getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(List<WorkgroupViewData> workgroups) {
        this.workgroups = workgroups;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Override
    public String execute() {

        try {

            List<Workgroup> workgroupDatas = this.workgroupService.getAllWorkgroupsByInsurer(insurerId, "name");

            this.workgroups = new ArrayList<WorkgroupViewData>();

            for (Workgroup h : workgroupDatas) {
                this.workgroups.add(new WorkgroupViewData(h));
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SUCCESS;
    }

    public String addNewInsurerWorkgroup() {

        try {

            if (!workgroupService.isWorkgroupNameExistByInsurer(insurerId, workgroupName)) {

                model.setInsurer(insurerService.getInsurer(insurerId));
                model.setName(workgroupName);
                model.setStatus(true);
                workgroupService.saveWorkgroup(model);

                getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Workgroup '" + workgroupName + "' has been created");

            } else {

                getActionResponse().AddError("Workgroup '" + workgroupName + "' already exists");

            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeInsurerWorkgroup() {

        if (this.insurerId > 0 && this.workgroupId > 0) {

            try {

                Insurer insurer = insurerService.getInsurer(insurerId);

                // WORKGROUP FEATUERE IS ENABLE
                // EXCEPT THE WORKGROUP ITSELF, DO NOT HAVE ANY ACTIVE WORKGROUP
                if (insurer.isWorkgroupEnable() && !workgroupService.isWorkgroupAllowToInactive(insurerId, model.getId())) {
                    getActionResponse().AddError("Unable to remove this workgroup. Must maintain at least one active workgroup for this insurer.");
                    return SUCCESS;
                }

                if (workgroupService.isWorkgroupDeletable(model.getId())) {
                    workgroupService.deleteWorkgroup(model);
                    getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Workgroup '" + model.getName() + "' has been removed");

                } else {
                    getActionResponse().AddError("Workgroup '" + model.getName() + "' cannot be removed");
                }

            } catch (Exception ex) {
                handleException(this, ex);
                return ERROR;
            }
        }

        return SUCCESS;
    }

    public String triggerInsurerWorkgroupStatus() {

        boolean isAllowedToChange = true;
        String ackMsg = "";
        Insurer insurer = insurerService.getInsurer(insurerId);

        // CHECK WORKGROUPS STATUS IF CHANGE FROM ACTIVE TO INACTIVE
        // WORKGROUP FEATUERE IS ENABLE
        // EXCEPT THE WORKGROUP ITSELF, DO NOT HAVE ANY ACTIVE WORKGROUP
        if (insurer.isWorkgroupEnable() && model.isStatus() && !workgroupService.isWorkgroupAllowToInactive(insurerId, model.getId())) {
            isAllowedToChange = false;
        }

        if (isAllowedToChange) {
            model.setStatus(!model.isStatus());
            workgroupService.saveWorkgroup(model);
        } else {
            ackMsg = "Unable to de-activate this workgroup. Must maintain at least one active workgroup for this insurer.";
            getActionResponse().AddError(ackMsg);
        }

        return SUCCESS;

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }
    // </editor-fold>
}
