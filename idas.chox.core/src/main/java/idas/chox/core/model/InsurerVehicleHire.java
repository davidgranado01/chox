package idas.chox.core.model;

import java.io.Serializable;
import java.util.Date;

public class InsurerVehicleHire extends Entity implements Serializable {

    private Date rentalStart;
    private Date rentalEnd;
    private VehicleClass vehicleClass;


    /**
     * Method 'getRentalStart'
     *
     * @return java.util.Date
     */
    public java.util.Date getRentalStart() {
        return rentalStart;
    }

    /**
     * Method 'setRentalStart'
     *
     * @param rentalStart
     */
    public void setRentalStart(java.util.Date rentalStart) {
        this.rentalStart = rentalStart;
    }

    public Date getRentalEnd() {
        return rentalEnd;
    }

    public void setRentalEnd(Date rentalEnd) {
        this.rentalEnd = rentalEnd;
    }

    public VehicleClass getVehicleClass() {
        return vehicleClass;
    }

    public void setVehicleClass(VehicleClass vehicleClass) {
        this.vehicleClass = vehicleClass;
    }
    
    public enum DisplayName {
        // Insert in the order as they are displayed in the UI. 
        VEHICLE_CLASS           ("vehicleClass", "Replacement Vehicle Class"),
        RENTAL_START_DATE       ("rentalStart", "Hire Start (Date)");
        
        
        private final String ParameterName;
        private final String displayName;

        DisplayName(String name, String displayName) {
            this.ParameterName = name;
            this.displayName = displayName;
        }

        public String getParameterName() {
            return ParameterName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
