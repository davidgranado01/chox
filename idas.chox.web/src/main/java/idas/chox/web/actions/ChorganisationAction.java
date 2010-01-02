package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminChorganisationService;
import idas.chox.web.viewdata.ChorganisationViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class ChorganisationAction extends BaseAction implements ModelDriven<Chorganisation>, Preparable {

    private AdminChorganisationService adminChorganisationService;
    private List<ChorganisationViewData> credithireorganisation;
    private BreBandOrganisationService breBandOrganisationService;
    private ChorganisationService service;
    private int insurerId;
    private String objectId;
    private Chorganisation model;

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public boolean getIsNew() {
        if (Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }

    public Chorganisation getModel() {
        return model;
    }

    public void setModel(Chorganisation model) {
        this.model = model;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void prepare() throws Exception {

        try {

            if (Integer.valueOf(objectId) <= 0) {
                model = new Chorganisation();
            } else {
                model = adminChorganisationService.getChorganisation(objectId);
            }

        } catch (Exception ex) {
            handleException(this, ex);
        }
    }

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

    public String triggerChorganisationStatus() throws Exception {

        try {

            ActionResponse response = adminChorganisationService.UpdateChorganisationStatus(this.objectId);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String updateChorganisation() throws Exception {

        try {

            if (getIsNew()) {

                if (this.adminChorganisationService.isChorganisationNameExist(model.getName())) {
                    this.getActionResponse().AddError("Insurer name already exist!");
                    return SUCCESS;
                }
            }

            model = adminChorganisationService.UpdateChorganisation(model);

            if (getIsNew()) {
                this.getActionResponse().AssignNewIdResult(model.getId());
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Override
    public String execute() {

        List<Chorganisation> credithireorganisationData = this.service.getChorganisations("name");

        credithireorganisation = new ArrayList<ChorganisationViewData>();

        for (Chorganisation h : credithireorganisationData) {
            credithireorganisation.add(new ChorganisationViewData(h));
        }

        return SUCCESS;
    }

    // BRE MAPPING, GET CREDIT HIRE WITHOUT CHO BAND
    public String getChorganisationsByInsurerIdWithoutBreBand() {

        try {

            credithireorganisation = new ArrayList<ChorganisationViewData>();

            if (insurerId > 0) {

                List<Chorganisation> chorganisations = service.getChorganisationsByInsurerId(insurerId);

                for (Chorganisation object : chorganisations) {

                    if (!breBandOrganisationService.isActiveChorganisationWithBand(object.getId(), insurerId)) {
                        credithireorganisation.add(new ChorganisationViewData(object));
                    }

                }
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
    }

    public void setChorganisationService(ChorganisationService service) {
        this.service = service;
    }

    public void setAdminChorganisationService(AdminChorganisationService adminChorganisationService) {
        this.adminChorganisationService = adminChorganisationService;
    }
}
