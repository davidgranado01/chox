package idas.chox.web.actions;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.IdLookupItem;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.InsurerAutomaticRoutingViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class InsurerAutomaticRoutingAction extends BaseAction implements ModelDriven<AutomaticRouting>, Preparable {

    private int insurerId = -1;
    private int workgroupId = -1;
    private int automaticRoutingId = -1;
    private String objectId;
    private AutomaticRouting model;
    private List<InsurerAutomaticRoutingViewData> insurerAutomaticRoutings = new ArrayList<InsurerAutomaticRoutingViewData>();
    private AdminInsurerService adminInsurerService;

    public AutomaticRouting getModel() {
        return model;
    }

    public void setModel(AutomaticRouting model) {
        this.model = model;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerAutomaticRoutings);
        return "{totalCount:" + this.insurerAutomaticRoutings.size() + ",results:" + jObject.toString() + "}";
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public void prepare() throws Exception {

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

    public String editAutomaticRoutingDetail() {

        try {

            AutomaticRouting automaticRouting = adminInsurerService.getInsurerAutomaticRouting(this.automaticRoutingId);
            automaticRouting.setExpression(model.getExpression());
            adminInsurerService.updateAutomaticRouting(automaticRouting);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public List getAvailableWorkgroups() {

        List items = new ArrayList<IdLookupItem>();
        try {
            items = adminInsurerService.getAvailableWorkgroups(this.insurerId);
        } catch (Exception ex) {
            handleException(ex);
        }

        return items;
    }

    public String deleteAutomaticRoutingDetail() {
        ActionResponse response;
        response = adminInsurerService.deleteAutomaticRouting(this.automaticRoutingId);
        setActionResponse(response);
        return SUCCESS;
    }

    public String addNewAutomaticRoutingDetail() {
        ActionResponse response;
        response = adminInsurerService.addNewAutomaticRouting(this.insurerId, this.workgroupId, model.getExpression());
        setActionResponse(response);
        return SUCCESS;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }
    // </editor-fold>
}
