package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.service.admin.AdminChorganisationService;
import idas.chox.web.viewdata.ChorganisationViewData;
import idas.chox.web.viewdata.InsurerChorganisationViewData;

public class InsurerChorganisationAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerChorganisationAction.class);

    private int insurerId;
    private int insurerChorganisationId;
    private int chorganisationId = -1;
    private String jsonRecords;
    private AdminInsurerService adminInsurerService;
    private AdminChorganisationService adminChorganisationService;



    @Secured ({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    public void setJsonData(Object object, Integer recordSize) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting object to json string.");
        }
        this.jsonRecords = "{totalCount:" + recordSize + ",results:" + jsonString + "}";
    }

    public String getJsonData() {
        return this.jsonRecords;
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getInsurerChorganisationId() {
        return insurerChorganisationId;
    }

    public void setInsurerChorganisationId(int insurerChorganisationId) {
        this.insurerChorganisationId = insurerChorganisationId;
    }

    public int getChorganisationId() {
        return chorganisationId;
    }

    public void setChorganisationId(int chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    public String getTpiInsurerChorganisation() {

        try {
            List<InsurerChorganisation> chorganisationsData = adminChorganisationService.getTpiInsurerChorganisations(this.chorganisationId);
            List<InsurerChorganisationViewData> insurerChorganisations = parsetChoViewDataList(chorganisationsData);
            setJsonData(insurerChorganisations, insurerChorganisations.size());
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String getSelectedChorganisations() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        try {
            List<InsurerChorganisation> chorganisationsData = adminInsurerService.getInsurerChorganisations(this.insurerId);
            List<InsurerChorganisationViewData> insurerChorganisations = parsetChoViewDataList(chorganisationsData);
            setJsonData(insurerChorganisations, insurerChorganisations.size());

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    public String getAvailableChorganisations() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        try {
            List<Chorganisation> chorganisationData = this.adminInsurerService.getAvailableChorganisationsByInsurer(this.insurerId);
            List<ChorganisationViewData> credithireorganisation = new ArrayList<>();

            for (Chorganisation h : chorganisationData) {
                credithireorganisation.add(new ChorganisationViewData(h));
            }

            setJsonData(credithireorganisation, credithireorganisation.size());
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    private List<InsurerChorganisationViewData> parsetChoViewDataList(List<InsurerChorganisation> objects) {
        List<InsurerChorganisationViewData> insurerChorgs = new ArrayList<>();
        for (InsurerChorganisation h : objects) {
            insurerChorgs.add(new InsurerChorganisationViewData(h));
        }
        return insurerChorgs;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addNewInsurerChorganisation() {

        try {

            if (this.insurerId > 0 && this.chorganisationId > 0) {
                ActionResponse response;
                response = adminInsurerService.addNewInsurerChorganisation(this.insurerId, this.chorganisationId);
                setActionResponse(response);
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String removeInsurerChorganisation() {

        try {

            if (this.insurerChorganisationId > 0) {

                ActionResponse response;
                response = adminInsurerService.removeInsurerChorganisation(this.insurerChorganisationId);
                setActionResponse(response);
            }

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

    public void setAdminChorganisationService(AdminChorganisationService adminChorganisationService) {
        this.adminChorganisationService = adminChorganisationService;
    }
    // </editor-fold>
}
