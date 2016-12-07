package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.SUCCESS;

import idas.chox.core.model.BreBand;
import idas.chox.core.services.BreBandService;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.InsurerBreBandViewData;

public class BreBandDropDownAction extends BaseAction {

    private List<BreBand> breBands;
    private Integer insurerId;
    private BreBandService services;
    private AdminInsurerService adminInsurerService;
    private String jsonData;

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public void setBreBandService(BreBandService services) {
        this.services = services;
    }

    public Integer getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(Integer insurerId) {
        this.insurerId = insurerId;
    }

    public List<BreBand> getBreBands() {
        return this.breBands;
    }

    @Override
    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String execute() throws Exception {
        if ((getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId()) || getUserOrganisationType() == 3) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        try {

            this.breBands = services.getInsurerBreBandsByInsurer(this.insurerId);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }
    
    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String getInsurerBreBands() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Trying to get the insurer BRE Bands for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
        }

        try {
            List<InsurerBreBandViewData> insurerBreBands = new ArrayList<>();
            List<BreBand> insurerBreBandData = adminInsurerService.getInsurerBreBands(this.insurerId);
            for (BreBand h : insurerBreBandData) {
                insurerBreBands.add(new InsurerBreBandViewData(h));
            }

            JSONArray jsonArray = JSONArray.fromObject(insurerBreBands);
            setJsonData("{totalCount:" + insurerBreBands.size() + ",results:" + jsonArray.toString() + "}");

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }
    
    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }
}
