package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.BreBand;
import idas.chox.core.services.BreBandService;
import idas.chox.web.viewdata.InsurerBreBandViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerBreBandAction extends BaseAction implements ModelDriven<BreBand>, Preparable {

    private String objectId;
    private int insurerId = -1;
    private BreBand model;
    protected List<InsurerBreBandViewData> insurerBreBands;
    protected BreBandService breBandService;
    
    public String doRenderActionPage() {
        return SUCCESS;
    }

    public boolean getIsNew() {
        if (Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }
    
    public BreBand getModel() {
        return model;
    }

    public void setModel(BreBand model) {
        this.model = model;
    }
    
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void prepare() throws Exception {
        if (Integer.valueOf(objectId) <= 0) {
            model = new BreBand();
        } else {
            model = breBandService.getBreBand(Integer.valueOf(objectId));
        }
    }
    
    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerBreBands);
        return "{totalCount:" + this.insurerBreBands.size() + ",results:" + jObject.toString() + "}";
    }

    @Override
    public String execute() {

        try {

            List<BreBand> insurerBreBandData = this.breBandService.getInsurerBreBandsByInsurer(this.insurerId);

            insurerBreBands = new ArrayList<InsurerBreBandViewData>();

            for (BreBand h : insurerBreBandData) {
                insurerBreBands.add(new InsurerBreBandViewData(h));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return SUCCESS;
    }

    public void setBreBandService(BreBandService service) {
        this.breBandService = service;
    }
}