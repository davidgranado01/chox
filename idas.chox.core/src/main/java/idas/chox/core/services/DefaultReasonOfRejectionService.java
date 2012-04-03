package idas.chox.core.services;

import idas.chox.core.model.DefaultReasonOfRejection;

import java.util.List;

public interface DefaultReasonOfRejectionService {

    public DefaultReasonOfRejection getDefaultReasonOfRejection(int reasonOfRejectionId);

    public List<DefaultReasonOfRejection> getAllDefaultReasonOfRejection(String type, Boolean status, Boolean restricted);

    public int getDefaultInvoiceLiabilityDisputeReasonId();
}
