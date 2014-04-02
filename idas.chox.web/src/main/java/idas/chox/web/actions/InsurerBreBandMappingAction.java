package idas.chox.web.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;

import net.sf.json.JSONArray;

import idas.chox.core.model.BreBand;
import idas.chox.core.model.BreBandOrganisation;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.services.BreBandOrganisationService;
import idas.chox.service.admin.AdminInsurerService;
import idas.chox.web.viewdata.BreBandChorganisationViewData;
import idas.chox.web.viewdata.ChorganisationViewData;
import org.hibernate.StaleObjectStateException;

public class InsurerBreBandMappingAction extends BaseAction {

    private static final Logger LOG = LoggerFactory.getLogger(InsurerBreBandMappingAction.class);
    private int insurerId = -1;
    private int breBandId = -1;
    private int chorganisationId = -1;
    private int breBandChorganisationId = -1;
    private String jsonRecords;
    private AdminInsurerService adminInsurerService;
    private BreBandOrganisationService breBandOrganisationService;

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String doRenderActionPage() {
        return SUCCESS;
    }

    public String getJsonData() {
        return this.jsonRecords;
    }

    public void setJsonData(Object object, Integer recordSize) {
        JSONArray jObject = JSONArray.fromObject(object);
        this.jsonRecords = "{totalCount:" + recordSize + ",results:" + jObject.toString() + "}";
    }

    // <editor-fold defaultstate="collapsed" desc="GET SET">
    public int getBreBandChorganisationId() {
        return breBandChorganisationId;
    }

    public void setBreBandChorganisationId(int breBandChorganisationId) {
        this.breBandChorganisationId = breBandChorganisationId;
    }

    public BreBandOrganisationService getBreBandOrganisationService() {
        return breBandOrganisationService;
    }

    public void setBreBandOrganisationService(BreBandOrganisationService breBandOrganisationService) {
        this.breBandOrganisationService = breBandOrganisationService;
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

    public int getBreBandId() {
        return breBandId;
    }

    public void setBreBandId(int breBandId) {
        this.breBandId = breBandId;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="ACTIONS">

    public String getChorganisationsByInsurerIdWithoutBreBand() {

        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Trying to add BRE Band mapping for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
        }
        //try {

        List<ChorganisationViewData> credithireorganisation = new ArrayList<ChorganisationViewData>();

        if (this.insurerId > 0) {

            List<Chorganisation> chorganisations = adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(this.insurerId);

            for (Chorganisation object : chorganisations) {
                // If Insurer admin logged in then show only the active cho in the 'Available CHO' list of 'BRE Band Mapping'.
                // But Chox admin can see all the active and inactive cho in the 'Available CHO' list of 'BRE Band Mapping'. 
                // Please check bug#2785.
                if (getUserOrganisationType() == 2) {
                    if (object.isStatus()) {
                        credithireorganisation.add(new ChorganisationViewData(object));
                    }
                } else {
                    credithireorganisation.add(new ChorganisationViewData(object));
                }
            }

            setJsonData(credithireorganisation, credithireorganisation.size());

        }

        //} catch (Exception ex) {
        //  handleException(this, ex);
        //return ERROR;
        //}

        return SUCCESS;
    }

    public String getChorganisationWithBreBandAssigned() {
        if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != getUserOrganisationId())) {
            throw new AccessDeniedException("Trying to add BRE Band mapping for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
        }

        try {

            List<BreBandChorganisationViewData> insurerBreBand;
            List<BreBandOrganisation> brebandorganisations = new ArrayList<BreBandOrganisation>();
            brebandorganisations = adminInsurerService.getBreBandChorganisationsByBreBandId(this.breBandId);
            insurerBreBand = getChoViewDataList(brebandorganisations);
            setJsonData(insurerBreBand, insurerBreBand.size());

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String addBreBandChorganisation() {

        try {
            if (getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && this.insurerId != -1 && this.insurerId != getUserOrganisationId())
                    || (getUserOrganisationType() == 2 && !canAddBreBandChorganisation(this.chorganisationId))) {
                throw new AccessDeniedException("Trying to add BRE Band mapping for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }
            // Now check that the breBandId belongs to this insurer
            BreBand band = adminInsurerService.getBreBand(breBandId);
            if (band == null || (getUserOrganisationType() == 2 && band.getInsurer().getId().intValue() != getUserOrganisationId())) {
                throw new AccessDeniedException("Trying to add BRE Band mapping to an insurer that doen't own the band (POSSIBLE HACK ATTEMPT)");
            }
            // check the breband organisation already added by another concurrent user.
            List<BreBandOrganisation> brebandorganisations = adminInsurerService.getBreBandChorganisationsByBreBandId(this.breBandId);
            if (brebandorganisations.size() > 0) {
                for (BreBandOrganisation breBandOrganisation : brebandorganisations) {
                    if (breBandOrganisation.getChorganisation().getId().compareTo(this.chorganisationId) == 0) {
                        throw new Exception("Record was updated by another transaction/user, please try again.",
                                new StaleObjectStateException(breBandOrganisation.getClass().getSimpleName().concat("Version"), breBandOrganisation.getId()));
                    }
                }
            }

            adminInsurerService.addBreBandChorganisation(this.breBandId, this.chorganisationId);
        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    private boolean canAddBreBandChorganisation(int chorganisationId) {
        int myInsurerId = this.insurerId;

        if (myInsurerId == -1) {
            myInsurerId = getUserOrganisationId();
        }

        if (myInsurerId > 0) {

            List<Chorganisation> chorganisations = adminInsurerService.getChorganisationsWithoutBreBandByInsurerId(myInsurerId);

            for (Chorganisation object : chorganisations) {
                if (object.getId() == chorganisationId) {
                    return true;
                }
            }
        } else {
            LOG.warn("No insurerId - cannot verify if allowed");
        }

        LOG.debug("Cannot add BreBand CHO as CHO not available (already mapped)");

        return false;
    }

    @Secured({"ROLE_CHOX_ADMIN", "ROLE_INS_ADMIN"})
    public String deleteBreBandChorganisation() {

        try {
            // Check that we can delete this BRE Band CHO
//            if ( getUserOrganisationType() == 3 || (getUserOrganisationType() == 2 && !canDeleteBreBandChorganisation(this.breBandChorganisationId))) {
            if (getUserOrganisationType() == 3) {
                throw new AccessDeniedException("Trying to delete BRE Band mapping for an insurer that isn't mine (POSSIBLE HACK ATTEMPT)");
            }

            if (this.breBandChorganisationId > 0) {
                // check breband organisation exists before remove because concurrent user might have removed. 
                BreBandOrganisation breBandOrganisation = breBandOrganisationService.getBreBandOrganisation(breBandChorganisationId);
                if (breBandOrganisation != null) {
                    breBandOrganisationService.deleteBreBandOrganisation(breBandOrganisation);
                } else {
                    throw new Exception("Record was updated by another transaction/user, please try again.",
                                new StaleObjectStateException(BreBandOrganisation.class.getSimpleName().concat("Version"), 0));
                }
                    
            }

        } catch (Exception ex) {
            handleException(ex);
            return ERROR;
        }

        return SUCCESS;
    }

    // This doesn't currently work as the breBandId is not set
    private boolean canDeleteBreBandChorganisation(int breBandChorganisationId) {
        LOG.debug("Checking if canDeleteBreBandChorganisation for breBandId={}, breBandChorganisationId={}", this.breBandId, breBandChorganisationId);
        List<BreBandChorganisationViewData> chos = getChoViewDataList(adminInsurerService.getBreBandChorganisationsByBreBandId(this.breBandId));
        for (Iterator<BreBandChorganisationViewData> i = chos.iterator(); i.hasNext();) {
            BreBandChorganisationViewData vd = i.next();
            if (vd.getId() == breBandChorganisationId) {
                return true;
            }
            LOG.debug("No match: {] != {}", vd.getId(), breBandChorganisationId);
        }


        return false;
    }

    public List<BreBandChorganisationViewData> getChoViewDataList(List<BreBandOrganisation> objects) {
        List<BreBandChorganisationViewData> breBandChorganisationViewDatas = new ArrayList<BreBandChorganisationViewData>();
        for (BreBandOrganisation h : objects) {
            breBandChorganisationViewDatas.add(new BreBandChorganisationViewData(h));
        }
        return breBandChorganisationViewDatas;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SERVICES">

    public void setAdminInsurerService(AdminInsurerService adminInsurerService) {
        this.adminInsurerService = adminInsurerService;
    }
    // </editor-fold>
}
