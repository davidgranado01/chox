package chox.web.actions;

import chox.model.BreBandOrganisation;
import chox.services.BreBandOrganisationService;
import chox.web.viewdata.BreBandChorganisationViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerBreBandMappingAction extends BaseAction {

    private List<BreBandChorganisationViewData> insurerBreBand;
    private BreBandOrganisationService breBandOrganisationService;
    private int insurerId = -1;
    private int breBandId = -1;
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerBreBand);
        return "{totalCount:" + this.insurerBreBand.size() + ",results:" + jObject.toString() + "}";
    }
    
    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService)
    {
        this.breBandOrganisationService = breBandOrganisationService;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getBreBandId() {
        return breBandId;
    }

    public void setBreBandId(int breBandId) {
        this.breBandId = breBandId;
    }

    public List<BreBandChorganisationViewData> getChoViewDataList(List<BreBandOrganisation> objects){
        
        List<BreBandChorganisationViewData> breBandChorganisationViewDatas = new ArrayList<BreBandChorganisationViewData>();
        
        for(BreBandOrganisation h : objects)
        {    
            breBandChorganisationViewDatas.add(new BreBandChorganisationViewData(h));
        }
        
        return breBandChorganisationViewDatas;
    }
    
    
    public String getChorganisationWithBreBandAssigned(){

        try{
            
            List<BreBandOrganisation> brebandorganisations = new ArrayList<BreBandOrganisation>();
            
            brebandorganisations = breBandOrganisationService.getBreBandChorganisationsByBreBandId(breBandId);
            insurerBreBand = getChoViewDataList(brebandorganisations);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
      
        return SUCCESS;
    }
    
    @Override
    public String execute() {
        return SUCCESS;
    }    
    
}
