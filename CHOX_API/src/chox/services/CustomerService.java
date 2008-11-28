/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import chox.model.XMLParseResult;
import chox.model.Customer;

public interface CustomerService {
    XMLParseResult saveCustomerForXMLUploader(XMLParseResult xmlParseResult);
    public Customer getObject(int id);
    public void updateObject(Customer customer);

}
