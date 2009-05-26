package chox.web.actions;

import chox.model.ChoBand;
import chox.services.ChoBandService;
import chox.web.viewdata.InsurerChoBandViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerChoBandAction extends BaseAction {

    private List<InsurerChoBandViewData> insurerChoBand;
    private ChoBandService service;
    private int insurerId = -1;
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerChoBand);
        return "{totalCount:" + this.insurerChoBand.size() + ",results:" + jObject.toString() + "}";
    }
    
    public void setChoBandService(ChoBandService service)
    {
        this.service = service;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    @Override
    public String execute() {

        List<ChoBand> insurerChoBandData = this.service.getInsurerChoBand(this.insurerId);
        
        insurerChoBand = new ArrayList<InsurerChoBandViewData>();
        
        for(ChoBand h : insurerChoBandData)
        {
            insurerChoBand.add(new InsurerChoBandViewData(h));
        }

        return SUCCESS;
    }         
    
}
