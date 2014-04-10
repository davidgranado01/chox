package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.InsurerAutomaticRoutingViewData;
import org.hibernate.StaleObjectStateException;

public class InsurerAutomaticRoutingAction extends BaseAction implements ModelDriven<AutomaticRouting>, Preparable {

    static final Logger LOG = LoggerFactory.getLogger(InsurerAutomaticRoutingAction.class);

    private int insurerId = -1;
    private int workgroupId = -1;
    private int automaticRoutingId = -1;
    private String objectId;
    private AutomaticRouting model;
    private AutomaticRoutingService automaticRoutingService;
    
    private List<InsurerAutomaticRoutingViewData> insurerAutomaticRoutings = new ArrayList<InsurerAutomaticRoutingViewData>();
    private AdminInsurerService adminInsurerService;


    private boolean workgroupEnableFlg;
    private boolean autoRoutingEnableFlg;
    private boolean autoRoutingPriceFlg;

    @Override
    public AutomaticRouting getModel() {
        return model;
    }

    public void setModel(AutomaticRouting model) {
        this.model = model;
    }

    public AutomaticRoutingService getAutomaticRoutingService() {
        return automaticRoutingService;
    }

    public void setAutomaticRoutingService(AutomaticRoutingService automaticRoutingService) {
        this.automaticRoutingService = automaticRoutingService;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerAutomaticRoutings);
        return "{totalCount:" + this.insurerAutomaticRoutings.size() + ",results:" + jObject.toString() + "}";
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    @Override
    public void prepare() throws Exception {

        LOG.debug("InsurerAutomaticRouting ..... Prepare");

        try {

            model = new AutomaticRouting();

            if (this.objectId != null && !this.objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminInsurerService.getInsurerAutomaticRouting(Integer.valueOf(this.objectId));

                }
            }

        } catch (Exception ex) {
            handleException(ex);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getWorkgroupId() {
        return workgroupId;
    }

    public void setWorkgroupId(int workgroupId) {
        this.workgroupId = workgroupId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getAutomaticRoutingId() {
        return automaticRoutingId;
    }

    public void setAutomaticRoutingId(int automaticRoutingId) {
        this.automaticRoutingId = automaticRoutingId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    @Override
    public String execute() {
        return SUCCESS;
    }

    public String getInsurerAutomaticRouting() {

        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        LOG.debug("InsurerAutomaticRouting .....");

        try {

            List<AutomaticRouting> automaticRoutingData = adminInsurerService.getInsurerAutomaticRoutings(this.insurerId);
            for (AutomaticRouting h : automaticRoutingData) {
                insurerAutomaticRoutings.add(new InsurerAutomaticRoutingViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String editAutomaticRoutingDetail() {

        try {
            if (automaticRoutingService.getAutomaticRouting(automaticRoutingId) != null) {
                AutomaticRouting automaticRouting = adminInsurerService.getInsurerAutomaticRouting(this.automaticRoutingId);
                automaticRouting.setExpression(model.getExpression());
                adminInsurerService.updateAutomaticRouting(automaticRouting);
            } else {
                throw new Exception("Record was updated by another transaction/user, please try again.",
                        new StaleObjectStateException(AutomaticRouting.class.getSimpleName().concat("Version"), 0));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public List getAvailableWorkgroups() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Illegal access detected.");
        }

        List items = new ArrayList<IdLookupItem>();
        try {
            items = adminInsurerService.getAvailableWorkgroups(this.insurerId, true);
        } catch (Exception ex) {
            handleException(ex);
        }

        return items;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String deleteAutomaticRoutingDetail() {
        try {
            if (automaticRoutingService.getAutomaticRouting(automaticRoutingId) != null) {
                ActionResponse response;
                response = adminInsurerService.deleteAutomaticRouting(this.automaticRoutingId);
                setActionResponse(response);
            } else {
                throw new Exception("Record was updated by another transaction/user, please try again.",
                        new StaleObjectStateException(AutomaticRouting.class.getSimpleName().concat("Version"), 0));
            }
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }
        return SUCCESS;
    }

    @Secured ({"ROLE_CHOX_ADMIN"})
    public String addNewAutomaticRoutingDetail() {
        try {
            if (automaticRoutingService.getAutomaticRouting(insurerId, workgroupId) == null) {
                ActionResponse response = adminInsurerService.addNewAutomaticRouting(this.insurerId, this.workgroupId, model.getExpression());
                setActionResponse(response);
            } else {
                throw new Exception("Record was updated by another transaction/user, please try again.",
                        new StaleObjectStateException(AutomaticRouting.class.getSimpleName().concat("Version"), 0));
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

    /**
     * @return the workgroupEnableFlg
     */
    public boolean isWorkgroupEnableFlg() {
        return adminInsurerService.getInsurer(this.insurerId).isWorkgroupEnable();
    }

    /**
     * @param workgroupEnableFlg the workgroupEnableFlg to set
     */
    public void setWorkgroupEnableFlg(boolean workgroupEnableFlg) {
        this.workgroupEnableFlg = workgroupEnableFlg;
    }

    /**
     * @return the autoRoutingEnableFlg
     */
    public boolean isAutoRoutingEnableFlg() {
        return adminInsurerService.getInsurer(this.insurerId).isAutoRoutingEnable();
    }

    /**
     * @param autoRoutingEnableFlg the autoRoutingEnableFlg to set
     */
    public void setAutoRoutingEnableFlg(boolean autoRoutingEnableFlg) {
        this.autoRoutingEnableFlg = autoRoutingEnableFlg;
    }

    /**
     * @return the autoRoutingPriceFlg
     */
    public boolean isAutoRoutingPriceFlg() {
        return adminInsurerService.getInsurer(this.insurerId).isAutoRoutingEnablePrice();
    }

    /**
     * @param autoRoutingPriceFlg the autoRoutingPriceFlg to set
     */
    public void setAutoRoutingPriceFlg(boolean autoRoutingPriceFlg) {
        this.autoRoutingPriceFlg = autoRoutingPriceFlg;
    }

    
    
    // </editor-fold>
}
