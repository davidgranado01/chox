package idas.chox.web.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.ReasonOfRejectionViewData;

public class ReasonsOfRejectionAction extends BaseAction implements ModelDriven<ReasonOfRejection>, Preparable {
    
//    private static final Logger LOG = LoggerFactory.getLogger(ReasonsOfRejectionAction.class);

    private int insurerId = -1;
    private String activeType;
    private boolean restricted;
    private String type;
    private int reasonOfRejectionId = -1;
    private ReasonOfRejection model;
    private List<ReasonOfRejectionViewData> reasonOfRejectionViewData = new ArrayList<ReasonOfRejectionViewData>();
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
            List<ReasonOfRejection> reasonsOfRejection = reasonOfRejectionService.getInsurerReasonsOfRejection(
                                                                this.insurerId, null, null, null, null);
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
            ReasonOfRejection ror = reasonOfRejectionService.getReasonOfRejection(reasonOfRejectionId);
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
                ReasonOfRejection ror = reasonOfRejectionService.getReasonOfRejection(reasonOfRejectionId);
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
                ReasonOfRejection ror = reasonOfRejectionService.getReasonOfRejection(reasonOfRejectionId);
                if(activeType.equals("gtaActive")) {
                    ror.setGtaActive(!ror.isGtaActive());
                }
                else if(activeType.equals("collaborationActive")) {
                    ror.setCollaborationActive(!ror.isCollaborationActive());
                }
                else if(activeType.equals("insurerVsInsurerActive")) {
                    ror.setInsurerVsInsurerActive(!ror.isInsurerVsInsurerActive());
                }
                else if(activeType.equals("subscriberActive")) {
                    ror.setSubscriberActive(!ror.isSubscriberActive());
                }
                else if(activeType.equals("fixedFeeActive")) {
                    ror.setFixedFeeActive(!ror.isFixedFeeActive());
                }
                else if(activeType.equals("tpiActive")) {
                    ror.setTpiActive(!ror.isTpiActive());
                }
                else if(activeType.equals("insurerUploadActive")) {
                    ror.setInsurerUploadActive(!ror.isInsurerUploadActive());
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
                ReasonOfRejection ror = reasonOfRejectionService.getReasonOfRejection(reasonOfRejectionId);
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
        JSONArray jObject = JSONArray.fromObject(this.getReasonOfRejectionViewData());

        return MessageFormat.format("'{'totalCount:{0},results:{1}'}'", String.valueOf(this.getReasonOfRejectionViewData().size()), jObject.toString());
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
