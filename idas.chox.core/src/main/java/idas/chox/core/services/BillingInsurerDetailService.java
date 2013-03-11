package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.BillingInsurerDetail;

/**
 *
 * @author abrar
 */
public interface BillingInsurerDetailService {
    BillingInsurerDetail getObject(int id);
    BillingInsurerDetail updateObject(BillingInsurerDetail object);
    void deleteObject(BillingInsurerDetail object);
    List<BillingInsurerDetail> getBillingInsurerDetails(int billingInsurerId);
    List sumPaymentAmount(int billingInsurerId);
}
