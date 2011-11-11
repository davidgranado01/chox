package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.ChorganisationAlias;
import org.springframework.security.annotation.Secured;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminChorganisationService;
import idas.chox.web.viewdata.ChoAliasViewData;

public class ChoAliasAction extends BaseAction implements ModelDriven<ChorganisationAlias>, Preparable {

    private int choId = -1;
    private int choAliasId = -1;
    private String choAliasName;
    private ChorganisationAlias model;
    private List<ChoAliasViewData> choAliases;
    private AdminChorganisationService adminChorganisationService;

    @Override
    public ChorganisationAlias getModel() {
        return model;
    }

    public void setModel(ChorganisationAlias model) {
        this.model = model;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.choAliases);
        return "{totalCount:" + this.choAliases.size() + ",results:" + jObject.toString() + "}";
    }

    @Override
    public void prepare() throws Exception {

        if (Integer.valueOf(this.choAliasId) <= 0) {
            model = new ChorganisationAlias();
        } else {
            model = adminChorganisationService.getChorganisationAlias(choAliasId);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getChoAliasId() {
        return choAliasId;
    }

    public void setChoAliasId(int choAliasId) {
        this.choAliasId = choAliasId;
    }

    public String getChoAliasName() {
        return choAliasName;
    }

    public void setChoAliasName(String choAliasName) {
        this.choAliasName = choAliasName;
    }

    public List<ChoAliasViewData> getChoAliases() {
        return choAliases;
    }

    public void setChoAliases(List<ChoAliasViewData> choAliases) {
        this.choAliases = choAliases;
    }

    public int getChoId() {
        return choId;
    }

    public void setChoId(int choId) {
        this.choId = choId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    public String getChoAlias() {

        try {

            List<ChorganisationAlias> choAliasData = adminChorganisationService.getChorganisationAliases(this.choId);
            choAliases = new ArrayList<ChoAliasViewData>();

            for (ChorganisationAlias h : choAliasData) {
                choAliases.add(new ChoAliasViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addNewChoAlias() {

        try {

            ActionResponse response;
            response = adminChorganisationService.addNewChoAlias(this.choId, this.choAliasName);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String removeChoAlias() {

        try {

            ActionResponse response;
            response = adminChorganisationService.removeChoAlias(model);
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setAdminChorganisationService(AdminChorganisationService adminChorganisationService) {
        this.adminChorganisationService = adminChorganisationService;
    }
    // </editor-fold>
}