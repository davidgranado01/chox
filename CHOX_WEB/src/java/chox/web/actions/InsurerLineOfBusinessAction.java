
package chox.web.actions;

import chox.model.LineOfBusiness;
import chox.services.LineOfBusinessService;
import chox.web.viewdata.LineOfBusinessViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerLineOfBusinessAction extends BaseAction {

    protected int insurerId;
    protected List<LineOfBusinessViewData> lineOfBusinesses;
    protected LineOfBusinessService service;

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setLineOfBusinessService(LineOfBusinessService service) {
        this.service = service;
    }


    @Override
    public String execute() {
        
        List<LineOfBusiness> lineOfBusinessData = this.service.getInsurerLineOfBusiness(insurerId);
        
        lineOfBusinesses = new ArrayList<LineOfBusinessViewData>();
        
        for(LineOfBusiness h : lineOfBusinessData)
        {    
            lineOfBusinesses.add(new LineOfBusinessViewData(h));
        }
        
        return SUCCESS;
    }     
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.lineOfBusinesses);
        return "{totalCount:" + this.lineOfBusinesses.size() + ",results:" + jObject.toString() + "}";
    }    
}
