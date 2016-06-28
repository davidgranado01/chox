package idas.chox.service.workflow.activities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;

import idas.chox.core.model.Claim;
import idas.chox.core.model.KeoghsRequest;
import idas.chox.keoghs.Keoghs;


/**
 *
 * @author John
 */
public class RunFraudCheck extends BaseActivity {
    private static final Logger LOG = LoggerFactory.getLogger(RunFraudCheck.class);
    private String checkType = "Manual";
    private Keoghs keoghs;
    
    public String getCheckType() {
        return checkType;
    }

    public void setKeoghs(Keoghs keoghs) {
LOG.info("keoghs set");
        this.keoghs = keoghs;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    @Override
    @Secured ({"ROLE_INS"})
    protected void doProcess(Claim claim) throws Exception {
        LOG.info("Run Keoghs Fraud Check on claim '{}' to Keoghs ({})...", claim.getChoReference(), keoghs);
//        boolean result = false;
//        result = keoghs.queueAndSubmitDebug(claim, checkType);
//        LOG.info("Claim '{}' submitted to Keoghs with result: {}", claim.getChoReference(), result);
        KeoghsRequest request  = keoghs.queue(claim, checkType);
        LOG.info("Claim '{}' queued to Keoghs with request id={}", claim.getChoReference(), request.getId());
        
    }
}
