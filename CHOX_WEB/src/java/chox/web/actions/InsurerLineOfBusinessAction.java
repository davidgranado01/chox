
package chox.web.actions;

import chox.model.LineOfBusiness;
import chox.services.LineOfBusinessService;
import chox.web.viewdata.LineOfBusinessViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerLineOfBusinessAction extends AdminBaseModelAction {

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
        
        String sActionMsg = "";
        boolean bActionFlag = false;
        
        try{   
            
            List<LineOfBusiness> lineOfBusinessData = this.service.getInsurerLineOfBusiness(insurerId);

            lineOfBusinesses = new ArrayList<LineOfBusinessViewData>();

            for(LineOfBusiness h : lineOfBusinessData)
            {    
                lineOfBusinesses.add(new LineOfBusinessViewData(h));
            }
            
            bActionFlag = true;
            sActionMsg = getSystemLogService().getListingLogMsg(lineOfBusinesses.size(), "InsurerId:"+insurerId);
            
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM005", sActionMsg, bActionFlag, 0);
        
        return SUCCESS;
    }     
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.lineOfBusinesses);
        return "{totalCount:" + this.lineOfBusinesses.size() + ",results:" + jObject.toString() + "}";
    }    
}
