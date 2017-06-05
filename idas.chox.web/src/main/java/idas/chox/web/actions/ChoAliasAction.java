package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.ChorganisationAlias;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminChorganisationService;
import idas.chox.web.viewdata.ChoAliasViewData;

public class ChoAliasAction extends BaseAction implements ModelDriven<ChorganisationAlias>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(ChoAliasAction.class);

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
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(choAliases);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting choAliases to json string.");
        }
        return "{totalCount:" + this.choAliases.size() + ",results:" + jsonString + "}";
    }

    @Override
    public void prepare() throws Exception {

        if (this.choAliasId <= 0) {
            model = new ChorganisationAlias();
        } else {
            model = adminChorganisationService.getChorganisationAlias(choAliasId);
        }
    }

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

    public String getChoAlias() {

        try {

            List<ChorganisationAlias> choAliasData = adminChorganisationService.getChorganisationAliases(this.choId);
            choAliases = new ArrayList<>();

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

    public void setAdminChorganisationService(AdminChorganisationService adminChorganisationService) {
        this.adminChorganisationService = adminChorganisationService;
    }
}