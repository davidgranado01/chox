package idas.chox.web.actions;

import idas.chox.web.viewdata.WorkgroupViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.Workgroup;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;

public class InsurerWorkgroupAction extends BaseAction implements ModelDriven<Workgroup>, Preparable {

    protected int insurerId;
    protected int workgroupId = -1;
    protected String workgroupName;
    private Workgroup model;
    protected List<WorkgroupViewData> workgroups;
    private AdminInsurerService adminInsurerService;

    public Workgroup getModel() {
        return model;
    }

    public void setModel(Workgroup model) {
        this.model = model;
    }

    public void prepare() throws Exception {
        if (workgroupId <= 0) {
            model = new Workgroup();
        } else {
            model = adminInsurerService.getWorkgroup(workgroupId);
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

    public String getInsurerWorkgroups() {

        try {

            List<Workgroup> workgroupDatas = adminInsurerService.getInsurerWorkgroups(this.insurerId);
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
            model.setName(this.workgroupName);
            ActionResponse response = adminInsurerService.addNewInsurerWorkgroup(model, this.insurerId);
            setActionResponse(response);
        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeInsurerWorkgroup() {

        if (this.insurerId > 0 && this.workgroupId > 0) {

            try {

                model.setName(this.workgroupName);
                ActionResponse response = adminInsurerService.removeInsurerWorkgroup(model, this.insurerId);
                setActionResponse(response);

            } catch (Exception ex) {
                handleException(this, ex);
                return ERROR;
            }
        }

        return SUCCESS;
    }

    public String triggerInsurerWorkgroupStatus() {

        if (this.insurerId > 0 && this.workgroupId > 0) {

            try {

                ActionResponse response = adminInsurerService.triggerInsurerWorkgroupStatus(model, this.insurerId);
                setActionResponse(response);

            } catch (Exception ex) {
                handleException(this, ex);
                return ERROR;
            }
        }

        return SUCCESS;

    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }
    // </editor-fold>
}
