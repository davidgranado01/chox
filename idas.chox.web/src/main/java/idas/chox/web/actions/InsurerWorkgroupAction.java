package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import org.apache.commons.text.StringEscapeUtils;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Workgroup;
import idas.chox.core.services.InsurerService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.WorkgroupViewData;

public class InsurerWorkgroupAction extends BaseAction implements ModelDriven<Workgroup>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerWorkgroupAction.class);

    protected int insurerId;
    protected int workgroupId = -1;
    protected String workgroupName;
    protected String workgroupSite;
    protected String workgroupTeam;
    private InsurerService insurerService;
    private Workgroup model;
    protected List<WorkgroupViewData> workgroups;
    private AdminInsurerService adminInsurerService;

    @Override
    public Workgroup getModel() {
        return model;
    }

    public void setModel(Workgroup model) {
        this.model = model;
    }

    @Override
    public void prepare() throws Exception {
        if (workgroupId <= 0) {
            model = new Workgroup();
        } else {
            model = adminInsurerService.getWorkgroup(workgroupId);
        }
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(workgroups);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting workgroups to json string.");
        }
        return "{totalCount:" + workgroups.size() + ",results:" + jsonString + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

    public String getWorkgroupSite() {
        return workgroupSite;
    }

    public void setWorkgroupSite(String workgroupSite) {
        this.workgroupSite = workgroupSite;
    }

    public String getWorkgroupTeam() {
        return workgroupTeam;
    }

    public void setWorkgroupTeam(String workgroupTeam) {
        this.workgroupTeam = workgroupTeam;
    }

    public List<WorkgroupViewData> getWorkgroups() {
        return workgroups;
    }

    public void setWorkgroups(List<WorkgroupViewData> workgroups) {
        this.workgroups = workgroups;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    public String getInsurerWorkgroups() {

        try {

            List<Workgroup> workgroupDatas = adminInsurerService.getInsurerWorkgroups(this.insurerId);
            this.workgroups = new ArrayList<>();
            for (Workgroup h : workgroupDatas) {
                this.workgroups.add(new WorkgroupViewData(h));
            }

        } catch (Exception ex) {
            LOG.error("Exception thrown getting workgroups for insurer {}: {}", insurerId, ex.getMessage());
//            ex.printStackTrace();
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String addNewInsurerWorkgroup() {
        LOG.debug("Adding new insurer workgroup.");
        try {
            if ( getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId()) {
                LOG.error("Trying to create an insurer workgroup for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
                throw new AccessDeniedException("Trying to create an insurer workgroup for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            if (!insurerService.getInsurer(insurerId).isWorkgroupEnable()) {
                LOG.error("Trying to create an insurer workgroup for an insurer where workgroups are disabled (POSSIBLE HACK ATTEMPT)");
                throw new AccessDeniedException("Trying to create an insurer workgroup for an insurer where workgroups are disabled");
            }
            if (!this.workgroupName.equals(StringEscapeUtils.unescapeHtml4(Jsoup.clean(this.workgroupName, Whitelist.none())))) {
                throw new Exception("Illegal characters found in Workgroup name");
            }
            if (!this.workgroupSite.equals(StringEscapeUtils.unescapeHtml4(Jsoup.clean(this.workgroupSite, Whitelist.none())))) {
                throw new Exception("Illegal characters found in Workgroup Site");
            }
            if (!this.workgroupTeam.equals(StringEscapeUtils.unescapeHtml4(Jsoup.clean(this.workgroupTeam, Whitelist.none())))) {
                throw new Exception("Illegal characters found in Workgroup Team");
            }
            model.setName(this.workgroupName);
            model.setSite(this.workgroupSite);
            model.setTeam(this.workgroupTeam);
            ActionResponse response = adminInsurerService.addNewInsurerWorkgroup(model, this.insurerId);
            setActionResponse(response);
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String removeInsurerWorkgroup() {

        if (this.insurerId > 0 && this.workgroupId > 0) {

            try {
            if ( getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId()) {
                throw new AccessDeniedException("Trying to remove an insurer workgroup for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }

                ActionResponse response = adminInsurerService.removeInsurerWorkgroup(this.workgroupId, this.insurerId);
                setActionResponse(response);

            } catch (Exception ex) {
                handleException(ex);
                return ERROR;
            }
        }

        return SUCCESS;
    }

    public String triggerInsurerWorkgroupStatus() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        if (this.insurerId > 0 && this.workgroupId > 0) {

            try {

                ActionResponse response = adminInsurerService.triggerInsurerWorkgroupStatus(model, this.insurerId);
                setActionResponse(response);

            } catch (Exception ex) {
                handleException(ex);
                return ERROR;
            }
        }

        return SUCCESS;

    }

    public String triggerInsurerWorkgroupStpExcluded() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        if (this.insurerId > 0 && this.workgroupId > 0) {

            try {

                ActionResponse response = adminInsurerService.triggerInsurerWorkgroupStpExcluded(model, this.insurerId);
                setActionResponse(response);

            } catch (Exception ex) {
                handleException(ex);
                return ERROR;
            }
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
