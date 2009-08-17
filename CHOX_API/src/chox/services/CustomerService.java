/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.Customer;
import chox.xmlValidation.model.ClaimResult;

public interface CustomerService {
    public void saveObjectForXMLUploader(final ClaimResult claimResult);
    public Customer getObject(int id);
    public void updateObject(Customer customer);

}
