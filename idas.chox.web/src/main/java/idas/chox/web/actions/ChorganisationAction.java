package idas.chox.web.actions;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.web.viewdata.ChorganisationViewData;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class ChorganisationAction extends BaseAction {

    private List<ChorganisationViewData> credithireorganisation;
    private BreBandOrganisationService breBandOrganisationService;
    private ChorganisationService service;
    private int insurerId;

    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.credithireorganisation);
        return "{totalCount:" + this.credithireorganisation.size() + ",results:" + jObject.toString() + "}";
    }

    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
    }

    public void setChorganisationService(ChorganisationService service) {
        this.service = service;
    }

    @Override
    public String execute() {

        List<Chorganisation> credithireorganisationData = this.service.getChorganisation();

        credithireorganisation = new ArrayList<ChorganisationViewData>();

        for (Chorganisation h : credithireorganisationData) {
            credithireorganisation.add(new ChorganisationViewData(h));
        }

        return SUCCESS;
    }

    public String getAvailableChorganisation() {

        if (insurerId > 0) {

            List<Chorganisation> chorganisationData = this.service.getObjectsWithoutInsurer(insurerId);

            credithireorganisation = new ArrayList<ChorganisationViewData>();

            for (Chorganisation h : chorganisationData) {
                credithireorganisation.add(new ChorganisationViewData(h));
            }
        }

        return SUCCESS;
    }

    // BRE MAPPING, GET CREDIT HIRE WITHOUT CHO BAND
    public String getChorganisationsByInsurerIdWithoutBreBand() {

        try {

            credithireorganisation = new ArrayList<ChorganisationViewData>();

            if (insurerId > 0) {

                List<Chorganisation> chorganisations = service.getObjectsByInsurerId(insurerId);

                for (Chorganisation object : chorganisations) {

                    if (!breBandOrganisationService.isActiveChorganisationWithBand(object.getId(), insurerId)) {
                        credithireorganisation.add(new ChorganisationViewData(object));
                    }

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return SUCCESS;
    }
}
