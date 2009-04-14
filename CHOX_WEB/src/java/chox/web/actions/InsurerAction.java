package chox.web.actions;

import chox.model.Insurer;
import chox.services.InsurerService;
import chox.web.viewdata.InsurerViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerAction extends BaseAction {

    private List<InsurerViewData> insurer;
    private InsurerService service;
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurer);
        return "{totalCount:" + this.insurer.size() + ",results:" + jObject.toString() + "}";
    }

    public void setInsurerService(InsurerService service)
    {
        this.service = service;
    }
    
    @Override
    public String execute() {

        List<Insurer> insurerData = this.service.getInsurers();
        
        insurer = new ArrayList<InsurerViewData>();
        
        for(Insurer h : insurerData)
        {
            insurer.add(new InsurerViewData(h));
        }
        
        return SUCCESS;
    }         
    
}
