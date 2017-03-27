package idas.chox.web.scheduler;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.SchedulerJob;
import idas.chox.data.services.SecureDataService;

public class TotalLossChaseDbSchedulerJob extends DbSchedulerJob {

    private static final Logger LOG = LoggerFactory.getLogger(TotalLossChaseDbSchedulerJob.class);
    private static final String JOB_NAME = "TOTALLOSS_CHASE_TASK";

    @Secured({"ROLE_CHO"})
    @Override
    public Map<Integer, List<String>> doJob() {

        try {
            // Start new transaction?
            handleHibernateTransactionIntricacies();
            ((SecureDataService)claimService).setSecurityInfoProvider(((SecureDataService)claimService).getSecurityInfoProvider());
            List<Claim> claims = claimService.getTotalLossChaseClaims();
            if (claims != null && claims.size() > 0) {
                LOG.debug("total no. claims to chase is {}", claims.size());

                for (Claim claim : claims) {
                    int status = claimService.createChaseTask(claim);
                    if (status == 1 && LOG.isDebugEnabled()) {
                        LOG.debug("Chase task created for claim {} [{}]: {}",
                            new Object[]{claim.getChoReference(), claim.getId(), status});
                    }
                }

            } else {
                LOG.debug("No claims to chase.");
            }
        } catch (Exception ex) {
            LOG.warn("Exception thrown creating chase task: {}", ex.getMessage(), ex);
        } finally {
            releaseHibernateSessionConditionally();
        }
        return null;
    }

    @Override
    protected List<SchedulerJob> getSchedulerJobs() {
        return getSchedulerJobService().getSchedulerJobs(JOB_NAME);
    }
}