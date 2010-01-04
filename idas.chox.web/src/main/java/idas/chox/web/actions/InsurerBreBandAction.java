package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.BreBand;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.InsurerService;
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
    protected InsurerService insurerService;

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public boolean getIsNew() {

        if (objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0) {
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
        try {

            model = new BreBand();

            if (objectId != null && !objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = breBandService.getBreBand(Integer.valueOf(this.objectId));
                }
            }

        } catch (Exception ex) {
            handleException(this, ex);
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
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String updateInsurerBreBand() {

        try {

            model.setInsurer(insurerService.getInsurer(this.insurerId));

            if (breBandService.isBreBandNameExist(model)) {
                getActionResponse().AddError("Selected Band Name already exists");
            } else {

                breBandService.saveBreBand(model);

                if (getIsNew()) {
                    getActionResponse().AssignNewIdResult(model.getId());
                }

            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String deleteInsurerBreBand() {
        try {

            if (breBandService.isBreBandOccupied(model)) {
                getActionResponse().AddError("You cannot delete '" + model.getName() + "' because it is currently being used by one or more Credit Hire Organisations. Please remove the Credit Hire Organisations from this BRE and try again");
            } else {
                this.breBandService.deleteBreBand(model);
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }
        return SUCCESS;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }
}
