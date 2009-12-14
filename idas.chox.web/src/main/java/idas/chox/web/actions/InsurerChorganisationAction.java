package idas.chox.web.actions;

import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.web.viewdata.InsurerChorganisationViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerChorganisationAction extends BaseAction {

    protected int insurerId;
    protected List<InsurerChorganisationViewData> insurerChorganisations;
    protected InsurerChorganisationService service;
    protected ChorganisationService chorganisationService;

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setInsurerChorganisationService(InsurerChorganisationService service) {
        this.service = service;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public List<InsurerChorganisationViewData> getChoViewDataList(List<InsurerChorganisation> objects) {

        List<InsurerChorganisationViewData> insurerChorgs = new ArrayList<InsurerChorganisationViewData>();

        for (InsurerChorganisation h : objects) {
            insurerChorgs.add(new InsurerChorganisationViewData(h));
        }
        return insurerChorgs;
    }

    // INSURER v.s CREDIT HIRE -- NEED ACTIVE ONLY 
    public String getSelectedChorganisation() {

        try {

            List<InsurerChorganisation> chorganisationsData = this.service.getObjects(insurerId, null);
            insurerChorganisations = getChoViewDataList(chorganisationsData);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerChorganisations);
        return "{totalCount:" + this.insurerChorganisations.size() + ",results:" + jObject.toString() + "}";
    }
}
