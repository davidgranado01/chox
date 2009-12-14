package idas.chox.web.actions;

import idas.chox.core.model.BreBand;
import idas.chox.core.services.BreBandService;
import idas.chox.web.viewdata.InsurerBreBandViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerBreBandAction extends BaseAction {

    private List<InsurerBreBandViewData> insurerBreBand;
    private BreBandService service;
    private int insurerId = -1;

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerBreBand);
        return "{totalCount:" + this.insurerBreBand.size() + ",results:" + jObject.toString() + "}";
    }

    public void setBreBandService(BreBandService service) {
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


        try {

            List<BreBand> insurerBreBandData = this.service.getInsurerBreBand(this.insurerId);

            insurerBreBand = new ArrayList<InsurerBreBandViewData>();

            for (BreBand h : insurerBreBandData) {
                insurerBreBand.add(new InsurerBreBandViewData(h));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return SUCCESS;
    }
}
