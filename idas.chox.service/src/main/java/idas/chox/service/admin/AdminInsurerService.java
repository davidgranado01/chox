package idas.chox.service.admin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import idas.chox.core.model.AutomaticRoutingPolicy;
import idas.chox.core.model.AutomaticRoutingPrice;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.IdLookupItem;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerAlias;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.model.InsurerIntelligentNote;
import idas.chox.core.model.ReasonOfRejection;
import idas.chox.core.model.VehicleClassCeiling;
import idas.chox.core.model.WebUser;
import idas.chox.core.model.Workgroup;
import idas.chox.core.services.AutomaticRoutingService;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.core.services.BreBandService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.ClaimService;
import idas.chox.core.services.InsurerAliasService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.InsurerIntelligentNoteService;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.InvoiceService;
import idas.chox.core.services.ReasonOfRejectionService;
import idas.chox.core.services.UserService;
import idas.chox.core.services.VehicleClassCeilingService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.core.services.WorkgroupService;
import idas.chox.core.util.EmailHelper;
import idas.chox.data.services.SecureDataService;
import idas.chox.service.ActionResponse;
import java.io.IOException;
import javax.mail.MessagingException;

public class AdminInsurerService extends SecureDataService {

    static final Logger LOG = LoggerFactory.getLogger(AdminInsurerService.class);
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
    private UserService userService;
    private ClaimService claimService;
    private InvoiceService invoiceService;
    private ReasonOfRejectionService reasonOfRejectionService;
    private InsurerIntelligentNoteService insurerIntelligentNoteService;
    private boolean emailOnBreBandCreation;
    private String  emailReceivers;
    private String hostName;

    public void setEmailOnBreBandCreation(boolean emailOnBreBandCreation) {
        this.emailOnBreBandCreation = emailOnBreBandCreation;
    }

    public void setEmailReceivers(String emailReceivers) {
        this.emailReceivers = emailReceivers;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public ActionResponse getActionResponse() {
        return actionResponse;
    }

    public void setActionResponse(ActionResponse actionResponse) {
        this.actionResponse = actionResponse;
    }

//    public void setProtocolVehicleClassCeilingService(ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService) {
//        this.protocolVehicleClassCeilingService = protocolVehicleClassCeilingService;
//    }

    public void triggerInsurerStatus(int insurerId) {

        Insurer insurer = this.insurerService.getInsurer(insurerId);

        if (insurer.isStatus()) {
            insurer.setStatus(false);
        } else {
            insurer.setStatus(true);
        }

        insurerService.saveInsurer(insurer);

    }

    public ActionResponse updateInsurer(Insurer insurer, boolean isNew, String originalName) {

        actionResponse = new ActionResponse();
        boolean isAllowUpdate = true;

        if (isNew) {
            if (insurerService.isInsurerNameExist(insurer.getName())) {
                actionResponse.AddError("Insurer name already exists!");
                isAllowUpdate = false;
            }
        } else {
            if (insurer.isWorkgroupEnable() && !workgroupService.isInsurerWithWorkgroup(insurer.getId())) {
                actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Please make sure there is at least one active workgroup exist in order to enable workgroup function");
            }
            
            if(!originalName.equals(insurer.getName())) {
                if (this.insurerService.isInsurerNameExist(insurer.getName())){
                    this.getActionResponse().AddError("Insurer name already exists!");
                    isAllowUpdate = false;
                }
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
                reasonOfRejectionService.createDefaultRecord(insurer);

                actionResponse.AssignNewIdResult(insurer.getId());
            }

        }

        return actionResponse;
    }

    public List<Insurer> getInsurers() {
        return insurerService.getInsurers();
    }

    public Insurer getInsurer(int insurerId) {
        return insurerService.getInsurer(insurerId);
    }
    
    public List<InsurerAlias> getInsurerAliases(int insurerId) {
        return insurerAliasService.getInsurerAliasesByInsurer(insurerId);
    }

    public InsurerAlias getInsurerAlias(int insurerAliasId) {
        return insurerAliasService.getInsurerAlias(insurerAliasId);
    }

    public ActionResponse addNewInsurerAlias(int insurerId, String insurerAliasName) {

        actionResponse = new ActionResponse();
        if (!insurerAliasService.isInsurerAliasExist(insurerId, insurerAliasName)) {

            InsurerAlias insurerAlias = new InsurerAlias();
            // Strip out white-space before saving
            insurerAlias.setAliasName(insurerAliasName.replaceAll("[^A-Za-z0-9]", ""));
            insurerAlias.setInsurer(insurerService.getInsurer(insurerId));
            insurerAliasService.saveInsurerAlias(insurerAlias);
            getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Alias '" + insurerAliasName + "' has been created");

        } else {
            getActionResponse().AddError("Alias '" + insurerAliasName + "' already exists");
        }

        return actionResponse;
    }

    public ActionResponse removeInsurerAlias(InsurerAlias insurerAlias) {
        actionResponse = new ActionResponse();
        insurerAliasService.deleteInsurerAlias(insurerAlias);
        getActionResponse().AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Alias '" + insurerAlias.getAliasName() + "' has been removed");
        return actionResponse;
    }

    public AutomaticRoutingPolicy getInsurerAutomaticRouting(int automaticRoutingId) {
        return automaticRoutingService.getAutomaticRouting(automaticRoutingId);
    }

    public AutomaticRoutingPrice getInsurerAutomaticRoutingByPrice(int automaticRoutingId) {
        return automaticRoutingService.getAutomaticRoutingByPrice(automaticRoutingId);
    }

    public List<AutomaticRoutingPolicy> getInsurerAutomaticRoutings(int insurerId) {
        return automaticRoutingService.getAutomaticRoutingsByPolicy(insurerId, -1);
    }

    public List<AutomaticRoutingPrice> getInsurerAutomaticRoutingsByPrice(int insurerId) {

        return automaticRoutingService.getAutomaticRoutingsByPrice(insurerId, -1);
    }

    public List getAvailableWorkgroups(int insurerId, boolean isActiveOnly) {

        List<IdLookupItem> items = new ArrayList<>();
        List<Workgroup> availableWorkgroups = workgroupService.getAvailableAutoRoutingWorkgroupsByInsurer(insurerId, isActiveOnly);

        for (Workgroup s : availableWorkgroups) {
            items.add(new IdLookupItem(s.getId(), s.getName()));
        }

        return items;
    }

    public ActionResponse addNewAutomaticRouting(Integer insurerId, Integer workgroupId, String regExpression) {
        actionResponse = new ActionResponse();

        if (insurerId > 0 && workgroupId > 0 && !regExpression.equalsIgnoreCase("")) {
            AutomaticRoutingPolicy automaticRouting = new AutomaticRoutingPolicy();
            automaticRouting.setExpression(regExpression);
            automaticRouting.setInsurer(insurerService.getInsurer(insurerId));
            automaticRouting.setWorkgroup(workgroupService.getWorkgroup(workgroupId));
            automaticRoutingService.saveAutomaticRouting(automaticRouting);
        } else {
            actionResponse.AddError("Incorrect Insurer and Workgroup");
        }

        return actionResponse;
    }

    public ActionResponse addNewAutomaticRoutingByPrice(Integer insurerId, Integer workgroupId, BigDecimal price) {
        actionResponse = new ActionResponse();

        if (insurerId > 0 && workgroupId > 0 && price != null) {
            AutomaticRoutingPrice automaticRouting = new AutomaticRoutingPrice();
            automaticRouting.setPrice(price);
            automaticRouting.setInsurer(insurerService.getInsurer(insurerId));
            automaticRouting.setWorkgroup(workgroupService.getWorkgroup(workgroupId));
            automaticRoutingService.saveAutomaticRoutingByPrice(automaticRouting);
        } else {
            actionResponse.AddError("Incorrect Insurer and Workgroup");
        }

        return actionResponse;
    }

    public ActionResponse updateAutomaticRouting(AutomaticRoutingPolicy automaticRouting) {
        actionResponse = new ActionResponse();
        automaticRoutingService.saveAutomaticRouting(automaticRouting);
        return actionResponse;
    }

    public ActionResponse deleteAutomaticRouting(Integer automaticRoutingId) {
        actionResponse = new ActionResponse();

        if (automaticRoutingId > 0 && automaticRoutingId != null) {
            AutomaticRoutingPolicy automaticRouting = automaticRoutingService.getAutomaticRouting(automaticRoutingId);
            automaticRoutingService.deleteAutomaticRouting(automaticRouting);
        } else {
            actionResponse.AddError("Incorrect Automatic Routing Record");
        }

        return actionResponse;
    }

    public ActionResponse deleteAutomaticRoutingByPrice(Integer automaticRoutingId) {
        actionResponse = new ActionResponse();
        if (automaticRoutingId > 0 && automaticRoutingId != null) {
            AutomaticRoutingPrice automaticRouting = automaticRoutingService.getAutomaticRoutingByPrice(automaticRoutingId);
            automaticRoutingService.deleteAutomaticRoutingByPrice(automaticRouting);
        } else {
            actionResponse.AddError("Incorrect Automatic Routing Record");
        }

        return actionResponse;
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
       actionResponse = new ActionResponse();
        if (breBandService.isBreBandOccupied(breBand)) {
            actionResponse.AddError("You cannot delete '" + breBand.getName() + "' because it is currently being used by one or more Credit Hire Organisations. Please remove the Credit Hire Organisations from this BRE and try again");
        } else {
            breBandService.deleteBreBand(breBand);
        }

        return actionResponse;
    }

    public ActionResponse updateInsurerBreBand(BreBand breBand, int insurerId, boolean isNew) {
        actionResponse = new ActionResponse();
        
        breBand.setInsurer(insurerService.getInsurer(insurerId));

        if (breBandService.isBreBandNameExist(breBand)) {
            actionResponse.AddError("Selected Band Name already exists");
        } else {

            breBandService.saveBreBand(breBand);

            if (isNew) {
                this.actionResponse.AssignNewIdResult(breBand.getId());
                if (emailOnBreBandCreation && !getSecurityInfoProvider().getIsCHOXAdmin()) {
                    // Email Valexa Staff of new BRE Band Creation
                    LOG.debug("Sending email to '{}' from {}", emailReceivers, hostName);
                    try {
                        Resource resource = new ClassPathResource("/application.properties");
                        Properties props = PropertiesLoaderUtils.loadProperties(resource);

                        String smtpHostName = props.getProperty("smtpHostName");
                        String smtpPort = props.getProperty("smtpPort");
                        String smtpEmailUser = props.getProperty("smtpEmailUser");
                        String smtpEmailUserPassword = props.getProperty("smtpEmailPassword");

                        String[] recipients = emailReceivers.split(",");

                        String emailSubject;
                        if ("PRODUCTION".equals(hostName)) {
                            emailSubject = "New BRE Band Created";
                        } else {
                            emailSubject = "New BRE Band Created (" + hostName + ")";
                        }
                        LOG.debug("Initialising emailHelper with smtpHostName={}, smtpPort={}, smtpEmailUser={}, smtpEmailUserPassword={}",
                                new Object[]{smtpHostName, smtpPort, smtpEmailUser, smtpEmailUserPassword});
                        EmailHelper emailHelper = new EmailHelper(smtpHostName, smtpPort, smtpEmailUser, smtpEmailUserPassword);
                        String emailMessage = "Insurer " + breBand.getInsurer().getName() + ", User " + breBand.getCreatedBy().getFullName() + " Has Added A New BRE Band Called " + breBand.getName() + " On " + breBand.getCreatedDate().toString() + ".";
                        emailHelper.postMail(emailSubject, emailMessage, recipients);
                        LOG.debug("Email sent: {}", emailSubject);
                    } catch (IOException | MessagingException ex) {
                        LOG.error("Error sending email for new RE Band '{}' Creation: {}", breBand.getName(), ex.getMessage());
                    }
                }
            }

        }
        return actionResponse;
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
    
//    public ActionResponse addNewProtocolVehicleClassCeiling(ProtocolVehicleClassCeiling protocolVehicleClassCeling, int vehicleClassId, int breBandId) {
//        this.actionResponse = new ActionResponse();
//        protocolVehicleClassCeling.setVehicleClass(vehicleClassService.getVehicleClass(vehicleClassId));
//        protocolVehicleClassCeling.setBreBand(breBandService.getBreBand(breBandId));
//        protocolVehicleClassCeilingService.saveProtocolVehicleClassCeiling(protocolVehicleClassCeling);
//        getActionResponse().AssignNewIdResult(protocolVehicleClassCeling.getId());
//        return this.actionResponse;
//    }

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
    
//    public ActionResponse updateProtocolVehicleClassCeiling(int protocolVehicleClassCeilingId, double hireNetCeiling, double repairNetCeiling) {
//        this.actionResponse = new ActionResponse();
//        ProtocolVehicleClassCeiling protocolVehicleClassCeiling = protocolVehicleClassCeilingService.getProtocolVehicleClassCeiling(protocolVehicleClassCeilingId);
//        protocolVehicleClassCeiling.setHireNetCeiling(new BigDecimal(hireNetCeiling));
//        protocolVehicleClassCeiling.setRepairNetCeiling(new BigDecimal(repairNetCeiling));
//        protocolVehicleClassCeilingService.saveProtocolVehicleClassCeiling(protocolVehicleClassCeiling);
//        return this.actionResponse;
//    }
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
        insurerChorganisationService.saveInsurerChorganisation(insurerChorganisation);

        return this.actionResponse;
    }

    public ActionResponse removeInsurerChorganisation(int insurerChorganisationId) {

        this.actionResponse = new ActionResponse();
        InsurerChorganisation insurerChorganisation = insurerChorganisationService.getInsurerChorganisation(insurerChorganisationId);

        if (chorganisationService.isActiveChorganisationsByInsurerCreditHireWithBreBand(insurerChorganisation.getInsurer().getId(), insurerChorganisation.getChorganisation().getId())) {
            this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Not allowed to delete this Credit Hire Org from this Insurer. Please remove the Bre Band assigned to this Credit Hire Organisation first.");
        } else {
            insurerChorganisationService.deleteInsurerChorganisation(insurerChorganisation);
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

    public WebUser getWebuserById(int webUserId) {
        return userService.getWebUser(webUserId);
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

    public ActionResponse addOrUpdateReasonOfRejection(ReasonOfRejection ror) {
        this.actionResponse = new ActionResponse();
        reasonOfRejectionService.saveReason(ror);
        return this.actionResponse;
    }
    
    public ActionResponse deleteReasonOfRejection(ReasonOfRejection ror) {
        this.actionResponse = new ActionResponse();
        if(ror.getType().equalsIgnoreCase(ReasonOfRejection.TYPE_CLAIM) && claimService.getNoOfRejectedClaims(ror.getId()) != 0){
            this.actionResponse.AddError("Rejection reason '" + ror.getRorName() + "' is assigned to a claim and it cannot be deleted");
        } else if(ror.getType().equalsIgnoreCase(ReasonOfRejection.TYPE_INVOICE) && invoiceService.getNoOfRejectedInvoices(ror.getId()) != 0){
            this.actionResponse.AddError("Rejection reason '" + ror.getRorName() + "' is assigned to an invoice and it cannot be deleted");
        } else {
            reasonOfRejectionService.deleteReason(ror);
            this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Rejection reason '" + ror.getRorName() + "' has been removed");
        }
        return this.actionResponse;
    }
    
    public ActionResponse updateReasonOfRejectionRestricted(ReasonOfRejection ror) {
        this.actionResponse = new ActionResponse();
        if(ror.getType().equalsIgnoreCase(ReasonOfRejection.TYPE_INVOICE)) {
            this.actionResponse.AddError("'Visible before assigned' is not applicable to Invoice type Rejection Reasons.");
        } else {
            reasonOfRejectionService.saveReason(ror);
            this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Rejection reason '" + ror.getRorName() + "' has been updated");
        }
        return this.actionResponse;
    }
    
    public ActionResponse updateReasonOfRejectionActive(ReasonOfRejection ror) {
        this.actionResponse = new ActionResponse();
        reasonOfRejectionService.saveReason(ror);
        this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Rejection reason '" + ror.getRorName() + "' has been updated");
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
        } else if (claimService.isOpenClaimByWorkgroupExist(workgroup.getId())&& workgroup.isStatus()) {
            this.actionResponse.AddError("This workgroup currently has assigned open claims. Please reassign these open claims before de-activating this workgroup.");
        } else {
            workgroup.setStatus(!workgroup.isStatus());
            workgroupService.saveWorkgroup(workgroup);
        }

        return this.actionResponse;
    }

    public ActionResponse triggerInsurerWorkgroupStpExcluded(Workgroup workgroup, int insurerId) {
        this.actionResponse = new ActionResponse();

        Insurer insurer = insurerService.getInsurer(insurerId);

        workgroup.setStpExcluded(!workgroup.isStpExcluded());
        workgroupService.saveWorkgroup(workgroup);

        return this.actionResponse;
    }

    public ActionResponse updateInsurerIntelligentNote(InsurerIntelligentNote iin) {
        this.actionResponse = new ActionResponse();
        insurerIntelligentNoteService.updateInsurerIntelligentNote(iin);
        this.actionResponse.AssignResult(ActionResponse.RESULT_TYPE_MESSAGE, "Intelligent Note has been updated");
        return this.actionResponse;
    }
    
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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setReasonOfRejectionService(
            ReasonOfRejectionService reasonOfRejectionService) {
        this.reasonOfRejectionService = reasonOfRejectionService;
    }
    
    public void setClaimService(ClaimService claimService) {
        this.claimService = claimService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public void setInsurerIntelligentNoteService(
            InsurerIntelligentNoteService insurerIntelligentNoteService) {
        this.insurerIntelligentNoteService = insurerIntelligentNoteService;
    }
}
