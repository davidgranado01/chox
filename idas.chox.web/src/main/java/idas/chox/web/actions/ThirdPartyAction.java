package idas.chox.web.actions;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ThirdParty;
import idas.chox.core.services.InsurerService;
import idas.chox.core.services.LookupService;
import idas.chox.core.services.VehicleClassService;
import idas.chox.service.security.TabAccessibility;

/**
 *
 * @author Emmanuel
 */
public class ThirdPartyAction extends ClaimModelAction<ThirdParty> {
    private static final Logger LOG = LoggerFactory.getLogger(ThirdPartyAction.class);

    private VehicleClassService vehicleClassService;
    private InsurerService insurerService;
    private LookupService lookupService;
  
    private int vehicleClassId;
    private List vehicleClasses;
    private List insurers;
    private ThirdParty originalModel;

    @Override
    public ThirdParty loadModel() {
        ThirdParty thirdParty = claim.getThirdParty();
        if (thirdParty == null) {
            thirdParty = new ThirdParty();
        }

        try {
//            originalModel = (ThirdParty) SerializationUtils.clone(ThirdParty); -- ising this causes a hibernate no session error when trying to access originalModel fields
            originalModel = new ThirdParty();
            originalModel.setTitle(thirdParty.getTitle());
            originalModel.setFirstName(thirdParty.getFirstName());
            originalModel.setLastName(thirdParty.getLastName());
            originalModel.setAddress1(thirdParty.getAddress1());
            originalModel.setAddress2(thirdParty.getAddress2());
            originalModel.setAddress3(thirdParty.getAddress3());
            originalModel.setAddress4(thirdParty.getAddress4());
            originalModel.setAddress5(thirdParty.getAddress5());
            originalModel.setPostcode(thirdParty.getPostcode());
            originalModel.setTelephoneDay(thirdParty.getTelephoneDay());
            originalModel.setTelephoneEvening(thirdParty.getTelephoneEvening());
            originalModel.setEmail(thirdParty.getEmail());
        } catch (Exception ex) {
            LOG.error("Exception cloning Third Party: {}", ex.getMessage(), ex);
        }

        replaceHashedStrings(thirdParty);
        
        return thirdParty;
    }

    @Override
    public String updateModel() {
        if (vehicleClassId >= 0) {
            model.setVehicleClass(this.vehicleClassService.getVehicleClass(vehicleClassId));
        }

        if (claim.isHashed()) {
            // Hashed fields should not be changed
            model.setTitle(originalModel.getTitle());
            model.setFirstName(originalModel.getFirstName());
            model.setLastName(originalModel.getLastName());
            model.setAddress1(originalModel.getAddress1());
            model.setAddress2(originalModel.getAddress2());
            model.setAddress3(originalModel.getAddress3());
            model.setAddress4(originalModel.getAddress4());
            model.setAddress5(originalModel.getAddress5());
            model.setPostcode(originalModel.getPostcode());
            model.setTelephoneDay(originalModel.getTelephoneDay());
            model.setTelephoneEvening(originalModel.getTelephoneEvening());
            model.setEmail(originalModel.getEmail());
        }
        
         // If claim has been re-opened after being hashed..
        if (GDPR_REMOVED_STRING.equals(model.getTitle())) {
            model.setTitle(originalModel.getTitle());
        }
        if (GDPR_REMOVED_STRING.equals(model.getFirstName())) model.setFirstName(originalModel.getFirstName());
        if (GDPR_REMOVED_STRING.equals(model.getLastName())) model.setLastName(originalModel.getLastName());
        if (GDPR_REMOVED_STRING.equals(model.getAddress1())) model.setAddress1(originalModel.getAddress1());
        if (GDPR_REMOVED_STRING.equals(model.getAddress2())) model.setAddress2(originalModel.getAddress2());
        if (GDPR_REMOVED_STRING.equals(model.getAddress3())) model.setAddress3(originalModel.getAddress3());
        if (GDPR_REMOVED_STRING.equals(model.getAddress4())) model.setAddress4(originalModel.getAddress4());
        if (GDPR_REMOVED_STRING.equals(model.getAddress5())) model.setAddress5(originalModel.getAddress5());
        if (GDPR_REMOVED_STRING.equals(model.getPostcode())) model.setPostcode(originalModel.getPostcode());
        if (GDPR_REMOVED_STRING.equals(model.getTelephoneDay())) model.setTelephoneDay(originalModel.getTelephoneDay());
        if (GDPR_REMOVED_STRING.equals(model.getTelephoneEvening())) model.setTelephoneEvening(originalModel.getTelephoneEvening());
        if (GDPR_REMOVED_STRING.equals(model.getEmail())) model.setEmail(originalModel.getEmail());

        claim.setThirdParty(model);

        super.updateModel();
        
        replaceHashedStrings(model);

        return SUCCESS;
    }

    @Override
    public void validate() {
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("ThirtPartyAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("ThirtPartyAction validate success");
        }
        else {
            LOG.debug(" ThirtPartyAction validation is not done as claim is null");
        }
    }


    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }

    public void setVehicleClassId(int vehicleClassId) {
        this.vehicleClassId = vehicleClassId;
    }

   

    public int getVehicleClassId() {
        return this.model.getVehicleClass() != null ? vehicleClassId = this.model.getVehicleClass().getId() : 0;
    }

   

    public void setVehicleClassService(VehicleClassService vehicleClassService) {
        this.vehicleClassService = vehicleClassService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setLookupService(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    public List getVehicleClasses() {
        if (vehicleClasses == null) {
            vehicleClasses = lookupService.getVehicleClasses();
        }
        return vehicleClasses;
    }

    public List getInsurers() {

        if (insurers == null) {

            if (this.getAuthenticatedUser().getChorganisation() != null) {
                Chorganisation currentCho = this.getAuthenticatedUser().getChorganisation();
                insurers = this.lookupService.getInsurers(currentCho.getId());
            } else {
                insurers = this.lookupService.getInsurers();
            }

        }

        return insurers;
    }

    public String getDefaultInsurerName(){

        return this.model.getInsurer().getName();
    }
    
    public void replaceHashedStrings(ThirdParty thirdParty) {
        if (thirdParty.getTitle() != null && thirdParty.getTitle().startsWith("~~")) thirdParty.setTitle(GDPR_REMOVED_STRING);
        if (thirdParty.getFirstName() != null && thirdParty.getFirstName().startsWith("~~")) thirdParty.setFirstName(GDPR_REMOVED_STRING);
        if (thirdParty.getLastName() != null && thirdParty.getLastName().startsWith("~~")) thirdParty.setLastName(GDPR_REMOVED_STRING);
        if (thirdParty.getAddress1() != null && thirdParty.getAddress1().startsWith("~~")) thirdParty.setAddress1(GDPR_REMOVED_STRING);
        if (thirdParty.getAddress2() != null && thirdParty.getAddress2().startsWith("~~")) thirdParty.setAddress2(GDPR_REMOVED_STRING);
        if (thirdParty.getAddress3() != null && thirdParty.getAddress3().startsWith("~~")) thirdParty.setAddress3(GDPR_REMOVED_STRING);
        if (thirdParty.getAddress4() != null && thirdParty.getAddress4().startsWith("~~")) thirdParty.setAddress4(GDPR_REMOVED_STRING);
        if (thirdParty.getAddress5() != null && thirdParty.getAddress5().startsWith("~~")) thirdParty.setAddress5(GDPR_REMOVED_STRING);
        if (thirdParty.getTelephoneDay() != null && thirdParty.getTelephoneDay().startsWith("~~")) thirdParty.setTelephoneDay(GDPR_REMOVED_STRING);
        if (thirdParty.getTelephoneEvening() != null && thirdParty.getTelephoneEvening().startsWith("~~")) thirdParty.setTelephoneEvening(GDPR_REMOVED_STRING);
        if (thirdParty.getEmail() != null && thirdParty.getEmail().startsWith("~~")) thirdParty.setEmail(GDPR_REMOVED_STRING);
    }

}
