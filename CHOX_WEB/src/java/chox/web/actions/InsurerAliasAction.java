package chox.web.actions;

import chox.model.InsurerAlias;
import chox.services.InsurerAliasService;
import chox.web.viewdata.InsurerAliasViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerAliasAction extends BaseAction {

    protected int insurerId;
    protected List<InsurerAliasViewData> insurerAliases;
    protected InsurerAliasService service;

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    
    public void setInsurerAliasService(InsurerAliasService service)
    {
        this.service = service;
    }
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerAliases);
        return "{totalCount:" + this.insurerAliases.size() + ",results:" + jObject.toString() + "}";
    }
    
    @Override
    public String execute() {

        
        try{
            
            List<InsurerAlias> insurerAliasData = this.service.getInsurerAlias(insurerId);
            insurerAliases = new ArrayList<InsurerAliasViewData>();

            for(InsurerAlias h : insurerAliasData)
            {    
                insurerAliases.add(new InsurerAliasViewData(h));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        

        
        return SUCCESS;
    }     
    
}
