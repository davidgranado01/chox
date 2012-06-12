package idas.chox.web.actions;

import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.util.DateHelper;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.ReasonOfRejectionViewData;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class ReasonsOfRejectionAction extends BaseAction implements ModelDriven<ReasonOfRejection>, Preparable {
    
    private static final Logger LOG = LoggerFactory.getLogger(ReasonsOfRejectionAction.class);

    private int insurerId = -1;
    private String reasonOfRejectionName;
    private String reasonOfRejectionDesc;
    private boolean restricted;
    private String type;
    private boolean status;
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
            List<ReasonOfRejection> reasonsOfRejection = reasonOfRejectionService.getInsurerReasonsOfRejection(this.insurerId, null, null, null);
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
            if (!getIsChoxAdmin()) {
                LOG.error("Trying to update a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT): {}");
                throw new AccessDeniedException("Trying to update a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            ReasonOfRejection ror = reasonOfRejectionService.getReasonOfRejection(reasonOfRejectionId);
            ror.setLastModifiedDate(DateHelper.getCurrentDate());
            ror.setLastModifiedBy(getAuthenticatedUser());
            ror.setDescription(reasonOfRejectionDesc);
            
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
            if (!getIsChoxAdmin()) {
                LOG.error("Trying to add a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT): {}");
                throw new AccessDeniedException("Trying to add a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            ReasonOfRejection ror = new ReasonOfRejection();
            ror.setInsurer(adminInsurerService.getInsurer(insurerId));
            ror.setLastModifiedDate(DateHelper.getCurrentDate());
            ror.setLastModifiedBy(getAuthenticatedUser());
            ror.setName(reasonOfRejectionName);
            ror.setDescription(reasonOfRejectionDesc);
            ror.setRestricted(restricted);
            ror.setType(type);
            ror.setStatus(status);
            
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
            if (!getIsChoxAdmin()) {
                LOG.error("Trying to delete a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT): {}");
                throw new AccessDeniedException("Trying to delete a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            
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
            if (!getIsChoxAdmin()) {
                LOG.error("Trying to update a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT): {}");
                throw new AccessDeniedException("Trying to update a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            
            if (this.reasonOfRejectionId > 0) {
                ReasonOfRejection ror = reasonOfRejectionService.getReasonOfRejection(reasonOfRejectionId);
                ror.setStatus(!ror.isStatus());
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
            if (!getIsChoxAdmin()) {
                LOG.error("Trying to update a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT): {}");
                throw new AccessDeniedException("Trying to update a Reason Of Rejection for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            
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
        return "{totalCount:" + this.getReasonOfRejectionViewData().size() + ",results:" + jObject.toString() + "}";
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getReasonOfRejectionName() {
        return reasonOfRejectionName;
    }

    public void setReasonOfRejectionName(String reasonOfRejectionName) {
        this.reasonOfRejectionName = reasonOfRejectionName;
    }

    public String getReasonOfRejectionDesc() {
        return reasonOfRejectionDesc;
    }

    public void setReasonOfRejectionDesc(String reasonOfRejectionDesc) {
        this.reasonOfRejectionDesc = reasonOfRejectionDesc;
    }

    public List<ReasonOfRejectionViewData> getReasonOfRejectionViewData() {
        return reasonOfRejectionViewData;
    }

    public void setReasonOfRejectionViewData(
            List<ReasonOfRejectionViewData> reasonOfRejectionViewData) {
        this.reasonOfRejectionViewData = reasonOfRejectionViewData;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public AdminInsurerService getAdminInsurerService() {
        return adminInsurerService;
    }

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }
    
}
