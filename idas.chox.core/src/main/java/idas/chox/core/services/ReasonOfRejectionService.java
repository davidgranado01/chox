package idas.chox.core.services;

import idas.chox.core.model.ReasonOfRejection;
import java.util.List;

public interface ReasonOfRejectionService {

    public ReasonOfRejection getReasonOfRejection(int reasonOfRejectionId);

    public List<ReasonOfRejection> getInsurerReasonsOfRejection(int insurerId, String type, Boolean status, Boolean restricted);

    public int getInvoiceLiabilityDisputeReasonId(int insurerId);

}
