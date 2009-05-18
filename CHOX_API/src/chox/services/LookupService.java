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
    public List getVehicleClasses();
    public List getClaimRejectionReason();
    public List getInvoiceRejectionReason();
    public List getNonProvisionReason();
    //public List getSelectedUserAvailableRole(int orgTypeId, int webUserId);
    
    public List getLineOfBusinesses();
    public List getAllLineOfBusinesses();
    
    public List getSuppliers();
    public List getSuppliers(Integer insurerId);
    public List getAllSuppliers();
    
    public List getInsurers();
    public List getInsurers(Integer choId);
    public List getAllInsurers();
    
    
    
    
}