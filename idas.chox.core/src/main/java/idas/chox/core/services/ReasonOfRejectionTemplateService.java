package idas.chox.core.services;

import idas.chox.core.model.ReasonOfRejectionTemplate;

import java.util.List;

public interface ReasonOfRejectionTemplateService {

    public ReasonOfRejectionTemplate getReasonOfRejectionTemplate(int reasonOfRejectionId);

    public List<ReasonOfRejectionTemplate> getAllReasonOfRejectionTemplate(String type, Boolean status, Boolean restricted);

    public int getDefaultInvoiceLiabilityDisputeReasonId();
}
