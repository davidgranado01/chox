package idas.chox.service.admin;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.data.services.SecureDataService;
import idas.chox.service.ActionResponse;
import java.util.List;

public class AdminChorganisationService extends SecureDataService {

    private ActionResponse actionResponse;
    private ChorganisationService chorganisationService;
    private InsurerChorganisationService insurerChorganisationService;

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    public ChorganisationService getChorganisationService() {
        return chorganisationService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public Chorganisation UpdateChorganisation(Chorganisation chorganisation) {
        chorganisation = chorganisationService.updateChorganisation(chorganisation);
        return chorganisation;
    }

    public boolean isChorganisationNameExist(String chorganisationName) {
        return chorganisationService.isChorgNameExist(chorganisationName);
    }

    public List<Chorganisation> getAllChorganisations(String orderColumn) {
        return this.chorganisationService.getChorganisations("name");
    }

    public List<InsurerChorganisation> getInsurerChorganisations(int choId) {
        return this.insurerChorganisationService.getInsurerChorganisations(null, choId);
    }

    public List<InsurerChorganisation> getTpiInsurerChorganisations(int choId) {
        return this.insurerChorganisationService.getTpiActivatedInsurerChorganisations(null, choId);
    }

    public ActionResponse UpdateChorganisationStatus(String chorganisationId) {

        if (!chorganisationId.equalsIgnoreCase("")) {

            this.actionResponse = new ActionResponse();

            Chorganisation chorganisation = null;
            chorganisation = chorganisationService.getChorganisation(Integer.valueOf(chorganisationId));

            if (chorganisation.isStatus()) {
                chorganisation.setStatus(false);
            } else {
                chorganisation.setStatus(true);
            }

            this.chorganisationService.updateChorganisation(chorganisation);

        } else {

            getActionResponse().AddError("Incorrect Credit Hire Organisation");

        }

        return getActionResponse();

    }

    public Chorganisation getChorganisation(String ChorganisationId) {

        Chorganisation chorganisation = null;

        if (!ChorganisationId.equalsIgnoreCase("")) {
            chorganisation = chorganisationService.getChorganisation(Integer.valueOf(ChorganisationId));
        }

        return chorganisation;
    }
}
