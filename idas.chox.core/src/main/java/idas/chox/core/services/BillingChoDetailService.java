package idas.chox.core.services;

import java.util.List;

import idas.chox.core.model.BillingChoDetail;

/**
 *
 * @author abrar
 */
public interface BillingChoDetailService {
    BillingChoDetail getObject(int id);
    BillingChoDetail updateObject(BillingChoDetail object);
    void deleteObject(BillingChoDetail object);
    List<BillingChoDetail> getBillingChoDetails(int billingChoId);
    List sumPaymentAmount(int billingChoId);
}
