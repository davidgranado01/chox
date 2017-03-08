package idas.chox.service.workflow.listeners;

import com.google.common.eventbus.Subscribe;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingBand;
import idas.chox.core.model.ClaimMatchingEntry;
import idas.chox.core.model.ClaimType;
import idas.chox.core.services.ClaimMatchingBandService;
import idas.chox.core.services.ClaimMatchingService;
import idas.chox.events.ClaimRejectionContestedEvent;
import idas.chox.events.ClaimReviewedByEngEvent;
import idas.chox.events.NewClaimEvent;
import idas.chox.events.SubscriberClaimRejectedToGtaEvent;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.ClaimMatching;

/**
 *
 * @author john
 */
@Listener
public class ClaimMatchingListener {
    private static final Logger LOG = LoggerFactory.getLogger(ClaimMatchingListener.class);
    @Autowired
    private ClaimMatchingService claimMatchingService;
    @Autowired
    private ClaimMatchingBandService claimMatchingBandService;
    @Autowired
    private ActivityFactory activityFactory;

    public void setClaimMatchingService(ClaimMatchingService claimMatchingService) {
        this.claimMatchingService = claimMatchingService;
    }

    public void setClaimMatchingBandService(ClaimMatchingBandService claimMatchingBandService) {
        this.claimMatchingBandService = claimMatchingBandService;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }

    @Handler
    @Subscribe
    public void handle(NewClaimEvent event){
        LOG.debug("NewClaimEvent Message received in ClaimMatchingListener:{}", event);
        try {
        if (event.getClaim().getInsurer().isEnableClaimMatching() && event.getClaim().getBreBand().isClaimMatchingEnable()) {
            matchclaim(event.getClaim());
        }
        } catch (Exception ex) {
            LOG.error("Exception thrown: {}", ex.getMessage(), ex);
        }
    } 

    @Handler
    @Subscribe
    public void handle(ClaimRejectionContestedEvent event){
        LOG.debug("ClaimRejectionContestedEvent Message received in ClaimMatchingListener:{}", event);
        if (event.getClaim().getInsurer().isEnableClaimMatching() && event.getClaim().getBreBand().isClaimMatchingEnable()) {
            matchclaim(event.getClaim());
        }
    } 

    @Handler
    @Subscribe
    public void handle(SubscriberClaimRejectedToGtaEvent event){
        LOG.debug("SubscriberClaimRejectedToGtaEvent Message received in ClaimMatchingListener:{}", event);
        if (event.getClaim().getInsurer().isEnableClaimMatching() && event.getClaim().getBreBand().isClaimMatchingEnable()) {
            matchclaim(event.getClaim());
        }
    } 

    @Handler
    @Subscribe
    public void handle(ClaimReviewedByEngEvent event){
        LOG.debug("ClaimReviewedByEngEvent Message received in ClaimMatchingListener:{}", event);
        if (event.getClaim().getInsurer().isEnableClaimMatching() && event.getClaim().getBreBand().isClaimMatchingEnable()) {
            matchclaim(event.getClaim());
        }
    } 

    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, value = "transactionManager")
    private void matchclaim(Claim claim) {
        // Do not perform claim matching on supplementary claims
        if (ClaimType.isSupplementaryInvoice(claim.getClaimType()) && !ClaimType.isOriginalSupplementaryInvoice(claim.getClaimType())) {
            return;
        }
        LOG.debug("Attempting to claim match claim:{}", claim.getChoReference());
        ClaimMatchingBand claimMatchingBand = claimMatchingBandService.getClaimMatchingBand(claim.getBreBand().getId(),
                    claim.getClaimType(), claim.getCustomer().getVehicleClass().getName());
        if (claimMatchingBand == null) {
            LOG.debug("No claim matching band found for claim '{}' with type '{}' and vehicle class '{}'",
                    new Object[]{claim.getChoReference(), claim.getClaimType(), claim.getCustomer().getVehicleClass().getName()});
            return;
        }
        ClaimMatchingEntry matchedClaim = claimMatchingService.getClaimMatchingEntry(claim.getIncident().getDate(),
                    claim.getThirdParty().getVehicleRegistration());
        if (matchedClaim == null) {
            LOG.debug("No claim match found for claim '{} with incident date {} and vehicle class '{}'",
                    new Object[]{claim.getIncident().getDate(), claim.getCustomer().getVehicleRegistration()});
            return;
        }
        
        // Claim Matched - start claim matching process (via activity)
        LOG.debug("Found matched claim in claim matching db: {}", matchedClaim);
        ClaimMatching activity = (ClaimMatching) activityFactory.getActivity("claimMatching");
        activity.setClaimNumber(matchedClaim.getClaimNumber());
        activity.setIncidentDate(matchedClaim.getIncidentDate());
        activity.setIndemnityStance(matchedClaim.getIndemnityStance());
        activity.setLiabilityInsurer(matchedClaim.getLiabilityInsurer());
        activity.setLiabilityStance(matchedClaim.getLiabilityStance());
        activity.setThirdPartyVehicleRegistration(matchedClaim.getThirdPartyVehicleRegistration());

        try {
            activity.processInBatch(claim);
        } catch (Exception ex) {
            LOG.error("Exception thrown by ClaimMatching activity: {}", ex.getMessage(), ex);
            return;
        }
        matchedClaim.setMatchStatus(claim.getMatchStatus());
        claimMatchingService.save(matchedClaim);
    }
        
}
