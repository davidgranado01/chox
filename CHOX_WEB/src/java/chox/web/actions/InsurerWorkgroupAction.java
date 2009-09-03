package chox.web.actions;

import chox.model.Workgroup;
import chox.services.WorkgroupService;
import chox.web.viewdata.WorkgroupViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerWorkgroupAction extends AdminBaseModelAction {

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
        
        String sActionMsg = "";
        boolean bActionFlag = false;
        
        try{
            
            List<Workgroup> workgroupDatas = this.workgroupService.getAllObjects(insurerId);
            
            this.workgroups = new ArrayList<WorkgroupViewData>();

            for(Workgroup h : workgroupDatas)
            {    
                this.workgroups.add(new WorkgroupViewData(h));
            }
                    
            bActionFlag = true;
            sActionMsg = getSystemLogService().getListingLogMsg(this.workgroups.size(), "InsurerId:"+insurerId);
            
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM012", sActionMsg, bActionFlag, 0);
        
        return SUCCESS;
    }     
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.workgroups);
        return "{totalCount:" + this.workgroups.size() + ",results:" + jObject.toString() + "}";
    }      
    
    
}
