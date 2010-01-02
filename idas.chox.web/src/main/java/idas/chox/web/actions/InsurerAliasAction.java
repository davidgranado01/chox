package idas.chox.web.actions;

import idas.chox.web.viewdata.InsurerAliasViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerService;
import idas.chox.service.ActionResponse;

public class InsurerAliasAction extends BaseAction implements ModelDriven<InsurerAlias>, Preparable {

    protected int insurerId = -1;
    protected int insurerAliasId = -1;
    protected String insurerAliasName;
    private InsurerAlias model;
    protected List<InsurerAliasViewData> insurerAliases;
    protected InsurerAliasService insurerAliasService;
    protected InsurerService insurerService;

    public int getInsurerAliasId() {
        return insurerAliasId;
    }

    public void setInsurerAliasId(int insurerAliasId) {
        this.insurerAliasId = insurerAliasId;
    }

    public String getInsurerAliasName() {
        return insurerAliasName;
    }

    public void setInsurerAliasName(String insurerAliasName) {
        this.insurerAliasName = insurerAliasName;
    }

    public List<InsurerAliasViewData> getInsurerAliases() {
        return insurerAliases;
    }

    public void setInsurerAliases(List<InsurerAliasViewData> insurerAliases) {
        this.insurerAliases = insurerAliases;
    }

    public InsurerAlias getModel() {
        return model;
    }

    public void setModel(InsurerAlias model) {
        this.model = model;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerAliases);
        return "{totalCount:" + this.insurerAliases.size() + ",results:" + jObject.toString() + "}";
    }

    @Override
    public String execute() {

        try {

            List<InsurerAlias> insurerAliasData = this.insurerAliasService.getInsurerAliasesByInsurer(insurerId);
            insurerAliases = new ArrayList<InsurerAliasViewData>();

            for (InsurerAlias h : insurerAliasData) {
                insurerAliases.add(new InsurerAliasViewData(h));
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public void prepare() throws Exception {

        if (Integer.valueOf(insurerAliasId) <= 0) {
            model = new InsurerAlias();
        } else {
            model = insurerAliasService.getInsurerAlias(insurerAliasId);
        }
    }

    public String addNewInsurerAlias() {

        try {

            if (!insurerAliasService.isInsurerAliasExist(insurerId, insurerAliasName)) {

                model = new InsurerAlias();
                model.setAliasName(insurerAliasName);
                model.setInsurer(insurerService.getInsurer(insurerId));
                insurerAliasService.saveInsurerAlias(model);

                getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Alias '" + insurerAliasName + "' has been created");

            } else {

                getActionResponse().AddError("Alias '" + insurerAliasName + "' already exists");

            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeInsurerAlias() {

        try {

            insurerAliasService.deleteInsurerAlias(model);
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Alias '" + model.getAliasName() + "' has been removed");

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public void setInsurerAliasService(InsurerAliasService insurerAliasService) {
        this.insurerAliasService = insurerAliasService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }
}
