package idas.chox.web.actions;

import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.web.viewdata.BreBandChorganisationViewData;
import idas.chox.web.viewdata.ChorganisationViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerBreBandMappingAction extends BaseAction {

    protected int insurerId = -1;
    private int breBandId = -1;
    private int chorganisationId = -1;
    private int breBandChorganisationId = -1;
    protected String jsonRecords;
    private BreBandService breBandService;
    private ChorganisationService chorganisationService;
    private BreBandOrganisationService breBandOrganisationService;

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

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public int getBreBandId() {
        return breBandId;
    }

    public void setBreBandId(int breBandId) {
        this.breBandId = breBandId;
    }

    public String getJsonData() {
        return this.jsonRecords;
    }

    public void setJsonData(Object object, Integer recordSize) {
        JSONArray jObject = JSONArray.fromObject(object);
        this.jsonRecords = "{totalCount:" + recordSize + ",results:" + jObject.toString() + "}";
    }

    public String getChorganisationsByInsurerIdWithoutBreBand() {

        try {

            List<ChorganisationViewData> credithireorganisation = new ArrayList<ChorganisationViewData>();

            if (this.insurerId > 0) {

                List<Chorganisation> chorganisations = chorganisationService.getChorganisationsByInsurerId(this.insurerId);

                for (Chorganisation object : chorganisations) {
                    if (!breBandOrganisationService.isActiveChorganisationWithBand(object.getId(), insurerId)) {
                        credithireorganisation.add(new ChorganisationViewData(object));
                    }
                }
                
                setJsonData(credithireorganisation, credithireorganisation.size());

            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String getChorganisationWithBreBandAssigned() {

        try {

            List<BreBandChorganisationViewData> insurerBreBand;
            List<BreBandOrganisation> brebandorganisations = new ArrayList<BreBandOrganisation>();
            brebandorganisations = breBandOrganisationService.getBreBandChorganisationsByBreBandId(this.breBandId);
            insurerBreBand = getChoViewDataList(brebandorganisations);
            setJsonData(insurerBreBand, insurerBreBand.size());

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

    public String addBreBandChorganisation() {

        try {

            BreBandOrganisation breBandOrganisation = new BreBandOrganisation();
            breBandOrganisation.setBreBand(breBandService.getBreBand(this.breBandId));
            breBandOrganisation.setChorganisation(chorganisationService.getChorganisation(this.chorganisationId));
            breBandOrganisationService.saveBreBandOrganisation(breBandOrganisation);

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String deleteBreBandChorganisation() {

        try {
            
            if(this.breBandChorganisationId>0){
                
                BreBandOrganisation breBandOrganisation = breBandOrganisationService.getBreBandOrganisation(this.breBandChorganisationId);
                breBandOrganisationService.deleteBreBandOrganisation(breBandOrganisation);
                
            }
            
        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Override
    public String execute() {
        return SUCCESS;
    }

    public void setBreBandService(BreBandService breBandService) {
        this.breBandService = breBandService;
    }
    
    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }
}
