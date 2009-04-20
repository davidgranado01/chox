
package chox.web.actions;

import chox.model.LineOfBusiness;
import chox.services.LineOfBusinessService;
import chox.web.viewdata.LineOfBusinessViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerLineOfBusinessAction extends BaseAction {

    protected int insurerId;
    protected List<LineOfBusinessViewData> LineOfBusinesses;
    protected LineOfBusinessService service;

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setService(LineOfBusinessService service) {
        this.service = service;
    }
    
    @Override
    public String execute() {
        
        System.out.println(">>>>>>>>>>>>>> 0"+insurerId);
        
        List<LineOfBusiness> lineOfBusinessData = this.service.getInsurerLineOfBusiness(insurerId);
        
        System.out.println(">>>>>>>>>>>>>> 1");
        
        LineOfBusinesses = new ArrayList<LineOfBusinessViewData>();
        
        System.out.println(">>>>>>>>>>>>>> 2");
        
        for(LineOfBusiness h : lineOfBusinessData)
        {    
            System.out.println(">>>>>>>>>>>>>> + "+h.getName());
            
            LineOfBusinesses.add(new LineOfBusinessViewData(h));
        }
        
        System.out.println(">>>>>>>>>>>>>> 3");
        
        return SUCCESS;
    }     
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.LineOfBusinesses);
        return "{totalCount:" + this.LineOfBusinesses.size() + ",results:" + jObject.toString() + "}";
    }    
}
