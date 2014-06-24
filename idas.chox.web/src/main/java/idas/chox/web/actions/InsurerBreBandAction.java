package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

import net.sf.json.JSONArray;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.ProtocolVehicleClassCeiling;
import idas.chox.core.services.ProtocolVehicleClassCeilingService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.ActionResponse;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.InsurerBreBandViewData;
import idas.chox.web.viewdata.VehicleClassCeilingViewData;
import org.apache.commons.lang3.SerializationUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsurerBreBandAction extends BaseAction implements ModelDriven<BreBand>, Preparable {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerBreBandAction.class);
    private String objectId;
    private int insurerId = -1;
    private BreBand model;
    private List<InsurerBreBandViewData> insurerBreBands;
    private AdminInsurerService adminInsurerService;
    private String protocolVehicleClassCeilingRecords;
    private VehicleClassService vehicleClassService;
    private ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService;
    private boolean asCopy;

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

    public String getJsonData() {
        JSONArray jObject = JSONArray.fromObject(this.insurerBreBands);
        return "{totalCount:" + this.insurerBreBands.size() + ",results:" + jObject.toString() + "}";
    }

    public boolean getIsNew() {

        if (objectId != null && !objectId.equalsIgnoreCase("") && Integer.valueOf(objectId) <= 0) {
            return true;
        }
        return false;
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
            insurerBreBands = new ArrayList<InsurerBreBandViewData>();
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
                adminInsurerService.evict(model);
                model = newModel;
            } else {
                checkVersion(Arrays.asList(model));
            }
            ActionResponse response;
            if (asCopy || !protocolVehicleClassCeilingRecords.isEmpty()) {
                updateProtocolVehicleClassCeiling(asCopy);
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

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String deleteInsurerBreBand() {
        try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && model.getInsurer().getId().intValue() != getUserOrganisationId())) {
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

    public void setProtocolVehicleClassCeilingService(ProtocolVehicleClassCeilingService protocolVehicleClassCeilingService) {
        this.protocolVehicleClassCeilingService = protocolVehicleClassCeilingService;
    }

    public boolean getAsCopy() {
        return asCopy;
    }

    public void setAsCopy(boolean asCopy) {
        this.asCopy = asCopy;
    }
}

