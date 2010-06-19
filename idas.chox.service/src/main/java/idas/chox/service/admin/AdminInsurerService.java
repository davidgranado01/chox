package idas.chox.service.admin;

import idas.chox.core.model.AutomaticRouting;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.VehicleClassCeilingService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.service.ActionResponse;
import idas.chox.data.services.SecureDataService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminInsurerService extends SecureDataService {

    private ActionResponse actionResponse;
    private BreBandService breBandService;
    private InsurerService insurerService;
    private WorkgroupService workgroupService;
    private InsurerAliasService insurerAliasService;
    private AutomaticRoutingService automaticRoutingService;
    private ChorganisationService chorganisationService;
    private BreBandOrganisationService breBandOrganisationService;
    private InsurerChorganisationService insurerChorganisationService;
    private VehicleClassCeilingService vehicleClassCeilingService;
    private VehicleClassService vehicleClassService;

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

    // <editor-fold defaultstate="collapsed" desc="INSURER">
    public void triggerInsurerStatus(int insurerId) {

        Insurer insurer = this.insurerService.getInsurer(insurerId);

        if (insurer.isStatus()) {
            insurer.setStatus(false);
        } else {
            insurer.setStatus(true);
        }

        insurerService.saveInsurer(insurer);

    }

    public ActionResponse updateInsurer(Insurer insurer, boolean isNew) {

        this.actionResponse = new ActionResponse();
        boolean isAllowUpdate = true;

        if (isNew) {
            if (this.insurerService.isInsurerNameExist(insurer.getName())) {
                this.actionResponse.AddError("Insurer name already exist!");
                isAllowUpdate = false;
            }
        } else {
            if (insurer.isWorkgroupEnable() && !workgroupService.isInsurerWithWorkgroup(insurer.getId())) {
                this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Please make sure there is at least one active workgroup exist in order to enable workgroup function");
            }
        }

        if (isAllowUpdate) {

            insurerService.saveInsurer(insurer);

            if (isNew) {
                if (insurer.isWorkgroupEnable()) {
                    workgroupService.createDefaultWorkgroup(insurer);
                }
                insurerAliasService.createDefaultRecord(insurer);
                breBandService.createDefaultRecord(insurer);

                this.actionResponse.AssignNewIdResult(insurer.getId());
            }

        }

        return this.actionResponse;
    }

    public List<Insurer> getInsurers() {
        return insurerService.getInsurers();
    }

    public Insurer getInsurer(int insurerId) {
        return insurerService.getInsurer(insurerId);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER ALIAS">
    public List<InsurerAlias> getInsurerAliases(int insurerId) {
        return insurerAliasService.getInsurerAliasesByInsurer(insurerId);
    }

    public InsurerAlias getInsurerAlias(int insurerAliasId) {
        return insurerAliasService.getInsurerAlias(insurerAliasId);
    }

    public ActionResponse addNewInsurerAlias(int insurerId, String insurerAliasName) {

        this.actionResponse = new ActionResponse();
        if (!insurerAliasService.isInsurerAliasExist(insurerId, insurerAliasName)) {

            InsurerAlias insurerAlias = new InsurerAlias();
            insurerAlias.setAliasName(insurerAliasName);
            insurerAlias.setInsurer(insurerService.getInsurer(insurerId));
            insurerAliasService.saveInsurerAlias(insurerAlias);
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Alias '" + insurerAliasName + "' has been created");

        } else {
            getActionResponse().AddError("Alias '" + insurerAliasName + "' already exists");
        }

        return this.actionResponse;
    }

    public ActionResponse removeInsurerAlias(InsurerAlias insurerAlias) {
        this.actionResponse = new ActionResponse();
        insurerAliasService.deleteInsurerAlias(insurerAlias);
        getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Alias '" + insurerAlias.getAliasName() + "' has been removed");
        return this.actionResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER AUTOMATIC ROUTING">
    public AutomaticRouting getInsurerAutomaticRouting(int automaticRoutingId) {
        return automaticRoutingService.getAutomaticRouting(automaticRoutingId);
    }

    public List<AutomaticRouting> getInsurerAutomaticRoutings(int insurerId) {
        return automaticRoutingService.getAutomaticRoutings(insurerId, -1);
    }

    public List getAvailableWorkgroups(int insurerId) {

        List items = new ArrayList<IdLookupItem>();
        List<Workgroup> availableWorkgroups = workgroupService.getAvailableAutoRoutingWorkgroupsByInsurer(insurerId);

        for (Workgroup s : availableWorkgroups) {
            items.add(new IdLookupItem(s.getId(), s.getName()));
        }

        return items;
    }

    public ActionResponse addNewAutomaticRouting(Integer insurerId, Integer workgroupId, String regExpression) {
        this.actionResponse = new ActionResponse();

        if (insurerId > 0 && workgroupId > 0 && !regExpression.equalsIgnoreCase("")) {
            AutomaticRouting automaticRouting = new AutomaticRouting();
            automaticRouting.setExpression(regExpression);
            automaticRouting.setInsurer(insurerService.getInsurer(insurerId));
            automaticRouting.setWorkgroup(workgroupService.getWorkgroup(workgroupId));
            automaticRoutingService.saveAutomaticRouting(automaticRouting);
        } else {
            this.actionResponse.AddError("Incorrect Insurer and Workgroup");
        }

        return this.actionResponse;
    }

    public ActionResponse updateAutomaticRouting(AutomaticRouting automaticRouting) {
        this.actionResponse = new ActionResponse();
        automaticRoutingService.saveAutomaticRouting(automaticRouting);
        return this.actionResponse;
    }

    public ActionResponse deleteAutomaticRouting(Integer automaticRoutingId) {
        this.actionResponse = new ActionResponse();

        if (automaticRoutingId > 0 && automaticRoutingId != null) {
            AutomaticRouting automaticRouting = automaticRoutingService.getAutomaticRouting(automaticRoutingId);
            automaticRoutingService.deleteAutomaticRouting(automaticRouting);
        } else {
            this.actionResponse.AddError("Incorrect Automatic Routing Record");
        }

        return this.actionResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER BRE BAND">
    public List<BreBand> getInsurerBreBands(int insurerId) {
        return breBandService.getInsurerBreBandsByInsurer(insurerId);
    }

    public BreBand getBreBand(int breBandId) {
        return breBandService.getBreBand(breBandId);
    }

    public ActionResponse deleteInsurerBreBand(BreBand breBand) {
        this.actionResponse = new ActionResponse();
        if (breBandService.isBreBandOccupied(breBand)) {
            this.actionResponse.AddError("You cannot delete '" + breBand.getName() + "' because it is currently being used by one or more Credit Hire Organisations. Please remove the Credit Hire Organisations from this BRE and try again");
        } else {
            this.breBandService.deleteBreBand(breBand);
        }

        return this.actionResponse;
    }

    public ActionResponse updateInsurerBreBand(BreBand breBand, int insurerId, boolean isNew) {
        this.actionResponse = new ActionResponse();

        breBand.setInsurer(insurerService.getInsurer(insurerId));

        if (breBandService.isBreBandNameExist(breBand)) {
            this.actionResponse.AddError("Selected Band Name already exists");
        } else {

            breBandService.saveBreBand(breBand);

            if (isNew) {
                this.actionResponse.AssignNewIdResult(breBand.getId());
            }

        }

        return this.actionResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER BRE BAND MAPPING">
    public List<BreBandOrganisation> getBreBandChorganisationsByBreBandId(int breBandId) {
        return breBandOrganisationService.getBreBandChorganisationsByBreBandId(breBandId);
    }

    public List<Chorganisation> getChorganisationsWithoutBreBandByInsurerId(int insurerId) {
        return chorganisationService.getActiveChorganisationsByInsurerWithoutBreBand(insurerId);
    }

    public ActionResponse addBreBandChorganisation(int breBandId, int chorganisationId) {
        this.actionResponse = new ActionResponse();
        BreBandOrganisation breBandOrganisation = new BreBandOrganisation();
        breBandOrganisation.setBreBand(breBandService.getBreBand(breBandId));
        breBandOrganisation.setChorganisation(chorganisationService.getChorganisation(chorganisationId));
        breBandOrganisationService.saveBreBandOrganisation(breBandOrganisation);
        return this.actionResponse;
    }

    public ActionResponse deleteBreBandChorganisation(int breBandChorganisationId) {
        this.actionResponse = new ActionResponse();
        BreBandOrganisation breBandOrganisation = breBandOrganisationService.getBreBandOrganisation(breBandChorganisationId);
        breBandOrganisationService.deleteBreBandOrganisation(breBandOrganisation);
        return this.actionResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER VEHICLE CLASS CEILING">
    public List<VehicleClassCeiling> getVehicleClassCeilingByInsurer(int insurerId) {
        return vehicleClassCeilingService.getSelectedVehicleClassCeilingByInsurer(insurerId);
    }

    public ActionResponse addNewVehicleClassCeiling(VehicleClassCeiling vehicleClassCeling, int vehicleClassId, int insurerId) {
        this.actionResponse = new ActionResponse();
        vehicleClassCeling.setVehicleClass(vehicleClassService.getVehicleClass(vehicleClassId));
        vehicleClassCeling.setInsurer(insurerService.getInsurer(insurerId));
        vehicleClassCeilingService.saveVehicleClassCeiling(vehicleClassCeling);
        getActionResponse().AssignNewIdResult(vehicleClassCeling.getId());
        return this.actionResponse;
    }

    public ActionResponse removeVehicleClassCeiling(int vehicleClassCeilingId) {
        this.actionResponse = new ActionResponse();
        VehicleClassCeiling vehicleClassCeiling = vehicleClassCeilingService.getVehicleClassCeiling(vehicleClassCeilingId);
        vehicleClassCeilingService.deleteVehicleClassCeiling(vehicleClassCeiling);
        return this.actionResponse;
    }

    public ActionResponse updateVehicleClassCeiling(int vehicleClassCeilingId, double hireNetCeiling, double repairNetCeiling) {
        this.actionResponse = new ActionResponse();
        VehicleClassCeiling vehicleClassCeiling = vehicleClassCeilingService.getVehicleClassCeiling(vehicleClassCeilingId);
        vehicleClassCeiling.setHireNetCeiling(new BigDecimal(hireNetCeiling));
        vehicleClassCeiling.setRepairNetCeiling(new BigDecimal(repairNetCeiling));
        vehicleClassCeilingService.saveVehicleClassCeiling(vehicleClassCeiling);
        return this.actionResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER CH ORGANISATION">
    public List<InsurerChorganisation> getInsurerChorganisations(int insurerId) {
        return this.insurerChorganisationService.getInsurerChorganisations(insurerId, null);
    }

    public List<Chorganisation> getAvailableChorganisationsByInsurer(int insurerId) {
        return chorganisationService.getAvailableChorganisationsByInsurer(insurerId);
    }

    public ActionResponse addNewInsurerChorganisation(int insurerId, int chorganisationId) {
        this.actionResponse = new ActionResponse();

        InsurerChorganisation insurerChorganisation = insurerChorganisationService.getInsurerChorganisation(insurerId, chorganisationId);

        if (insurerChorganisation == null) {
            insurerChorganisation = new InsurerChorganisation();
        }

        insurerChorganisation.setChorganisation(chorganisationService.getChorganisation(chorganisationId));
        insurerChorganisation.setInsurer(insurerService.getInsurer(insurerId));
        insurerChorganisation.setStatus(true);
        insurerChorganisationService.saveInsurerChorganisation(insurerChorganisation);

        return this.actionResponse;
    }

    public ActionResponse removeInsurerChorganisation(int insurerChorganisationId) {

        this.actionResponse = new ActionResponse();
        InsurerChorganisation insurerChorganisation = insurerChorganisationService.getInsurerChorganisation(insurerChorganisationId);

        if (chorganisationService.isActiveChorganisationsByInsurerCreditHireWithBreBand(insurerChorganisation.getInsurer().getId(), insurerChorganisation.getChorganisation().getId())) {
            this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Not allowed to delete Thic Credit Hire From This insuere. Please remove the Bre Band assigned to this Credit Hire First");
        } else {
            insurerChorganisation.setStatus(false);
            insurerChorganisationService.saveInsurerChorganisation(insurerChorganisation);
        }

        return this.actionResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="INSURER WORKGROUP">
    public Workgroup getWorkgroup(int workgroupId) {
        return workgroupService.getWorkgroup(workgroupId);
    }

    public List<Workgroup> getInsurerWorkgroups(int insurerId) {
        return workgroupService.getWorkgroupsByInsurer(insurerId);
    }

    public ActionResponse addNewInsurerWorkgroup(Workgroup workgroup, int insurerId) {
        this.actionResponse = new ActionResponse();
        if (!workgroupService.isWorkgroupNameExistByInsurer(insurerId, workgroup.getName())) {
            workgroup.setInsurer(insurerService.getInsurer(insurerId));
            workgroup.setStatus(true);
            workgroupService.saveWorkgroup(workgroup);
            this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Workgroup '" + workgroup.getName() + "' has been created");
        } else {
            this.actionResponse.AddError("Workgroup '" + workgroup.getName() + "' already exists");
        }
        return this.actionResponse;
    }

    public ActionResponse removeInsurerWorkgroup(int workgroupId, int insurerId) {

        this.actionResponse = new ActionResponse();

        Insurer insurer = insurerService.getInsurer(insurerId);
        Workgroup workgroup = workgroupService.getWorkgroup(workgroupId);

        // WORKGROUP FEATUERE IS ENABLE
        // EXCEPT THE WORKGROUP ITSELF, DO NOT HAVE ANY ACTIVE WORKGROUP
        if (insurer.isWorkgroupEnable() && !workgroupService.isWorkgroupAllowToInactive(insurerId, workgroup.getId())) {
            this.actionResponse.AddError("Unable to remove this workgroup. Must maintain at least one active workgroup for this insurer.");
        } else {
            if (workgroupService.isWorkgroupDeletable(workgroup.getId())) {
                workgroupService.deleteWorkgroup(workgroup);
                this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Workgroup '" + workgroup.getName() + "' has been removed");
            } else {
                this.actionResponse.AddError("Workgroup '" + workgroup.getName() + "' cannot be removed");
            }
        }
        return this.actionResponse;
    }

    public ActionResponse triggerInsurerWorkgroupStatus(Workgroup workgroup, int insurerId) {
        this.actionResponse = new ActionResponse();

        Insurer insurer = insurerService.getInsurer(insurerId);

        // CHECK WORKGROUPS STATUS IF CHANGE FROM ACTIVE TO INACTIVE
        // WORKGROUP FEATUERE IS ENABLE
        // EXCEPT THE WORKGROUP ITSELF, DO NOT HAVE ANY ACTIVE WORKGROUP
        if (insurer.isWorkgroupEnable() && workgroup.isStatus() && !workgroupService.isWorkgroupAllowToInactive(insurerId, workgroup.getId())) {
            this.actionResponse.AddError("Unable to de-activate this workgroup. Must maintain at least one active workgroup for this insurer.");
        } else {
            workgroup.setStatus(!workgroup.isStatus());
            workgroupService.saveWorkgroup(workgroup);
        }

        return this.actionResponse;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SERVICES">
    public void setAutomaticRoutingService(AutomaticRoutingService automaticRoutingService) {
        this.automaticRoutingService = automaticRoutingService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setWorkgroupService(WorkgroupService workgroupService) {
        this.workgroupService = workgroupService;
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

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setInsurerAliasService(InsurerAliasService insurerAliasService) {
        this.insurerAliasService = insurerAliasService;
    }

    public void setVehicleClassCeilingService(VehicleClassCeilingService vehicleClassCeilingService) {
        this.vehicleClassCeilingService = vehicleClassCeilingService;
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }
    // </editor-fold>
}
