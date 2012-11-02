package idas.chox.core.services;

import idas.chox.core.model.Insurer;
import idas.chox.core.model.ReasonOfRejection;
import java.util.List;

public interface ReasonOfRejectionService {

    public ReasonOfRejection getReasonOfRejection(int reasonOfRejectionId);

    public List<ReasonOfRejection> getInsurerReasonsOfRejection(int insurerId, String type, String activeType ,  Boolean status, Boolean restricted);

    public int getInvoiceLiabilityDisputeReasonId(int insurerId);

    public void createDefaultRecord(Insurer insurer);
    
    public void saveReasonOfRejection(ReasonOfRejection reasonOfRejection);
    
    public void deleteReasonOfRejection(ReasonOfRejection reasonOfRejection);

}
