package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.ClaimType;
import idas.chox.core.model.Insurer;
import idas.chox.core.model.ReasonOfRejection;

public interface ReasonOfRejectionService {

    public ReasonOfRejection getReasonOfRejection(int reasonOfRejectionId);

    public List<ReasonOfRejection> getInsurerReasonsOfRejection(int insurerId, String type, ClaimType activeType ,  Boolean status, Boolean restricted);

    public int getInvoiceLiabilityDisputeReasonId(int insurerId);

    public void createDefaultRecord(Insurer insurer);
    
    public void saveReasonOfRejection(ReasonOfRejection reasonOfRejection);
    
    public void deleteReasonOfRejection(ReasonOfRejection reasonOfRejection);

}
