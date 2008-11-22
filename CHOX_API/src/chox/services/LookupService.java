/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.services;

import java.util.List;

/**
 *
 * @author Emmanuel
 */
public interface LookupService {
    
    public List getStatuses();
    public List getLineOfBusinesses();   
    public List getSuppliers();
    public List getInsurers();

}
