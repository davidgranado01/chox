package idas.chox.web.actions;

import idas.chox.web.viewdata.InsurerViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.Insurer;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.annotation.Secured;

public class InsurerAction extends BaseAction implements ModelDriven<Insurer>, Preparable {
    private static final Logger LOG = LoggerFactory.getLogger(InsurerAction.class);
    private List<InsurerViewData> insurer;
    private String objectId;
    private Insurer model;
    private Integer tabIndex;
    private AdminInsurerService adminInsurerService;
    private int relatedInsurerId;
    private int claimOwnerIdField;
    private int workgroupIdField;

    public int getClaimOwnerIdField() {
        return this.model.getTpiClaimOwner() != null ? this.model.getTpiClaimOwner().getId() : 0;
    }

    public String getClaimOwnerIdFieldName() {
        return this.model.getTpiClaimOwner() != null ? this.model.getTpiClaimOwner().getDisplayName() : "--- Please Select ---";
    }

    public void setClaimOwnerIdField(int claimOwnerIdField) {
        this.claimOwnerIdField = claimOwnerIdField;
    }

    public int getWorkgroupIdField() {
        return this.model.getTpiWorkgroup() != null ? this.model.getTpiWorkgroup().getId() : 0;
    }

    public String getWorkgroupIdFieldName() {
        return this.model.getTpiWorkgroup() != null ? this.model.getTpiWorkgroup().getName() : "--- Please Select ---";
    }

    public void setWorkgroupIdField(int workgroupIdField) {
        this.workgroupIdField = workgroupIdField;
    }

    public boolean getIsNew() {

        if (this.objectId != null && !objectId.equalsIgnoreCase("")) {
            if (Integer.valueOf(objectId) <= 0) {
                // set default support procedure
                model.setSupportProcedure("/chox_support.html");
                return true;
            }
        }

        return false;
    }

    @Override
    public Insurer getModel() {
        return model;
    }

    public void setModel(Insurer model) {
        this.model = model;
    }

    @Secured({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurer);
        return "{totalCount:" + this.insurer.size() + ",results:" + jObject.toString() + "}";
    }

    @Override
    public void prepare() throws Exception {
        try {
            model = new Insurer();
            if (this.objectId != null && !objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminInsurerService.getInsurer(Integer.valueOf(objectId));
                }
            }

        } catch (Exception ex) {
            handleException(ex);

        }
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    @Override
    public boolean getInsurerIsWorkgroupEnabled() {
        return model.isWorkgroupEnable();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTION">

    @Secured({"ROLE_CHOX_ADMIN"})
    public String getInsurers() {

        try {

            List<Insurer> insurerData = adminInsurerService.getInsurers();
            insurer = new ArrayList<InsurerViewData>();

            for (Insurer h : insurerData) {
                insurer.add(new InsurerViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG"})
    public String updateInsurer() {
        if (getIsInsurer() && model.getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue()) {
            throw new AccessDeniedException("Cannot update other Insurer");
        }

        try {

            model.setRelatedInsurer(this.adminInsurerService.getInsurer(relatedInsurerId));
            if (this.adminInsurerService.getWebuserById(claimOwnerIdField) != null) {
                model.setTpiClaimOwner(this.adminInsurerService.getWebuserById(claimOwnerIdField));
            }
            if (this.adminInsurerService.getWorkgroup(workgroupIdField) != null) {
                model.setTpiWorkgroup(this.adminInsurerService.getWorkgroup(workgroupIdField));
            }
            ActionResponse response = adminInsurerService.updateInsurer(model, getIsNew());
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_MNG"})
    public String triggerInsurerStatus() throws Exception {
        if (getIsInsurer() && model.getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue()) {
            throw new AccessDeniedException("Cannot update other Insurer");
        }

        try {

            if (objectId != null && !objectId.equalsIgnoreCase("")) {
                adminInsurerService.triggerInsurerStatus(Integer.valueOf(objectId));
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
    // </editor-fold>

    public List<Insurer> getRelatedInsurers() {
        List<Insurer> relatedInsurer = adminInsurerService.getInsurers();
        relatedInsurer.remove(model);
        return relatedInsurer;

    }

    public int getRelatedInsurerId() {
        return this.model.getRelatedInsurer() != null ? this.model.getRelatedInsurer().getId() : 0;
    }

    public void setRelatedInsurerId(int id) {
        this.relatedInsurerId = id;
    }
}
