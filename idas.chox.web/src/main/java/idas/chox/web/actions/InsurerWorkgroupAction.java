package idas.chox.web.actions;

import idas.chox.core.model.Workgroup;
import idas.chox.core.services.WorkgroupService;
import idas.chox.web.viewdata.WorkgroupViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerWorkgroupAction extends BaseAction {

    protected int insurerId;
    protected List<WorkgroupViewData> workgroups;
    protected WorkgroupService workgroupService;

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public List<WorkgroupViewData> getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(List<WorkgroupViewData> workgroups) {
        this.workgroups = workgroups;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

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

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.workgroups);
        return "{totalCount:" + this.workgroups.size() + ",results:" + jObject.toString() + "}";
    }
    
}
