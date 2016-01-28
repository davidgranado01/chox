package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ReasonOfRejection;

public interface ReasonOfRejectionService {

    ReasonOfRejection getReason(int reasonOfRejectionId);

    List<ReasonOfRejection> getInsurerReasons(int insurerId, String type, ClaimType activeType ,  Boolean status, Boolean restricted);

    int getInvoiceLiabilityDisputeReasonId(int insurerId);

    void createDefaultRecord(Insurer insurer);
    
    void saveReason(ReasonOfRejection reasonOfRejection);
    
    void deleteReason(ReasonOfRejection reasonOfRejection);
    
    boolean isSubscriberClaimRejected (ReasonOfRejection reasonOfRejection);

}
