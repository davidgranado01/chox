
package chox.web.actions;

import chox.model.InsurerChorganisation;
import chox.services.InsurerChorganisationService;
import chox.web.viewdata.InsurerChorganisationViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerChorganisationAction extends BaseAction {

    protected int insurerId;
    protected List<InsurerChorganisationViewData> insurerChorganisations;
    protected InsurerChorganisationService service;
      
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setInsurerChorganisationService(InsurerChorganisationService service) {
        this.service = service;
    }

    @Override
    public String execute() {
        
        /*
        List<Chorganisation> chorganisationsData = this.service.get
        
        chorganisations = new ArrayList<ChorganisationViewData>();
        
        for(Chorganisation h : lineOfBusinessData)
        {    
            chorganisations.add(new ChorganisationViewData(h));
        }
        */
        
        return SUCCESS;
    }     
    
    public List<InsurerChorganisationViewData> getChoViewDataList(List<InsurerChorganisation> objects){
        
        List<InsurerChorganisationViewData> insurerChorgs = new ArrayList<InsurerChorganisationViewData>();
        
        for(InsurerChorganisation h : objects)
        {    
            insurerChorgs.add(new InsurerChorganisationViewData(h));
        }
        return insurerChorgs;
    }
    
    public String getSelectedChorganisation(){
        List<InsurerChorganisation> chorganisationsData = this.service.getInsurerChorganisationByInsurer(insurerId);
        insurerChorganisations = getChoViewDataList(chorganisationsData);
        return SUCCESS;
    }
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerChorganisations);
        return "{totalCount:" + this.insurerChorganisations.size() + ",results:" + jObject.toString() + "}";
    }    
}
