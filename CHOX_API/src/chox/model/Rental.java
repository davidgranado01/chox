/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import com.filesystemsoftware.utils.StringEncoder;
import java.sql.Timestamp;
/**
 *
 * @author Carlson
 */
public class Rental {

    public static final String PENDING="Pending";
    public static final String IN_PROGRESS="InProgress";
    public static final String COMPLETE="Complete";
    public static final String CANCELLED="Cancelled";
    
    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long supplierID = -1;
    private String supplierReference;
    private String rentalStatus = "In Progress";
    private Timestamp firstContact = new Timestamp(System.currentTimeMillis());
    private Timestamp created = new Timestamp(System.currentTimeMillis());
    private Supplier supplier;

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
    
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getFirstContact() {
        return firstContact;
    }

    public void setFirstContact(Timestamp firstContact) {
        this.firstContact = firstContact;
    }

    public String getRentalStatus() {
        return rentalStatus;
    }

    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

    public long getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(long supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierReference() {
        return supplierReference;
    }

    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
}
