package idas.chox.web.actions;

import idas.chox.web.viewdata.InsurerViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Insurer;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.service.ActionResponse;

public class InsurerAction extends BaseAction implements ModelDriven<Insurer>, Preparable {

    private List<InsurerViewData> insurer;
    private String objectId;
    private Insurer model;
    private Integer tabIndex;
    private InsurerAliasService insurerAliasService;
    private WorkgroupService workgroupService;
    private BreBandService breBandService;
    private InsurerService insurerService;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public boolean getIsNew() {
        if (Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
    }

    public Insurer getModel() {
        return model;
    }

    public void setModel(Insurer model) {
        this.model = model;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurer);
        return "{totalCount:" + this.insurer.size() + ",results:" + jObject.toString() + "}";
    }

    public void prepare() throws Exception {
        try {
            if (!objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0) {
                model = new Insurer();
            } else {
                model = insurerService.getInsurer(Integer.valueOf(objectId));
            }
        } catch (Exception ex) {
            handleException(this, ex);

        }
    }

    @Override
    public String execute() {

        try {
            List<Insurer> insurerData = this.insurerService.getInsurers();

            insurer = new ArrayList<InsurerViewData>();

            for (Insurer h : insurerData) {
                insurer.add(new InsurerViewData(h));
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String updateInsurer() throws Exception {

        try {

            if (this.getIsNew()) {

                if (this.insurerService.isInsurerNameExist(model.getName())) {
                    this.getActionResponse().AddError("Insurer name already exist!");
                    return SUCCESS;
                }

            } else {

                if (model.isWorkgroupEnable() && !workgroupService.isInsurerAllowToEnableWorkgroup(model)) {
                    this.getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Please make sure there is atleast one active workgroup exist in order to enable workgroup function");
                    return SUCCESS;
                }

            }

            model = this.insurerService.updateInsurer(model);

            if (this.getIsNew()) {

                if (model.isWorkgroupEnable()) {
                    workgroupService.defaultWorkgroup(model);
                }

                insurerAliasService.createDefaultRecord(model);
                breBandService.createDefaultRecord(model);
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String triggerInsurerStatus() throws Exception {
        try {
            Insurer thisObject = this.insurerService.getInsurer(Integer.valueOf(objectId));

            if (thisObject.isStatus()) {
                thisObject.setStatus(false);
            } else {
                thisObject.setStatus(true);
            }

            this.insurerService.updateInsurer(thisObject);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
    }

    public void setInsurerAliasService(InsurerAliasService insurerAliasService) {
        this.insurerAliasService = insurerAliasService;
    }
}
