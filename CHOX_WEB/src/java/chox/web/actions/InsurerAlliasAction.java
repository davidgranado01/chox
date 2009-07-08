package chox.web.actions;

import chox.model.InsurerAllias;
import chox.services.InsurerAlliasService;
import chox.web.viewdata.InsurerAlliasViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerAlliasAction extends AdminBaseModelAction {

    protected int insurerId;
    protected List<InsurerAlliasViewData> insurerAlliases;
    protected InsurerAlliasService service;

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    
    public void setInsurerAlliasService(InsurerAlliasService service)
    {
        this.service = service;
    }
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerAlliases);
        return "{totalCount:" + this.insurerAlliases.size() + ",results:" + jObject.toString() + "}";
    }
    
    @Override
    public String execute() {

        String sActionMsg = "";
        boolean bActionFlag = false;
        
        try{
            
            List<InsurerAllias> insurerAlliasData = this.service.getInsurerAllias(insurerId);
            insurerAlliases = new ArrayList<InsurerAlliasViewData>();

            for(InsurerAllias h : insurerAlliasData)
            {    
                insurerAlliases.add(new InsurerAlliasViewData(h));
            }

            bActionFlag = true;
            sActionMsg = getSystemLogService().getListingLogMsg(insurerAlliases.size(), "InsurerId:"+insurerId);
        
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM002", sActionMsg, bActionFlag, 0);
        
        return SUCCESS;
    }     
    
}
