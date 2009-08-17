package chox.web.actions;

import chox.model.ChoBandOrganisation;
import chox.services.ChoBandOrganisationService;
import chox.web.viewdata.ChoBandChorganisationViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerChoBandMappingAction extends AdminBaseModelAction {

    private List<ChoBandChorganisationViewData> insurerChoBand;
    private ChoBandOrganisationService service;
    private int insurerId = -1;
    private int chobandId = -1;
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerChoBand);
        return "{totalCount:" + this.insurerChoBand.size() + ",results:" + jObject.toString() + "}";
    }
    
    public void setChoBandOrganisationService(ChoBandOrganisationService service)
    {
        this.service = service;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getChobandId() {
        return chobandId;
    }

    public void setChobandId(int chobandId) {
        this.chobandId = chobandId;
    }

    public List<ChoBandChorganisationViewData> getChoViewDataList(List<ChoBandOrganisation> objects){
        
        List<ChoBandChorganisationViewData> choBandChorganisationViewDatas = new ArrayList<ChoBandChorganisationViewData>();
        
        for(ChoBandOrganisation h : objects)
        {    
            choBandChorganisationViewDatas.add(new ChoBandChorganisationViewData(h));
        }
        
        return choBandChorganisationViewDatas;
    }
    
    
    public String getChorganisationWithChoBandAssigned(){
        
        String sActionMsg = "";
        boolean bActionFlag = false;
        
        try{
            
            List<ChoBandOrganisation> chobandorganisations = new ArrayList<ChoBandOrganisation>();
            
            chobandorganisations = service.getChoBandChorganisationsByChoBandId(chobandId);
            insurerChoBand = getChoViewDataList(chobandorganisations);
            
            bActionFlag = true;
            sActionMsg = getSystemLogService().getListingLogMsg(insurerChoBand.size(), "ChoBandId:"+chobandId+"|InsurerId:"+insurerId);
            
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM006", sActionMsg, bActionFlag, 0);        
        return SUCCESS;
    }
    
    @Override
    public String execute() {
        return SUCCESS;
    }    
    
}
