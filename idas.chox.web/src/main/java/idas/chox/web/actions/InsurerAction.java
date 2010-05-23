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

public class InsurerAction extends BaseAction implements ModelDriven<Insurer>, Preparable {

    private List<InsurerViewData> insurer;
    private String objectId;
    private Insurer model;
    private Integer tabIndex;
    private AdminInsurerService adminInsurerService;

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

    public Insurer getModel() {
        return model;
    }

    public void setModel(Insurer model) {
        this.model = model;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurer);
        return "{totalCount:" + this.insurer.size() + ",results:" + jObject.toString() + "}";
    }

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

    public String updateInsurer(){

        try {

            ActionResponse response = adminInsurerService.updateInsurer(model, getIsNew());
            setActionResponse(response);

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String triggerInsurerStatus() throws Exception {

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
}
