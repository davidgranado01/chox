package idas.chox.web.actions;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.InsurerService;
import idas.chox.web.viewdata.ChorganisationViewData;
import idas.chox.web.viewdata.InsurerChorganisationViewData;
import java.util.ArrayList;
import net.sf.json.JSONArray;
import java.util.List;

public class InsurerChorganisationAction extends BaseAction {

    protected int insurerId;
    protected int insurerChorganisationId;
    protected InsurerChorganisationService insurerChorganisationService;
    protected ChorganisationService chorganisationService;
    private InsurerService insurerService;
    protected String jsonRecords;
    private int chorganisationId = -1;

    public int getInsurerChorganisationId() {
        return insurerChorganisationId;
    }

    public void setInsurerChorganisationId(int insurerChorganisationId) {
        this.insurerChorganisationId = insurerChorganisationId;
    }

    public int getChorganisationId() {
        return chorganisationId;
    }

    public void setChorganisationId(int chorganisationId) {
        this.chorganisationId = chorganisationId;
    }

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getSelectedChorganisations() {

        try {

            List<InsurerChorganisation> chorganisationsData = this.insurerChorganisationService.getInsurerChorganisations(insurerId, null);
            List<InsurerChorganisationViewData> insurerChorganisations = parsetChoViewDataList(chorganisationsData);
            setJsonData(insurerChorganisations, insurerChorganisations.size());

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String getAvailableChorganisations() {

        try {


            List<Chorganisation> chorganisationData = this.chorganisationService.getAvailableChorganisationsByInsurer(this.insurerId);

            List<ChorganisationViewData> credithireorganisation = new ArrayList<ChorganisationViewData>();

            for (Chorganisation h : chorganisationData) {
                credithireorganisation.add(new ChorganisationViewData(h));
            }

            setJsonData(credithireorganisation, credithireorganisation.size());

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

    private List<InsurerChorganisationViewData> parsetChoViewDataList(List<InsurerChorganisation> objects) {

        List<InsurerChorganisationViewData> insurerChorgs = new ArrayList<InsurerChorganisationViewData>();

        for (InsurerChorganisation h : objects) {
            insurerChorgs.add(new InsurerChorganisationViewData(h));
        }
        return insurerChorgs;
    }

    public String getJsonData() {
        return this.jsonRecords;
    }

    public String addNewInsurerChorganisation() {

        try {

            if (this.insurerId > 0 && this.chorganisationId > 0) {

                InsurerChorganisation insurerChorganisation = insurerChorganisationService.getInsurerChorganisation(this.insurerId, this.chorganisationId);

                if (insurerChorganisation == null) {
                    insurerChorganisation = new InsurerChorganisation();
                }

                insurerChorganisation.setChorganisation(chorganisationService.getChorganisation(chorganisationId));
                insurerChorganisation.setInsurer(insurerService.getInsurer(insurerId));
                insurerChorganisation.setStatus(true);
                insurerChorganisationService.saveInsurerChorganisation(insurerChorganisation);

            }

        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public String removeInsurerChorganisation() {

        try {
            
            if (this.insurerChorganisationId > 0) {
                InsurerChorganisation insurerChorganisation = insurerChorganisationService.getInsurerChorganisation(this.insurerChorganisationId);
                insurerChorganisation.setStatus(false);
                insurerChorganisationService.saveInsurerChorganisation(insurerChorganisation);
            }
            
        } catch (Exception ex) {
            handleException(this, ex);
            return ERROR;
        }

        return SUCCESS;
    }

    public void setJsonData(Object object, Integer recordSize) {
        JSONArray jObject = JSONArray.fromObject(object);
        this.jsonRecords = "{totalCount:" + recordSize + ",results:" + jObject.toString() + "}";
    }

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }
}
