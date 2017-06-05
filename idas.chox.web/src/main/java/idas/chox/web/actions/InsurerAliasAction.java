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

import idas.chox.core.model.InsurerAlias;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.InsurerAliasViewData;

public class InsurerAliasAction extends BaseAction implements ModelDriven<InsurerAlias>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerAliasAction.class);

    private int insurerId = -1;
    private int insurerAliasId = -1;
    private String insurerAliasName;
    private InsurerAlias model;
    private List<InsurerAliasViewData> insurerAliases;
    private AdminInsurerService adminInsurerService;

    @Override
    public InsurerAlias getModel() {
        return model;
    }

    public void setModel(InsurerAlias model) {
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
            jsonString = mapper.writeValueAsString(insurerAliases);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting insurerAliases to json string.");
        }
        return "{totalCount:" + insurerAliases.size() + ",results:" + jsonString + "}";
    }

    @Override
    public void prepare() throws Exception {

        if (this.insurerAliasId <= 0) {
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
            insurerAliases = new ArrayList<>();

            for (InsurerAlias h : insurerAliasData) {
                insurerAliases.add(new InsurerAliasViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
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

    @Secured ({"ROLE_CHOX_ADMIN"})
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