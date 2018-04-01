package idas.chox.web.actions;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import idas.chox.core.model.Incident;
import idas.chox.core.model.Injury;
import idas.chox.service.security.TabAccessibility;


/**
 *
 * @author Emmanuel
 */
public class InjuryAction extends ClaimModelAction<Injury> {
    private static final Logger LOG = LoggerFactory.getLogger(InjuryAction.class);
    private Injury originalModel;

    @Override
    public Injury loadModel() {
        Injury injury = null;
        Incident incident = claim.getIncident();
        
        if (incident != null) {
            injury = incident.getInjury();
        }
        if (injury == null) {
            injury =  new Injury();
        }

        try {
            originalModel = (Injury) BeanUtils.cloneBean(injury);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            LOG.error("Exception cloning witness: {}", ex.getMessage(), ex);
        }

        if (claim.isHashed()) {
            if (injury.getName() != null && !injury.getName().isEmpty()) injury.setName(GDPR_REMOVED_STRING);
            if (injury.getAddress1() != null && !injury.getAddress1().isEmpty()) injury.setAddress1(GDPR_REMOVED_STRING);
            if (injury.getAddress2() != null && !injury.getAddress2().isEmpty()) injury.setAddress2(GDPR_REMOVED_STRING);
            if (injury.getAddress3() != null && !injury.getAddress3().isEmpty()) injury.setAddress3(GDPR_REMOVED_STRING);
            if (injury.getAddress4() != null && !injury.getAddress4().isEmpty()) injury.setAddress4(GDPR_REMOVED_STRING);
            if (injury.getAddress5() != null && !injury.getAddress5().isEmpty()) injury.setAddress5(GDPR_REMOVED_STRING);
            if (injury.getTelephoneDay() != null && !injury.getTelephoneDay().isEmpty()) injury.setTelephoneDay(GDPR_REMOVED_STRING);
            if (injury.getTelephoneEvening() != null && !injury.getTelephoneEvening().isEmpty()) injury.setTelephoneEvening(GDPR_REMOVED_STRING);
            if (injury.getEmail() != null && !injury.getEmail().isEmpty()) injury.setEmail(GDPR_REMOVED_STRING);
        }
        
        // If claim has been re-opened after being hashed..
        if (injury.getName() != null && injury.getName().startsWith("~~")) injury.setName(GDPR_REMOVED_STRING);
        if (injury.getAddress1() != null && injury.getAddress1().startsWith("~~")) injury.setAddress1(GDPR_REMOVED_STRING);
        if (injury.getAddress2() != null && injury.getAddress2().startsWith("~~")) injury.setAddress2(GDPR_REMOVED_STRING);
        if (injury.getAddress3() != null && injury.getAddress3().startsWith("~~")) injury.setAddress3(GDPR_REMOVED_STRING);
        if (injury.getAddress4() != null && injury.getAddress4().startsWith("~~")) injury.setAddress4(GDPR_REMOVED_STRING);
        if (injury.getAddress5() != null && injury.getAddress5().startsWith("~~")) injury.setAddress5(GDPR_REMOVED_STRING);
        if (injury.getTelephoneDay() != null && injury.getTelephoneDay().startsWith("~~")) injury.setTelephoneDay(GDPR_REMOVED_STRING);
        if (injury.getTelephoneEvening() != null && injury.getTelephoneEvening().startsWith("~~")) injury.setTelephoneEvening(GDPR_REMOVED_STRING);
        if (injury.getEmail() != null && injury.getEmail().startsWith("~~")) injury.setEmail(GDPR_REMOVED_STRING);

        return injury;
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

        incident.setInjury(model);
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
        if (claim != null) {
            if ((getIsInsurer() && claim.getInsurer().getId().intValue() != getAuthenticatedUser().getInsurer().getId().intValue())
                    || (getIsCHO() && claim.getChorganisation().getId().intValue() != getAuthenticatedUser().getChorganisation().getId().intValue())) {
                LOG.error("InjuryAction validation failed, Attempt to access a claim that you do not own.");
                throw new AccessDeniedException("Attempt to access a claim that you do not own.");
            }
            LOG.debug("InjuryAction validate success");
        }
        LOG.debug(" InjuryAction validation is not done as claim is null");
    }
}
