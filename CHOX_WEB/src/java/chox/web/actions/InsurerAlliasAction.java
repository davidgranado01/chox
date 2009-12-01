package chox.web.actions;

import chox.model.InsurerAllias;
import chox.services.InsurerAlliasService;
import chox.web.viewdata.InsurerAlliasViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerAlliasAction extends BaseAction {

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

        
        try{
            
            List<InsurerAllias> insurerAlliasData = this.service.getInsurerAllias(insurerId);
            insurerAlliases = new ArrayList<InsurerAlliasViewData>();

            for(InsurerAllias h : insurerAlliasData)
            {    
                insurerAlliases.add(new InsurerAlliasViewData(h));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        

        
        return SUCCESS;
    }     
    
}
