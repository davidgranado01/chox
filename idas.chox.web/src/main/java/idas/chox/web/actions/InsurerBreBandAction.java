package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;


import idas.chox.core.model.BreBand;
import idas.chox.core.model.BrePenaltyBand;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.LookupItem;
import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.services.BrePenaltyBandService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.ProtocolVehicleClassCeilingService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.BrePenaltyBandViewData;
import idas.chox.web.viewdata.InsurerBreBandViewData;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.SerializationUtils;

public class InsurerBreBandAction extends BaseAction implements ModelDriven<BreBand>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerBreBandAction.class);
    private String objectId;
    private int insurerId = -1;
    private BreBand model;
    private List<InsurerBreBandViewData> insurerBreBands;
    private AdminInsurerService adminInsurerService;
    private String protocolVehicleClassCeilingRecords;
    private String penaltyBandRecords;
    private VehicleClassService vehicleClassService;
    private ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService;
    private BrePenaltyBandService brePenaltyBandService;
    private boolean asCopy;
    private LookupService lookupService;

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        updateModelInSession(Arrays.asList(model));
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

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerBreBands);
        return "{totalCount:" + this.insurerBreBands.size() + ",results:" + jObject.toString() + "}";
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

            model = new BreBand();

            if (objectId != null && !objectId.equalsIgnoreCase("")) {
                if (Integer.valueOf(objectId) > 0) {
                    model = adminInsurerService.getBreBand(Integer.valueOf(this.objectId));
                    addModelToSession(Arrays.asList(model));
                }
            }

        } catch (Exception ex) {
            handleException(ex);
        }
    }

    public String getInsurerBreBands() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Trying to get the insurer BRE Bands for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
        }

        try {

            List<BreBand> insurerBreBandData = adminInsurerService.getInsurerBreBands(this.insurerId);
            insurerBreBands = new ArrayList<>();
            for (BreBand h : insurerBreBandData) {
                insurerBreBands.add(new InsurerBreBandViewData(h));
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String updateInsurerBreBand() {

        try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to update an insurer BRE Band for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            if (asCopy) {
                objectId = "-1";
                BreBand newModel = (BreBand) SerializationUtils.clone(model);
                newModel.setId(null);
                newModel.setProtocolVehicleClassCeilings(null);
                List<ProtocolVehicleClassCeiling> protocolVehicleClassCeilings = model.getProtocolVehicleClassCeilings();
                for (ProtocolVehicleClassCeiling protocolVehicleClassCeiling : protocolVehicleClassCeilings) {
                    adminInsurerService.evict(protocolVehicleClassCeiling);
                }
                newModel.setBrePenaltyBands(null);
                List<BrePenaltyBand> brePenaltyBands = model.getBrePenaltyBands();
                for (BrePenaltyBand brePenaltyBand : brePenaltyBands) {
                    adminInsurerService.evict(brePenaltyBand);
                }
                adminInsurerService.evict(model);
                model = newModel;
            } else {
                checkVersion(Arrays.asList(model));
            }
            ActionResponse response;
            if (asCopy || !protocolVehicleClassCeilingRecords.isEmpty()) {
                updateProtocolVehicleClassCeiling(asCopy);
            }
            if (asCopy || !penaltyBandRecords.isEmpty()) {
                updatePenaltyBands(asCopy);
            }
            response = adminInsurerService.updateInsurerBreBand(model, this.insurerId, getIsNew());
            updateModelInSession(Arrays.asList(model));
            setActionResponse(response);
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    private void updateProtocolVehicleClassCeiling(boolean asCopy) {
        List<VehicleClassCeilingViewData> vehicleClassCeilingViewDatas =
                ((List<VehicleClassCeilingViewData>) new Gson().fromJson(protocolVehicleClassCeilingRecords, new TypeToken<List<VehicleClassCeilingViewData>>() {}.getType()));
        if (vehicleClassCeilingViewDatas != null) {
            for (VehicleClassCeilingViewData vehicleClassCeilingViewData : vehicleClassCeilingViewDatas) {
                ProtocolVehicleClassCeiling pvcc;
                if (!asCopy && vehicleClassCeilingViewData.getId() > 0) {
                    pvcc = protocolVehicleClassCeilingService.getProtocolVehicleClassCeiling(vehicleClassCeilingViewData.getId());
                } else if (asCopy || model.getId() == null || (pvcc = protocolVehicleClassCeilingService.getProtocolVehicleClassCeilingByVehicleClass(vehicleClassCeilingViewData.getVehicleClassId(), model.getId())) == null) {
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
            }
        }
    }

    private void updatePenaltyBands(boolean asCopy) throws Exception {
        try {
            List<BrePenaltyBandViewData> penaltyBandViewDatas
                    = ((List<BrePenaltyBandViewData>) new Gson().fromJson(penaltyBandRecords, new TypeToken<List<BrePenaltyBandViewData>>() {
                    }.getType()));
            if (penaltyBandViewDatas != null) {
                for (BrePenaltyBandViewData penaltyBandViewData : penaltyBandViewDatas) {
                    BrePenaltyBand bpb;
                    if (!asCopy && penaltyBandViewData.getId() > 0) {
                        bpb = brePenaltyBandService.getBrePenaltyBand(penaltyBandViewData.getId());
                    } else if (asCopy || model.getId() == null || (bpb = brePenaltyBandService.getBrePenaltyBand(penaltyBandViewData.getId())) == null) {
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
                checkVersion(Arrays.asList(model));
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
    // </editor-fold>

    public boolean isSubscriberClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isAllowSubscriberClaims();
    }
    
    public boolean isCollaborationProtocolClaimsEnabled() {
        return adminInsurerService.getInsurer(insurerId).isAllowCollaborationProtocolClaims();
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
        String claimTypesJson = JSONArray.fromObject(claimTypesList).toString();
        return "{totalCount:" + claimTypesList.size() + ", results:" + claimTypesJson + "}";
    }
    
    public String getClaimTypesForInsurerJsonString() {
        List<LookupItem> claimTypesList = lookupService.getClaimTypes(adminInsurerService.getInsurer(insurerId));
        String claimTypesJson = JSONArray.fromObject(claimTypesList).toString();
        return "{totalCount:" + claimTypesList.size() + ", results:" + claimTypesJson + "}";
    }
}

