package chox.web.actions;

import chox.model.Chorganisation;
import chox.services.ChoBandOrganisationService;
import chox.services.ChorganisationService;
import chox.web.viewdata.ChorganisationViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class ChorganisationAction extends AdminBaseModelAction {

    private List<ChorganisationViewData> credithireorganisation;
    private ChoBandOrganisationService choBandOrganisationService;
    private ChorganisationService service;
    private int insurerId;
    
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.credithireorganisation);
        return "{totalCount:" + this.credithireorganisation.size() + ",results:" + jObject.toString() + "}";
    }

    public void setChoBandOrganisationService(ChoBandOrganisationService choBandOrganisationService) {
        this.choBandOrganisationService = choBandOrganisationService;
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
    
    public String getAvailableChorganisation(){
        
        if(insurerId>0){
        
            List<Chorganisation> chorganisationData = this.service.getObjectsWithoutInsurer(insurerId);
            
            credithireorganisation = new ArrayList<ChorganisationViewData>();

            for(Chorganisation h : chorganisationData)
            {
                credithireorganisation.add(new ChorganisationViewData(h));
            }
        }
        
        return SUCCESS;
    }  
    
    // BRE MAPPING, GET CREDIT HIRE WITHOUT CHO BAND
    public String getChorganisationsByInsurerIdWithoutChoBand(){
        
        String sActionMsg = "";
        boolean bActionFlag = false;
         
        try{
            
            credithireorganisation = new ArrayList<ChorganisationViewData>();
            
            if(insurerId>0){
                
                List<Chorganisation> chorganisations = service.getObjectsByInsurerId(insurerId);
                
                for(Chorganisation object : chorganisations){
                    
                    if(!choBandOrganisationService.isActiveChorganisationWithBand(object.getId(), insurerId)){
                        credithireorganisation.add(new ChorganisationViewData(object));
                    }

                }
            }

            bActionFlag = true;
            sActionMsg = getSystemLogService().getListingLogMsg(credithireorganisation.size(), "InsurerId:"+insurerId);
            
        } catch (Exception ex) {
            sActionMsg = ex.getMessage();
        }
        
        getSystemLogService().logSystemLog("ADM003", sActionMsg, bActionFlag, 0);          
        return SUCCESS;
    } 
    
}
