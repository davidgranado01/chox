package idas.chox.service.workflow.activities;

import idas.chox.core.model.Claim;
import idas.chox.core.model.ClaimStatus;
import idas.chox.core.model.Comment;
import idas.chox.core.model.ReasonOfRejection;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.util.StringHelper;

public class ClaimAwaitingCarHireInfo extends BaseActivity {

    @Override
    protected void beforeProcess(Claim claim) {
        
    }

    @Override
    protected void doProcess(Claim claim) {
        claim.setStatus(ClaimStatus.AWAITING_INVOICE_DATA);
    }

    @Override
    protected void setupExpectingStatuses(List<String> expectingStatuses) {
        expectingStatuses.add(ClaimStatus.AWAITING_CAR_HIRE_INFO);
    }
    
}
