package idas.chox.web.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.quartz.JobExecutionException;

import idas.chox.data.services.BaseDataService;

/**
 *
 * @author John
 */
public class StoredProcSchedulerJob implements Scheduler {
    private static final Logger LOG = LoggerFactory.getLogger(StoredProcSchedulerJob.class);
    private String storedProcName;
    private BaseDataService baseDataService;
    
    @Override
    public void execute() throws JobExecutionException {
        LOG.info("calling stored proc '{}' with '{}'", storedProcName, baseDataService);
        try {
            if ("addInvoicePenaltyTask".equals(storedProcName)) {
                baseDataService.callAddInvoicePenaltyTask(999);
            }
            else if ("addMissingEcdTask".equals(storedProcName)) {
                baseDataService.callAddMissingEcdTask(999);
            }
            else if ("applyAutoPenaltyCharge".equals(storedProcName)) {
                baseDataService.callApplyAutoPenaltyCharge(999, -1);
            }
            else if ("updateDashboard".equals(storedProcName)) {
                baseDataService.callUpdateDashboard(999);
            }
            else if ("updateWorkflowTables".equals(storedProcName)) {
                baseDataService.callUpdateWorkflowTables(999);
            }
        } catch (Exception ex) {
            LOG.error("Exception thrown calling stored proc '{}' in scheduler job:", storedProcName, ex);
        }
    }

    public String getStoredProcName() {
        return storedProcName;
    }

    public void setStoredProcName(String storedProcName) {
        this.storedProcName = storedProcName;
    }

    public void setBaseDataService(BaseDataService baseDataService) {
        this.baseDataService = baseDataService;
    }

}
