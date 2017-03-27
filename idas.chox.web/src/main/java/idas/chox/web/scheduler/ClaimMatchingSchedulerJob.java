package idas.chox.web.scheduler;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimMatchingEntry;
import idas.chox.core.model.ClaimMatchingImportEntry;
import idas.chox.core.model.Insurer;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import idas.chox.core.model.SchedulerJob;
import idas.chox.core.services.ClaimMatchingService;
import idas.chox.service.workflow.ActivityFactory;
import idas.chox.service.workflow.activities.ClaimMatching;

public class ClaimMatchingSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(ClaimMatchingSchedulerJob.class);
    private ActivityFactory activityFactory;
    public static final String JOB_NAME = "CLAIM_MATCHING";
    private String insurerName;
    @Autowired
    private ClaimMatchingService claimMatchingService;

    @Override
    public final Map<Integer, List<String>> doJob() {
        int matchStatus;
        Insurer ins = getSecurityInfoProvider().getCurrentUser().getInsurer();
        
        if (!ins.isEnableClaimMatching()) {
            LOG.warn("Claim Matching not enabled for insurer '{}' but scheduler job is active", insurerName);
            return null;
        }
        
        try {
            LOG.debug("Checking for claim matching import entries...");
            List<ClaimMatchingImportEntry> claimMatchingImportEntries = claimMatchingService.getClaimMatchingImportEntries(insurerName);
            LOG.debug("Found {} claim matching import entries", claimMatchingImportEntries.size());
            // Start new transaction?
            if (claimMatchingImportEntries.size() > 0) {
              handleHibernateTransactionIntricacies();
            }
            for (ClaimMatchingImportEntry importEntry : claimMatchingImportEntries) {
                matchStatus = 0;
                // Find matching claim
                Claim matchedClaim = null;
                try {
                    matchedClaim = claimMatchingService.getClaimMatch(importEntry.getIncidentDate(), importEntry.getThirdPartyVehicleRegistration());
                    if (matchedClaim != null) {
                        LOG.debug("Match found for import entry with insurer number {} (id={}): id={}, choRef='{}'", new Object[]{
                            importEntry.getClaimNumber(), importEntry.getId(), matchedClaim.getId(), matchedClaim.getChoReference()});
                        // Claim Matched - start claim matching process
                        ClaimMatching activity = (ClaimMatching) activityFactory.getActivity("claimMatching");
                        activity.setClaimNumber(importEntry.getClaimNumber());
                        activity.setIncidentDate(importEntry.getIncidentDate());
                        activity.setIndemnityStance(importEntry.getIndemnityStance());
                        activity.setLiabilityInsurer(importEntry.getLiabilityInsurer());
                        activity.setLiabilityStance(importEntry.getLiabilityStance());
                        activity.setThirdPartyVehicleRegistration(importEntry.getThirdPartyVehicleRegistration());
                            
                        activity.process(matchedClaim);
                        matchStatus = matchedClaim.getMatchStatus();
                    } else if (LOG.isDebugEnabled()) {
                        LOG.debug("No Match found for import entry with insurer number {} (id={})", new Object[]{
                            importEntry.getClaimNumber(), importEntry.getId()});
                    }
                } catch (Exception ex) {
                    LOG.error("Exception thrown trying to match claim with insurer number {} (id={}): ", new Object[]{
                            importEntry.getClaimNumber(), importEntry.getId(), ex});
                }
                // Progress import entry to claim matching entry
                // If a claim matching entry already exists, then update
                ClaimMatchingEntry entry = claimMatchingService.getClaimMatchingEntry(importEntry.getClaimNumber());
                if (entry == null) {
                    LOG.debug("Creating new ClaimMatchingEntry");
                    entry = new ClaimMatchingEntry();
                    entry.setClaimNumber(entry.getClaimNumber());
                }
                entry.setClaim(matchedClaim);
                entry.setClaimNumber(importEntry.getClaimNumber());
                entry.setIncidentDate(importEntry.getIncidentDate());
                entry.setIndemnityStance(importEntry.getIndemnityStance());
                entry.setInsurer(ins);
                entry.setInsurerName(insurerName);
                entry.setLiabilityInsurer(importEntry.getLiabilityInsurer());
                entry.setLiabilityStance(importEntry.getLiabilityStance());
                entry.setThirdPartyVehicleRegistration(importEntry.getThirdPartyVehicleRegistration());
                entry.setMatchStatus(matchStatus);
                LOG.debug("Saving ClaimMatchingEntry");
                claimMatchingService.save(entry);
                LOG.debug("Deleting ClaimMatchingImportEntry");
                claimMatchingService.delete(importEntry);
            }
            if (claimMatchingImportEntries.size() > 0) {
                releaseHibernateSessionConditionally();
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown checking fo claim matching: {}", ex.getMessage(), ex);
        }

        return null;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }

    public void setInsurerName(String insurerName) {
        this.insurerName = insurerName;
    }

    public void setClaimMatchingService(ClaimMatchingService claimMatchingService) {
        this.claimMatchingService = claimMatchingService;
    }

    public void setActivityFactory(ActivityFactory activityFactory) {
        this.activityFactory = activityFactory;
    }
}
