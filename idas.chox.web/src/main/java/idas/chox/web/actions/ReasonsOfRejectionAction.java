package idas.chox.web.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.ReasonOfRejectionViewData;

public class ReasonsOfRejectionAction extends BaseAction implements ModelDriven<ReasonOfRejection>, Preparable {
        private static final Logger LOG = LoggerFactory.getLogger(ReasonsOfRejectionAction.class);

    private int insurerId = -1;
    private String activeType;
    private boolean restricted;
    private String type;
    private int reasonOfRejectionId = -1;
    private ReasonOfRejection model;
    private List<ReasonOfRejectionViewData> reasonOfRejectionViewData = new ArrayList<>();
    private ReasonOfRejectionService reasonOfRejectionService;
    private AdminInsurerService adminInsurerService;
    
    @Override
    public void prepare() throws Exception {
       model = new ReasonOfRejection();
    }

    @Override
    public ReasonOfRejection getModel() {
        return model;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }
    
    public String getInsurersReasonsOfRejection() {
        try {
            if (activeType != null && activeType.isEmpty()) {
                activeType = null;
            }
            List<ReasonOfRejection> reasonsOfRejection = reasonOfRejectionService.getInsurerReasons(
                                                                this.insurerId, activeType, null, null, null);
            for (ReasonOfRejection ror : reasonsOfRejection) {
                reasonOfRejectionViewData.add(new ReasonOfRejectionViewData(ror));
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String updateReasonOfRejection() {
        try {
            ReasonOfRejection ror = reasonOfRejectionService.getReason(reasonOfRejectionId);
            ror.setLastModifiedDate(DateHelper.getCurrentDate());
            ror.setLastModifiedBy(getAuthenticatedUser());
            ror.setDescription(model.getDescription());
            
            ActionResponse response;
            response = adminInsurerService.addOrUpdateReasonOfRejection(ror);
            setActionResponse(response);
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addReasonOfRejection() {
        try {
            ReasonOfRejection ror = new ReasonOfRejection();
            ror.setInsurer(adminInsurerService.getInsurer(insurerId));
            ror.setLastModifiedDate(DateHelper.getCurrentDate());
            ror.setLastModifiedBy(getAuthenticatedUser());
            ror.setRorName(model.getRorName());
            ror.setDescription(model.getDescription());
            ror.setRestricted(restricted);
            ror.setType(type);
            ror.setGtaActive(model.isGtaActive());
            ror.setCollaborationActive(model.isCollaborationActive());
            ror.setSubscriberActive(model.isSubscriberActive());
            ror.setFixedFeeActive(model.isFixedFeeActive());
            ror.setInsurerUploadActive(model.isInsurerUploadActive());
            ror.setInsurerVsInsurerActive(model.isInsurerVsInsurerActive());
            ror.setTpiActive(model.isTpiActive());
            
            ActionResponse response;
            response = adminInsurerService.addOrUpdateReasonOfRejection(ror);
            setActionResponse(response);
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String deleteReasonOfRejection() throws Exception {
        try {
            if (this.reasonOfRejectionId > 0) {
                ReasonOfRejection ror = reasonOfRejectionService.getReason(reasonOfRejectionId);
                ActionResponse response;
                response = adminInsurerService.deleteReasonOfRejection(ror);
                setActionResponse(response);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String updateReasonOfRejectionActive() throws Exception {
        try {
            if (this.reasonOfRejectionId > 0 && activeType != null) {
                ReasonOfRejection ror = reasonOfRejectionService.getReason(reasonOfRejectionId);
                switch (activeType) {
                    case "gtaActive":
                        ror.setGtaActive(!ror.isGtaActive());
                        break;
                    case "collaborationActive":
                        ror.setCollaborationActive(!ror.isCollaborationActive());
                        break;
                    case "insurerVsInsurerActive":
                        ror.setInsurerVsInsurerActive(!ror.isInsurerVsInsurerActive());
                        break;
                    case "subscriberActive":
                        ror.setSubscriberActive(!ror.isSubscriberActive());
                        break;
                    case "fixedFeeActive":
                        ror.setFixedFeeActive(!ror.isFixedFeeActive());
                        break;
                    case "tpiActive":
                        ror.setTpiActive(!ror.isTpiActive());
                        break;
                    case "insurerUploadActive":
                        ror.setInsurerUploadActive(!ror.isInsurerUploadActive());
                        break;
                    default:
                        break;
                }
                ActionResponse response;
                response = adminInsurerService.updateReasonOfRejectionActive(ror);
                setActionResponse(response);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    
    @Secured ({"ROLE_CHOX_ADMIN"})
    public String updateReasonOfRejectionRestricted() throws Exception {
        try {            
            if (this.reasonOfRejectionId > 0) {
                ReasonOfRejection ror = reasonOfRejectionService.getReason(reasonOfRejectionId);
                ror.setRestricted(!ror.isRestricted());
                ActionResponse response;
                response = adminInsurerService.updateReasonOfRejectionRestricted(ror);
                setActionResponse(response);
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }
    
    public String getJsonData() {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        List<ReasonOfRejectionViewData> vd = getReasonOfRejectionViewData();
        try {
            jsonString = mapper.writeValueAsString(vd);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting ReasonOfRejectionViewData to json string.");
        }

        return MessageFormat.format("'{'totalCount:{0},results:{1}'}'", String.valueOf(vd.size()), jsonString);
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }


    public List<ReasonOfRejectionViewData> getReasonOfRejectionViewData() {
        return reasonOfRejectionViewData;
    }

    public ReasonOfRejectionService getReasonOfRejectionService() {
        return reasonOfRejectionService;
    }

    public void setReasonOfRejectionService(
            ReasonOfRejectionService reasonOfRejectionService) {
        this.reasonOfRejectionService = reasonOfRejectionService;
    }

    public void setModel(ReasonOfRejection model) {
        this.model = model;
    }

    public int getReasonOfRejectionId() {
        return reasonOfRejectionId;
    }

    public void setReasonOfRejectionId(int reasonOfRejectionId) {
        this.reasonOfRejectionId = reasonOfRejectionId;
    }

    public boolean isRestricted() {
        return restricted;
    }

    public void setRestricted(boolean restricted) {
        this.restricted = restricted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AdminInsurerService getAdminInsurerService() {
        return adminInsurerService;
    }

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }

    public String getActiveType() {
        return activeType;
    }

    public void setActiveType(String activeType) {
        this.activeType = activeType;
    }

}
