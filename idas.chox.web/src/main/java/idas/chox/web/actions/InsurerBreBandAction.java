package idas.chox.web.actions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.BreAppliedLiability;
import idas.chox.core.model.BreBand;
import idas.chox.core.model.BrePenaltyBand;
import idas.chox.core.model.ClaimMatchingBand;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.services.AppliedLiabilityService;
import idas.chox.core.services.BrePenaltyBandService;
import idas.chox.core.services.ClaimMatchingBandService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ProtocolVehicleClassCeilingService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.AppliedLiabilityViewData;
import idas.chox.web.viewdata.BrePenaltyBandViewData;
import idas.chox.web.viewdata.ClaimMatchingBandViewData;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;

public class InsurerBreBandAction extends BaseAction implements ModelDriven<BreBand>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerBreBandAction.class);
    private String objectId;
    private int insurerId = -1;
    private BreBand model;
    private AdminInsurerService adminInsurerService;
    private String protocolVehicleClassCeilingRecords;
    private String penaltyBandRecords;
    private String claimMatchingRecords;
    private String appliedLiabilityRecords;
    private VehicleClassService vehicleClassService;
    private ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService;
    private BrePenaltyBandService brePenaltyBandService;
    private ClaimMatchingBandService claimMatchingBandService;
    private AppliedLiabilityService appliedLiabilityService;
    private boolean asCopy;
    private LookupService lookupService;
    private int claimMatchingOwnerId = -1;
    private int claimMatchingWorkgroupId = -1;

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        updateModelInSession(model);
        return SUCCESS;
    }

    public String getProtocolVehicleClassCeilingRecords() {
        return protocolVehicleClassCeilingRecords;
    }

    public void setProtocolVehicleClassCeilingRecords(String protocolVehicleClassCeilingRecords) {
        this.protocolVehicleClassCeilingRecords = protocolVehicleClassCeilingRecords;
    }

    public String getPenaltyBandRecords() {
        return penaltyBandRecords;
    }

    public void setPenaltyBandRecords(String penaltyBandRecords) {
        this.penaltyBandRecords = penaltyBandRecords;
    }

    public String getClaimMatchingRecords() {
        return claimMatchingRecords;
    }

    public void setClaimMatchingRecords(String claimMatchingRecords) {
        this.claimMatchingRecords = claimMatchingRecords;
    }

    public String getAppliedLiabilityRecords() {
        return appliedLiabilityRecords;
    }

    public void setAppliedLiabilityRecords(String appliedLiabilityRecords) {
        this.appliedLiabilityRecords = appliedLiabilityRecords;
    }

    public boolean getIsNew() {
        return objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0;
    }

    @Override
    public BreBand getModel() {
        return model;
    }

    public void setModel(BreBand model) {
        this.model = model;
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getInsurerId() {
        return insurerId;
    }

    public void setInsurerId(int insurerId) {
        this.insurerId = insurerId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    @Override
    public void prepare() throws Exception {
        try {

            if (objectId != null && !objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminInsurerService.getBreBand(Integer.valueOf(this.objectId));
                    addModelToSession(model);
                }
            }

            if (model == null) {
                model = new BreBand();
                // Set insurer to the new BreBand. Used to access insurer settings in the UI.
                if (insurerId > 0) {
                    LOG.debug("Attaching insurer id: {} to the new BreBand.", insurerId);
                    model.setInsurer(adminInsurerService.getInsurer(insurerId));
                }
            }

        } catch (Exception ex) {
            handleException(ex);
        }
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    public String updateInsurerBreBand() {

        try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to update an insurer BRE Band for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            if (asCopy) {
                // We want to copy the current model, so get complete model stored rather then using the loaded one
                // Needed as parts of model not visible to non-CHOX Admin will not be present in current model
                model = adminInsurerService.getBreBand(Integer.valueOf(this.objectId));
                objectId = "-1";
                BreBand newModel = (BreBand) SerializationUtils.clone(model);
                newModel.setId(null);
                newModel.setProtocolVehicleClassCeilings(null);
                List<ProtocolVehicleClassCeiling> protocolVehicleClassCeilings = model.getProtocolVehicleClassCeilings();
                protocolVehicleClassCeilings.forEach((protocolVehicleClassCeiling) -> {
                    adminInsurerService.evict(protocolVehicleClassCeiling);
                    ProtocolVehicleClassCeiling pvc = new ProtocolVehicleClassCeiling();
                    pvc.setHireNetCeiling(protocolVehicleClassCeiling.getHireNetCeiling());
                    pvc.setRepairNetCeiling(protocolVehicleClassCeiling.getRepairNetCeiling());
                    pvc.setVehicleClass(protocolVehicleClassCeiling.getVehicleClass());
                    newModel.addProtocolVehicleClassCeiling(pvc);
                });
                newModel.setBrePenaltyBands(null);
                List<BrePenaltyBand> brePenaltyBands = model.getBrePenaltyBands();
                brePenaltyBands.forEach((brePenaltyBand) -> {
                    adminInsurerService.evict(brePenaltyBand);
                    BrePenaltyBand pb = new BrePenaltyBand();
                    pb.setClaimType(brePenaltyBand.getClaimType());
                    pb.setHireDay1(brePenaltyBand.getHireDay1());
                    pb.setHireDay2(brePenaltyBand.getHireDay2());
                    pb.setHireDay3(brePenaltyBand.getHireDay3());
                    pb.setHirePeriodStartDay1(brePenaltyBand.getHirePeriodStartDay1());
                    pb.setHirePeriodStartDay2(brePenaltyBand.getHirePeriodStartDay2());
                    pb.setHirePeriodStartDay3(brePenaltyBand.getHirePeriodStartDay3());
                    pb.setRepairDay1(brePenaltyBand.getRepairDay1());
                    pb.setRepairDay2(brePenaltyBand.getRepairDay2());
                    pb.setRepairDay3(brePenaltyBand.getRepairDay3());
                    pb.setRepairPeriodStartDay1(brePenaltyBand.getRepairPeriodStartDay1());
                    pb.setRepairPeriodStartDay2(brePenaltyBand.getRepairPeriodStartDay2());
                    pb.setRepairPeriodStartDay3(brePenaltyBand.getRepairPeriodStartDay3());
                    pb.setStartDate(brePenaltyBand.getStartDate());
                    pb.setUseCommercialDay1(brePenaltyBand.isUseCommercialDay1());
                    pb.setUseCommercialDay2(brePenaltyBand.isUseCommercialDay2());
                    pb.setUseCommercialDay3(brePenaltyBand.isUseCommercialDay3());
                    newModel.addBrePenaltyBand(pb);
                });
                newModel.setClaimMatchingBands(null);
                List<ClaimMatchingBand> claimMatchingBands = model.getClaimMatchingBands();
                claimMatchingBands.forEach((claimMatchingBand) -> {
                    adminInsurerService.evict(claimMatchingBand);
                    ClaimMatchingBand cmb = new ClaimMatchingBand();
                    cmb.setAutoAcknowledge(claimMatchingBand.isAutoAcknowledge());
                    cmb.setClaimType(claimMatchingBand.getClaimType());
                    cmb.setCmClass(claimMatchingBand.isCmClass());
                    cmb.setCpClass(claimMatchingBand.isCpClass());
                    cmb.setCsClass(claimMatchingBand.isCsClass());
                    cmb.setCvClass(claimMatchingBand.isCvClass());
                    cmb.setLiabilityPercentage(claimMatchingBand.getLiabilityPercentage());
                    cmb.setNtClass(claimMatchingBand.isNtClass());
                    cmb.setPtClass(claimMatchingBand.isPtClass());
                    cmb.setPvClass(claimMatchingBand.isPvClass());
                    cmb.setRvClass(claimMatchingBand.isRvClass());
                    cmb.setSpClass(claimMatchingBand.isSpClass());
                    cmb.setbClass(claimMatchingBand.isbClass());
                    cmb.setfClass(claimMatchingBand.isfClass());
                    cmb.setmClass(claimMatchingBand.ismClass());
                    cmb.setpClass(claimMatchingBand.ispClass());
                    cmb.setsClass(claimMatchingBand.issClass());
                    cmb.settClass(claimMatchingBand.istClass());
                    cmb.setuClass(claimMatchingBand.isuClass());
                    newModel.addClaimMatchingBand(cmb);
                });
                newModel.setAppliedLiabilities(null);
                List<BreAppliedLiability> appliedLiabilities = model.getAppliedLiabilities();
                appliedLiabilities.forEach((appliedLiability) -> {
                    adminInsurerService.evict(appliedLiability);
                    BreAppliedLiability al = new BreAppliedLiability();
                    al.setAppliedLiability(appliedLiability.getAppliedLiability());
                    al.setApplyToRepudiated(appliedLiability.isApplyToRepudiated());
                    al.setClaimType(appliedLiability.getClaimType());
                    newModel.addAppliedLiability(al);
                });

                adminInsurerService.evict(model);
                model = newModel;
            } else {
                checkVersion(model);

                if (protocolVehicleClassCeilingRecords != null && !protocolVehicleClassCeilingRecords.isEmpty()) {
                    updateProtocolVehicleClassCeiling();
                }
                if (penaltyBandRecords != null && !penaltyBandRecords.isEmpty()) {
                    updatePenaltyBands();
                }
                if (claimMatchingRecords != null && !claimMatchingRecords.isEmpty()) {
                    updateClaimMatching();
                }
                if (claimMatchingOwnerId > 0 && this.adminInsurerService.getWebuserById(claimMatchingOwnerId) != null) {
                    model.setClaimMatchingOwner(this.adminInsurerService.getWebuserById(claimMatchingOwnerId));
                } else if (getIsChoxAdmin()) {
                    model.setClaimMatchingOwner(null);
                }
                if (claimMatchingWorkgroupId > 0 && this.adminInsurerService.getWorkgroup(claimMatchingWorkgroupId) != null) {
                    model.setClaimMatchingWorkgroup(this.adminInsurerService.getWorkgroup(claimMatchingWorkgroupId));
                } else if (getIsChoxAdmin()) {
                    model.setClaimMatchingWorkgroup(null);
                }
                if (appliedLiabilityRecords != null && !appliedLiabilityRecords.isEmpty()) {
                    updateAppliedLiability();
                }
            }
            ActionResponse response;
            response = adminInsurerService.updateInsurerBreBand(model, this.insurerId, getIsNew());
            updateModelInSession(model);
            setActionResponse(response);
        } catch (Exception ex) {
            LOG.error("Error updating Insurer BRE Band: {}", ex.getMessage(), ex);
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    private void updateProtocolVehicleClassCeiling() {
        List<VehicleClassCeilingViewData> vehicleClassCeilingViewDatas
                = ((List<VehicleClassCeilingViewData>) new Gson().fromJson(protocolVehicleClassCeilingRecords, new TypeToken<List<VehicleClassCeilingViewData>>() {
                }.getType()));
        if (vehicleClassCeilingViewDatas != null) {
            vehicleClassCeilingViewDatas.forEach((vehicleClassCeilingViewData) -> {
                ProtocolVehicleClassCeiling pvcc;
                if (vehicleClassCeilingViewData.getId() > 0) {
                    pvcc = protocolVehicleClassCeilingService.getProtocolVehicleClassCeiling(vehicleClassCeilingViewData.getId());
                } else if (model.getId() == null || (pvcc = protocolVehicleClassCeilingService.getProtocolVehicleClassCeilingByVehicleClass(vehicleClassCeilingViewData.getVehicleClassId(), model.getId())) == null) {
                    pvcc = new ProtocolVehicleClassCeiling();
                    pvcc.setVehicleClass(vehicleClassService.getVehicleClass(vehicleClassCeilingViewData.getVehicleClassId()));
                    pvcc.setBreBand(model);
                }
                if (pvcc != null) {
                    if (vehicleClassCeilingViewData.isRemoved()) {
                        if (model.getProtocolVehicleClassCeilings() != null) {
                            model.getProtocolVehicleClassCeilings().remove(pvcc);
                        }
                    } else {
                        pvcc.setHireNetCeiling(vehicleClassCeilingViewData.getHireNetCeiling());
                        pvcc.setRepairNetCeiling(vehicleClassCeilingViewData.getRepairNetCeiling());
                        model.addProtocolVehicleClassCeiling(pvcc);
                    }
                }
            });
        }
    }

    private void updateClaimMatching() throws Exception {
        try {
            List<ClaimMatchingBandViewData> claimMatchingViewDatas
                    = ((List<ClaimMatchingBandViewData>) new Gson().fromJson(claimMatchingRecords, new TypeToken<List<ClaimMatchingBandViewData>>() {
                    }.getType()));
            if (claimMatchingViewDatas != null) {
                claimMatchingViewDatas.forEach((claimMatchingViewData) -> {
                    ClaimMatchingBand cm;
                    if (claimMatchingViewData.getId() > 0) {
                        cm = claimMatchingBandService.getClaimMatchingBand(claimMatchingViewData.getId());
                    } else if (model.getId() == null || (cm = claimMatchingBandService.getClaimMatchingBand(claimMatchingViewData.getId())) == null) {
                        cm = new ClaimMatchingBand();
                    }
                    if (cm != null) {
                        if (claimMatchingViewData.isRemoved()) {
                            if (model.getClaimMatchingBands() != null) {
                                model.getClaimMatchingBands().remove(cm);
                            }
                        } else {
                            cm.setClaimType(ClaimType.values()[claimMatchingViewData.getClaimTypeId()]);
                            cm.setLiabilityPercentage(claimMatchingViewData.getMinimumLiability());
                            cm.setAutoAcknowledge(claimMatchingViewData.getAutoAcknowledge().equals("Yes"));
                            if (claimMatchingViewData.getVehicleClasses().contains("CM")) {
                                cm.setCmClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("CP")) {
                                cm.setCpClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("CS")) {
                                cm.setCsClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("CV")) {
                                cm.setCvClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("NT")) {
                                cm.setNtClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("PT")) {
                                cm.setPtClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("PV")) {
                                cm.setPvClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("RV")) {
                                cm.setRvClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("SP")) {
                                cm.setSpClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("B")) {
                                cm.setbClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("F")) {
                                cm.setfClass(true);
                            }

                            if (claimMatchingViewData.getVehicleClasses().contains(",M")
                                    || claimMatchingViewData.getVehicleClasses().startsWith("M")) {
                                cm.setmClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains(",T")
                                    || claimMatchingViewData.getVehicleClasses().startsWith("T")) {
                                cm.settClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains(",P,")
                                    || claimMatchingViewData.getVehicleClasses().endsWith(",P") || claimMatchingViewData.getVehicleClasses().equals("P")) {
                                cm.setpClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains(",S,")
                                    || claimMatchingViewData.getVehicleClasses().endsWith(",S") || claimMatchingViewData.getVehicleClasses().equals("S")) {
                                cm.setsClass(true);
                            }
                            if (claimMatchingViewData.getVehicleClasses().contains("UNATTACHED")) {
                                cm.setuClass(true);
                            }
                            cm.setBreBand(model);
                            model.addClaimMatchingBand(cm);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            LOG.error("Exceeption thrown updating BRE Claim Matching bands: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private void updateAppliedLiability() throws Exception {
        try {
            List<AppliedLiabilityViewData> appliedLiabilityViewDatas
                    = ((List<AppliedLiabilityViewData>) new Gson().fromJson(appliedLiabilityRecords, new TypeToken<List<AppliedLiabilityViewData>>() {
                    }.getType()));
            if (appliedLiabilityViewDatas != null) {
                // First remove records
                appliedLiabilityViewDatas.forEach((appliedLiabilityViewData) -> {
                    BreAppliedLiability al;
                    if (appliedLiabilityViewData.getId() > 0) {
                        al = appliedLiabilityService.getAppliedLiability(appliedLiabilityViewData.getId());
                    } else if (model.getId() == null || (al = appliedLiabilityService.getAppliedLiability(appliedLiabilityViewData.getId())) == null) {
                        al = new BreAppliedLiability();
                    }
                    if (al != null) {
                        if (appliedLiabilityViewData.isRemoved()) {
                            if (model.getAppliedLiabilities() != null) {
                                model.getAppliedLiabilities().remove(al);
                            }
                        }
                    }
                });
                // Need to flush now as we may be adding a claim type just removed
                this.getBaseDataService().getCurrentSession().flush();
                
                // Now add new records
                appliedLiabilityViewDatas.forEach((appliedLiabilityViewData) -> {
                    BreAppliedLiability al;
                    if (appliedLiabilityViewData.getId() > 0) {
                        al = appliedLiabilityService.getAppliedLiability(appliedLiabilityViewData.getId());
                    } else if (model.getId() == null || (al = appliedLiabilityService.getAppliedLiability(appliedLiabilityViewData.getId())) == null) {
                        al = new BreAppliedLiability();
                    }
                    if (al != null) {
                        if (!appliedLiabilityViewData.isRemoved()) {
                            al.setClaimType(ClaimType.values()[appliedLiabilityViewData.getClaimTypeId()]);
                            al.setAppliedLiability(appliedLiabilityViewData.getAppliedLiability());
                            al.setApplyToRepudiated(appliedLiabilityViewData.getApplyToRepudiated().equals("Yes"));
                            al.setBreBand(model);
                            model.addAppliedLiability(al);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            LOG.error("Exceeption thrown updating BRE Claim Matching bands: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    private void updatePenaltyBands() throws Exception {
        try {
            List<BrePenaltyBandViewData> penaltyBandViewDatas
                    = ((List<BrePenaltyBandViewData>) new Gson().fromJson(penaltyBandRecords, new TypeToken<List<BrePenaltyBandViewData>>() {
                    }.getType()));
            if (penaltyBandViewDatas != null) {
                for (BrePenaltyBandViewData penaltyBandViewData : penaltyBandViewDatas) {
                    BrePenaltyBand bpb;
                    if (penaltyBandViewData.getId() > 0) {
                        bpb = brePenaltyBandService.getBrePenaltyBand(penaltyBandViewData.getId());
                    } else if (model.getId() == null || (bpb = brePenaltyBandService.getBrePenaltyBand(penaltyBandViewData.getId())) == null) {
                        bpb = new BrePenaltyBand();
                    }
                    if (bpb != null) {
                        if (penaltyBandViewData.isRemoved()) {
                            if (model.getBrePenaltyBands() != null) {
                                model.getBrePenaltyBands().remove(bpb);
                            }
                        } else {
                            bpb.setClaimType(ClaimType.values()[penaltyBandViewData.getClaimTypeId()]);
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            try {
                                bpb.setStartDate(formatter.parse((penaltyBandViewData.getPenaltyBandStartDate()).replace('T', ' ')));
                            } catch (ParseException ex) {
                                LOG.error("Exception converting string date to date with '{}': {}", penaltyBandViewData.getPenaltyBandStartDate(), ex.getMessage());
                                throw new Exception("Error converting Penalty Band Start Date");
                            }
                            bpb.setHireDay1(penaltyBandViewData.getHireDayRate1());
                            bpb.setHireDay2(penaltyBandViewData.getHireDayRate2());
                            bpb.setHireDay3(penaltyBandViewData.getHireDayRate3());
                            bpb.setUseCommercialDay1(penaltyBandViewData.isUseCommercialDay1());
                            bpb.setUseCommercialDay2(penaltyBandViewData.isUseCommercialDay2());
                            bpb.setUseCommercialDay3(penaltyBandViewData.isUseCommercialDay3());
                            bpb.setHirePeriodStartDay1(penaltyBandViewData.getHirePeriodStartDay1());
                            bpb.setHirePeriodStartDay2(penaltyBandViewData.getHirePeriodStartDay2());
                            bpb.setHirePeriodStartDay3(penaltyBandViewData.getHirePeriodStartDay3());
                            bpb.setRepairDay1(penaltyBandViewData.getRepairDayRate1());
                            bpb.setRepairDay2(penaltyBandViewData.getRepairDayRate2());
                            bpb.setRepairDay3(penaltyBandViewData.getRepairDayRate3());
                            bpb.setRepairPeriodStartDay1(penaltyBandViewData.getRepairPeriodStartDay1());
                            bpb.setRepairPeriodStartDay2(penaltyBandViewData.getRepairPeriodStartDay2());
                            bpb.setRepairPeriodStartDay3(penaltyBandViewData.getRepairPeriodStartDay3());
                            bpb.setBreBand(model);
                            model.addBrePenaltyBand(bpb);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exceeption thrown updating BRE penalty bands: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String deleteInsurerBreBand() {
        try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && model.getInsurer().getId() != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to delete an insurer BRE Band for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            if (model != null) {
                checkVersion(model);
                ActionResponse response;
                response = adminInsurerService.deleteInsurerBreBand(model);
                setActionResponse(response);
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

    public void setProtocolVehicleClassCeilingService(ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService) {
        this.protocolVehicleClassCeilingService = protocolVehicleClassCeilingService;
    }

    public void setBrePenaltyBandService(BrePenaltyBandService brePenaltyBandService) {
        this.brePenaltyBandService = brePenaltyBandService;
    }

    public void setClaimMatchingBandService(ClaimMatchingBandService claimMatchingBandService) {
        this.claimMatchingBandService = claimMatchingBandService;
    }

    public void setAppliedLiabilityService(AppliedLiabilityService appliedLiabilityService) {
        this.appliedLiabilityService = appliedLiabilityService;
    }
    // </editor-fold>

    public int getClaimMatchingOwnerId() {
        return this.model.getClaimMatchingOwner() != null ? this.model.getClaimMatchingOwner().getId() : 0;
    }

    public String getClaimMatchingOwnerName() {
        return this.model.getClaimMatchingOwner() != null ? this.model.getClaimMatchingOwner().getDisplayName() : "--- Please Select ---";
    }

    public void setClaimMatchingOwnerId(int claimMatchingOwnerId) {
        this.claimMatchingOwnerId = claimMatchingOwnerId;
    }

    public int getClaimMatchingWorkgroupId() {
        return this.model.getClaimMatchingWorkgroup() != null ? this.model.getClaimMatchingWorkgroup().getId() : 0;
    }

    public String getClaimMatchingWorkgroupName() {
        return this.model.getClaimMatchingWorkgroup() != null ? this.model.getClaimMatchingWorkgroup().getName() : "--- Please Select ---";
    }

    public void setClaimMatchingWorkgroupId(int claimMatchingWorkgroupId) {
        this.claimMatchingWorkgroupId = claimMatchingWorkgroupId;
    }

    public boolean isWorkgroupsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isWorkgroupEnable();
    }

    public boolean isOwnershipEnabled() {
        return adminInsurerService.getInsurer(insurerId).isClaimOwnershipEnable();
    }

    public boolean isClaimMatchingEnabled() {
        return adminInsurerService.getInsurer(insurerId).isEnableClaimMatching();
    }

    public boolean isSubscriberClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isAllowSubscriberClaims();
    }

    public boolean isCollaborationProtocolClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isAllowCollaborationProtocolClaims();
    }

    public boolean isImpecuniosRuleEnabled() {
        return adminInsurerService.getInsurer(insurerId).isEnableLouDates() || adminInsurerService.getInsurer(insurerId).isEnableManualLouDates();
    }

    public boolean isCopleyOfferEnabled() {
        return adminInsurerService.getInsurer(insurerId).isCopleyQuestion();
    }

    public boolean isFixedFeeClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isAllowFixedFeeClaims();
    }

    public boolean isTpiClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isThirdPartyInterventionActivated();
    }

    @Override
    public boolean isInsurerUploadEnabled() {
        return adminInsurerService.getInsurer(insurerId).isClaimUploadEnabled()
                || adminInsurerService.getInsurer(insurerId).isInvoiceUploadEnabled();
    }

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public boolean getAsCopy() {
        return asCopy;
    }

    public void setAsCopy(boolean asCopy) {
        this.asCopy = asCopy;
    }

    public String getClaimTypesJsonString() {
        List<LookupItem> claimTypesList = lookupService.getClaimTypes(getAuthenticatedUser());
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(claimTypesList);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting claimTypesList to json string.");
        }
        return "{totalCount:" + claimTypesList.size() + ", results:" + jsonString + "}";
    }

    public String getClaimTypesForInsurerJsonString() {
        List<LookupItem> claimTypesList = lookupService.getClaimTypes(adminInsurerService.getInsurer(insurerId));
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(claimTypesList);
        } catch (JsonProcessingException ex) {
            LOG.error("Error converting claimTypesList to json string.");
        }
        return "{totalCount:" + claimTypesList.size() + ", results:" + jsonString + "}";
    }
}
