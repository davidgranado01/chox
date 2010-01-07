package idas.chox.web.actions;

import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Chorganisation;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.BreBandChorganisationViewData;
import idas.chox.web.viewdata.ChorganisationViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerBreBandMappingAction extends BaseAction {

    private int insurerId = -1;
    private int breBandId = -1;
    private int chorganisationId = -1;
    private int breBandChorganisationId = -1;
    private String jsonRecords;
    private AdminInsurerService adminInsurerService;

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        return this.jsonRecords;
    }

    public void setJsonData(Object object, Integer recordSize) {
        JSONArray jObject = JSONArray.fromObject(object);
        this.jsonRecords = "{totalCount:" + recordSize + ",results:" + jObject.toString() + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getBreBandChorganisationId() {
        return breBandChorganisationId;
    }

    public void setBreBandChorganisationId(int breBandChorganisationId) {
        this.breBandChorganisationId = breBandChorganisationId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public int getChorganisationId() {
        return chorganisationId;
    }

    public void setChorganisationId(int chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public int getBreBandId() {
        return breBandId;
    }

    public void setBreBandId(int breBandId) {
        this.breBandId = breBandId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">
    public String getChorganisationsByInsurerIdWithoutBreBand() {

        //try {

        List<ChorganisationViewData> credithireorganisation = new ArrayList<ChorganisationViewData>();

        if (this.insurerId > 0) {

            List<Chorganisation> chorganisations = adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(this.insurerId);

            for (Chorganisation object : chorganisations) {
                credithireorganisation.add(new ChorganisationViewData(object));
            }

            setJsonData(credithireorganisation, credithireorganisation.size());

        }

        //} catch (Exception ex) {
        //  handleException(this, ex);
        //return ERROR;
        //}

        return SUCCESS;
    }

    public String getChorganisationWithBreBandAssigned() {

        try {

            List<BreBandChorganisationViewData> insurerBreBand;
            List<BreBandOrganisation> brebandorganisations = new ArrayList<BreBandOrganisation>();
            brebandorganisations = adminInsurerService.getBreBandChorganisationsByBreBandId(this.breBandId);
            insurerBreBand = getChoViewDataList(brebandorganisations);
            setJsonData(insurerBreBand, insurerBreBand.size());

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String addBreBandChorganisation() {

        try {
            adminInsurerService.addBreBandChorganisation(this.breBandId, this.chorganisationId);
        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String deleteBreBandChorganisation() {

        try {

            if (this.breBandChorganisationId > 0) {
                adminInsurerService.deleteBreBandChorganisation(this.breBandChorganisationId);
            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public List<BreBandChorganisationViewData> getChoViewDataList(List<BreBandOrganisation> objects) {
        List<BreBandChorganisationViewData> breBandChorganisationViewDatas = new ArrayList<BreBandChorganisationViewData>();
        for (BreBandOrganisation h : objects) {
            breBandChorganisationViewDatas.add(new BreBandChorganisationViewData(h));
        }
        return breBandChorganisationViewDatas;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }
    // </editor-fold>
}
