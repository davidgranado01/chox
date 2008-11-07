/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package chox.model;

import com.filesystemsoftware.utils.StringEncoder;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class RentalVehicle {

    private long ID = -1;
    private String uuid = StringEncoder.getRandomString(64);
    private long rentalID = -1;
    private String vehicleRegistration;
    private String vehicleManufacturer;
    private String vehicleModel;
    private long vehicleClassID = -1;
    private Timestamp rentalStart;
    private Timestamp rentalEnd;
    private BigDecimal days;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public BigDecimal getDays() {
        return days;
    }

    public void setDays(BigDecimal days) {
        this.days = days;
    }

    public Timestamp getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(Timestamp rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public long getRentalID() {
        return rentalID;
    }

    public void setRentalID(long rentalID) {
        this.rentalID = rentalID;
    }

    public Timestamp getRentalStart() {
        return rentalStart;
    }

    public void setRentalStart(Timestamp rentalStart) {
        this.rentalStart = rentalStart;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getVehicleClassID() {
        return vehicleClassID;
    }

    public void setVehicleClassID(long vehicleClassID) {
        this.vehicleClassID = vehicleClassID;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleRegistration() {
        return vehicleRegistration;
    }

    public void setVehicleRegistration(String vehicleRegistration) {
        this.vehicleRegistration = vehicleRegistration;
    }
    
    
}
