package chox.web.actions;

import chox.model.Chorganisation;
import chox.services.ChorganisationService;
import chox.web.viewdata.ChorganisationViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class ChorganisationAction extends BaseAction {

    private List<ChorganisationViewData> credithireorganisation;
    private ChorganisationService service;
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.credithireorganisation);
        return "{totalCount:" + this.credithireorganisation.size() + ",results:" + jObject.toString() + "}";
    }
     
    public void setChorganisationService(ChorganisationService service)
    {
        this.service = service;
    }
    
    @Override
    public String execute() {

        List<Chorganisation> credithireorganisationData = this.service.getChorganisation();
        
        credithireorganisation = new ArrayList<ChorganisationViewData>();
        
        for(Chorganisation h : credithireorganisationData)
        {
            credithireorganisation.add(new ChorganisationViewData(h));
        }
        
        return SUCCESS;
    }         
    
}
