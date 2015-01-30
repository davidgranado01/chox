package idas.chox.service.admin;

import java.util.List;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ChorganisationAlias;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.ChorganisationAliasService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.data.services.SecureDataService;
import idas.chox.service.ActionResponse;

public class AdminChorganisationService extends SecureDataService {

    private ActionResponse actionResponse;
    private ChorganisationService chorganisationService;
    private ChorganisationAliasService chorganisationAliasService;
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

    public ChorganisationAliasService getChorganisationAliasService() {
        return chorganisationAliasService;
    }

    public void setChorganisationAliasService(ChorganisationAliasService chorganisationAliasService) {
        this.chorganisationAliasService = chorganisationAliasService;
    }

    public ChorganisationService getChorganisationService() {
        return chorganisationService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }

    public Chorganisation updateChorganisation(Chorganisation chorganisation) {
        chorganisation = chorganisationService.updateChorganisation(chorganisation);
        return chorganisation;
    }

    public boolean isChorganisationNameExist(String chorganisationName) {
        return chorganisationService.isChorgNameExist(chorganisationName);
    }

    public List<Chorganisation> getAllNonManualChorganisations(String orderColumn) {
        return this.chorganisationService.getNonManualChorganisations("name");
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

    public ActionResponse updateChorganisationStatus(String chorganisationId) {

        if (!chorganisationId.equalsIgnoreCase("")) {

            this.actionResponse = new ActionResponse();

            Chorganisation chorganisation = chorganisationService.getChorganisation(Integer.valueOf(chorganisationId));

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

     // <editor-fold defaultstate="collapsed" desc="CHO ALIAS">
    public ActionResponse addNewChoAlias(int choId, String choAliasName) {

        this.actionResponse = new ActionResponse();
        if (!chorganisationAliasService.isChorganisationAliasExist(choId, choAliasName)) {

            ChorganisationAlias choAlias = new ChorganisationAlias();
            // Strip out white-space before saving
            choAlias.setAliasName(choAliasName.replaceAll("[^A-Za-z0-9]", ""));
            choAlias.setChorganisation(chorganisationService.getChorganisation(choId));
            chorganisationAliasService.saveChorganisationAlias(choAlias);
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Alias '" + choAliasName + "' has been created");

        } else {
            getActionResponse().AddError("Alias '" + choAliasName + "' already exists");
        }

        return this.actionResponse;
    }
    
    public List<ChorganisationAlias> getChorganisationAliases(int choId) {
        return chorganisationAliasService.getChorganisationAliasesByChorganisation(choId);
    }

    public ChorganisationAlias getChorganisationAlias(int choAliasId) {
        return chorganisationAliasService.getChorganisationAlias(choAliasId);
    }
    
    public ActionResponse removeChoAlias(ChorganisationAlias choAlias) {
        this.actionResponse = new ActionResponse();
        chorganisationAliasService.deleteChorganisationAlias(choAlias);
        getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Alias '" + choAlias.getAliasName() + "' has been removed");
        return this.actionResponse;
    }
// </editor-fold>
    
    public Chorganisation getChorganisation(String chorganisationIdStr) {

        Chorganisation chorganisation = null;

        if (!chorganisationIdStr.equalsIgnoreCase("")) {
            chorganisation = chorganisationService.getChorganisation(Integer.valueOf(chorganisationIdStr));
        }

        return chorganisation;
    }
    
    public Chorganisation getChorganisation(int chorganisationId) {
        return chorganisationService.getChorganisation(chorganisationId);
    }
}
