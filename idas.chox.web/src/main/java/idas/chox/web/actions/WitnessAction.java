package idas.chox.web.actions;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Witness;
import idas.chox.service.security.TabAccessibility;


public class WitnessAction extends ClaimModelAction<Witness> {

    private static final Logger LOG = LoggerFactory.getLogger(WitnessAction.class);
    private Witness originalModel;

    @Override
    public Witness loadModel() {

        Incident incident = claim.getIncident();
        Witness witness = null;
        
        if (incident != null) {
            witness = incident.getWitness();
        }
        if (witness == null) {
            witness = new Witness();
        }

        try {
            originalModel = (Witness) BeanUtils.cloneBean(witness);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            LOG.error("Exception cloning witness: {}", ex.getMessage(), ex);
        }

        if (claim.isHashed()) {
            if (witness.getName() != null && !witness.getName().isEmpty()) witness.setName(GDPR_REMOVED_STRING);
            if (witness.getAddress1() != null && !witness.getAddress1().isEmpty()) witness.setAddress1(GDPR_REMOVED_STRING);
            if (witness.getAddress2() != null && !witness.getAddress2().isEmpty()) witness.setAddress2(GDPR_REMOVED_STRING);
            if (witness.getAddress3() != null && !witness.getAddress3().isEmpty()) witness.setAddress3(GDPR_REMOVED_STRING);
            if (witness.getAddress4() != null && !witness.getAddress4().isEmpty()) witness.setAddress4(GDPR_REMOVED_STRING);
            if (witness.getAddress5() != null && !witness.getAddress5().isEmpty()) witness.setAddress5(GDPR_REMOVED_STRING);
            if (witness.getTelephoneDay() != null && !witness.getTelephoneDay().isEmpty()) witness.setTelephoneDay(GDPR_REMOVED_STRING);
            if (witness.getTelephoneEvening() != null && !witness.getTelephoneEvening().isEmpty()) witness.setTelephoneEvening(GDPR_REMOVED_STRING);
            if (witness.getEmail() != null && !witness.getEmail().isEmpty()) witness.setEmail(GDPR_REMOVED_STRING);
        }

        // If claim has been re-opened after being hashed..
        if (witness.getName() != null && witness.getName().startsWith("~~")) witness.setName(GDPR_REMOVED_STRING);
        if (witness.getAddress1() != null && witness.getAddress1().startsWith("~~")) witness.setAddress1(GDPR_REMOVED_STRING);
        if (witness.getAddress2() != null && witness.getAddress2().startsWith("~~")) witness.setAddress2(GDPR_REMOVED_STRING);
        if (witness.getAddress3() != null && witness.getAddress3().startsWith("~~")) witness.setAddress3(GDPR_REMOVED_STRING);
        if (witness.getAddress4() != null && witness.getAddress4().startsWith("~~")) witness.setAddress4(GDPR_REMOVED_STRING);
        if (witness.getAddress5() != null && witness.getAddress5().startsWith("~~")) witness.setAddress5(GDPR_REMOVED_STRING);
        if (witness.getTelephoneDay() != null && witness.getTelephoneDay().startsWith("~~")) witness.setTelephoneDay(GDPR_REMOVED_STRING);
        if (witness.getTelephoneEvening() != null && witness.getTelephoneEvening().startsWith("~~")) witness.setTelephoneEvening(GDPR_REMOVED_STRING);
        if (witness.getEmail() != null && witness.getEmail().startsWith("~~")) witness.setEmail(GDPR_REMOVED_STRING);

        return witness;
    }

    @Override
    public String updateModel() {
        Incident incident = claim.getIncident();
        if (incident == null) {
            incident = new Incident();
        }
        if (claim.isHashed()) {
            // Hashed fields should not be changed
            model.setName(originalModel.getName());
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
        if (GDPR_REMOVED_STRING.equals(model.getName())) model.setName(originalModel.getName());
        if (GDPR_REMOVED_STRING.equals(model.getAddress1())) model.setAddress1(originalModel.getAddress1());
        if (GDPR_REMOVED_STRING.equals(model.getAddress2())) model.setAddress2(originalModel.getAddress2());
        if (GDPR_REMOVED_STRING.equals(model.getAddress3())) model.setAddress3(originalModel.getAddress3());
        if (GDPR_REMOVED_STRING.equals(model.getAddress4())) model.setAddress4(originalModel.getAddress4());
        if (GDPR_REMOVED_STRING.equals(model.getAddress5())) model.setAddress5(originalModel.getAddress5());
        if (GDPR_REMOVED_STRING.equals(model.getPostcode())) model.setPostcode(originalModel.getPostcode());
        if (GDPR_REMOVED_STRING.equals(model.getTelephoneDay())) model.setTelephoneDay(originalModel.getTelephoneDay());
        if (GDPR_REMOVED_STRING.equals(model.getTelephoneEvening())) model.setTelephoneEvening(originalModel.getTelephoneEvening());
        if (GDPR_REMOVED_STRING.equals(model.getEmail())) model.setEmail(originalModel.getEmail());

        incident.setWitness(model);
        model.setIncident(incident);
        claim.setIncident(incident);

        return super.updateModel();
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }
    
    @Override
    public void validate() {

        if (claim != null && (claim.getChorganisation() != null || claim.getInsurer() != null)) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("Validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("WitnessAction validated");
        } else {
            LOG.info(" WitnessAction validation not done as claim or cho or insurer is null");
        }
    }
}
