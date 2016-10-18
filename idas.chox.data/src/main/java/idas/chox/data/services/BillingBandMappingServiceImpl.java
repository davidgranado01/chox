package idas.chox.data.services;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import idas.chox.core.model.ChoBillingBandMapping;
import idas.chox.core.model.Chorganisation;
import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.InsurerBillingBandMapping;
import idas.chox.core.model.InsurerChorganisation;
import idas.chox.core.services.BillingBandMappingService;
import idas.chox.core.services.ChorganisationService;
import idas.chox.core.services.InsurerChorganisationService;
import idas.chox.core.services.InsurerService;

/**
 * @author john
 */
public class BillingBandMappingServiceImpl extends SecureDataService implements BillingBandMappingService {
    InsurerChorganisationService insurerChorganisationService;
    InsurerService insurerService;
    ChorganisationService chorganisationService;

    public void setInsurerChorganisationService(InsurerChorganisationService insurerChorganisationService) {
        this.insurerChorganisationService = insurerChorganisationService;
    }

    public void setInsurerService(InsurerService insurerService) {
        this.insurerService = insurerService;
    }

    public void setChorganisationService(ChorganisationService chorganisationService) {
        this.chorganisationService = chorganisationService;
    }
    
    @Override
    public List<InsurerBillingBandMapping> getInsurerBillingBandMappings(int insurerId, int bandId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerBillingBandMapping.class);
        criteria.add(Restrictions.eq("insurerBillingBand.id", bandId));
        return findByCriteria(criteria);
    }

    @Override
    public List<InsurerBillingBandMapping> getInsurerBillingBandMappings(int insurerId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(InsurerBillingBandMapping.class);
        criteria.createCriteria("insurerBillingBand").add(Restrictions.eq("insurer.id", insurerId));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public List<ChoBillingBandMapping> getChoBillingBandMappings(int choId, int bandId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChoBillingBandMapping.class);
        criteria.add(Restrictions.eq("choBillingBand.id", bandId));
        return findByCriteria(criteria);
    }

    @Override
    public List<ChoBillingBandMapping> getChoBillingBandMappings(int choId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ChoBillingBandMapping.class);
        criteria.createCriteria("choBillingBand").add(Restrictions.eq("chorganisation.id", choId));
        criteria.addOrder(Order.asc("id"));
        return findByCriteria(criteria);
    }

    @Override
    public List<InsurerBillingBandMapping> getAvailableInsurerBillingBandMappings(int insurerId) {
        List<InsurerBillingBandMapping> result = new ArrayList<>();
        Insurer ins = insurerService.getInsurer(insurerId);
        
        // Fist we need a list of active CHOs mapped to the insurer
        List<InsurerChorganisation> chorganisationsData = insurerChorganisationService.getInsurerChorganisations(insurerId, null);
        for (InsurerChorganisation ic : chorganisationsData) {
            Chorganisation cho = ic.getChorganisation();
            if (cho.isStatus()) {
                if (cho.isInsurerUploadOnly()) {
                    if (ins.isClaimUploadEnabled() || ins.isInvoiceUploadEnabled()) {
                        InsurerBillingBandMapping m = new InsurerBillingBandMapping();
                        m.setChorganisation(cho);
                        m.setClaimType(ClaimType.INSURER_UPLOAD);
                        result.add(m);
                    }
                } else {
                    InsurerBillingBandMapping m = new InsurerBillingBandMapping();
                    m.setChorganisation(cho);
                    m.setClaimType(ClaimType.GTA);
                    result.add(m);
                    m = new InsurerBillingBandMapping();
                    m.setChorganisation(cho);
                    m.setClaimType(ClaimType.INSURER_VS_INSURER);
                    result.add(m);
                    if (cho.isEnableCollaborationProtocolClaims() && ins.isAllowCollaborationProtocolClaims()) {
                        m = new InsurerBillingBandMapping();
                        m.setChorganisation(cho);
                        m.setClaimType(ClaimType.COLLABORATION_PROTOCOL);
                        result.add(m);
                    }
                    if (cho.isEnableFixedFeeClaims() && ins.isAllowFixedFeeClaims()) {
                        m = new InsurerBillingBandMapping();
                        m.setChorganisation(cho);
                        m.setClaimType(ClaimType.FIXED_FEE);
                        result.add(m);
                    }
                    if (cho.isEnableSubscriberClaims()&& ins.isAllowSubscriberClaims()) {
                        m = new InsurerBillingBandMapping();
                        m.setChorganisation(cho);
                        m.setClaimType(ClaimType.SUBSCRIBER);
                        result.add(m);
                    }
                    if (cho.isThirdPartyInterventionActivated()&& ins.isThirdPartyInterventionActivated()) {
                        m = new InsurerBillingBandMapping();
                        m.setChorganisation(cho);
                        m.setClaimType(ClaimType.TPI);
                        result.add(m);
                    }
                }
            }
        }
        
        return result;
    }

    @Override
    public List<ChoBillingBandMapping> getAvailableChoBillingBandMappings(int choId) {
        List<ChoBillingBandMapping> result = new ArrayList<>();
        Chorganisation cho = chorganisationService.getChorganisation(choId);
        
        // Fist we need a list of active CHOs mapped to the insurer
        List<InsurerChorganisation> chorganisationsData = insurerChorganisationService.getInsurerChorganisations(null, choId);
        for (InsurerChorganisation ic : chorganisationsData) {
            Insurer ins = ic.getInsurer();
            if (ins.isStatus()) {
                if (cho.isInsurerUploadOnly()) {
                    if (ins.isClaimUploadEnabled() || ins.isInvoiceUploadEnabled()) {
                        ChoBillingBandMapping m = new ChoBillingBandMapping();
                        m.setInsurer(ins);
                        m.setClaimType(ClaimType.INSURER_UPLOAD);
                        result.add(m);
                    }
                } else {
                    ChoBillingBandMapping m = new ChoBillingBandMapping();
                    m.setInsurer(ins);
                    m.setClaimType(ClaimType.GTA);
                    result.add(m);
                    m = new ChoBillingBandMapping();
                    m.setInsurer(ins);
                    m.setClaimType(ClaimType.INSURER_VS_INSURER);
                    result.add(m);
                    if (cho.isEnableCollaborationProtocolClaims() && ins.isAllowCollaborationProtocolClaims()) {
                        m = new ChoBillingBandMapping();
                        m.setInsurer(ins);
                        m.setClaimType(ClaimType.COLLABORATION_PROTOCOL);
                        result.add(m);
                    }
                    if (cho.isEnableFixedFeeClaims() && ins.isAllowFixedFeeClaims()) {
                        m = new ChoBillingBandMapping();
                        m.setInsurer(ins);
                        m.setClaimType(ClaimType.FIXED_FEE);
                        result.add(m);
                    }
                    if (cho.isEnableSubscriberClaims()&& ins.isAllowSubscriberClaims()) {
                        m = new ChoBillingBandMapping();
                        m.setInsurer(ins);
                        m.setClaimType(ClaimType.SUBSCRIBER);
                        result.add(m);
                    }
                    if (cho.isThirdPartyInterventionActivated()&& ins.isThirdPartyInterventionActivated()) {
                        m = new ChoBillingBandMapping();
                        m.setInsurer(ins);
                        m.setClaimType(ClaimType.TPI);
                        result.add(m);
                    }
                }
            }
        }
        
        return result;
    }
    
}
