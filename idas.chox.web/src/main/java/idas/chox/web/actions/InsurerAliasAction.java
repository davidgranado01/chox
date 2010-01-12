package idas.chox.web.actions;

import idas.chox.web.viewdata.InsurerAliasViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.InsurerAlias;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;

public class InsurerAliasAction extends BaseAction implements ModelDriven<InsurerAlias>, Preparable {

    private int insurerId = -1;
    private int insurerAliasId = -1;
    private String insurerAliasName;
    private InsurerAlias model;
    private List<InsurerAliasViewData> insurerAliases;
    private AdminInsurerService adminInsurerService;

    public InsurerAlias getModel() {
        return model;
    }

    public void setModel(InsurerAlias model) {
        this.model = model;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerAliases);
        return "{totalCount:" + this.insurerAliases.size() + ",results:" + jObject.toString() + "}";
    }

    public void prepare() throws Exception {

        if (Integer.valueOf(this.insurerAliasId) <= 0) {
            model = new InsurerAlias();
        } else {
            model = adminInsurerService.getInsurerAlias(this.insurerAliasId);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
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

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    public String getInsurerAlias() {

        try {

            List<InsurerAlias> insurerAliasData = adminInsurerService.getInsurerAliases(this.insurerId);
            insurerAliases = new ArrayList<InsurerAliasViewData>();

            for (InsurerAlias h : insurerAliasData) {
                insurerAliases.add(new InsurerAliasViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String addNewInsurerAlias() {

        try {

            ActionResponse response;
            response = adminInsurerService.addNewInsurerAlias(this.insurerId, this.insurerAliasName);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeInsurerAlias() {

        try {

            ActionResponse response;
            response = adminInsurerService.removeInsurerAlias(model);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }
    // </editor-fold>
}