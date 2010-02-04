/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.services;

import idas.chox.core.model.BillingInsurerDetail;
import java.util.List;

/**
 *
 * @author abrar
 */
public interface BillingInsurerDetailService {
    public BillingInsurerDetail getObject(int id);
    public BillingInsurerDetail updateObject(BillingInsurerDetail object);
    public void deleteObject(BillingInsurerDetail object);
    public List<BillingInsurerDetail> getBillingInsurerDetails(int billingInsurerId);
    public List sumPaymentAmount(int billingInsurerId);
}
