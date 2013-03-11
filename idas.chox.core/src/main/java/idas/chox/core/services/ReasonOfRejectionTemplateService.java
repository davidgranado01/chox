package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.ReasonOfRejectionTemplate;


public interface ReasonOfRejectionTemplateService {

    ReasonOfRejectionTemplate getReasonOfRejectionTemplate(int reasonOfRejectionId);

    List<ReasonOfRejectionTemplate> getAllReasonOfRejectionTemplate(String type, Boolean status, Boolean restricted);

    int getDefaultInvoiceLiabilityDisputeReasonId();

    List<ReasonOfRejectionTemplate> getReasonOfRejectionTemplates();
}
