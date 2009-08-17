package chox.web.actions;

import chox.model.Insurer;
import chox.services.InsurerService;
import chox.web.viewdata.InsurerViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerAction extends AdminBaseModelAction {

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

        String sActionMsg = "";
        boolean bActionFlag = false;
        
        try{
            List<Insurer> insurerData = this.service.getInsurers();

            insurer = new ArrayList<InsurerViewData>();

            for(Insurer h : insurerData)
            {
                insurer.add(new InsurerViewData(h));
            }
            
            bActionFlag = true;
            sActionMsg = getSystemLogService().getListingLogMsg(insurer.size(), "");
            
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM001", sActionMsg, bActionFlag, 0);
        
        return SUCCESS;
    }         
    
}
