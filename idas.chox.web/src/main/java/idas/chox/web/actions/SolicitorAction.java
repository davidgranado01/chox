package idas.chox.web.actions;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import idas.chox.core.model.Solicitor;
import idas.chox.core.model.Incident;
import idas.chox.service.security.TabAccessibility;
import idas.chox.core.model.Injury;

/**
 *
 * @author Emmanuel
 */
public class SolicitorAction extends ClaimModelAction<Injury> {
    private static final Logger LOG = LoggerFactory.getLogger(SolicitorAction.class);
    private Solicitor originalModel;

    @Override
    public Injury loadModel() {
        Injury injury = null;
        
        Incident incident = claim.getIncident();
        if (incident != null) {
            injury = incident.getInjury();
        }
        if (injury == null) {
            injury = new Injury();
        }
        try {
            originalModel = (Solicitor) BeanUtils.cloneBean(injury.getSolicitor());
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            LOG.error("Exception cloning witness: {}", ex.getMessage(), ex);
        }

        if (claim.isHashed() && injury.getSolicitor() != null) {
            if (injury.getSolicitor().getName() != null && !injury.getSolicitor().getName().isEmpty()) injury.getSolicitor().setName(GDPR_REMOVED_STRING);
            if (injury.getSolicitor().getAddress1() != null && !injury.getSolicitor().getAddress1().isEmpty()) injury.getSolicitor().setAddress1(GDPR_REMOVED_STRING);
            if (injury.getSolicitor().getAddress2() != null && !injury.getSolicitor().getAddress2().isEmpty()) injury.getSolicitor().setAddress2(GDPR_REMOVED_STRING);
            if (injury.getSolicitor().getAddress3() != null && !injury.getSolicitor().getAddress3().isEmpty()) injury.getSolicitor().setAddress3(GDPR_REMOVED_STRING);
            if (injury.getSolicitor().getAddress4() != null && !injury.getSolicitor().getAddress4().isEmpty()) injury.getSolicitor().setAddress4(GDPR_REMOVED_STRING);
            if (injury.getSolicitor().getAddress5() != null && !injury.getSolicitor().getAddress5().isEmpty()) injury.getSolicitor().setAddress5(GDPR_REMOVED_STRING);
            if (injury.getSolicitor().getTelephone() != null && !injury.getSolicitor().getTelephone().isEmpty()) injury.getSolicitor().setTelephone(GDPR_REMOVED_STRING);
            if (injury.getSolicitor().getEmail() != null && !injury.getSolicitor().getEmail().isEmpty()) injury.getSolicitor().setEmail(GDPR_REMOVED_STRING);
        }

        // If claim has been re-opened after being hashed..
        if (injury.getSolicitor().getName() != null && injury.getSolicitor().getName().startsWith("~~")) injury.getSolicitor().setName(GDPR_REMOVED_STRING);
        if (injury.getSolicitor().getAddress1() != null && injury.getSolicitor().getAddress1().startsWith("~~")) injury.getSolicitor().setAddress1(GDPR_REMOVED_STRING);
        if (injury.getSolicitor().getAddress2() != null && injury.getSolicitor().getAddress2().startsWith("~~")) injury.getSolicitor().setAddress2(GDPR_REMOVED_STRING);
        if (injury.getSolicitor().getAddress3() != null && injury.getSolicitor().getAddress3().startsWith("~~")) injury.getSolicitor().setAddress3(GDPR_REMOVED_STRING);
        if (injury.getSolicitor().getAddress4() != null && injury.getSolicitor().getAddress4().startsWith("~~")) injury.getSolicitor().setAddress4(GDPR_REMOVED_STRING);
        if (injury.getSolicitor().getAddress5() != null && injury.getSolicitor().getAddress5().startsWith("~~")) injury.getSolicitor().setAddress5(GDPR_REMOVED_STRING);
        if (injury.getSolicitor().getTelephone() != null && injury.getSolicitor().getTelephone().startsWith("~~")) injury.getSolicitor().setTelephone(GDPR_REMOVED_STRING);
        if (injury.getSolicitor().getEmail() != null && injury.getSolicitor().getEmail().startsWith("~~")) injury.getSolicitor().setEmail(GDPR_REMOVED_STRING);

        return injury;
    }

    @Override
    public String updateModel() {
        Incident incident = claim.getIncident();
        if (incident == null) {
            incident = new Incident();
        }
        if (claim.isHashed() && model.getSolicitor() != null) {
            // Hashed fields should not be changed
            model.getSolicitor().setName(originalModel.getName());
            model.getSolicitor().setAddress1(originalModel.getAddress1());
            model.getSolicitor().setAddress2(originalModel.getAddress2());
            model.getSolicitor().setAddress3(originalModel.getAddress3());
            model.getSolicitor().setAddress4(originalModel.getAddress4());
            model.getSolicitor().setAddress5(originalModel.getAddress5());
            model.getSolicitor().setPostcode(originalModel.getPostcode());
            model.getSolicitor().setTelephone(originalModel.getTelephone());
            model.getSolicitor().setEmail(originalModel.getEmail());
        }

         // If claim has been re-opened after being hashed..
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getName())) model.getSolicitor().setName(originalModel.getName());
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getAddress1())) model.getSolicitor().setAddress1(originalModel.getAddress1());
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getAddress2())) model.getSolicitor().setAddress2(originalModel.getAddress2());
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getAddress3())) model.getSolicitor().setAddress3(originalModel.getAddress3());
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getAddress4())) model.getSolicitor().setAddress4(originalModel.getAddress4());
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getAddress5())) model.getSolicitor().setAddress5(originalModel.getAddress5());
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getPostcode())) model.getSolicitor().setPostcode(originalModel.getPostcode());
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getTelephone())) model.getSolicitor().setTelephone(originalModel.getTelephone());
        if (GDPR_REMOVED_STRING.equals(model.getSolicitor().getEmail())) model.setEmail(originalModel.getEmail());

        incident.setInjury(model);
        model.setIncident(incident);
        claim.setIncident(incident);

        return super.updateModel();
    }

    @Override
    String getTabName() {
        return TabAccessibility.TAB_CLAIM_DETAIL;
    }
}
