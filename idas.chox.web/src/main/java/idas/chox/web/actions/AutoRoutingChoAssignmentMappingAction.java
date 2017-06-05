package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import static com.opensymphony.xwork2.Action.SUCCESS;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.services.ChorganisationService;
import idas.chox.web.viewdata.ChorganisationViewData;
import org.springframework.security.access.annotation.Secured;

/**
 *
 * @author John
 */
public class AutoRoutingChoAssignmentMappingAction extends BaseAction {
    private static final Logger LOG = LoggerFactory.getLogger(AutoRoutingChoAssignmentMappingAction.class);
    private int insurerId = -1;
    private int workgroupId = -1;
    private int chorganisationId = -1;
    private String jsonData;
    private ChorganisationService chorganisationService;

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public void setChorganisationId(int chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setJsonData(Object object, Integer recordSize) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting object to json string.");
        }
        this.jsonData = "{totalCount:" + recordSize + ",results:" + jsonString + "}";
    }
    
    public String getJsonData() {
        return this.jsonData;
    }

    public String getChorganisationsWithAutoRoutingWorkgroupMapping() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Trying to add CHO mapping for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
        }

        try {
            List<Chorganisation> choOrganisations = chorganisationService.getChorganisationsWithAutoRoutingWorkgroupMapping(workgroupId);
            List<ChorganisationViewData> mappedChos = getChoViewDataList(choOrganisations);
            setJsonData(mappedChos, mappedChos.size());

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String addChorganisationWorkgroupMapping() {
       try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != -1 && this.insurerId != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to add CHO/Workgroup auto-routing mapping for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            chorganisationService.addChorganisationAutoRoutingWorkgroupMapping(chorganisationId, workgroupId);
       } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String removeChorganisationWorkgroupMapping() {
       try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != -1 && this.insurerId != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to add CHO/Workgroup auto-routing mapping for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            chorganisationService.removeChorganisationAutoRoutingWorkgroupMapping(chorganisationId, workgroupId);
       } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String getChorganisationsByInsurerIdWithoutWorkgroupMapping() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Trying to add CHO mapping for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
        }

        List<ChorganisationViewData> choList = new ArrayList<>();

        if (this.insurerId > 0) {
            List<Chorganisation> chorganisations = chorganisationService.getChorganisationsByInsurerWithoutWorkgroupMapping(
                                                                            insurerId);
            for (Chorganisation object : chorganisations) {
                // If Insurer admin logged in then show only the active cho in the 'Available CHO' list of 'CHO Mapping'.
                // But Chox Admin can see all the active and inactive cho in the 'Available CHO' list of 'CHO Mapping'. 
                if (getUserOrganisationType() == 2) {
                    if (object.isStatus()) {
                        choList.add(new ChorganisationViewData(object));
                    }
                } else {
                    choList.add(new ChorganisationViewData(object));
                }
            }
            setJsonData(choList, choList.size());
        }

        return SUCCESS;
    }

    private List<ChorganisationViewData> getChoViewDataList(List<Chorganisation> chos) {
        List<ChorganisationViewData> chorganisationViewData = new ArrayList<>();
        for (Chorganisation cho : chos) {
            chorganisationViewData.add(new ChorganisationViewData(cho));
        }
        return chorganisationViewData;
    }


}
