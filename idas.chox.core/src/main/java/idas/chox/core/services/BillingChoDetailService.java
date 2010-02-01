/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package idas.chox.core.services;

import idas.chox.core.model.BillingChoDetail;
import java.util.List;

/**
 *
 * @author abrar
 */
public interface BillingChoDetailService {
    public BillingChoDetail getObject(int id);
    public BillingChoDetail updateObject(BillingChoDetail object);
    public void deleteObject(BillingChoDetail object);
    public List<BillingChoDetail> getBillingChoDetails(int billingChoId);
}
