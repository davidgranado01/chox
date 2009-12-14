/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package idas.chox.core.services;

import idas.chox.core.model.WebUser;
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

    public List getAllSuppliers();

    public List getSuppliers(Integer insurerId);

    public List getInsurers();

    public List getInsurers(Integer choId);

    public List getAllInsurers();

    public List getInsurerChoBand(int insurerId);

    public List getReasonOfDelay();

    public List getWorkgroups(WebUser user, boolean isActiveOnly);

    public List getNotMyWorkgroups(WebUser user, boolean isActiveOnly);

    public List getWorkgroupsByInsurerId(int insurerId, boolean isActiveOnly);
}
